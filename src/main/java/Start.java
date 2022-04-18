import controllers.ControllerLogin;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import persistance.IRepositoryDBUsers;
import persistance.RepositoryDBUsers;
import service.ServiceAdmin;
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
        loginController.setServiceLogin(getServiceLogin(getProps()));
        loginController.setStage(primaryStage);
        loginController.load();
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static Properties getProps(){
        Properties props = new Properties();
        try {
            props.load(Start.class.getResourceAsStream("/iss.properties"));
//            Path currentRelativePath = Paths.get("");
//            String s = currentRelativePath.toAbsolutePath().toString();
//            System.out.println("Current absolute path is: " + s);
            System.out.println("Props loaded");
            props.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find iss.properties");
            return null;
        }
        return props;
    }

    public static ServiceSecurity getServiceLogin(Properties props){
        IRepositoryDBUsers repo = new RepositoryDBUsers(props);
        return new ServiceSecurity(repo);
    }
}
