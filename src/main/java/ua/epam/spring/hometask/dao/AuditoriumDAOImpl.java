package ua.epam.spring.hometask.dao;

import org.springframework.stereotype.Component;
import ua.epam.spring.hometask.dao.interf.AuditoriumDAO;
import ua.epam.spring.hometask.domain.Auditorium;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

/**
 * Created by Oleksii_Kovetskyi on 4/4/2018.
 */
@Component
public class AuditoriumDAOImpl implements AuditoriumDAO {

    private Map<String, Auditorium> auditoriumMap;

    @PostConstruct
    public void init() throws IOException {
        auditoriumMap = new HashMap<>();

        Properties props = new Properties();
        props.load(this.getClass().getClassLoader().getResourceAsStream("auditoriums.properties"));

        String[] names = props.getProperty("auditorium.names").split(",");
        String[] seatsNumbers = props.getProperty("auditorium.seatsNumber").split(",");
        String[] vipSeats = props.getProperty("auditorium.vipSeats").split("\\|");

        for (int i = 0; i < names.length; i++) {
            Auditorium auditorium = new Auditorium();
            auditorium.setName(names[i]);
            auditorium.setNumberOfSeats(Integer.parseInt(seatsNumbers[i]));

            Set<Integer> vipSeatsSet = vipSeats[i].isEmpty() ? Collections.emptySet() :
                    Arrays.stream(vipSeats[i].split(","))
                    .mapToInt(Integer::parseInt)
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
