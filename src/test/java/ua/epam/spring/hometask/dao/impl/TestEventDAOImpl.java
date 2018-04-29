package ua.epam.spring.hometask.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.epam.spring.hometask.TestDataCreator;
import ua.epam.spring.hometask.configuration.AppConfiguration;
import ua.epam.spring.hometask.dao.AuditoriumDAOImpl;
import ua.epam.spring.hometask.dao.EventDAOImpl;
import ua.epam.spring.hometask.dao.EventDateDAOImpl;
import ua.epam.spring.hometask.dao.interf.AuditoriumDAO;
import ua.epam.spring.hometask.dao.interf.EventDAO;
import ua.epam.spring.hometask.dao.interf.EventDateDAO;
import ua.epam.spring.hometask.domain.Event;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static ua.epam.spring.hometask.domain.EventRating.HIGH;
import static ua.epam.spring.hometask.domain.EventRating.LOW;

/**
 * Created by Oleksii_Kovetskyi on 4/5/4018.
 */
public class TestEventDAOImpl {

    private EventDAO eventDAO;

    @Before
    public void init() throws IOException {
        JdbcTemplate jdbcTemplate = AppConfiguration.jdbcTemplate(new AppConfiguration().getDataSource());
        AuditoriumDAO auditoriumDAO = mock(AuditoriumDAOImpl.class);
        EventDateDAO eventDateDAO = mock(EventDateDAOImpl.class);
        eventDAO = new EventDAOImpl(jdbcTemplate, auditoriumDAO, eventDateDAO);

        Event event1 = TestDataCreator.createEvent(null, "Titanik", HIGH, 40, Collections.emptySet());
        Event event2 = TestDataCreator.createEvent(null, "Santa Barbara", LOW, 5, Collections.emptySet());
        Event event3 = TestDataCreator.createEvent(null, "Star Wars", HIGH, 45, Collections.emptySet());
        eventDAO.save(event1);
        eventDAO.save(event2);
        eventDAO.save(event3);
    }

    @After
    public void clear() {
        for (Event event : eventDAO.getAll()) {
            eventDAO.remove(event);
        }
    }

    @Test
    public void shouldReturnCorrectEventOnGetByNameCall() {
        Event res1 = eventDAO.getByName("Titanik");
        assertEquals("Titanik", res1.getName());
        assertEquals(40, res1.getBasePrice(), 0);
        assertEquals(HIGH, res1.getRating());

        Event res2 = eventDAO.getByName("Santa Barbara");
        assertEquals("Santa Barbara", res2.getName());
        assertEquals(5, res2.getBasePrice(), 0);
        assertEquals(LOW, res2.getRating());
    }
}
