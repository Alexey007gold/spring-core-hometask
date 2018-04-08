package ua.epam.spring.hometask.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.spring.hometask.configuration.AppConfiguration;
import ua.epam.spring.hometask.domain.User;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Created by Oleksii_Kovetskyi on 4/5/2018.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfiguration.class})
@Transactional(rollbackFor = Exception.class)
public class TestAbstractDomainObjectServiceImpl {

    @Autowired
    private AbstractDomainObjectService<User> abstractDomainObjectService;
    private User user1;
    private User user2;

    @Before
    public void init() {
        user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("example@gmail.com");
        user1.setBirthDate(LocalDate.of(1990, 11, 11));
        abstractDomainObjectService.save(user1);

        user2 = new User();
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

        assertNotNull(user1.getId());
        assertNotNull(user2.getId());
    }

    @Test
    public void shouldRemoveObjectFromStorageOnRemoveCall() {
        abstractDomainObjectService.remove(user1);
        abstractDomainObjectService.remove(user2);

        assertNull(abstractDomainObjectService.getById(user1.getId()));
        assertNull(abstractDomainObjectService.getById(user2.getId()));
    }

    @Test
    public void shouldReturnObjectFromStorageOnGetByIdCall() {
        User user1 = abstractDomainObjectService.getById(this.user1.getId());
        User user2 = abstractDomainObjectService.getById(this.user2.getId());

        assertEquals((long) this.user1.getId(), (long) user1.getId());
        assertEquals("John", user1.getFirstName());
        assertEquals((long) this.user2.getId(), (long) user2.getId());
        assertEquals("Jane", user2.getFirstName());
    }

    @Test
    public void shouldReturnAllObjectsFromStorageOnGetAllCall() {
        Collection<User> userCollection = abstractDomainObjectService.getAll();

        assertTrue(userCollection.contains(user1));
        assertTrue(userCollection.contains(user2));
    }
}
