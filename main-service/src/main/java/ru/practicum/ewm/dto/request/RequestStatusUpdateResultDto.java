package ru.practicum.ewm.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class RequestStatusUpdateResultDto {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}