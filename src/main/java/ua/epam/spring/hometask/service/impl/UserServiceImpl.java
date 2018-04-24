package ua.epam.spring.hometask.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.spring.hometask.dao.interf.UserDAO;
import ua.epam.spring.hometask.domain.User;
import ua.epam.spring.hometask.domain.UserAccount;
import ua.epam.spring.hometask.domain.UserRole;
import ua.epam.spring.hometask.service.interf.UserAccountService;
import ua.epam.spring.hometask.service.interf.UserRoleService;
import ua.epam.spring.hometask.service.interf.UserService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleksii_Kovetskyi on 4/4/2018.
 */
@Service
public class UserServiceImpl extends AbstractDomainObjectServiceImpl<User, UserDAO> implements UserService {

    private UserRoleService userRoleService;
    private UserAccountService userAccountService;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDAO domainObjectDAO, UserRoleService userRoleService,
                           UserAccountService userAccountService, PasswordEncoder passwordEncoder) {
        super(domainObjectDAO);
        this.userRoleService = userRoleService;
        this.userAccountService = userAccountService;
        this.passwordEncoder = passwordEncoder;
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

    @Nullable
    @Override
    public User getUserByLogin(String login) {
        return domainObjectDAO.getOneBy(new String[] {"login"}, login);
    }

    @Nullable
    @Override
    public Long getUserIdByLogin(String login) {
        return domainObjectDAO.getUserIdByLogin(login);
    }

    @Override
    public boolean isUserRegistered(User user) {
        return user != null && user.getId() != null && getById(user.getId()) != null;
    }

    @Override
    @Transactional()
    public void parseUsersFromInputStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] split = line.split(",");
            if (getUserByLogin(split[3]) != null) continue;
            LocalDate dateTime = new Timestamp(Long.parseLong(split[5]) * 1000).toLocalDateTime().toLocalDate();
            User user = new User();
            user.setFirstName(split[0]);
            user.setLastName(split[1]);
            user.setEmail(split[2]);
            user.setLogin(split[3]);
            user.setPassword(passwordEncoder.encode(split[4]));
            user.setBirthDate(dateTime);
            save(user);

            List<UserRole> userRoles = new ArrayList<>();
            for (int i = 6; i < split.length; i++) {
                userRoles.add(new UserRole(user.getId(), split[i]));
            }
            userRoles.forEach(ur -> userRoleService.save(ur));

            userAccountService.save(new UserAccount(user.getId(), 0));
        }
        reader.close();
    }
}
