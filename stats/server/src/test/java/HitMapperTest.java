import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import practicum.HitDto;
import practicum.hit.Hit;
import practicum.hit.HitMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HitMapperTest {
    @InjectMocks
    private HitMapper hitMapper;

    @Test
    void toHit() {
        HitDto hitDto = new HitDto(
                "test-service",
                "/test/1",
                "192.168.0.1",
                LocalDateTime.of(2022, 1, 2, 3, 0, 0)
        );

        Hit result = hitMapper.toHit(hitDto);

        assertNotNull(result);
        assertEquals(hitDto.getApp(), result.getApp());
        assertEquals(hitDto.getUri(), result.getUri());
        assertEquals(hitDto.getIp(), result.getIp());
    }
}