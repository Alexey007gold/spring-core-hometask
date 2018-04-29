package ua.epam.spring.hometask.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.service.interf.EventService;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.TimeZone;

/**
 * Created by Oleksii_Kovetskyi on 4/16/2018.
 */
@Controller
@RequestMapping("/events")
public class EventController {

    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @RequestMapping("/coming")
    public String getComingEvents(Model model,
                                  @RequestParam(required = false) Long until,
                                  @RequestParam(required = false, defaultValue = "false") boolean pdf) {
        model.addAttribute("eventList", getEvents(until));

        return pdf ? "eventPdfView" : "events";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public void uploadEvents(@RequestParam("file") MultipartFile file) throws IOException {
        eventService.parseEventsFromInputStream(file.getInputStream());
    }

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String uploadUsersPage() {
        return "upload_events";
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
