package ua.epam.spring.hometask.service;

import ua.epam.spring.hometask.domain.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Oleksii_Kovetskyi on 4/4/2018.
 */
public class BookingServiceImpl implements BookingService {

    private DiscountService discountService;
    private UserService userService;
    private Map<EventRating, Double> eventRatingPriceRate;
    private double vipSeatPriceRate;

    private Map<Event, Set<Ticket>> bookedTickets;

    public BookingServiceImpl(DiscountService discountService,
                              UserService userService,
                              Map<EventRating, Double> eventRatingPriceRate,
                              double vipSeatPriceRate) {
        this.discountService = discountService;
        this.userService = userService;
        this.eventRatingPriceRate = new EnumMap<>(eventRatingPriceRate);
        this.vipSeatPriceRate = vipSeatPriceRate;

        bookedTickets = new HashMap<>();
    }

    @Override
    public double getTicketsPrice(@Nonnull Event event, @Nonnull LocalDateTime dateTime,
                                  @Nullable User user, @Nonnull Set<Long> seats) {
        if (!event.getAirDates().contains(dateTime))
            throw new IllegalArgumentException("This event will not happen on the specified date");

        double normalSeatPrice = event.getBasePrice() * eventRatingPriceRate.get(event.getRating());
        double vipSeatPrice = normalSeatPrice * vipSeatPriceRate;

        Auditorium auditorium = event.getAuditoriums().get(dateTime);
        Set<Long> vipSeats = auditorium.getVipSeats();

        double totalPrice = 0;
        byte discount = discountService.getDiscount(user, event, dateTime, seats.size());

        for (Long seat : seats) {
            totalPrice += vipSeats.contains(seat) ? vipSeatPrice : normalSeatPrice;
        }

        totalPrice *= ((100 - discount) / 100.0);

        return totalPrice;
    }

    @Override
    public void bookTickets(@Nonnull Set<Ticket> tickets) {
        for (Ticket ticket : tickets) {
            if (!getBookedTicketsForEvent(ticket.getEvent()).contains(ticket)) {//if ticket is not booked yet
                User user = ticket.getUser();
                if (user != null) {
                    Long userId = user.getId();
                    if (userId != null && userService.getById(userId) != null) {//if user is registered
                        user.getTickets().add(ticket);
                        userService.save(user);
                    }
                }
                bookTicket(ticket);
            }
        }
    }

    private Set<Ticket> getBookedTicketsForEvent(Event event) {
        Set<Ticket> tickets = bookedTickets.get(event);
        return tickets == null ? Collections.emptySet() : tickets;
    }

    private void bookTicket(Ticket ticket) {
        Set<Ticket> ticketSet = bookedTickets.computeIfAbsent(ticket.getEvent(), k -> new HashSet<>());
        ticketSet.add(ticket);
    }

    @Nonnull
    @Override
    public Set<Ticket> getPurchasedTicketsForEvent(@Nonnull Event event, @Nonnull LocalDateTime dateTime) {
        if (!event.getAirDates().contains(dateTime))
            throw new IllegalArgumentException("This event will not happen on the specified date");

        return getBookedTicketsForEvent(event).stream()
                .filter(t -> t.getDateTime().equals(dateTime))
                .collect(Collectors.toSet());
    }
}
