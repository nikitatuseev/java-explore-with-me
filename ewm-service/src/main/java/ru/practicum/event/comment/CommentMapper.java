package ru.practicum.event.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.event.comment.dto.CommentDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "event", source = "comment.event.title")
    @Mapping(target = "creator", source = "comment.creator.name")
    CommentDto commentToDto(Comment comment);

    @Mapping(target = "event", source = "comment.event.title")
    @Mapping(target = "creator", source = "comment.creator.name")
    List<CommentDto> listCommentsToListDto(List<Comment> commentList);
}
