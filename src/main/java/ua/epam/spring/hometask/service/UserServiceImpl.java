package ua.epam.spring.hometask.service;

import ua.epam.spring.hometask.domain.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created on 4/4/2018.
 */
public class UserServiceImpl extends AbstractDomainObjectServiceImpl<User> implements UserService {


    @Nullable
    @Override
    public User getUserByEmail(@Nonnull String email) {
        for (User user : domainObjectMap.values()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }
}
