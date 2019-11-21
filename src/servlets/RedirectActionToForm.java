package servlets;

import constants.Constants;
import database.dao.AdministrationDao;
import entity.AuthenticationUser;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/entry/admin/RedirectActionToForm")
public class RedirectActionToForm extends HttpServlet {

    AdministrationDao administrationDao = new AdministrationDao();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String idToDelete = request.getParameter(Constants.DELETE);
        String idToUpdate = request.getParameter(Constants.UPDATE);
        String action = request.getParameter(Constants.ACTION);

        AuthenticationUser byId = null;

        switch (action) {

            case Constants.DELETE: {
                if (null != idToDelete) {
                    int id = Integer.parseInt(idToDelete);
                    byId = administrationDao.findById(id);
                }
                if (null != byId) {
                    byId.setPassword(null);
                    request.setAttribute(Constants.AUTHENTICATION, byId);
                    request.setAttribute(Constants.ACTION, Constants.DELETE);
                }
                break;
            }
            case Constants.UPDATE: {
                if (null != idToUpdate) {
                    int id = Integer.parseInt(idToUpdate);
                    byId = administrationDao.findById(id);
                }
                if (null != byId) {
                    request.setAttribute(Constants.AUTHENTICATION, byId);
                    request.setAttribute(Constants.ACTION, Constants.UPDATE);
                }
                break;
            }
            case Constants.ADD: {
                request.setAttribute(Constants.AUTHENTICATION, new AuthenticationUser(0, null, "", "", "", ""));
                request.setAttribute(Constants.ACTION, Constants.ADD);
                break;
            }
        }

        RequestDispatcher view = request.getRequestDispatcher("/entry/admin/Form.jsp");
        view.forward(request, response);
        return;

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
