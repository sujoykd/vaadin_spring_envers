package org.vaadin.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.data.book.entity.Book;
import org.vaadin.example.data.book.service.BookService;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;


@Route(value = "/book", layout = MainView.class)
public class BookView extends VerticalLayout {

    private final VerticalLayout container;
    private final BookService service;

    public BookView(@Autowired BookService service) {
        this.service = service;

        var textField = new TextField("Update the name of the first entry & the first tag.");
        textField.addClassNames(LumoUtility.Width.FULL);

        var button = new Button("Update", e -> {
            service.updateFirstBook(textField.getValue());
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
        var books = this.service.fetchBooks();
        books.forEach(book -> {
            var historyBtn = new Button("Show History", event -> showPostHistory(book));
            var addAuthorBtn = new Button("Add Author", event -> {
                service.addAuthorToBook(book);
                updateContainer();
            });
            var content = bookDisplayComponent(book);
            content.add(historyBtn, addAuthorBtn);
            this.container.add(content);
        });
    }

    private void showPostHistory(Book book) {
        var revisions = service.fetchBookRevisions(book);

        var components = revisions.stream().map(rev -> {
            var detailsWrapper = new Div();
            var paragraph = new Paragraph(rev.toString());
            var button = new Button("Show Details", e -> {
                var revisionPost = service.fetchBookHistory(book, rev);
                var content = bookDisplayComponent(revisionPost);
                detailsWrapper.removeAll();
                detailsWrapper.add(content);
            });
            return new Div(paragraph, detailsWrapper, button);
        }).toArray(Div[]::new);

        var dialog = new Dialog("Post revisions");
        dialog.add(components);
        dialog.open();
    }

    private Paragraph bookDisplayComponent(Book book) {
        var names = new Span(book.getName());
        var authorsDisplay = book.getAuthors().stream()
                .map(author -> {
                    var btn = new Button(VaadinIcon.FILE_REMOVE.create(), event -> {
                        service.removeAuthorFromBook(book, author);
                        updateContainer();
                    });
                    return new Span(new Span(author.getName()), btn);
                })
                .toArray(Span[]::new);
        var content = new Paragraph(names, new Paragraph(authorsDisplay));
        content.addClassNames(LumoUtility.Display.INLINE_FLEX, LumoUtility.Gap.MEDIUM, LumoUtility.AlignItems.BASELINE);
        return content;
    }

}
