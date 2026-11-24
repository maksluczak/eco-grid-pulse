package io.github.maksluczak.backend.reservation;

import io.github.maksluczak.backend.reservation.dto.ReservationRequest;
import io.github.maksluczak.backend.reservation.dto.ReservationResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/vi/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> makeReservation(
            @Valid @RequestBody ReservationRequest reservationRequest,
            Authentication authentication
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservationService.createNewChargerReservation(
                        reservationRequest,
                        authentication.getName()
                ));
    }

    @GetMapping("/me")
    public ResponseEntity<List<ReservationResponse>> getMyReservations(Authentication authentication) {
        return ResponseEntity
                .ok(reservationService.getUserReservationsByEmail(
                        authentication.getName()
                ));
    }

    @PatchMapping("{id}/cancel")
    public ResponseEntity<Void> cancelReservation(
            @PathVariable Integer id,
            Authentication authentication
    ) {
        reservationService.cancelUserReservation(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
