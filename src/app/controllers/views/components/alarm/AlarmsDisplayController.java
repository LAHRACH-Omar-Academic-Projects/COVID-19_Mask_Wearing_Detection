package app.controllers.views.components.alarm;

import app.controllers.db.AlarmController;
import app.models.Alarm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class AlarmsDisplayController {

    private List<Alarm> alarmList;
    private final AlarmController alarmController;

    public AlarmsDisplayController() {
        this.alarmList = new ArrayList<>();
        this.alarmController = new AlarmController();
    }

    public void displayAllAlarms(VBox container) {
        alarmList = alarmController.getAllAlarms();
        ObservableList<Alarm> alarms = FXCollections.observableArrayList(alarmList);

        ListView<Alarm> listView = new ListView<>(alarms);
        listView.setMaxSize(230, 160);
        listView.setFocusTraversable( false );

        container.getChildren().add(listView);
        createAlarmItems(listView);
    }

    public void createAlarmItems(ListView<Alarm> listView) {
        ToggleGroup group = new ToggleGroup();
        listView.setCellFactory(new AlarmCellFactory(group));
    }
}
