package app.controllers.db;

import app.models.Alarm;
import app.services.AlarmServices;
import app.utils.Utils;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.util.List;

public class AlarmController {
    private final AlarmServices alarmServices;

    public AlarmController() {
        alarmServices = new AlarmServices();
    }

    public void addAlarm(String alarm_name, String alarm_path) {
        if (!alarm_name.equals("") && !alarm_path.equals("")) {
            Alarm alarm = new Alarm(alarm_name, alarm_path, false);
            try {
                if (!alarmServices.alarmExists(alarm.getPath())) {
                    int alarmId = alarmServices.addAlarm(alarm);
                    if (alarmId > 0) {
                        Utils.alert("Sauvegarder", "L'alarme bien été ajoutée!", Alert.AlertType.INFORMATION);
                    } else {
                        Utils.alert("Erreur", "Manquée!", Alert.AlertType.ERROR);
                    }
                } else {
                    Utils.alert("Erreur", "L'alarme existe déjà!", Alert.AlertType.ERROR);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            Utils.alert("Erreur", "Veuillez remplir les champs!", Alert.AlertType.ERROR);
        }
    }

    public List<Alarm> getAllAlarms() {
        try {
            return alarmServices.getAllAlarms();
        } catch (SQLException throwable) {
            Utils.alert("Problème", "Nous ne pouvons pas se connecter au serveur!", Alert.AlertType.ERROR);
            return null;
        }
    }

    public void deleteAlarm(int id) {
        try {
            boolean deleted = alarmServices.deleteAlarm(id);
            if (deleted) {
                Utils.alert("Suppression", "L'alarme a bien été supprimée!", Alert.AlertType.INFORMATION);
            } else {
                Utils.alert("Erreur", "Problème lors de la suppression", Alert.AlertType.ERROR);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void choose(Alarm alarm) {
        try {
            alarm.setSelected(true);
            Alarm updated = alarmServices.updateAlarm(alarm);
            if (updated == null) {
                Utils.alert("Erreur", "Problème lors de la sélection", Alert.AlertType.ERROR);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void unChoose(Alarm alarm) {
        try {
            alarm.setSelected(false);
            Alarm updated = alarmServices.updateAlarm(alarm);
            if (updated == null) {
                Utils.alert("Erreur", "Problème lors de la sélection", Alert.AlertType.ERROR);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public Alarm getSelectedAlarm() {
        Alarm selected = null;
        try {
            selected = alarmServices.getSelectedAlarm();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return selected;
    }
}
