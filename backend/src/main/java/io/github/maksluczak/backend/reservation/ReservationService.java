package io.github.maksluczak.backend.reservation;

import io.github.maksluczak.backend.charger.Charger;
import io.github.maksluczak.backend.charger.ChargerRepository;
import io.github.maksluczak.backend.exception.*;
import io.github.maksluczak.backend.reservation.dto.ReservationRequest;
import io.github.maksluczak.backend.reservation.dto.ReservationResponse;
import io.github.maksluczak.backend.user.User;
import io.github.maksluczak.backend.user.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    public static final int MIN_DURATION_HOURS = 1;
    public static final int MAX_DURATION_HOURS = 6;

    private final UserRepository userRepository;
    private final ChargerRepository chargerRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(
            UserRepository userRepository,
            ChargerRepository chargerRepository,
            ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.chargerRepository = chargerRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public ReservationResponse createNewChargerReservation(
            ReservationRequest request,
            String email
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        Charger charger = chargerRepository.findById(request.getChargerId())
                .orElseThrow(() -> new ChargerNotFoundException(request.getChargerId()));

        int hours = request.getDurationHours();
        if (hours < MIN_DURATION_HOURS || hours > MAX_DURATION_HOURS) {
            throw new InvalidReservationTimeException("Reservation must last between 1 and 6 hours");
        }

        LocalDateTime startTime = request.getStartTime();
        LocalDateTime endTime = request.getStartTime().plusHours(hours);

        if (startTime.isBefore(LocalDateTime.now())) {
            throw new InvalidReservationTimeException("Reservation can not start in the past");
        }

        if (!charger.isOpenDuring(startTime, endTime)) {
            throw new InvalidReservationTimeException("Reservation hours exceed charger working hours");
        }

        boolean overlaps = reservationRepository
                .existsOverlappingReservation(
                        charger.getId(), startTime, endTime, ReservationStatus.ACTIVE
                );
        if (overlaps) {
            throw new ChargerNotAvailableException(charger.getId(), startTime, endTime);
        }

        Reservation reservation = Reservation.builder()
                .startTime(startTime)
                .endTime(endTime)
                .reservationStatus(ReservationStatus.ACTIVE)
                .user(user)
                .charger(charger)
                .build();

        return toResponse(reservationRepository.save(reservation));
    }

    public List<ReservationResponse> getUserReservationsByEmail(String email) {
        return reservationRepository.findAllByUserEmailOrderByStartTimeDesc(email).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void cancelUserReservation(Integer id, String email) {
        Reservation reservation = reservationRepository.findByIdAndUserEmail(id, email)
                .orElseThrow(() -> new ReservationNotFoundException(id));

        if (reservation.getReservationStatus() != ReservationStatus.ACTIVE) {
            throw new ReservationCannotBeCancelledException("Reservation is not active");
        }

        if (!reservation.getStartTime().isAfter(LocalDateTime.now())) {
            throw new ReservationCannotBeCancelledException(
                    "You can not cancel reservation that has already started or finished"
            );
        }

        reservation.setReservationStatus(ReservationStatus.CANCELED);
        reservationRepository.save(reservation);
    }

    private ReservationResponse toResponse(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .chargerId(reservation.getCharger().getId())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .reservationStatus(reservation.getReservationStatus())
                .build();
    }
}
