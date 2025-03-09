package ch.hftm.ai.prompt;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Manager für Prompt-Templates, der die JSON-Dateien im /prompts Verzeichnis liest
 * und die Templates für verschiedene AI-Services bereitstellt.
 */
@ApplicationScoped
public class PromptTemplateManager {

    private final Map<String, PromptTemplateConfig> templateConfigs = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PromptTemplateManager() {
        loadTemplateConfig("blog-generation");
    }

    /**
     * Lädt eine Template-Konfiguration aus einer JSON-Datei im /prompts Verzeichnis.
     * 
     * @param templateName Name des Templates (entspricht dem Dateinamen ohne .json)
     */
    private void loadTemplateConfig(String templateName) {
        String resourcePath = "/prompts/" + templateName + ".json";
        
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                Log.error("Prompt-Template nicht gefunden: " + resourcePath);
                return;
            }
            
            JsonNode rootNode = objectMapper.readTree(is);
            PromptTemplateConfig config = new PromptTemplateConfig();
            
            // Templates laden
            JsonNode templatesNode = rootNode.get("templates");
            if (templatesNode != null && templatesNode.isObject()) {
                templatesNode.fields().forEachRemaining(entry -> {
                    String type = entry.getKey();
                    JsonNode templateNode = entry.getValue();
                    PromptTemplate template = new PromptTemplate(
                        templateNode.get("system").asText(),
                        templateNode.get("user").asText()
                    );
                    config.addTemplate(type, template);
                });
            }
            
            // Parameter-Optionen laden
            JsonNode paramsNode = rootNode.get("parameters");
            if (paramsNode != null && paramsNode.isObject()) {
                paramsNode.fields().forEachRemaining(entry -> {
                    String paramName = entry.getKey();
                    List<String> options = objectMapper.convertValue(
                        entry.getValue(), 
                        objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)
                    );
                    config.addParameterOptions(paramName, options);
                });
            }
            
            templateConfigs.put(templateName, config);
            Log.info("Prompt-Template geladen: " + templateName);
            
        } catch (IOException e) {
            Log.error("Fehler beim Laden des Prompt-Templates: " + templateName, e);
        }
    }

    /**
     * Liefert ein template für einen bestimmten Anwendungsfall und Typ.
     * 
     * @param category Kategorie des Templates (z.B. "blog-generation")
     * @param type Typ des Templates (z.B. "standard", "technical")
     * @return Das angeforderte Template oder null, wenn nicht gefunden
     */
    public PromptTemplate getTemplate(String category, String type) {
        PromptTemplateConfig config = templateConfigs.get(category);
        if (config == null) {
            return null;
        }
        return config.getTemplate(type);
    }

    /**
     * Liefert die verfügbaren Parameterwerte für einen bestimmten Parameter.
     * 
     * @param category Kategorie des Templates
     * @param paramName Name des Parameters
     * @return Liste der verfügbaren Werte oder null, wenn nicht gefunden
     */
    public List<String> getParameterOptions(String category, String paramName) {
        PromptTemplateConfig config = templateConfigs.get(category);
        if (config == null) {
            return null;
        }
        return config.getParameterOptions(paramName);
    }

    /**
     * Füllt ein Template mit den angegebenen Parametern.
     * 
     * @param template Das zu füllende Template
     * @param params Die einzusetzenden Parameter
     * @return Das gefüllte Template
     */
    public PromptTemplate fillTemplate(PromptTemplate template, Map<String, String> params) {
        String systemMessage = template.systemMessage();
        String userMessage = template.userMessage();
        
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            userMessage = userMessage.replace(placeholder, entry.getValue());
            systemMessage = systemMessage.replace(placeholder, entry.getValue());
        }
        
        return new PromptTemplate(systemMessage, userMessage);
    }
}