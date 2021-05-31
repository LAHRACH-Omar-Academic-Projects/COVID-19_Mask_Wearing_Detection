package app.controllers.db;

import app.models.Person;
import app.services.PersonServices;
import app.utils.Utils;
import javafx.scene.control.Alert;

import java.sql.SQLException;
import java.util.List;

public class PersonController {
    private final PersonServices personServices;

    public PersonController() {
        personServices = new PersonServices();
    }

    public void addPerson(Person person) {
        try {
            boolean exists = personServices.personExists(person.getPhoto());
            if(!exists) {
                personServices.addPerson(person);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Person> getAllPersons() {
        try {
            return personServices.getAllPersons();
        } catch (SQLException throwable) {
            Utils.alert("Problème", "Nous ne pouvons pas se connecter au serveur!", Alert.AlertType.ERROR);
            return null;
        }
    }

    public void deletePerson(int id) {
        try {
            boolean deleted = personServices.deletePerson(id);
            if (deleted) {
                Utils.alert("Suppression", "La personne a bien été supprimée!", Alert.AlertType.INFORMATION);
            } else {
                Utils.alert("Erreur", "Problème lors de la suppression", Alert.AlertType.ERROR);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
