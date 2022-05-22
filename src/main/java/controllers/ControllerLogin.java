package controllers;

import Utils.SessionFactoryUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Agent;
import model.JobType;
import model.User;
import persistance.*;
import service.IService;
import service.ServiceAdmin;
import service.ServiceAgent;
import service.ServiceSecurity;

import java.io.IOException;

public class ControllerLogin implements IController{
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button loginButton;

    private Stage stage = null;
    private ServiceSecurity serviceSecurity = null;


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void handleOperation(){
        loginButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                try{
                    User user = serviceSecurity.login(username, password);
                    if(user != null){
                        loadTerminal(user);
                    }
                    stage.close();
                } catch(Exception e){
                    e.printStackTrace();
                }
                usernameField.clear();
                passwordField.clear();
            }
        });
    }

    private void loadTerminal(User user) {
        String fileName = "";
        if(user.getJob() == JobType.Admin){
            fileName = "/view/admin_window.fxml";
        }
        else if(user.getJob() == JobType.Agent)
        {
            fileName = "/view/agent_window.fxml";
        }
        System.out.println(fileName);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileName));
        Pane pane = null;
        try {
            pane = (Pane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        IController controller = (IController) loader.getController();
        switch (user.getJob()) {
            case Admin -> controller.setService(getServiceAdmin());
            case Agent -> controller.setService(getServiceAgent((Agent)user));
            default -> throw new RuntimeException(String.format("Unknown user type: %s", user.getJob()));
        }
        Scene scene = new Scene(pane);
        Stage _stage = new Stage();
        _stage.setScene(scene);
        _stage.show();
        controller.setStage(_stage);
        controller.load();
    }

    private ServiceAgent getServiceAgent(Agent agent) {
        IRepositoryProduct repoProduct = new RepositoryORMProduct(SessionFactoryUtils.getSessionFactory());
        IRepositoryOrder repoOrder = new RepositoryORMOrder(SessionFactoryUtils.getSessionFactory());
        IRepositoryReceipt repoReceipt = new RepositoryORMReceipt(SessionFactoryUtils.getSessionFactory());
        ServiceAgent serviceAdmin = new ServiceAgent();
        serviceAdmin.setRepositoryProduct(repoProduct);
        serviceAdmin.setRepositoryOrder(repoOrder);
        serviceAdmin.setRepositoryReceipt(repoReceipt);
        serviceAdmin.setLoggedAgent(agent);
        return serviceAdmin;
    }

    @Override
    public void load() {
        handleOperation();
    }

    @Override
    public void setService(IService service) {
        this.serviceSecurity = (ServiceSecurity) service;
    }

    public static ServiceAdmin getServiceAdmin(){
        IRepositoryDBUsers repo = new RepositoryORMUser(SessionFactoryUtils.getSessionFactory());
        return new ServiceAdmin(repo);
    }
}
