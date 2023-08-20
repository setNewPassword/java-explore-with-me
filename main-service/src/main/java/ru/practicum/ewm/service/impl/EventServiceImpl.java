package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.event.*;
import ru.practicum.ewm.exception.*;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.mapper.LocationMapper;
import ru.practicum.ewm.model.*;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.LocationRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.EventService;
import ru.practicum.stats.client.StatsClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;
    private final LocationMapper locationMapper;
    private final UserRepository userRepository;
    private final StatsClient statsClient;
    private final LocationRepository locationRepository;
    private static final DateTimeFormatter DATE_FORMATTER = Formats.DATE_FORMATTER;
    private static final String APP_NAME = "ewm-main-service";

    @Override
    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotExistException(String
                        .format("Пользователь с id = %d не найден.", userId)));
        checkEventTime(newEventDto.getEventDate());
        Event eventToSave = eventMapper.toEventModel(newEventDto);
        eventToSave.setLocation(setIdToLocation(newEventDto.getLocation()));
        eventToSave.setState(EventState.PENDING);
        eventToSave.setConfirmedRequests(0);
        eventToSave.setCreatedOn(LocalDateTime.now());

        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new CategoryNotExistException(String
                        .format("Категория с id = %d не существует!", newEventDto.getCategory())));
        eventToSave.setCategory(category);
        eventToSave.setInitiator(user);
        Event saved = eventRepository.save(eventToSave);
        return eventMapper.toEventFullDto(saved);
    }

    private Location setIdToLocation(LocationDto locationDto) {
        Optional<Location> savedLocationOpt = locationRepository
                .findByLatAndLon(locationDto.getLat(), locationDto.getLat());
        return savedLocationOpt.orElseGet(() -> locationRepository.save(locationMapper.toLocation(locationDto)));
    }

    private void checkEventTime(LocalDateTime start) {
        if (start.isBefore(LocalDateTime.now())) {
            throw new EventValidationException("Нельзя опубликовать событие, которое уже началось!");
        }
    }

    @Override
    public List<EventShortDto> getEvents(Long userId, Pageable pageable) {
        List<EventShortDto> result = new ArrayList<>();
        List<Event> eventsFromDb = eventRepository.findAllByInitiatorId(userId, pageable).toList();
        for (Event event : eventsFromDb) {
            result.add(eventMapper.toEventShortDto(event));
        }
        return result;
    }

    @Transactional
    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventRequestDto updateEventAdminRequest) {

        Event eventToUpdate = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotExistException(String.format("Событие с id = %d не найдено.", eventId)));
        if (updateEventAdminRequest.getEventDate() != null) {
            checkEventTime(updateEventAdminRequest.getEventDate());
        }
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction() == StateAction.PUBLISH_EVENT) {
                if (eventToUpdate.getState().equals(EventState.PENDING)) {
                    eventToUpdate.setState(EventState.PUBLISHED);
                    eventToUpdate.setPublishedOn(LocalDateTime.now());
                } else {
                    throw new AlreadyPublishedException("Событие может быть опубликовано только в том случае, " +
                            "если оно ожидает публикации. " +
                            updateEventAdminRequest.getStateAction());
                }
            }
            if (updateEventAdminRequest.getStateAction() == StateAction.REJECT_EVENT) {
                if (eventToUpdate.getState().equals(EventState.PUBLISHED)) {
                    throw new AlreadyPublishedException("Событие может быть отклонено только в том случае, " +
                            "если оно еще не было опубликовано. " +
                            updateEventAdminRequest.getStateAction());
                }
                eventToUpdate.setState(EventState.CANCELED);
            }
        }
        updateEventEntity(updateEventAdminRequest, eventToUpdate);

        return eventMapper.toEventFullDto(eventRepository.save(eventToUpdate));
    }

    @Transactional
    @Override
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventRequestDto updateEventUserRequest) {

        Event eventFromDb = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotExistException(String.format("Событие с id = %d не найдено.", eventId)));
        if (eventFromDb.getState().equals(EventState.CANCELED) || eventFromDb.getState().equals(EventState.PENDING)) {
            if (updateEventUserRequest.getEventDate() != null
                    && updateEventUserRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new EventValidationException("Дата и время на которые намечено событие не может быть раньше, " +
                        "чем через два часа от текущего момента");
            }
            if (StateAction.SEND_TO_REVIEW == updateEventUserRequest.getStateAction()) {
                eventFromDb.setState(EventState.PENDING);
            }
            if (StateAction.CANCEL_REVIEW == updateEventUserRequest.getStateAction()) {
                eventFromDb.setState(EventState.CANCELED);
            }
        } else {
            throw new AlreadyPublishedException("Вы можете изменить отмененные события, ровно как и события, " +
                    "ожидающие модерации. Статус данного события = " + eventFromDb.getState());
        }

        updateEventEntity(updateEventUserRequest, eventFromDb);
        eventRepository.save(eventFromDb);
        return eventMapper.toEventFullDto(eventFromDb);
    }

    private void updateEventEntity(UpdateEventRequestDto event, Event eventToUpdate) {
        eventToUpdate.setAnnotation(Objects.requireNonNullElse(event.getAnnotation(), eventToUpdate.getAnnotation()));
        eventToUpdate.setCategory(event.getCategory() == null
                ? eventToUpdate.getCategory()
                : categoryRepository.findById(event.getCategory())
                .orElseThrow(() -> new CategoryNotExistException(String
                        .format("Не найдена категория с id = %d.", event.getCategory()))));
        eventToUpdate.setDescription(Objects.requireNonNullElse(event.getDescription(), eventToUpdate.getDescription()));
        eventToUpdate.setEventDate(Objects.requireNonNullElse(event.getEventDate(), eventToUpdate.getEventDate()));
        eventToUpdate.setLocation(event.getLocation() == null
                ? eventToUpdate.getLocation()
                : locationRepository.findByLatAndLon(event.getLocation().getLat(), event.getLocation().getLon())
                .orElse(new Location(null, event.getLocation().getLat(), event.getLocation().getLon())));
        eventToUpdate.setPaid(Objects.requireNonNullElse(event.getPaid(), eventToUpdate.getPaid()));
        eventToUpdate.setParticipantLimit(Objects
                .requireNonNullElse(event.getParticipantLimit(), eventToUpdate.getParticipantLimit()));
        eventToUpdate.setRequestModeration(Objects
                .requireNonNullElse(event.getRequestModeration(), eventToUpdate.getRequestModeration()));
        eventToUpdate.setTitle(Objects.requireNonNullElse(event.getTitle(), eventToUpdate.getTitle()));
    }

    @Override
    public EventFullDto getEventByUser(Long userId, Long eventId) {
        return eventMapper.toEventFullDto(eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new EventNotExistException(String
                        .format("Событие с id = %d не найдено.", eventId))));
    }

    @Override
    public List<EventFullDto> getAllEventsByAdmin(List<Long> userIds,
                                                  List<String> states,
                                                  List<Long> categories,
                                                  String rangeStart,
                                                  String rangeEnd,
                                                  Pageable pageable) {

        if (states == null & rangeStart == null & rangeEnd == null) {
            return eventRepository.findAll(pageable)
                    .stream()
                    .map(eventMapper::toEventFullDto)
                    .collect(Collectors.toList());
        }

        List<EventState> stateList = states
                .stream()
                .map(EventState::valueOf)
                .collect(Collectors.toList());

        LocalDateTime start;
        if (rangeStart != null && !rangeStart.isEmpty()) {
            start = LocalDateTime.parse(rangeStart, DATE_FORMATTER);
        } else {
            start = LocalDateTime.now().plusYears(5);
        }

        LocalDateTime end;
        if (rangeEnd != null && !rangeEnd.isEmpty()) {
            end = LocalDateTime.parse(rangeEnd, DATE_FORMATTER);
        } else {
            end = LocalDateTime.now().plusYears(5);
        }

        if (userIds.size() != 0 && states.size() != 0 && categories.size() != 0) {
            return findEventDtos(userIds,
                    categories,
                    pageable,
                    stateList,
                    start,
                    end);
        }
        if (userIds.size() == 0 && categories.size() != 0) {
            return findEventDtos(userIds,
                    categories,
                    pageable,
                    stateList,
                    start,
                    end);
        } else {
            return new ArrayList<>();
        }
    }

    private List<EventFullDto> findEventDtos(List<Long> userIds,
                                             List<Long> categories,
                                             Pageable pageable,
                                             List<EventState> stateList,
                                             LocalDateTime start,
                                             LocalDateTime end) {
        Page<Event> eventsWithPage = eventRepository
                .findAllWithAllParameters(userIds,
                        stateList,
                        categories,
                        start,
                        end,
                        pageable);
        Set<Long> eventIds = eventsWithPage
                .stream()
                .map(Event::getId)
                .collect(Collectors.toSet());
        Map<Long, Long> viewStatsMap = statsClient.getSetViews(eventIds);

        List<EventFullDto> events = eventsWithPage
                .stream()
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());
        events.forEach(eventFullDto ->
                eventFullDto.setViews(viewStatsMap.getOrDefault(eventFullDto.getId(), 0L)));
        return events;
    }

    @Override
    public List<EventShortDto> getAllEventsByPublic(String text,
                                                    List<Long> categories,
                                                    Boolean paid,
                                                    String rangeStart,
                                                    String rangeEnd,
                                                    Boolean onlyAvailable,
                                                    EventSortValue sort,
                                                    Pageable pageable,
                                                    String uri,
                                                    String ip) {

        LocalDateTime start = null;
        LocalDateTime end = null;

        if (rangeStart != null && rangeEnd != null) {
            start = LocalDateTime.parse(rangeStart, DATE_FORMATTER);
            end = LocalDateTime.parse(rangeEnd, DATE_FORMATTER);
            if (start.isAfter(end)) {
                throw new EventValidationException("Время начала события не может быть позже времени его окончания.");
            }
        } else {
            if (rangeStart == null && rangeEnd == null) {
                start = LocalDateTime.now();
                end = LocalDateTime.now().plusYears(10);
            } else {
                if (rangeStart == null) {
                    start = LocalDateTime.now();
                }
                if (rangeEnd == null) {
                    end = LocalDateTime.now();
                }
            }
        }
        final Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by("eventDate"));
        List<Event> eventEntities = eventRepository.searchPublishedEvents(
                        categories,
                        paid,
                        start,
                        end,
                        (text != null && !text.isEmpty() ? text.toLowerCase() : ""),
                        pageRequest)
                .getContent();
        statsClient.addHit(APP_NAME, uri, ip, LocalDateTime.now());

        if (eventEntities.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Long> eventIds = eventEntities
                .stream()
                .map(Event::getId)
                .collect(Collectors.toSet());
        Map<Long, Long> viewStatsMap = statsClient.getSetViews(eventIds);

        List<EventShortDto> events = eventEntities
                .stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
        events.forEach(eventShortDto ->
                eventShortDto.setViews(viewStatsMap.getOrDefault(eventShortDto.getId(), 0L)));

        if (EventSortValue.VIEWS.equals(sort)) {
            eventEntities.sort(Comparator.comparing(Event::getViews));
        }

        return events;

    }

    @Override
    public EventFullDto getEvent(Long eventId, String uri, String ip) {

        Event event = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> new EventNotExistException(String.format("Событие с id = %d не найдено.", eventId)));

        statsClient.addHit(APP_NAME, uri, ip, LocalDateTime.now());

        Long views = statsClient.getStatistics(eventId);

        EventFullDto eventDto = eventMapper.toEventFullDto(event);
        eventDto.setViews(views);

        return eventDto;
    }

}