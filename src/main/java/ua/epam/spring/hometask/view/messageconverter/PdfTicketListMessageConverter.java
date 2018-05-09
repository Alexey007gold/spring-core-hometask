package ua.epam.spring.hometask.view.messageconverter;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import ua.epam.spring.hometask.view.writer.TicketsPdfWriter;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Oleksii_Kovetskyi on 5/9/2018.
 */
public class PdfTicketListMessageConverter extends AbstractHttpMessageConverter<TicketList> {

    public PdfTicketListMessageConverter() {
        super(MediaType.APPLICATION_PDF);
    }

    @Override
    protected boolean supports(@Nonnull Class<?> clazz) {
        return TicketList.class.isAssignableFrom(clazz);
    }

    @Override
    protected TicketList readInternal(@Nonnull Class<? extends TicketList> clazz, @Nonnull HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    protected void writeInternal(@Nonnull TicketList ticketList, @Nonnull HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document doc = new Document(PageSize.A4, 50, 50, 50, 50);
            PdfWriter.getInstance(doc, out);

            doc.open();
            TicketsPdfWriter.write(doc, ticketList.getTickets());
            doc.close();

            outputMessage.getBody().write(out.toByteArray());
        } catch (DocumentException e) {
            throw new HttpMessageNotWritableException(e.getMessage());
        }
    }

    @Override
    protected boolean canRead(MediaType mediaType) {
        return false;
    }
}
