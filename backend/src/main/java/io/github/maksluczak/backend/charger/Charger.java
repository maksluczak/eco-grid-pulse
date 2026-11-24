package io.github.maksluczak.backend.charger;

import io.github.maksluczak.backend.reservation.Reservation;
import jakarta.persistence.*;
import lombok.*;

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
}
