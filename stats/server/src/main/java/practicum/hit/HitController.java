package practicum.hit;

import practicum.HitDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hit")
@RequiredArgsConstructor
public class HitController {
    private final HitService service;
    private final HitMapper mapper;

    @PostMapping
    public HitDto create(@RequestBody @Validated HitDto hitDto) {
        Hit hit = mapper.toHit(hitDto);
        Hit createdHit = service.create(hit);
        return mapper.toHitDto(createdHit);
    }
}