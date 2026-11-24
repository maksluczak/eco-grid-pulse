package io.github.maksluczak.backend.charger;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChargerRepository extends JpaRepository<Charger, Integer> {

    Optional<Charger> findById(Integer id);
}
