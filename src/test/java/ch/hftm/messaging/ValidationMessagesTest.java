package ch.hftm.messaging;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ValidationMessagesTest {

    @Test
    public void testValidationRequestRecord() {
        // Arrange & Act
        ValidationRequest request = new ValidationRequest(123L, "Test content");
        
        // Assert
        assertEquals(123L, request.id());
        assertEquals("Test content", request.text());
        
        // Test equals and hashCode
        ValidationRequest sameRequest = new ValidationRequest(123L, "Test content");
        ValidationRequest differentIdRequest = new ValidationRequest(456L, "Test content");
        ValidationRequest differentTextRequest = new ValidationRequest(123L, "Different content");
        
        assertEquals(request, sameRequest);
        assertEquals(request.hashCode(), sameRequest.hashCode());
        
        assertNotEquals(request, differentIdRequest);
        assertNotEquals(request, differentTextRequest);
    }
    
    @Test
    public void testValidationResponseRecord() {
        // Arrange & Act
        ValidationResponse validResponse = new ValidationResponse(123L, true);
        ValidationResponse invalidResponse = new ValidationResponse(456L, false);
        
        // Assert
        assertEquals(123L, validResponse.id());
        assertTrue(validResponse.valid());
        
        assertEquals(456L, invalidResponse.id());
        assertFalse(invalidResponse.valid());
        
        // Test equals and hashCode
        ValidationResponse sameValidResponse = new ValidationResponse(123L, true);
        
        assertEquals(validResponse, sameValidResponse);
        assertEquals(validResponse.hashCode(), sameValidResponse.hashCode());
        
        assertNotEquals(validResponse, invalidResponse);
    }
    
    @Test
    public void testToString() {
        // Arrange & Act
        ValidationRequest request = new ValidationRequest(123L, "Test content");
        ValidationResponse response = new ValidationResponse(123L, true);
        
        // Assert
        String requestString = request.toString();
        String responseString = response.toString();
        
        assertTrue(requestString.contains("ValidationRequest"));
        assertTrue(requestString.contains("123"));
        assertTrue(requestString.contains("Test content"));
        
        assertTrue(responseString.contains("ValidationResponse"));
        assertTrue(responseString.contains("123"));
        assertTrue(responseString.contains("true"));
    }
}