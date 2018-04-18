package ua.epam.spring.hometask.view;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.EventDate;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

import static com.lowagie.text.Element.ALIGN_CENTER;

/**
 * Created by Oleksii_Kovetskyi on 4/17/2018.
 */
@Component
public class EventPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(@Nonnull Map model, @Nonnull Document document,
									@Nonnull PdfWriter writer, @Nonnull HttpServletRequest request,
									@Nonnull HttpServletResponse response) throws Exception {
		Set<Event> events = (Set<Event>) model.get("eventList");

		if (events.isEmpty()) {
			document.add(new Paragraph("No events"));
		} else {
			Paragraph par = new Paragraph("Events");
			par.setAlignment("center");
			par.setSpacingAfter(5);
			document.add(par);
		}
		for (Event event : events) {
			PdfPTable outer = new PdfPTable(1);
			outer.setSpacingAfter(5);
			outer.setHorizontalAlignment(ALIGN_CENTER);

			PdfPTable inner1 = new PdfPTable(3);
			inner1.setHorizontalAlignment(ALIGN_CENTER);
			inner1.addCell("Name");
			inner1.addCell("Base Price");
			inner1.addCell("Rating");
			inner1.addCell(event.getName());
			inner1.addCell(String.valueOf(event.getBasePrice()));
			inner1.addCell(String.valueOf(event.getRating()));

			PdfPTable inner2 = new PdfPTable(2);
			inner2.setHorizontalAlignment(ALIGN_CENTER);
			inner2.addCell("Date");
			inner2.addCell("Auditorium");
			for (EventDate eventDate : event.getAirDates().values()) {
				inner2.addCell(String.valueOf(eventDate.getDateTime()));
				inner2.addCell(String.valueOf(eventDate.getAuditorium().getName()));
			}
			outer.addCell(inner1);
			outer.addCell(inner2);
			document.add(outer);
		}
	}
}