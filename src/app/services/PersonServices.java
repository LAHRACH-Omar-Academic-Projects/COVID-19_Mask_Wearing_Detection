package app.services;

import app.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonServices {
    public boolean personExists(String photo) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<Person> persons = new ArrayList<>();

        try {
            connection = DBConnection.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT * FROM persons WHERE photo = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, photo);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Person person = new Person();
                person.setId(resultSet.getInt(1));
                person.setPhoto(resultSet.getString(2));
                person.setNumberOfDetections(resultSet.getInt(3));
                persons.add(person);
            }

            return !persons.isEmpty();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }
        }

        return !persons.isEmpty();
    }

    public void addPerson(Person person) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBConnection.getDBConnection();
            connection.setAutoCommit(false);
            String query = "INSERT INTO persons(photo, number_of_detections) VALUES(?, ?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, person.getPhoto());
            statement.setInt(2, person.getNumberOfDetections());
            statement.executeUpdate();
            connection.commit();
            resultSet = statement.getGeneratedKeys();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            connection.rollback();
        } finally {
            if (null != resultSet) {
                resultSet.close();
            }

            if (null != statement) {
                statement.close();
            }
        }
    }

    public List<Person> getAllPersons() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        List<Person> persons = new ArrayList<>();

        try {
            connection = DBConnection.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT * FROM persons Order by id DESC";
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Person person = new Person();
                person.setId(resultSet.getInt(1));
                person.setPhoto(resultSet.getString(2));
                person.setNumberOfDetections(resultSet.getInt(3));
                persons.add(person);
            }

            return persons;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }
        }

        return persons;
    }


    public Person updatePerson(Person person) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DBConnection.getDBConnection();
            connection.setAutoCommit(false);
            String query = "UPDATE persons SET photo = ?, numberOfDetections = ? WHERE id = ? ";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, person.getPhoto());
            statement.setInt(2, person.getNumberOfDetections());
            statement.executeUpdate();
            connection.commit();
            return person;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            connection.rollback();
            return null;
        } finally {
            if (null != statement) {
                statement.close();
            }
        }
    }

    public boolean deletePerson(int id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DBConnection.getDBConnection();
            connection.setAutoCommit(false);
            String query = "DELETE FROM persons WHERE id = ? ";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, id);
            statement.executeUpdate();
            connection.commit();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            connection.rollback();
            return false;
        } finally {
            if (null != statement) {
                statement.close();
            }
        }
    }
}
