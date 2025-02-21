package ch.hftm.messaging;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import io.smallrye.mutiny.Multi;

@ApplicationScoped
public class MessagingTesting {

    @Outgoing("source-outgoing")
    public Multi<String> produceMessages() {
        return Multi.createFrom().items("hallo", "böse", "quarkus", "fans");
    }

    @Incoming("source")
    @Outgoing("processed-a")
    public String toUpperCase(String payload) {
        return payload.toUpperCase();
    }

    @Incoming("processed-a")
    @Outgoing("processed-b")
    public String replaceBad(String word) {
        return word.equals("BÖSE") ? "LIEBE" : word;
    }

    @Incoming("processed-b")
    @Outgoing("processed-c")
    public Multi<String> filterShort(Multi<String> stream) {
        return stream.filter(word -> word.length() >= 6);
    }

    @Incoming("processed-c")
    public void sink(String word) {
        System.out.println(">> " + word);
    }
}
