package ru.practicum.compilation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {
    @Query("SELECT c " +
            "FROM Compilation c " +
            "LEFT JOIN FETCH c.events e " +
            "WHERE (:pinnedParam IS NULL OR c.pinned = :pinnedParam)")
    List<Compilation> findByPinned(@Param("pinnedParam") Boolean pinned, Pageable pageable);
}
