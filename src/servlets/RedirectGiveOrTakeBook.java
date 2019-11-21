package servlets;

import constants.Constants;
import database.dao.BookDao;
import entity.Book;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/entry/librarian/RedirectGiveOrTakeBook")
public class RedirectGiveOrTakeBook extends HttpServlet {

    BookDao bookDao = new BookDao();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        String idToGive = request.getParameter(Constants.GIVE);
        String idToTake =request.getParameter(Constants.TAKE);
        String action = request.getParameter(Constants.ACTION);
        String page1 = request.getParameter("page1");
        Book byId = null;
        switch (action) {

            case Constants.TAKE: {
                if (null != idToTake) {
                    int id = Integer.parseInt(idToTake);
                    byId = bookDao.tskeBookWithUserByID(id);
                }
                if (null != byId) {
                    session.setAttribute(Constants.BOOK, byId);
                    session.setAttribute(Constants.ACTION, Constants.TAKE);
                    session.setAttribute("page1",page1);
                }
                break;
            }
            case Constants.GIVE: {
                if (null != idToGive) {
                    int id = Integer.parseInt(idToGive);
                    byId = bookDao.tskeBookWithUserByID(id);
                }
                if (null != byId) {
                    session.setAttribute(Constants.BOOK, byId);
                    session.setAttribute(Constants.ACTION, Constants.GIVE);
                    session.setAttribute("page1",page1);
                }
                break;
            }
        }

        RequestDispatcher view = request.getRequestDispatcher("/entry/librarian/giveOrTakeBookForm.jsp");
        view.forward(request, response);
    }
}
