package ru.practicum.ewm.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.model.StateAction;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventRequestDto extends NewEventDto {

    private StateAction stateAction;

}