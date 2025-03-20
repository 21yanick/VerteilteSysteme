package ch.hftm.control;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ch.hftm.entity.Blog;
import ch.hftm.entity.BlogLike;
import ch.hftm.entity.Comment;
import ch.hftm.repository.BlogRepository;
import ch.hftm.repository.CommentRepository;
import ch.hftm.repository.LikeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import ch.hftm.dto.BlogDTO;
import ch.hftm.dto.CommentDTO;
import ch.hftm.dto.LikeDTO;

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
            throw new NotFoundException("Blog mit ID " + blogId + " nicht gefunden");
        }
    }

    @Transactional
    public void updateBlog(Blog blog) {
        if (blog.getId() != null && blogRepository.findById(blog.getId()) != null) {
            blogRepository.getEntityManager().merge(blog);
            Log.info("Blog aktualisiert: " + blog.getTitle());
        } else {
            throw new NotFoundException("Blog mit ID " + blog.getId() + " nicht gefunden");
        }
    }

    public Blog getBlogById(Long id) {
        Blog blog = blogRepository.findById(id);
        if (blog == null) {
            throw new NotFoundException("Blog mit ID " + id + " nicht gefunden");
        }
        return blog;
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
            throw new NotFoundException("Blog mit ID " + blogId + " nicht gefunden");
        }
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId);
        if (comment != null) {
            commentRepository.delete(comment);
        } else {
            throw new NotFoundException("Kommentar mit ID " + commentId + " nicht gefunden");
        }
    }

    @Transactional
    public void addLikeToBlog(Long blogId, BlogLike like) {
        Blog blog = blogRepository.findById(blogId);
        if (blog != null) {
            like.setBlog(blog); // Verknüpft den Like mit dem Blog
            likeRepository.persist(like);
        } else {
            throw new NotFoundException("Blog mit ID " + blogId + " nicht gefunden");
        }
    }

    @Transactional
    public void removeLikeFromBlog(Long likeId) {
        BlogLike like = likeRepository.findById(likeId);
        if (like != null) {
            likeRepository.delete(like);
        } else {
            throw new NotFoundException("Like mit ID " + likeId + " nicht gefunden");
        }
    }

    public Blog mapToBlog(BlogDTO blogDTO) {
        Blog blog = new Blog();
        blog.setId(blogDTO.getId());
        blog.setTitle(blogDTO.getTitle());
        blog.setContent(blogDTO.getContent());
        
        // Status übernehmen, falls vorhanden
        if (blogDTO.getStatus() != null) {
            blog.setStatus(blogDTO.getStatus());
        }

        // Null Check und Comments
        blog.setComments(blogDTO.getComments() != null 
            ? blogDTO.getComments().stream()
                  .map(this::mapToComment)
                  .collect(Collectors.toList())
            : new ArrayList<>());

        // Null Check und Likes
        blog.setLikes(blogDTO.getLikes() != null 
            ? blogDTO.getLikes().stream()
                  .map(this::mapToLike)
                  .collect(Collectors.toList())
            : new ArrayList<>());

        return blog;
    }

    public BlogDTO mapToBlogDTO(Blog blog) {
        BlogDTO blogDTO = new BlogDTO();
        blogDTO.setId(blog.getId());
        blogDTO.setTitle(blog.getTitle());
        blogDTO.setContent(blog.getContent());
        
        // Status als String setzen (Enums werden als String gespeichert)
        if (blog.getStatus() != null) {
            blogDTO.setStatus(blog.getStatus().name());
        }
        
        // Ablehnungsgrund immer setzen, wenn vorhanden
        if (blog.getRejectionReason() != null) {
            blogDTO.setRejectionReason(blog.getRejectionReason());
        }

        // Null Check und Comments
        blogDTO.setComments(blog.getComments() != null 
            ? blog.getComments().stream()
                  .map(this::mapToCommentDTO)
                  .collect(Collectors.toList())
            : new ArrayList<>());

        // Null Check und Likes
        blogDTO.setLikes(blog.getLikes() != null 
            ? blog.getLikes().stream()
                  .map(this::mapToLikeDTO)
                  .collect(Collectors.toList())
            : new ArrayList<>());

        return blogDTO;
    }

    public Comment mapToComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setId(commentDTO.getId());
        comment.setText(commentDTO.getText());
        return comment;
    }

    public CommentDTO mapToCommentDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setText(comment.getText());
        return commentDTO;
    }

    public BlogLike mapToLike(LikeDTO likeDTO) {
        BlogLike like = new BlogLike();
        like.setId(likeDTO.getId());
        like.setUsername(likeDTO.getUsername());
        return like;
    }

    public LikeDTO mapToLikeDTO(BlogLike like) {
        LikeDTO likeDTO = new LikeDTO();
        likeDTO.setId(like.getId());
        likeDTO.setUsername(like.getUsername());
        return likeDTO;
    }
}