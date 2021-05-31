package app.controllers.db;

import app.models.User;
import app.services.DBConnection;
import app.services.UserServices;
import app.utils.Utils;
import javafx.scene.control.Alert;

import java.sql.*;

public class UserController {
    private final UserServices userServices;

    public UserController() {
        userServices = new UserServices();
    }

    public boolean userExists(User user) {
        try {
            return userServices.userExists(user);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return false;
        }
    }

    public int addUser(User user) {
        try {
            return userServices.addUser(user);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return 0;
        }
    }

    public User getUserByEmail(String email) {
        try {
            return userServices.getUserByEmail(email);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return null;
        }
    }
}
