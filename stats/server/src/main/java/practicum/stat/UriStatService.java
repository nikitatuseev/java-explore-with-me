package practicum.stat;

import java.time.LocalDateTime;
import java.util.List;

public interface UriStatService {
    List<UriStat> getAll(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
