package ua.epam.spring.hometask.soap.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ua.epam.spring.hometask.domain.Event;
import ua.epam.spring.hometask.service.interf.EventService;
import ua.epam.spring.hometask.soap.message.*;

import static ua.epam.spring.hometask.configuration.SoapConfig.NAMESPACE_URI;
import static ua.epam.spring.hometask.soap.message.Result.FAIL;
import static ua.epam.spring.hometask.soap.message.Result.SUCCESS;

@Endpoint
public class EventEndpoint {

    private EventService eventService;

    @Autowired
    public EventEndpoint(EventService eventService) {
        this.eventService = eventService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getEventRequest")
    @ResponsePayload
    public GetEventResponse getEvent(@RequestPayload GetEventRequest request) {
        Event event = eventService.getByName(request.getName());
        GetEventResponse response = new GetEventResponse();
        response.setEvent(event);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addEventRequest")
    @ResponsePayload
    public AddEventResponse addEvent(@RequestPayload AddEventRequest request) {
        AddEventResponse response = new AddEventResponse();
        try {
            eventService.save(request.getEvent());
            response.setResult(SUCCESS);
        } catch (Exception e) {
            response.setResult(FAIL);
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteEventRequest")
    @ResponsePayload
    public DeleteEventResponse deleteEvent(@RequestPayload DeleteEventRequest request) {
        Event event = eventService.getByName(request.getName());
        DeleteEventResponse response = new DeleteEventResponse();
        if (event != null) {
            eventService.remove(event);
            response.setResult(SUCCESS);
        } else {
            response.setResult(FAIL);
        }
        return response;
    }
}