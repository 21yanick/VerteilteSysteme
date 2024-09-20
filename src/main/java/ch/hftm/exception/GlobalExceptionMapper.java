package ch.hftm.exception;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import io.quarkus.logging.Log;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        // Exception loggen
        Log.error("Unerwarteter Fehler: ", exception);

        // Überprüfen, ob die Exception eine NotFoundException ist
        if (exception instanceof NotFoundException) {
            return handleNotFoundException((NotFoundException) exception);
        }

        // Erstelle eine generische Fehlerantwort für alle anderen Exceptions
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("Interner Serverfehler");
        errorResponse.setDetails(exception.getMessage());

        // Gib eine generische Fehlerantwort zurück
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorResponse)
                .build();
    }

    // Spezifische Behandlung für NotFoundException
    private Response handleNotFoundException(NotFoundException exception) {
        Log.warn("Ressource nicht gefunden: " + exception.getMessage());

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(exception.getMessage());
        errorResponse.setDetails("Die angefragte Ressource konnte nicht gefunden werden");

        return Response.status(Response.Status.NOT_FOUND)
                .entity(errorResponse)
                .build();
    }
}