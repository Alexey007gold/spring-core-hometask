<html>
    <head>
        <link rel="stylesheet" type="text/css" href="/resources/css/style.css">
        <script>
            function onBookClicked(eventId, time) {
                var seats = prompt("Please enter seats to book");
                window.location.href = "http://localhost:8080/tickets/book?eventId=" + eventId + "&time=" + time + "&seats=" + seats;
            }
        </script>
    </head>
    <body>
        <a href="/home">Home</a>
        <div id="header">
            <h4>Events</h4>
        </div>
        <div id="content">
            <table class="datatable">
                <tr>
                    <th>Id</th>
                    <th>Name</th>
                    <th>Base Price</th>
                    <th>Rating</th>
                    <th>Air Date</th>
                </tr>
            <#list eventList as event>
                <tr>
                    <td>${event.id}</td>
                    <td>${event.name}</td>
                    <td>${event.basePrice}</td>
                    <td>${event.rating}</td>
                    <td>
                        <table class="datatable">
                            <tr>
                                <th>Date</th>
                                <th>Auditorium</th>
                                <th></th>
                                <th></th>
                            </tr>
                            <#list event.airDates as date, eventDate>
                                <tr>
                                    <td>${date}</td>
                                    <td>${eventDate.auditorium.name}</td>
                                    <td><a href="/tickets/available?eventId=${event.id}&time=${eventDate.getDateTimeAsSeconds()?c}">Available seats</a> </td>
                                    <td><a onclick="onBookClicked(${event.id}, ${eventDate.getDateTimeAsSeconds()?c})">Book</a> </td>
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