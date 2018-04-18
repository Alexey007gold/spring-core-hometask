package ua.epam.spring.hometask.service.impl;

import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.dao.UserDAO;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.service.interf.UserService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by Oleksii_Kovetskyi on 4/4/2018.
 */
@Service
public class UserServiceImpl extends AbstractDomainObjectServiceImpl<User, UserDAO> implements UserService {

    public UserServiceImpl(UserDAO userDAO) {
        super(userDAO);
    }

    @Nullable
    @Override
    public User getUserByEmail(@Nonnull String email) {
        return domainObjectDAO.getByEmail(email);
    }

    @Nullable
    @Override
    public List<User> getUsersByFirstName(@Nonnull String firstName) {
        return domainObjectDAO.getBy(new String[] {"first_name"}, firstName);
    }

    @Nullable
    @Override
    public List<User> getUsersByLastName(@Nonnull String lastName) {
        return domainObjectDAO.getBy(new String[] {"last_name"}, lastName);
    }

    @Nullable
    @Override
    public List<User> getUsersByFirstAndLastName(@Nonnull String firstName, @Nonnull String lastName) {
        return domainObjectDAO.getBy(new String[] {"first_name", "last_name"}, firstName, lastName);
    }
}
