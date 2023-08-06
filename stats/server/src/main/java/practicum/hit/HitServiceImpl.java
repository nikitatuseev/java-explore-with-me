package practicum.hit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HitServiceImpl implements HitService {
    private final HitRepository repository;

    @Transactional
    public Hit create(Hit hit) {
        Hit savedHit = repository.save(hit);
        log.info("Hit сохранен: {}", savedHit);
        return savedHit;
    }
}