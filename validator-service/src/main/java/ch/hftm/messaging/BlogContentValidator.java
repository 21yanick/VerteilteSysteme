package ch.hftm.messaging;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.annotations.Blocking;

/**
 * Dienst zur Validierung von Blog-Inhalten über Kafka-Messaging
 * Dieser Dienst empfängt ValidationRequest-Nachrichten, verarbeitet sie
 * und sendet ValidationResponse-Nachrichten zurück
 */
@ApplicationScoped
public class BlogContentValidator {

    /**
     * Verarbeitet eingehende Validierungsanfragen und transformiert sie zu Antworten
     * Diese Methode wendet Validierungsregeln auf den Blog-Inhalt an und gibt eine Antwort zurück
     *
     * @param request Die von Kafka empfangene Validierungsanfrage
     * @return Die zu sendende Validierungsantwort
     */
    @Incoming("validation-request-in")
    @Outgoing("validation-response-out")
    @Blocking
    public ValidationResponse processValidationRequest(ValidationRequest request) {
        Log.info("Verarbeite Validierungsanfrage für Blog-ID: " + request.id());
        
        // Validierungsregeln auf den Blog-Inhalt anwenden
        boolean isValid = validateContent(request.text());
        
        // Antwort erstellen
        ValidationResponse response = new ValidationResponse(request.id(), isValid);
        
        Log.info("Sende Validierungsantwort für Blog-ID: " + request.id() + ", gültig: " + isValid);
        
        return response;
    }
    
    /**
     * Validiert den Blog-Inhalt basierend auf definierten Regeln
     * 
     * @param content Der zu validierende Blog-Inhalt
     * @return true wenn der Inhalt gültig ist, sonst false
     */
    private boolean validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }
        
        // Prüfe auf Mindestlänge des Inhalts (mindestens 10 Zeichen)
        if (content.trim().length() < 10) {
            return false;
        }
        
        // Prüfe auf verbotenen Text
        if (content.toLowerCase().contains("hftm sucks")) {
            return false;
        }
        
        // Hier könnten weitere Validierungsregeln hinzugefügt werden
        
        return true;
    }
}