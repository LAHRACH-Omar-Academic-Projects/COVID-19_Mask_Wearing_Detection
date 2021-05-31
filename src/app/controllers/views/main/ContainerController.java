package app.controllers.views.main;

import app.controllers.views.components.camera.CameraDisplayController;
import app.models.Camera;
import app.services.CameraService;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContainerController {
    public BorderPane container;
    public ComboBox<Camera> cameras;
    public StackPane camerasDisplayPanel;
    public GridPane camerasGridPanel;
    public BorderPane playBtnPanel;
    public ImageView playBtn;
    public CameraService cameraService;
    public List<Camera> camerasList;
    public int camerasCount;

    public ContainerController() {
        this.cameraService = new CameraService();
    }

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            startPlayBtnAnimation();
            collectCamerasInfo();
            cameras.setOnMouseClicked(mouseEvent -> {
                fillCamerasList();
            });
            playBtn.setOnMouseClicked(mouseEvent -> {
                camerasDisplayPanel.getChildren().remove(playBtnPanel);
                displayCameras();
            });
        });
    }

    private void startPlayBtnAnimation() {
        Duration duration = Duration.millis(1000);
        ScaleTransition scaleTransition = new ScaleTransition(duration, playBtn);
        scaleTransition.setByX(0.3);
        scaleTransition.setByY(0.3);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(Animation.INDEFINITE);
        scaleTransition.play();
        playBtn.setOnMouseEntered(mouseEvent -> scaleTransition.pause());
        playBtn.setOnMouseExited(mouseEvent -> scaleTransition.play());
    }

    public void collectCamerasInfo() {
        try {
            List<Camera> camerasList = cameraService.getAllCameras();
            setCamerasList(camerasList);
            setCamerasCount(camerasList.size());
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    private void fillCamerasList() {
        collectCamerasInfo();
        camerasList.add(0, new Camera("Toutes les caméras", "0", 0));
        cameras.setItems(FXCollections.observableArrayList(camerasList));
        cameras.setConverter(new StringConverter<Camera>() {
            @Override
            public String toString(Camera camera) {
                return camera.getName();
            }

            @Override
            public Camera fromString(String string) {
                return null;
            }
        });
        cameras.setOnAction((event) -> {
            Camera camera = cameras.getSelectionModel().getSelectedItem();
            if(camera != null) {
                if(camera.getName().equals("Toutes les caméras")) {
                    displayCameras();
                }
                else {
                    expandCamera(camera);
                }
            }
        });
    }

    public void displayCameras() {
        collectCamerasInfo();
        camerasGridPanel = new GridPane();
        camerasGridPanel.setHgap(10);
        camerasGridPanel.setVgap(10);
        camerasGridPanel.setStyle("-fx-padding: 15");
        container.setCenter(camerasGridPanel);
        List<Integer> shape = getGridShape(getCamerasCount());
        setGridConstraints(shape);
        addCamerasToTheGrid(shape, camerasList);
    }

    public void expandCamera(Camera camera) {
        List<Camera> newList = new ArrayList<>();
        newList.add(camera);
        setCamerasList(newList);
        setCamerasCount(newList.size());
        camerasGridPanel = new GridPane();
        camerasGridPanel.setHgap(10);
        camerasGridPanel.setVgap(10);
        camerasGridPanel.setStyle("-fx-padding: 15");
        container.setCenter(camerasGridPanel);
        List<Integer> shape = getGridShape(getCamerasCount());
        setGridConstraints(shape);
        addCamerasToTheGrid(shape, camerasList);
    }

    public List<Integer> getGridShape(int camerasCount) {
        List<Integer> shape = new ArrayList<>();
        int rows_count = camerasGridPanel.getRowCount() + 1;
        int cols_count = camerasGridPanel.getColumnCount() + 1;
        for (int i = 1; i <= camerasCount; i++) {
            int size = rows_count * cols_count;
            if (size < i) {
                if (rows_count < cols_count) {
                    rows_count++;
                } else if (rows_count == cols_count) {
                    cols_count++;
                }
            }
        }
        shape.add(rows_count);
        shape.add(cols_count);
        return shape;
    }

    public void setGridConstraints(List<Integer> shape) {
        for (int i = 0; i < shape.get(0); i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight((double) 100 / shape.get(0));
            camerasGridPanel.getRowConstraints().add(row);
        }
        for (int i = 0; i < shape.get(1); i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth((double) 100 / shape.get(1));
            camerasGridPanel.getColumnConstraints().add(col);
        }
    }

    public void addCamerasToTheGrid(List<Integer> shape, List<Camera> camerasList) {
        int cameraId = 0;
        for (int i = 0; i < shape.get(0); i++) {
            for (int j = 0; j < shape.get(1); j++) {
                if ((long) camerasGridPanel.getChildren().size() < getCamerasCount()) {
                    CameraDisplayController cameraDisplayController = new CameraDisplayController();
                    cameraDisplayController.addCamera(camerasList.get(cameraId), camerasDisplayPanel, camerasGridPanel, i, j);
                }
                cameraId++;
            }
        }
    }

    public void setCamerasList(List<Camera> camerasList) {
        this.camerasList = camerasList;
    }

    public int getCamerasCount() {
        return camerasCount;
    }

    public void setCamerasCount(int camerasCount) {
        this.camerasCount = camerasCount;
    }
}
