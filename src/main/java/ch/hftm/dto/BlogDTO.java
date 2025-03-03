package ch.hftm.dto;

import java.util.List;

@org.eclipse.microprofile.openapi.annotations.media.Schema(description = "Blog-DataTransferObject")
public class BlogDTO {

    @org.eclipse.microprofile.openapi.annotations.media.Schema(description = "Die eindeutige Kennung des Blogs", example = "1")
    private Long id;

    @org.eclipse.microprofile.openapi.annotations.media.Schema(description = "Titel des Blogs", example = "Mein erster Blog")
    private String title;

    @org.eclipse.microprofile.openapi.annotations.media.Schema(description = "Inhalt des Blogs", example = "Dies ist der Inhalt meines ersten Blogbeitrags")
    private String content;
    
    @org.eclipse.microprofile.openapi.annotations.media.Schema(description = "Status des Blog-Posts", example = "PENDING", enumeration = {"PENDING", "APPROVED", "REJECTED"})
    private String status;

    @org.eclipse.microprofile.openapi.annotations.media.Schema(description = "Liste der Kommentare, die dem Blog zugeordnet sind")
    private List<CommentDTO> comments;

    @org.eclipse.microprofile.openapi.annotations.media.Schema(description = "Liste der Likes, die dem Blog zugeordnet sind")
    private List<LikeDTO> likes;

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

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public List<LikeDTO> getLikes() {
        return likes;
    }

    public void setLikes(List<LikeDTO> likes) {
        this.likes = likes;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}
