package com.achiever.menschenfahren.admin.views.addeventtype;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.achiever.menschenfahren.admin.data.service.EventTypeService;
import com.achiever.menschenfahren.admin.views.main.MainView;
import com.achiever.menschenfahren.base.dto.EventTypeCreateDto;
import com.achiever.menschenfahren.base.dto.EventTypeDto;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.extern.slf4j.Slf4j;

@Route(value = "event-type", layout = MainView.class)
@PageTitle("Add Event Type")
@CssImport("./styles/views/addeventtype/add-event-type-view.css")
@Slf4j
public class AddEventTypeView extends Div {

    private static final long          serialVersionUID = 8649241951703175274L;

    private final TextField            eventName        = new TextField("Display Name");
    private final TextField            description      = new TextField("Description");

    private final Button               cancel           = new Button("Cancel");
    private final Button               save             = new Button("Save");

    private final EventTypeService     eventTypeService;
    private static final String        TAB2_ID          = "id-tab2";
    private static final String        TAB1_ID          = "id-tab1";

    private Binder<EventTypeCreateDto> binder           = new Binder<>(EventTypeCreateDto.class);

    @Autowired
    public AddEventTypeView(final EventTypeService eventTypeService) {
        this.eventTypeService = eventTypeService;
        setId("add-event-type-view");

        Tab tab1 = new Tab("Add Event Type");
        tab1.setId(TAB1_ID);
        Div page1 = createTab1();

        Tab tab2 = new Tab("All Events Type");
        tab2.setId(TAB2_ID);

        Div page2 = createTab2();

        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(tab1, page1);
        tabsToPages.put(tab2, page2);

        Tabs tabs = new Tabs(tab1, tab2);
        Div pages = new Div(page1, page2);

        tabs.addSelectedChangeListener(event -> {
            tabsToPages.values().forEach(page -> page.setVisible(false));
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
            initTabListener(tab1, tab2, page2);
        });

        add(tabs, pages);

    }

    /**
     * Initializes tab listener, creates grid when tab is selected.
     *
     * @param tab1
     * @param tab2
     */
    private void initTabListener(@Nonnull final Tab tab1, @Nonnull final Tab tab2, @Nonnull final Div page2) {

        if (tab2.isSelected()) {
            page2.add(createGrid());
        } else {
            page2.removeAll();
        }
    }

    /**
     * Create second page
     *
     * @return
     */
    private Div createTab2() {
        Div page2 = new Div();
        page2.addClassName("page2-wrapper");
        page2.setVisible(false);
        return page2;
    }

    /**
     * Creates table with different columns with feature for sorting and filtering.
     */
    private Grid<EventTypeDto> createGrid() {

        final List<EventTypeDto> eventTypeList = getItems();
        final Grid<EventTypeDto> grid = new Grid<>();
        final ListDataProvider<EventTypeDto> dataProvider = new ListDataProvider<>(eventTypeList);
        grid.setDataProvider(dataProvider);

        final Grid.Column<EventTypeDto> nameColumn = grid.addColumn(EventTypeDto::getName).setSortable(true).setHeader("Event Type Name");

        final Grid.Column<EventTypeDto> descriptionColumn = grid.addColumn(EventTypeDto::getDescription).setSortable(true).setHeader("Description");
        final Grid.Column<EventTypeDto> voidedColumn = grid.addColumn(eventTypeDto -> eventTypeDto.isVoided()).setSortable(true).setHeader("Voided");

        final HeaderRow filterRow = grid.appendHeaderRow();
        // First filter
        final TextField nameField = createFilter(dataProvider, nameColumn, filterRow);
        nameField.addValueChangeListener(
                eventType -> dataProvider.addFilter(eventTypeDto -> StringUtils.containsIgnoreCase(eventTypeDto.getName(), nameField.getValue())));

        // Second filter
        final TextField descriptionField = createFilter(dataProvider, descriptionColumn, filterRow);
        descriptionField.addValueChangeListener(eventType -> dataProvider
                .addFilter(eventTypeDto -> StringUtils.containsIgnoreCase(String.valueOf(eventTypeDto.getDescription()), descriptionField.getValue())));

        // Third filter
        final TextField voidedField = createFilter(dataProvider, voidedColumn, filterRow);
        voidedField.addValueChangeListener(eventType -> dataProvider
                .addFilter(eventTypeDto -> StringUtils.containsIgnoreCase(Boolean.toString(eventTypeDto.isVoided()), voidedField.getValue())));

        return grid;

    }

    /**
     * Create text field for filtering.
     */
    private TextField createFilter(final ListDataProvider<EventTypeDto> dataProvider, final Grid.Column<EventTypeDto> column, final HeaderRow filterRow) {

        final TextField textField = new TextField();

        textField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(column).setComponent(textField);

        textField.setSizeFull();
        textField.setPlaceholder("Filter");
        return textField;
    }

    /** Fetch all users. **/
    private List<EventTypeDto> getItems() {
        return this.eventTypeService.getAllEventType();
    }

    /**
     * Creates first tab for adding event type.
     * 
     * @return
     */
    private Div createTab1() {

        Div page1 = new Div();
        page1.addClassName("page1-wrapper");
        page1.add(createTitle());
        page1.add(createFormLayout());
        page1.add(createButtonLayout());

        this.save.setEnabled(false);
        this.eventName.addValueChangeListener(this::nameUpdated);
        this.eventName.setRequiredIndicatorVisible(true);

        binder.bindInstanceFields(this);

        initBinding();

        return page1;
    }

    /**
     * Listener which check for display name validation and enables/disable save button.
     *
     * @param event
     */
    private void nameUpdated(@Nullable final ComponentValueChangeEvent<TextField, String> event) {
        final String eventNameField = this.eventName.getValue();
        if (StringUtils.isNotBlank(eventNameField)) {
            UI.getCurrent().access(() -> {
                this.save.setEnabled(true);
            });
        } else {
            UI.getCurrent().access(() -> {
                this.save.setEnabled(false);
            });
        }

    }

    /**
     * Creates form layout.
     *
     * @return
     */
    private Component createFormLayout() {
        final FormLayout formLayout = new FormLayout();
        formLayout.add(eventName, description);
        return formLayout;
    }

    /**
     * clears form.
     */
    private void clearForm() {
        binder.setBean(new EventTypeCreateDto());
    }

    /**
     * Initialises button listener.
     */
    private void initButtonListener() {
        this.cancel.addClickListener(e -> {

        });

        this.save.addClickListener(e -> {
            EventTypeCreateDto eventTypeDto = new EventTypeCreateDto();
            eventTypeDto.setName(this.eventName.getValue());
            eventTypeDto.setDescription(this.description.getValue());
            eventTypeDto.setVoided(false);
            validateAndSave(this.binder, eventTypeDto);
        });
    }

    /**
     * Initialises form binding.
     */
    private void initBinding() {
        binder.forField(this.eventName).asRequired("Display name is required.").withValidator(new StringLengthValidator("Please enter Display name", 1, null))
                .bind(EventTypeCreateDto::getName, EventTypeCreateDto::setName);
        binder.readBean(new EventTypeCreateDto());
    }

    /**
     * Validation error.
     *
     * @param binder
     */
    private void validationError(final Binder<EventTypeCreateDto> binder) {
        final BinderValidationStatus<EventTypeCreateDto> validate = binder.validate();
        final String errorText = validate.getFieldValidationStatuses().stream().filter(BindingValidationStatus::isError)
                .map(BindingValidationStatus::getMessage).map(Optional::get).distinct().collect(Collectors.joining(", "));
        log.error("Could not validate: {}", errorText);
    }

    /**
     * Validates and tries to save the event type.
     *
     * @param binder
     * @param request
     */
    private void validateAndSave(@Nonnull final Binder<EventTypeCreateDto> binder, @Nonnull final EventTypeCreateDto request) {
        if (binder.writeBeanIfValid(request)) {
            try {
                final EventTypeDto response = this.eventTypeService.createEventType(request);
                if (response != null) {
                    Notification.show("Event Type successfully added.");
                    clearForm();
                }
            } catch (final Exception e) {
                Notification.show("Error while creating event type");
            }

        } else {
            validationError(binder);
        }
    }

    /**
     * Creates page title
     *
     * @return
     */
    private Component createTitle() {
        return new H3("Add Event Types");
    }

    /**
     * Creates Button layout containing save and cancel button.
     *
     * @return
     */
    private Component createButtonLayout() {
        final HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save);
        buttonLayout.add(cancel);
        initButtonListener();
        return buttonLayout;
    }

}
