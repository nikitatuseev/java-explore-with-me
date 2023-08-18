package ru.practicum.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    @Query("select new ru.practicum.request.ParticipationRequestDto" +
            "(r.created, r.event.id, r.id, r.requester.id, r.status) " +
            "from Request as r " +
            "where r.event.initiator.id = ?1 and r.event.id = ?2 ")
    List<ParticipationRequestDto> findRequestsForUserInEvent(Integer userId, Integer eventId);

    List<Request> findByIdInAndEventInitiatorIdAndEventId(List<Integer> requestIds, Integer userId, Integer eventId);

    List<Request> findByRequesterId(Integer userId);

    boolean existsByRequesterIdAndEventId(Integer userId, Integer eventId);
}
