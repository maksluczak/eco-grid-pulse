package io.github.maksluczak.backend.reservation;

import io.github.maksluczak.backend.availability.AvailabilityService;
import io.github.maksluczak.backend.charger.Charger;
import io.github.maksluczak.backend.charger.ChargerRepository;
import io.github.maksluczak.backend.reservation.dto.ReservationRequest;
import io.github.maksluczak.backend.reservation.dto.ReservationResponse;
import io.github.maksluczak.backend.user.User;
import io.github.maksluczak.backend.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final UserRepository userRepository;
    private final AvailabilityService availabilityService;
    private final ChargerRepository chargerRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(UserRepository userRepository, AvailabilityService availabilityService, ChargerRepository chargerRepository, ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.availabilityService = availabilityService;
        this.chargerRepository = chargerRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public ReservationResponse createNewChargerReservation(
            ReservationRequest reservationRequest,
            String email
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow();

        Charger charger = chargerRepository.findById(reservationRequest.getChargerId())
                .orElseThrow();

        LocalDateTime now = LocalDateTime.now();
        if (reservationRequest.getStartTime().isBefore(now)) {
            // TODO: throw
        }

        if (!availabilityService.isAvailable(
                reservationRequest.getChargerId(),
                reservationRequest.getStartTime(),
                reservationRequest.getStartTime().plusMinutes(
                        reservationRequest.getDuration().getMinutes()
                )
        )) {
            // TODO: throw
        }

        Reservation reservation = Reservation.builder()
                .startTime(reservationRequest.getStartTime())
                .endTime(reservationRequest.getStartTime().plusMinutes(
                        reservationRequest.getDuration().getMinutes()
                ))
                .reservationStatus(ReservationStatus.ACTIVE)
                .user(user)
                .charger(charger)
                .build();

        user.getReservations().add(reservation);
        charger.getReservations().add(reservation);

        reservationRepository.save(reservation);

        return toResponse(reservation);
    }

    public List<ReservationResponse> getUserReservationsByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow();

        return user.getReservations().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void cancelUserReservation(Integer id, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow();

        Reservation reservation = user.getReservations().stream()
                .filter(each -> each.getId().equals(id))
                .findFirst()
                .orElseThrow();

        LocalDateTime now = LocalDateTime.now();
        if (reservation.getEndTime().isAfter(now)) {
            // TODO: throw
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
