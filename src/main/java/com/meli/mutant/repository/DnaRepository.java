package com.meli.mutant.repository;

import com.meli.mutant.model.Dna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DnaRepository extends JpaRepository<Dna, Long> {

    Dna findByDna(@Param("adn") String adn);

    @Query(value = "SELECT SUM(Dna.accumulate) FROM Dna WHERE Dna.is_mutant = :type", nativeQuery = true)
    Integer getAccumulatedByType(@Param("type") int type);

    @Override
    <S extends Dna> S saveAndFlush(S entity);
}
