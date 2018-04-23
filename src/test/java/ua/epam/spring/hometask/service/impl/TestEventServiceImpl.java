package ua.epam.spring.hometask.service.impl;

import org.junit.Before;
import org.junit.Test;
import ua.epam.spring.hometask.dao.EventDAOImpl;
import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.EventDate;
import ua.epam.spring.hometask.service.interf.EventService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static java.util.stream.Collectors.toMap;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ua.epam.spring.hometask.domain.EventRating.HIGH;
import static ua.epam.spring.hometask.domain.EventRating.LOW;

/**
 * Created by Oleksii_Kovetskyi on 4/5/4018.
 */
public class TestEventServiceImpl {

    private EventService eventService;

    @Before
    public void init() {
        EventDAOImpl eventDAO = mock(EventDAOImpl.class);
        eventService = new EventServiceImpl(eventDAO);

        Auditorium auditorium = new Auditorium();
        auditorium.setName("1");
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

        Event event1 = new Event();
        event1.setName("Titanik");
        event1.setBasePrice(40);
        event1.setRating(HIGH);
        event1.setAirDates(airDates1);

        Event event2 = new Event();
        event2.setName("Santa Barbara");
        event2.setBasePrice(5);
        event2.setRating(LOW);
        event2.setAirDates(airDates2);

        Event event3 = new Event();
        event3.setName("Star Wars");
        event3.setBasePrice(45);
        event3.setRating(HIGH);
        event3.setAirDates(airDates3);

        when(eventDAO.getAll()).thenReturn(Arrays.asList(event1, event2, event3));
    }

    @Test
    public void shouldReturnCorrectEventsOnGetForDateRangeCall() {
        Set<Event> res1 = eventService.getForDateRange(LocalDate.of(4018, 4, 3),
                LocalDate.of(4018, 4, 5));
        Map<String, Event> res1Map = res1.stream().collect(toMap(Event::getName, e -> e));

        Set<Event> res2 = eventService.getForDateRange(LocalDate.of(4018, 4, 8),
                LocalDate.of(4018, 4, 9));
        Map<String, Event> res2Map = res2.stream().collect(toMap(Event::getName, e -> e));

        Set<Event> res3 = eventService.getForDateRange(LocalDate.of(4018, 4, 15),
                LocalDate.of(4018, 4, 25));


        assertEquals(2, res1.size());
        assertEquals(40, res1Map.get("Titanik").getBasePrice(), 0);
        assertEquals(5, res1Map.get("Santa Barbara").getBasePrice(), 0);

        assertEquals(1, res2.size());
        assertEquals(45, res2Map.get("Star Wars").getBasePrice(), 0);

        assertEquals(0, res3.size());
    }

    @Test
    public void shouldReturnCorrectEventsOnGetNextEventsCall() {
        Set<Event> res1 = eventService.getNextEvents(LocalDateTime.of(4018, 4, 3, 9, 0));
        Map<String, Event> res1Map = res1.stream().collect(toMap(Event::getName, e -> e));

        Set<Event> res2 = eventService.getNextEvents(LocalDateTime.of(4018, 4, 9, 9, 0));
        Map<String, Event> res2Map = res2.stream().collect(toMap(Event::getName, e -> e));

        Set<Event> res3 = eventService.getNextEvents(LocalDateTime.of(4018, 4, 1, 10, 30));


        assertEquals(2, res1.size());
        assertEquals(40, res1Map.get("Titanik").getBasePrice(), 0);
        assertEquals(5, res1Map.get("Santa Barbara").getBasePrice(), 0);

        assertEquals(3, res2.size());
        assertEquals(40, res1Map.get("Titanik").getBasePrice(), 0);
        assertEquals(5, res1Map.get("Santa Barbara").getBasePrice(), 0);
        assertEquals(45, res2Map.get("Star Wars").getBasePrice(), 0);

        assertEquals(0, res3.size());
    }
}
