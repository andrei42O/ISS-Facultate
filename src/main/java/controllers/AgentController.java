package controllers;

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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.*;
import observer.IObserver;
import observer.ProductQuantityUpdated;
import observer.ReceiptGenerated;
import service.IService;
import service.ServiceAgent;


import java.awt.*;
import observer.Event;
import java.io.IOException;

public class AgentController implements IController, IObserver<Event> {
    @FXML
    public Button createOrderButton;
    @FXML
    public Label agentLoggedLabel;
    @FXML
    public ListView<Product> productsListView;
    @FXML
    public ListView<Order> ordersListView;
    private String orderWindowFile = "/view/comanda_window.fxml";

    private ObservableList<Product> productsList = FXCollections.observableArrayList();;
    private ObservableList<Order> ordersList = FXCollections.observableArrayList();

    private String agentDisplayFormat = "Logged as: %s #%s";
    private ServiceAgent service;
    private Stage stage = null;

    @Override
    public void load() {
        assignSignals();
        initializeProducts();
        initializeOrders();
        loadAgentDetails();
    }

    private void loadAgentDetails() {
        agentLoggedLabel.setText(String.format(agentDisplayFormat, service.getLoggedAgent().getName(),service.getLoggedAgent().getID()));
    }

    private void initializeOrders() {
        loadOrders(service.getOrders());
        ordersListView.setItems(ordersList);
    }

    private void initializeProducts() {
        loadProducts(service.getAllProducts());
        productsListView.setItems(productsList);
    }

    private void loadOrders(Iterable<Order> orders) {
        ordersList.clear();
        orders.forEach((order) -> {
            if(!order.getPlaced()){
                ordersList.add(order);
            }
        });
    }

    private void loadProducts(Iterable<Product> products) {
        productsList.clear();
        products.forEach(p -> {
            if(p.getStock() != 0)
                productsList.add(p);
        });
    }


    @Override
    public void setService(IService service) {
        if(service instanceof ServiceAgent) {
            this.service = (ServiceAgent) service;
            this.service.add(this);
            return;
        }
        throw new RuntimeException("The service provided doesn't fulfill the requirements");
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void assignSignals() {
        createOrderButton.setOnAction(new EventHandler<>(){
            @Override
            public void handle(ActionEvent event) {
                Order order = service.createOrder();
                addOrderToList(order);
                // openOrderWindow(order);
            }
        });

        ordersListView.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.SECONDARY) {
                    loadMenuBar(MouseInfo.getPointerInfo().getLocation().getX(), MouseInfo.getPointerInfo().getLocation().getY());
                    return;
                }
                if( event.getButton().equals(MouseButton.PRIMARY) &&
                    event.getClickCount() == 2 &&
                    ordersListView.getSelectionModel().getSelectedItems().isEmpty())
                    return;
                openOrderWindow(ordersListView
                        .getSelectionModel()
                        .getSelectedItems()
                        .get(0));
            }
        });
    }

    private void openOrderWindow(Order order) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(this.orderWindowFile));
        Pane pane = null;
        try {
            pane = (Pane) loader.load() ;
        } catch (IOException e) {
            e.printStackTrace();
        }
        OrderController controller = (OrderController) loader.getController();
        controller.setService(service);
        controller.setOrder(order);
        controller.load();
        Scene scene = new Scene(pane);
        Stage _stage = new Stage();
        controller.setStage(_stage);
        _stage.setScene(scene);
        _stage.show();
    }

    private void addOrderToList(Order order) {
        ordersList.add(order);
    }

    private void loadMenuBar(double x, double y) {
        ContextMenu menu = new ContextMenu();
        MenuItem removeOption = new MenuItem("Remove");
        menu.getItems().add(removeOption);
        System.out.println("Mno");
        removeOption.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                if(!ordersListView.getSelectionModel().getSelectedItems().isEmpty()){
                    var item = ordersListView.getSelectionModel().getSelectedItem();
                    if(service.deleteOrder(item) != null) {
                        ordersList.remove(item);
                    }
                }
            }
        });
        menu.show(stage, x, y);
    }

    @Override
    public void update(Event arg) {
        if(arg instanceof ReceiptGenerated)
            ordersList.remove(((Receipt)arg.getObject()).getOrder());
        if(arg instanceof ProductQuantityUpdated) {
            productsList.remove((Product) arg.getObject());
            if(((Product) arg.getObject()).getStock() != 0)
                productsList.add((Product) arg.getObject());
        }
    }
}
