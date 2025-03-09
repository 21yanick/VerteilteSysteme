package ch.hftm.ai.prompt;

/**
 * Repräsentiert ein Prompt-Template mit System- und User-Message für LLM-Anfragen.
 * Die Nachrichten können Platzhalter der Form {parameter} enthalten, die bei der
 * Verwendung durch konkrete Werte ersetzt werden.
 */
public record PromptTemplate(String systemMessage, String userMessage) {
    
    /**
     * Erstellt ein leeres Prompt-Template.
     * 
     * @return Ein leeres Template
     */
    public static PromptTemplate empty() {
        return new PromptTemplate("", "");
    }
}