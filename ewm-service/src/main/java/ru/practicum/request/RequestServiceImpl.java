package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.*;
import ru.practicum.event.enums.State;
import ru.practicum.event.enums.Status;
import ru.practicum.event.Event;
import ru.practicum.user.User;
import ru.practicum.event.EventRepository;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final RequestMapper requestMapper;
    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getUserRequests(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователя с ID %s не найдено", userId);
        }
        return requestMapper.listRequestsToListParticipationRequestDto(requestRepository.findByRequesterId(userId));
    }

    @Override
    @Transactional
    public ParticipationRequestDto createRequest(Integer userId, Integer eventId) {
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new RequestException("Вы уже участвуйте в событии с ID %s. Нельзя создать повторный запрос", eventId);
        }

        Request request = new Request();
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователя с ID %s не найдено", userId));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие с ID %s не найдено", eventId));

        if (event.getInitiator().getId().equals(userId)) {
            throw new RequestException("Вы не можете учавствовать в собственном событии. ID события - %s", eventId);
        }

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new InvalidEventStateException(State.PUBLISHED, event.getState());
        }

        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new RequestException("Доcтигнут лимит заявок на участие в количестве %s", event.getParticipantLimit());
        }

        if (event.getRequestModeration() && event.getParticipantLimit() != 0) {
            request.setStatus(Status.PENDING);
        } else {
            request.setStatus(Status.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }

        request.setEvent(event);
        request.setRequester(user);
        request.setCreated(LocalDateTime.now());

        return requestMapper.requestToParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Integer userId, Integer requestId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователя с ID %s не найдено", userId);
        }
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Запрос с ID %s не найден.", requestId));
        if (!request.getRequester().getId().equals(userId)) {
            throw new NotFoundException("Запрос с ID %s не найден.", requestId);
        }
        request.setStatus(Status.CANCELED);
        requestRepository.save(request);
        return requestMapper.requestToParticipationRequestDto(request);
    }
}
