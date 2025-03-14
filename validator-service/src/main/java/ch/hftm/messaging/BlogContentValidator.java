package ch.hftm.messaging;

import ch.hftm.messaging.service.ContentModerationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.annotations.Blocking;

/**
 * Dienst zur Validierung von Blog-Inhalten über Kafka-Messaging
 * Dieser Dienst empfängt ValidationRequest-Nachrichten, verarbeitet sie mit KI
 * und sendet ValidationResponse-Nachrichten zurück
 */
@ApplicationScoped
public class BlogContentValidator {

    @Inject
    ContentModerationService moderationService;

    /**
     * Verarbeitet eingehende Validierungsanfragen und transformiert sie zu Antworten
     * Diese Methode wendet Validierungsregeln und KI-Moderation auf den Blog-Inhalt an
     *
     * @param request Die von Kafka empfangene Validierungsanfrage
     * @return Die zu sendende Validierungsantwort
     */
    @Incoming("validation-request-in")
    @Outgoing("validation-response-out")
    @Blocking
    @Transactional
    public ValidationResponse processValidationRequest(ValidationRequest request) {
        Log.info("Verarbeite Validierungsanfrage für Blog-ID: " + request.id());
        
        // Basis-Validierung durchführen
        if (request.text() == null || request.text().trim().isEmpty()) {
            return new ValidationResponse(request.id(), false, "Inhalt ist leer");
        }
        
        // Prüfe auf Mindestlänge des Inhalts (mindestens 10 Zeichen)
        if (request.text().trim().length() < 10) {
            return new ValidationResponse(request.id(), false, "Text zu kurz (min. 10 Zeichen)");
        }
        
        try {
            // KI-basierte Moderation durchführen
            String moderationResult = moderationService.moderateContent(request.text());
            Log.info("KI-Moderationsergebnis für Blog-ID " + request.id() + ": " + moderationResult);
            
            if (moderationResult.startsWith("APPROVED")) {
                return new ValidationResponse(request.id(), true, null);
            } else {
                String reason = moderationResult.startsWith("REJECTED:") ? 
                    moderationResult.substring(9).trim() : "Unangemessener Inhalt";
                return new ValidationResponse(request.id(), false, reason);
            }
        } catch (Exception e) {
            Log.error("Fehler bei KI-Moderation für Blog-ID " + request.id(), e);
            
            // Fallback zur einfachen Validierung bei KI-Fehler
            boolean isValid = validateContentFallback(request.text());
            String reason = isValid ? null : "Validierungsfehler (Fallback-Prüfung)";
            
            return new ValidationResponse(request.id(), isValid, reason);
        }
    }
    
    /**
     * Fallback-Validierung für den Fall, dass die KI-Moderation fehlschlägt
     * 
     * @param content Der zu validierende Blog-Inhalt
     * @return true wenn der Inhalt gültig ist, sonst false
     */
    private boolean validateContentFallback(String content) {
        // Prüfe auf verbotenen Text
        if (content.toLowerCase().contains("hftm sucks")) {
            return false;
        }
        
        return true;
    }
}