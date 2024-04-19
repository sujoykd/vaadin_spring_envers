package org.vaadin.example;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.data.post.entity.Post;
import org.vaadin.example.data.post.service.PostService;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;


@Route(value = "/post", layout = MainView.class)
public class PostView extends VerticalLayout {

    private final VerticalLayout container;
    private final PostService service;

    public PostView(@Autowired PostService service) {
        this.service = service;

        var textField = new TextField("Update the name of the first entry & the first tag.");
        textField.addClassNames(LumoUtility.Width.FULL);

        var button = new Button("Update", e -> {
            service.updateFirstPost(textField.getValue());
            updateContainer();
        });
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickShortcut(Key.ENTER);

        this.container = new VerticalLayout();
        add(textField, button, container);
        updateContainer();
    }

    private void updateContainer() {
        this.container.removeAll();
        var posts = this.service.fetchPosts();
        posts.forEach(post -> {
            var btn = new Button("Show History", event -> showPostHistory(post));
            var content = postDisplayComponent(post);
            content.add(btn);
            this.container.add(content);
        });
    }

    private void showPostHistory(Post post) {
        var revisions = service.fetchPostRevisions(post);

        var components = revisions.stream().map(rev -> {
            var detailsWrapper = new Div();
            var paragraph = new Paragraph(rev.toString());
            var button = new Button("Show Details", e -> {
                var revisionPost = service.fetchPostHistory(post, rev);
                var content = postDisplayComponent(revisionPost);
                detailsWrapper.removeAll();
                detailsWrapper.add(content);
            });
            return new Div(paragraph, detailsWrapper, button);
        }).toArray(Div[]::new);

        var dialog = new Dialog("Post revisions");
        dialog.add(components);
        dialog.open();
    }

    private Paragraph postDisplayComponent(Post post) {
        var title = new Span(post.getTitle());
        var tags = post.getTags().stream().map(pt -> pt.getTag().getName()).collect(Collectors.joining(" | "));
        var content = new Paragraph(title, new Span(tags));
        content.addClassNames(LumoUtility.Display.INLINE_FLEX, LumoUtility.Gap.MEDIUM, LumoUtility.AlignItems.BASELINE);
        return content;
    }

}
