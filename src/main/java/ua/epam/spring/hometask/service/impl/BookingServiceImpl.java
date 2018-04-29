package ua.epam.spring.hometask.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.spring.hometask.domain.*;
import ua.epam.spring.hometask.exception.NotEnoughMoneyException;
import ua.epam.spring.hometask.exception.SeatIsAlreadyBookedException;
import ua.epam.spring.hometask.service.interf.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toSet;
import static ua.epam.spring.hometask.domain.EventRating.*;

/**
 * Created by Oleksii_Kovetskyi on 4/4/2018.
 */
@Service
public class BookingServiceImpl implements BookingService {

    private static final String THIS_EVENT_WILL_NOT_HAPPEN_ON_THE_SPECIFIED_DATE =
            "This event will not happen on the specified date";
    private static final String PLEASE_BOOK_TICKETS_ONLY_FOR_ONE_USER_EVENT_AND_TIME_AT_A_TIME =
            "Please book tickets only for one user, event and time at a time";

    private DiscountService discountService;
    private UserService userService;
    private UserAccountService userAccountService;
    private TicketService ticketService;
    private Map<EventRating, Double> eventRatingPriceRate;
    private double vipSeatPriceRate;

    public BookingServiceImpl(DiscountService discountService, UserService userService,
                              UserAccountService userAccountService, TicketService ticketService) throws IOException {
        this.discountService = discountService;
        this.userService = userService;
        this.userAccountService = userAccountService;
        this.ticketService = ticketService;

        init();
    }

    public void init() throws IOException {
        Properties props = new Properties();
        props.load(this.getClass().getClassLoader().getResourceAsStream("application.properties"));

        Double highRate = Double.valueOf(props.getProperty("movie.rating.high.price_rate"));
        Double midRate = Double.valueOf(props.getProperty("movie.rating.mid.price_rate"));
        Double lowRate = Double.valueOf(props.getProperty("movie.rating.low.price_rate"));
        this.eventRatingPriceRate = new EnumMap<>(EventRating.class);
        this.eventRatingPriceRate.put(HIGH, highRate);
        this.eventRatingPriceRate.put(MID, midRate);
        this.eventRatingPriceRate.put(LOW, lowRate);

        this.vipSeatPriceRate = Double.parseDouble(props.getProperty("vip_seat.price_rate"));
    }

    @Override
    public List<Ticket> generateTickets(@Nonnull Event event, @Nonnull LocalDateTime dateTime,
                                        @Nullable User user, @Nonnull Set<Integer> seats) {
        checkIsAnyTicketBooked(event, dateTime, seats);

        List<Ticket> ticketList = new ArrayList<>(seats.size());

        double normalSeatPrice = event.getBasePrice() * eventRatingPriceRate.get(event.getRating());
        double vipSeatPrice = normalSeatPrice * vipSeatPriceRate;

        Auditorium auditorium = event.getAirDates().get(dateTime).getAuditorium();
        Set<Integer> vipSeats = auditorium.getVipSeats();

        for (Integer seat : seats) {
            double ticketPrice = formatPrice(vipSeats.contains(seat) ? vipSeatPrice : normalSeatPrice);
            Ticket ticket = new Ticket(user, event, dateTime, seat, ticketPrice);
            ticketList.add(ticket);
        }
        return ticketList;
    }

    @Override
    public double getTicketsPriceWithDiscount(@Nonnull Event event, @Nonnull LocalDateTime dateTime,
                                              @Nullable User user, @Nonnull List<Ticket> tickets) {
        if (!event.getAirDates().containsKey(dateTime))
            throw new IllegalArgumentException(THIS_EVENT_WILL_NOT_HAPPEN_ON_THE_SPECIFIED_DATE);

        double totalPrice = 0;
        List<Discount> discount = discountService.getDiscount(user, event, dateTime, tickets.size());

        for (int i = 0; i < tickets.size(); i++) {
            double ticketPrice = tickets.get(i).getPrice() * ((100 - discount.get(i).getPercent()) / 100.0);
            totalPrice += ticketPrice;
        }

        return formatPrice(totalPrice);
    }

    @Override
    @Transactional
    public Set<Integer> bookTickets(@Nonnull List<Ticket> tickets, @Nonnull List<Discount> discounts) {
        User user = tickets.get(0).getUser();
        Event event = tickets.get(0).getEvent();
        LocalDateTime dateTime = tickets.get(0).getDateTime();
        for (Ticket ticket : tickets) {
            if (!Objects.equals(ticket.getUser(), user) ||
                    !Objects.equals(ticket.getEvent(), event) ||
                    !Objects.equals(ticket.getDateTime(), dateTime)) {
                throw new IllegalStateException(PLEASE_BOOK_TICKETS_ONLY_FOR_ONE_USER_EVENT_AND_TIME_AT_A_TIME);
            }
        }

        checkIsAnyTicketBooked(event, dateTime, tickets.stream().mapToInt(Ticket::getSeat).boxed().collect(toSet()));

        if (!userService.isUserRegistered(user)) {
            user = null;
        }

        for (int i = 0; i < tickets.size(); i++) {
            applyDiscount(tickets.get(i), discounts.get(i));
        }
        if (user != null) {
            UserAccount userAccount = userAccountService.getByUserId(user.getId());
            double balance = userAccount.getBalance();
            double totalPrice = formatPrice(tickets.stream().mapToDouble(Ticket::getPrice).sum());
            if (Double.compare(balance, totalPrice) < 0) {
                throw new NotEnoughMoneyException(totalPrice, balance);
            }
            userAccount.setBalance(balance - totalPrice);
            userAccountService.save(userAccount);
            user.getTickets().addAll(tickets);
            userService.save(user);
        } else {//for unregistered user
            tickets.forEach(t -> ticketService.save(t));
        }

        return tickets.stream().mapToInt(Ticket::getSeat).boxed().collect(toSet());
    }

    private void checkIsAnyTicketBooked(Event event, LocalDateTime dateTime, Set<Integer> seats) {
        for (Integer seat : ticketService.getBookedSeatsForEventAndDate(event.getId(), dateTime)) {
            if (seats.contains(seat)) {
                throw new SeatIsAlreadyBookedException(seat);
            }
        }
    }

    private void applyDiscount(Ticket ticket, @Nonnull Discount discount) {
        double price = formatPrice(ticket.getPrice() * ((100 - discount.getPercent()) / 100.0));
        ticket.setPrice(price);
    }

    @Nonnull
    @Override
    public Set<Ticket> getPurchasedTicketsForEvent(@Nonnull Event event, @Nonnull LocalDateTime dateTime) {
        if (!event.getAirDates().containsKey(dateTime))
            throw new IllegalArgumentException(THIS_EVENT_WILL_NOT_HAPPEN_ON_THE_SPECIFIED_DATE);

        return new HashSet<>(ticketService.getByEventAndTime(event, dateTime));
    }

    @Nonnull
    @Override
    public Set<Ticket> getPurchasedTicketsForEvent(@Nonnull Event event) {
        return new HashSet<>(ticketService.getByEvent(event));
    }

    private double formatPrice(double totalPrice) {
        return BigDecimal.valueOf(totalPrice)
                .setScale(2, RoundingMode.FLOOR)
                .doubleValue();
    }
}
