package org.vaadin.example;

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
import org.vaadin.example.data.entity.Post;
import org.vaadin.example.data.entity.Tag;
import org.vaadin.example.data.repository.PostRepository;
import org.vaadin.example.data.repository.TagRepository;

@Service
@Slf4j
public class DataService {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PostRepository postRepository;

    private AuditReader getAuditReader() {
        return AuditReaderFactory.get(entityManager);
    }

    public void test() {
        var tagCount = tagRepository.count();
        if (tagCount == 0) {
            createTags();
        }

        var postCount = postRepository.count();
        if (postCount == 0) {
            createPosts();
        }
    }

    private void createTags() {
        Tag misc = new Tag("Misc");
        Tag jdbc = new Tag("JDBC");
        Tag hibernate = new Tag("Hibernate");
        Tag jooq = new Tag("jOOQ");
        try {
            tagRepository.saveAll(List.of(misc, jdbc, hibernate, jooq));
        } catch (DataIntegrityViolationException ex) {
            log.info(ex.getMessage());
        }
    }

    private void createPosts() {
        var tags = tagRepository.findAll();

        var post1 = new Post("High-Performance Java Persistence 1st edition");
        post1.setId(1L);
        tags.forEach(post1::addTag);

        var post2 = new Post("High-Performance Java Persistence 2nd edition");
        post2.setId(2L);
        tags.forEach(post2::addTag);

        try {
            postRepository.saveAll(List.of(post1, post2));
        } catch (DataIntegrityViolationException ex) {
            log.info(ex.getMessage());
        }
    }

    @Transactional
    public void updateFirstPost(String name) {
        var firstTag = tagRepository.findAll().getFirst();
        firstTag.setName(name);

        var firstPost = postRepository.findAll().getFirst();
        firstPost.setTitle("Post by name: " + name);
        firstPost.removeTag(firstTag);
        firstPost.addTag(firstTag);

        postRepository.save(firstPost);
    }

    @Transactional
    public List<Number> fetchPostRevisions(Post post) {
        return getAuditReader().getRevisions(Post.class, post.getId());
    }

    @Transactional
    public Post fetchPostHistory(Post p, Number rev) {
        var post = getAuditReader().find(Post.class, Post.class.getName(), p.getId(), rev, true);
        Hibernate.initialize(post.getTags());
        post.getTags().forEach(pt -> Hibernate.initialize(pt.getTag()));
        return post;
    }

    public List<Post> fetchPosts() {
        return postRepository.findAll();
    }
}
