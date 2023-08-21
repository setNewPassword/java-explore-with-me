package ru.practicum.ewm.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.model.Formats;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto extends EventShortDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Formats.DATE)
    private String createdOn;
    private String description;
    private LocationDto location;
    private Integer participantLimit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Formats.DATE)
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventState state;
}