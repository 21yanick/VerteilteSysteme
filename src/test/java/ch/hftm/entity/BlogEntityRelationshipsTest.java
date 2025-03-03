package ch.hftm.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BlogEntityRelationshipsTest {

    @Test
    public void testCommentRelationship() {
        // Arrange
        Blog blog = new Blog("Test Blog", "Test Content");
        Comment comment = new Comment();
        comment.setText("Test Comment");
        
        // Act
        blog.addComment(comment);
        
        // Assert
        assertEquals(1, blog.getComments().size());
        assertEquals("Test Comment", blog.getComments().get(0).getText());
        assertEquals(blog, comment.getBlog());
        
        // Test removal
        blog.removeComment(comment);
        assertEquals(0, blog.getComments().size());
        assertNull(comment.getBlog());
    }
    
    @Test
    public void testLikeRelationship() {
        // Arrange
        Blog blog = new Blog("Test Blog", "Test Content");
        BlogLike like = new BlogLike();
        like.setUsername("testuser");
        
        // Act
        blog.addLike(like);
        
        // Assert
        assertEquals(1, blog.getLikes().size());
        assertEquals("testuser", blog.getLikes().get(0).getUsername());
        assertEquals(blog, like.getBlog());
        
        // Test removal
        blog.removeLike(like);
        assertEquals(0, blog.getLikes().size());
        assertNull(like.getBlog());
    }
    
    @Test
    public void testSetCommentsMethod() {
        // Arrange
        Blog blog = new Blog("Test Blog", "Test Content");
        Comment comment1 = new Comment();
        comment1.setText("Comment 1");
        Comment comment2 = new Comment();
        comment2.setText("Comment 2");
        
        // Act
        blog.setComments(java.util.Arrays.asList(comment1, comment2));
        
        // Assert
        assertEquals(2, blog.getComments().size());
        assertEquals(blog, comment1.getBlog());
        assertEquals(blog, comment2.getBlog());
    }
    
    @Test
    public void testSetLikesMethod() {
        // Arrange
        Blog blog = new Blog("Test Blog", "Test Content");
        BlogLike like1 = new BlogLike();
        like1.setUsername("user1");
        BlogLike like2 = new BlogLike();
        like2.setUsername("user2");
        
        // Act
        blog.setLikes(java.util.Arrays.asList(like1, like2));
        
        // Assert
        assertEquals(2, blog.getLikes().size());
        assertEquals(blog, like1.getBlog());
        assertEquals(blog, like2.getBlog());
    }
}