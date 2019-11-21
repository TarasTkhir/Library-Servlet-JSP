<%@ page import="parser.XMLParser" %>
<%@ page import="database.dao.BookDao" %>
<%@ page import="entity.Book" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="java.util.Locale" %>
<%@ page import="parser.ResourceBundleForLibrary" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%
        String lang = request.getLocale().getISO3Language();
        ResourceBundle RB = ResourceBundleForLibrary.getRB(lang);
    %>
    <title><%=RB.getString("all-books")%></title>
    <style>
        body {
            background-color: #f6f6ff;
            font-family: Calibri, Myriad;
        }

        #main {
            width: 80%;
        }

        table.timecard {
            width:90%;
            border: 3px solid #f79646;
            border-style: hidden;
        }
        table.pages{

            border: 1px solid #f79646;
            border-style: dashed;
        }

        table.pages td{
            border: 1px solid #f79646;
            border-style: dashed;
        }

        table.timecard caption {
            background-color: #f79646;
            color: #fff;
            font-size: x-large;
            font-weight: bold;
            letter-spacing: .3em;
        }

        table.timecard thead th {
            padding: 8px;
            background-color: #fde9d9;
            font-size: large;
        }

        table.timecard thead th#thDay {
            width: 60%;
        }

        table.timecard thead th#thRegular, table.timecard thead th#thOvertime, table.timecard thead th#thTotal {
            width: 30%;
        }

        table.timecard th, table.timecard td {
            padding: 3px;
            border-width: 1px;
            border-style: solid;
            border-color: #f79646 #ccc;
        }

        table.timecard td {
            text-align: center;
        }

        table.timecard tbody th {
            text-align: left;
            font-weight: normal;
        }

        table.timecard tr.even {
            background-color: #fde9d9;
        }
    </style>
</head>
<body>
<%@ include file = "../header.jsp" %>

<br>
<%
    BookDao boookDao = new BookDao();
    int page1 = 1;
    int recordsPerPage = 3;
    if (request.getParameter("page1") != null) {
        page1 = Integer.parseInt(request.getParameter("page1"));
    }
    List<Book> allBooks = boookDao.paginationQueryJustForBooks((page1 - 1) * recordsPerPage,
            recordsPerPage);
    int noOfRecords = boookDao.getNumberOfRecords();
    int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
    request.setAttribute("allBooks", allBooks);
    request.setAttribute("noOfPages", noOfPages);
    request.setAttribute("currentPage", page1);%>

<br>
<div id="main">
<table border="1" bgcolor="#deb887" cellpadding="5" cellspacing="5" width="80%" class="timecard">
    <caption><%=RB.getString("all-books")%></caption>
    <thead>
    <tr>
        <th><%=RB.getString("ID")%>

        <th><%=RB.getString("book-author")%>
        </th>
        <th><%=RB.getString("book-name")%>
        </th>
        <th><%=RB.getString("book-year")%>
        </th>
        <th><%=RB.getString("book-status")%>
        </th>
        <th><%=RB.getString("update")%>
        </th>
        <th><%=RB.getString("delete")%>
        </th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${allBooks}" var="book">
        <tr class="even">
            <td><c:out value=" ${book.bookID}"/></td>
            <td><c:out value="${book.bookAuthor}"/></td>
            <td><c:out value="${book.bookName}"/></td>
            <td><c:out value="${book.bookYear}"/></td>
            <td><c:out value="${book.bookStatus}"/></td>

            <td>
                <form style="text-decoration-color: chocolate; display:inline-block;"
                      action="<%=session.getServletContext().getContextPath()%>/entry/librarian/RedirectActionToFormBook"
                      method="post">
                    <input type="hidden" name="<%=Constants.UPDATE%>" value="${book.bookID}">
                    <input type="hidden" name="<%=Constants.ACTION%>" value="<%=Constants.UPDATE%>">
                    <input type="submit" value="<%=RB.getString("update")%>">

                </form>
            </td>
            <td>
                <form style="text-decoration-color: chocolate; display:inline-block;"
                      action="<%=session.getServletContext().getContextPath()%>/entry/librarian/RedirectActionToFormBook"
                      method="post">
                    <input type="hidden" name="<%=Constants.DELETE%>" value="${book.bookID}">
                    <input type="hidden" name="<%=Constants.ACTION%>" value="<%=Constants.DELETE%>">
                    <input type="submit" value="<%=RB.getString("delete")%>">
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<c:if test="${currentPage != 1}">
    <td>
        <a href="<%=session.getServletContext().getContextPath()%>/entry/librarian/books.jsp?page1=${currentPage - 1}"><%=RB.getString("previous")%>
        </a></td>
</c:if>

<table border="1" cellpadding="5" cellspacing="5" class="pages">
    <tr>
        <c:forEach begin="1" end="${noOfPages}" var="i">
            <c:choose>
                <c:when test="${currentPage eq i}">
                    <td class="pages">${i}</td>
                </c:when>
                <c:otherwise>
                    <td class="pages">
                        <a href="<%=session.getServletContext().getContextPath()%>/entry/librarian/books.jsp?page1=${i}">${i}</a>
                    </td>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </tr>
</table>

<c:if test="${currentPage lt noOfPages}">
    <td>
        <a href="<%=session.getServletContext().getContextPath()%>/entry/librarian/books.jsp?page1=${currentPage + 1}"><%=RB.getString("next")%>
        </a></td>
</c:if>
<br>
<br>
<form style="width:600px; size: 40px; display:inline-block;"
      action="<%=session.getServletContext().getContextPath()%>/entry/librarian/RedirectActionToFormBook" method="post">
    <input type="hidden" name="<%=Constants.ACTION%>" value="<%=Constants.ADD%>">
    <input type="submit" name="<%=RB.getString("add")%>" value="<%=RB.getString("add")%>">
</form>
    </div>
<%@ include file = "../footer.jsp" %>
</body>
</html>
