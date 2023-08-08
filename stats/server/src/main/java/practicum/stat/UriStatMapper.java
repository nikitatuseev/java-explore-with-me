package practicum.stat;

import practicum.UriStatDto;
import org.springframework.stereotype.Service;

@Service
public class UriStatMapper {
    public UriStatDto toUriStatDto(UriStat uriStat) {
        UriStatDto uriStatDto = new UriStatDto();
        uriStatDto.setHits(uriStat.getHits());
        uriStatDto.setApp(uriStat.getApp());
        uriStatDto.setUri(uriStat.getUri());
        return uriStatDto;
    }
}