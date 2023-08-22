package ru.practicum.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.event.enums.SortBy;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFilterDto {
    private String text = null;
    private List<Integer> categories = null;
    private Boolean paid = null;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeStart = null;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rangeEnd = null;
    private Boolean onlyAvailable = false;
    private SortBy sort = null;
    private Integer from = 0;
    private Integer size = 10;
}

