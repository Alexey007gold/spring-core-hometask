package ua.epam.spring.hometask.service.impl;

import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.dao.interf.EventStatsDAO;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.EventStats;
import ua.epam.spring.hometask.service.interf.EventCounterStatsService;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
@Service
public class EventCounterStatsServiceImpl implements EventCounterStatsService {

    private EventStatsDAO eventStatsCounterDAO;

    public EventCounterStatsServiceImpl(EventStatsDAO eventStatsCounterDAO) {
        this.eventStatsCounterDAO = eventStatsCounterDAO;
    }

    @Override
    public int getAccessByNameCount(Event event) {
        EventStats stats = eventStatsCounterDAO.getByEventId(event.getId());
        return stats != null ? stats.getAccessByName() : 0;
    }

    @Override
    public void incrementAccessByNameCount(Event event) {
        EventStats stats = getEventStats(event);
        stats.incrementAccessByName();
        eventStatsCounterDAO.save(stats);
    }

    @Override
    public int getPriceQueryCount(Event event) {
        EventStats stats = eventStatsCounterDAO.getByEventId(event.getId());
        return stats != null ? stats.getPriceQuery() : 0;
    }

    @Override
    public void incrementPriceQueryCount(Event event) {
        EventStats stats = getEventStats(event);
        stats.incrementPriceQuery();
        eventStatsCounterDAO.save(stats);
    }

    @Override
    public int getTicketsBookedTimesCount(Event event) {
        EventStats stats = eventStatsCounterDAO.getByEventId(event.getId());
        return stats != null ? stats.getTicketsBooked() : 0;
    }

    @Override
    public void incrementTicketsBookedTimesCount(Event event) {
        EventStats stats = getEventStats(event);
        stats.incrementTicketsBookedTimes();
        eventStatsCounterDAO.save(stats);
    }

    private EventStats getEventStats(Event event) {
        EventStats stats = eventStatsCounterDAO.getByEventId(event.getId());
        if (stats == null) {
            stats = new EventStats();
            stats.setEventId(event.getId());
        }
        return stats;
    }
}
