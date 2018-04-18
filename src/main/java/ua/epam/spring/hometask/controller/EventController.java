package ua.epam.spring.hometask.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.epam.spring.hometask.domain.Auditorium;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.domain.EventDate;
import ua.epam.spring.hometask.domain.EventRating;
import ua.epam.spring.hometask.service.interf.AuditoriumService;
import ua.epam.spring.hometask.service.interf.EventService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

/**
 * Created by Oleksii_Kovetskyi on 4/16/2018.
 */
@Controller
@RequestMapping("/events")
public class EventController {

    private EventService eventService;
    private AuditoriumService auditoriumService;

    public EventController(EventService eventService, AuditoriumService auditoriumService) {
        this.eventService = eventService;
        this.auditoriumService = auditoriumService;
    }

    @RequestMapping("/coming")
    public String getComingEvents(@ModelAttribute("model") ModelMap model, @RequestParam(required = false) Long until) {
        model.addAttribute("eventList", getEvents(until));
        return "events";
    }

    @RequestMapping("/coming/pdf")
    public String getComingEventsPdf(Model model, @RequestParam(required = false) Long until) {
        model.addAttribute("eventList", getEvents(until));
        return "eventPdfView";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void uploadEvents(@RequestParam("file") MultipartFile file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] split = line.split(",");
            Set<EventDate> eventDateSet = new HashSet<>();
            for (int i = 3; i < split.length; i++) {
                LocalDateTime dateTime = Timestamp.from(Instant.ofEpochSecond(Long.parseLong(split[i]))).toLocalDateTime();
                Auditorium auditorium = auditoriumService.getByName(split[++i]);
                eventDateSet.add(new EventDate(dateTime, auditorium));
            }
            Event event = new Event();
            event.setName(split[0]);
            event.setBasePrice(Double.parseDouble(split[1]));
            event.setRating(EventRating.valueOf(split[2]));
            event.setAirDates(eventDateSet);
            eventService.save(event);
        }
        reader.close();
    }


    private Set<Event> getEvents(Long until) {
        LocalDateTime to;
        if (until == null) {
            to = LocalDateTime.now().plusYears(1);
        } else {
            to = LocalDateTime.ofInstant(Instant.ofEpochSecond(until), TimeZone.getDefault().toZoneId());
        }
        return eventService.getNextEvents(to);
    }
}
