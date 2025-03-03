package ch.hftm.messaging;

import ch.hftm.entity.Blog;
import ch.hftm.entity.BlogStatus;
import ch.hftm.repository.BlogRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import io.quarkus.logging.Log;

/**
 * Dienst zur Handhabung der Blog-Validierung über Kafka-Messaging
 */
@ApplicationScoped
public class BlogValidationService {

    @Inject
    BlogRepository blogRepository;

    @Inject
    @Channel("validation-request-out")
    Emitter<ValidationRequest> validationRequestEmitter;

    /**
     * Sendet einen Blog zur Validierung über Kafka
     * Diese Methode sollte nach dem Persistieren des Blogs aufgerufen werden,
     * aber außerhalb der Transaktion
     *
     * @param blog Der zu validierende Blog
     */
    public void sendBlogForValidation(Blog blog) {
        Log.info("Sende Blog mit ID " + blog.getId() + " zur Validierung");
        ValidationRequest request = new ValidationRequest(blog.getId(), blog.getContent());
        validationRequestEmitter.send(request);
    }

    /**
     * Verarbeitet Validierungsantworten vom Validierungsdienst
     * Diese Methode aktualisiert den Blog-Status basierend auf dem Validierungsergebnis
     *
     * @param response Die von Kafka empfangene Validierungsantwort
     */
    @Incoming("validation-response-in")
    @Transactional
    public void processValidationResponse(ValidationResponse response) {
        Log.info("Validierungsantwort für Blog-ID: " + response.id() + " empfangen, gültig: " + response.valid());
        
        Blog blog = blogRepository.findById(response.id());
        if (blog != null) {
            BlogStatus newStatus = response.valid() ? BlogStatus.APPROVED : BlogStatus.REJECTED;
            blog.setStatus(newStatus);
            
            Log.info("Blog-ID: " + blog.getId() + " Status aktualisiert auf: " + newStatus);
        } else {
            Log.warn("Blog mit ID: " + response.id() + " wurde für Validierungsaktualisierung nicht gefunden");
        }
    }
}