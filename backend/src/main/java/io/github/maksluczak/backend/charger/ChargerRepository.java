package io.github.maksluczak.backend.charger;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChargerRepository extends JpaRepository<Charger, Integer> {

    List<Charger> findByCity(String city);

}
