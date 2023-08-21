package ru.practicum.ewm.controller.admins;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.UpdateEventRequestDto;
import ru.practicum.ewm.dto.group.UpdateGroup;
import ru.practicum.ewm.service.EventService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/admin/events")
@Validated
public class EventAdminController {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventFullDto> getEvents(@RequestParam(name = "users", required = false) List<Long> users,
                                        @RequestParam(name = "states", required = false) List<String> states,
                                        @RequestParam(name = "categories", required = false) List<Long> categoriesId,
                                        @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                        @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                        @RequestParam(name = "from", required = false, defaultValue = "0")
                                        @PositiveOrZero Integer from,
                                        @RequestParam(name = "size", required = false, defaultValue = "10")
                                        @Positive Integer size) {
        return eventService.getAllEventsByAdmin(users, states, categoriesId, rangeStart, rangeEnd,
                PageRequest.of(from / size, size));
    }


    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto updateEvent(@NotNull @PathVariable(name = "eventId") Long eventId,
                                    @Validated(UpdateGroup.class)
                                    @RequestBody UpdateEventRequestDto updateEventAdminRequest) {
        return eventService.updateEventByAdmin(eventId, updateEventAdminRequest);
    }

}