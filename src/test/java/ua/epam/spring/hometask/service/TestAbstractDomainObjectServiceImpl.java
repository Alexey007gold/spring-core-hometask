package ua.epam.spring.hometask.service;

import org.junit.Before;
import org.junit.Test;
import ua.epam.spring.hometask.domain.DomainObject;
import ua.epam.spring.hometask.domain.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by Oleksii_Kovetskyi on 4/5/2018.
 */
public class TestAbstractDomainObjectServiceImpl {

    private AbstractDomainObjectService<User> abstractDomainObjectService;

    @Before
    public void init() {
        abstractDomainObjectService = new AbstractDomainObjectServiceImpl<User>() {};
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("example@gmail.com");
        user1.setBirthDate(LocalDate.of(1990, 11, 11));
        abstractDomainObjectService.save(user1);

        User user2 = new User();
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setEmail("exampleJane@gmail.com");
        user2.setBirthDate(LocalDate.of(1993, 1, 22));
        abstractDomainObjectService.save(user2);
    }

    @Test
    public void shouldStoreObjectAndReturnItWithAssignedIdOnSaveCall() {
        User user1 = new User();
        user1.setFirstName("Bill");
        user1.setLastName("Doe");
        user1.setEmail("example@gmail.com");
        user1.setBirthDate(LocalDate.of(1990, 11, 11));
        user1 = abstractDomainObjectService.save(user1);

        User user2 = new User();
        user2.setFirstName("Tom");
        user2.setLastName("Smith");
        user2.setEmail("exampleJane@gmail.com");
        user2.setBirthDate(LocalDate.of(1993, 1, 22));
        user2 = abstractDomainObjectService.save(user2);

        assertEquals(3L, (long) user1.getId());
        assertEquals(4L, (long) user2.getId());
    }

    @Test
    public void shouldRemoveObjectFromStorageOnRemoveCall() {
        User user1 = abstractDomainObjectService.getById(1L);
        User user2 = abstractDomainObjectService.getById(2L);

        abstractDomainObjectService.remove(user1);
        abstractDomainObjectService.remove(user2);

        assertNull(abstractDomainObjectService.getById(1L));
        assertNull(abstractDomainObjectService.getById(2L));
    }

    @Test
    public void shouldReturnObjectFromStorageOnGetByIdCall() {
        User user1 = abstractDomainObjectService.getById(1L);
        User user2 = abstractDomainObjectService.getById(2L);

        assertEquals(1L, (long) user1.getId());
        assertEquals("John", user1.getFirstName());
        assertEquals(2L, (long) user2.getId());
        assertEquals("Jane", user2.getFirstName());
    }

    @Test
    public void shouldReturnAllObjectsFromStorageOnGetAllCall() {
        Collection<User> userCollection = abstractDomainObjectService.getAll();
        Map<Long, User> userMap = userCollection.stream()
                .collect(Collectors.toMap(DomainObject::getId, e -> e));

        assertEquals(1L, (long) userMap.get(1L).getId());
        assertEquals("John", userMap.get(1L).getFirstName());
        assertEquals(2L, (long) userMap.get(2L).getId());
        assertEquals("Jane", userMap.get(2L).getFirstName());
    }
}
