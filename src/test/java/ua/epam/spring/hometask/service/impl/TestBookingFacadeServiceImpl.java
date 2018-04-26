package ua.epam.spring.hometask.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ua.epam.spring.hometask.domain.*;
import ua.epam.spring.hometask.exception.SeatIsAlreadyBookedException;
import ua.epam.spring.hometask.service.interf.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static ua.epam.spring.hometask.TestDataCreator.createAuditorium;
import static ua.epam.spring.hometask.TestDataCreator.createEvent;
import static ua.epam.spring.hometask.TestDataCreator.createUser;
import static ua.epam.spring.hometask.domain.EventRating.HIGH;

/**
 * Created by Oleksii_Kovetskyi on 4/26/2018.
 */
public class TestBookingFacadeServiceImpl {

    private BookingFacadeServiceImpl bookingFacadeService;
    private BookingService bookingService;
    private EventService eventService;
    private UserService userService;
    private UserAccountService userAccountService;
    private DiscountService discountService;
    private TicketService ticketService;


    private Event event;
    private User user;
    private LocalDateTime dateTime;
    private List<Ticket> tickets;
    private Set<Integer> seatsToBook;

    @Before
    public void init() throws Exception {
        bookingService = mock(BookingServiceImpl.class);
        eventService = mock(EventServiceImpl.class);
        userService = mock(UserServiceImpl.class);
        userAccountService = mock(UserAccountServiceImpl.class);
        discountService = mock(DiscountServiceImpl.class);
        ticketService = mock(TicketServiceImpl.class);

        bookingFacadeService = new BookingFacadeServiceImpl(bookingService, eventService, userService,
                userAccountService, discountService, ticketService);

        List<Integer> vipSeats = Arrays.asList(1, 2, 9, 10);
        Auditorium auditorium = createAuditorium("1", 10, vipSeats);
        dateTime = LocalDateTime.of(4018, 4, 3, 10, 30);

        TreeSet<EventDate> airDates = new TreeSet<>(Arrays.asList(
                new EventDate(LocalDateTime.of(4018, 4, 2, 10, 30), auditorium),
                new EventDate(LocalDateTime.of(4018, 4, 2, 18, 0), auditorium),
                new EventDate(dateTime, auditorium),
                new EventDate(LocalDateTime.of(4018, 4, 4, 10, 20), auditorium),
                new EventDate(LocalDateTime.of(4018, 4, 5, 10, 30), auditorium)
        ));

        event = createEvent(1L, "Titanik", HIGH, 40, airDates);
        user = createUser(1L, "John", "Doe", "John@mail.ru", "login", "1234", null, new TreeSet<>());
        tickets = Arrays.asList(new Ticket(user, event, dateTime, 6, 40), new Ticket(user, event, dateTime, 7, 40),
                new Ticket(user, event, dateTime, 8, 40));
        seatsToBook = new HashSet<>(Arrays.asList(6, 7, 8));
    }

    @Test
    public void shouldCallServicesMethodsAndReturnTheResultOnGetTicketsPriceCall() throws Exception {
        when(eventService.getById(event.getId())).thenReturn(event);
        when(userService.getById(user.getId())).thenReturn(user);
        when(bookingService.generateTickets(event, dateTime, user, seatsToBook)).thenReturn(tickets);
        when(bookingService.getTicketsPriceWithDiscount(event, dateTime, user, tickets)).thenReturn(400.0);


        double ticketsPrice = bookingFacadeService.getTicketsPrice(event.getId(), dateTime, user.getId(), seatsToBook);
        verify(eventService, times(1)).getById(event.getId());
        verify(userService, times(1)).getById(user.getId());
        verify(bookingService, times(1)).generateTickets(event, dateTime, user, seatsToBook);
        verify(bookingService, times(1)).getTicketsPriceWithDiscount(event, dateTime, user, tickets);
        assertEquals(400.0, ticketsPrice, 0.0000001);
    }

    @Test(expected = SeatIsAlreadyBookedException.class)
    public void shouldCallServicesMethodsAndThrowAnExceptionOnGetTicketsPriceCallWhenASeatIsAlreadyBooked() throws Exception {
        when(eventService.getById(event.getId())).thenReturn(event);
        when(userService.getById(user.getId())).thenReturn(user);
        when(bookingService.generateTickets(event, dateTime, user, seatsToBook)).thenThrow(SeatIsAlreadyBookedException.class);
        when(bookingService.getTicketsPriceWithDiscount(event, dateTime, user, tickets)).thenReturn(400.0);

        try {
            bookingFacadeService.getTicketsPrice(event.getId(), dateTime, user.getId(), seatsToBook);
        } catch (SeatIsAlreadyBookedException e) {
            verify(eventService, times(1)).getById(event.getId());
            verify(userService, times(1)).getById(user.getId());
            verify(bookingService, times(1)).generateTickets(event, dateTime, user, seatsToBook);
            verify(bookingService, times(0)).getTicketsPriceWithDiscount(event, dateTime, user, tickets);
            throw e;
        }
    }

    @Test
    public void shouldReturnCorrectSetOfAvailableSeatsOnGetAvailableSeatsCall() throws Exception {
        when(eventService.getById(event.getId())).thenReturn(event);
        when(ticketService.getByEventAndTime(event, dateTime)).thenReturn(tickets);

        Set<Integer> expectedResult = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 9, 10));

        Set<Integer> result = bookingFacadeService.getAvailableSeats(event.getId(), dateTime);
        assertEquals(expectedResult, result);
        verify(eventService, times(1)).getById(event.getId());
        verify(ticketService, times(1)).getByEventAndTime(event, dateTime);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionOnGetAvailableSeatsCallWhenEventDoesNotExist() throws Exception {
        when(eventService.getById(event.getId())).thenReturn(null);
        when(ticketService.getByEventAndTime(event, dateTime)).thenReturn(tickets);

        try {
            bookingFacadeService.getAvailableSeats(event.getId(), dateTime);
        } catch (IllegalArgumentException e) {
            verify(eventService, times(1)).getById(event.getId());
            verify(ticketService, times(0)).getByEventAndTime(event, dateTime);
            assertEquals("No such event!", e.getMessage());
            throw e;
        }
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowAnExceptionOnGetAvailableSeatsCallWhenEventDoesNotHappenAtGivenDateTime() throws Exception {
        when(eventService.getById(event.getId())).thenReturn(event);
        when(ticketService.getByEventAndTime(event, dateTime)).thenReturn(null);

        try {
            bookingFacadeService.getAvailableSeats(event.getId(), dateTime);
        } catch (NullPointerException e) {
            verify(eventService, times(1)).getById(event.getId());
            verify(ticketService, times(1)).getByEventAndTime(event, dateTime);
            throw e;
        }
    }

    @Test
    public void shouldCallServicesAndReturnSetOfBookedSeatsOnBookTicketsCall() throws Exception {
        List<Discount> discountList = Arrays.asList(new Discount("a", (byte) 0), new Discount("a", (byte) 20),
                new Discount("a", (byte) 0));

        when(eventService.getById(event.getId())).thenReturn(event);
        when(userService.getById(user.getId())).thenReturn(user);
        when(bookingService.generateTickets(event, dateTime, user, seatsToBook)).thenReturn(tickets);
        when(discountService.getDiscount(user, event, dateTime, 3)).thenReturn(discountList);
        when(bookingService.bookTickets(tickets, discountList)).thenReturn(seatsToBook);

        Set<Integer> result = bookingFacadeService.bookTickets(event.getId(), dateTime, user.getId(), seatsToBook);
        verify(eventService, times(1)).getById(event.getId());
        verify(userService, times(1)).getById(user.getId());
        verify(bookingService, times(1)).generateTickets(event, dateTime, user, seatsToBook);
        verify(discountService, times(1)).getDiscount(user, event, dateTime, 3);
        verify(bookingService, times(1)).bookTickets(tickets, discountList);
        assertEquals(seatsToBook, result);
    }

    @Test(expected = SeatIsAlreadyBookedException.class)
    public void shouldCallServicesAndThrowAnExceptionOnBookTicketsCallWhenASeatIsAlreadyBooked() throws Exception {
        List<Discount> discountList = Arrays.asList(new Discount("a", (byte) 0), new Discount("a", (byte) 20),
                new Discount("a", (byte) 0));

        when(eventService.getById(event.getId())).thenReturn(event);
        when(userService.getById(user.getId())).thenReturn(user);
        when(bookingService.generateTickets(event, dateTime, user, seatsToBook)).thenThrow(SeatIsAlreadyBookedException.class);
        when(discountService.getDiscount(user, event, dateTime, 3)).thenReturn(discountList);
        when(bookingService.bookTickets(tickets, discountList)).thenReturn(seatsToBook);

        try {
            bookingFacadeService.bookTickets(event.getId(), dateTime, user.getId(), seatsToBook);
        } catch (SeatIsAlreadyBookedException e) {
            verify(eventService, times(1)).getById(event.getId());
            verify(userService, times(1)).getById(user.getId());
            verify(bookingService, times(1)).generateTickets(event, dateTime, user, seatsToBook);
            verify(discountService, times(0)).getDiscount(user, event, dateTime, 3);
            verify(bookingService, times(0)).bookTickets(tickets, discountList);

            throw e;
        }
    }

    @Test
    public void shouldCallServicesOnRefillAccountCall() throws Exception {
        UserAccount account = new UserAccount(user.getId(), 20);
        when(userAccountService.getByUserId(user.getId())).thenReturn(account);

        bookingFacadeService.refillAccount(user.getId(), 30);
        ArgumentCaptor<UserAccount> captor = ArgumentCaptor.forClass(UserAccount.class);
        verify(userAccountService, times(1)).getByUserId(user.getId());
        verify(userAccountService, times(1)).save(captor.capture());
        assertEquals(50, captor.getValue().getBalance(), 0.00000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionOnRefillAccountCallIfUserDoesNotExist() throws Exception {
        when(userAccountService.getByUserId(user.getId())).thenReturn(null);

        try {
            bookingFacadeService.refillAccount(user.getId(), 30);
        } catch (IllegalArgumentException e) {
            verify(userAccountService, times(1)).getByUserId(user.getId());
            verify(userAccountService, times(0)).save(any());
            assertEquals("User with such id does not exist", e.getMessage());
            throw e;
        }
    }

}