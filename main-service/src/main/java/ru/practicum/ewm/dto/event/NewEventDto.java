package ru.practicum.ewm.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.dto.group.*;
import ru.practicum.ewm.model.Formats;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotNull(groups = NewGroup.class)
    @Size(min = 20, max = 2000, groups = {NewGroup.class, UpdateGroup.class})
    private String annotation;
    @NotNull
    private Long category;
    @NotNull(groups = NewGroup.class)
    @Size(min = 20, max = 7000, groups = {NewGroup.class, UpdateGroup.class})
    private String description;
    @NotNull(groups = NewGroup.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Formats.DATE)
    private LocalDateTime eventDate;
    @NotNull(groups = NewGroup.class)
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotNull(groups = NewGroup.class)
    @Size(min = 3, max = 120, groups = {NewGroup.class, UpdateGroup.class})
    private String title;
}