package ua.epam.spring.hometask.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ua.epam.spring.hometask.configuration.AppConfiguration;
import ua.epam.spring.hometask.dao.AbstractDomainObjectDAO;
import ua.epam.spring.hometask.domain.UserRole;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Oleksii_Kovetskyi on 4/5/2018.
 */
public class TestAbstractDomainObjectDAOImpl {

    private AbstractDomainObjectDAO<UserRole> abstractDomainObjectDAO;
    private UserRole userRole1;
    private UserRole userRole2;

    @Before
    public void init() throws IOException {
        JdbcTemplate jdbcTemplate = AppConfiguration.jdbcTemplate(new AppConfiguration().getDataSource());
        abstractDomainObjectDAO = new AbstractDomainObjectDAO<UserRole>(jdbcTemplate) {
            @Override
            protected RowMapper<UserRole> getRowMapper() {
                return (resultSet, i) -> {
                    UserRole userRole = new UserRole(resultSet.getLong(2), resultSet.getString(3));
                    userRole.setId(resultSet.getLong(1));
                    return userRole;
                };
            }

            @Override
            protected String getTableName() {
                return "user_roles";
            }

            @Override
            public void save(UserRole userRole) {
                KeyHolder keyHolder = new GeneratedKeyHolder();
                jdbcTemplate.update(con -> {
                    String sql = String.format("INSERT INTO %s (user_id, role) VALUES (?, ?)", getTableName());
                    PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setLong(1, userRole.getUserId());
                    ps.setString(2, userRole.getRole());
                    return ps;
                }, keyHolder);

                userRole.setId((Long) keyHolder.getKeys().get("id"));
            }
        };
        userRole1 = new UserRole(1L, "USER");
        abstractDomainObjectDAO.save(userRole1);

        userRole2 = new UserRole(2L, "ADMIN");
        abstractDomainObjectDAO.save(userRole2);
    }

    @After
    public void clear() {
        for (UserRole role : abstractDomainObjectDAO.getAll()) {
            abstractDomainObjectDAO.remove(role);
        }
    }

    @Test
    public void shouldStoreObjectAndReturnItWithAssignedIdOnSaveCall() {
        UserRole role1 = new UserRole(3L, "SOME_ROLE");
        abstractDomainObjectDAO.save(role1);

        UserRole role2 = new UserRole(3L, "ANOTHER_ROLE");
        abstractDomainObjectDAO.save(role2);

        assertNotNull(role1.getId());
        assertNotNull(role2.getId());
    }

    @Test
    public void shouldRemoveObjectFromStorageOnRemoveCall() {
        abstractDomainObjectDAO.remove(userRole1);
        abstractDomainObjectDAO.remove(userRole2);

        assertNull(abstractDomainObjectDAO.getById(userRole1.getId()));
        assertNull(abstractDomainObjectDAO.getById(userRole2.getId()));
    }

    @Test
    public void shouldReturnObjectFromStorageOnGetByIdCall() {
        UserRole role1 = abstractDomainObjectDAO.getById(this.userRole1.getId());
        UserRole role2 = abstractDomainObjectDAO.getById(this.userRole2.getId());

        assertEquals((long) this.userRole1.getId(), (long) role1.getId());
        assertEquals(this.userRole1.getUserId(), role1.getUserId());
        assertEquals((long) this.userRole2.getId(), (long) role2.getId());
        assertEquals(this.userRole2.getUserId(), role2.getUserId());
    }

    @Test
    public void shouldReturnObjectFromStorageOnGetByCall() {
        List<UserRole> role1 = abstractDomainObjectDAO.getBy(new String[] {"role"}, this.userRole1.getRole());
        List<UserRole> role2 = abstractDomainObjectDAO.getBy(new String[] {"user_id"}, this.userRole2.getUserId());

        assertEquals((long) this.userRole1.getId(), (long) role1.get(0).getId());
        assertEquals(this.userRole1.getUserId(), role1.get(0).getUserId());
        assertEquals((long) this.userRole2.getId(), (long) role2.get(0).getId());
        assertEquals(this.userRole2.getUserId(), role2.get(0).getUserId());
    }

    @Test
    public void shouldReturnAllObjectsFromStorageOnGetAllCall() {
        Collection<UserRole> userCollection = abstractDomainObjectDAO.getAll();

        assertTrue(userCollection.contains(userRole1));
        assertTrue(userCollection.contains(userRole2));
    }
}
