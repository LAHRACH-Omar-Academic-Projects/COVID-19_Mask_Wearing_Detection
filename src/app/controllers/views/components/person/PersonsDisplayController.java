package app.controllers.views.components.person;

import app.controllers.db.PersonController;
import app.models.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class PersonsDisplayController {

    private List<Person> personList;
    private final PersonController personController;

    public PersonsDisplayController() {
        this.personList = new ArrayList<>();
        this.personController = new PersonController();
    }

    public void displayAllPersons(VBox container) {
        personList = personController.getAllPersons();
        ObservableList<Person> persons = FXCollections.observableArrayList(personList);

        ListView<Person> listView = new ListView<>(persons);
        listView.setMaxSize(230, 160);

        container.getChildren().add(listView);
        createPersonItems(listView);
    }

    public void createPersonItems(ListView<Person> listView) {
        listView.setCellFactory(new PersonCellFactory());
    }
}
