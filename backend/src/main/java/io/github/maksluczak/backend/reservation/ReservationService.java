package io.github.maksluczak.backend.reservation;

import io.github.maksluczak.backend.reservation.dto.ReservationRequest;
import io.github.maksluczak.backend.reservation.dto.ReservationResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    public ReservationResponse createNewChargerReservation(
            ReservationRequest reservationRequest,
            String name
    ) {
        return new ReservationResponse();
    }

    public List<ReservationResponse> getUserReservationsByEmail(String name) {
        return new ArrayList<>();
    }

    public void cancelUserReservation(Integer id, String name) {
    }

    private ReservationResponse toResponse(Reservation reservation) {
        return ReservationResponse.builder()
                .build();
    }
}
