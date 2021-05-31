package app.controllers.views.components.alarm;

import app.controllers.db.AlarmController;
import app.models.Alarm;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;

public class AlarmCell extends ListCell<Alarm> {

    @FXML
    private Label alarmName;

    @FXML
    private AnchorPane playBtn;

    @FXML
    private ImageView playImage;

    @FXML
    private ImageView pauseImage;

    @FXML
    private HBox deleteBtn;

    @FXML
    private RadioButton radioBtn;

    private boolean clicked;

    private ToggleGroup group;

    private MediaPlayer mediaPlayer;

    private AlarmController alarmController;

    public AlarmCell(ToggleGroup group) {
        this.group = group;
        loadFXML();
        this.alarmController = new AlarmController();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../../views/components/alarm/alarmCell.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(Alarm alarm, boolean empty) {
        super.updateItem(alarm, empty);

        if(empty || alarm == null) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        else {
            alarmName.setText(alarm.getName());
            Media sound = new Media(new File(alarm.getPath()).toURI().toString());
            mediaPlayer = new MediaPlayer(sound);
            playBtn.setOnMouseClicked(mouseEvent -> {
                setClicked(!isClicked());
                if(clicked) {
                    alarm.play(mediaPlayer);
                    playImage.setVisible(false);
                    pauseImage.setVisible(true);
                }
                else {
                    alarm.stop(mediaPlayer);
                    playImage.setVisible(true);
                    pauseImage.setVisible(false);
                }
            });

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            radioBtn.setToggleGroup(group);

            if (alarm.isSelected()) {
                radioBtn.setSelected(true);
            }

            radioBtn.setOnAction(actionEvent -> alarm.choose());
            deleteBtn.setOnMouseClicked(mouseEvent -> {
                alarmController.deleteAlarm(alarm.getId());
                this.getListView().refresh();
            });
        }
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
}