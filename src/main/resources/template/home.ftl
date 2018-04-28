<html>
<head>
    <link rel="stylesheet" type="text/css" href="/resources/css/style.css">
</head>
<body>
<div id="header">
    <h4>Home Page</h4>
</div>
<div id="content">
    <a href="/events/coming?until=999999999999999">Coming Events</a>
    <a href="/tickets/booked">My tickets</a>
    <#if model["admin"]==true>
        <a href="/users/upload">Upload Users</a>
        <a href="/events/upload">Upload Events</a>
    </#if>
</div>
</body>
</html>