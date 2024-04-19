package org.vaadin.example.data.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.data.post.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
