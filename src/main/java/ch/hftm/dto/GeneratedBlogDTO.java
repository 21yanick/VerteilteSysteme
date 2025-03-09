package ch.hftm.dto;

import java.time.LocalDateTime;

/**
 * DTO für generierte Blog-Beiträge mit KI.
 * Enthält die Parameter für die Generierung und das Ergebnis.
 */
public class GeneratedBlogDTO {
    
    private Long id;
    private String title;
    private String content;
    private String status;
    private LocalDateTime createdAt;
    
    // Blog-Generierungsparameter
    private String templateType;
    private String audience;
    private String tone;
    private String length;
    
    // Getters und Setters
    
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
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getTemplateType() {
        return templateType;
    }
    
    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }
    
    public String getAudience() {
        return audience;
    }
    
    public void setAudience(String audience) {
        this.audience = audience;
    }
    
    public String getTone() {
        return tone;
    }
    
    public void setTone(String tone) {
        this.tone = tone;
    }
    
    public String getLength() {
        return length;
    }
    
    public void setLength(String length) {
        this.length = length;
    }
}