package practicum.hit;

import lombok.experimental.UtilityClass;
import ru.practicum.model.hit.dto.HitDto;

@UtilityClass
public class HitMapper {

    public Hit toHit(HitDto hitDto) {
        return new Hit(
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                hitDto.getTimestamp());
    }
}
