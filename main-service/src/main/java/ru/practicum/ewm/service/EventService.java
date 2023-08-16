package ru.practicum.ewm.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.dto.event.*;
import ru.practicum.ewm.model.EventSortValue;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getEvents(Long userId, Pageable pageable);

    EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    EventFullDto getEventByUser(Long userId, Long eventId);

    List<EventFullDto> getAllEventsByAdmin(List<Long> userIds, List<String> states, List<Long> categories,
                                           String rangeStart, String rangeEnd, Pageable pageable,
                                           HttpServletRequest request);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventShortDto> getAllEventsByPublic(String text, List<Long> categoriesIds, Boolean paid, String rangeStart,
                                             String rangeEnd, Boolean onlyAvailable, EventSortValue sort, Pageable pageable,
                                             HttpServletRequest request);

    EventFullDto getEvent(Long id, HttpServletRequest request);
}