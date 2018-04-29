<html>
<head>
    <link rel="stylesheet" type="text/css" href="/resources/css/style.css">
</head>
    <body>
        <a href="/home">Home</a>
        <div id="header">
            <h4>Users</h4>
        </div>
        <div id="content">
            <table class="datatable">
                <tr>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th>BirthDate</th>
                </tr>
            <#list userList as user>
              <tr>
                  <td>${user.firstName}</td>
                  <td>${user.lastName}</td>
                  <td>${user.email}</td>
                  <td>${user.birthDate}</td>
              </tr>
            </#list>
            </table>
        </div>
    </body>
</html>