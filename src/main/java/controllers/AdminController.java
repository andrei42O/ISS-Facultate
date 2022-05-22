package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Admin;
import model.Agent;
import model.JobType;
import model.User;
import service.IService;
import service.ServiceAdmin;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AdminController implements IController {
    @FXML
    public Button updateButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Button refreshButton;
    @FXML
    public ListView<User> agentsList;
    @FXML
    public TextField usernameField;
    @FXML
    public TextField passwordField;
    @FXML
    public TextField nameField;
    @FXML
    public ComboBox<String> jobComboBox;
    @FXML
    public Button addButton;

    private ServiceAdmin service;
    private ObservableList<String> jobComboBoxObservableList = FXCollections.observableArrayList();
    private ObservableList<User> agentObservableList = FXCollections.observableArrayList();
    private Stage stage = null;

    public void load() {
        loadJobBox();
        loadAgents();
        handleOperation();
    }

    private User getWorker(String username, String password, String name, JobType job){
        return switch (job) {
            case Admin -> new Admin(username, password, name);
            case Agent -> new Agent(username, password, name);
            default -> null;
        };
    }

    private void handleOperation() {
        addButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                String name = nameField.getText();
                System.out.println(jobComboBox.getSelectionModel().getSelectedItem());
                JobType job = JobType.valueOf(jobComboBox.getSelectionModel().getSelectedItem());
                if (username.length() == 0 || password.length() == 0 || name.length() == 0) {
                    System.out.println("Valori eronate!");
                }
                try {
                    if (service.addUser(getWorker(username, password, name, job)) == null) {
                        System.out.println("Salvare esuata! :(");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                usernameField.clear();
                passwordField.clear();
                nameField.clear();
            }
        });

        updateButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (agentsList.getSelectionModel().getSelectedItems().size() == 0) {
                    System.out.println("Please select an agent from the list");
                    return;
                }
                String username = usernameField.getText();
                String password = passwordField.getText();
                String name = nameField.getText();
                JobType job = JobType.valueOf(jobComboBox.getSelectionModel().getSelectedItem());
                if (username.length() == 0 || password.length() == 0 || name.length() == 0) {
                    System.out.println("Valori eronate!");
                }
                User selectedUser = agentsList.getSelectionModel().getSelectedItems().get(0);
                System.out.println(selectedUser);
                try {
                    if (service.updateAgent(getWorker(username, password, name, job), selectedUser.getID()) == null) {
                        System.out.println("Modificare esuata! :(");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                usernameField.clear();
                passwordField.clear();
                nameField.clear();
            }
        });

        deleteButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (agentsList.getSelectionModel().getSelectedItems().size() == 0) {
                    System.out.println("Please select an agent from the list");
                    return;
                }
                User selectedUser = agentsList.getSelectionModel().getSelectedItems().get(0);
                try {
                    User _temp = service.deleteAgent(selectedUser);
                    if(_temp == null){
                        System.out.println("Stergerea a esuat! :(");
                    }

                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        });

        refreshButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                loadAgents();
            }
        });
    }

    private void loadAgents() {
        agentObservableList.clear();
        agentObservableList.addAll(
                StreamSupport.stream(service.getAgents().spliterator(), false)
                        .filter(x -> x.getJob() != JobType.Admin)
                        .collect(Collectors.toList()));
        agentsList.setItems(agentObservableList);
    }

    private void loadJobBox() {
        jobComboBoxObservableList.addAll(Arrays.asList("Agent", "Admin"));
        jobComboBox.setItems(jobComboBoxObservableList);
        jobComboBox.getSelectionModel().select(0);
    }

    @Override
    public void setService(IService service) {
        this.service = (ServiceAdmin)service;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
