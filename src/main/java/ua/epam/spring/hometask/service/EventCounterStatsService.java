package ua.epam.spring.hometask.service;

import ua.epam.spring.hometask.domain.Event;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
public interface EventCounterStatsService {
    long getAccessByNameCount(Event event);

    void incrementAccessByNameCount(Event event);

    long getPriceQueryCount(Event event);

    void incrementPriceQueryCount(Event event);

    long getTicketsBookedTimesCount(Event event);

    void incrementTicketsBookedTimesCount(Event event);
}
