<html>
<head>
    <link rel="stylesheet" type="text/css" href="/resources/css/style.css">
</head>
<body>
<div id="header">
    <h4>Tickets</h4>
</div>
<div id="content">
    <table class="datatable">
        <tr>
            <th>User</th>
            <th>Event</th>
            <th>Date</th>
            <th>Seat</th>
            <th>Price</th>
        </tr>
            <#list model["ticketList"] as ticket>
              <tr>
                  <#if (ticket.user)??>
                      <td>${ticket.user.getFirstName()} ${ticket.user.getLastName()}</td>
                  <#else>
                      <td></td>
                  </#if>
                  <td>${ticket.event.getName()}</td>
                  <td>${ticket.getDateTime()}</td>
                  <td>${ticket.getSeat()}</td>
                  <td>${ticket.getPrice()}</td>
              </tr>
            </#list>
    </table>
</div>
</body>
</html>