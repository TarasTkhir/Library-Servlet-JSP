<%@ page import="parser.XMLParser" %>
<%@ page import="constants.Constants" %>
<%@ page import="entity.Book" %>
<%@ page import="database.dao.UserDao" %>
<%@ page import="entity.User" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="parser.ResourceBundleForLibrary" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page  pageEncoding="UTF-8" %>
<html>
<head>
    <%
        String lang = request.getLocale().getISO3Language();
        ResourceBundle RB = ResourceBundleForLibrary.getRB(lang);
    %>
    <title><%=RB.getString("change-user")%>
    </title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>jQuery UI Autocomplete - Default functionality</title>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <link rel="stylesheet" href="/resources/demos/style.css">
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <style>
        .button {
            margin-left: 15px;
            margin-right: 15px;
        }

        .inline-buttons {
            display: table;
            margin: 0 auto;
        }

        @media only screen and (max-width: 960px) {

            .button {
                width: 100%;
                margin: 20px;
                text-align: center;
            }

        }

        * {
            box-sizing: border-box;
        }

        body {
            font: 16px Arial;
        }

        .autocomplete {
            position: relative;
            display: inline-block;
        }

        input {
            border: 1px solid transparent;
            background-color: #f1f1f1;
            padding: 10px;
            font-size: 16px;
        }

        input[type=text] {
            background-color: #f1f1f1;
            width: 100%;
        }

        input[type=submit] {
            background-color: DodgerBlue;
            color: #fff;
        }

        input[type=reset] {
            background-color: DodgerBlue;
            color: #fff;
        }

        .autocomplete-items {
            position: absolute;
            border: 1px solid #d4d4d4;
            border-bottom: none;
            border-top: none;
            z-index: 99;
            top: 100%;
            left: 0;
            right: 0;
        }

        .autocomplete-items div {
            padding: 10px;
            cursor: pointer;
            background-color: #fff;
            border-bottom: 1px solid #d4d4d4;
        }

        .autocomplete-items div:hover {
            background-color: #e9e9e9;
        }
    </style>
</head>
<body>

<%Book book = (Book) session.getAttribute(Constants.BOOK);%>
<%
    UserDao userDao = new UserDao();
    List<User> usersList = userDao.findAll();
    request.setAttribute("usersList", usersList);
    String action = (String) session.getAttribute(Constants.ACTION);
    String page1 = (String) session.getAttribute("page1");
    String readonly = "";
    String readonly1 = "";
    String firstNameAndLastName = "";
    String returnedRow = "";

    String error = (String) session.getAttribute("idError");
    System.out.println(error);
    String actionButton = RB.getString("confirm");
    if (action.equals(Constants.GIVE)) {
        readonly = "readonly";
        returnedRow = RB.getString("book-must-be-returned-after-days") + " <br><input type=\"number\" name=\"must be returned\" value=\"" + "" + "\" pattern=\"[0-9]{1,2}\"><br><br>";
        actionButton = RB.getString("give");
    } else if (action.equals(Constants.TAKE)) {
        readonly = "readonly";
        readonly1 = "readonly";
        returnedRow = RB.getString("book-must-be-returned") + ": <br><input type=\"date\" name=\"must be returned\" value=\"" + book.getBookMustBeReturned() + "\"readonly><br><br>";
        firstNameAndLastName = book.getUser().getFirstName() + " " + book.getUser().getLastName();
        actionButton = RB.getString("take");
    }
%>

<script>

    var visitors = [""];

    <c:forEach items="${usersList}" var="user">
    visitors.push("${user.firstName} ${user.lastName} ID:${user.id}");
    </c:forEach>

    $(function () {
        var availableTags = visitors;
        $("#tags").autocomplete({
            source: availableTags
        });
    });

</script>
<div class="inline-buttons">
    <form autocomplete="off" action="ConfirmActionTakeOrGiveBook" method="post">
        <div class="ui-widget">
            <%=RB.getString("visitor-id")%> : <br><input id="tags" type="text" name="Visitors"
                                                                        placeholder="Visitor"
                                                                        value="<%=firstNameAndLastName%>" <%=readonly1%>>
        </div>
        <br><br>
        <%=RB.getString("book-author")%> : <br><input type="text" name="book author"
                                                                     value="<%=book.getBookAuthor()%>"  <%=readonly%> required="required" pattern="[A-Za-z0-9]{1,20}"><br><br>
        <%=RB.getString("book-name")%> : <br><input type="text" name="book name"
                                                                   value="<%=book.getBookName()%>"  <%=readonly%> required="required" pattern="[A-Za-z0-9]{1,20}">
<%--        <c:if test="${not empty error}"><span><%=error%></span></c:if><br><br>--%>

        <%=RB.getString("book-year")%> : <br><input type="date" name="book year"
                                                                   value="<%=book.getBookYear()%>"  <%=readonly%>><br><br>

        <%=returnedRow%>
        <input hidden="hidden" name="<%=action%>" value="<%=book.getBookID()%>">
        <input hidden="hidden" name="<%=Constants.OPERATION%>" value="<%=action%>">
        <input hidden="hidden" name="page1" value="<%=page1%>">
        <input class="button" type="submit" value="<%=actionButton%>">
        <input class="button" type="reset" onclick="history.back()" value="<%=RB.getString("cancel")%>">
    </form>
</div>
</body>
</html>