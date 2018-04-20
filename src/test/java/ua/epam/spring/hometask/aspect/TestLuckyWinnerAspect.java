package ua.epam.spring.hometask.aspect;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import ua.epam.spring.hometask.domain.Discount;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.service.impl.BookingServiceImpl;
import ua.epam.spring.hometask.service.interf.BookingService;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestLuckyWinnerAspect {

    private BookingService bookingService = mock(BookingServiceImpl.class);

    @Before
    public void init() {
        AspectJProxyFactory factory = new AspectJProxyFactory(bookingService);
        LuckyWinnerAspect luckyWinnerAspect = new LuckyWinnerAspect();
        factory.addAspect(luckyWinnerAspect);
        bookingService = factory.getProxy();
    }


    @Test
    public void ticketPriceShouldBeZeroAfterBookTicketCall() {
        Ticket ticket = new Ticket(null, null, null, 1, 40);

        LuckyWinnerAspect.bound = 1;
        bookingService.bookTickets(Collections.singletonList(ticket), Collections.singletonList(new Discount()));
        assertEquals(0, ticket.getPrice(), 0);
    }
}
