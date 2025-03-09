package ch.hftm.messaging;

/**
 * Record zum Empfangen von Validierungsantworten für Blog-Inhalte.
 * Dieser Record wird verwendet, um das Validierungsergebnis für einen Blog zu empfangen.
 * Enthält zusätzlich einen optionalen Ablehnungsgrund, der bei Ablehnung gefüllt ist.
 */
public record ValidationResponse(long id, boolean valid, String rejectionReason) {
    /**
     * Rückwärtskompatibilitätskonstruktor ohne Ablehnungsgrund
     */
    public ValidationResponse(long id, boolean valid) {
        this(id, valid, null);
    }
}