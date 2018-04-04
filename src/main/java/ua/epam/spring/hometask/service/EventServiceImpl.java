package ua.epam.spring.hometask.service;

import ua.epam.spring.hometask.domain.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by Oleksii_Kovetskyi on 4/4/2018.
 */
public class EventServiceImpl extends AbstractDomainObjectServiceImpl<Event> implements EventService {

    @Nullable
    @Override
    public Event getByName(@Nonnull String name) {
        for (Event event : domainObjectMap.values()) {
            if (event.getName().equals(name)) {
                return event;
            }
        }
        return null;
    }
}
