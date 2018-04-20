package ua.epam.spring.hometask.aspect;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import ua.epam.spring.hometask.domain.*;
import ua.epam.spring.hometask.service.impl.BookingServiceImpl;
import ua.epam.spring.hometask.service.impl.DiscountCounterServiceImpl;
import ua.epam.spring.hometask.service.interf.*;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestDiscountAspect {

    private DiscountCounterService discountCounterService = mock(DiscountCounterServiceImpl.class);
    private BookingService bookingService = mock(BookingServiceImpl.class);

    private User user;
    private List<Ticket> tickets;

    @Before
    public void init() {
        AspectJProxyFactory factory = new AspectJProxyFactory(bookingService);
        DiscountAspect discountAspect = new DiscountAspect(discountCounterService);
        factory.addAspect(discountAspect);
        bookingService = factory.getProxy();

        user = new User();

        Event event = new Event();
        this.tickets = Arrays.asList(new Ticket(user, event, null, 1, 0),
                new Ticket(user, event, null, 2, 0),
                new Ticket(user, event, null, 3, 0),
                new Ticket(user, event, null, 4, 0));
    }

    @Test
    public void countMethodShouldBeCalled_3_times_afterBookTicketsCall() {
        List<Discount> discount = Arrays.asList(new Discount("SomeStrategy", (byte) 5),
                new Discount("AnotherStrategy", (byte) 6),
                new Discount("SomeStrategy", (byte) 5),
                new Discount("noDiscount", (byte) 0));
        bookingService.bookTickets(tickets, discount);

        verify(discountCounterService, times(2)).count("SomeStrategy", user);
        verify(discountCounterService, times(1)).count("AnotherStrategy", user);
    }
}
