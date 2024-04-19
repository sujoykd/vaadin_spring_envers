package org.vaadin.example.data.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.data.book.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
