package ch.hftm.messaging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.hftm.entity.Blog;
import ch.hftm.entity.BlogStatus;
import ch.hftm.repository.BlogRepository;

public class BlogValidationServiceTest {

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private Emitter<ValidationRequest> validationRequestEmitter;

    @InjectMocks
    private BlogValidationService blogValidationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendBlogForValidation() {
        // Arrange
        Blog blog = new Blog("Test Blog", "This is test content");
        blog.setId(1L);

        // Act
        blogValidationService.sendBlogForValidation(blog);

        // Assert
        ArgumentCaptor<ValidationRequest> requestCaptor = ArgumentCaptor.forClass(ValidationRequest.class);
        verify(validationRequestEmitter, times(1)).send(requestCaptor.capture());
        
        ValidationRequest capturedRequest = requestCaptor.getValue();
        assertEquals(1L, capturedRequest.id());
        assertEquals("This is test content", capturedRequest.text());
    }

    @Test
    public void testProcessValidationResponse_ValidBlog() {
        // Arrange
        ValidationResponse response = new ValidationResponse(1L, true);
        Blog blog = new Blog("Test Blog", "This is test content");
        blog.setId(1L);
        blog.setStatus(BlogStatus.PENDING);
        
        when(blogRepository.findById(1L)).thenReturn(blog);

        // Act
        blogValidationService.processValidationResponse(response);

        // Assert
        assertEquals(BlogStatus.APPROVED, blog.getStatus());
        verify(blogRepository, times(1)).findById(1L);
    }

    @Test
    public void testProcessValidationResponse_InvalidBlog() {
        // Arrange
        ValidationResponse response = new ValidationResponse(1L, false);
        Blog blog = new Blog("Test Blog", "This is test content");
        blog.setId(1L);
        blog.setStatus(BlogStatus.PENDING);
        
        when(blogRepository.findById(1L)).thenReturn(blog);

        // Act
        blogValidationService.processValidationResponse(response);

        // Assert
        assertEquals(BlogStatus.REJECTED, blog.getStatus());
        verify(blogRepository, times(1)).findById(1L);
    }

    @Test
    public void testProcessValidationResponse_BlogNotFound() {
        // Arrange
        ValidationResponse response = new ValidationResponse(999L, true);
        when(blogRepository.findById(999L)).thenReturn(null);

        // Act
        blogValidationService.processValidationResponse(response);

        // Assert
        verify(blogRepository, times(1)).findById(999L);
        // No exception should be thrown
    }
}