<%@ page import="parser.XMLParser" %>
<%@ page import="constants.Constants" %>
<%@ page import="entity.Book" %>
<%@ page import="java.util.Locale" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="parser.ResourceBundleForLibrary" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <%
        String lang = request.getLocale().getISO3Language();
        ResourceBundle RB = ResourceBundleForLibrary.getRB(lang);
    %>
    <title><%=RB.getString("change-user")%>
    </title>
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
    String action = (String) session.getAttribute(Constants.ACTION);
    String readonly = "";
    String idRow = "";
    String shouldHaveBeenReturned = "";

    String actionButton = RB.getString("confirm");
    if (action.equals(Constants.DELETE)) {
        readonly = "readonly";
        shouldHaveBeenReturned = RB.getString("book-must-be-returned") + " : <br><input type=\"date\"" +
                "name=\"must be returned\" value=\"" + book.getBookMustBeReturned() + "\"" + readonly + "><br><br>";
        idRow = RB.getString("ID") + " : <br> <input type=\"text\" name=\"ID\" value=\"" + book.getBookID() + "\"" + "readonly><br><br>";
        actionButton = RB.getString("delete");
    } else if (action.equals(Constants.UPDATE)) {
        actionButton = RB.getString("update");
    } else if (action.equals(Constants.ADD)) {
        actionButton = RB.getString("add");
    }
    session.removeAttribute(Constants.BOOK);
    session.removeAttribute(Constants.ACTION);
%>
<div class="inline-buttons">
    <form action="ConfirmActionBooks" method="post">
        <%=idRow%>
        <%=RB.getString("book-author")%> : <br><input type="text" name="book author"
                                                                     value="<%=book.getBookAuthor()%>"  <%=readonly%> required="required" pattern="[A-Za-z0-9]{1,20}"><br><br>
        <%=RB.getString("book-name")%> : <br><input type="text" name="book name"
                                                                   value="<%=book.getBookName()%>"  <%=readonly%> required="required" pattern="[A-Za-z0-9]{1,20}"><br><br>

        <%=RB.getString("book-year")%> : <br><input type="date" name="book year"
                                                                   value="<%=book.getBookYear()%>"  <%=readonly%> required="required" pattern="[A-Za-z0-9]{1,20}"><br><br>
        <%=shouldHaveBeenReturned%>

        <input hidden="hidden" name="<%=action%>" value="<%=book.getBookID()%>">
        <input hidden="hidden" name="<%=Constants.OPERATION%>" value="<%=action%>">

        <input class="button" type="submit" value="<%=actionButton%>">
        <input class="button" type="reset" onclick="history.back()" value="<%=RB.getString("cancel")%>">
    </form>
</div>
</body>
</html>