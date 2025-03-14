package ch.hftm.messaging.service;

import io.quarkiverse.langchain4j.RegisterAiService;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * KI-Service zur Moderation von Blog-Inhalten.
 * Prüft Inhalte auf unangemessene Sprache, Qualität und andere Kriterien.
 */
@RegisterAiService
@ApplicationScoped
public interface ContentModerationService {
    
    /**
     * Moderiert einen Blog-Inhalt mit KI.
     * 
     * @param content Der zu moderierende Blog-Inhalt
     * @return Moderationsergebnis: "APPROVED" oder "REJECTED: Grund"
     */
    @SystemMessage("Du bist ein Blog-Moderator. Prüfe den Text auf unangemessene oder irreführende Inhalte.")
    @UserMessage("Bewerte den folgenden Blog-Inhalt. Enthält er unangemessene Inhalte (Beleidigungen, Hassrede, etc.) oder ist qualitativ unzureichend? Antworte mit 'APPROVED' oder 'REJECTED: Grund'.\n\n{content}")
    String moderateContent(String content);
}