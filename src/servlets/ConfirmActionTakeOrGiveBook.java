package servlets;

import constants.Constants;
import database.dao.BookDao;
import database.dao.UserDao;
import entity.Book;
import entity.User;
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

@WebServlet("/entry/librarian/ConfirmActionTakeOrGiveBook")
public class ConfirmActionTakeOrGiveBook extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BookDao bookDao = new BookDao();
        UserDao userDao = new UserDao();

        String operation = request.getParameter(Constants.OPERATION);
        switch (operation) {

            case Constants.GIVE: {

                give(bookDao, userDao, request, response);
                break;
            }
            case Constants.TAKE: {
                take(bookDao, request, response);
                break;
            }
        }
    }



    public void give(BookDao bookDao, UserDao userDao, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String userID;
        int userId;
        int bookMustBeReturned;
        int bookId = Integer.valueOf(request.getParameter(Constants.GIVE));
        int result;
        String page1 = request.getParameter("page1");
        String bookStatus = Constants.FALSE;
        User user = null;
        LocalDate bookMustBeReturnedDate;
        Book book = null;
        HttpSession session = request.getSession();
        try {
            userID = request.getParameter("Visitors").split(":")[1];
            userId = Integer.parseInt(userID);
            bookMustBeReturned = Integer.parseInt(request.getParameter(Constants.MUST_BE_RETURNED));
        } catch (NumberFormatException | NullPointerException | IndexOutOfBoundsException e) {
            session.setAttribute("idError","<font color=red>" + XMLParser.getMessages().get("username-or-userID-is-wrong") + ". </font>");
            book = bookDao.tskeBookWithUserByID(bookId);
            if (null != book) {
                session.setAttribute(Constants.BOOK, book);
                session.setAttribute(Constants.ACTION, Constants.GIVE);
                session.setAttribute("page1",page1);
            }
            response.sendRedirect(getServletContext().getContextPath() +"/entry/librarian/giveOrTakeBookForm.jsp");
            return;
        }
        if ((userId <= 0) || ((bookMustBeReturned <= 0) || (bookMustBeReturned > Constants.MAX_DAYS_FOR_BOOK_GIVEN))) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/entry/librarian/giveOrTakeBookForm.jsp");
            PrintWriter out = response.getWriter();
            out.println("<font color=red>" + XMLParser.getMessages().get("book-days-for-return") + ". </font>");
            book = bookDao.tskeBookWithUserByID(bookId);
            if (null != book) {
                session.setAttribute(Constants.BOOK, book);
                session.setAttribute(Constants.ACTION, Constants.GIVE);
                session.setAttribute("page1",page1);
            }
            dispatcher.include(request, response);
            response.sendRedirect(getServletContext().getContextPath() +"/entry/librarian/giveOrTakeBookForm.jsp");
            return;
        }
        user = userDao.findById(userId);
        if (null != user) {
            result = bookDao.giveBook(userId, bookId, bookStatus, LocalDate.now().plusDays(bookMustBeReturned));
            generateResponse(page1,request, response, result, bookDao, bookId);
            return;
        } else {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/entry/librarian/giveOrTakeBookForm.jsp");
            PrintWriter out = response.getWriter();
            out.println("<font color=red>" + XMLParser.getMessages().get("book-days-for-return") + ". </font>");
            book = bookDao.tskeBookWithUserByID(bookId);
            if (null != book) {
                session.setAttribute(Constants.BOOK, book);
               session.setAttribute(Constants.ACTION, Constants.GIVE);
                session.setAttribute("page1",page1);
            }
            dispatcher.include(request, response);
            response.sendRedirect(getServletContext().getContextPath() +"/entry/librarian/giveOrTakeBookForm.jsp");
            return;
        }
    }

    public void take(BookDao bookDao, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        int bookId = Integer.valueOf(request.getParameter(Constants.TAKE));
        HttpSession session = request.getSession();
        Book byId = null;
        int result;
        String page1 = request.getParameter("page1");
        if (bookId > 0) {
            byId = bookDao.findById(bookId);
        }
        if (null != byId) {
            result = bookDao.takeBook(bookId);
            switch (result) {
                case Constants.ZERO: {
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/entry/librarian/giveOrTakeBookForm.jsp");
                    PrintWriter out = response.getWriter();
                    out.println("<font color=red> " + XMLParser.getMessages().get("something-went-wrong") + "" + "</font>");
                    byId = bookDao.tskeBookWithUserByID(bookId);
                    if (null != byId) {
                        session.setAttribute(Constants.BOOK, byId);
                        session.setAttribute(Constants.ACTION, Constants.GIVE);
                        session.setAttribute("page1",page1);
                    }
                    dispatcher.include(request, response);
                    response.sendRedirect(getServletContext().getContextPath() +"/entry/librarian/giveOrTakeBookForm.jsp");
                    break;
                }
                case Constants.ONE: {
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/entry/librarian/issuingBooks.jsp");
                    PrintWriter out = response.getWriter();
                    out.println("<font color=green> " + XMLParser.getMessages().get("was-saved") + "" + "</font>");
                    session.setAttribute("page1",page1);
                    dispatcher.include(request, response);
                    response.sendRedirect(getServletContext().getContextPath() +"/entry/librarian/issuingBooks.jsp");
                    break;
                }

                case Constants.TWO: {
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/entry/librarian/giveOrTakeBookForm.jsp");
                    PrintWriter out = response.getWriter();
                    out.println("<font color=red> " + XMLParser.getMessages().get("failed-connection-to-create") + "" + "</font>");
                    byId = bookDao.tskeBookWithUserByID(bookId);
                    if (null != byId) {
                        session.setAttribute(Constants.BOOK, byId);
                        session.setAttribute(Constants.ACTION, Constants.GIVE);
                        session.setAttribute("page1",page1);
                    }
                    dispatcher.include(request, response);
                    response.sendRedirect(getServletContext().getContextPath() +"/entry/librarian/giveOrTakeBookForm.jsp");
                    break;
                }
            }
            return;
        }
    }

    public void generateResponse(String page1,HttpServletRequest request, HttpServletResponse response, int result, BookDao bookDao, int bookId) throws IOException, ServletException {

        HttpSession session = request.getSession();

        switch (result) {
            case Constants.ZERO: {
                Book book;
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/entry/librarian/giveOrTakeBookForm.jsp");
                PrintWriter out = response.getWriter();
                out.println("<font color=red> " + XMLParser.getMessages().get("something-went-wrong") + "" + "</font>");
                book = bookDao.tskeBookWithUserByID(bookId);
                if (null != book) {
                    session.setAttribute(Constants.BOOK, book);
                    session.setAttribute(Constants.ACTION, Constants.GIVE);
                    session.setAttribute("page1",page1);
                }
                dispatcher.include(request, response);
                response.sendRedirect(getServletContext().getContextPath() +"/entry/librarian/giveOrTakeBookForm.jsp");
                break;
            }
            case Constants.ONE: {
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/entry/librarian/issuingBooks.jsp");
                PrintWriter out = response.getWriter();
                out.println("<font color=green> " + XMLParser.getMessages().get("was-saved") + "" + "</font>");
                session.setAttribute("page1",page1);
                dispatcher.include(request, response);
                response.sendRedirect(getServletContext().getContextPath() +"/entry/librarian/issuingBooks.jsp");
                break;
            }

            case Constants.TWO: {
                Book book;
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/entry/librarian/giveOrTakeBookForm.jsp");
                PrintWriter out = response.getWriter();
                out.println("<font color=red> " + XMLParser.getMessages().get("failed-connection-to-create") + "" + "</font>");
                book = bookDao.tskeBookWithUserByID(bookId);
                if (null != book) {
                    session.setAttribute(Constants.BOOK, book);
                    session.setAttribute(Constants.ACTION, Constants.GIVE);
                    session.setAttribute("page1",page1);
                }
                dispatcher.include(request, response);
                response.sendRedirect(getServletContext().getContextPath() +"/entry/librarian/giveOrTakeBookForm.jsp");
                break;
            }
        }
    }
}
