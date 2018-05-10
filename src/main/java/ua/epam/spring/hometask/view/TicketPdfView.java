package ua.epam.spring.hometask.view;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.view.writer.TicketsPdfWriter;

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

	public static final String TICKET_LIST = "ticketList";

	@Override
	protected void buildPdfDocument(@Nonnull Map model, @Nonnull Document document,
									@Nonnull PdfWriter writer, @Nonnull HttpServletRequest request,
									@Nonnull HttpServletResponse response) throws Exception {
		Set<Ticket> tickets = (Set<Ticket>) model.get(TICKET_LIST);
		if (tickets.isEmpty()) {
			document.add(new Paragraph("No tickets"));
		} else {
            TicketsPdfWriter.write(document, tickets);
		}
	}
}