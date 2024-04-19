package org.vaadin.example.data.post.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString(onlyExplicitlyIncluded = true, callSuper = false)
@Embeddable
public class PostTagId implements Serializable {

    @Column(name = "post_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long postId;

    @Column(name = "tag_id")
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long tagId;
}
