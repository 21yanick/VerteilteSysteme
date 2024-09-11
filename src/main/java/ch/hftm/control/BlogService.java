package ch.hftm.control;

import java.util.List;

import ch.hftm.entity.Blog;
import ch.hftm.entity.BlogLike;
import ch.hftm.entity.Comment;
import ch.hftm.repository.BlogRepository;
import ch.hftm.repository.CommentRepository;
import ch.hftm.repository.LikeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;

@ApplicationScoped
public class BlogService {

    @Inject
    private BlogRepository blogRepository;

    @Inject
    private CommentRepository commentRepository;

    @Inject
    private LikeRepository likeRepository;

    public List<Blog> getBlogs() {
        List<Blog> blogs = blogRepository.listAll(Sort.by("title"));
        Log.info("Es werden " + blogs.size() + " Blogs zurückgegeben");
        return blogs;
    }

    @Transactional
    public void addBlog(Blog blog) {
        Log.info("Blog wird hinzugefügt mit ID: " + blog.getId() + " und Titel: " + blog.getTitle());
        blogRepository.persist(blog);
    }

    @Transactional
    public void deleteBlog(Long blogId) {
        Blog blog = blogRepository.findById(blogId);
        if (blog != null) {
            blogRepository.delete(blog);
            Log.info("Blog gelöscht: " + blog.getId() + " mit Titel: " + blog.getTitle());
        } else {
            Log.warn("Blog nicht gefunden mit ID: " + blogId);
        }
    }

    @Transactional
    public void updateBlog(Blog blog) {
        if (blog.getId() != null && blogRepository.findById(blog.getId()) != null) {
            blogRepository.getEntityManager().merge(blog);
            Log.info("Blog aktualisiert: " + blog.getTitle());
        } else {
            Log.warn("Blog nicht gefunden mit ID: " + blog.getId());
        }
    }

    public Blog getBlogById(Long id) {
        return blogRepository.findById(id);
    }

    public List<Blog> getBlogsByTitle(String title) {
        return blogRepository.find("title like ?1", "%" + title + "%").list();
    }

    public List<Blog> getBlogsPaginated(int page, int size) {
        Log.info("Paginierung - Seite: " + page + ", Größe: " + size);
        List<Blog> blogs = blogRepository.findAll().page(Page.of(page, size)).list();
        Log.info("Anzahl der zurückgegebenen Blogs: " + blogs.size());
        return blogs;
    }

    public List<Blog> getBlogsByTitlePaginated(String title, int page, int size) {
        return blogRepository.find("title like ?1", "%" + title + "%").page(page, size).list();
    }

    @Transactional
    public void addCommentToBlog(Long blogId, Comment comment) {
        Blog blog = blogRepository.findById(blogId);
        if (blog != null) {
            comment.setBlog(blog);  // Setzt die Beziehung zwischen Kommentar und Blog
            commentRepository.persist(comment);
        } else {
            throw new WebApplicationException("Blog nicht gefunden", 404);
        }
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId);
        if (comment != null) {
            commentRepository.delete(comment);
        } else {
            throw new WebApplicationException("Kommentar nicht gefunden", 404);
        }
    }

    @Transactional
    public void addLikeToBlog(Long blogId, BlogLike like) {
        Blog blog = blogRepository.findById(blogId);
        if (blog != null) {
            like.setBlog(blog); // Verknüpft den Like mit dem Blog
            likeRepository.persist(like);
        } else {
            throw new WebApplicationException("Blog nicht gefunden", 404);
        }
    }

    @Transactional
    public void removeLikeFromBlog(Long likeId) {
        BlogLike like = likeRepository.findById(likeId);
        if (like != null) {
            likeRepository.delete(like);
        } else {
            throw new WebApplicationException("Like nicht gefunden", 404);
        }
    }
}