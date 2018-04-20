package ua.epam.spring.hometask.dao.interf;

import ua.epam.spring.hometask.domain.EventStats;

/**
 * Created by Oleksii_Kovetskyi on 4/20/2018.
 */
public interface EventStatsDAO extends DomainObjectDAO<EventStats> {
    EventStats getByEventId(Long eventId);
}
