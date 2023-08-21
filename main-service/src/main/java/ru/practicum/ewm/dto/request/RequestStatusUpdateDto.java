package ru.practicum.ewm.dto.request;

import lombok.*;
import ru.practicum.ewm.model.UpdateRequestStatus;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestStatusUpdateDto {
    private List<Long> requestIds;
    private UpdateRequestStatus status;
}