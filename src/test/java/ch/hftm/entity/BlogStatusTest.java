package ch.hftm.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BlogStatusTest {

    @Test
    public void testEnumValues() {
        assertEquals(3, BlogStatus.values().length);
        assertEquals(BlogStatus.PENDING, BlogStatus.valueOf("PENDING"));
        assertEquals(BlogStatus.APPROVED, BlogStatus.valueOf("APPROVED"));
        assertEquals(BlogStatus.REJECTED, BlogStatus.valueOf("REJECTED"));
    }
    
    @Test
    public void testBlogStatusSetting() {
        Blog blog = new Blog();
        
        // Test default status
        assertEquals(BlogStatus.PENDING, blog.getStatus());
        
        // Test setting via enum
        blog.setStatus(BlogStatus.APPROVED);
        assertEquals(BlogStatus.APPROVED, blog.getStatus());
        
        // Test setting via string (valid case)
        blog.setStatus("REJECTED");
        assertEquals(BlogStatus.REJECTED, blog.getStatus());
        
        // Test setting via string (lowercase)
        blog.setStatus("approved");
        assertEquals(BlogStatus.APPROVED, blog.getStatus());
        
        // Test setting via string (invalid case)
        blog.setStatus("INVALID_STATUS");
        assertEquals(BlogStatus.PENDING, blog.getStatus());
        
        // Test setting via null
        blog.setStatus(BlogStatus.APPROVED);
        blog.setStatus((String)null);
        assertEquals(BlogStatus.PENDING, blog.getStatus());
    }
}