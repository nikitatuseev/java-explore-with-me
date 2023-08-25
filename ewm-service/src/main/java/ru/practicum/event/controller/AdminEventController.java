package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.EventService;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.enums.State;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEventsByAdmin(@RequestParam(required = false) List<Integer> users,
                                               @RequestParam(required = false) List<State> states,
                                               @RequestParam(required = false) List<Integer> categories,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @RequestParam(defaultValue = "10") Integer size) {
        return eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable Integer eventId,
                                           @RequestBody @Valid UpdateEventDto updateEventAdminDto) {
        return eventService.updateEventByAdmin(eventId, updateEventAdminDto);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByAdmin(@PathVariable Integer commentId) {
        eventService.deleteCommentByAdmin(commentId);
    }
}
