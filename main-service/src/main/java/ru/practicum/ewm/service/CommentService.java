package ru.practicum.ewm.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.dto.comment.*;

import java.util.List;

public interface CommentService {
    List<CommentDto> getCommentsByAdmin(Pageable pageable);

    void deleteByAdmin(Long commentId);

    List<CommentDto> getCommentsByPrivate(Long userId, Long eventId, Pageable pageable);

    CommentDto createByPrivate(Long userId, Long eventId, NewCommentDto newCommentDto);

    CommentDto updateByPrivate(Long userId, Long commentId, NewCommentDto newCommentDto);

    void deleteByPrivate(Long userId, Long commentId);

    List<CommentDto> getCommentsByPublic(Long eventId, Pageable pageable);

    CommentDto getCommentByPublic(Long commentId);
}