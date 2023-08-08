
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import practicum.HitDto;
import practicum.StatsClient;
import practicum.UriStatDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsClientTests {

    @Mock
    private RestTemplate restTemplate;

    private StatsClient statsClient;

    @BeforeEach
    public void setup() {
        statsClient = new StatsClient(restTemplate);
    }

    @Test
    public void testSendHit() {
        String baseUrl = "http://example.com";
        HitDto hit = new HitDto();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<HitDto> responseEntity = new ResponseEntity<>(hit, HttpStatus.OK);
        when(restTemplate.exchange(eq(baseUrl + "/hit"), eq(HttpMethod.POST), any(HttpEntity.class), eq(HitDto.class)))
                .thenReturn(responseEntity);

        HitDto result = statsClient.sendHit(baseUrl, hit);

        assertNotNull(result);
    }

    @Test
    public void testGetStats() {
        String baseUrl = "http://example.com";
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusHours(1);
        List<UriStatDto> expectedStats = new ArrayList<>();
        ResponseEntity<List<UriStatDto>> responseEntity = new ResponseEntity<>(expectedStats, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), isNull(), any(ParameterizedTypeReference.class)))
                .thenReturn(responseEntity);

        List<UriStatDto> result = statsClient.getStats(baseUrl, start, end);

        assertNotNull(result);
        assertEquals(expectedStats.size(), result.size());
    }
}
