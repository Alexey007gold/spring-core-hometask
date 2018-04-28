<html>
<head>
    <link rel="stylesheet" type="text/css" href="/resources/css/style.css">
</head>
<body>
<a href="/home">Home</a>
<div id="header">
    <h4>Upload Events</h4>
</div>
<div>
    <form method="POST" enctype="multipart/form-data" action="/events/upload">
        <table>
            <tr><td>File to upload:</td><td><input type="file" name="file" /></td></tr>
            <tr><td></td><td><input type="submit" value="Upload" /></td></tr>
        </table>
    </form>
</div>
</body>
</html>