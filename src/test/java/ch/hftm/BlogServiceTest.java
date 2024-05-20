package ch.hftm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class BlogServiceTest {

    @Inject
    BlogService blogService;

    @Test
    void testAddingAndGettingBlogs() {
        // Arrange
        Blog newBlog = new Blog("Test Blog", "This is a test blog");
        int initialSize = blogService.getBlogs().size();

        // Act
        blogService.addBlog(newBlog);
        List<Blog> blogs = blogService.getBlogs();

        // Assert
        assertEquals(initialSize + 1, blogs.size(), "Blog should be added");
        assertTrue(blogs.contains(newBlog), "The new blog should be in the list");
    }

    @Test
    void testDeletingBlogs() {
        // Arrange
        Blog blogToDelete = new Blog("Delete Me", "This blog should be deleted");
        blogService.addBlog(blogToDelete);

        // Act
        blogService.deleteBlog(blogToDelete);
        List<Blog> blogs = blogService.getBlogs();

        // Assert
        assertTrue(!blogs.contains(blogToDelete), "The blog should be deleted");
    }
}
