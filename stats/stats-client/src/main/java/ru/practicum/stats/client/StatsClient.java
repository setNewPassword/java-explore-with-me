package ru.practicum.stats.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StatsClient extends BaseClient {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ObjectMapper mapper = new ObjectMapper();
    private final TypeReference<List<ViewStatsDto>> mapType = new TypeReference<>() {
    };

    @Autowired
    public StatsClient(@Value("http://stats-server:9090") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public ResponseEntity<Object> addHit(String appName, String uri, String ip, LocalDateTime timestamp) {
        EndpointHitDto endpointHit = EndpointHitDto.builder()
                .app(appName)
                .uri(uri)
                .ip(ip)
                .timestamp(timestamp.format(formatter))
                .build();
        return post("/hit", endpointHit);
    }

    public Long getStatistics(Long eventId) {
        Map<String, Object> parameters = Map.of(
                "start", LocalDateTime.now().minusYears(500).format(formatter),
                "end", LocalDateTime.now().plusYears(500).format(formatter),
                "uris", List.of("/events/" + eventId),
                "unique", true
        );
        ResponseEntity<Object> response = get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);

        List<ViewStatsDto> viewStatsList = response.hasBody()
                ? mapper.convertValue(response.getBody(), mapType)
                : Collections.emptyList();
        return viewStatsList != null && !viewStatsList.isEmpty()
                ? viewStatsList.get(0).getHits()
                : 0L;
    }

    public Map<Long, Long> getSetViews(Set<Long> eventIds) {
        Map<String, Object> parameters = Map.of(
                "start", LocalDateTime.now().minusYears(500).format(formatter),
                "end", LocalDateTime.now().plusYears(500).format(formatter),
                "uris", (eventIds.stream().map(id -> "/events/" + id).collect(Collectors.toList())),
                "unique", Boolean.FALSE
        );
        ResponseEntity<Object> response = get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);

        return response.hasBody()
                ? mapper
                .convertValue(response.getBody(), mapType)
                .stream()
                .collect(Collectors.toMap(this::getEventIdFromURI, ViewStatsDto::getHits))
                : Collections.emptyMap();
    }

    private Long getEventIdFromURI(ViewStatsDto e) {
        return Long.parseLong(e.getUri().substring(e.getUri().lastIndexOf("/") + 1));
    }

}