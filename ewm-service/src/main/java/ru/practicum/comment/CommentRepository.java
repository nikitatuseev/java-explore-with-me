package ru.practicum.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByEventId(Integer eventId);

    Optional<Comment> findByIdAndCreatorId(Integer id, Integer creatorId);

    List<Comment> findByEventIdOrderByCreatedOnDesc(Integer eventId);

    //используется при выводе списка событий с полем commentCount
    @Query("SELECT c.event.id, COUNT(c) FROM Comment c WHERE c.event.id IN :eventIds GROUP BY c.event.id")
    Map<Integer, Integer> countCommentsByEventIds(@Param("eventIds") List<Integer> eventIds);

    //используется при выводе одного события c полем commentCount
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.event.id = :eventId")
    Integer countCommentsByEventId(@Param("eventId") Integer eventId);
}
