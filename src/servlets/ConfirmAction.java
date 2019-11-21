package servlets;

import constants.Constants;
import database.dao.AdministrationDao;
import parser.XMLParser;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/entry/admin/ConfirmAction")
public class ConfirmAction extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        AdministrationDao administrationDao = new AdministrationDao();

        String operation = request.getParameter(Constants.OPERATION);
        switch (operation){

            case Constants.DELETE: {
                delete(administrationDao,request,response);
                break;
            }
            case Constants.UPDATE:{
                update(administrationDao,request,response);
                break;
            }
            case Constants.ADD:{
                addNew(administrationDao,request,response);
                break;
            }
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    public  void update(AdministrationDao administrationDao, HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {

        String firstName = request.getParameter(Constants.FIRST_NAME);
        String lastName = request.getParameter(Constants.LAST_NAME);
        String login = request.getParameter(Constants.LOGIN);
        String role = request.getParameter(Constants.ROLE);
        int  id = Integer.valueOf(request.getParameter(Constants.UPDATE));
        String password = request.getParameter(Constants.PASSWORD);

        int result = administrationDao.updateAdministration(role,password, firstName, lastName, login, id);

        generateResponse(request,response,result,role,login);

    }

    public void delete(AdministrationDao administrationDao, HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/entry/admin/adminpage.jsp");
        PrintWriter out = response.getWriter();

        int idToDelete = Integer.valueOf(request.getParameter(Constants.DELETE));
        int result = administrationDao.deleteAdministration(idToDelete);

        switch (result) {
            case Constants.ZERO: {
                out.println("<font color=red>" + XMLParser.getMessages().get("no-librarian-or-admin-found") + idToDelete + ". </font>");
                dispatcher.include(request, response);
                return;
            }
            case Constants.ONE: {
                out.println("<font color=green>" + idToDelete + " " + XMLParser.getMessages().get("was-deleted")+"</font>");
                dispatcher.include(request, response);
                return;
            }

            case Constants.TWO: {
                out.println("<font color=red>"+XMLParser.getMessages().get("cannot-delete-connection-problem")+"</font>");
                dispatcher.include(request, response);
                return;
            }
        }
    }
    public void addNew (AdministrationDao administrationDao, HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {

        String firstName = request.getParameter(Constants.FIRST_NAME);
        String lastName = request.getParameter(Constants.LAST_NAME);
        String login = request.getParameter(Constants.LOGIN);
        String password = request.getParameter(Constants.PASSWORD);
        String role = request.getParameter(Constants.ROLE);

        int result = administrationDao.addNewAdministration(role, firstName, lastName, password, login);

       generateResponse(request,response,result,role,login);

    }

    public void generateResponse(HttpServletRequest request, HttpServletResponse response, int result, String role, String login) throws IOException, ServletException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/entry/admin/adminpage.jsp");
        PrintWriter out = response.getWriter();

        switch (result) {
            case Constants.ZERO: {
                out.println("<font color=red> "+ XMLParser.getMessages().get("login-already-exist") + login+"</font>");
                dispatcher.include(request, response);
                return;
            }
            case Constants.ONE: {
                out.println("<font color=green> " + role +" " + XMLParser.getMessages().get("was-saved") + login + "</font>");
                dispatcher.include(request, response);
                return;
            }

            case Constants.TWO: {
                out.println("<font color=red> " +XMLParser.getMessages().get("failed-connection-to-create") +" "+ role +"</font>");
                dispatcher.include(request, response);
                return;
            }
        }
    }
}
