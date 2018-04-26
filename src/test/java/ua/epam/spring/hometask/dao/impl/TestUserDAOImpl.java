package ua.epam.spring.hometask.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.epam.spring.hometask.configuration.AppConfiguration;
import ua.epam.spring.hometask.dao.EventDAOImpl;
import ua.epam.spring.hometask.dao.TicketDAOImpl;
import ua.epam.spring.hometask.dao.UserDAOImpl;
import ua.epam.spring.hometask.dao.interf.EventDAO;
import ua.epam.spring.hometask.dao.interf.TicketDAO;
import ua.epam.spring.hometask.domain.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static ua.epam.spring.hometask.TestDataCreator.createUser;

/**
 * Created by Oleksii_Kovetskyi on 4/5/2018.
 */
public class TestUserDAOImpl {

    private UserDAOImpl userDAO;

    @Before
    public void init() throws IOException {
        JdbcTemplate jdbcTemplate = AppConfiguration.jdbcTemplate(new AppConfiguration().getDataSource());
        EventDAO eventDAO = mock(EventDAOImpl.class);
        TicketDAO ticketDAO = mock(TicketDAOImpl.class);
        userDAO = new UserDAOImpl(jdbcTemplate, eventDAO);
        userDAO.setTicketDAO(ticketDAO);

        User user1 = createUser(null, "John", "Doe", "example@gmail.com", "user1", "1234", LocalDate.of(1990, 11, 11), new TreeSet<>());
        User user2 = createUser(null, "Jane", "Doe", "exampleJane@gmail.com", "user2", "1234", LocalDate.of(1993, 1, 22), new TreeSet<>());
        userDAO.save(user1);
        userDAO.save(user2);
    }

    @After
    public void clear() {
        for (User user : userDAO.getAll()) {
            userDAO.remove(user);
        }
    }

    @Test
    public void shouldReturnCorrectUserOnGetByEmailCall() {
        User res1 = userDAO.getByEmail("example@gmail.com");
        assertEquals("John", res1.getFirstName());
        assertEquals("Doe", res1.getLastName());
        assertEquals("example@gmail.com", res1.getEmail());
        assertEquals("user1", res1.getLogin());
        assertEquals("1234", res1.getPassword());
        assertEquals(LocalDate.of(1990, 11, 11), res1.getBirthDate());

        User res2 = userDAO.getByEmail("exampleJane@gmail.com");
        assertEquals("Jane", res2.getFirstName());
        assertEquals("Doe", res2.getLastName());
        assertEquals("exampleJane@gmail.com", res2.getEmail());
        assertEquals("user2", res2.getLogin());
        assertEquals("1234", res2.getPassword());
        assertEquals(LocalDate.of(1993, 1, 22), res2.getBirthDate());
    }
}
