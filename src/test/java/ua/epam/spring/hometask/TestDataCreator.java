package ua.epam.spring.hometask;

import ua.epam.spring.hometask.domain.*;
import ua.epam.spring.hometask.service.impl.discount.strategy.DiscountStrategy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Oleksii_Kovetskyi on 4/25/2018.
 */
public class TestDataCreator {

    public static Event createEvent(Long id, String name, EventRating rating, double price, Set<EventDate> airDates) {
        Event event = new Event();
        event.setId(id);
        event.setName(name);
        event.setRating(rating);
        event.setBasePrice(price);
        event.setAirDates(airDates);
        return event;
    }

    public static User createUser(String firstName, String lastName, String mail,
                                  String login, String password, LocalDate birthDate,
                                  NavigableSet<Ticket> tickets) {
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(mail);
        user.setBirthDate(birthDate);
        user.setLogin(login);
        user.setPassword(password);
        user.setTickets(tickets);
        return user;
    }

    public static Auditorium createAuditorium(String name, int numOfSeats, Set<Integer> vipSeats) {
        Auditorium auditorium = new Auditorium();
        auditorium.setName(name);
        auditorium.setNumberOfSeats(numOfSeats);
        auditorium.setVipSeats(vipSeats);
        return auditorium;
    }

    public static Auditorium createAuditorium(String name, int numOfSeats, List<Integer> vipSeats) {
        return createAuditorium(name, numOfSeats, new HashSet<>(vipSeats));
    }

    public static List<DiscountStrategy> createMockDiscountStrategyList(int... discounts) {
        List<DiscountStrategy> strategies = new ArrayList<>();
        char name = 'a';
        for (int discount : discounts) {
            char finalName = name;
            strategies.add((user, event, airDateTime, numberOfTickets) ->
                    Collections.singletonList(new Discount(String.valueOf(finalName), (byte) discount)));
            name++;
        }
        return strategies;
    }

    public static List<TreeSet<EventDate>> createAirDates(Auditorium auditorium) {
        TreeSet<EventDate> airDates1 = new TreeSet<EventDate>(){{
            add(new EventDate(LocalDateTime.of(4018, 4, 2, 10, 30), auditorium));
            add(new EventDate(LocalDateTime.of(4018, 4, 2, 18, 0), auditorium));
            add(new EventDate(LocalDateTime.of(4018, 4, 3, 10, 30), auditorium));
            add(new EventDate(LocalDateTime.of(4018, 4, 4, 10, 20), auditorium));
            add(new EventDate(LocalDateTime.of(4018, 4, 5, 10, 30), auditorium));
        }};
        TreeSet<EventDate> airDates2 = new TreeSet<EventDate>() {{
            add(new EventDate(LocalDateTime.of(4018, 4, 3, 10, 30), auditorium));
            add(new EventDate(LocalDateTime.of(4018, 4, 2, 18, 0), auditorium));
            add(new EventDate(LocalDateTime.of(4018, 4, 2, 10, 30), auditorium));
            add(new EventDate(LocalDateTime.of(4018, 4, 4, 10, 20), auditorium));
            add(new EventDate(LocalDateTime.of(4018, 4, 5, 10, 30), auditorium));
        }};
        TreeSet<EventDate> airDates3 = new TreeSet<EventDate>() {{
            add(new EventDate(LocalDateTime.of(4018, 4, 8, 10, 30), auditorium));
            add(new EventDate(LocalDateTime.of(4018, 4, 9, 10, 20), auditorium));
            add(new EventDate(LocalDateTime.of(4018, 4, 10, 10, 30), auditorium));
            add(new EventDate(LocalDateTime.of(4018, 4, 11, 10, 30), auditorium));
            add(new EventDate(LocalDateTime.of(4018, 4, 12, 18, 0), auditorium));
        }};
        return Arrays.asList(airDates1, airDates2, airDates3);
    }
}
