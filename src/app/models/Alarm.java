package app.models;

import app.controllers.db.AlarmController;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.Serializable;

public class Alarm implements Serializable {
    private int id;
    private String name;
    private String path;
    private boolean selected;

    private final AlarmController alarmController;

    public Alarm() {
        this.alarmController = new AlarmController();
    }

    public Alarm(String name, String path, boolean selected) {
        this.name = name;
        this.path = path;
        this.selected = selected;
        this.alarmController = new AlarmController();
    }

    public void choose() {
        Alarm alarm = alarmController.getSelectedAlarm();
        if(alarm != null) {
            alarmController.unChoose(alarm);
            alarmController.choose(this);
        }
        else {
            alarmController.choose(this);
        }
    }

    public void play(MediaPlayer mediaPlayer) {
        if(!mediaPlayer.getStatus().name().equals("PLAYING")) {
            mediaPlayer.setCycleCount(1000);
            mediaPlayer.play();
        }
    }

    public void stop(MediaPlayer mediaPlayer) {
        mediaPlayer.stop();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", selected=" + selected + '}';
    }
}
