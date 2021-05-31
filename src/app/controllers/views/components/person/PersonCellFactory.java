package app.controllers.views.components.person;

import app.models.Person;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class PersonCellFactory implements Callback<ListView<Person>, ListCell<Person>> {

    @Override
    public ListCell<Person> call(ListView<Person> param) {
        return new PersonCell();
    }
}
