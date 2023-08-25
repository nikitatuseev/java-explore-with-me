package ru.practicum.comment.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    Integer id;
    String text;
    String event;
    String creator;
    LocalDateTime createdOn;
    LocalDateTime lastEditedOn;
}
