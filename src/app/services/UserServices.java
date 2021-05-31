package app.services;

import app.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserServices {
    public boolean userExists(User user) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<User> users = new ArrayList<>();

        try {
            connection = DBConnection.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT * FROM users WHERE username = ? or email = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user1 = new User();
                user1.setId(resultSet.getInt(1));
                users.add(user1);
            }

            return !users.isEmpty();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }
        }

        return !users.isEmpty();
    }

    public int addUser(User user) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBConnection.getDBConnection();
            connection.setAutoCommit(false);
            String query = "INSERT INTO users(username, fullName, email, password) VALUES(?, ?, ?, ?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getFullName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.executeUpdate();
            connection.commit();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
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

        return 0;
    }

    public User getUserByEmail(String email) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        User user = null;
        try {
            connection = DBConnection.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT * FROM users WHERE email = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt(1));
                user.setUsername(resultSet.getString(2));
                user.setFullName(resultSet.getString(3));
                user.setEmail(resultSet.getString(4));
                user.setPassword(resultSet.getString(5));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }
        }
        return user;
    }
}
