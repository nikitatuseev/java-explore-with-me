package ru.practicum;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import practicum.HitDto;
import practicum.UriStatDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StatsClient {
    private final RestTemplate restTemplate;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatsClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public HitDto sendHit(String baseUrl, HitDto hit) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return restTemplate.exchange(
                baseUrl + "/hit",
                HttpMethod.POST,
                new HttpEntity<>(hit, headers),
                HitDto.class
        ).getBody();
    }

    public List<UriStatDto> getStats(String baseUrl, LocalDateTime start, LocalDateTime end) {
        String fullUrl = buildUrl(baseUrl, start, end, null, null);
        return getStatsFromUrl(fullUrl);
    }

    public List<UriStatDto> getStats(String baseUrl, LocalDateTime start, LocalDateTime end, List<String> uris) {
        String fullUrl = buildUrl(baseUrl, start, end, uris, null);
        return getStatsFromUrl(fullUrl);
    }

    public List<UriStatDto> getStats(String baseUrl, LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        String fullUrl = buildUrl(baseUrl, start, end, uris, unique);
        return getStatsFromUrl(fullUrl);
    }

    private String buildUrl(String baseUrl, LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("start", formatDate(start))
                .queryParam("end", formatDate(end));

        if (uris != null && !uris.isEmpty()) {
            builder.queryParam("uris", String.join(",", uris));
        }

        if (unique != null) {
            builder.queryParam("unique", unique);
        }

        return builder.toUriString();
    }

    private List<UriStatDto> getStatsFromUrl(String url) {
        ResponseEntity<List<UriStatDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );
        return response.getBody();
    }

    private String formatDate(LocalDateTime date) {
        return date.format(formatter);
    }
}
