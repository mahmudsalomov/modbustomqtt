package uz.maniac4j.views.historyoneitem;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
import java.time.Duration;
import java.util.Optional;
import java.lang.Long;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import uz.maniac4j.data.entity.HistoryOneItem;
import uz.maniac4j.data.service.HistoryOneItemService;
import uz.maniac4j.views.MainLayout;

@PageTitle("History One Item")
@Route(value = "History-One-Item/:historyOneItemID?/:action?(edit)", layout = MainLayout.class)
public class HistoryOneItemView extends Div implements BeforeEnterObserver {

    private final String HISTORYONEITEM_ID = "historyOneItemID";
    private final String HISTORYONEITEM_EDIT_ROUTE_TEMPLATE = "History-One-Item/%s/edit";

    private Grid<HistoryOneItem> grid = new Grid<>(HistoryOneItem.class, false);

    private DateTimePicker date;
    private TextField value;
    private TextField status;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<HistoryOneItem> binder;

    private HistoryOneItem historyOneItem;

    private final HistoryOneItemService historyOneItemService;

    @Autowired
    public HistoryOneItemView(HistoryOneItemService historyOneItemService) {
        this.historyOneItemService = historyOneItemService;
        addClassNames("history-one-item-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("date").setAutoWidth(true);
        grid.addColumn("value").setAutoWidth(true);
        grid.addColumn("status").setAutoWidth(true);
        grid.setItems(query -> historyOneItemService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(HISTORYONEITEM_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(HistoryOneItemView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(HistoryOneItem.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(value).withConverter(new StringToIntegerConverter("Only numbers are allowed")).bind("value");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.historyOneItem == null) {
                    this.historyOneItem = new HistoryOneItem();
                }
                binder.writeBean(this.historyOneItem);

                historyOneItemService.update(this.historyOneItem);
                clearForm();
                refreshGrid();
                Notification.show("HistoryOneItem details stored.");
                UI.getCurrent().navigate(HistoryOneItemView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the historyOneItem details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> historyOneItemId = event.getRouteParameters().get(HISTORYONEITEM_ID).map(Long::valueOf);
        if (historyOneItemId.isPresent()) {
            Optional<HistoryOneItem> historyOneItemFromBackend = historyOneItemService.get(historyOneItemId.get());
            if (historyOneItemFromBackend.isPresent()) {
                populateForm(historyOneItemFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested historyOneItem was not found, ID = %s", historyOneItemId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(HistoryOneItemView.class);
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
        date = new DateTimePicker("Date");
        date.setStep(Duration.ofSeconds(1));
        value = new TextField("Value");
        status = new TextField("Status");
        Component[] fields = new Component[]{date, value, status};

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

    private void populateForm(HistoryOneItem value) {
        this.historyOneItem = value;
        binder.readBean(this.historyOneItem);

    }
}
