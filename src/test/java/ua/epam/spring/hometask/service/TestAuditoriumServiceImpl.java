package ua.epam.spring.hometask.service;

import org.junit.BeforeClass;
import org.junit.Test;
import ua.epam.spring.hometask.domain.Auditorium;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Created by Oleksii_Kovetskyi on 4/4/2018.
 */
public class TestAuditoriumServiceImpl {

    private static String[] namesArr;
    private static String[] seatsArr;
    private static String[] vipSeatsArr;
    private static AuditoriumService auditoriumService;

    @BeforeClass
    public static void init() throws IOException {
        Properties props = new Properties();
        props.load(TestAuditoriumServiceImpl.class.getClassLoader()
                .getResourceAsStream("auditoriums.properties"));

        String names = props.getProperty("auditorium.names");
        String seats = props.getProperty("auditorium.seatsNumber");
        String vipSeats = props.getProperty("auditorium.vipSeats");
        namesArr = names.split(",");
        seatsArr = seats.split(",");
        vipSeatsArr = vipSeats.split("\\|");

        auditoriumService = new AuditoriumServiceImpl(names, seats, vipSeats);
    }

    @Test
    public void shouldReturnCorrectAuditoriumSetWhenCallingGetAll() {
        Map<String, Auditorium> auditoriumMap = auditoriumService.getAll().stream()
                .collect(Collectors.toMap(Auditorium::getName, a -> a));
        assertEquals(6, auditoriumMap.size());

        for (int i = 0; i < namesArr.length; i++) {
            assertEquals(auditoriumMap.get(namesArr[i]).getName(), namesArr[i]);
            assertEquals(auditoriumMap.get(namesArr[i]).getNumberOfSeats(), Long.parseLong(seatsArr[i]));

            Set<Long> vipSeatsSet = vipSeatsArr[i].isEmpty() ? Collections.emptySet() :
                    Arrays.stream(vipSeatsArr[i].split(","))
                            .mapToLong(Long::parseLong)
                            .collect(HashSet::new, HashSet::add, HashSet::addAll);
            assertEquals(auditoriumMap.get(namesArr[i]).getVipSeats(), vipSeatsSet);
        }
    }

    @Test
    public void shouldReturnCorrectAuditoriumWhenCallingGetByName() {
        for (int i = 0; i < namesArr.length; i++) {
            Auditorium auditorium = auditoriumService.getByName(namesArr[i]);
            assertEquals(auditorium.getName(), namesArr[i]);
            assertEquals(auditorium.getNumberOfSeats(), Long.parseLong(seatsArr[i]));

            Set<Long> vipSeatsSet = vipSeatsArr[i].isEmpty() ? Collections.emptySet() :
                    Arrays.stream(vipSeatsArr[i].split(","))
                            .mapToLong(Long::parseLong)
                            .collect(HashSet::new, HashSet::add, HashSet::addAll);
            assertEquals(auditorium.getVipSeats(), vipSeatsSet);
        }
    }
}
