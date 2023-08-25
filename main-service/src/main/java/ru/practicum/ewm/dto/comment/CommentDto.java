package ru.practicum.ewm.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;
import ru.practicum.ewm.dto.user.UserShortDto;
import ru.practicum.ewm.model.Formats;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private String text;
    private UserShortDto author;
    private Long eventId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Formats.DATE)
    private LocalDateTime createdOn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Formats.DATE)
    private LocalDateTime editedOn;
}