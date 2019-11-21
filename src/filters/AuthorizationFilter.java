package filters;

import constants.Constants;
import entity.AuthenticationUser;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

import static entity.AuthenticationUser.ROLE.ADMIN;
import static entity.AuthenticationUser.ROLE.LIBRARIAN;

public class AuthorizationFilter implements Filter {

    Map<String, List<String>> pages = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) {
        String[] adminPages = filterConfig.getInitParameter(ADMIN.toString()).split(";");
        String[] librarianPages = filterConfig.getInitParameter(LIBRARIAN.toString()).split(";");
        pages.put(ADMIN.toString(), new ArrayList<>(Arrays.asList(adminPages)));
        pages.put(LIBRARIAN.toString(), new ArrayList<>(Arrays.asList(librarianPages)));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpSession session = httpServletRequest.getSession();

        AuthenticationUser authenticationUser = (AuthenticationUser) session.getAttribute(Constants.AUTHENTICATION);

        AuthenticationUser.ROLE role = authenticationUser.getRole();
        String requestURI = httpServletRequest.getRequestURI();

        switch (role) {
            case ADMIN: {
                List<String> adminPages = pages.get(ADMIN.toString());
                boolean found = false;
                for (int i = 0; i < adminPages.size(); i++) {
                    if (requestURI.contains(adminPages.get(i))) {
                        found = true;
                        break;
                    }
                }
                if(!found)
                {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                break;
            }
            case LIBRARIAN: {
                List<String> librarianPages = pages.get(LIBRARIAN.toString());
                boolean found = false;
                for (int i = 0; i < librarianPages.size(); i++) {
                    if (requestURI.contains(librarianPages.get(i))) {
                        found = true;
                        break;
                    }
                }
                if(!found)
                {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                break;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }
}
