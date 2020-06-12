package rest.client;

import swimmingContest.model.SwimmingEvent;
import swimmingContest.services.rest.ServiceException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

public class SwimmingEventsClient {
    public static final String URL = "http://localhost:8080/swimmingContest/swimmingEvents";

    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new ServiceException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public SwimmingEvent[] getAll() {
        return execute(() -> restTemplate.getForObject(URL, SwimmingEvent[].class));
    }

    public SwimmingEvent getById(Long id) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id), SwimmingEvent.class));
    }

    public SwimmingEvent create(SwimmingEvent swimmingEvent) {
        return execute(() -> restTemplate.postForObject(URL, swimmingEvent, SwimmingEvent.class));
    }

    public void update(SwimmingEvent swimmingEvent) {
        execute(() -> {
            restTemplate.put(String.format("%s/%s", URL, swimmingEvent.getId()), swimmingEvent);
            return null;
        });
    }

    public void delete(Long id) {
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id));
            return null;
        });
    }

}
