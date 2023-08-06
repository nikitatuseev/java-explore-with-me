import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import practicum.UriStatDto;
import practicum.stat.UriStat;
import practicum.stat.UriStatMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class UriStatMapperTest {
    @InjectMocks
    private UriStatMapper uriStatMapper;

    @Test
    void toUriStatDto() {
        UriStat uriStat = new UriStat(
                "test-service",
                "/test/1",
                6L
        );

        UriStatDto result = uriStatMapper.toUriStatDto(uriStat);

        assertNotNull(result);
        assertEquals(uriStat.getApp(), result.getApp());
        assertEquals(uriStat.getUri(), result.getUri());
        assertEquals(uriStat.getHits(), result.getHits());
    }
}