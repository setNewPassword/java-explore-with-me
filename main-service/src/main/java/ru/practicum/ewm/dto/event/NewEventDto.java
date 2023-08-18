package ru.practicum.ewm.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.dto.event.constraint.NewEventConstraint;
import ru.practicum.ewm.dto.event.constraint.UpdateEventConstraint;
import ru.practicum.ewm.model.Formats;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotNull(groups = NewEventConstraint.class)
    @Size(min = 20, max = 2000, groups = {NewEventConstraint.class, UpdateEventConstraint.class})
    private String annotation;
    @NotNull
    private Long category;
    @NotNull(groups = NewEventConstraint.class)
    @Size(min = 20, max = 7000, groups = {NewEventConstraint.class, UpdateEventConstraint.class})
    private String description;
    @NotNull(groups = NewEventConstraint.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Formats.DATE)
    private LocalDateTime eventDate;
    @NotNull(groups = NewEventConstraint.class)
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotNull(groups = NewEventConstraint.class)
    @Size(min = 3, max = 120, groups = {NewEventConstraint.class, UpdateEventConstraint.class})
    private String title;
}