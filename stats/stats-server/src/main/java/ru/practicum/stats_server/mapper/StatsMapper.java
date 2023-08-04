package ru.practicum.stats_server.mapper;

import ru.practicum.stats_dto.EndpointHit;
import ru.practicum.stats_server.model.Stats;

import java.time.LocalDateTime;

public class StatsMapper {
    public static Stats endpointToStats(EndpointHit endpointHit, LocalDateTime timestamp) {
        return new Stats(null, endpointHit.getApp(), endpointHit.getUri(), endpointHit.getIp(), timestamp);
    }
}