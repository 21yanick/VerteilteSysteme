package ch.hftm.control;

import java.util.List;

import ch.hftm.entity.Blog;
import ch.hftm.entity.Comment;
import ch.hftm.repository.BlogRepository;
import ch.hftm.repository.CommentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Sort;

@ApplicationScoped
public class BlogService {

    @Inject
    private BlogRepository blogRepository;

    @Inject
    private CommentRepository commentRepository;

    public List<Blog> getBlogs() {
        List<Blog> blogs = blogRepository.listAll(Sort.by("title"));
        Log.info("Returning " + blogs.size() + " blogs");
        return blogs;
    }

    @Transactional
    public void addBlog(Blog blog) {
        Log.info("Adding blog with ID: " + blog.getId());
        blogRepository.persist(blog);
    }

    @Transactional
    public void deleteBlog(Long blogId) {
        Blog blog = blogRepository.findById(blogId);
        if (blog != null) {
            blogRepository.delete(blog);
            Log.info("Deleted blog: " + blog.getTitle());
        } else {
            Log.warn("Blog not found with ID: " + blogId);
        }
    }

    @Transactional
    public void updateBlog(Blog blog) {
        if (blog.getId() != null && blogRepository.findById(blog.getId()) != null) {
            blogRepository.getEntityManager().merge(blog);
            Log.info("Updated blog: " + blog.getTitle());
        } else {
            Log.warn("Blog not found with ID: " + blog.getId());
        }
    }

    public Blog getBlogById(Long id) {
        return blogRepository.findById(id);
    }

    public List<Blog> getBlogsByTitle(String title) {
        return blogRepository.find("title like ?1", "%" + title + "%").list();
    }
    
    @Transactional
    public void addCommentToBlog(Long blogId, Comment comment) {
        Blog blog = blogRepository.findById(blogId);
        if (blog != null) {
            comment.setBlog(blog);  // Setzt die Beziehung zwischen Kommentar und Blog
            commentRepository.persist(comment);
        } else {
            throw new WebApplicationException("Blog not found", 404);
        }
    }


}
