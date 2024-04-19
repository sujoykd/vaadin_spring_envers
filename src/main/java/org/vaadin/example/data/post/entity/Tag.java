package org.vaadin.example.data.post.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@EqualsAndHashCode(callSuper = false)
@ToString(onlyExplicitlyIncluded = true, callSuper = false)
@Entity(name = "Tag")
@Table(name = "tag")
@Audited
@EntityListeners(AuditingEntityListener.class)
public class Tag {

    @Id
    @GeneratedValue
    private Long id;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(unique = true)
    private String name;

    @OneToMany(
            mappedBy = "tag",
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<PostTag> posts = new ArrayList<>();

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

    public Tag(String name) {
        this.name = name;
    }
}