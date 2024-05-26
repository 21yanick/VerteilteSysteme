package ch.hftm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import ch.hftm.control.BlogService;
import ch.hftm.entity.Blog;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class BlogServiceTest {

    @Inject
    BlogService blogService;

    @Test
    void testAddingAndGettingBlogs() {
        // Arrange
        Blog newBlog = new Blog("Test Blog", "This is a test blog", 1L);
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
        Blog blogToDelete = new Blog("Delete Me", "This blog should be deleted", 343L);
        blogService.addBlog(blogToDelete);
        Long blogIdToDelete = blogToDelete.getID();

        // Act
        blogService.deleteBlog(blogIdToDelete);
        Blog foundBlog = blogService.getBlogById(blogIdToDelete);

        // Assert
        assertNull(foundBlog, "The blog should be deleted");
    }

}
