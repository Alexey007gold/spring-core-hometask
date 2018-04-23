package ua.epam.spring.hometask.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.epam.spring.hometask.configuration.AppConfiguration;
import ua.epam.spring.hometask.dao.AuditoriumDAOImpl;
import ua.epam.spring.hometask.dao.EventDAOImpl;
import ua.epam.spring.hometask.dao.EventDateDAOImpl;
import ua.epam.spring.hometask.dao.interf.AuditoriumDAO;
import ua.epam.spring.hometask.dao.interf.EventDAO;
import ua.epam.spring.hometask.dao.interf.EventDateDAO;
import ua.epam.spring.hometask.domain.Event;

import java.io.IOException;

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

        Event event1 = new Event();
        event1.setName("Titanik");
        event1.setBasePrice(40);
        event1.setRating(HIGH);
        eventDAO.save(event1);

        Event event2 = new Event();
        event2.setName("Santa Barbara");
        event2.setBasePrice(5);
        event2.setRating(LOW);
        eventDAO.save(event2);

        Event event3 = new Event();
        event3.setName("Star Wars");
        event3.setBasePrice(45);
        event3.setRating(HIGH);
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
