package practicum.stat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practicum.exception.ValidationException;
import practicum.hit.HitMapper;
import practicum.hit.Hit;
import ru.practicum.model.hit.dto.HitDto;
import ru.practicum.model.hit.dto.UriStatDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public void sendHit(HitDto hitDto) {
        Hit hit = HitMapper.toHit(hitDto);
        statsRepository.save(hit);
    }

    @Override
    public List<UriStatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<UriStatDto> result;
        if (end.isBefore(start)) {
            throw new ValidationException("дата конца не может быть раньше даты начала");
        }

        if (uris == null) {
            if (unique) {
                result = statsRepository.getUniqueStats(start, end);
            } else {
                result = statsRepository.getStats(start, end);
            }
        } else {
            if (unique) {
                result = statsRepository.getUniqueStatsByUris(start, end, uris);
            } else
                result = statsRepository.getStatsByUris(start, end, uris);
        }
        return result;
    }
}
