package ru.practicum.event;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.event.location.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {
}
