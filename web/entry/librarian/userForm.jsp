<%@ page import="parser.XMLParser" %>
<%@ page import="entity.User" %>
<%@ page import="constants.Constants" %>
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

<%User user = (User) request.getAttribute(Constants.AUTHENTICATION);%>
<%
    String action = (String) request.getAttribute(Constants.ACTION);
    String readonly = "";
    String idRow = "";
    String confirmPassword = "";
    String isActive = " <option value=\"true\">" + RB.getString("active") + "</option>\n" +
            "        <option value=\"false\">" + RB.getString("not-active") + "</option>\n" +
            "    </select>";
    String actionButton = RB.getString("confirm");
    if (action.equals(Constants.DELETE)) {
        idRow = RB.getString("ID") + " : <br> <input type=\"text\" name=\"ID\" value=\"" + user.getId() + "\"" + "readonly><br><br>";
        readonly = "readonly";
        isActive = "<br><input type=\"text\" name=\"activity\" value=\"" + user.getIsUserActive() + "\"" + readonly + "><br><br>";
        actionButton = RB.getString("delete");
    } else if (action.equals(Constants.UPDATE)) {
        isActive = "<select name=\"activity\" value=\"" + user.getIsUserActive() + "\">\n" + isActive;
        actionButton = RB.getString("update");
        confirmPassword = RB.getString("confirm-password") + ": <br><input type=\"password\" name=\"confirm password\"" +
                "value=\""+user.getPassword()+"\"   required=\"required\" pattern=\"[A-Za-z0-9]{3,20}\"><br><br>";
    } else if (action.equals(Constants.ADD)) {
        confirmPassword = RB.getString("confirm-password") + ": <br><input type=\"password\" name=\"confirm password\"" +
                "value=\"\"   required=\"required\" pattern=\"[A-Za-z0-9]{3,20}\"><br><br>";
        isActive = "<select name=\"activity\">\n" + isActive;
        actionButton = RB.getString("add");
    }
%>
<div class="inline-buttons">
    <form action="ConfirmActionUsers" method="post">
        <%=idRow%>
        <%=RB.getString("first-name")%> : <br><input type="text" name="first name"
                                                                    value="<%=user.getFirstName()%>" <%=readonly%>
                                                                    required="required" pattern="[A-Za-z0-9]{1,20}"><br><br>
        <%=RB.getString("last-name")%> : <br><input type="text" name="last name"
                                                                   value="<%=user.getLastName()%>"  <%=readonly%>
                                                                   required="required"
                                                                   pattern="[A-Za-z0-9]{1,20}"><br><br>
        <%=RB.getString("login")%> : <br><input type="text" name="login"
                                                                   value="<%=user.getLogin()%>"  <%=readonly%>
                                                                   required="required"
                                                                   pattern="[A-Za-z0-9]{3,20}"><br><br>

        <%=RB.getString("birth-day")%> : <br><input type="date" name="date"
                                                                   value="<%=user.getBirthDay()%>"  <%=readonly%>><br><br>
        <%=RB.getString("email")%> : <br><input type="email" name="email"
                                                               value="<%=user.getEmail()%>"  <%=readonly%>
                                                               required="required" "><br><br>
        <%=RB.getString("password")%> : <br><input type="password" name="password"
                                                                  value="<%=user.getPassword()%>"  <%=readonly%>
                                                                  required="required"
                                                                  pattern="[A-Za-z0-9]{3,20}"><br><br>
        <%=confirmPassword%>
        <%=RB.getString("user-activity")%><br>
        <%=isActive%><br><br>
        <input hidden="hidden" name="<%=action%>" value="<%=user.getId()%>">
        <input hidden="hidden" name="<%=Constants.OPERATION%>" value="<%=action%>">

        <input class="button" type="submit" value="<%=actionButton%>">
        <input class="button" type="reset" onclick="history.back()" value="<%=RB.getString("cancel")%>">
    </form>
</div>
</body>
</html>
