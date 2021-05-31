package app.services;

import app.models.Alarm;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlarmServices {
    public boolean alarmExists(String path) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<Alarm> alarms = new ArrayList<>();

        try {
            connection = DBConnection.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT * FROM alarms WHERE path = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, path);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Alarm alarm = new Alarm();
                alarm.setId(resultSet.getInt(1));
                alarm.setName(resultSet.getString(2));
                alarm.setPath(resultSet.getString(3));
                alarms.add(alarm);
            }

            return !alarms.isEmpty();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }
        }

        return !alarms.isEmpty();
    }

    public int addAlarm(Alarm alarm) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBConnection.getDBConnection();
            connection.setAutoCommit(false);
            String query = "INSERT INTO alarms(name, path, selected) VALUES(?, ?, ?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, alarm.getName());
            statement.setString(2, alarm.getPath());
            statement.setBoolean(3, alarm.isSelected());
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

    public List<Alarm> getAllAlarms() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        List<Alarm> alarms = new ArrayList<>();

        try {
            connection = DBConnection.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT * FROM alarms Order by id DESC";
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Alarm alarm = new Alarm();
                alarm.setId(resultSet.getInt(1));
                alarm.setName(resultSet.getString(2));
                alarm.setPath(resultSet.getString(3));
                alarm.setSelected(resultSet.getBoolean(4));
                alarms.add(alarm);
            }

            return alarms;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }
        }

        return alarms;
    }


    public Alarm updateAlarm(Alarm alarm) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DBConnection.getDBConnection();
            connection.setAutoCommit(false);
            String query = "UPDATE alarms SET selected = ? WHERE id = ? ";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setBoolean(1, alarm.isSelected());
            statement.setInt(2, alarm.getId());
            statement.executeUpdate();
            connection.commit();
            return alarm;
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

    public boolean deleteAlarm(int id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DBConnection.getDBConnection();
            connection.setAutoCommit(false);
            String query = "DELETE FROM alarms WHERE id = ? ";
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

    public Alarm getSelectedAlarm() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        Alarm alarm = null;

        try {
            connection = DBConnection.getDBConnection();
            connection.setAutoCommit(false);
            String query = "SELECT * FROM alarms WHERE selected = true";
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                alarm = new Alarm();
                alarm.setId(resultSet.getInt(1));
                alarm.setName(resultSet.getString(2));
                alarm.setPath(resultSet.getString(3));
            }

            return alarm;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (null != statement) {
                statement.close();
            }
        }

        return alarm;
    }
}
