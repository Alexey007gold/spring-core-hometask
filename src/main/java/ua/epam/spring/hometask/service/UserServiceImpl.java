package ua.epam.spring.hometask.service;

import ua.epam.spring.hometask.domain.User;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Created on 4/4/2018.
 */
public class UserServiceImpl implements UserService {

    private Map<Long, User> userMap;
    private long lastId;

    public UserServiceImpl() {
        userMap = new HashMap<>();
    }

    @Nullable
    @Override
    public User getUserByEmail(@Nonnull String email) {
        for (User user : userMap.values()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User save(@Nonnull User object) {
        if (object.getId() == null) {
            object.setId(++lastId);
        }
        return userMap.put(object.getId(), object);
    }

    @Override
    public void remove(@Nonnull User object) {
        userMap.remove(object.getId());
    }

    @Override
    public User getById(@Nonnull Long id) {
        return userMap.get(id);
    }

    @Nonnull
    @Override
    public Collection<User> getAll() {
        return new ArrayList<>(userMap.values());
    }
}
