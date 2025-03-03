package ch.hftm.messaging;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

/**
 * Deserializer für ValidationResponse-Objekte, die von Kafka stammen
 */
public class ValidationResponseDeserializer extends ObjectMapperDeserializer<ValidationResponse> {
    public ValidationResponseDeserializer() {
        super(ValidationResponse.class);
    }
}