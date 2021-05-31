package app.controllers.views.components.alarm;

import app.models.Alarm;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleGroup;
import javafx.util.Callback;

public class AlarmCellFactory implements Callback<ListView<Alarm>, ListCell<Alarm>> {
    private ToggleGroup group;

    public AlarmCellFactory(ToggleGroup group) {
        this.group = group;
    }

    @Override
    public ListCell<Alarm> call(ListView<Alarm> param) {
        return new AlarmCell(group);
    }

    public ToggleGroup getGroup() {
        return group;
    }

    public void setGroup(ToggleGroup group) {
        this.group = group;
    }
}
