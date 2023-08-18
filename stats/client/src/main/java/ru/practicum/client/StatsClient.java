package ru.practicum.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.model.hit.dto.HitDto;
import ru.practicum.model.hit.dto.UriStatDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Component
public class StatsClient {
    private static final String URL = "http://stats-server:9090";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final RestTemplate restTemplate;

    public StatsClient() {
        restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }

    public void postHit(HitDto hitDto) {
        String url = URL + "/hit";
        restTemplate.postForEntity(url, hitDto, Void.class);
    }

    public List<UriStatDto> getStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        String url = URL + "/stats?start={start}&end={end}&uris={uris}&unique={unique}";
        ResponseEntity<List<UriStatDto>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<>() {
                },
                Map.of("start", start.format(formatter), "end", end.format(formatter), "uris", uris, "unique", unique)
        );
        return responseEntity.getBody();
    }
}
