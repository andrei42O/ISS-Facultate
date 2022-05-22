import Utils.SessionFactoryUtils;
import controllers.ControllerLogin;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import persistance.IRepositoryDBUsers;
import persistance.RepositoryORMUser;
import service.ServiceSecurity;

import java.io.IOException;
import java.util.Properties;

public class Start extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("AirForce");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/login_window.fxml"));
        Pane pane = (Pane) loader.load();
        ControllerLogin loginController = loader.getController();
        loginController.setService(getServiceLogin());
        loginController.setStage(primaryStage);
        loginController.load();
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static ServiceSecurity getServiceLogin(){
        IRepositoryDBUsers repo = new RepositoryORMUser(SessionFactoryUtils.getSessionFactory());
        return new ServiceSecurity(repo);
    }
}
