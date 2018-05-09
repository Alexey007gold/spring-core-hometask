package ua.epam.spring.hometask.client;

import org.springframework.http.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

/**
 * Created by Oleksii_Kovetskyi on 5/9/2018.
 */
public class RestServiceClient {

    private RestTemplate restTemplate;

    public RestServiceClient() {
//        MappingJackson2HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter();
//        jacksonConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
        byteArrayHttpMessageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        this.restTemplate = new RestTemplate(Collections.singletonList(byteArrayHttpMessageConverter));
    }

    public ResponseEntity<byte[]> get(String url, Map<String, String> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpHeaders.add(entry.getKey(), entry.getValue());
            }
        }

        HttpEntity entity = new HttpEntity<>(null, httpHeaders);
        return restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);
    }

    public ResponseEntity<byte[]> post(String url, String body, MediaType contentType) {
        HttpHeaders headers = new HttpHeaders();
        if (contentType != null) {
            headers.setContentType(contentType);
        }

        HttpEntity<byte[]> entity = new HttpEntity<>(body.getBytes(), headers);
        return restTemplate.exchange(url, HttpMethod.POST, entity, byte[].class);
    }
}
