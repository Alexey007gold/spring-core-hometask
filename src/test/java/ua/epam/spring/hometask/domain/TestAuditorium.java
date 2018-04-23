package ua.epam.spring.hometask.domain;

import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

/**
 * @author Yuriy_Tkach
 */
public class TestAuditorium {
	
	@Test
	public void testCountVips() {
		Auditorium a = new Auditorium();
		a.setVipSeats(Stream.of(1,2,3).collect(Collectors.toSet()));
		assertEquals(0, a.countVipSeats(Arrays.asList(10, 20, 30)));
		assertEquals(1, a.countVipSeats(Arrays.asList(10, 2, 30)));
		assertEquals(2, a.countVipSeats(Arrays.asList(10, 2, 3, 4, 5, 6)));
	}

	@Test
	public void testGetAllSeats() {
	    Auditorium a = new Auditorium();
	    a.setNumberOfSeats(10);
	    assertEquals(10, a.getAllSeats().size());
	}

}
