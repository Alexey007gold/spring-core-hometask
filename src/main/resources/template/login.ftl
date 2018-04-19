<html>
    <head>
        <title>Login Page</title>
        <style type="text/css">
            html, body {
                height: 100%;
            }
            html {
                display: table;
                margin: auto;
            }
            body {
                display: table-cell;
                vertical-align: middle;
            }
        </style>
    </head>
    <body onload="document.f.username.focus();">
        <h3>Login with your Credentials</h3>
        <#if error??>
            <p style="color:red;">Login failed</p>
        </#if>
        <#if logout??>
            <p style="color:green;">You have logged out</p>
        </#if>
        <form name="f" action="/login" method="POST">
            <table>
                <tbody>
                    <tr><td>User:</td><td><input type="text" name="username" value=""></td></tr>
                    <tr><td>Password:</td><td><input type="password" name="password"></td></tr>
                    <tr><td colspan="2"><input name="submit" type="submit" value="Login"></td></tr>
                </tbody>
            </table>
        </form>
    </body>
</html>