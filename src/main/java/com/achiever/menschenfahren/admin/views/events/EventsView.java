package com.achiever.menschenfahren.admin.views.events;

import java.util.List;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;

import com.achiever.menschenfahren.admin.common.Helper;
import com.achiever.menschenfahren.admin.data.service.EventService;
import com.achiever.menschenfahren.admin.views.main.MainView;
import com.achiever.menschenfahren.base.dto.EventDto;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
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

    private static final long    serialVersionUID = -4936032145117863122L;

    private final Grid<EventDto> grid             = new Grid<>();

    private final EventService   eventService;

    @Autowired
    public EventsView(@NonNull final EventService eventService) {
        this.eventService = eventService;
        setId("events-view");
        addClassName("events-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.setItems(getEvents());
        grid.addComponentColumn(event -> createCard(event));
        add(grid);

        this.eventService.getEvents();
    }

    /**
     * Create card for displaying event
     */
    private HorizontalLayout createCard(final EventDto event) {
        final HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        final Image image = new Image();
        image.setSrc("https://randomuser.me/api/portraits/women/42.jpg");
        final VerticalLayout description = new VerticalLayout();
        description.addClassName("description");
        description.setSpacing(false);
        description.setPadding(false);

        final Span eventDetail = new Span(event.getDescription());
        eventDetail.addClassName("post");

        description.add(createEventCardTitle(event.getName(), Helper.convertDate(event.getCreatedTimestamp())), eventDetail);
        card.add(image, description);
        return card;
    }

    /**
     * Creates card title for displaying event name and created date.
     * 
     * @param eventName
     * @param dateString
     * @return
     */
    private HorizontalLayout createEventCardTitle(@Nonnull final String eventName, @Nonnull final String dateString) {
        final HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        final Span name = new Span(eventName);
        name.addClassName("name");
        final Span date = new Span(dateString);
        date.addClassName("date");
        header.add(name, date);
        return header;
    }

    /**
     * fetch all the events which are not voided and is not private.
     * 
     * @return {@link List} of events
     */
    private List<EventDto> getEvents() {
        return this.eventService.getEvents();
    }

}
