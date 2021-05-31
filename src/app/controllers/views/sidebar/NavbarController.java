package app.controllers.views.sidebar;

import app.controllers.db.AlarmController;
import app.controllers.db.CameraController;
import app.controllers.views.components.alarm.AlarmAddFormController;
import app.controllers.views.components.alarm.AlarmsDisplayController;
import app.controllers.views.components.camera.CameraItemController;
import app.controllers.views.components.person.PersonsDisplayController;
import app.controllers.views.main.MainController;
import app.models.Alarm;
import app.models.Camera;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NavbarController {
    public VBox container0;
    public VBox container1;
    public VBox container2;

    public HBox item0;
    public HBox item1;
    public HBox item2;

    public VBox alarmAddIcon;

    public boolean extended = false;
    public int selectedItem = 0;

    public static int DETECTED_PERSONS = 0;
    public static int CAMERAS = 1;
    public static int ALARMS = 2;

    public List<VBox> containers;
    public List<HBox> items;
    public List<Integer> lists;

    public String CAMERAS_NOT_FOUND =
            "Aucune caméra trouvée!, " +
                    "Essayer d'ajouter une à l'aide" +
                    " de la formulaire ci-dessus";

    public ScrollPane scrollPane;
    public VBox listContainer;

    public CameraController cameraController;
    public AlarmController alarmController;
    public CameraItemController cameraItemController;
    private MainController mainController;
    private AlarmsDisplayController alarmsDisplayController;
    private PersonsDisplayController personsDisplayController;

    public NavbarController() {
        this.cameraController = new CameraController();
        this.alarmController = new AlarmController();
        this.cameraItemController = new CameraItemController();
        this.alarmsDisplayController = new AlarmsDisplayController();
        this.personsDisplayController = new PersonsDisplayController();
    }

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            containers = new ArrayList<>() {{
                add(container0);
                add(container1);
                add(container2);
            }};
            items = new ArrayList<>() {{
                add(item0);
                add(item1);
                add(item2);
            }};
            lists = new ArrayList<>() {{
                add(DETECTED_PERSONS);
                add(CAMERAS);
                add(ALARMS);
            }};

            for (HBox item : items) {
                item.setOnMouseClicked(mouseEvent -> {
                    if(!(items.indexOf(item) == selectedItem)) {
                        setExtended(false);
                    }
                    activateItem(items.indexOf(item));
                    setSelectedItem(items.indexOf(item));
                    setExtended(!extended);
                });
            }

            alarmAddIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(new URL("file:///C:\\Users\\Lahrach Omar\\Downloads\\Work space\\Education\\S4\\PFA\\Je-te-surveille\\src\\app\\views\\components\\alarm\\alarmAddForm.fxml"));
                        VBox item = loader.<VBox>load();
                        item.getStylesheets().add(getClass().getResource("../../../assets/styles/components/alarm/alarmAddForm.css").toExternalForm());
                        Stage stage = new Stage();
                        stage.setResizable(false);
                        stage.setTitle("Nouvelle alarme");
                        stage.setScene(new Scene(item, 300, 250));
                        stage.show();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
    }

    public void activateItem(int index) {
        if (selectedItem == index && extended) {
            for (VBox container : containers) {
                if (container == containers.get(index)) {
                    container.getChildren().remove(scrollPane);
                    container.getChildren().remove(listContainer);
                }
            }
        } else {
            for (HBox item : items) {
                if (item == items.get(index)) {
                    item.setStyle("-fx-background-color: #efefef");
                } else {
                    item.setStyle("-fx-background-color: #FFFFFF");
                }
            }

            for (VBox container : containers) {
                if (!(container == containers.get(index))) {
                    container.getChildren().remove(scrollPane);
                    container.getChildren().remove(listContainer);
                }
            }

            for (VBox container : containers) {
                if (container == containers.get(index)) {
                    showListOf(index, container);
                }
            }
        }
    }

    public void showListOf(int list, VBox container) {
        scrollPane = new ScrollPane();
        listContainer = new VBox();
        listContainer.setStyle(
                "-fx-background-color:  #FFFFFF;" +
                        " -fx-spacing: 2px;" +
                        " -fx-pref-width: 238;" +
                        " -fx-pref-height: 200;"
        );
        if (list == DETECTED_PERSONS) {
            personsDisplayController.displayAllPersons(listContainer);
            container.getChildren().add(listContainer);
        }
        else if (list == CAMERAS) {
            List<Camera> camerasList = cameraController.getAllCameras();
            if (!camerasList.isEmpty()) {
                cameraItemController.createCameraItem(mainController, camerasList, listContainer, scrollPane);
            } else {
                Label message = new Label(CAMERAS_NOT_FOUND);
                listContainer.getChildren().add(message);
            }
            container.getChildren().add(scrollPane);
            scrollPane.setContent(listContainer);
        }
        else if(list == ALARMS) {
            alarmsDisplayController.displayAllAlarms(listContainer);
            container.getChildren().add(listContainer);
        }
    }

    public void refreshCamerasList() {
        listContainer.getChildren().clear();
        List<Camera> camerasList = cameraController.getAllCameras();
        cameraItemController.createCameraItem(mainController, camerasList, listContainer, scrollPane);
        scrollPane.setContent(listContainer);
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }

    public void injectController(MainController mainController) {
        this.mainController = mainController;
    }
}
