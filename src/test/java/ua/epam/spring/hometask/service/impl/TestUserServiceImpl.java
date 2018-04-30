package ua.epam.spring.hometask.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.epam.spring.hometask.TestDataCreator;
import ua.epam.spring.hometask.dao.UserDAOImpl;
import ua.epam.spring.hometask.dao.interf.UserDAO;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.interf.UserAccountService;
import ua.epam.spring.hometask.service.interf.UserRoleService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by Oleksii_Kovetskyi on 4/30/2018.
 */
public class TestUserServiceImpl {

    private UserServiceImpl userService;
    private UserDAO userDAO;
    private UserRoleService userRoleService;
    private UserAccountService userAccountService;
    private PasswordEncoder passwordEncoder;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void init(){
        userDAO = mock(UserDAOImpl.class);
        userRoleService = mock(UserRoleServiceImpl.class);
        userAccountService = mock(UserAccountServiceImpl.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);

        userService = new UserServiceImpl(userDAO, userRoleService, userAccountService, passwordEncoder);

        user1 = TestDataCreator.createUser(1L, "John", "Doe", "abcd@gmail.com",
                "johndoe", "1234", null, new HashSet<>());
        user2 = TestDataCreator.createUser(2L, "Bill", "Gates", "bill@gmail.com",
                "billgates", "4321", null, new HashSet<>());
        user3 = TestDataCreator.createUser(3L, "Elon", "Musk", "musk@gmail.com",
                "elonmusk", "8765", null, new HashSet<>());
    }

    @Test
    public void shouldCallDAOMethodOnGetUserByEmailCall() {
        when(userDAO.getByEmail("bill@gmail.com")).thenReturn(user2);
        User userByEmail = userService.getUserByEmail("bill@gmail.com");

        assertEquals("Bill", userByEmail.getFirstName());

        verify(userDAO, times(1)).getByEmail("bill@gmail.com");
    }

    @Test
    public void shouldCallDAOMethodOnGetUsersByFirstNameCall() {
        when(userDAO.getBy(new String[] {"first_name"}, "John")).thenReturn(Collections.singletonList(user1));
        List<User> users = userService.getUsersByFirstName("John");

        assertEquals("John", users.get(0).getFirstName());

        verify(userDAO, times(1)).getBy(new String[] {"first_name"}, "John");
    }

    @Test
    public void shouldCallDAOMethodOnGetUsersByLastNameCall() {
        when(userDAO.getBy(new String[] {"last_name"}, "Doe")).thenReturn(Collections.singletonList(user1));
        List<User> users = userService.getUsersByLastName("Doe");

        assertEquals("John", users.get(0).getFirstName());

        verify(userDAO, times(1)).getBy(new String[] {"last_name"}, "Doe");
    }

    @Test
    public void shouldCallDAOMethodOnGetUsersByFirstAndLastNameCall() {
        when(userDAO.getBy(new String[] {"first_name", "last_name"}, "John", "Doe")).thenReturn(Collections.singletonList(user1));
        List<User> users = userService.getUsersByFirstAndLastName("John", "Doe");

        assertEquals("John", users.get(0).getFirstName());

        verify(userDAO, times(1)).getBy(new String[] {"first_name", "last_name"}, "John", "Doe");
    }

    @Test
    public void shouldCallDAOMethodOnGetUserByLoginCall() {
        when(userDAO.getOneBy(new String[] {"login"}, "elonmusk")).thenReturn(user3);
        User user = userService.getUserByLogin("elonmusk");

        assertEquals("Elon", user.getFirstName());

        verify(userDAO, times(1)).getOneBy(new String[] {"login"}, "elonmusk");
    }

    @Test
    public void shouldCallDAOMethodOnGetUserIdByLoginCall() {
        when(userDAO.getUserIdByLogin("elonmusk")).thenReturn(3L);
        Long userId = userService.getUserIdByLogin("elonmusk");

        assertEquals(3L, (long) userId);

        verify(userDAO, times(1)).getUserIdByLogin("elonmusk");
    }

    @Test
    public void shouldCallDAOMethodOnIsUserRegisteredCall() {
        when(userDAO.getById(3L)).thenReturn(user3);
        boolean registered = userService.isUserRegistered(user3);

        assertTrue(registered);

        verify(userDAO, times(1)).getById(3L);
    }

    @Test
    public void shouldSaveUsersToDAO_OnParseUsersFromInputStreamCall() throws IOException {
        String str = "John,Doe,asa@gmail.com,user11,1234,111111,USER\n" +
                "John,John,assa@gmail.com,user21,1234,1111111,USER\n" +
                "Alex,Koveckiy,admin@gmail.com,admin1,1234,5555555,ADMIN,BOOKING_MANAGER";
        InputStream inputStream = new ByteArrayInputStream(str.getBytes());
        userService.parseUsersFromInputStream(inputStream);

        verify(passwordEncoder, times(3)).encode(any());
        verify(userRoleService, times(4)).save(any());
        verify(userAccountService, times(3)).save(any());
    }
}