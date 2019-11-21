package database.dao;

import constants.Constants;
import database.JdbcConnector;
import entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public List<User> paginationUsers(int from, int total) {

        Connection connection = new JdbcConnector().getConnection();

        if (null == connection) {
            return null;
        }
        try {
            PreparedStatement query = connection.prepareStatement("SELECT *FROM users ORDER  BY ctid DESC LIMIT "
                    + total + " OFFSET " + (from));
            ResultSet set = query.executeQuery();
            List<User> users = takeDataFromResultSet(set);
            query.close();
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public List<User> findAll() {

        Connection connection = new JdbcConnector().getConnection();

        if (null == connection) {
            return null;
        }
        try {
            PreparedStatement query = connection.prepareStatement("SELECT *FROM users");
            ResultSet set = query.executeQuery();
            List<User> users = takeDataFromResultSet(set);
            query.close();
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public User findById(int id) {

        Connection connection = new JdbcConnector().getConnection();

        if (null == connection) {
            return null;
        }
        PreparedStatement query = null;
        try {
            query = connection.prepareStatement("SELECT *FROM users WHERE users_id = "
                    + id + ";");
            ResultSet set = query.executeQuery();
            List<User> results = takeDataFromResultSet(set);
            if (!results.isEmpty()) {
                return results.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (!connection.isClosed()) {
                    query.close();
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public User findByLogin(String login) {

        Connection connection = new JdbcConnector().getConnection();

        if (null == connection) {
            return null;
        }
        PreparedStatement query = null;
        try {
            query = connection.prepareStatement("SELECT *FROM users WHERE users_login LIKE "
                    + "'" + login + "'" + ";");
            ResultSet set = query.executeQuery();
            List<User> results = takeDataFromResultSet(set);
            if (!results.isEmpty()) {
                return results.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (!connection.isClosed()) {
                    query.close();
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public User findByEmail(String email) {

        Connection connection = new JdbcConnector().getConnection();

        if (null == connection) {
            return null;
        }
        PreparedStatement query = null;
        try {
            query = connection.prepareStatement("SELECT *FROM users WHERE email LIKE "
                    + "'" + email + "'" + ";");
            ResultSet set = query.executeQuery();
            List<User> results = takeDataFromResultSet(set);
            if (!results.isEmpty()) {
                return results.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (!connection.isClosed()) {
                    query.close();
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public int getNumberOfUsers() {
        Connection connection = new JdbcConnector().getConnection();
        int result = Constants.ZERO;
        if (null == connection) {
            return result;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT (*)FROM users");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result = resultSet.getInt(Constants.ONE);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        } finally {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public int addNewUser(String firstName, String lastName, String status, String birthDate, String email, String password, String login) {

        Connection connection = new JdbcConnector().getConnection();
        int result = Constants.TWO;
        if (null == connection) {
            return result;
        }
        try {
            PreparedStatement addStatement = connection.prepareStatement("INSERT INTO users " +
                    "(users_first_name,users_last_name,users_status,users_birth_date, users_password,email,users_login,users_role)" +
                    " VALUES (" + "'" + firstName + "'," + "'" + lastName + "'," + "" + status + "," + "'" + birthDate + "'," +
                    "'" + password + "'," + "'" + email + "'," + "'" + login + "'," + "'" + Constants.USER + "'" + ")");
            result = addStatement.executeUpdate();
            addStatement.close();
        } catch (SQLException e) {
            result = Constants.ZERO;
            return result;
        } finally {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public int updateUser(String firstName, String lastName, String birthDate, String activityStatus, int id, String email, String password, String login) {

        Connection connection = new JdbcConnector().getConnection();
        int result = Constants.TWO;
        if (null == connection) {
            return result;
        }
        try {
            PreparedStatement addStatement = connection.prepareStatement("UPDATE users " +
                    " SET users_first_name = '" + firstName + "'," +
                    "users_last_name = '" + lastName + "'," +
                    "users_birth_date = '" + birthDate + "'," +
                    "email = '" + email + "'," +
                    "users_password = '" + password + "'," +
                    "users_login = '" + login + "'," +
                    "users_status = " + activityStatus +
                    " WHERE users_id = " + id);
            result = addStatement.executeUpdate();
            addStatement.close();
        } catch (SQLException e) {
            result = Constants.ZERO;
            return result;
        } finally {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public int deleteUser(int id) {
        Connection connection = new JdbcConnector().getConnection();
        int result = Constants.TWO;
        if (null == connection) {
            return result;
        }
        try {
            PreparedStatement addStatement = connection.prepareStatement("DELETE FROM users WHERE users_id = " + id + ";");
            result = addStatement.executeUpdate();
            addStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return result;
        } finally {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public List<User> takeDataFromResultSet(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();
        LocalDate birthDate = null;
        while (resultSet.next()) {
            int id = resultSet.getInt(Constants.ONE);
            String firstName = resultSet.getString(Constants.TWO);
            String lastName = resultSet.getString(Constants.THREE);
            boolean isUserActive = resultSet.getBoolean(Constants.FOUR);
            String BirthDate = resultSet.getString(Constants.FIVE);
            if (BirthDate != null) {
                birthDate = LocalDate.parse(BirthDate);
            }
            String password = resultSet.getString(Constants.SIX);
            String login = resultSet.getString(Constants.SEVEN);
            String email = resultSet.getString(Constants.EIGHT);
            users.add(new User(id, firstName, lastName, isUserActive, birthDate, password, login, email));
            birthDate = null;
        }
        System.out.println(users);
        return users;
    }

}
