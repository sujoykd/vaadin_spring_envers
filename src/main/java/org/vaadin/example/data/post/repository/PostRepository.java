package org.vaadin.example.data.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.data.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
