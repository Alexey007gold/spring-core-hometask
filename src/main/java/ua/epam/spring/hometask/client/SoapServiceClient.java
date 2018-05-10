package ua.epam.spring.hometask.client;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import ua.epam.spring.hometask.soap.message.*;

import javax.xml.bind.JAXBElement;

public class SoapServiceClient {

    private WebServiceTemplate webServiceTemplate;

    public static void main(String[] args) throws Exception {
        SoapServiceClient soapServiceClient = new SoapServiceClient("http://localhost:8080/ws");
        GetUserRequest request = new GetUserRequest();
        request.setLogin("user1");
        GetUserResponse user = soapServiceClient.getUser(request);
        System.out.println(user.getUser().getFirstName());
    }

    public SoapServiceClient(String uri) throws Exception {
        webServiceTemplate = new WebServiceTemplate();
        webServiceTemplate.setDefaultUri(uri);
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

        marshaller.setContextPath("ua.epam.spring.hometask.soap");
        marshaller.afterPropertiesSet();

        webServiceTemplate.setMarshaller(marshaller);
        webServiceTemplate.setUnmarshaller(marshaller);
        webServiceTemplate.afterPropertiesSet();
    }

    public GetUserResponse getUser(GetUserRequest request) {
        return (GetUserResponse) ((JAXBElement) webServiceTemplate.marshalSendAndReceive(request)).getValue();
    }

    public AddUserResponse addUser(AddUserRequest request) {
        return (AddUserResponse) ((JAXBElement) webServiceTemplate.marshalSendAndReceive(request)).getValue();
    }

    public DeleteUserResponse deleteUser(DeleteUserRequest request) {
        return (DeleteUserResponse) ((JAXBElement) webServiceTemplate.marshalSendAndReceive(request)).getValue();
    }

    public GetEventResponse getEvent(GetEventRequest request) {
        return (GetEventResponse) ((JAXBElement) webServiceTemplate.marshalSendAndReceive(request)).getValue();
    }

    public AddEventResponse addEvent(AddEventRequest request) {
        return (AddEventResponse) ((JAXBElement) webServiceTemplate.marshalSendAndReceive(request)).getValue();
    }

    public DeleteUserResponse deleteEvent(DeleteEventRequest request) {
        return (DeleteUserResponse) ((JAXBElement) webServiceTemplate.marshalSendAndReceive(request)).getValue();
    }
}