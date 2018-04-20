package ua.epam.spring.hometask.aspect;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import ua.epam.spring.hometask.dao.EventDAOImpl;
import ua.epam.spring.hometask.dao.interf.EventDAO;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.service.impl.BookingServiceImpl;
import ua.epam.spring.hometask.service.impl.EventCounterStatsServiceImpl;
import ua.epam.spring.hometask.service.interf.BookingService;
import ua.epam.spring.hometask.service.interf.EventCounterStatsService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestCounterAspect {

    private EventDAO eventDAO = mock(EventDAOImpl.class);
    private EventCounterStatsService eventCounterStatsService = mock(EventCounterStatsServiceImpl.class);
    private BookingService bookingService = mock(BookingServiceImpl.class);

    private List<Ticket> tickets;
    private Event event;

    @Before
    public void init() {
        AspectJProxyFactory factory = new AspectJProxyFactory(eventDAO);
        CounterAspect counterAspect = new CounterAspect(eventCounterStatsService);
        factory.addAspect(counterAspect);
        eventDAO = factory.getProxy();

        factory = new AspectJProxyFactory(bookingService);
        factory.addAspect(counterAspect);
        bookingService = factory.getProxy();

        event = new Event();
        event.setName("Titanik");

        tickets = Arrays.asList(new Ticket(null, event, null, 0, 0),
                new Ticket(null, event, null, 0, 0));
    }

    @Test
    public void accessByNameCountShouldBeOne() {
        Event event = eventDAO.getByName("Titanik");
        verify(eventCounterStatsService, times(1)).incrementAccessByNameCount(event);
        verify(eventCounterStatsService, times(0)).incrementPriceQueryCount(any());
        verify(eventCounterStatsService, times(0)).incrementTicketsBookedTimesCount(any());
    }

    @Test
    public void priceQueryCountShouldBeOne() {
        bookingService.getTicketsPriceWithDiscount(event, null, null, null);

        verify(eventCounterStatsService, times(0)).incrementAccessByNameCount(any());
        verify(eventCounterStatsService, times(1)).incrementPriceQueryCount(event);
        verify(eventCounterStatsService, times(0)).incrementTicketsBookedTimesCount(any());
    }

    @Test
    public void bookedTicketsCountShouldBeTwo() {
        bookingService.bookTickets(tickets, null);

        verify(eventCounterStatsService, times(0)).incrementAccessByNameCount(any());
        verify(eventCounterStatsService, times(0)).incrementPriceQueryCount(any());
        verify(eventCounterStatsService, times(2)).incrementTicketsBookedTimesCount(event);
    }
}
