package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.request.RequestDto;
import ru.practicum.ewm.dto.request.RequestStatusUpdateDto;
import ru.practicum.ewm.dto.request.RequestStatusUpdateResultDto;

import java.util.List;

public interface RequestService {

    List<RequestDto> getCurrentUserRequests(Long userId);

    RequestDto createRequest(Long userId, Long eventId);

    RequestDto cancelRequests(Long userId, Long requestId);

    List<RequestDto> getRequestsByOwnerOfEvent(Long userId, Long eventId);

    RequestStatusUpdateResultDto updateRequests(Long userId,
                                                Long eventId,
                                                RequestStatusUpdateDto requestStatusUpdateDto);

}