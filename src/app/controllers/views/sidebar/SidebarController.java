package app.controllers.views.sidebar;

import app.controllers.views.main.MainController;
import javafx.application.Platform;
import javafx.fxml.FXML;

public class SidebarController {

    @FXML NavbarController navbarController;

    private void initialize() {
        Platform.runLater(() -> {

        });
    }

    public void refreshCamerasList() {
        navbarController.refreshCamerasList();
    }

    public void injectController(MainController mainController) {
        navbarController.injectController(mainController);
    }
}
