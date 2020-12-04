package com.achiever.menschenfahren.admin.views.addeventtype;

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
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.BindingValidationStatus;
import com.vaadin.flow.data.validator.StringLengthValidator;
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

    @Autowired
    private EventTypeService           eventTypeService;

    private Binder<EventTypeCreateDto> binder           = new Binder<>(EventTypeCreateDto.class);

    @Autowired
    public AddEventTypeView() {
        setId("add-event-type-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        this.save.setEnabled(false);
        this.eventName.addValueChangeListener(this::nameUpdated);
        this.eventName.setRequiredIndicatorVisible(true);

        binder.bindInstanceFields(this);

        initBinding();
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
