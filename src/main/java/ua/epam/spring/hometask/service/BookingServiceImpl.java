package ua.epam.spring.hometask.service;

import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.domain.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ua.epam.spring.hometask.domain.EventRating.*;

/**
 * Created by Oleksii_Kovetskyi on 4/4/2018.
 */
@Service
public class BookingServiceImpl implements BookingService {

    private DiscountService discountService;
    private UserService userService;
    private TicketService ticketService;
    private Map<EventRating, Double> eventRatingPriceRate;
    private double vipSeatPriceRate;

    public BookingServiceImpl(DiscountService discountService, UserService userService,
                              TicketService ticketService) throws IOException {
        this.discountService = discountService;
        this.userService = userService;
        this.ticketService = ticketService;

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
                                        @Nullable User user, @Nonnull Set<Long> seats) {
        List<Ticket> ticketList = new ArrayList<>(seats.size());
        for (Long seat : seats) {
            Ticket ticket = new Ticket(user, event, dateTime, seat, event.getBasePrice());
            ticketList.add(ticket);
        }
        return ticketList;
    }

    @Override
    public double getTicketsPriceWithDiscount(@Nonnull Event event, @Nonnull LocalDateTime dateTime,
                                              @Nullable User user, @Nonnull List<Ticket> tickets) {
        if (!event.getAirDates().containsKey(dateTime))
            throw new IllegalArgumentException("This event will not happen on the specified date");

        double normalSeatPrice = event.getBasePrice() * eventRatingPriceRate.get(event.getRating());
        double vipSeatPrice = normalSeatPrice * vipSeatPriceRate;

        Auditorium auditorium = event.getAirDates().get(dateTime).getAuditorium();
        Set<Long> vipSeats = auditorium.getVipSeats();

        double totalPrice = 0;
        List<Discount> discount = discountService.getDiscount(user, event, dateTime, tickets.size());

        double ticketPrice;
        for (int i = 0; i < tickets.size(); i++) {
            ticketPrice = vipSeats.contains(tickets.get(i).getSeat()) ? vipSeatPrice : normalSeatPrice;
            ticketPrice *= ((100 - discount.get(i).getPercent()) / 100.0);
            totalPrice += ticketPrice;
        }

        return totalPrice;
    }

    @Override
    public void bookTickets(@Nonnull List<Ticket> tickets, @Nonnull List<Discount> discounts) {
        for (int i = 0; i < tickets.size(); i++) {
            bookTicket(tickets.get(i), discounts.get(i));
        }
    }

    @Override
    public void bookTicket(@Nonnull Ticket ticket, @Nonnull Discount discount) {
        if (!getPurchasedTicketsForEvent(ticket.getEvent()).contains(ticket)) {//if ticket is not booked yet
            User user = ticket.getUser();
            if (user != null) {
                Long userId = user.getId();
                if (userId != null && userService.getById(userId) != null) {//if user is registered
                    user.getTickets().add(ticket);
                    userService.save(user);
                }
            }
        }
    }

    @Nonnull
    @Override
    public Set<Ticket> getPurchasedTicketsForEvent(@Nonnull Event event, @Nonnull LocalDateTime dateTime) {
        if (!event.getAirDates().containsKey(dateTime))
            throw new IllegalArgumentException("This event will not happen on the specified date");

        return getPurchasedTicketsForEvent(event).stream()
                .filter(t -> t.getDateTime().equals(dateTime))
                .collect(Collectors.toSet());
    }

    @Nonnull
    @Override
    public Set<Ticket> getPurchasedTicketsForEvent(@Nonnull Event event) {
        return new HashSet<>(ticketService.getByEvent(event));
    }
}
