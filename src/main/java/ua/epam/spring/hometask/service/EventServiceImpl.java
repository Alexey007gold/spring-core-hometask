package ua.epam.spring.hometask.service;

import ua.epam.spring.hometask.domain.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Oleksii_Kovetskyi on 4/4/2018.
 */
public class EventServiceImpl implements EventService {

    private Map<Long, Event> eventMap;
    private long lastId;

    public EventServiceImpl() {
        eventMap = new HashMap<>();
    }

    @Nullable
    @Override
    public Event getByName(@Nonnull String name) {
        for (Event event : eventMap.values()) {
            if (event.getName().equals(name)) {
                return event;
            }
        }
        return null;
    }

    @Override
    public Event save(@Nonnull Event object) {
        if (object.getId() == null) {
            object.setId(++lastId);
        }
        return eventMap.put(lastId, object);
    }

    @Override
    public void remove(@Nonnull Event object) {
        eventMap.remove(object.getId());
    }

    @Override
    public Event getById(@Nonnull Long id) {
        return eventMap.get(id);
    }

    @Nonnull
    @Override
    public Collection<Event> getAll() {
        return new ArrayList<>(eventMap.values());
    }
}
