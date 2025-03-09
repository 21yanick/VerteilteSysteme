package ch.hftm.ai.boundary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.hftm.ai.prompt.PromptTemplate;
import ch.hftm.ai.prompt.PromptTemplateManager;
import ch.hftm.ai.service.BlogGenerationService;
import ch.hftm.dto.GeneratedBlogDTO;
import ch.hftm.entity.Blog;
import ch.hftm.entity.BlogStatus;
import ch.hftm.messaging.BlogValidationService;
import ch.hftm.repository.BlogRepository;
import io.quarkus.logging.Log;
import io.quarkus.security.Authenticated;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * REST-Endpunkte für die KI-basierte Blog-Generierung.
 */
@Path("/blogs/ai")
@ApplicationScoped
@Tag(name = "Blog-Generation", description = "KI-gestützte Blog-Generierung")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class BlogGenerationResource {
    
    /**
     * Sendet eine Validierungsanfrage für einen Blog
     * Diese Methode ist separat, um außerhalb der Transaktion ausgeführt zu werden
     */
    private void sendBlogForValidation(Blog blog) {
        try {
            Log.info("Sende KI-generierten Blog mit ID " + blog.getId() + " zur Validierung");
            blogValidationService.sendBlogForValidation(blog);
        } catch (Exception e) {
            Log.error("Fehler beim Senden der Validierungsanfrage für KI-Blog: " + e.getMessage(), e);
        }
    }

    @Inject
    BlogGenerationService blogGenerationService;
    
    @Inject
    PromptTemplateManager promptTemplateManager;
    
    @Inject
    BlogRepository blogRepository;
    
    @Inject
    BlogValidationService blogValidationService;

    /**
     * Test-Endpunkt zum Prüfen der KI-Integration.
     * Verwendet eine optimierte Methode für kürzere Antworten, um Timeout-Probleme zu vermeiden.
     */
    @GET
    @Path("/test")
    @Operation(summary = "Test der KI-Integration", description = "Generiert einen einfachen Test-Blog")
    public Response testAiIntegration(@QueryParam("title") String title, @QueryParam("topic") String topic) {
        try {
            if (title == null || title.isBlank()) {
                title = "Test-Blog";
            }
            if (topic == null || topic.isBlank()) {
                topic = "Künstliche Intelligenz";
            }
            
            // Kürzere Methode verwenden, um Timeout zu vermeiden
            String content = blogGenerationService.generateTestBlog(title, topic);
            return Response.ok(Map.of(
                "title", title,
                "topic", topic,
                "content", content
            )).build();
        } catch (Exception e) {
            Log.error("Fehler bei der KI-Integration", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("error", "KI-Integration fehlgeschlagen: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Liefert die verfügbaren Template-Typen und Parameter-Optionen.
     */
    @GET
    @Path("/templates")
    @Operation(summary = "Template-Optionen", description = "Liefert die verfügbaren Template-Typen und Parameter-Optionen")
    public Response getTemplateOptions() {
        try {
            Map<String, Object> options = new HashMap<>();
            
            // Template-Typen (nur die Namen)
            List<String> templateTypes = promptTemplateManager.getTemplate("blog-generation", "standard") == null ? 
                    List.of() : 
                    List.of("standard", "technical", "creative");
            
            // Parameter-Optionen
            Map<String, List<String>> paramOptions = new HashMap<>();
            paramOptions.put("audience", promptTemplateManager.getParameterOptions("blog-generation", "audience"));
            paramOptions.put("tone", promptTemplateManager.getParameterOptions("blog-generation", "tone"));
            paramOptions.put("length", promptTemplateManager.getParameterOptions("blog-generation", "length"));
            
            options.put("templateTypes", templateTypes);
            options.put("parameters", paramOptions);
            
            return Response.ok(options).build();
        } catch (Exception e) {
            Log.error("Fehler beim Abrufen der Template-Optionen", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("error", "Fehler beim Abrufen der Template-Optionen: " + e.getMessage()))
                .build();
        }
    }
    
    /**
     * Generiert einen Blog-Beitrag mit den angegebenen Parametern.
     */
    @POST
    @Authenticated
    @Operation(summary = "Blog generieren", description = "Generiert einen Blog-Beitrag mit den angegebenen Parametern")
    public Response generateBlog(GeneratedBlogDTO blogRequest) {
        try {
            // Parameter validieren
            if (blogRequest.getTitle() == null || blogRequest.getTitle().isBlank()) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Titel ist erforderlich"))
                    .build();
            }
            
            // Template auswählen
            String templateType = blogRequest.getTemplateType() != null ? 
                    blogRequest.getTemplateType() : "standard";
            PromptTemplate template = promptTemplateManager.getTemplate("blog-generation", templateType);
            
            if (template == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Ungültiger Template-Typ: " + templateType))
                    .build();
            }
            
            // Parameter für das Template zusammenstellen
            Map<String, String> params = new HashMap<>();
            params.put("title", blogRequest.getTitle());
            params.put("audience", blogRequest.getAudience() != null ? 
                    blogRequest.getAudience() : "allgemeine");
            params.put("tone", blogRequest.getTone() != null ? 
                    blogRequest.getTone() : "informativ");
            params.put("length", blogRequest.getLength() != null ? 
                    blogRequest.getLength() : "mittel (ca. 500-800 Wörter)");
            
            // Template mit Parametern füllen
            PromptTemplate filledTemplate = promptTemplateManager.fillTemplate(template, params);
            
            // Blog generieren
            String content = blogGenerationService.generateBlogWithDynamicPrompt(
                    filledTemplate.systemMessage(), 
                    filledTemplate.userMessage());
            
            // Blog-Entity erstellen und speichern
            Blog blog = new Blog();
            blog.setTitle(blogRequest.getTitle());
            blog.setContent(content);
            blog.setStatus(BlogStatus.PENDING); // Setzt den Status auf PENDING für die Validierung
            
            // Blog in der Datenbank speichern
            blogRepository.persist(blog);
            
            // Blog zur Validierung senden
            sendBlogForValidation(blog);
            
            // Erfolgsantwort mit generiertem Blog
            GeneratedBlogDTO response = new GeneratedBlogDTO();
            response.setId(blog.getId());
            response.setTitle(blog.getTitle());
            response.setContent(content);
            response.setStatus(blog.getStatus().toString());
            response.setTemplateType(templateType);
            response.setAudience(params.get("audience"));
            response.setTone(params.get("tone"));
            response.setLength(params.get("length"));
            
            return Response.status(Response.Status.CREATED).entity(response).build();
            
        } catch (Exception e) {
            Log.error("Fehler bei der Blog-Generierung", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of("error", "Blog-Generierung fehlgeschlagen: " + e.getMessage()))
                .build();
        }
    }
}