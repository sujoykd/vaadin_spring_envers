package org.vaadin.example.data.book.service;


import java.util.HashSet;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.vaadin.example.data.book.entity.Author;
import org.vaadin.example.data.book.entity.Book;
import org.vaadin.example.data.book.repository.AuthorRepository;
import org.vaadin.example.data.book.repository.BookRepository;

@Service
@Slf4j
public class BookService {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;

    private AuditReader getAuditReader() {
        return AuditReaderFactory.get(entityManager);
    }

    public void initialSetup() {
        var authorCount = authorRepository.count();
        if (authorCount == 0) {
            createAuthors();
        }

        var bookCount = bookRepository.count();
        if (bookCount == 0) {
            createBooks();
        }
    }

    private void createAuthors() {
        var author1 = new Author("author 1");
        var author2 = new Author("author 2");
        var author3 = new Author("author 3");
        var author4 = new Author("author 4");
        try {
            authorRepository.saveAll(List.of(author1, author2, author3, author4));
        } catch (DataIntegrityViolationException ex) {
            log.info(ex.getMessage());
        }
    }

    private void createBooks() {
        var authors = authorRepository.findAll();

        var book1 = new Book("Book 1");
        book1.setAuthors(new HashSet<>(authors));

        var book2 = new Book("Book 2");
        book2.setAuthors(new HashSet<>(authors));

        var book3 = new Book("Book 3");
        book3.setAuthors(new HashSet<>(authors));

        try {
            bookRepository.saveAll(List.of(book1, book2, book3));
        } catch (DataIntegrityViolationException ex) {
            log.info(ex.getMessage());
        }
    }

    @Transactional
    public void updateFirstBook(String name) {
        var firstAuthor = authorRepository.findAll().getFirst();
        firstAuthor.setName(name);

        var firstBook = bookRepository.findAll().getFirst();
        firstBook.setName("Book by name: " + name);
        firstBook.getAuthors().add(firstAuthor);

        bookRepository.save(firstBook);
    }

    @Transactional
    public List<Book> fetchBooks() {
        return bookRepository.findAll();
    }

    @Transactional
    public List<Number> fetchBookRevisions(Book book) {
        return getAuditReader().getRevisions(Book.class, book.getId());
    }

    @Transactional
    public Book fetchBookHistory(Book book, Number rev) {
        var bookHistory = getAuditReader().find(Book.class, Book.class.getName(), book.getId(), rev, true);
        Hibernate.initialize(bookHistory.getAuthors());
        return bookHistory;
    }

    public void removeAuthorFromBook(Book book, Author author) {
        book.getAuthors().remove(author);
        bookRepository.save(book);
    }

    public void addAuthorToBook(Book book) {
        var authors = authorRepository.findAll();
        authors.removeAll(book.getAuthors());

        if (!authors.isEmpty()) {
            book.getAuthors().add(authors.getFirst());
            bookRepository.save(book);
        }
    }
}
