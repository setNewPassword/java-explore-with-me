package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.Compilation;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query("SELECT c " +
            "FROM Compilation c ")
    List<Compilation> getCompilations(Pageable pageable);

    @Query("SELECT c " +
            "FROM Compilation c " +
            "WHERE c.pinned = :pinned ")
    List<Compilation> getCompilationsByPinned(Boolean pinned, Pageable pageable);
}