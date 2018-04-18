package ua.epam.spring.hometask.service.impl;

import org.springframework.stereotype.Service;
import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.service.interf.AuditoriumService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

/**
 * Created by Oleksii_Kovetskyi on 4/4/2018.
 */
@Service
public class AuditoriumServiceImpl implements AuditoriumService {

    private Map<String, Auditorium> auditoriumMap;

    public AuditoriumServiceImpl() throws IOException {
        auditoriumMap = new HashMap<>();

        Properties props = new Properties();
        props.load(this.getClass().getClassLoader().getResourceAsStream("auditoriums.properties"));

        String[] names = props.getProperty("auditorium.names").split(",");
        String[] seatsNumbers = props.getProperty("auditorium.seatsNumber").split(",");
        String[] vipSeats = props.getProperty("auditorium.vipSeats").split("\\|");

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
