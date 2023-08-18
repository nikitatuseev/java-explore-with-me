package practicum.stat;

import ru.practicum.model.hit.dto.HitDto;
import ru.practicum.model.hit.dto.UriStatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void sendHit(HitDto hitDto);
    List<UriStatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
