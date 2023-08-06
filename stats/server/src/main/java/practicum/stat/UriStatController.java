package practicum.stat;

import practicum.UriStatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class UriStatController {
    private final UriStatService service;
    private final UriStatMapper mapper;

    @GetMapping
    public List<UriStatDto> getAll(@RequestParam(name = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                   @RequestParam(name = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                   @RequestParam(name = "uris", required = false) List<String> uris,
                                   @RequestParam(name = "unique", defaultValue = "false") Boolean isUnique) {
        return service.getAll(start, end, uris, isUnique).stream()
                .map(mapper::toUriStatDto)
                .collect(Collectors.toList());
    }
}