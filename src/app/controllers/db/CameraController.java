package app.controllers.db;

import app.models.Camera;
import app.services.CameraService;
import app.utils.Utils;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.util.List;

public class CameraController {
    private final CameraService cameraService;

    public CameraController() {
        cameraService = new CameraService();
    }

    public boolean addCamera(String camera_name, String camera_ip_address, String camera_port) {
        if (!camera_name.equals("") && !camera_ip_address.equals("") && !camera_port.equals("")) {
            Camera camera = new Camera(camera_name, camera_ip_address, Integer.parseInt(camera_port));
            try {
                if (!cameraService.cameraExists(camera.getIpAddress())) {
                    int cameraId = cameraService.addCamera(camera);
                    if (cameraId > 0) {
                        Utils.alert("Sauvegarder", "La caméra a bien été ajoutée!", Alert.AlertType.INFORMATION);
                        return true;
                    } else {
                        Utils.alert("Erreur", "Manquée!", Alert.AlertType.ERROR);
                        return false;
                    }
                } else {
                    Utils.alert("Erreur", "La caméra existe déjà!", Alert.AlertType.ERROR);
                    return false;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
        } else {
            Utils.alert("Erreur", "Veuillez remplir les champs!", Alert.AlertType.ERROR);
            return false;
        }
    }

    public List<Camera> getAllCameras() {
        try {
            return cameraService.getAllCameras();
        } catch (SQLException throwable) {
            Utils.alert("Problème", "Nous ne pouvons pas se connecter au serveur!", Alert.AlertType.ERROR);
            return null;
        }
    }

    public void updateCamera(Camera camera, List<String> newValues) {
        try {
            Camera editedCamera = new Camera();
            editedCamera.setId(camera.getId());
            editedCamera.setName(newValues.get(0));
            editedCamera.setIpAddress(newValues.get(1));
            editedCamera.setPort(Integer.parseInt(newValues.get(2)));
            Camera updated = cameraService.updateCamera(editedCamera);
            if (updated != null) {
                Utils.alert("Modification", "La caméra " + updated.getName() + " a bien été modifié!", Alert.AlertType.INFORMATION);
            } else {
                Utils.alert("Erreur", "Problème lors de la modification", Alert.AlertType.ERROR);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void deleteCamera(int id) {
        try {
            boolean deleted = cameraService.deleteCamera(id);
            if (deleted) {
                Utils.alert("Suppression", "La caméra a bien été supprimée!", Alert.AlertType.INFORMATION);
            } else {
                Utils.alert("Erreur", "Problème lors de la suppression", Alert.AlertType.ERROR);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
