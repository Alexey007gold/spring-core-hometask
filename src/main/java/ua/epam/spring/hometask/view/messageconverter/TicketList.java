package ua.epam.spring.hometask.view.messageconverter;

import ua.epam.spring.hometask.domain.Ticket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Oleksii_Kovetskyi on 5/9/2018.
 */
public class TicketList {

    private List<Ticket> ticketList;

    public TicketList(Collection<Ticket> collection) {
        this.ticketList = new ArrayList<>(collection);
    }

    public List<Ticket> getTickets() {
        return ticketList;
    }

    public void setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }
}
