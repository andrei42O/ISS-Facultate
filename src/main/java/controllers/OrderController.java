package controllers;

import Utils.PopUP;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import model.Order;
import model.OrderProduct;
import model.Product;
import model.Receipt;
import observer.IObserver;
import service.IService;
import service.ServiceAgent;

import java.awt.*;
import observer.Event;
import java.io.IOException;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class OrderController implements IController, IObserver<Event> {

    @FXML
    public ListView<Product> productsListView;
    @FXML
    public ListView<OrderProduct> orderContentListView;
    @FXML
    public Label productNameLabel;
    @FXML
    public Label stockLabel;
    @FXML
    public TextField quantityField;
    @FXML
    public Button modifyQuantityButton;
    @FXML
    public TextArea descriptionTextArea;
    @FXML
    public Label errorLabel;
    @FXML
    public Button generateReceiptButton;
    @FXML
    private Label orderDetailsLabel;

    private Order currentOrder = null;
    private ObservableList<Product> productsList = FXCollections.observableArrayList();
    private ObservableList<OrderProduct> orderContentList = FXCollections.observableArrayList();
    private ServiceAgent service = null;
    private Product selectedProduct = null;
    private Stage stage = null;

    public OrderController() {
    }

    public OrderController(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public void setOrder(Order order) {
        this.currentOrder = order;
    }

    @Override
    public void load() {
        loadConstraints();
        assignSignals();
        initializeProductList();
        initializeOrderContentListList();
        loadOrderDetails();
    }

    private void loadOrderDetails() {
        orderDetailsLabel.setText(String.format("Order #%s", currentOrder.getID()));
    }

    private void loadConstraints() {
        initializeQuantityField();
    }

    private void initializeQuantityField() {
        quantityField.setTextFormatter(
                new TextFormatter<Integer>(
                    new IntegerStringConverter(),
                    1,
                    c -> Pattern.matches("\\d*", c.getText()) ? c : null )
        );
    }

    private void initializeOrderContentListList() {
        loadOrderContentList(currentOrder.getProducts());
        orderContentListView.setItems(orderContentList);
    }

    private void loadOrderContentList(Set<OrderProduct> products) {
        orderContentList.clear();
        orderContentList.addAll(products);
    }

    private void initializeProductList() {
        loadProductsList(service.getAllProducts());
        productsListView.setItems(productsList);
    }

    private void loadProductsList(Iterable<Product> products) {
        productsList.clear();
        products.forEach(p -> {
            if(p.getStock() != 0)
                productsList.add(p);
        });
    }

    private void assignSignals() {
        modifyQuantityButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                Long quantity = null;
                try{
                    quantity = Long.parseLong(quantityField.getText());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid quantity");
                }
                if(quantity == null){
                    errorLabel.setText("Introduceti o cantitate!");
                    errorLabel.setVisible(true);
                    return;
                }
                if(quantity > service.searchProduct(selectedProduct.getID()).getStock()){
                    errorLabel.setText("Cantitate indisponibila pe stock!");
                    errorLabel.setVisible(true);
                    return;
                }
                OrderProduct orderProduct = new OrderProduct(selectedProduct, quantity);
                currentOrder.modifyQuantity(orderProduct);
                if(service.updateOrder(currentOrder) != null){
                    orderContentList.remove(orderProduct);
                    orderContentList.add(orderProduct);
                    errorLabel.setVisible(false);
                    errorLabel.setText(null);
                }
            }
        });
        productsListView.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                if(productsListView.getSelectionModel().getSelectedItems().size() == 1)
                {
                    selectedProduct =  productsListView.getSelectionModel().getSelectedItems().get(0);
                    displayProduct(selectedProduct);
                }
            }
        });
        orderContentListView.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.SECONDARY) {
                    loadMenuBar(MouseInfo.getPointerInfo().getLocation().getX(), MouseInfo.getPointerInfo().getLocation().getY());
                    return;
                }
                if(orderContentListView.getSelectionModel().getSelectedItems().size() == 1)
                {
                    selectedProduct =  orderContentListView.getSelectionModel().getSelectedItems().get(0).getProduct();
                    displayProduct(selectedProduct);
                }
            }
        });

        generateReceiptButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                loadReceiptWindow();
            }
        });
    }

    private void loadReceiptWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/receipt_window.fxml"));
        Pane pane = null;
        try {
            pane = (Pane) loader.load() ;
        } catch (IOException e) {
            e.printStackTrace();
        }
        ReceiptController controller = (ReceiptController) loader.getController();
        controller.setService(service);
        controller.setOrder(currentOrder);
        controller.load();
        Scene scene = new Scene(pane);
        Stage _stage = new Stage();
        controller.setStage(_stage);
        _stage.setScene(scene);
        _stage.show();
    }

    private void loadMenuBar(double x, double y) {
        ContextMenu menu = new ContextMenu();
        MenuItem removeOption = new MenuItem("Remove");
        menu.getItems().add(removeOption);
        removeOption.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(!orderContentListView.getSelectionModel().getSelectedItems().isEmpty()){
                    OrderProduct item = orderContentListView.getSelectionModel().getSelectedItem();
                    item.setQuantity(0L);
                    currentOrder.modifyQuantity(item);
                    if(service.updateOrder(currentOrder) != null)
                        orderContentList.remove(item);
                }
            }
        });
        menu.show(stage, x, y);
    }

    private void displayProduct(Product selectedProduct) {
        productNameLabel.setText(selectedProduct.getName());
        stockLabel.setText(service.searchProduct(selectedProduct.getID()).getStock().toString());
        descriptionTextArea.setText(selectedProduct.getDetails());
    }

    @Override
    public void setService(IService service) {
        if(service instanceof ServiceAgent) {
            this.service = (ServiceAgent) service;
            this.service.add(this);
            return;
        }
        throw new RuntimeException("The service provided doesn't respect the requirements");
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void update(Event arg) {
        this.service.remove(this);
        this.stage.close();
    }
}
