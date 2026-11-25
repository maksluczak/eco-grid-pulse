package io.github.maksluczak.backend.charger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChargerRepository extends JpaRepository<Charger, Integer> {

    List<Charger> findByCity(String city);

    @Query("SELECT c FROM Charger c WHERE c.id = :id")
    Optional<Charger> findByIdForUpdate(@Param("id") Integer id);
}
