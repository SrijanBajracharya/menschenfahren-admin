package com.achiever.menschenfahren.admin.views.events;

import java.util.ArrayList;
import java.util.List;

import com.achiever.menschenfahren.admin.data.service.EventService;
import com.achiever.menschenfahren.admin.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.IronIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.NonNull;

@Route(value = "events", layout = MainView.class)
@PageTitle("Events")
@CssImport(value = "./styles/views/events/events-view.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
public class EventsView extends Div {

    private static final long  serialVersionUID = -4936032145117863122L;

    private final Grid<Person> grid             = new Grid<>();

    private final EventService eventService;

    public EventsView(@NonNull final EventService eventService) {
        this.eventService = eventService;
        setId("events-view");
        addClassName("events-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.setItems(getEvents());
        grid.addComponentColumn(person -> createCard(person));
        add(grid);

        this.eventService.getEvents();
    }

    private HorizontalLayout createCard(final Person person) {
        final HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        final Image image = new Image();
        image.setSrc(person.getImage());
        final VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        final HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        final Span name = new Span(person.getName());
        name.addClassName("name");
        final Span date = new Span(person.getDate());
        date.addClassName("date");
        header.add(name, date);

        final Span post = new Span(person.getPost());
        post.addClassName("post");

        final HorizontalLayout actions = new HorizontalLayout();
        actions.addClassName("actions");
        actions.setSpacing(false);
        actions.getThemeList().add("spacing-s");

        final IronIcon likeIcon = new IronIcon("vaadin", "heart");
        final Span likes = new Span(person.getLikes());
        likes.addClassName("likes");
        final IronIcon commentIcon = new IronIcon("vaadin", "comment");
        final Span comments = new Span(person.getComments());
        comments.addClassName("comments");
        final IronIcon shareIcon = new IronIcon("vaadin", "connect");
        final Span shares = new Span(person.getShares());
        shares.addClassName("shares");

        actions.add(likeIcon, likes, commentIcon, comments, shareIcon, shares);

        description.add(header, post, actions);
        card.add(image, description);
        return card;
    }

    private List<Person> getEvents() {
        final List<Person> personList = new ArrayList<>();

        personList.add(new Person("https://randomuser.me/api/portraits/women/42.jpg", "Penny", "May 8",
                "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without", "68",
                "abc", "bcd"));
        personList.add(new Person("https://randomuser.me/api/portraits/women/42.jpg", "Elin", "May 8", "undertaker", "68", "jdbc", "wwrew"));
        personList.add(new Person("https://randomuser.me/api/portraits/women/42.jpg", "Anuj", "May 8",
                "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without", "70",
                "pky", "asdf"));
        personList.add(new Person("https://randomuser.me/api/portraits/women/42.jpg", "Srijan", "May 8", "pk", "68", "hi", "assdf"));
        personList.add(new Person("https://randomuser.me/api/portraits/women/42.jpg", "Jack", "May 8",
                "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without", "58",
                "bye", "werwe"));
        personList.add(new Person("https://randomuser.me/api/portraits/women/42.jpg", "Harry", "May 8",
                "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document without", "80",
                "hello", "hkjjk"));
        return personList;
    }

}
