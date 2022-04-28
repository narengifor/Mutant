package com.meli.mutant.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Este modelo permite obtener la secuencia de ADN para guardarla posteriormente en la base de datos.
 * @Param id identificador autoincremental
 * @Param dna Es el array de strings con la secuencia de ADN
 * @Param isMutant Variable que permite identificar si la secuendia de ADN es (1)-mutante o (0)-humana
 * @Param accumulate acumula el numero de veces que a sido consultada una secuencia de ADN, se usa para el calculo del ratio.
 */

@Data
@Entity
@Table()
public class Dna implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String dna;
    private int isMutant;
    private Long accumulate;
}
