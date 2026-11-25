package io.github.maksluczak.backend.charger;

import io.github.maksluczak.backend.reservation.Reservation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Charger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;
    private String city;
    private String address;
    private String description;

    private Double longitude;
    private Double latitude;

    private LocalTime openTime;
    private LocalTime closeTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChargerPower chargerPowerKW;

    @OneToMany(mappedBy = "charger")
    private Set<Reservation> reservations;

    public LocalDateTime openingOn(LocalDate date) {
        return openTime != null ? date.atTime(openTime) : date.atStartOfDay();
    }

    public LocalDateTime closingOn(LocalDate date) {
        return closeTime != null ? date.atTime(closeTime) : date.plusDays(1).atStartOfDay();
    }

    public boolean isOpenDuring(LocalDateTime start, LocalDateTime end) {
        LocalDate day = start.toLocalDate();
        return !start.isBefore(openingOn(day)) && !end.isAfter(closingOn(day));
    }
}
