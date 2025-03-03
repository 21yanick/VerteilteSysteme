package ch.hftm.messaging;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ValidationDeserializersTest {
    
    @Test
    public void testValidationMessages() {
        // Test ValidationRequest record
        ValidationRequest request = new ValidationRequest(123L, "This is test content");
        assertEquals(123L, request.id());
        assertEquals("This is test content", request.text());
        
        // Test ValidationResponse record
        ValidationResponse responseTrue = new ValidationResponse(123L, true);
        ValidationResponse responseFalse = new ValidationResponse(456L, false);
        
        assertEquals(123L, responseTrue.id());
        assertTrue(responseTrue.valid());
        
        assertEquals(456L, responseFalse.id());
        assertFalse(responseFalse.valid());
    }
    
    @Test
    public void testValidationRequestJsonConversion() throws Exception {
        // Since we can't directly access the ObjectMapper in the deserializer,
        // we'll test basic JSON conversion instead
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"id\": 123, \"text\": \"This is test content\"}";
        
        ValidationRequest request = mapper.readValue(json, ValidationRequest.class);
        
        assertNotNull(request);
        assertEquals(123L, request.id());
        assertEquals("This is test content", request.text());
    }
    
    @Test
    public void testValidationResponseJsonConversion() throws Exception {
        // Create a mapper for testing JSON conversion
        ObjectMapper mapper = new ObjectMapper();
        
        // Test true value
        String jsonTrue = "{\"id\": 123, \"valid\": true}";
        ValidationResponse responseTrue = mapper.readValue(jsonTrue, ValidationResponse.class);
        
        assertNotNull(responseTrue);
        assertEquals(123L, responseTrue.id());
        assertTrue(responseTrue.valid());
        
        // Test false value
        String jsonFalse = "{\"id\": 456, \"valid\": false}";
        ValidationResponse responseFalse = mapper.readValue(jsonFalse, ValidationResponse.class);
        
        assertNotNull(responseFalse);
        assertEquals(456L, responseFalse.id());
        assertFalse(responseFalse.valid());
    }
}