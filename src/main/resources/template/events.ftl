<html>
    <head>
        <link rel="stylesheet" type="text/css" href="/resources/css/style.css">
    </head>
    <body>
        <div id="header">
            <h4>Events</h4>
        </div>
        <div id="content">
            <table class="datatable">
                <tr>
                    <th>Name</th>
                    <th>Base Price</th>
                    <th>Rating</th>
                    <th>Air Date</th>
                </tr>
            <#list model["eventList"] as event>
                <tr>
                    <td>${event.name}</td>
                    <td>${event.basePrice}</td>
                    <td>${event.rating}</td>
                    <td>
                        <table class="datatable">
                            <tr>
                                <th>Date</th>
                                <th>Auditorium</th>
                            </tr>
                            <#list event.airDates as date, eventDate>
                                <tr>
                                    <td>${date}</td>
                                    <td>${eventDate.auditorium.name}</td>
                                </tr>
                            </#list>
                        </table>
                    </td>
                </tr>
            </#list>
            </table>
        </div>
    </body>
</html>