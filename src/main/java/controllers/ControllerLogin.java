package controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.JobType;
import model.User;
import persistance.IRepositoryDBUsers;
import persistance.RepositoryDBUsers;
import service.ServiceAdmin;
import service.ServiceSecurity;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class ControllerLogin implements IController{
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button loginButton;

    private Stage stage = null;
    private ServiceSecurity serviceSecurity = null;

    public void setServiceLogin(ServiceSecurity serviceLogin) {
        this.serviceSecurity = serviceLogin;
    }

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
            fileName = "/view/client_window.fxml";
        }
        System.out.println(fileName);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileName));
        Pane pane = null;
        try {
            pane = (Pane) loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(JobType.Admin == user.getJob()) {
            AdminController controller = (AdminController) loader.getController();
            ServiceAdmin service = getServiceAdmin(getProps());
            controller.setService(service);
            controller.load();
        } else{
            IController controller = (IController ) loader.getController();
            ServiceAdmin service = getServiceAdmin(getProps());
            controller.load();
        }
        Scene scene = new Scene(pane);
        Stage _stage = new Stage();
        _stage.setScene(scene);
        _stage.show();
    }

    @Override
    public void load() {
        handleOperation();
    }

    public static ServiceAdmin getServiceAdmin(Properties props){
        IRepositoryDBUsers repo = new RepositoryDBUsers(props);
        return new ServiceAdmin(repo);
    }

    public static Properties getProps(){
        Properties props = new Properties();
        try {
            props.load(ControllerLogin.class.getResourceAsStream("/iss.properties"));
            System.out.println("Props loaded");
            props.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find iss.properties");
            return null;
        }
        return props;
    }
}
