package uz.maniac4j.views.modbusclient;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import uz.maniac4j.data.entity.ModbusClient;
import uz.maniac4j.data.service.ModbusClientService;
import uz.maniac4j.views.MainLayout;
//@org.springframework.stereotype.Component
@PageTitle("Modbus Client")
@Route(value = "Modbus-Client/:modbusClientID?/:action?(edit)", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@Uses(Icon.class)
public class ModbusClientView extends Div implements BeforeEnterObserver {

    private final String MODBUSCLIENT_ID = "modbusClientID";
    private final String MODBUSCLIENT_EDIT_ROUTE_TEMPLATE = "Modbus-Client/%s/edit";

    private Grid<ModbusClient> grid = new Grid<>(ModbusClient.class, false);

    private TextField name;
    private TextField ip;
    private TextField port;
    private TextField polling;
    private TextField slaveId;
    private Checkbox enable;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<ModbusClient> binder;

    private ModbusClient modbusClient;

    private final ModbusClientService modbusClientService;

    @Autowired
    public ModbusClientView(ModbusClientService modbusClientService) {

//        name.setRequired(true);
//        ip.setRequired(true);
//        port.setRequired(true);
//        polling.setRequired(true);
//        slaveId.setRequired(true);

        this.modbusClientService = modbusClientService;
        addClassNames("modbus-client-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("name").setAutoWidth(true);
        grid.addColumn("ip").setAutoWidth(true);
        grid.addColumn("port").setAutoWidth(true);
        grid.addColumn("polling").setAutoWidth(true);
        grid.addColumn("slaveId").setAutoWidth(true);
        LitRenderer<ModbusClient> enableRenderer = LitRenderer.<ModbusClient>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", enable -> enable.isEnable() ? "check" : "minus").withProperty("color",
                        enable -> enable.isEnable()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(enableRenderer).setHeader("Enable").setAutoWidth(true);

        grid.setItems(query -> modbusClientService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(MODBUSCLIENT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ModbusClientView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(ModbusClient.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(port).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("port");
        binder.forField(polling).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("polling");
        binder.forField(slaveId).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("slaveId");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.modbusClient == null) {
                    this.modbusClient = new ModbusClient();
                }
                binder.writeBean(this.modbusClient);

                if (modbusClient.validCheck()){
                    modbusClientService.update(this.modbusClient);
                    clearForm();
                    refreshGrid();
                    Notification.show("ModbusClient details stored.");
                    UI.getCurrent().navigate(ModbusClientView.class);
                }
                else {
                    Notification.show("Fill in the fields!").addThemeVariants(NotificationVariant.LUMO_ERROR);
                }

            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the modbusClient details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> modbusClientId = event.getRouteParameters().get(MODBUSCLIENT_ID).map(UUID::fromString);
        if (modbusClientId.isPresent()) {
            Optional<ModbusClient> modbusClientFromBackend = modbusClientService.get(modbusClientId.get());
            if (modbusClientFromBackend.isPresent()) {
                populateForm(modbusClientFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested modbusClient was not found, ID = %s", modbusClientId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ModbusClientView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        name = new TextField("Name");
        ip = new TextField("Ip");
        port = new TextField("Port");
        polling = new TextField("Polling");
        slaveId = new TextField("Slave Id");
        enable = new Checkbox("Enable");
        Component[] fields = new Component[]{name, ip, port, polling, slaveId, enable};

        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getLazyDataView().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(ModbusClient value) {
        this.modbusClient = value;
        binder.readBean(this.modbusClient);

    }
}
