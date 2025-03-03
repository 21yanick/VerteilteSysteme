package ch.hftm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Blog {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Titel kann nicht null sein")
    @Size(min = 2, max = 100, message = "Titel muss zwischen 2 und 100 Zeichen haben")
    private String title;

    @NotNull(message = "Content kann nicht null sein")
    @Size(min = 10, message = "Content muss mindestens 10 Zeichen lang sein")
    private String content;

    /**
     * Status-Feld, das den aktuellen Validierungsstatus des Blogs speichert
     * Standard: PENDING
     */
    @Enumerated(EnumType.STRING)
    private BlogStatus status;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlogLike> likes = new ArrayList<>();

    public Blog() {
        status = BlogStatus.PENDING;
    }

    public Blog(String title, String content) {
        this.title = title;
        this.content = content;
        this.status = BlogStatus.PENDING;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public BlogStatus getStatus() {
        return status;
    }

    public void setStatus(BlogStatus status) {
        this.status = status;
    }
    
    /**
     * Setzt den Status anhand eines String-Werts
     * Nützlich für REST-Aufrufe und Deserialisierung
     */
    public void setStatus(String statusString) {
        if (statusString == null) {
            this.status = BlogStatus.PENDING;
            return;
        }
        
        try {
            this.status = BlogStatus.valueOf(statusString.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Fallback bei ungültigen Werten
            this.status = BlogStatus.PENDING;
        }
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
        comments.forEach(comment -> comment.setBlog(this));
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setBlog(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setBlog(null);
    }

    public List<BlogLike> getLikes() {
        return likes;
    }

    public void setLikes(List<BlogLike> likes) {
        this.likes = likes;
        likes.forEach(like -> like.setBlog(this));
    }

    public void addLike(BlogLike like) {
        likes.add(like);
        like.setBlog(this);
    }

    public void removeLike(BlogLike like) {
        likes.remove(like);
        like.setBlog(null);
    }
}