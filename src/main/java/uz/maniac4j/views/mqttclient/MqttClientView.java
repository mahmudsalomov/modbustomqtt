package uz.maniac4j.views.mqttclient;

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
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
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
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import java.util.List;
import java.util.Optional;
import java.lang.Long;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import uz.maniac4j.data.entity.ModbusClient;
import uz.maniac4j.data.entity.MqttClient;
import uz.maniac4j.data.service.ModbusClientService;
import uz.maniac4j.data.service.MqttClientService;
import uz.maniac4j.views.MainLayout;

@PageTitle("Mqtt Client")
@Route(value = "Mqtt-Client/:mqttClientID?/:action?(edit)", layout = MainLayout.class)
@Uses(Icon.class)
public class MqttClientView extends Div implements BeforeEnterObserver {

    private final String MQTTCLIENT_ID = "mqttClientID";
    private final String MQTTCLIENT_EDIT_ROUTE_TEMPLATE = "Mqtt-Client/%s/edit";

    private Grid<MqttClient> grid = new Grid<>(MqttClient.class, false);

    private TextField name;
//    private TextField modbus;
    private TextField polling;
    private TextField ip;
    private TextField port;
    private TextField topic;
    private Checkbox enable;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private H1 title=new H1("No select");

    private BeanValidationBinder<MqttClient> binder;

    private MqttClient mqttClient;

    private ModbusClient selectedModbusClient;


    private final MqttClientService mqttClientService;
    private final ModbusClientService modbusClientService;


    @Autowired
    public MqttClientView(MqttClientService mqttClientService, ModbusClientService modbusClientService) {
        this.mqttClientService = mqttClientService;
        this.modbusClientService = modbusClientService;
        add(select());
        addClassNames("mqtt-client-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("name").setAutoWidth(true);
//        grid.addColumn("modbus").setAutoWidth(true);
        grid.addColumn("polling").setAutoWidth(true);
        grid.addColumn("ip").setAutoWidth(true);
        grid.addColumn("port").setAutoWidth(true);
        grid.addColumn("topic").setAutoWidth(true);
        grid.addColumn("json").setAutoWidth(true);
        LitRenderer<MqttClient> enableRenderer = LitRenderer.<MqttClient>of(
                "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
                .withProperty("icon", enable -> enable.isEnable() ? "check" : "minus").withProperty("color",
                        enable -> enable.isEnable()
                                ? "var(--lumo-primary-text-color)"
                                : "var(--lumo-disabled-text-color)");

        grid.addColumn(enableRenderer).setHeader("Enable").setAutoWidth(true);

        grid.setItems(query -> mqttClientService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)),selectedModbusClient)
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(MQTTCLIENT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(MqttClientView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(MqttClient.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(polling).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("polling");
        binder.forField(port).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("port");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.mqttClient == null) {
                    this.mqttClient = new MqttClient();
                }

                if (selectedModbusClient==null){
                    Notification.show("Please select modbus client!").addThemeVariants(NotificationVariant.LUMO_ERROR);
                } else {
                    binder.writeBean(this.mqttClient);

                    try {
                        if (this.mqttClient.validCheck()){
                            this.mqttClient.setModbusClient(selectedModbusClient);
                            mqttClientService.update(this.mqttClient);
                            clearForm();
                            refreshGrid();
                            Notification.show("MqttClient details stored.");
                            UI.getCurrent().navigate(MqttClientView.class);
                        }else {
                            Notification.show("Fill in the fields!").addThemeVariants(NotificationVariant.LUMO_ERROR);
                        }
                    }catch (Exception q){
                        Notification.show("Error!").addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }

                }


            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the mqttClient details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> mqttClientId = event.getRouteParameters().get(MQTTCLIENT_ID).map(Long::valueOf);
        if (mqttClientId.isPresent()) {
            Optional<MqttClient> mqttClientFromBackend = mqttClientService.get(mqttClientId.get());
            if (mqttClientFromBackend.isPresent()) {
                populateForm(mqttClientFromBackend.get());
            } else {
                Notification.show(String.format("The requested mqttClient was not found, ID = %s", mqttClientId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(MqttClientView.class);
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
//        modbus = new TextField("Modbus");
        polling = new TextField("Polling");
        ip = new TextField("Ip");
        port = new TextField("Port");
        topic = new TextField("Topic");
        enable = new Checkbox("Enable");
        Component[] fields = new Component[]{name, polling, ip, port, topic, enable};

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

    private void populateForm(MqttClient value) {
        this.mqttClient = value;
        binder.readBean(this.mqttClient);

    }








    //Modbus client select
    public HorizontalLayout select(){

        Button refresh=new Button("Refresh");
        refresh.addClickListener(event -> refreshGrid());

        List<ModbusClient> all = modbusClientService.all();
        Select<ModbusClient> select = new Select<>();
        select.setLabel("Select Modbus client");

        select.setItemLabelGenerator(ModbusClient::getName);

        select.setItems(all);
        select.addValueChangeListener(e->{
            ModbusClient value = e.getValue();
            System.out.println(value);
            selectedModbusClient=value;
            title.setText(selectedModbusClient.getName());
            refreshGrid();
        });




//        select.setItems("Most recent first", "Rating: high to low",
//                "Rating: low to high", "Price: high to low", "Price: low to high");
//        select.setValue("Most recent first");

        HorizontalLayout layout = new HorizontalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);

        layout.add(refresh,select);
        layout.add(new HorizontalLayout(title));


        return layout;
    }
}
