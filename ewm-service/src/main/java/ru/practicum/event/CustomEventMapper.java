package ru.practicum.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryRepository;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.enums.State;
import ru.practicum.event.location.Location;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;

@Component
public class CustomEventMapper {

    private final EventMapper eventMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public CustomEventMapper(EventMapper eventMapper, UserRepository userRepository, CategoryRepository categoryRepository, LocationRepository locationRepository) {
        this.eventMapper = eventMapper;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.locationRepository = locationRepository;
    }

    public Event createEventFromDtoAndUser(Event event, NewEventDto eventDto, Location location, Category category, User initiator) {
        event.setLocation(location);
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(initiator);
        event.setPaid(eventDto.getPaid() != null ? eventDto.getPaid() : false);
        event.setParticipantLimit(eventDto.getParticipantLimit() != null ? eventDto.getParticipantLimit() : 0);
        event.setRequestModeration(eventDto.getRequestModeration() != null ? eventDto.getRequestModeration() : true);
        event.setState(State.PENDING);
        event.setViews(0);
        event.setConfirmedRequests(0);

        return event;
    }
}
