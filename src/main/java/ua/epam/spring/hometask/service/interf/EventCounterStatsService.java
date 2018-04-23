package ua.epam.spring.hometask.service.interf;

import ua.epam.spring.hometask.domain.Event;

/**
 * Created by Oleksii_Kovetskyi on 4/8/2018.
 */
public interface EventCounterStatsService {
    int getAccessByNameCount(Event event);

    void incrementAccessByNameCount(Event event);

    int getPriceQueryCount(Event event);

    void incrementPriceQueryCount(Event event);

    int getTicketsBookedTimesCount(Event event);

    void incrementTicketsBookedTimesCount(Event event);
}
