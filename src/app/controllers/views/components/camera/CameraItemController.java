package app.controllers.views.components.camera;

import app.controllers.db.CameraController;
import app.controllers.views.main.MainController;
import app.models.Camera;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CameraItemController {
    public Label cameraName;
    public HBox deleteContainer;
    public HBox editContainer;
    public VBox editForm;
    public TextField nameInput;
    public TextField ipAddressInput;
    public TextField portInput;
    public long itemsCount;
    public Button editBtn;

    public boolean extended = false;
    public int selectedItem = 0;

    private final CameraController cameraController;

    public CameraItemController() {
        this.cameraController = new CameraController();
    }

    private VBox loadElements() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(new URL("file:///C:\\Users\\Lahrach Omar\\Downloads\\Work space\\Education\\S4\\PFA\\Je-te-surveille\\src\\app\\views\\components\\camera\\cameraItem.fxml"));
        VBox item = loader.<VBox>load();
        item.getStylesheets().add(getClass().getResource("../../../../assets/styles/components/camera/cameraItem.css").toExternalForm());
        cameraName = (Label) loader.getNamespace().get("cameraName");
        deleteContainer = (HBox) loader.getNamespace().get("deleteContainer");
        editContainer = (HBox) loader.getNamespace().get("editContainer");
        return item;
    }

    public void createCameraItem(MainController mainController, List<Camera> cameraList, VBox container, ScrollPane scrollPane) {
        for (Camera camera : cameraList) {
            try {
                VBox item = loadElements();
                cameraName.setText(camera.getName());
                container.getChildren().add(item);

                deleteContainer.setOnMouseClicked(mouseEvent -> {
                    deleteCameraItem(mainController, camera, container, item);
                });

                editContainer.setOnMouseClicked(mouseEvent -> {
                    try {
                        int itemIndex = container.getChildren().indexOf(item);
                        setItemsCount(container.getChildren().stream().count());
                        if(!(itemIndex == selectedItem)) {
                            setExtended(false);
                        }
                        createEditForm(item, container, itemIndex);
                        setSelectedItem(itemIndex);
                        setExtended(!extended);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    nameInput.setText(camera.getName());
                    ipAddressInput.setText(camera.getIpAddress());
                    portInput.setText(String.valueOf(camera.getPort()));
                    editBtn.setOnMouseClicked(mouseEvent1 -> {
                        List<String> newValues = new ArrayList<>();
                        newValues.add(nameInput.getText());
                        newValues.add(ipAddressInput.getText());
                        newValues.add(portInput.getText());
                        updateCameraItem(mainController, camera, newValues, scrollPane, container);
                    });
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createEditForm(VBox item, VBox container, int index) throws IOException {
        if (selectedItem == index && extended) {
            if(item.getChildren().size() == 2) {
                item.getChildren().remove(1);
            }
        }
        else {
            for(int i = 0; i < getItemsCount(); i++) {
                VBox node = (VBox) container.getChildren().get(i);
                if(i != index && node.getChildren().stream().count() == 2) {
                    node.getChildren().remove(1);
                }
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new URL("file:///C:\\Users\\Lahrach Omar\\Downloads\\Work space\\Education\\S4\\PFA\\Je-te-surveille\\src\\app\\views\\components\\camera\\cameraEditForm.fxml"));
            editForm = loader.<VBox>load();
            editForm.getStylesheets().add(getClass().getResource("../../../../assets/styles/components/camera/cameraEditForm.css").toExternalForm());
            nameInput = (TextField) loader.getNamespace().get("nameInput");
            ipAddressInput = (TextField) loader.getNamespace().get("ipAddressInput");
            portInput = (TextField) loader.getNamespace().get("portInput");
            editBtn = (Button) loader.getNamespace().get("editBtn");
            item.getChildren().add(editForm);
        }
    }

    private void updateCameraItem(MainController mainController, Camera camera, List<String> newValues, ScrollPane scrollPane, VBox container) {
        cameraController.updateCamera(camera, newValues);
        container.getChildren().clear();
        List<Camera> camerasList = cameraController.getAllCameras();
        createCameraItem(mainController, camerasList, container, scrollPane);
        scrollPane.setContent(container);
    }

    private void deleteCameraItem(MainController mainController, Camera camera, VBox container, VBox item) {
        Alert a = new Alert(Alert.AlertType.NONE);
        a.setAlertType(Alert.AlertType.CONFIRMATION);
        Optional<ButtonType> result = a.showAndWait();
        if (result.get() == ButtonType.OK) {
            cameraController.deleteCamera(camera.getId());
            mainController.refreshCamerasList();
        }
        container.getChildren().remove(item);
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    public long getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(long itemsCount) {
        this.itemsCount = itemsCount;
    }
}
