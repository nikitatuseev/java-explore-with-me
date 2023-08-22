package ru.practicum.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.enums.Status;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestStatusUpdateRequest {
    Status status;
    List<Integer> requestIds;
}
