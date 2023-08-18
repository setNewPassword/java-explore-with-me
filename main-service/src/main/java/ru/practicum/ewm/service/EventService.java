package ru.practicum.ewm.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventRequest;
import ru.practicum.ewm.model.EventSortValue;

import java.util.List;

public interface EventService {

    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    List<EventShortDto> getEvents(Long userId, Pageable pageable);

    EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventRequest updateEventUserRequest);

    EventFullDto getEventByUser(Long userId, Long eventId);

    List<EventFullDto> getAllEventsByAdmin(List<Long> userIds,
                                           List<String> states,
                                           List<Long> categories,
                                           String rangeStart,
                                           String rangeEnd,
                                           Pageable pageable);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventRequest updateEventAdminRequest);

    List<EventShortDto> getAllEventsByPublic(String text,
                                             List<Long> categoriesIds,
                                             Boolean paid,
                                             String rangeStart,
                                             String rangeEnd,
                                             Boolean onlyAvailable,
                                             EventSortValue sort,
                                             Pageable pageable,
                                             String uri,
                                             String ip);

    EventFullDto getEvent(Long id, String uri, String ip);
}