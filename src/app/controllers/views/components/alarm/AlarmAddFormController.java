package app.controllers.views.components.alarm;

import app.controllers.db.AlarmController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.*;

public class AlarmAddFormController {
    public TextField nameInput;
    public Button chooseFile;
    public Button addBtn;
    private String alarmPath;
    public String initialDirectory = "";
    private final AlarmController alarmController;

    public AlarmAddFormController() {
        alarmController = new AlarmController();
    }

    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            chooseFile.setOnAction(e -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(
                                new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"),
                                new FileChooser.ExtensionFilter("WAV Files", "*.wav"));
                if(!getInitialDirectory().equals("")) {
                    fileChooser.setInitialDirectory(new File(getInitialDirectory()));
                }
                File selectedFile = fileChooser.showOpenDialog(null);
                if(selectedFile != null) {
                    setAlarmPath(selectedFile.getAbsolutePath());
                    setInitialDirectory(selectedFile.getAbsolutePath());
                }
                else {
                    System.out.println("File is not valid!");
                }
            });

            addBtn.setOnAction(actionEvent -> {
                String alarmName = nameInput.getText();
                String alarmPath = getAlarmPath();
                addAlarm(alarmName, alarmPath);
            });
        });
    }

    private void addAlarm(String alarmName, String alarmPath) {
        alarmController.addAlarm(alarmName, alarmPath);
    }

    public String getAlarmPath() {
        return alarmPath;
    }

    public void setAlarmPath(String alarmPath) {
        this.alarmPath = alarmPath;
    }

    public String getInitialDirectory() {
        return initialDirectory;
    }

    public void setInitialDirectory(String initialDirectory) {
        String[] arrOfStr = initialDirectory.split("\\\\");
        arrOfStr[arrOfStr.length - 1] = "";
        arrOfStr[arrOfStr.length - 1] = "";
        String path = "";
        for (String str: arrOfStr) {
            path += str + "\\";
        }
        this.initialDirectory = path;
    }
}
