package ua.epam.spring.hometask.view;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import ua.epam.spring.hometask.domain.Ticket;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

/**
 * Created by Oleksii_Kovetskyi on 4/17/2018.
 */
@Component
public class TicketPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(@Nonnull Map model, @Nonnull Document document,
									@Nonnull PdfWriter writer, @Nonnull HttpServletRequest request,
									@Nonnull HttpServletResponse response) throws Exception {
		Set<Ticket> tickets = (Set<Ticket>) model.get("ticketList");
		if (tickets.isEmpty()) {
			document.add(new Paragraph("No tickets"));
		} else {
            Paragraph par = new Paragraph("Tickets");
            par.setAlignment("center");
            document.add(par);

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

			document.add(table);
		}
	}
}