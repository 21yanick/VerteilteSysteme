package ch.hftm;

import java.util.List;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import io.quarkus.logging.Log;

@Dependent
public class BlogService {

    @Inject
    private BlogRepository blogRepository;

    public List<Blog> getBlogs() {
        List<Blog> blogs = blogRepository.getBlogs();
        Log.info("Returning " + blogs.size() + " blogs");
        return blogs;
    }

    public void addBlog(Blog blog) {
        Log.info("Adding blog: " + blog.getTitle());
        blogRepository.addBlog(blog);
    }

    public void deleteBlog(Blog blog) {
        Log.info("Deleting blog: " + blog.getTitle());
        blogRepository.deleteBlog(blog);
    }
}
