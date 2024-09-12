package ch.hftm.dto;

@org.eclipse.microprofile.openapi.annotations.media.Schema(description = "Kommentar-DataTransferObject")
public class CommentDTO {

    @org.eclipse.microprofile.openapi.annotations.media.Schema(description = "Die eindeutige Kennung des Kommentars", example = "1")
    private Long id;

    @org.eclipse.microprofile.openapi.annotations.media.Schema(description = "Der Textinhalt des Kommentars", example = "Dies ist ein Testkommentar")
    private String text;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
