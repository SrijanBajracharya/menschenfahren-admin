package com.achiever.menschenfahren.admin.views.usermanagement;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.achiever.menschenfahren.admin.common.Helper;
import com.achiever.menschenfahren.admin.data.service.UserService;
import com.achiever.menschenfahren.admin.views.main.MainView;
import com.achiever.menschenfahren.base.dto.UserDto;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.NonNull;

@Route(value = "users", layout = MainView.class)
@PageTitle("User Management")
@CssImport(value = "./styles/views/usermanagement/user-management-view.css", include = "lumo-badge")
@JsModule("@vaadin/vaadin-lumo-styles/badge.js")
public class UserManagementView extends Div {

    private static final long serialVersionUID = 6727792850782049795L;

    private final UserService userService;

    @Autowired
    public UserManagementView(@NonNull final UserService userService) {
        this.userService = userService;
        setId("user-management-view");
        add(createTitle());
        createGrid();
    }

    private H3 createTitle() {
        return new H3("User Management");
    }

    /**
     * Create text field for filtering.
     */
    private TextField createFilter(final ListDataProvider<UserDto> dataProvider, final Grid.Column<UserDto> column, final HeaderRow filterRow) {

        final TextField textField = new TextField();

        textField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(column).setComponent(textField);

        textField.setSizeFull();
        textField.setPlaceholder("Filter");
        return textField;
    }

    /**
     * Creates table with different columns with feature for sorting and filtering.
     */
    private void createGrid() {

        final List<UserDto> userList = getItems();
        final Grid<UserDto> grid = new Grid<>();
        final ListDataProvider<UserDto> dataProvider = new ListDataProvider<>(userList);
        grid.setDataProvider(dataProvider);

        final Grid.Column<UserDto> firstNameColumn = grid.addColumn(UserDto::getFirstName).setSortable(true).setHeader("First Name");

        final Grid.Column<UserDto> lastNameColumn = grid.addColumn(UserDto::getLastName).setSortable(true).setHeader("Last Name");
        final Grid.Column<UserDto> emailColumn = grid.addColumn(userDto -> userDto.getEmail()).setSortable(true).setHeader("Email");
        final Grid.Column<UserDto> createdTimestampColumn = grid.addColumn(userDto -> Helper.convertDate(userDto.getCreatedTimestamp())).setSortable(true)
                .setHeader("Created Timestamp");
        final Grid.Column<UserDto> modifiedTimestampColumn = grid.addColumn(userDto -> Helper.convertDate(userDto.getModifiedTimestamp())).setSortable(true)
                .setHeader("Modified Timestamp");

        final HeaderRow filterRow = grid.appendHeaderRow();
        // First filter
        final TextField firstNameField = createFilter(dataProvider, firstNameColumn, filterRow);
        firstNameField.addValueChangeListener(
                user -> dataProvider.addFilter(userDto -> StringUtils.containsIgnoreCase(userDto.getFirstName(), firstNameField.getValue())));

        // Second filter
        final TextField lastNameField = createFilter(dataProvider, lastNameColumn, filterRow);
        lastNameField.addValueChangeListener(
                user -> dataProvider.addFilter(userDto -> StringUtils.containsIgnoreCase(String.valueOf(userDto.getLastName()), lastNameField.getValue())));

        // Third filter
        final TextField emailField = createFilter(dataProvider, emailColumn, filterRow);
        emailField.addValueChangeListener(user -> dataProvider.addFilter(userDto -> StringUtils.containsIgnoreCase(userDto.getEmail(), emailField.getValue())));

        // Fourth filter
        final TextField createdTimestampField = createFilter(dataProvider, createdTimestampColumn, filterRow);
        createdTimestampField.addValueChangeListener(user -> dataProvider
                .addFilter(userDto -> StringUtils.containsIgnoreCase(Helper.convertDate(userDto.getCreatedTimestamp()), createdTimestampField.getValue())));

        // Fifth filter
        final TextField modifiedTimestampField = createFilter(dataProvider, modifiedTimestampColumn, filterRow);
        modifiedTimestampField.addValueChangeListener(user -> dataProvider
                .addFilter(userDto -> StringUtils.containsIgnoreCase(Helper.convertDate(userDto.getModifiedTimestamp()), modifiedTimestampField.getValue())));

        add(grid);

    }

    private List<UserDto> getItems() {
        return this.userService.getAllUsers();
    }
};
