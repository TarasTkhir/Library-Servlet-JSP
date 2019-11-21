<%@ page import="parser.XMLParser" %>
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
    <title><%=RB.getString("login-page")%>
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
        * { box-sizing: border-box; }
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
<script type="text/javascript">
    function getCookie(name) {
        var match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
        if (match) return match[2];
    }

    function WriteCookie() {
        if (document.getElementById("setCookies").checked === true) {
            if (document.logIn.login.value == "") {
                alert("Login cannot be empty!");
                return;
            }
            cookievalue = escape(document.logIn.login.value) + ";";
            document.cookie = "login=" + cookievalue;
        }
    }

    onload = function () {
        var val = getCookie("login");
        if (val !== null)
            document.getElementById("login").value = val;
    }
</script>
<div class="inline-buttons">
<h1><%=RB.getString("library-service")%>
</h1>
<form action="Logining" method="post" name="logIn">
    <%=RB.getString("enter-login")%> : <br><input type="text" name="login" id="login"/><br><br><br>
    <%=RB.getString("enter-password")%> :<br><input type="password" name="password"/><br><br>
    <input  class="button" type="checkbox" id="setCookies"><%=RB.getString("save-login")%><br><br>
    <input class="button" type="submit" value="<%=RB.getString("log-in")%>" onclick="WriteCookie();">
</form>
</div>
</body>
</html>
