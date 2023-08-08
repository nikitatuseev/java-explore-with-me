import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import practicum.hit.HitRepository;
import practicum.stat.UriStat;
import practicum.stat.UriStatServiceImpl;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UriStatServiceImplTest {
    @Mock
    private HitRepository hitRepository;
    @InjectMocks
    private UriStatServiceImpl uriStatServiceImpl;

    @Test
    void getAll() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        List<String> uris = List.of("/test/1", "/test/2");
        Boolean unique = true;

        when(hitRepository.getSelectedUriStatsWithUniqueIp(
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(List.class)))
                .thenReturn(List.of(new UriStat()));

        List<UriStat> result = uriStatServiceImpl.getAll(start, end, uris, unique);

        assertEquals(1, result.size());
    }

    @Test
    void getAllUrisEmptyAndNotUnique() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(2);
        List<String> uris = Collections.emptyList();
        Boolean unique = false;

        when(hitRepository.getAllUriStatsWithoutUniqueIp(
                any(LocalDateTime.class),
                any(LocalDateTime.class)))
                .thenReturn(List.of(new UriStat()));

        List<UriStat> result = uriStatServiceImpl.getAll(start, end, uris, unique);

        assertEquals(1, result.size());
    }
}