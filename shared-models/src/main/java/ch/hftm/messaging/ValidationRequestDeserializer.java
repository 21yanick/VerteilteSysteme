package ch.hftm.messaging;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

/**
 * Deserializer für ValidationRequest-Objekte von Kafka
 */
public class ValidationRequestDeserializer extends ObjectMapperDeserializer<ValidationRequest> {
    public ValidationRequestDeserializer() {
        super(ValidationRequest.class);
    }
}