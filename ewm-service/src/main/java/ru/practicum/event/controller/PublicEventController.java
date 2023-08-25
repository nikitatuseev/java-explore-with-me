package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.event.dto.EventFilterDto;
import ru.practicum.event.enums.SortBy;
import ru.practicum.model.hit.dto.HitDto;
import ru.practicum.client.StatsClient;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.EventService;
import ru.practicum.event.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {

    private final EventService eventService;
    private final StatsClient statsClient;

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Integer> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                         @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(required = false) SortBy sort,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size,
                                         HttpServletRequest httpServletRequest) {
        statsClient.postHit(new HitDto("exploreWithMe", httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr(), LocalDateTime.now()));

        return eventService.getEvents(EventFilterDto.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .sort(sort)
                .from(from)
                .size(size)
                .build());
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable Integer eventId, HttpServletRequest httpServletRequest) {
        String uri = httpServletRequest.getRequestURI();
        statsClient.postHit(new HitDto("exploreWithMe", uri, httpServletRequest.getRemoteAddr(), LocalDateTime.now()));
        return eventService.getEvent(eventId, uri);
    }

    @GetMapping("/{eventId}/comments")
    public List<CommentDto> getCommentsForEvent(@PathVariable Integer eventId) {
        return eventService.getCommentsForEvent(eventId);
    }
}
