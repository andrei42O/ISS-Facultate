package controllers;

import Utils.PopUP;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Order;
import service.IService;
import service.ServiceAgent;

public class ReceiptController implements IController{
    @FXML
    public TextField addressField;
    @FXML
    public TextField nameField;
    @FXML
    public Button generateReceiptButtonFinal;

    private Stage stage;

    public ReceiptController() {
    }

    private Order currentOrder = null;
    private ServiceAgent service = null;

    void assignSignals(){
        generateReceiptButtonFinal.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String clientName = nameField.getText();
                String address = addressField.getText();
                if(clientName.length() == 0){
                    return;
                }
                if(service.placeOder(currentOrder, clientName, address) != null){
                    PopUP.info("Receipt generated succesfully!");
                }
                else{
                    PopUP.warning("Receipt couldn't be generated...");
                }
                stage.close();
            }
        });
    }

    public void setOrder(Order order) {
        this.currentOrder = order;
    }

    @Override
    public void load() {
        assignSignals();
    }

    @Override
    public void setService(IService service) {
        this.service = (ServiceAgent)service;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
