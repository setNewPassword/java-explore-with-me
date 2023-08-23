package ru.practicum.ewm.service.impl;

import ru.practicum.ewm.exception.CommentNotFoundException;
import ru.practicum.ewm.exception.EventNotPublishedException;
import ru.practicum.ewm.exception.UserIsNotAnAuthorException;
import ru.practicum.ewm.service.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.service.UserService;
import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.dto.comment.NewCommentDto;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.mapper.CommentMapper;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final UserService userService;
    private final EventService eventService;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public List<CommentDto> getCommentsByAdmin(Pageable pageable) {
        return toCommentsDto(commentRepository.findAll(pageable).toList());
    }

    @Override
    @Transactional
    public void deleteByAdmin(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getCommentsByPrivate(Long userId, Long eventId, Pageable pageable) {
        userService.getUserById(userId);

        List<Comment> comments;
        if (eventId != null) {
            eventService.getEventById(eventId);
            comments = commentRepository.findAllByAuthorIdAndEventId(userId, eventId);
        } else {
            comments = commentRepository.findAllByAuthorId(userId);
        }

        return toCommentsDto(comments);
    }

    @Override
    @Transactional
    public CommentDto createByPrivate(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User user = userService.getUserById(userId);
        Event event = eventService.getEventById(eventId);

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new EventNotPublishedException("Невозможно добавить комментарий к этому событию.");
        }

        Comment comment = Comment.builder()
                .text(newCommentDto.getText())
                .author(user)
                .event(event)
                .createdOn(LocalDateTime.now())
                .build();

        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDto updateByPrivate(Long userId, Long commentId, NewCommentDto newCommentDto) {
        userService.getUserById(userId);

        Comment commentFromRepository = getCommentById(commentId);

        checkUserIsAuthor(userId, commentFromRepository.getAuthor().getId());

        commentFromRepository.setText(newCommentDto.getText());
        commentFromRepository.setEditedOn(LocalDateTime.now());

        return commentMapper.toCommentDto(commentRepository.save(commentFromRepository));
    }

    @Override
    @Transactional
    public void deleteByPrivate(Long userId, Long commentId) {
        userService.getUserById(userId);

        checkUserIsAuthor(userId, getCommentById(commentId).getAuthor().getId());

        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getCommentsByPublic(Long eventId, Pageable pageable) {
        eventService.getEventById(eventId);

        return toCommentsDto(commentRepository.findAllByEventId(eventId, pageable));
    }

    @Override
    public CommentDto getCommentByPublic(Long commentId) {
        return commentMapper.toCommentDto(getCommentById(commentId));
    }

    private List<CommentDto> toCommentsDto(List<Comment> comments) {
        return comments.stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(String
                        .format("Комментарий с id = %d не найден.", commentId)));
    }

    private void checkUserIsAuthor(Long id, Long userId) {
        if (!Objects.equals(id, userId)) {
            throw new UserIsNotAnAuthorException("Пользователь не является автором этого комментария.");
        }
    }
}