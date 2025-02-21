package ch.hftm.messaging;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@Path("/messages")
public class MessageResource {

    @Inject
    @Channel("source-outgoing") 
    Emitter<String> emitter;

    @POST
    @Path("/send/{word}")
    public String sendMessage(@PathParam("word") String word) {
        emitter.send(word);
        return "Sent: " + word;
    }
}
