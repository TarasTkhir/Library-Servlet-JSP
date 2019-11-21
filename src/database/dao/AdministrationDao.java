package database.dao;

import database.JdbcConnector;
import entity.AuthenticationUser;
import constants.Constants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdministrationDao {

    public List<AuthenticationUser> paginationQuery(int start, int total) {

        Connection connection = new JdbcConnector().getConnection();

        if (null == connection) {
            return null;
        }
        PreparedStatement query = null;
        try {
            query = connection.prepareStatement("SELECT *FROM administration ORDER  BY ctid DESC LIMIT "
                    + total + " OFFSET " + (start));
            ResultSet set = query.executeQuery();
            return takeDataFromResultSet(set);
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

    public AuthenticationUser findById(int id) {

        Connection connection = new JdbcConnector().getConnection();

        if (null == connection) {
            return null;
        }
        PreparedStatement query = null;
        try {
            query = connection.prepareStatement("SELECT *FROM administration WHERE administration_id = "
                    + id + ";");
            ResultSet set = query.executeQuery();
            List<AuthenticationUser> results = takeDataFromResultSet(set);
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

    public AuthenticationUser findByLoginAndPassword(String login, String password) throws SQLException {

        Connection connection = new JdbcConnector().getConnection();

        if (null == connection) {
            return null;
        }
        PreparedStatement query = connection.prepareStatement("SELECT *FROM administration WHERE administration_login LIKE "
                + "'" + login + "'" + " AND administration_password LIKE " + "'" + password + "'");
        try {
            ResultSet set = query.executeQuery();
            List<AuthenticationUser> results = takeDataFromResultSet(set);
            if (!results.isEmpty())
                return results.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (!connection.isClosed()) {
                query.close();
                connection.close();
            }
        }
        return null;
    }

    public int updateAdministration(String role, String password, String firstName, String lastName, String login, int id) {

        Connection connection = new JdbcConnector().getConnection();
        int result = Constants.TWO;
        if (null == connection) {
            return result;
        }
        try {
            PreparedStatement addStatement = connection.prepareStatement("UPDATE administration " +
                    " SET administration_first_name = '" + firstName + "'," +
                    "administration_last_name = '" + lastName + "'," +
                    "administration_password = '" + password + "'," +
                    "administration_role = '" + role + "'," +
                    "administration_login = '" + login + "'" +
                    " WHERE administration_id = " + id);
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

    public int addNewAdministration(String role, String firstName, String lastName, String password, String login) {

        Connection connection = new JdbcConnector().getConnection();
        int result = Constants.TWO;
        if (null == connection) {
            return result;
        }
        try {
            PreparedStatement addStatement = connection.prepareStatement("INSERT INTO administration " +
                    "(administration_role,administration_first_name,administration_last_name,administration_password,administration_login)" +
                    " VALUES (" + "'" + role + "'," + "'" + firstName + "'," + "'" + lastName + "'," + "'" + password + "'," + "'" + login + "')");
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

    public int deleteAdministration(int id) {
        Connection connection = new JdbcConnector().getConnection();
        int result = Constants.TWO;
        if (null == connection) {
            return result;
        }
        try {
            PreparedStatement addStatement = connection.prepareStatement("DELETE FROM administration WHERE administration_id = " + id + ";");
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

    public int getNoOfRecords() {
        Connection connection = new JdbcConnector().getConnection();
        int result = Constants.ZERO;
        if (null == connection) {
            return result;
        }
        try {
            PreparedStatement Statement = connection.prepareStatement("SELECT COUNT (*)FROM administration");
            ResultSet resultSet = Statement.executeQuery();
            while (resultSet.next()) {
                result = resultSet.getInt(Constants.ONE);
            }
            Statement.close();
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

    public List<AuthenticationUser> takeDataFromResultSet(ResultSet set) throws SQLException {
        List<AuthenticationUser> authenticationUserList = new ArrayList<>();
        while (set.next()) {
            int id = set.getInt(Constants.SIX);
            String role = set.getString(Constants.ONE);
            String firstName = set.getString(Constants.TWO);
            String lastName = set.getString(Constants.THREE);
            String password = set.getString(Constants.FOUR);
            String log = set.getString(Constants.FIVE);

            authenticationUserList.add(new AuthenticationUser(id, AuthenticationUser.ROLE.valueOf(role.toUpperCase()), firstName, lastName, password, log));
        }
        return authenticationUserList;
    }

}
