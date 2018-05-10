package ua.epam.spring.hometask.view.writer;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import ua.epam.spring.hometask.domain.Ticket;

import java.util.Collection;

/**
 * Created by Oleksii_Kovetskyi on 5/9/2018.
 */
public class TicketsPdfWriter {

    public static void write(Document doc, Collection<Ticket> tickets) throws DocumentException {
        Paragraph par = new Paragraph("Tickets");
        par.setAlignment("center");
        doc.add(par);

        Table table = new Table(5);
        table.setPadding(5);
        table.setAlignment("center");

        table.addCell("User");
        table.addCell("Event");
        table.addCell("Date");
        table.addCell("Seat");
        table.addCell("Price");

        for (Ticket ticket : tickets) {
            if (ticket.getUser() != null) {
                table.addCell(ticket.getUser().getFirstName() + " " + ticket.getUser().getLastName());
            } else {
                table.addCell("");
            }
            table.addCell(ticket.getEvent().getName());
            table.addCell(String.valueOf(ticket.getDateTime()));
            table.addCell(String.valueOf(ticket.getSeat()));
            table.addCell(String.valueOf(ticket.getPrice()));
        }

        doc.add(table);
    }
}
