package practicum.stat;

import practicum.hit.HitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UriStatServiceImpl implements UriStatService {
    private final HitRepository repository;

    @Override
    public List<UriStat> getAll(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (unique) {
            if (uris == null || uris.isEmpty()) {
                return repository.getAllUriStatsWithUniqueIp(start, end);
            } else {
                return repository.getSelectedUriStatsWithUniqueIp(start, end, uris);
            }
        } else {
            if (uris == null || uris.isEmpty()) {
                return repository.getAllUriStatsWithoutUniqueIp(start, end);
            } else {
                return repository.getSelectedUriStatsWithoutUniqueIp(start, end, uris);
            }
        }
    }
}
