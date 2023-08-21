package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.Request;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("SELECT p FROM Request p " +
            "JOIN FETCH p.event e " +
            "WHERE p.event.id = :eventId and e.initiator.id = :userId")
    List<Request> findAllByEventWithInitiator(@Param(value = "userId") Long userId,
                                              @Param("eventId") Long eventId);

    List<Request> findAllByRequesterId(Long userId);

    Optional<Request> findByRequesterIdAndId(Long userId, Long requestId);


    @Query("SELECT p FROM Request p " +
            "JOIN FETCH p.event e " +
            "WHERE p.requester.id =:requesterId " +
            "AND e.id = :eventId ")
    Optional<Request> findByRequesterIdAndEventId(@Param("requesterId") Long requesterId, @Param("eventId") Long eventId);
}