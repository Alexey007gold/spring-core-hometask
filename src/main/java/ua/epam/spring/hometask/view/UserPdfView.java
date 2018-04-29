package ua.epam.spring.hometask.view;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import ua.epam.spring.hometask.domain.User;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Oleksii_Kovetskyi on 4/17/2018.
 */
@Component
public class UserPdfView extends AbstractPdfView {

	public static final String USER_LIST = "userList";

	@Override
	protected void buildPdfDocument(@Nonnull Map model, @Nonnull Document document,
									@Nonnull PdfWriter writer, @Nonnull HttpServletRequest request,
									@Nonnull HttpServletResponse response) throws Exception {
		List<User> users = (List<User>) model.get(USER_LIST);
		if (users.isEmpty()) {
			document.add(new Paragraph("No users"));
		} else {
			Paragraph par = new Paragraph("Users");
			par.setAlignment("center");
			document.add(par);

			Table table = new Table(4);
			table.setPadding(5);
			table.setAlignment("center");

			table.addCell("First Name");
			table.addCell("Last Name");
			table.addCell("Email");
			table.addCell("Birth Date");

			for (User user : users) {
				table.addCell(user.getFirstName());
				table.addCell(user.getLastName());
				table.addCell(user.getEmail());
				table.addCell(user.getBirthDate().toString());
			}

			document.add(table);
		}
	}
}