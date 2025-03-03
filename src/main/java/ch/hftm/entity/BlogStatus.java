package ch.hftm.entity;

/**
 * Enum representing the possible status values for a blog post.
 */
public enum BlogStatus {
    /**
     * The blog is pending validation
     */
    PENDING,
    
    /**
     * The blog has been validated and approved
     */
    APPROVED,
    
    /**
     * The blog has been validated and rejected
     */
    REJECTED
}