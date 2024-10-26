package ch.hftm;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import ch.hftm.control.BlogService;
import ch.hftm.entity.Blog;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class BlogServiceTest {

    @Inject
    BlogService blogService;

    @Test
    public void testAddingAndGettingBlogs() {
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
        assertNotNull(addedBlog, "The new blog should be in the list");
    }

    @Test
    public void testDeletingBlogs() {
        // Arrange
        Blog blogToDelete = new Blog("Delete Me", "This blog should be deleted");
        blogService.addBlog(blogToDelete);
        Long blogIdToDelete = blogToDelete.getId();

        // Act
        blogService.deleteBlog(blogIdToDelete);

        // Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            blogService.getBlogById(blogIdToDelete);
        });

        assertEquals("Blog mit ID " + blogIdToDelete + " nicht gefunden", exception.getMessage());
    }

    @Test
    public void testPagination() {
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

    @Test
    public void testGetNonExistingBlog() {
        // Trying to get a non-existing blog should throw a NotFoundException
        Long nonExistentBlogId = 9999L;
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            blogService.getBlogById(nonExistentBlogId);
        });

        assertEquals("Blog mit ID " + nonExistentBlogId + " nicht gefunden", exception.getMessage());
    }
}
