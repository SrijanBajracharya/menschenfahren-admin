package com.achiever.menschenfahren.admin.views.dashboard;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.annotation.Autowired;

import com.achiever.menschenfahren.admin.data.service.EventService;
import com.achiever.menschenfahren.admin.data.service.UserService;
import com.achiever.menschenfahren.admin.views.main.MainView;
import com.github.appreciated.card.ClickableCard;
import com.github.appreciated.card.RippleClickableCard;
import com.github.appreciated.card.content.Item;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@Route(value = "dashboard", layout = MainView.class)
@PageTitle("Dashboard")
@CssImport(value = "./styles/views/dashboard/dashboard-view.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
@RouteAlias(value = "", layout = MainView.class)
@Tag("clickable-card")
public class DashboardView extends RippleClickableCard {

    private final EventService eventService;

    private final UserService  userService;

    @Autowired
    public DashboardView(@Nonnull final EventService eventService, @Nonnull final UserService userService) {
        setId("dashboard-view");
        this.eventService = eventService;
        this.userService = userService;
        add(createCard("Number of User", this.userService.getUserSize()));
        add(createCard("Number of Events", this.eventService.getEventSize()));
    }

    private ClickableCard createCard(@Nonnull final String title, @Nonnull final int size) {

        RippleClickableCard rcard = new RippleClickableCard(onClick -> {
            /* Handle Card click */}, new Item(title, String.valueOf(size)) // Follow up with more Components ...
        );

        rcard.addClassName("card-item");
        // ClickableCard card = new ClickableCard();
        // card.addClassName("card-item");
        // Item item = new Item(title, String.valueOf(size));
        // card.add(item);
        return rcard;
    }

}
