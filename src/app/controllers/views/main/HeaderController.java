package app.controllers.views.main;

import app.controllers.db.CameraController;
import app.controllers.views.sidebar.SidebarController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class HeaderController {
    public TextField nameInput;
    public TextField ipAddressInput;
    public TextField portInput;
    public Button addBtn;

    private final CameraController cameraController;

    private ContainerController containerController;
    private SidebarController sidebarController;

    public HeaderController() {
        cameraController = new CameraController();
    }

    @FXML
    private void initialize() {
        Platform.runLater(() ->
            addBtn.setOnMouseClicked(mouseEvent -> {
                String camera_name = nameInput.getText();
                String camera_ip_address = ipAddressInput.getText();
                String camera_port = portInput.getText();
                boolean success = cameraController.addCamera(camera_name, camera_ip_address, camera_port);
                if (success) {
                    containerController.displayCameras();
                    sidebarController.refreshCamerasList();
                }
            })
        );
    }

    public void injectControllers(ContainerController containerController, SidebarController sidebarController) {
        this.containerController = containerController;
        this.sidebarController = sidebarController;
    }
}
