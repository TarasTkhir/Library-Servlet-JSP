package servlets;

import constants.Constants;
import database.dao.UserDao;
import entity.User;
import parser.XMLParser;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.DateTimeException;
import java.time.LocalDate;

@WebServlet("/entry/librarian/ConfirmActionUsers")
public class ConfirmActionUsers extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UserDao userDao = new UserDao();

        String operation = request.getParameter(Constants.OPERATION);
        switch (operation) {

            case Constants.DELETE: {

                delete(userDao, request, response);
                break;
            }
            case Constants.UPDATE: {
                update(userDao, request, response);
                break;
            }
            case Constants.ADD: {
                addNew(userDao, request, response);
                break;
            }
        }

    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    public void update(UserDao userDao, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String firstName = request.getParameter(Constants.FIRST_NAME);
        String lastName = request.getParameter(Constants.LAST_NAME);
        String birthDate = request.getParameter(Constants.DATE);
        String activityStatus = request.getParameter(Constants.ACTIVITY);
        String email = request.getParameter(Constants.EMAIL);
        String password = request.getParameter(Constants.PASSWORD);
        String login = request.getParameter(Constants.LOGIN);
        String confirmPassword = request.getParameter(Constants.COFIRM_PASSWORD);
        int id = Integer.valueOf(request.getParameter(Constants.UPDATE));
        User byLogin = userDao.findByLogin(login);
        User byEmail = userDao.findByEmail(email);
        User user = null;

        if (!password.equals(confirmPassword)) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/entry/librarian/userForm.jsp");
            PrintWriter out = response.getWriter();
            out.println("<font color=red>" + XMLParser.getMessages().get("difference-passwords") + ". </font>");
            if (Constants.ZERO != id) {
                user = userDao.findById(id);
            }
            if (null != user) {
                request.setAttribute(Constants.AUTHENTICATION, user);
                request.setAttribute(Constants.ACTION, Constants.UPDATE);
            }
            dispatcher.include(request, response);
            return;
        }
        if ((null != byLogin)) {
            int idToCheckLogin = byLogin.getId();
            if (id != idToCheckLogin) {
                PrintWriter out = response.getWriter();
                out.println("<font color=red>" + XMLParser.getMessages().get("login-already-exist") + ". </font>");
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/entry/librarian/userForm.jsp");
                if (Constants.ZERO != id) {
                    user = userDao.findById(id);
                }
                if (null != user) {
                    request.setAttribute(Constants.AUTHENTICATION, user);
                    request.setAttribute(Constants.ACTION, Constants.UPDATE);
                }
                dispatcher.include(request, response);
                return;
            }
        }
        if (null != byEmail) {
            if (id != byEmail.getId()) {
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/entry/librarian/userForm.jsp");
                PrintWriter out = response.getWriter();
                out.println("<font color=red>" + XMLParser.getMessages().get("email-exist") + ". </font>");
                if (Constants.ZERO != id) {
                    user = userDao.findById(id);
                }
                if (null != user) {
                    request.setAttribute(Constants.AUTHENTICATION, user);
                    request.setAttribute(Constants.ACTION, Constants.UPDATE);
                }
                dispatcher.include(request, response);
                return;
            }
        }
        try {
            LocalDate varification = LocalDate.parse(birthDate);
        } catch (DateTimeException e) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/entry/librarian/userForm.jsp");
            PrintWriter out = response.getWriter();
            out.println("<font color=red>" + XMLParser.getMessages().get("date-format-wrong") + ". </font>");
            if (Constants.ZERO != id) {
                user = userDao.findById(id);
            }
            if (null != user) {
                request.setAttribute(Constants.AUTHENTICATION, user);
                request.setAttribute(Constants.ACTION, Constants.UPDATE);
            }
            dispatcher.include(request, response);
            return;
        }

        int result = userDao.updateUser(firstName, lastName, birthDate, activityStatus, id, email, password, login);

        generateResponse(request, response, result, firstName);

    }

    public void delete(UserDao userDao, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/entry/librarian/librarianpage.jsp");
        PrintWriter out = response.getWriter();

        int idToDelete = Integer.valueOf(request.getParameter(Constants.DELETE));
        int result = userDao.deleteUser(idToDelete);

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

    public void addNew(UserDao userDao, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String firstName = request.getParameter(Constants.FIRST_NAME);
        String lastName = request.getParameter(Constants.LAST_NAME);
        String birthDate = request.getParameter(Constants.DATE);
        String activityStatus = request.getParameter(Constants.ACTIVITY);
        String email = request.getParameter(Constants.EMAIL);
        String password = request.getParameter(Constants.PASSWORD);
        String login = request.getParameter(Constants.LOGIN);
        String confirmPassword = request.getParameter(Constants.COFIRM_PASSWORD);

        if (!password.equals(confirmPassword)) {
            PrintWriter out = response.getWriter();
            out.println("<font color=red>" + XMLParser.getMessages().get("difference-passwords") + ". </font>");
            responseWhenErrorFoundInUpdate(request, response);
            return;

        }
        if (null != userDao.findByLogin(login)) {
            PrintWriter out = response.getWriter();
            out.println("<font color=red>" + XMLParser.getMessages().get("login-already-exist") + ". </font>");
            responseWhenErrorFoundInUpdate(request, response);
            return;
        }
        if (null != userDao.findByEmail(email)) {
            PrintWriter out = response.getWriter();
            out.println("<font color=red>" + XMLParser.getMessages().get("email-exist") + ". </font>");
            responseWhenErrorFoundInUpdate(request, response);
            return;
        }
        try {
            LocalDate varification = LocalDate.parse(birthDate);
        } catch (DateTimeException e) {
            PrintWriter out = response.getWriter();
            out.println("<font color=red>" + XMLParser.getMessages().get("date-format-wrong") + ". </font>");
            responseWhenErrorFoundInUpdate(request, response);
            return;
        }

        int result = userDao.addNewUser(firstName, lastName, activityStatus, birthDate, email, password, login);

        generateResponse(request, response, result, firstName);

    }

    public void generateResponse(HttpServletRequest request, HttpServletResponse response, int result, String firstName) throws IOException, ServletException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/entry/librarian/librarianpage.jsp");
        PrintWriter out = response.getWriter();

        switch (result) {
            case Constants.ZERO: {
                out.println("<font color=red> " + XMLParser.getMessages().get("login-already-exist") + firstName + "</font>");
                dispatcher.include(request, response);
                return;
            }
            case Constants.ONE: {
                out.println("<font color=green> " + XMLParser.getMessages().get("was-saved") + firstName + "</font>");
                dispatcher.include(request, response);
                return;
            }

            case Constants.TWO: {
                out.println("<font color=red> " + XMLParser.getMessages().get("failed-connection-to-create") + " " + firstName + "</font>");
                dispatcher.include(request, response);
                return;
            }
        }
    }

    private void responseWhenErrorFoundInUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/entry/librarian/userForm.jsp");
        request.setAttribute(Constants.AUTHENTICATION, new User(0, "", "", false, null, "", "", ""));
        request.setAttribute(Constants.ACTION, Constants.ADD);
        dispatcher.include(request, response);
    }

    private void responseWhenErrorFoundInCreatingProccess(HttpServletRequest request, HttpServletResponse response) {

    }
}
