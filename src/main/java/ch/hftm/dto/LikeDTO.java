package ch.hftm.dto;

@org.eclipse.microprofile.openapi.annotations.media.Schema(description = "Like-DataTransferObject")
public class LikeDTO {

    @org.eclipse.microprofile.openapi.annotations.media.Schema(description = "Die eindeutige Kennung des Likes", example = "1")
    private Long id;

    @org.eclipse.microprofile.openapi.annotations.media.Schema(description = "Der Benutzer, der den Blog geliked hat", example = "Benutzer123")
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
