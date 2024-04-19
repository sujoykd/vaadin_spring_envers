package org.vaadin.example.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.data.entity.Post;
import org.vaadin.example.data.entity.Tag;

public interface PostRepository extends JpaRepository<Post, Long> {
}
