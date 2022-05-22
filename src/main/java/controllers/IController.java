package controllers;

import javafx.stage.Stage;
import service.IService;

public interface IController {
    void load();
    void setService(IService service);
    void setStage(Stage stage);
}
