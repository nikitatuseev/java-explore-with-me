package ru.practicum.event;

import ru.practicum.event.dto.*;
import ru.practicum.event.enums.State;
import ru.practicum.request.EventRequestStatusUpdateDto;
import ru.practicum.request.EventRequestStatusUpdateRequest;
import ru.practicum.request.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto createEvent(Integer userId, NewEventDto eventDto);

    EventFullDto updateEvent(Integer userId, Integer eventId, UpdateEventDto eventDto);

    List<EventShortDto> getUserEvents(Integer userId, Integer from, Integer size);

    EventFullDto getUserEvent(Integer userId, Integer eventId);

    List<ParticipationRequestDto> getEventRequestsForUser(Integer userId, Integer eventId);

    EventRequestStatusUpdateDto updateRequestStatus(Integer userId,
                                                    Integer eventId,
                                                    EventRequestStatusUpdateRequest statusUpdateDto);

    List<EventFullDto> getEventsByAdmin(List<Integer> users,
                                        List<State> states,
                                        List<Integer> categories,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        Integer from,
                                        Integer size);

    EventFullDto updateEventByAdmin(Integer eventId, UpdateEventDto updateEventAdminDto);

    EventFullDto getEvent(Integer eventId, String uri);

    List<EventShortDto> getEvents(EventFilterDto filterDto);
}
