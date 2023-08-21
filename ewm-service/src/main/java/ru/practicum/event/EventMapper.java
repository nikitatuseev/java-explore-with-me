package ru.practicum.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    List<EventShortDto> listEventsToListDto(List<Event> eventList);

    @Mapping(target = "category", ignore = true)
    Event newEventToDto(NewEventDto eventDto);

    EventFullDto eventToDto(Event event);

    List<EventFullDto> listEventToFullDto(List<Event> events);
}
