package uz.maniac4j.views.modbusitem;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
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
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import uz.maniac4j.data.entity.ModbusClient;
import uz.maniac4j.data.entity.ModbusItem;
import uz.maniac4j.data.enums.RegisterType;
import uz.maniac4j.data.enums.RegisterVarType;
import uz.maniac4j.data.service.ModbusClientService;
import uz.maniac4j.data.service.ModbusItemService;
import uz.maniac4j.views.MainLayout;
import uz.maniac4j.views.modbusclient.ModbusClientView;

@PageTitle("Modbus Item")
@Route(value = "Modbus-Item/:modbusItemID?/:action?(edit)", layout = MainLayout.class)
public class ModbusItemView extends Div implements BeforeEnterObserver {

    private final String MODBUSITEM_ID = "modbusItemID";
    private final String MODBUSITEM_EDIT_ROUTE_TEMPLATE = "Modbus-Item/%s/edit";

    private Grid<ModbusItem> grid = new Grid<>(ModbusItem.class, false);

    private TextField tagName;
    private Select<String> register;
    private Select<String> type;
    private TextField address;

    private H1 title=new H1("No select");

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<ModbusItem> binder;

    private ModbusItem modbusItem;

    private ModbusClient selectedModbusClient;

    private final ModbusItemService modbusItemService;
    private final ModbusClientService modbusClientService;

    @Autowired
    public ModbusItemView(ModbusItemService modbusItemService, ModbusClientService modbusClientService) {
        this.modbusItemService = modbusItemService;
        this.modbusClientService = modbusClientService;
        add(select());
        addClassNames("modbus-item-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("tagName").setAutoWidth(true);
        grid.addColumn("register").setAutoWidth(true);
        grid.addColumn("type").setAutoWidth(true);
        grid.addColumn("address").setAutoWidth(true);
        grid.addColumn("value").setAutoWidth(true);
        grid.setItems(query -> modbusItemService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)),selectedModbusClient)
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(MODBUSITEM_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ModbusItemView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(ModbusItem.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(address).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("address");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.modbusItem == null) {
                    this.modbusItem = new ModbusItem();
                }
                binder.writeBean(this.modbusItem);


                if (selectedModbusClient==null){
                    Notification.show("Please select modbus client!").addThemeVariants(NotificationVariant.LUMO_ERROR);
                } else {
                    if (this.modbusItem.validCheck()){
                        this.modbusItem.setModbusClient(selectedModbusClient);
                        modbusItemService.update(this.modbusItem);
                        clearForm();
                        refreshGrid();
                        Notification.show("ModbusItem details stored.");
                        UI.getCurrent().navigate(ModbusItemView.class);
                    }
                    else {
                        Notification.show("Fill in the fields!").addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                }



//
//                modbusItemService.update(this.modbusItem);
//                clearForm();
//                refreshGrid();
//                Notification.show("ModbusItem details stored.");
//                UI.getCurrent().navigate(ModbusItemView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the modbusItem details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> modbusItemId = event.getRouteParameters().get(MODBUSITEM_ID).map(UUID::fromString);
        if (modbusItemId.isPresent()) {
            Optional<ModbusItem> modbusItemFromBackend = modbusItemService.get(modbusItemId.get());
            if (modbusItemFromBackend.isPresent()) {
                populateForm(modbusItemFromBackend.get());
            } else {
                Notification.show(String.format("The requested modbusItem was not found, ID = %s", modbusItemId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ModbusItemView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        register = new Select<>();
        register.setItems(RegisterType.names());
        register.setLabel("Register");
        register.setPlaceholder("Select size");

        register.addValueChangeListener(this::changeType);


        type = new Select<>();
        type.setItems(RegisterVarType.names());
        type.setLabel("Type");
        type.setPlaceholder("Select type");

        FormLayout formLayout = new FormLayout();
        tagName = new TextField("TagName");
//        register = new TextField("Register");
//        type = new TextField("Type");
        address = new TextField("Address");
        Component[] fields = new Component[]{tagName, register, type, address};

        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void changeType(AbstractField.ComponentValueChangeEvent<Select<String>, String> e) {

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

    private void populateForm(ModbusItem value) {
        this.modbusItem = value;
        binder.readBean(this.modbusItem);

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
