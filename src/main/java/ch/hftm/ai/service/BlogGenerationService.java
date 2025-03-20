package ch.hftm.ai.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

/**
 * KI-Service für die Generierung von Blog-Beiträgen.
 * Verwendet die LangChain4J-Integration mit OpenAI.
 */
@RegisterAiService
public interface BlogGenerationService {
    
    /**
     * Generiert einen Blog-Beitrag mit dynamischen Prompts.
     * 
     * @param systemPrompt Der System-Prompt für das LLM
     * @param userPrompt Der User-Prompt für das LLM
     * @return Der generierte Blog-Inhalt
     */
    @SystemMessage("{systemPrompt}")
    @UserMessage("{userPrompt}")
    String generateBlogWithDynamicPrompt(String systemPrompt, String userPrompt);
    
    /**
     * Generiert einen Blog-Beitrag mit Standardwerten.
     * 
     * @param title Der Titel des Blogs
     * @param topic Das Thema des Blogs
     * @return Der generierte Blog-Inhalt
     */
    @SystemMessage("Du bist ein professioneller Blog-Autor, der informative und ansprechende Artikel verfasst.")
    @UserMessage("Schreibe einen Blog-Beitrag zum Thema \"{topic}\" mit dem Titel \"{title}\". " +
                "Der Beitrag sollte etwa 500 Wörter umfassen und eine klare Einleitung, Hauptteil und Fazit haben.")
    String generateSimpleBlog(String title, String topic);
    
    /**
     * Generiert einen minimalen Test-Blog-Beitrag zur schnellen Überprüfung der API-Verbindung.
     * Diese Methode erzeugt einen extrem kurzen Text, um Timeout-Probleme zu vermeiden.
     * 
     * @param title Der Titel des Blogs
     * @param topic Das Thema des Blogs
     * @return Der generierte Blog-Inhalt
     */
    @SystemMessage("Du bist ein präziser Autor, der extrem kurze Texte schreibt.")
    @UserMessage("Schreibe einen sehr kurzen Blog-Beitrag (maximal 50 Wörter) zum Thema \"{topic}\" mit dem Titel \"{title}\". Verwende keine Einleitung oder Zusammenfassung, sondern liefere nur einen einzigen Absatz.")
    String generateTestBlog(String title, String topic);
}