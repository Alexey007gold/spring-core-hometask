package ua.epam.spring.hometask.service;

import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.dao.UserDAO;
import ua.epam.spring.hometask.domain.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
}
