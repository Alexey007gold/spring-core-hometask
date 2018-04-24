package ua.epam.spring.hometask.dao.interf;

import ua.epam.spring.hometask.domain.User;

/**
 * Created by Oleksii_Kovetskyi on 4/20/2018.
 */
public interface UserDAO extends DomainObjectDAO<User> {
    User getByEmail(String email);

    Long getUserIdByLogin(String login);
}
