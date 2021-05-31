package app.controllers.views.components.person;

import app.models.Person;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class PersonCell extends ListCell<Person> {

    @FXML
    private ImageView personPhoto;

    @FXML
    private Label numberOfDetections;

    //@FXML
    //private HBox deleteBtn;

    public PersonCell() {
        loadFXML();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../../views/components/person/personCell.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(Person person, boolean empty) {
        super.updateItem(person, empty);

        if(empty || person == null) {
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
        else {
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            Image image = null;
            try {
                image = new Image(new FileInputStream(person.getPhoto()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            personPhoto.setImage(image);
        }
    }
}