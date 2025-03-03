package ch.hftm.messaging;

/**
 * Record zum Empfangen von Validierungsantworten für Blog-Inhalte.
 * Dieser Record wird verwendet, um das Validierungsergebnis für einen Blog zu empfangen.
 */
public record ValidationResponse(long id, boolean valid) {}