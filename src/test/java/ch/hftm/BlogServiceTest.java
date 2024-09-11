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
        Blog newBlog = new Blog("Test Blog", "This is a test blog");
        int initialSize = blogService.getBlogs().size();
    
        // Act
        blogService.addBlog(newBlog);
        List<Blog> blogs = blogService.getBlogs();
    
        // Assert
        Blog addedBlog = blogs.stream()
                              .filter(blog -> blog.getTitle().equals("Test Blog"))
                              .findFirst()
                              .orElse(null);
    
        assertEquals(initialSize + 1, blogs.size(), "Blog should be added");
        assertTrue(addedBlog != null, "The new blog should be in the list");
    }    

    @Test
    void testDeletingBlogs() {
        // Arrange
        Blog blogToDelete = new Blog("Delete Me", "This blog should be deleted");
        blogService.addBlog(blogToDelete);
        Long blogIdToDelete = blogToDelete.getId();

        // Act
        blogService.deleteBlog(blogIdToDelete);
        Blog foundBlog = blogService.getBlogById(blogIdToDelete);

        // Assert
        assertNull(foundBlog, "The blog should be deleted");
    }

    @Test
    void testPagination() {
        // Arrange
        Blog blog1 = new Blog("Blog 1", "Content 1");
        Blog blog2 = new Blog("Blog 2", "Content 2");
        blogService.addBlog(blog1);
        blogService.addBlog(blog2);

        // Act
        List<Blog> firstPage = blogService.getBlogsPaginated(0, 1);
        List<Blog> secondPage = blogService.getBlogsPaginated(1, 1);

        // Assert
        assertEquals(1, firstPage.size(), "First page should return one blog");
        assertEquals(1, secondPage.size(), "Second page should return one blog");
    }
}
