package ua.epam.spring.hometask.service;

import ua.epam.spring.hometask.domain.Auditorium;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Created by Oleksii_Kovetskyi on 4/4/2018.
 */
public class AuditoriumServiceImpl implements AuditoriumService {

    private Map<String, Auditorium> auditoriumMap;

    public AuditoriumServiceImpl(String auditoriumNames,String auditoriumSeatsNumbers,
                                 String auditoriumVipSeats) {
        auditoriumMap = new HashMap<>();

        String[] names = auditoriumNames.split(",");
        String[] seatsNumbers = auditoriumSeatsNumbers.split(",");
        String[] vipSeats = auditoriumVipSeats.split("\\|");

        for (int i = 0; i < names.length; i++) {
            Auditorium auditorium = new Auditorium();
            auditorium.setName(names[i]);
            auditorium.setNumberOfSeats(Long.parseLong(seatsNumbers[i]));

            Set<Long> vipSeatsSet = vipSeats[i].isEmpty() ? Collections.emptySet() :
                    Arrays.stream(vipSeats[i].split(","))
                    .mapToLong(Long::parseLong)
                    .collect(HashSet::new, HashSet::add, HashSet::addAll);
            auditorium.setVipSeats(vipSeatsSet);
            auditoriumMap.put(names[i], auditorium);
        }
    }

    @Nonnull
    @Override
    public Set<Auditorium> getAll() {
        return new HashSet<>(auditoriumMap.values());
    }

    @Nullable
    @Override
    public Auditorium getByName(@Nonnull String name) {
        return auditoriumMap.get(name);
    }
}
