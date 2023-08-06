package practicum.hit;

import practicum.HitDto;
import practicum.exception.ValidationException;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeParseException;

import org.modelmapper.ModelMapper;

@Service
public class HitMapper {
    private final ModelMapper modelMapper = new ModelMapper();

    public Hit toHit(HitDto hitDto) {
        try {
            return modelMapper.map(hitDto, Hit.class);
        } catch (DateTimeParseException e) {
            String timePattern = "yyyy-MM-dd HH:mm:ss";
            throw new ValidationException("Timestamp must be like: " + timePattern);
        }
    }

    public HitDto toHitDto(Hit hit) {
        return modelMapper.map(hit, HitDto.class);
    }
}
