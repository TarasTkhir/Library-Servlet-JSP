package servlets;

import constants.Constants;
import database.dao.UserDao;
import entity.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/entry/librarian/RedirectActionToFormUser")
public class RedirectActionToFormUser extends HttpServlet {

    private UserDao userDao = new UserDao();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String idToDelete = request.getParameter(Constants.DELETE);
        String idToUpdate = request.getParameter(Constants.UPDATE);
        String action = request.getParameter(Constants.ACTION);

        User byId = null;

        switch (action) {

            case Constants.DELETE: {
                if (null != idToDelete) {
                    int id = Integer.parseInt(idToDelete);
                    byId = userDao.findById(id);
                }
                if (null != byId) {
                    request.setAttribute(Constants.AUTHENTICATION, byId);
                    request.setAttribute(Constants.ACTION, Constants.DELETE);
                }
                break;
            }
            case Constants.UPDATE: {
                if (null != idToUpdate) {
                    int id = Integer.parseInt(idToUpdate);
                    byId = userDao.findById(id);
                }
                if (null != byId) {
                    request.setAttribute(Constants.AUTHENTICATION, byId);
                    request.setAttribute(Constants.ACTION, Constants.UPDATE);
                }
                break;
            }
            case Constants.ADD: {
                request.setAttribute(Constants.AUTHENTICATION, new User(0,"","",false,null,"","",""));
                request.setAttribute(Constants.ACTION, Constants.ADD);
                break;
            }
        }

        RequestDispatcher view = request.getRequestDispatcher("/entry/librarian/userForm.jsp");
        view.forward(request, response);
        return;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

    }
}
