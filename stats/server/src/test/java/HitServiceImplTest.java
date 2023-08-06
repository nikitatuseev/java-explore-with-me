import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import practicum.hit.Hit;
import practicum.hit.HitRepository;
import practicum.hit.HitServiceImpl;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HitServiceImplTest {

    @Mock
    private HitRepository hitRepository;
    @InjectMocks
    private HitServiceImpl hitServiceImpl;

    @Test
    void create_whenInvoke_thenSuccess() {
        Hit newHit = new Hit(
                null,
                "test-service",
                "/test/1",
                "192.168.0.1",
                LocalDateTime.now()
        );

        hitServiceImpl.create(newHit);

        verify(hitRepository, times(1)).save(newHit);
        verifyNoMoreInteractions(hitRepository);
    }
}