package ua.epam.spring.hometask.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.service.interf.EventService;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import static ua.epam.spring.hometask.view.EventPdfView.EVENT_LIST;

/**
 * Created by Oleksii_Kovetskyi on 4/16/2018.
 */
@Controller
@RequestMapping("/events")
public class EventController {

    private static final String HEADER_TEXT = "headerText";
    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @RequestMapping("/coming")
    public String getComingEvents(Model model,
                                  @RequestParam(required = false) Long until,
                                  @RequestParam(required = false, defaultValue = "false") boolean pdf) {
        model.addAttribute(EVENT_LIST, getEvents(until));
        model.addAttribute(HEADER_TEXT, "Coming Events");

        return pdf ? "eventPdfView" : "events";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadEvents(Model model, @RequestParam("file") MultipartFile file) throws IOException {
        List<Event> addedEvents = eventService.parseEventsFromInputStream(file.getInputStream());

        model.addAttribute(HEADER_TEXT, "Added Events");
        model.addAttribute(EVENT_LIST, addedEvents);
        return "events";
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
