package com.meli.mutant.repository;

import com.meli.mutant.model.Dna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Esta interface permite consultar y guardar los datos en la BD postgreSQL a traves de JpaRepository
 */

public interface DnaRepository extends JpaRepository<Dna, Long> {
    /**
     * @param adn secuencia de adn
     * @return retorna el objeto dna en donde coincida la secuencia de adn
     */
    Dna findByDna(@Param("adn") String adn);

    /**
     * @param type diferenciados del tipo de secuencia (1 mutante, 0 humano)
     * @return La cantidad de secuencias con ADN consultadas segun el parametro type
     */
    @Query(value = "SELECT SUM(Dna.accumulate) FROM Dna WHERE Dna.is_mutant = :type", nativeQuery = true)
    Integer getAccumulatedByType(@Param("type") int type);

    /**
     * Permite guardar el objeto dna cuando no exite en la BD
     */
    @Override
    <S extends Dna> S saveAndFlush(S entity);
}
