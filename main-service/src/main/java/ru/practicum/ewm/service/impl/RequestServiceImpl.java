package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.request.RequestDto;
import ru.practicum.ewm.dto.request.RequestStatusUpdateDto;
import ru.practicum.ewm.dto.request.RequestStatusUpdateResultDto;
import ru.practicum.ewm.exception.*;
import ru.practicum.ewm.mapper.RequestMapper;
import ru.practicum.ewm.model.*;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.RequestRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.RequestService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestMapper;
    private final UserRepository userRepository;

    @Override
    public List<RequestDto> getRequestsByOwnerOfEvent(Long userId, Long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format("Пользователь с id = %d не найден.", userId));
        }
        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        return requestMapper.toRequestDtoList(requestRepository.findAllByEventWithInitiator(userId, eventId));
    }

    @Override
    @Transactional
    public RequestDto createRequest(Long userId, Long eventId) {
        User requester = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String
                        .format("Пользователь с id = %d не найден.", userId)));
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(String
                        .format("Событие с id = %d не найдено.", eventId)));
        Request request = new Request(LocalDateTime.now(), event, requester, RequestStatus.PENDING);
        Optional<Request> requests = requestRepository.findByRequesterIdAndEventId(userId, eventId);
        if (requests.isPresent()) {
            throw new AlreadyExistsException(String
                    .format("Такой запрос уже существует: userId = %d, eventId = %d.", userId, eventId));
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new AlreadyExistsException(String
                    .format("Инициатор события не может подать запрос на участие в нем, userId = %d.", userId));
        }
        if (!(event.getState().equals(EventState.PUBLISHED))) {
            throw new AlreadyExistsException(
                    "Невозможно подать запрос на участие в событии, которе еще не опубликовано.");
        }
        int limit = event.getParticipantLimit();
        if (limit != 0) {
            if (limit == event.getConfirmedRequests()) {
                throw new AlreadyExistsException(String
                        .format("Достигнуто макимальное число участников — %d человек.", limit));
            }
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
        }

        if (!event.getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }
        Request savedRequest = requestRepository.save(request);
        return requestMapper.toRequestDto(savedRequest);
    }

    @Override
    @Transactional
    public RequestDto cancelRequests(Long userId, Long requestId) {
        Request request = requestRepository.findByRequesterIdAndId(userId, requestId)
                .orElseThrow(() -> new RequestNotFoundException(String
                        .format("Запрос с id = %d не найден.", requestId)));
        request.setStatus(RequestStatus.CANCELED);
        return requestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public List<RequestDto> getCurrentUserRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format("Пользователь с id = %d не найден.", userId));
        }
        return requestMapper.toRequestDtoList(requestRepository.findAllByRequesterId(userId));
    }

    @Override
    @Transactional
    public RequestStatusUpdateResultDto updateRequests(Long userId, Long eventId,
                                                       RequestStatusUpdateDto requestStatusUpdateDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(String.format("Событие с id = %d не найдено.", eventId)));
        RequestStatusUpdateResultDto result = new RequestStatusUpdateResultDto();

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            return result;
        }

        List<Request> requests = requestRepository.findAllByEventWithInitiator(userId, eventId);
        List<Request> requestsToUpdate = requests.stream().filter(x -> requestStatusUpdateDto.getRequestIds()
                .contains(x.getId())).collect(Collectors.toList());

        if (requestsToUpdate.stream().anyMatch(x -> x.getStatus().equals(RequestStatus.CONFIRMED) &&
                requestStatusUpdateDto.getStatus().equals(UpdateRequestStatus.REJECTED))) {
            throw new RequestAlreadyConfirmedException("Запрос уже подтвержден.");
        }

        if (event.getConfirmedRequests() + requestsToUpdate.size() > event.getParticipantLimit() &&
                requestStatusUpdateDto.getStatus().equals(UpdateRequestStatus.CONFIRMED)) {
            throw new ParticipantLimitException("Превышен лимит участников события.");
        }

        for (Request request : requestsToUpdate) {
            request.setStatus(RequestStatus.valueOf(requestStatusUpdateDto.getStatus().toString()));
        }

        requestRepository.saveAll(requestsToUpdate);

        if (requestStatusUpdateDto.getStatus().equals(UpdateRequestStatus.CONFIRMED)) {
            event.setConfirmedRequests(event.getConfirmedRequests() + requestsToUpdate.size());
        }

        eventRepository.save(event);

        if (requestStatusUpdateDto.getStatus().equals(UpdateRequestStatus.CONFIRMED)) {
            result.setConfirmedRequests(requestMapper.toRequestDtoList(requestsToUpdate));
        }

        if (requestStatusUpdateDto.getStatus().equals(UpdateRequestStatus.REJECTED)) {
            result.setRejectedRequests(requestMapper.toRequestDtoList(requestsToUpdate));
        }

        return result;
    }
}