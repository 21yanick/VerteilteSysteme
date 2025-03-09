package ch.hftm.ai.prompt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Konfiguration für eine Gruppe von Prompt-Templates zu einem bestimmten Anwendungsfall.
 * Enthält verschiedene Template-Typen und die verfügbaren Parameter-Optionen.
 */
public class PromptTemplateConfig {
    
    private final Map<String, PromptTemplate> templates = new HashMap<>();
    private final Map<String, List<String>> parameterOptions = new HashMap<>();
    
    /**
     * Fügt ein Template hinzu.
     * 
     * @param type Typ des Templates (z.B. "standard", "technical")
     * @param template Das Template
     */
    public void addTemplate(String type, PromptTemplate template) {
        templates.put(type, template);
    }
    
    /**
     * Fügt Optionen für einen Parameter hinzu.
     * 
     * @param paramName Name des Parameters
     * @param options Liste der möglichen Werte
     */
    public void addParameterOptions(String paramName, List<String> options) {
        parameterOptions.put(paramName, options);
    }
    
    /**
     * Liefert ein Template für einen bestimmten Typ.
     * 
     * @param type Typ des Templates
     * @return Das angeforderte Template oder null, wenn nicht gefunden
     */
    public PromptTemplate getTemplate(String type) {
        return templates.get(type);
    }
    
    /**
     * Liefert die verfügbaren Optionen für einen Parameter.
     * 
     * @param paramName Name des Parameters
     * @return Liste der verfügbaren Optionen oder null, wenn nicht gefunden
     */
    public List<String> getParameterOptions(String paramName) {
        return parameterOptions.get(paramName);
    }
    
    /**
     * Liefert alle verfügbaren Template-Typen.
     * 
     * @return Map mit den Template-Typen
     */
    public Map<String, PromptTemplate> getAllTemplates() {
        return new HashMap<>(templates);
    }
    
    /**
     * Liefert alle verfügbaren Parameter-Optionen.
     * 
     * @return Map mit den Parameter-Optionen
     */
    public Map<String, List<String>> getAllParameterOptions() {
        return new HashMap<>(parameterOptions);
    }
}