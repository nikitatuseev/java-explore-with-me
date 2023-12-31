package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatsClient;
import ru.practicum.comment.Comment;
import ru.practicum.comment.CommentMapper;
import ru.practicum.comment.CommentRepository;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.dto.UpdateCommentDto;
import ru.practicum.event.dto.*;
import ru.practicum.event.enums.SortBy;
import ru.practicum.model.hit.dto.UriStatDto;
import ru.practicum.category.CategoryRepository;
import ru.practicum.exception.*;
import ru.practicum.request.RequestMapper;
import ru.practicum.category.Category;
import ru.practicum.event.enums.State;
import ru.practicum.event.enums.Status;
import ru.practicum.event.location.Location;
import ru.practicum.request.*;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private static final Integer HOURS_BEFORE_EVENT = 2;
    private final EventMapper eventMapper;
    private final RequestMapper requestMapper;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final StatsClient statsClient;
    private final CustomEventMapper customEventMapper;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional
    public EventFullDto createEvent(Integer userId, NewEventDto eventDto) {
        User initiator = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователя с ID %s не найдено", userId));
        Category category = categoryRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Категория с ID %s не найдено", eventDto.getCategory()));
        if (!validateDateTime(eventDto.getEventDate())) {
            throw new EventException("Событие не может быть раньше, чем через %s часа от текущего момента.", HOURS_BEFORE_EVENT);
        }
        Location location = locationRepository.save(eventDto.getLocation());
        Event event = eventMapper.newEventToDto(eventDto);
        event = customEventMapper.createEventFromDtoAndUser(event, eventDto, location, category, initiator);
        event = eventRepository.save(event);
        return eventMapper.eventToDto(event);
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Integer userId, Integer eventId, UpdateEventDto eventDto) {

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователя с ID %s не найдено", userId);
        }
        Event eventToUpdate = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие с ID %s не найдено", eventId));
        if (!eventToUpdate.getState().equals(State.PENDING) && !eventToUpdate.getState().equals(State.CANCELED)) {
            throw new InvalidEventStateException(eventToUpdate.getState());
        }
        if (eventDto.getEventDate() != null) {
            if (!validateDateTime(eventDto.getEventDate())) {
                throw new EventException("Событие не может быть раньше, чем через %s часа от текущего момента.", HOURS_BEFORE_EVENT);
            }
        }
        Category categoryToUpdate = eventToUpdate.getCategory();
        if (eventDto.getCategory() != null) {
            categoryToUpdate = categoryRepository
                    .findById(eventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория с ID %s не найдено", eventDto.getCategory()));
        }
        Event event = updateEventFields(eventToUpdate, eventDto, categoryToUpdate);
        event = eventRepository.save(event);

        EventFullDto eventFullDto = eventMapper.eventToDto(event);
        List<CommentDto> eventComments = commentMapper.listCommentsToListDto(commentRepository.findByEventId(eventId));
        eventFullDto.setComments(eventComments);

        return eventFullDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getUserEvents(Integer userId, Integer from, Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователя с ID %s не найдено", userId);
        }
        int page = from > 0 ? from / size : 0;
        Pageable eventsPageable = PageRequest.of(page, size);
        List<Event> events = eventRepository.findByInitiatorId(userId, eventsPageable);
        return eventMapper.listEventsToListDto(events);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getUserEvent(Integer userId, Integer eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователя с ID %s не найдено", userId);
        }
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId).orElseThrow(() -> new NotFoundException("Событие с ID %s не найдено", eventId));
        EventFullDto eventFullDto = eventMapper.eventToDto(event);
        eventFullDto.setComments(commentMapper.listCommentsToListDto(commentRepository.findByEventId(eventId)));
        return eventFullDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getEventRequestsForUser(Integer userId, Integer eventId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователя с ID %s не найдено", userId);
        }
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Событие с ID %s не найдено", eventId);
        }
        return requestRepository.findRequestsForUserInEvent(userId, eventId);
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateDto updateRequestStatus(Integer userId,
                                                           Integer eventId,
                                                           EventRequestStatusUpdateRequest statusUpdateDto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователя с ID %s не найдено", userId);
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие с ID %s не найдено", eventId));
        List<Request> requests = requestRepository.findByIdInAndEventInitiatorIdAndEventId(statusUpdateDto.getRequestIds(), userId, eventId);
        for (Request request : requests) {
            if (!(request.getStatus().equals(Status.PENDING))) {
                throw new InvalidRequestStateException("Запрос должно иметь статус PENDING. Текущее состояние - %s.", request.getStatus());
            }
        }
        if (statusUpdateDto.getStatus().equals(Status.CONFIRMED)
                && event.getParticipantLimit() != 0
                && event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new RequestException("доcтигнут лимит заявок на участие в количестве %s", event.getParticipantLimit());
        }
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        for (Request request : requests) {
            if (statusUpdateDto.getStatus().equals(Status.REJECTED)) {
                request.setStatus(Status.REJECTED);
                requestRepository.save(request);
                rejectedRequests.add(requestMapper.requestToParticipationRequestDto(request));
            }
            if (statusUpdateDto.getStatus().equals(Status.CONFIRMED)) {
                if (!(event.getParticipantLimit() != 0 && event.getConfirmedRequests().equals(event.getParticipantLimit()))) {
                    request.setStatus(Status.CONFIRMED);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    eventRepository.save(event);
                    requestRepository.save(request);
                    confirmedRequests.add(requestMapper.requestToParticipationRequestDto(request));
                } else {
                    request.setStatus(Status.REJECTED);
                    requestRepository.save(request);
                    rejectedRequests.add(requestMapper.requestToParticipationRequestDto(request));
                }
            }
        }
        return new EventRequestStatusUpdateDto(confirmedRequests, rejectedRequests);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getEventsByAdmin(List<Integer> users,
                                               List<State> states,
                                               List<Integer> categories,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd,
                                               Integer from,
                                               Integer size) {
        List<EventFullDto> result;
        int page = from > 0 ? from / size : 0;
        Pageable eventsPageable = PageRequest.of(page, size);
        if (rangeStart == null || rangeEnd == null) {
            result = eventMapper.listEventToFullDto(eventRepository.findEventsByUsersStatesCategoriesPageable(users, states, categories, eventsPageable));
        } else {
            result = eventMapper.listEventToFullDto(eventRepository.findEventsByUsersStatesCategoriesDatePageable(users, states, categories, rangeStart, rangeEnd, eventsPageable));
        }
        return result;
    }

    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(Integer eventId, UpdateEventDto updateEventAdminDto) {
        Event eventToUpdate = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие с ID %s не найдено", eventId));
        if (eventToUpdate.getEventDate() != null && eventToUpdate.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new EventException("Событие не может быть раньше, чем через %s часа от текущего момента.", 1);
        }
        if (updateEventAdminDto.getStateAction() != null) {
            if (updateEventAdminDto.getStateAction().equals(State.PUBLISH_EVENT)) {
                if (!eventToUpdate.getState().equals(State.PENDING)) {
                    throw new InvalidEventStateException(eventToUpdate.getState());
                }
                eventToUpdate.setPublishedOn(LocalDateTime.now());
                eventToUpdate.setState(State.PUBLISHED);
            }
            if (updateEventAdminDto.getStateAction().equals(State.REJECT_EVENT)) {
                if (eventToUpdate.getState().equals(State.PUBLISHED)) {
                    throw new InvalidEventStateException(eventToUpdate.getState());
                }
                eventToUpdate.setState(State.CANCELED);
            }
        }
        Category categoryToUpdate = null;
        if (updateEventAdminDto.getCategory() != null) {
            categoryToUpdate = categoryRepository.findById(updateEventAdminDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория с ID %s не найдено", updateEventAdminDto.getCategory()));
        }
        updateEventFields(eventToUpdate, updateEventAdminDto, categoryToUpdate);
        return eventMapper.eventToDto(eventRepository.save(eventToUpdate));
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEvent(Integer eventId, String uri) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие с ID %s не найдено", eventId));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException("Событие с ID %s не найдено", eventId);
        }

        List<UriStatDto> statsDtos = statsClient.getStats(event.getCreatedOn(), LocalDateTime.now(), new String[]{uri}, true);

        EventFullDto eventFullDto = eventMapper.eventToDto(event);
        eventFullDto.setComments(commentMapper.listCommentsToListDto(commentRepository.findByEventId(eventId)));

        if (!statsDtos.isEmpty()) {
            eventFullDto.setViews(statsDtos.get(0).getHits().intValue());
        } else {
            eventFullDto.setViews(0);
        }

        return eventFullDto;
    }

    @Override
    @Transactional
    public List<EventShortDto> getEvents(EventFilterDto filterDto) {
        validateDateRange(filterDto.getRangeStart(), filterDto.getRangeEnd());

        int page = Math.max(0, filterDto.getFrom() / filterDto.getSize());
        Pageable eventsPageable = PageRequest.of(page, filterDto.getSize());

        List<Event> result;

        if (filterDto.getOnlyAvailable() != null && filterDto.getOnlyAvailable()) {
            if (filterDto.getRangeStart() == null || filterDto.getRangeEnd() == null) {
                result = eventRepository.findAvailablePublishedEvents(filterDto.getText(), filterDto.getCategories(), filterDto.getPaid(), eventsPageable);
            } else {
                result = eventRepository.findAvailablePublishedWithDateEvents(filterDto.getText(), filterDto.getCategories(), filterDto.getPaid(), filterDto.getRangeStart(), filterDto.getRangeEnd(), eventsPageable);
            }
        } else {
            if (filterDto.getRangeStart() == null || filterDto.getRangeEnd() == null) {
                result = eventRepository.findPublishedEvents(filterDto.getText(), filterDto.getCategories(), filterDto.getPaid(), eventsPageable);
            } else {
                result = eventRepository.findPublishedWithDateEvents(filterDto.getText(), filterDto.getCategories(), filterDto.getPaid(), filterDto.getRangeStart(), filterDto.getRangeEnd(), eventsPageable);
            }
        }

        List<Integer> eventIds = result.stream().map(Event::getId).collect(Collectors.toList());

        Map<Integer, Integer> commentCountMap = commentRepository.countCommentsByEventIds(eventIds);

        List<EventShortDto> eventShortDtos = new ArrayList<>();

        for (Event event : result) {
            Integer commentCount = commentCountMap.getOrDefault(event.getId(), 0);
            EventShortDto eventShortDto = eventMapper.eventToShortDto(event);
            eventShortDto.setCommentCount(commentCount);

            // Учет просмотров события
            List<UriStatDto> statsDtos = statsClient.getStats(event.getCreatedOn(), LocalDateTime.now(), new String[]{"event-" + event.getId()}, true);
            if (!statsDtos.isEmpty()) {
                eventShortDto.setViews(statsDtos.get(0).getHits().intValue());
            } else {
                eventShortDto.setViews(0);
            }

            eventShortDtos.add(eventShortDto);
        }
        // пришлось изменить способ сортировки так как возникала ошибка при сортировке по просмотрам
        if (filterDto.getSort() == SortBy.EVENT_DATE) {
            eventShortDtos.sort(Comparator.comparing(EventShortDto::getEventDate));
        } else {
            eventShortDtos.sort(Comparator.comparing(EventShortDto::getViews).reversed());
        }
        return eventShortDtos;
    }

    @Override
    @Transactional
    public CommentDto createComment(Integer userId, NewCommentDto newCommentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден", userId));

        Event event = eventRepository.findByIdAndState(newCommentDto.getEventId(), State.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Опубликованное событие не найдено", newCommentDto.getEventId()));

        Comment comment = new Comment();
        comment.setText(newCommentDto.getText());
        comment.setCreator(user);
        comment.setEvent(event);
        comment.setCreatedOn(LocalDateTime.now());
        comment = commentRepository.save(comment);

        return commentMapper.commentToDto(comment);
    }

    @Override
    @Transactional
    public CommentDto updateComment(Integer userId, UpdateCommentDto updateCommentDto) {
        Comment comment = commentRepository.findByIdAndCreatorId(updateCommentDto.getCommentId(), userId)
                .orElseThrow(() -> new NotFoundException("Comment не найден или вы не автор данного комментария", updateCommentDto.getCommentId()));

        comment.setText(updateCommentDto.getText());
        comment.setLastEditedOn(LocalDateTime.now());

        return commentMapper.commentToDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteComment(Integer userId, Integer commentId) {
        commentRepository.findByIdAndCreatorId(commentId, userId)
                .orElseThrow(() -> new CommentException("Comment не найден или вы не автор данного комментария"));
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public void deleteCommentByAdmin(Integer commentId) {
        commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException("Comment не найден или вы не автор данного комментария"));
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public List<CommentDto> getCommentsForEvent(Integer eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event не найден", eventId);
        }

        List<Comment> comments = commentRepository.findByEventIdOrderByCreatedOnDesc(eventId);
        return commentMapper.listCommentsToListDto(comments);
    }

    private void validateDateRange(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null && rangeEnd.isBefore(rangeStart)) {
            throw new BadRequestException("Дата окончания не может быть раньше даты начала");
        }
    }

    private Boolean validateDateTime(LocalDateTime dateTime) {
        return dateTime.isAfter(LocalDateTime.now().plusHours(HOURS_BEFORE_EVENT));
    }

    private Event updateEventFields(Event eventToUpdate, UpdateEventDto eventDto, Category category) {
        eventToUpdate.setAnnotation(eventDto.getAnnotation() == null ? eventToUpdate.getAnnotation() : eventDto.getAnnotation());
        eventToUpdate.setCategory(eventDto.getCategory() == null ? eventToUpdate.getCategory() : category);
        eventToUpdate.setDescription(eventDto.getDescription() == null ? eventToUpdate.getDescription() : eventDto.getDescription());
        eventToUpdate.setEventDate(eventDto.getEventDate() == null ? eventToUpdate.getEventDate() : eventDto.getEventDate());
        eventToUpdate.setLocation(eventDto.getLocation() == null ? eventToUpdate.getLocation() : eventDto.getLocation());
        eventToUpdate.setPaid(eventDto.getPaid() == null ? eventToUpdate.getPaid() : eventDto.getPaid());
        eventToUpdate.setParticipantLimit(eventDto.getParticipantLimit() == null ? eventToUpdate.getParticipantLimit() : eventDto.getParticipantLimit());
        eventToUpdate.setRequestModeration(eventDto.getRequestModeration() == null ? eventToUpdate.getRequestModeration() : eventDto.getRequestModeration());
        if (!(eventDto.getStateAction() == null)) {
            if (eventDto.getStateAction().equals(State.PUBLISH_EVENT)) {
                eventToUpdate.setState(State.PUBLISHED);
            }
            if (eventDto.getStateAction().equals(State.REJECT_EVENT) || eventDto.getStateAction().equals(State.CANCEL_REVIEW)) {
                eventToUpdate.setState(State.CANCELED);
            }
            if (eventDto.getStateAction().equals(State.SEND_TO_REVIEW)) {
                eventToUpdate.setState(State.PENDING);
            }
        }
        eventToUpdate.setTitle(eventDto.getTitle() == null ? eventToUpdate.getTitle() : eventDto.getTitle());
        return eventToUpdate;
    }
}