package ua.epam.spring.hometask;

import ua.epam.spring.hometask.domain.*;
import ua.epam.spring.hometask.service.impl.discount.strategy.DiscountStrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public static User createUser(Long id, String firstName, String lastName, String mail,
                                  String login, String password, LocalDate birthDate,
                                  NavigableSet<Ticket> tickets) {
        User user = new User();
        user.setId(id);
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

    public static List<TreeSet<EventDate>> loadAirDates(Auditorium auditorium, String file) throws IOException {
        InputStream inputStream = TestDataCreator.class.getClassLoader().getResourceAsStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List<TreeSet<EventDate>> airDates = new ArrayList<>();
        String line;
        TreeSet<EventDate> set = new TreeSet<>();
        reader.readLine();//skip first line
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                airDates.add(set);
                set = new TreeSet<>();
            } else {
                String[] split = line.split(",");
                int year = Integer.parseInt(split[0]);
                int month = Integer.parseInt(split[1]);
                int dayOfMonth = Integer.parseInt(split[2]);
                int hour = Integer.parseInt(split[3]);
                int minute = Integer.parseInt(split[4]);
                LocalDateTime dateTime = LocalDateTime.of(year, month, dayOfMonth, hour, minute);
                set.add(new EventDate(dateTime, auditorium));
            }
        }
        airDates.add(set);
        return airDates;
    }
}
