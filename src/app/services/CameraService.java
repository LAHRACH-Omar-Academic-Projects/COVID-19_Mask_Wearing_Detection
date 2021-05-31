package app.services;

import app.models.Camera;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CameraService {
    public boolean cameraExists(String ipAddress) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<Camera> cameras = new ArrayList<>();

        try {
            connection = DBConnection.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT * FROM cameras WHERE ipAddress = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, ipAddress);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Camera camera = new Camera();
                camera.setId(resultSet.getInt(1));
                camera.setName(resultSet.getString(2));
                camera.setIpAddress(resultSet.getString(3));
                camera.setPort(resultSet.getInt(4));
                cameras.add(camera);
            }

            return !cameras.isEmpty();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }
        }

        return !cameras.isEmpty();
    }

    public int addCamera(Camera camera) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBConnection.getDBConnection();
            connection.setAutoCommit(false);
            String query = "INSERT INTO cameras(name, ipAddress, port) VALUES(?, ?, ?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, camera.getName());
            statement.setString(2, camera.getIpAddress());
            statement.setInt(3, camera.getPort());
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

    public Camera getCameraByIpAddress(String ipAddress) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        Camera camera = null;

        try {
            connection = DBConnection.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT * FROM cameras WHERE ipAddress = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, ipAddress);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                camera = new Camera();
                camera.setId(resultSet.getInt(1));
                camera.setName(resultSet.getString(2));
                camera.setIpAddress(resultSet.getString(3));
                camera.setPort(resultSet.getInt(4));
            }

            return camera;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }
        }

        return camera;
    }

    public List<Camera> getAllCameras() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        List<Camera> cameras = new ArrayList<>();

        try {
            connection = DBConnection.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT * FROM cameras Order by id DESC";
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Camera camera = new Camera();
                camera.setId(resultSet.getInt(1));
                camera.setName(resultSet.getString(2));
                camera.setIpAddress(resultSet.getString(3));
                camera.setPort(resultSet.getInt(4));
                cameras.add(camera);
            }

            return cameras;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }
        }

        return cameras;
    }


    public Camera updateCamera(Camera camera) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DBConnection.getDBConnection();
            connection.setAutoCommit(false);
            String query = "UPDATE cameras SET name = ?, ipAddress = ?, port = ? WHERE id = ? ";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, camera.getName());
            statement.setString(2, camera.getIpAddress());
            statement.setInt(3, camera.getPort());
            statement.setInt(4, camera.getId());
            statement.executeUpdate();
            connection.commit();
            return camera;
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

    public boolean deleteCamera(int id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DBConnection.getDBConnection();
            connection.setAutoCommit(false);
            String query = "DELETE FROM cameras WHERE id = ? ";
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
