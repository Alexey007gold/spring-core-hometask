package ua.epam.spring.hometask.service.impl;

import org.junit.Before;
import org.junit.Test;
import ua.epam.spring.hometask.TestDataCreator;
import ua.epam.spring.hometask.dao.EventDAOImpl;
import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.EventDate;
import ua.epam.spring.hometask.service.interf.AuditoriumService;
import ua.epam.spring.hometask.service.interf.EventService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toMap;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static ua.epam.spring.hometask.domain.EventRating.HIGH;
import static ua.epam.spring.hometask.domain.EventRating.LOW;

/**
 * Created by Oleksii_Kovetskyi on 4/5/4018.
 */
public class TestEventServiceImpl {

    private EventService eventService;
    private EventDAOImpl eventDAO;
    private AuditoriumService auditoriumService;

    @Before
    public void init() throws IOException {
        eventDAO = mock(EventDAOImpl.class);
        auditoriumService = mock(AuditoriumServiceImpl.class);
        eventService = new EventServiceImpl(eventDAO, auditoriumService);

        Auditorium auditorium = TestDataCreator.createAuditorium("1", 30, Collections.emptySet());

        List<TreeSet<EventDate>> airDates = TestDataCreator.loadAirDates(auditorium, "testAirDates.csv");

        Event event1 = TestDataCreator.createEvent(null, "Titanik", HIGH, 40, airDates.get(0));
        Event event2 = TestDataCreator.createEvent(null, "Santa Barbara", LOW, 5, airDates.get(1));
        Event event3 = TestDataCreator.createEvent(null, "Star Wars", HIGH, 45, airDates.get(2));

        when(eventDAO.getAll()).thenReturn(Arrays.asList(event1, event2, event3));
    }

    @Test
    public void shouldReturnCorrectEventsOnGetForDateRangeCall() {
        Map<String, Event> res1Map = eventService.getForDateRange(LocalDate.of(4018, 4, 3),
                LocalDate.of(4018, 4, 5)).stream().collect(toMap(Event::getName, e -> e));
        Map<String, Event> res2Map = eventService.getForDateRange(LocalDate.of(4018, 4, 8),
                LocalDate.of(4018, 4, 9)).stream().collect(toMap(Event::getName, e -> e));
        Set<Event> res3 = eventService.getForDateRange(LocalDate.of(4018, 4, 15),
                LocalDate.of(4018, 4, 25));


        checkMapContainsEvents(res1Map, Arrays.asList(new Object[] {"Titanik", 40}, new Object[] {"Santa Barbara", 5}));
        checkMapContainsEvents(res2Map, Collections.singletonList(new Object[] {"Star Wars", 45}));
        assertEquals(0, res3.size());
    }

    @Test
    public void shouldReturnCorrectEventsOnGetNextEventsCall() {
        LocalDateTime dateTime1 = LocalDateTime.of(4018, 4, 3, 9, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(4018, 4, 9, 9, 0);
        LocalDateTime dateTime3 = LocalDateTime.of(4018, 4, 1, 10, 30);

        Map<String, Event> res1Map = eventService.getNextEvents(dateTime1).stream()
                .collect(toMap(Event::getName, e -> e));
        Map<String, Event> res2Map  = eventService.getNextEvents(dateTime2).stream()
                .collect(toMap(Event::getName, e -> e));
        Set<Event> res3 = eventService.getNextEvents(dateTime3);


        checkMapContainsEvents(res1Map, Arrays.asList(
                new Object[] {"Titanik", 40},
                new Object[] {"Santa Barbara", 5}));
        checkMapContainsEvents(res2Map, Arrays.asList(
                new Object[] {"Titanik", 40},
                new Object[] {"Santa Barbara", 5},
                new Object[] {"Star Wars", 45}));
        assertEquals(0, res3.size());
    }

    private void checkMapContainsEvents(Map<String, Event> res, List<Object[]> paramsList) {
        assertEquals(paramsList.size(), res.size());
        for (Object[] params : paramsList) {
            assertEquals((int) params[1], res.get(params[0]).getBasePrice(), 0.0000001);
        }
    }

    @Test
    public void shouldSaveEventsToDAO_OnParseEventsFromInputStreamCall() throws IOException {
        String str = "Titanik,40.99,HIGH,434353443,1,4343534344,2\n" +
                "Santa Barbara,20.99,LOW,434353943,1,4343934344,2";
        InputStream inputStream = new ByteArrayInputStream(str.getBytes());
        eventService.parseEventsFromInputStream(inputStream);

        verify(auditoriumService, times(4)).getByName(any());
        verify(auditoriumService, times(2)).getByName("1");
        verify(auditoriumService, times(2)).getByName("2");
        verify(eventDAO, times(2)).save(any());
    }
}
