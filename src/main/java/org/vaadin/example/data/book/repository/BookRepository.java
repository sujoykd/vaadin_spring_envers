package org.vaadin.example.data.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vaadin.example.data.book.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
