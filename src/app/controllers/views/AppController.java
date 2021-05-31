package app.controllers.views;

import app.controllers.views.main.MainController;
import app.controllers.views.sidebar.SidebarController;
import javafx.application.Platform;
import javafx.fxml.FXML;

public class AppController {

    @FXML SidebarController sidebarController;
    @FXML MainController mainController;

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            mainController.injectController(sidebarController);
            sidebarController.injectController(mainController);
        });
    }
}
