package ru.practicum.stats.server.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.server.model.Stats;

import java.time.LocalDateTime;

@UtilityClass
public class StatsMapper {
    public static Stats endpointToStats(EndpointHitDto endpointHitDto, LocalDateTime timestamp) {
        return new Stats(null, endpointHitDto.getApp(), endpointHitDto.getUri(), endpointHitDto.getIp(), timestamp);
    }
}