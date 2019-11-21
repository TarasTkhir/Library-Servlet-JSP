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

@WebServlet("/entry/librarian/RedirectActionToFormBook")
public class RedirectActionToFormBook extends HttpServlet {

    BookDao bookDao = new BookDao();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String idToDelete = request.getParameter(Constants.DELETE);
        String idToUpdate = request.getParameter(Constants.UPDATE);
        String action = request.getParameter(Constants.ACTION);
        HttpSession session = request.getSession();

        Book byId = null;

        switch (action) {

            case Constants.DELETE: {
                if (null != idToDelete) {
                    int id = Integer.parseInt(idToDelete);
                    byId = bookDao.findById(id);
                }
                if (null != byId) {
                    session.setAttribute(Constants.BOOK, byId);
                    session.setAttribute(Constants.ACTION, Constants.DELETE);
                }
                break;
            }
            case Constants.UPDATE: {
                if (null != idToUpdate) {
                    int id = Integer.parseInt(idToUpdate);
                    byId = bookDao.findById(id);
                }
                if (null != byId) {
                    session.setAttribute(Constants.BOOK, byId);
                    session.setAttribute(Constants.ACTION, Constants.UPDATE);
                }
                break;
            }
            case Constants.ADD: {
                session.setAttribute(Constants.BOOK, new Book(0, 0, "", "", null, null, false));
                session.setAttribute(Constants.ACTION, Constants.ADD);
                break;
            }
        }

        RequestDispatcher view = request.getRequestDispatcher("/entry/librarian/bookForm.jsp");
        view.forward(request, response);
        return;
    }

}
