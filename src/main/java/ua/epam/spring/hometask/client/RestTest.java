package ua.epam.spring.hometask.client;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Oleksii_Kovetskyi on 5/9/2018.
 */
public class RestTest {

    private RestServiceClient restServiceClient = new RestServiceClient();

    public static void main(String[] args) {
        RestTest restTest = new RestTest();
        restTest.testRestJson();
        restTest.testRestPdf();
        System.out.println("OK");
    }

    private void testRestJson() {
        Map<String, String> headers = loginAndGetHeaders();
        headers.put("Accept", "application/json");
        ResponseEntity<byte[]> response = restServiceClient.get("http://localhost:8080/rest/tickets/booked?eventId=20",
                headers);

        if (!"application/json;charset=UTF-8".equals(response.getHeaders().getContentType().toString()))
            throw new IllegalStateException();
        if (!new String(response.getBody()).startsWith("{"))
            throw new IllegalStateException();
    }

    private void testRestPdf() {
        Map<String, String> headers = loginAndGetHeaders();
        headers.put("Accept", "application/pdf");
        ResponseEntity<byte[]> response = restServiceClient.get("http://localhost:8080/rest/tickets/booked?eventId=20",
                headers);

        if (!"application/pdf".equals(response.getHeaders().getContentType().toString()))
            throw new IllegalStateException();
        if (!"%PDF-1.4".equals(new String(response.getBody()).substring(0, 8)))
            throw new IllegalStateException();
    }

    private Map<String, String> loginAndGetHeaders() {
        ResponseEntity<byte[]> loginResponse = restServiceClient.post("http://localhost:8080/login",
                "username=user2&password=1234", MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", loginResponse.getHeaders().get("Set-Cookie").get(0));
        return headers;
    }
}
