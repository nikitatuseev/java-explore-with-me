package ru.practicum.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByEventId(Integer eventId);
    Optional<Comment> findByIdAndCreatorId(Integer id, Integer creatorId);
    List<Comment> findByEventIdOrderByCreatedOnDesc(Integer eventId);
}
