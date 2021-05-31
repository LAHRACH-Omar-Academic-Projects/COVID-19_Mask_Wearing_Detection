package app.models;

import app.controllers.db.UserController;

public class User {
    private int id;
    private String username;
    private String fullName;
    private String email;
    private String password;

    private final UserController userController;

    public User() {
        this.userController = new UserController();
    }

    public User(int id, String username, String fullName, String email, String password) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.userController = new UserController();
    }

    public void register() {
        userController.addUser(this);
    }

    public boolean login() {
        return true;
    }

    public boolean logout() {
        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
