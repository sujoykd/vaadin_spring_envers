package org.vaadin.example.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.data.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);
}
