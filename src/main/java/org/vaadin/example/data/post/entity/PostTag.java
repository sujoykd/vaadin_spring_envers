package org.vaadin.example.data.post.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
@Entity(name = "PostTag")
@Table(name = "post_tag")
@Audited
@EntityListeners(AuditingEntityListener.class)
public class PostTag {

    @EmbeddedId
    @ToString.Include
    @EqualsAndHashCode.Include
    private PostTagId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("postId")
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("tagId")
    private Tag tag;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private LocalDateTime createdDateTime = LocalDateTime.now();

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "updated_datetime")
    private LocalDateTime updatedDateTime;


    public PostTag(Post post, Tag tag) {
        this.post = post;
        this.tag = tag;
        this.id = new PostTagId(post.getId(), tag.getId());
    }
}