package app.controllers.views.main;

import app.controllers.views.sidebar.SidebarController;
import javafx.application.Platform;
import javafx.fxml.FXML;

public class MainController {

    @FXML HeaderController headerController;
    @FXML ContainerController containerController;

    @FXML
    private void initialize() {
        Platform.runLater(() -> {

        });
    }

    public void injectController(SidebarController sidebarController) {
        headerController.injectControllers(containerController, sidebarController);
    }

    public void refreshCamerasList() {
        containerController.displayCameras();
    }
}
