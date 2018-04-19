package ua.epam.spring.hometask.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.spring.hometask.configuration.AppConfiguration;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.interf.UserService;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

/**
 * Created by Oleksii_Kovetskyi on 4/5/2018.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfiguration.class})
@Transactional(rollbackFor = Exception.class)
public class TestUserServiceImpl {

    @Autowired
    private UserService userService;

    @Before
    public void init() {
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("example@gmail.com");
        user1.setBirthDate(LocalDate.of(1990, 11, 11));
        userService.save(user1);

        User user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setEmail("exampleJane@gmail.com");
        user2.setBirthDate(LocalDate.of(1993, 1, 22));
        userService.save(user2);
    }

    @Test
    public void shouldReturnCorrectUserOnGetByEmailCall() {
        User res1 = userService.getUserByEmail("example@gmail.com");
        assertEquals("John", res1.getFirstName());
        assertEquals("Doe", res1.getLastName());
        assertEquals("example@gmail.com", res1.getEmail());
        assertEquals(LocalDate.of(1990, 11, 11), res1.getBirthDate());

        User res2 = userService.getUserByEmail("exampleJane@gmail.com");
        assertEquals("Jane", res2.getFirstName());
        assertEquals("Doe", res2.getLastName());
        assertEquals("exampleJane@gmail.com", res2.getEmail());
        assertEquals(LocalDate.of(1993, 1, 22), res2.getBirthDate());
    }
}