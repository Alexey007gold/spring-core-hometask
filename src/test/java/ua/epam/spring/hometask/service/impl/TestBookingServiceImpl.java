package ua.epam.spring.hometask.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.epam.spring.hometask.domain.*;
import ua.epam.spring.hometask.exception.NotEnoughMoneyException;
import ua.epam.spring.hometask.exception.SeatIsAlreadyBookedException;
import ua.epam.spring.hometask.service.interf.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static ua.epam.spring.hometask.domain.EventRating.HIGH;

/**
 * Created by Oleksii_Kovetskyi on 4/5/2018.
 */
public class TestBookingServiceImpl {

    private BookingService bookingService;
    private DiscountService discountService;
    private UserService userService;
    private UserAccountService userAccountService;
    private TicketService ticketService;

    private Event event;
    private User user;
    private List<Ticket> tickets;
    private LocalDateTime dateTime;

    @Before
    public void init() throws IOException {
        discountService = mock(DiscountServiceImpl.class);
        userService = mock(UserServiceImpl.class);
        userAccountService = mock(UserAccountServiceImpl.class);
        ticketService = mock(TicketServiceImpl.class);
        bookingService = new BookingServiceImpl(discountService, userService, userAccountService, ticketService);

        dateTime = LocalDateTime.of(4018, 4, 3, 10, 30);

        Auditorium auditorium = new Auditorium();
        auditorium.setName("1");
        auditorium.setNumberOfSeats(40);
        auditorium.setVipSeats(new HashSet<>(Arrays.asList(4, 5, 34, 35)));
        TreeSet<EventDate> airDates = new TreeSet<>(Arrays.asList(
                new EventDate(LocalDateTime.of(4018, 4, 2, 10, 30), auditorium),
                new EventDate(LocalDateTime.of(4018, 4, 2, 18, 0), auditorium),
                new EventDate(dateTime, auditorium),
                new EventDate(LocalDateTime.of(4018, 4, 4, 10, 20), auditorium),
                new EventDate(LocalDateTime.of(4018, 4, 5, 10, 30), auditorium)
        ));

        event = new Event();
        event.setId(1L);
        event.setName("Titanik");
        event.setBasePrice(40);
        event.setRating(HIGH);
        event.setAirDates(airDates);

        user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("mail");
        user.setBirthDate(LocalDate.of(1990, 11, 11));
        user.setTickets(new TreeSet<>());

        tickets = new ArrayList<>();
        tickets.add(new Ticket(user, event, dateTime, 1, 40));
        tickets.add(new Ticket(user, event, dateTime, 2, 40));
    }

    @Test
    public void shouldReturnSetOfTicketsOnGenerateTicketsCall() {
        List<Ticket> tickets = bookingService.generateTickets(event, dateTime, user, new HashSet<>(Arrays.asList(1, 2, 34)));
        assertEquals(3, tickets.size());
        assertEquals(1, tickets.get(0).getSeat());
        assertEquals(2, tickets.get(1).getSeat());
        assertEquals(34, tickets.get(2).getSeat());

        assertEquals(48, tickets.get(0).getPrice(), 0.0000001);//high rate
        assertEquals(48, tickets.get(1).getPrice(), 0.0000001);
        assertEquals(96, tickets.get(2).getPrice(), 0.0000001);//VIP seat
    }

    @Test
    public void shouldReturn_115_2_OnGetTicketPriceCall() {
        Discount discount = new Discount("a", (byte) 20);
        List<Discount> discountList = Arrays.asList(discount, discount, discount);
        when(discountService.getDiscount(user, event, dateTime, 3)).thenReturn(discountList);

        List<Ticket> tickets = bookingService.generateTickets(event, dateTime, user, new HashSet<>(Arrays.asList(1, 2, 3)));
        double price = bookingService.getTicketsPriceWithDiscount(event, dateTime, user, tickets);
        assertEquals(115.2, price, 0.0000001);
    }

    @Test
    public void shouldReturn_153_6_OnGetTicketPriceCall() {
        Discount discount = new Discount("a", (byte) 20);
        List<Discount> discountList = Arrays.asList(discount, discount, discount);
        when(discountService.getDiscount(user, event, dateTime, 3)).thenReturn(discountList);

        List<Ticket> tickets = bookingService.generateTickets(event, dateTime, user, new HashSet<>(Arrays.asList(1, 2, 34)));
        double price = bookingService.getTicketsPriceWithDiscount(event, dateTime, user, tickets);
        assertEquals(153.6, price, 0.0000001);
    }

    @Test
    public void shouldApplyDiscountAndAddTicketsToUserAndWithdrawMoneyOnBookTicketsCall() {
        when(userService.isUserRegistered(user)).thenReturn(true);
        when(userAccountService.getByUserId(user.getId())).thenReturn(new UserAccount(user.getId(), 200));
        Discount discount = new Discount("a", (byte) 20);
        List<Discount> discountList = Arrays.asList(discount, discount);

        bookingService.bookTickets(tickets, discountList);

        verify(userService, times(1)).save(user);
        verify(ticketService, times(0)).save(any());
        ArgumentCaptor<UserAccount> captor = ArgumentCaptor.forClass(UserAccount.class);
        verify(userAccountService, times(1)).save(captor.capture());
        assertEquals(200 - 64, captor.getValue().getBalance(), 0.0000001);
        assertEquals(2, user.getTickets().size());
        for (Ticket ticket : tickets) {
            assertTrue(user.getTickets().contains(ticket));
            assertEquals(32, ticket.getPrice(), 0.0000001);
        }
    }

    @Test(expected = SeatIsAlreadyBookedException.class)
    public void shouldThrowAnExceptionOnBookTicketsCallWhenATicketIsBooked() {
        when(userService.isUserRegistered(user)).thenReturn(true);
        when(userAccountService.getByUserId(user.getId())).thenReturn(new UserAccount(user.getId(), 200));
        when(ticketService.getBookedSeatsForEventAndDate(event.getId(), dateTime))
                .thenReturn(Collections.singletonList(2));
        Discount discount = new Discount("a", (byte) 20);
        List<Discount> discountList = Arrays.asList(discount, discount);

        bookingService.bookTickets(tickets, discountList);
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void shouldThrowAnExceptionOnBookTicketsCallWhenNotEnoughMoney() {
        when(userService.isUserRegistered(user)).thenReturn(true);
        when(userAccountService.getByUserId(user.getId())).thenReturn(new UserAccount(user.getId(), 63.99999));
        when(ticketService.getByEventAndTime(event, dateTime)).thenReturn(Collections.emptyList());
        Discount discount = new Discount("a", (byte) 20);
        List<Discount> discountList = Arrays.asList(discount, discount);

        bookingService.bookTickets(tickets, discountList);
    }

    @Test
    public void shouldApplyDiscountOnBookTicketsCallWithNullUser() {
        when(userService.isUserRegistered(any())).thenReturn(false);
        Discount discount = new Discount("a", (byte) 40);
        List<Discount> discountList = Arrays.asList(discount, discount);

        for (Ticket ticket : tickets) {
            ticket.setUser(null);
        }
        bookingService.bookTickets(tickets, discountList);

        verify(userService, times(0)).save(any());
        for (Ticket ticket : tickets) {
            verify(ticketService, times(1)).save(ticket);
        }
        for (Ticket ticket : tickets) {
            assertEquals(24, ticket.getPrice(), 0.0000001);
        }
    }

    @Test
    public void shouldReturnPurchasedTicketsOnGetPurchasedTicketsForEventCall() {
        Discount discount = new Discount("a", (byte) 20);
        List<Discount> discountList = Arrays.asList(discount, discount);

        bookingService.bookTickets(tickets, discountList);
        verify(ticketService, times(1)).getBookedSeatsForEventAndDate(event.getId(), dateTime);

        when(ticketService.getByEventAndTime(event, dateTime)).thenReturn(tickets);
        Set<Ticket> purchasedTicketsForEvent = bookingService.getPurchasedTicketsForEvent(event, dateTime);
        verify(ticketService, times(1)).getByEventAndTime(event, dateTime);
        assertEquals(2, purchasedTicketsForEvent.size());
    }
}
