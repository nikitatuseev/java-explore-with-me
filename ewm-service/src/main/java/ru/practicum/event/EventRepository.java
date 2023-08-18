package ru.practicum.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.event.enums.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    List<Event> findByInitiatorId(Integer userId, Pageable pageable);

    Optional<Event> findByInitiatorIdAndId(Integer userId, Integer eventId);

    @Query("select e " +
            "from Event as e " +
            "join fetch e.initiator as i " +
            "join fetch e.location as l " +
            "join fetch e.category as c " +
            "where (:users is null or e.initiator.id in :users) " +
            "and (:states is null or e.state in :states) " +
            "and (:categories is null or c.id in :categories) " +
            "and (e.eventDate between :rangeStart and :rangeEnd)")
    List<Event> findEventsByUsersStatesCategoriesDatePageable(@Param("users") List<Integer> users,
                                                              @Param("states") List<State> states,
                                                              @Param("categories") List<Integer> categories,
                                                              @Param("rangeStart") LocalDateTime rangeStart,
                                                              @Param("rangeEnd") LocalDateTime rangeEnd,
                                                              Pageable pageable);

    @Query("select e " +
            "from Event as e " +
            "join fetch e.initiator as i " +
            "join fetch e.location as l " +
            "join fetch e.category as c " +
            "where (:users is null or e.initiator.id in :users) " +
            "and (:states is null or e.state in :states) " +
            "and (:categories is null or c.id in :categories) " +
            "and (e.eventDate >= current_timestamp)")
    List<Event> findEventsByUsersStatesCategoriesPageable(@Param("users") List<Integer> users,
                                                          @Param("states") List<State> states,
                                                          @Param("categories")List<Integer> categories,
                                                          Pageable pageable);

    @Query("select e " +
            "from Event as e " +
            "join fetch e.category as c " +
            "join fetch e.initiator as i " +
            "where (:text is null or (lower(e.annotation) like lower(concat('%', :text, '%')) " +
            "or lower(e.description) like lower(concat('%', :text, '%')))) " +
            "and (:categories is null or c.id in :categories) " +
            "and (:paid is null or e.paid = :paid) " +
            "and (e.eventDate >= current_timestamp) " +
            "and (e.participantLimit = 0 or e.participantLimit <= e.confirmedRequests) " +
            "and (e.state like 'PUBLISHED') ")
    List<Event> findAvailablePublishedEvents(@Param("text") String text,
                                             @Param("categories") List<Integer> categories,
                                             @Param("paid") Boolean paid,
                                             Pageable pageable);

    @Query("select e " +
            "from Event as e " +
            "join fetch e.category as c " +
            "join fetch e.initiator as i " +
            "where (:text is null or (lower(e.annotation) like lower(concat('%', :text, '%')) " +
            "or lower(e.description) like lower(concat('%', :text, '%')))) " +
            "and (:categories is null or c.id in :categories) " +
            "and (:paid is null or e.paid = :paid) " +
            "and (e.eventDate between :rangeStart and :rangeEnd) " +
            "and (e.participantLimit = 0 or e.participantLimit <= e.confirmedRequests) " +
            "and (e.state like 'PUBLISHED') ")
    List<Event> findAvailablePublishedWithDateEvents(@Param("text") String text,
                                                     @Param("categories") List<Integer> categories,
                                                     @Param("paid") Boolean paid,
                                                     @Param("rangeStart") LocalDateTime rangeStart,
                                                     @Param("rangeEnd") LocalDateTime rangeEnd,
                                                     Pageable pageable);

    @Query("select e " +
            "from Event as e " +
            "join fetch e.category as c " +
            "join fetch e.initiator as i " +
            "where (:text is null or (lower(e.annotation) like lower(concat('%', :text, '%')) " +
            "or lower(e.description) like lower(concat('%', :text, '%')))) " +
            "and (:categories is null or c.id in :categories) " +
            "and (:paid is null or e.paid = :paid) " +
            "and (e.eventDate >= current_timestamp) " +
            "and (e.state like 'PUBLISHED') ")
    List<Event> findPublishedEvents(@Param("text") String text,
                                    @Param("categories") List<Integer> categories,
                                    @Param("paid") Boolean paid,
                                    Pageable pageable);

    @Query("select e " +
            "from Event as e " +
            "join fetch e.category as c " +
            "join fetch e.initiator as i " +
            "where (:text is null or (lower(e.annotation) like lower(concat('%', :text, '%')) " +
            "or lower(e.description) like lower(concat('%', :text, '%')))) " +
            "and (:categories is null or c.id in :categories) " +
            "and (:paid is null or e.paid = :paid) " +
            "and (e.eventDate between :rangeStart and :rangeEnd) " +
            "and (e.state like 'PUBLISHED') ")
    List<Event> findPublishedWithDateEvents(@Param("text") String text,
                                            @Param("categories") List<Integer> categories,
                                            @Param("paid") Boolean paid,
                                            @Param("rangeStart") LocalDateTime rangeStart,
                                            @Param("rangeEnd") LocalDateTime rangeEnd,
                                            Pageable pageable);
}
