package app.controllers.views.authentication;

import app.controllers.db.UserController;
import app.models.User;
import app.services.UserServices;
import app.utils.Utils;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

public class AuthController {

    public VBox authForm;
    public Label authTitle;
    public TextField usernameInput;
    public TextField fullNameInput;
    public TextField emailInput;
    public PasswordField passwordInput;
    public Button authBtn;
    public Label authLinkDesc;
    public Label authLink;
    public Label infoLabel;

    public UserController userController;

    @FXML
    private void initialize() {
        userController = new UserController();
        Platform.runLater(() -> {
            authBtn.setOnAction(actionEvent -> {
                if(authBtn.getText().equals("S'inscrire")) {
                    register();
                }
                else {
                    login();
                }
            });
            authLink.setOnMouseClicked(event -> {
                if(authLink.getText().equals("S'inscrire")) {
                    showRegisterForm();
                }
                else {
                    showLoginForm();
                }
            });
        });
    }

    private void showRegisterForm() {
        authForm.getChildren().add(1, usernameInput);
        authForm.getChildren().add(2, fullNameInput);
        infoLabel.setText("");
        usernameInput.setText("");
        fullNameInput.setText("");
        emailInput.setText("");
        passwordInput.setText("");
        authTitle.setText("S'inscrire");
        authBtn.setText("S'inscrire");
        authLinkDesc.setText("Vous débutez chez Je-te-surveille ?");
        authLink.setText("Connexion");
        usernameInput.requestFocus();
        animateShowForm();
    }

    private void showLoginForm() {
        authForm.getChildren().remove(usernameInput);
        authForm.getChildren().remove(fullNameInput);
        usernameInput.setText("");
        fullNameInput.setText("");
        emailInput.setText("");
        passwordInput.setText("");
        authTitle.setText("Se connecter");
        authBtn.setText("Connexion");
        authLinkDesc.setText("Vous utilisez déjà Je-te-surveille ?");
        authLink.setText("S'inscrire");
        animateShowForm();
    }

    private void animateShowForm() {
        authForm.setScaleX(0.7);
        authForm.setScaleY(0.7);
        Duration duration = Duration.millis(200);
        ScaleTransition scaleTransition = new ScaleTransition(duration, authForm);
        scaleTransition.setByX(0.3);
        scaleTransition.setByY(0.3);
        scaleTransition.play();
    }

    private void register() {
        User user = new User();
        user.setUsername(usernameInput.getText());
        user.setFullName(fullNameInput.getText());
        user.setEmail(emailInput.getText());
        user.setPassword(passwordInput.getText());
        if (!user.getUsername().equals("") &&
                !user.getFullName().equals("") &&
                !user.getEmail().equals("") &&
                !user.getPassword().equals("")) {
            try {
                user.setPassword(Utils.encryptPassword(passwordInput.getText()));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            try {
                boolean exists = userController.userExists(user);
                if (!exists) {
                    int userId = userController.addUser(user);
                    if (userId > 0) {
                        infoLabel.setText("L'utilisateur a été ajoutée!");
                        infoLabel.setStyle("-fx-text-fill: -fx-green; -fx-font-weight: bold");
                        showLoginForm();
                    } else {
                        infoLabel.setText("Manquée!");
                        infoLabel.setStyle("-fx-text-fill: -fx-red; -fx-font-weight: bold");
                    }
                } else {
                    infoLabel.setText("L'utilisateur existe déjà!");
                    infoLabel.setStyle("-fx-text-fill: -fx-red; -fx-font-weight: bold");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            infoLabel.setText("Veuillez remplir tous les champs!");
            infoLabel.setStyle("-fx-text-fill: -fx-orange; -fx-font-weight: bold");
        }
    }

    private void login() {
        User user = new User();
        user.setEmail(emailInput.getText());
        user.setPassword(passwordInput.getText());
        boolean exists = userController.userExists(user);
        if(!emailInput.getText().equals("") && !passwordInput.getText().equals("")) {
            if(exists) {
                User user1 = userController.getUserByEmail(user.getEmail());
                try {
                    if(user1.getPassword().equals(Utils.encryptPassword(user.getPassword()))) {
                        loadApp();
                    }
                    else {
                        infoLabel.setText("Mot de passe incorrect!");
                        infoLabel.setStyle("-fx-text-fill: -fx-red; -fx-font-weight: bold");
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
            else {
                infoLabel.setText("L'utilisateur avec ces identifiants n'existe pas!");
                infoLabel.setStyle("-fx-text-fill: -fx-red; -fx-font-weight: bold");
            }
        }
        else {
            infoLabel.setText("Veuillez remplir tous les champs.");
            infoLabel.setStyle("-fx-text-fill: -fx-orange; -fx-font-weight: bold");
        }
    }

    private void loadApp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../views/app.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Je te surveille");
            stage.setMinHeight(750);
            stage.setMinWidth(1280);
            stage.setScene(new Scene(root, 1280, 750));
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - 1280) / 2);
            stage.setY((screenBounds.getHeight() - 780) / 2);
            stage.show();
            Stage currentStage;
            currentStage = (Stage) authBtn.getScene().getWindow();
            currentStage.hide();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
