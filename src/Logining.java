import constants.Constants;
import database.dao.AdministrationDao;
import entity.AuthenticationUser;
import parser.XMLParser;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;


@WebServlet("/Logining")
public class Logining extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        AdministrationDao administrationDao = new AdministrationDao();

        String login = request.getParameter(Constants.LOGIN);
        String password = request.getParameter(Constants.PASSWORD);
        AuthenticationUser byLoginAndPassword = null;
        try {
            byLoginAndPassword = administrationDao.findByLoginAndPassword(login, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (null == byLoginAndPassword) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            PrintWriter out = response.getWriter();
            out.println("<font color=red>"+ XMLParser.getMessages().get("name-or-password-is-wrong") +"</font>");
            dispatcher.include(request, response);
            return;
        }

            HttpSession session = request.getSession();
            byLoginAndPassword.setPassword(null);
            session.setAttribute(Constants.AUTHENTICATION, byLoginAndPassword);
            session.setMaxInactiveInterval(30 * 60);
//            Cookie userName = new Cookie(Constants.LOGIN, login);
//            userName.setMaxAge(30 * 60);
//            response.addCookie(userName);
            if(byLoginAndPassword.getRole() == AuthenticationUser.ROLE.ADMIN){
                response.sendRedirect(getServletContext().getContextPath() +"/entry/admin/adminpage.jsp");
                }
            else if (byLoginAndPassword.getRole() == AuthenticationUser.ROLE.LIBRARIAN){
                response.sendRedirect(getServletContext().getContextPath() + "/entry/librarian/librarianpage.jsp");}
            else{
                response.sendRedirect(getServletContext().getContextPath() + "/errors/accessDenied.jsp");}

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
