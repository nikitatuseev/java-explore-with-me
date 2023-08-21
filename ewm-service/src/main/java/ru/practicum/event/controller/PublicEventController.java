package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.practicum.event.dto.EventFilterDto;
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
    public ResponseEntity<List<EventShortDto>> getEvents(@ModelAttribute EventFilterDto filterDto, HttpServletRequest httpServletRequest) {
        statsClient.postHit(new HitDto("exploreWithMe", httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr(), LocalDateTime.now()));
        List<EventShortDto> events = eventService.getEvents(filterDto);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable Integer eventId, HttpServletRequest httpServletRequest) {
        String uri = httpServletRequest.getRequestURI();
        statsClient.postHit(new HitDto("exploreWithMe", uri, httpServletRequest.getRemoteAddr(), LocalDateTime.now()));
        return ResponseEntity.ok().body(eventService.getEvent(eventId, uri));
    }
}
