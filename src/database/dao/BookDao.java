package database.dao;

import constants.Constants;
import database.JdbcConnector;
import entity.Book;
import entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookDao {

    private UserDao userDao = new UserDao();

    public List<Book> paginationQuery(int from, int total) {

        Connection connection = new JdbcConnector().getConnection();

        if (null == connection) {
            return null;
        }
        try {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM books books LEFT JOIN " +
                    "users ON books.users_id =users.users_id ORDER BY books_id LIMIT "
                    + total + " OFFSET " + (from));
            ResultSet resultSet = query.executeQuery();
            List<Book> books = takeBooksWithUsersFromResultSet(resultSet);
            query.close();
            return books;
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

    public List<Book> paginationQueryJustForBooks(int from, int total) {

        Connection connection = new JdbcConnector().getConnection();

        if (null == connection) {
            return null;
        }
        try {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM books ORDER  BY ctid DESC LIMIT "
                    + total + " OFFSET " + (from));
            ResultSet resultSet = query.executeQuery();
            List<Book> books = takeDataFromResultSet(resultSet);
            query.close();
            return books;
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

    public Book tskeBookWithUserByID(int id) {

        Connection connection = new JdbcConnector().getConnection();

        if (null == connection) {
            return null;
        }
        try {
            PreparedStatement query = connection.prepareStatement("SELECT * FROM books books LEFT JOIN " +
                    "users ON books.users_id =users.users_id WHERE books_id = "
                    + id);
            ResultSet resultSet = query.executeQuery();
            List<Book> books = takeBooksWithUsersFromResultSet(resultSet);
            query.close();
            return books.get(0);
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

    public int getNumberOfRecords() {
        Connection connection = new JdbcConnector().getConnection();
        int result = Constants.ZERO;
        if (null == connection) {
            return result;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT (*)FROM books");
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

    public List<Book> findAllBooksThatShouldHaveBeenReturned(int from, int total) {

        Connection connection = new JdbcConnector().getConnection();

        if (null == connection) {
            return null;
        }
        try {
            PreparedStatement query = connection.prepareStatement("SELECT *FROM books WHERE book_must_be_taken<NOW() LIMIT "
                    + total + " OFFSET " + (from));
            ResultSet set = query.executeQuery();
            List<Book> books = takeDataFromResultSet(set);
            query.close();
            return books;
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

    public int getNumberOfBooksThatShouldHaveBeenReturned() {
        Connection connection = new JdbcConnector().getConnection();
        int result = Constants.ZERO;
        if (null == connection) {
            return result;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT (*)FROM books WHERE book_must_be_taken<NOW()");
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

    public Book findById(int id) {

        Connection connection = new JdbcConnector().getConnection();

        if (null == connection) {
            return null;
        }
        PreparedStatement query = null;
        try {
            query = connection.prepareStatement("SELECT *FROM books WHERE books_id = "
                    + id + ";");
            ResultSet set = query.executeQuery();
            List<Book> results = takeDataFromResultSet(set);
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

    public int addNewBook(String author, String bookName, String bookYear, String bookStatus) {

        Connection connection = new JdbcConnector().getConnection();
        int result = Constants.TWO;
        if (null == connection) {
            return result;
        }
        try {
            PreparedStatement addStatement = connection.prepareStatement("INSERT INTO books " +
                    "(book_autor,book_name,book_year,book_status)" +
                    " VALUES (" + "'" + author + "'," + "'" + bookName + "'," + "'" + bookYear + "'," + bookStatus + ")");
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

    public int updateBook(String author, String bookName, String bookYear, int id) {

        Connection connection = new JdbcConnector().getConnection();
        int result = Constants.TWO;
        if (null == connection) {
            return result;
        }
        try {
            PreparedStatement addStatement = connection.prepareStatement("UPDATE books " +
                    " SET " +
                    "book_autor = '" + author + "'," +
                    "book_name = '" + bookName + "'," +
                    "book_year = '" + bookYear + "'" +
                    " WHERE books_id = " + id);
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

    public int giveBook(int userId, int bookId, String bookStatus, LocalDate bookMustBeReturned) {

        Connection connection = new JdbcConnector().getConnection();
        int result = Constants.TWO;
        if (null == connection) {
            return result;
        }
        try {
            PreparedStatement addStatement = connection.prepareStatement("UPDATE books " +
                    " SET users_id = " + userId + "," +
                    "book_must_be_taken = '" + bookMustBeReturned.toString() + "'," +
                    "book_status = " + bookStatus + "" +
                    " WHERE books_id = " + bookId);
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

    public int takeBook(int bookId) {

        Connection connection = new JdbcConnector().getConnection();
        int result = Constants.TWO;
        if (null == connection) {
            return result;
        }
        try {
            PreparedStatement addStatement = connection.prepareStatement("UPDATE books " +
                    " SET users_id = " + "null " + "," +
                    "book_must_be_taken = " + "null " + "," +
                    "book_status = " + "true" + "" +
                    " WHERE books_id = " + bookId);
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

    public int deleteBook(int id) {
        Connection connection = new JdbcConnector().getConnection();
        int result = Constants.TWO;
        if (null == connection) {
            return result;
        }
        try {
            PreparedStatement addStatement = connection.prepareStatement("DELETE FROM books WHERE books_id = " + id + ";");
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


    public List<Book> takeDataFromResultSet(ResultSet resultSet) throws SQLException {
        List<Book> books = new ArrayList<>();
        LocalDate bookMustBeReturned = null;
        LocalDate bookYear = null;
        while (resultSet.next()) {
            int id = resultSet.getInt(Constants.ONE);
            int users_id = resultSet.getInt(Constants.TWO);
            String bookName = resultSet.getString(Constants.THREE);
            String bookAuthor = resultSet.getString(Constants.FOUR);
            String bookYearString = resultSet.getString(Constants.FIVE);
            String returnedDate = resultSet.getString(Constants.SIX);
            if (returnedDate != null) {
                bookMustBeReturned = LocalDate.parse(returnedDate);
            }
            if (bookYearString != null) {
                bookYear = LocalDate.parse(bookYearString);
            }
            boolean bookStatus = resultSet.getBoolean(Constants.SEVEN);
            Book book = new Book(id, users_id, bookAuthor, bookName, bookYear, bookMustBeReturned, bookStatus);
            books.add(book);
            bookMustBeReturned = null;
            bookYear = null;
        }
        return books;
    }

    public List<Book> takeBooksWithUsersFromResultSet(ResultSet resultSet) throws SQLException {
        List<Book> books = new ArrayList<>();
        LocalDate bookMustBeReturned = null;
        LocalDate bookYear = null;
        LocalDate userBirthDate = null;
        while (resultSet.next()) {
            int id = resultSet.getInt(Constants.ONE);
            int users_id = resultSet.getInt(Constants.TWO);
            String bookName = resultSet.getString(Constants.THREE);
            String bookAuthor = resultSet.getString(Constants.FOUR);
            String bookYearString = resultSet.getString(Constants.FIVE);
            String returnedDate = resultSet.getString(Constants.SIX);
            if ((returnedDate != null)) {
                bookMustBeReturned = LocalDate.parse(returnedDate);
            }
            if (bookYearString != null) {
                bookYear = LocalDate.parse(bookYearString);
            }
            boolean bookStatus = resultSet.getBoolean(Constants.SEVEN);
            //now user fields
            int userID = resultSet.getInt(Constants.EIGHT);
            String firstName = resultSet.getString(Constants.NINE);
            String lastName = resultSet.getString(Constants.TEN);
            boolean isUserActive = resultSet.getBoolean(Constants.ELEVEN);
            String BirthDate = resultSet.getString(Constants.TVELVE);
            if (BirthDate != null) {
                userBirthDate = LocalDate.parse(BirthDate);
            }
            Book book = new Book(id, users_id, bookAuthor, bookName, bookYear, bookMustBeReturned, bookStatus);
            book.setUser(new User(userID, firstName, lastName, isUserActive, userBirthDate));
            books.add(book);
            bookMustBeReturned = null;
            bookYear = null;
            userBirthDate = null;
        }
        return books;
    }

}
