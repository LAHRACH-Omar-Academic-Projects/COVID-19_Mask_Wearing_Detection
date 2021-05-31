package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*Parent root = FXMLLoader.load(getClass().getResource("views/authentication/auth.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Authentification");
        primaryStage.show();*/
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/app.fxml"));
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
    }

    public static void main(String[] args) {
        launch(args);
    }
}
