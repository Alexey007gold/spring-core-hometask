package ua.epam.spring.hometask.dao.interf;

import ua.epam.spring.hometask.domain.Event;

/**
 * Created by Oleksii_Kovetskyi on 4/20/2018.
 */
public interface EventDAO extends DomainObjectDAO<Event> {

    public Event getByName(String name);
}
