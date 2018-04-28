<html>
<head>
    <link rel="stylesheet" type="text/css" href="/resources/css/style.css">
</head>
<body>
<a href="/home">Home</a>
<div id="header">
    <h4>Exception</h4>
</div>
<div id="content">
    <p>Error has happened</p>
    <#if message??>
        <p>${message}</p>
    <#else>
        <p>No message provided</p>
    </#if>
</div>
</body>
</html>