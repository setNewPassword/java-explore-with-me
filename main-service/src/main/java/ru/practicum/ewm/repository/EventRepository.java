package ru.practicum.ewm.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByInitiatorId(Long userId, Pageable page);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    Optional<Event> findByIdAndState(Long eventId, EventState eventStatus);

    List<Event> findAllByIdIn(List<Long> eventIds);

    Boolean existsByCategoryId(Long categoryId);

    @Query("SELECT e FROM Event e " +
            "JOIN e.category " +
            "WHERE ((:categoryIds) is null or e.category.id IN (:categoryIds)) " +
            "AND e.state = 'PUBLISHED' " +
            "AND (lower(e.annotation) like %:text% or lower(e.description) like %:text%) " +
            "AND ((:paid) is null or paid is (:paid)) " +
            "AND e.eventDate BETWEEN (:rangeStart) AND (:rangeEnd)")
    Page<Event> searchPublishedEvents(@Param("categoryIds") List<Long> categoryIds,
                                      @Param("paid") Boolean paid,
                                      @Param("rangeStart") LocalDateTime start,
                                      @Param("rangeEnd") LocalDateTime end,
                                      @Param("text") String text,
                                      Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "JOIN e.initiator " +
            "JOIN e.category " +
            "WHERE e.initiator.id IN :userIds " +
            "AND e.state IN :states " +
            "AND e.category.id IN :categories " +
            "AND e.eventDate BETWEEN :rangeStart AND :rangeEnd")
    Page<Event> findAllWithAllParameters(@Param("userIds") List<Long> userIds,
                                         @Param("states") List<EventState> states,
                                         @Param("categories") List<Long> categories,
                                         @Param("rangeStart") LocalDateTime rangeStart,
                                         @Param("rangeEnd") LocalDateTime rangeEnd, Pageable pageable);
}