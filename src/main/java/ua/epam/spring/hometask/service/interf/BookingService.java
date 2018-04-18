package ua.epam.spring.hometask.service.interf;

import ua.epam.spring.hometask.domain.Discount;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.Ticket;
import ua.epam.spring.hometask.domain.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author Yuriy_Tkach
 */
public interface BookingService {

    /**
     * Generating tickets for supplied seats for particular event
     *
     * @param event
     *            Event to get base ticket price, vip seats and other
     *            information
     * @param dateTime
     *            Date and time of event air
     * @param user
     *            User could be needed to calculate discount.
     *            Can be <code>null</code>
     * @param seats
     *            Set of seat numbers
     * @return total price
     */
    public List<Ticket> generateTickets(@Nonnull Event event, @Nonnull LocalDateTime dateTime, @Nullable User user,
                                        @Nonnull Set<Long> seats);

    /**
     * Getting price when buying all supplied seats for particular event
     * 
     * @param event
     *            Event to get base ticket price, vip seats and other
     *            information
     * @param dateTime
     *            Date and time of event air
     * @param user
     *            User that buys ticket could be needed to calculate discount.
     *            Can be <code>null</code>
     * @param tickets
     *            List of tickets that user wants to buy
     * @return total price
     */
    public double getTicketsPriceWithDiscount(@Nonnull Event event, @Nonnull LocalDateTime dateTime, @Nullable User user,
                                              @Nonnull List<Ticket> tickets);

    /**
     * Books tickets in internal system. If user is not
     * <code>null</code> in a ticket then booked tickets are saved with it
     *
     * @param tickets
     *            List of tickets
     * @param discounts
     *            List of discounts
     */
    public Set<Long> bookTickets(@Nonnull List<Ticket> tickets, @Nonnull List<Discount> discounts);

    /**
     * Getting all purchased tickets for event on specific air date and time
     * 
     * @param event
     *            Event to get tickets for
     * @param dateTime
     *            Date and time of airing of event
     * @return set of all purchased tickets
     */
    public @Nonnull Set<Ticket> getPurchasedTicketsForEvent(@Nonnull Event event, @Nonnull LocalDateTime dateTime);

    /**
     * Getting all purchased tickets for event
     *
     * @param event
     *            Event to get tickets for
     * @return set of all purchased tickets
     */
    public @Nonnull Set<Ticket> getPurchasedTicketsForEvent(@Nonnull Event event);

}
