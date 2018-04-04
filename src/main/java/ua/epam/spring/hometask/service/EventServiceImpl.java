package ua.epam.spring.hometask.service;

import ua.epam.spring.hometask.domain.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Oleksii_Kovetskyi on 4/4/2018.
 */
public class EventServiceImpl extends AbstractDomainObjectServiceImpl<Event> implements EventService {

    @Nullable
    @Override
    public Event getByName(@Nonnull String name) {
        return domainObjectMap.values().stream().filter(e -> e.getName().equals(name)).findFirst().orElse(null);
    }

    @Nonnull
    @Override
    public Set<Event> getForDateRange(@Nonnull LocalDate from, @Nonnull LocalDate to) {
        return domainObjectMap.values().stream()
                .filter(e -> e.getAirDates().stream()
                        .anyMatch(d -> d.isAfter(LocalDateTime.from(from)) && d.isBefore(LocalDateTime.from(to))))
                .collect(Collectors.toSet());
    }

    @Nonnull
    @Override
    public Set<Event> getNextEvents(@Nonnull LocalDateTime to) {
        LocalDateTime from = LocalDateTime.now();
        return domainObjectMap.values().stream()
                .filter(e -> e.getAirDates().stream()
                        .anyMatch(d -> d.isAfter(from) && d.isBefore(to)))
                .collect(Collectors.toSet());
    }


}
