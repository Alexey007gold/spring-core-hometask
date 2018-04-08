package ua.epam.spring.hometask.domain;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 * @author Yuriy_Tkach
 */
public class TestEvent {

	private Event event;

	@Before
	public void initEvent() {
		event = new Event();
		event.setBasePrice(1.1);
		event.setName("aaa");
		event.setRating(EventRating.HIGH);
	
		LocalDateTime now = LocalDateTime.now();

		Auditorium auditorium = new Auditorium();
		event.addAirDateTime(new EventDate(now, auditorium));
		event.addAirDateTime(new EventDate(now.plusDays(1), auditorium));
		event.addAirDateTime(new EventDate(now.plusDays(2), auditorium));
	}
	
	@Test
	public void testAddRemoveAirDates() {
		int size = event.getAirDates().size();
		
		LocalDateTime date = LocalDateTime.now().plusDays(5);
		
		event.addAirDateTime(new EventDate(date, new Auditorium()));
		
		assertEquals(size+1, event.getAirDates().size());
		assertTrue(event.getAirDates().containsKey(date));
		
		event.removeAirDateTime(date);
		
		assertEquals(size, event.getAirDates().size());
		assertFalse(event.getAirDates().containsKey(date));
	}
	
	@Test
	public void testCheckAirDates() {
		assertTrue(event.airsOnDate(LocalDate.now()));
		assertTrue(event.airsOnDate(LocalDate.now().plusDays(1)));
		assertFalse(event.airsOnDate(LocalDate.now().minusDays(10)));
		
		assertTrue(event.airsOnDates(LocalDate.now(), LocalDate.now().plusDays(10)));
		assertTrue(event.airsOnDates(LocalDate.now().minusDays(10), LocalDate.now().plusDays(10)));
		assertTrue(event.airsOnDates(LocalDate.now().plusDays(1), LocalDate.now().plusDays(1)));
		assertFalse(event.airsOnDates(LocalDate.now().minusDays(10), LocalDate.now().minusDays(5)));
		
		LocalDateTime time = LocalDateTime.now().plusHours(4);
		event.addAirDateTime(new EventDate(time, new Auditorium()));
		assertTrue(event.airsOnDateTime(time));
		time = time.plusHours(30);
		assertFalse(event.airsOnDateTime(time));
	}

}
