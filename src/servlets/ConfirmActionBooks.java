package servlets;

import constants.Constants;
import database.dao.BookDao;
import entity.Book;
import parser.XMLParser;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.DateTimeException;
import java.time.LocalDate;

@WebServlet("/entry/librarian/ConfirmActionBooks")
public class ConfirmActionBooks extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BookDao bookDao = new BookDao();

        String operation = request.getParameter(Constants.OPERATION);
        switch (operation) {

            case Constants.DELETE: {

                delete(bookDao, request, response);
                break;
            }
            case Constants.UPDATE: {
                update(bookDao, request, response);
                break;
            }
            case Constants.ADD: {
                addNew(bookDao, request, response);
                break;
            }
        }
    }

    public void update(BookDao bookDao, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String author = request.getParameter(Constants.BOOK_AUTHOR);
        String bookName = request.getParameter(Constants.BOOK_NAME);
        String bookYear = request.getParameter(Constants.BOOK_YEAR);
        Book book = null;
        int id = Integer.valueOf(request.getParameter(Constants.UPDATE));
        HttpSession session = request.getSession();

        try {
            LocalDate varification1 = LocalDate.parse(bookYear);
        } catch (DateTimeException e) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/entry/librarian/bookForm.jsp");
            PrintWriter out = response.getWriter();
            out.println("<font color=red>" + XMLParser.getMessages().get("date-format-wrong") + ". </font>");
            if (Constants.ZERO != id) {
                book = bookDao.findById(id);
            }
            if (null != book) {
                session.setAttribute(Constants.BOOK, book);
                session.setAttribute(Constants.ACTION, Constants.UPDATE);
            }
            dispatcher.include(request, response);
            response.sendRedirect(getServletContext().getContextPath() +"/entry/librarian/bookForm.jsp");
            return;
        }
        int result = bookDao.updateBook( author, bookName, bookYear, id);

        generateResponse(request, response, result, bookName);

    }

    public void delete(BookDao bookDao, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/entry/librarian/books.jsp");
        PrintWriter out = response.getWriter();

        int idToDelete = Integer.valueOf(request.getParameter(Constants.DELETE));
        int result = bookDao.deleteBook(idToDelete);

        switch (result) {
            case Constants.ZERO: {
                out.println("<font color=red>" + XMLParser.getMessages().get("no-librarian-or-admin-found") + idToDelete + ". </font>");
                dispatcher.include(request, response);
                return;
            }
            case Constants.ONE: {
                out.println("<font color=green>" + idToDelete + " " + XMLParser.getMessages().get("was-deleted") + "</font>");
                dispatcher.include(request, response);
                return;
            }

            case Constants.TWO: {
                out.println("<font color=red>" + XMLParser.getMessages().get("cannot-delete-connection-problem") + "</font>");
                dispatcher.include(request, response);
                return;
            }
        }
    }

    public void addNew(BookDao bookDao, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String author = request.getParameter(Constants.BOOK_AUTHOR);
        String bookName = request.getParameter(Constants.BOOK_NAME);
        String bookYear = request.getParameter(Constants.BOOK_YEAR);
        String bookStatus = Constants.TRUE;
        HttpSession session = request.getSession();
        try {
            LocalDate varification1 = LocalDate.parse(bookYear);
        } catch (DateTimeException e) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/entry/librarian/bookForm.jsp");
            PrintWriter out = response.getWriter();
            out.println("<font color=red>" + XMLParser.getMessages().get("date-format-wrong") + ". </font>");
            session.setAttribute(Constants.BOOK, new Book(0, 0, "", "", null, null, false));
            session.setAttribute(Constants.ACTION, Constants.ADD);
            dispatcher.include(request, response);
            response.sendRedirect(getServletContext().getContextPath() +"/entry/librarian/bookForm.jsp");
            return;
        }
        int result = bookDao.addNewBook(author, bookName, bookYear, bookStatus);

        generateResponse(request, response, result, bookName);

    }

    public void generateResponse(HttpServletRequest request, HttpServletResponse response, int result, String bookName) throws IOException, ServletException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/entry/librarian/books.jsp");
        PrintWriter out = response.getWriter();

        switch (result) {
            case Constants.ZERO: {
                out.println("<font color=red> " + XMLParser.getMessages().get("login-already-exist") + bookName + "</font>");
                dispatcher.include(request, response);
                return;
            }
            case Constants.ONE: {
                out.println("<font color=green> " + XMLParser.getMessages().get("was-saved") + bookName + "</font>");
                dispatcher.include(request, response);
                return;
            }

            case Constants.TWO: {
                out.println("<font color=red> " + XMLParser.getMessages().get("failed-connection-to-create") + " " + bookName + "</font>");
                dispatcher.include(request, response);
                return;
            }
        }
    }
}
