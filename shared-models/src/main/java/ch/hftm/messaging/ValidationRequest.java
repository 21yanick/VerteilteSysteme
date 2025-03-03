package ch.hftm.messaging;

/**
 * Record zum Senden von Validierungsanfragen für Blog-Inhalte.
 * Dieser Record wird verwendet, um die ID und den Textinhalt eines Blogs an den Validierungsdienst zu senden.
 */
public record ValidationRequest(long id, String text) {}