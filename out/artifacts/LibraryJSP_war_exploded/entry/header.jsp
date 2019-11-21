<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="parser.XMLParser" %>
<%@ page import="entity.AuthenticationUser" %>
<%@ page import="constants.Constants" %>
<meta name="viewport" content="width=device-width, initial-scale=1">
<style>
    .navbar {
        overflow: hidden;
        background-color: #333;
        position: fixed;
        top: 0;
        width: 100%;
    }

    .navbar a {
        float: left;
        display: block;
        color: #f2f2f2;
        text-align: center;
        padding: 14px 16px;
        text-decoration: none;
        font-size: 17px;
    }

    .navbar a:hover {
        background: #ddd;
        color: black;
    }

</style>

<div class="navbar">
    <a href="librarianpage.jsp"><%=RB.getString("visitors")%>
    </a>
    <a href="books.jsp"><%=RB.getString("books")%>
    </a>
    <a href="issuingBooks.jsp"><%=RB.getString("issuing-books")%>
    </a>
    <a href="shouldHaveBeenReturned.jsp"><%=RB.getString("books-that-should-have-been-returned")%>
    </a>
    <form action="<%=session.getServletContext().getContextPath()%>/Logout" method="post">
        <a href="javascript:;" onclick="parentNode.submit();"><%=RB.getString("logout")%>
            <input type="hidden" name="mess" value="hello">
        </a>
    </form>
</div>
<br><br><br><br>
<%
    AuthenticationUser librarian = (AuthenticationUser) session.getAttribute(Constants.AUTHENTICATION);
%>

<div>
    <h4><%=RB.getString("hello")%> : <%=librarian.getLogin()%>
    </h4>
</div>

<%=RB.getString("librarian")%>:  <%=" " + librarian.getFirstName() + " " + librarian.getLastName() + "" %>

