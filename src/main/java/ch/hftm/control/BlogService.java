package ch.hftm.control;

import java.util.List;

import ch.hftm.entity.Blog;
import ch.hftm.repository.BlogRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Sort;

@ApplicationScoped
public class BlogService {

    @Inject
    private BlogRepository blogRepository;

    public List<Blog> getBlogs() {
        List<Blog> blogs = blogRepository.listAll(Sort.by("title"));
        Log.info("Returning " + blogs.size() + " blogs");
        return blogs;
    }

    @Transactional
    public void addBlog(Blog blog) {
        Log.info("Adding blog with ID: " + blog.getID());
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
        if (blog.getID() != null && blogRepository.findById(blog.getID()) != null) {
            blogRepository.getEntityManager().merge(blog);
            Log.info("Updated blog: " + blog.getTitle());
        } else {
            Log.warn("Blog not found with ID: " + blog.getID());
        }
    }

    public Blog getBlogById(Long id) {
        return blogRepository.findById(id);
    }
}
