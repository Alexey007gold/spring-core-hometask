package ua.epam.spring.hometask.service.impl.discount.strategy;

import org.junit.Before;
import org.junit.Test;
import ua.epam.spring.hometask.domain.Discount;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.impl.UserServiceImpl;
import ua.epam.spring.hometask.service.interf.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Oleksii_Kovetskyi on 4/5/2018.
 */
public class TestEvery10TicketStrategy {

    private final LocalDateTime dateTime = LocalDateTime.of(4018, 4, 4, 10, 20);
    private DiscountStrategy discountStrategy;
    private UserService userService = mock(UserServiceImpl.class);

    private User user1;
    private User user2;
    private Event event;

    @Before
    public void init() {
        discountStrategy = new Every10TicketStrategy(userService);
        user1 = new User();
        user1.setId(1L);

        event = new Event();
        event.setName("Event");

        NavigableSet<Ticket> tickets = new TreeSet<>();
        for (int i = 0; i < 7; i++) {
            tickets.add(new Ticket(null, event, LocalDateTime.now(), i, 0));
        }
        user1.setTickets(tickets);

        user2 = new User();
        user2.setId(2L);

        when(userService.getById(user1.getId())).thenReturn(user1);
        when(userService.getById(user2.getId())).thenReturn(user2);
    }

    @Test
    public void shouldReturn_0_OnGetDiscountCallForUser1() {
        when(userService.getById(user1.getId())).thenReturn(user1);
        List<Discount> discount = discountStrategy.getDiscount(user1, event, dateTime, 2);

        for (Discount aDiscount : discount) {
            assertEquals(0, aDiscount.getPercent());
        }
    }

    @Test
    public void shouldReturn_50_ForThirdTicket_OnGetDiscountCallForUser1() {
        List<Discount> discount = discountStrategy.getDiscount(user1, event, dateTime, 3);

        for (int i = 0; i < 2; i++) {
            assertEquals(0, discount.get(i).getPercent());
        }
        assertEquals(50, discount.get(2).getPercent());
    }

    @Test
    public void shouldReturn_0_OnGetDiscountCallForUser2() {
        List<Discount> discount = discountStrategy.getDiscount(user2, event, dateTime, 9);

        for (int i = 0; i < discount.size(); i++) {
            assertEquals(0, discount.get(i).getPercent());
        }
    }

    @Test
    public void shouldReturn_50_ForTenthTicket_OnGetDiscountCallForUser2() {
        List<Discount> discount = discountStrategy.getDiscount(user2, event, dateTime, 10);

        for (int i = 0; i < 9; i++) {
            assertEquals(0, discount.get(i).getPercent());
        }
        assertEquals(50, discount.get(9).getPercent());
    }

    @Test
    public void shouldReturn_0_OnGetDiscountCallForUnknownUser() {
        User user = new User();

        List<Discount> discount1 = discountStrategy.getDiscount(null, event, dateTime, 9);
        List<Discount> discount2 = discountStrategy.getDiscount(user, event, dateTime, 4);
        user.setId(5L);
        List<Discount> discount3 = discountStrategy.getDiscount(user, event, dateTime, 9);

        for (Discount discount : discount1) {
            assertEquals(0, discount.getPercent());
        }
        for (Discount discount : discount2) {
            assertEquals(0, discount.getPercent());
        }
        for (Discount discount : discount3) {
            assertEquals(0, discount.getPercent());
        }
    }

    @Test
    public void shouldReturn_50_ForTenthTicket_OnGetDiscountCallForUnknownUser1() {
        List<Discount> discount = discountStrategy.getDiscount(null, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 10);

        for (int i = 0; i < 9; i++) {
            assertEquals(0, discount.get(i).getPercent());
        }
        assertEquals(50, discount.get(9).getPercent());
    }

    @Test
    public void shouldReturn_50_ForTenthAndTwentiethTicket_OnGetDiscountCallForUnknownUser2() {
        User user = new User();

        List<Discount> discount = discountStrategy.getDiscount(user, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 25);
        user.setId(5L);

        for (int i = 0; i < discount.size(); i++) {
            if (i != 9 && i != 19) {
                assertEquals(0, discount.get(i).getPercent());
            } else {
                assertEquals(50, discount.get(i).getPercent());
            }
        }
    }

    @Test
    public void shouldReturn_50_ForTenthTicket_OnGetDiscountCallForUnknownUser3() {
        User user = new User();
        user.setId(5L);

        List<Discount> discount = discountStrategy.getDiscount(user, event,
                LocalDateTime.of(4018, 4, 4, 10, 20), 10);

        for (int i = 0; i < 9; i++) {
            assertEquals(0, discount.get(i).getPercent());
        }
        assertEquals(50, discount.get(9).getPercent());
    }
}
