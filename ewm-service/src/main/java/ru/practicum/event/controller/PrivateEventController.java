package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.dto.UpdateCommentDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.request.EventRequestStatusUpdateDto;
import ru.practicum.request.EventRequestStatusUpdateRequest;
import ru.practicum.request.ParticipationRequestDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getUserEvents(@PathVariable Integer userId,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {
        return eventService.getUserEvents(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createNewEvent(@PathVariable Integer userId,
                                       @RequestBody @Valid NewEventDto newEventDto) {
        return eventService.createEvent(userId, newEventDto);
    }


    @GetMapping("/{eventId}")
    public EventFullDto getUserEvent(@PathVariable Integer userId, @PathVariable Integer eventId) {
        return eventService.getUserEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Integer userId,
                                    @PathVariable Integer eventId,
                                    @RequestBody @Valid UpdateEventDto updateEventDto) {
        return eventService.updateEvent(userId, eventId, updateEventDto);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequestsForUser(@PathVariable Integer userId,
                                                                 @PathVariable Integer eventId) {
        return eventService.getEventRequestsForUser(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateDto updateRequestStatus(@PathVariable Integer userId,
                                                           @PathVariable Integer eventId,
                                                           @RequestBody EventRequestStatusUpdateRequest statusUpdateRequest) {
        return eventService.updateRequestStatus(userId, eventId, statusUpdateRequest);
    }

    @PostMapping("/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable Integer userId,
                                    @RequestBody @Valid NewCommentDto newCommentDto) {
        return eventService.createComment(userId, newCommentDto);
    }

    @PatchMapping("/comments")
    public CommentDto updateComment(@PathVariable Integer userId,
                                    @RequestBody @Valid UpdateCommentDto updateCommentDto) {
        return eventService.updateComment(userId, updateCommentDto);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Integer userId,
                              @PathVariable Integer commentId) {
        eventService.deleteComment(userId, commentId);
    }
}




