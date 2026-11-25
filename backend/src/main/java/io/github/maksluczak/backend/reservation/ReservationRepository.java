package io.github.maksluczak.backend.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query("""
        SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END 
        FROM Reservation r WHERE r.charger.id = :chargerId
        AND r.reservationStatus = :status
        AND r.startTime < :endTime
        AND r.endTime > :startTime
    """)
    boolean existsOverlappingReservation(
            @Param("chargerId") Integer chargerId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("status") ReservationStatus status
    );

    @Query("""
        SELECT r FROM Reservation r 
        WHERE r.charger.id = :chargerId
        AND r.reservationStatus = :status
        AND r.startTime < :to
        AND r.endTime > :from
        ORDER BY r.startTime
    """)
    List<Reservation> findInRange(
            @Param("chargerId") Integer chargerId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to,
            @Param("status") ReservationStatus status
    );

    List<Reservation> findAllByUserEmailOrderByStartTimeDesc(String email);

    Optional<Reservation> findByIdAndUserEmail(Integer id, String email);
}
