package com.meli.mutant.model;

import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

/**
 * Este modelo permite obtener las estadisticas para posteriormente enviarlas en la peticion
 * @Param count_mutant_dna Es el numero de Secuencias de ADN mutantes
 * @Param count_human_dna Es el numero de secuencias de ADN humanas
 * @Param ratio Es el resultado de dividir la cantidad de secuencias mutantes entre las secuencias humanas
 */

@Getter
@Setter
public class Stat {
    private Integer count_mutant_dna;
    private Integer count_human_dna;
    private double ratio;

    /**
     * Constructor para obtener el numero de secuencias de ADN mutantes y humanas.
     */
    public Stat(Integer count_mutant_dna, Integer count_human_dna) {
        this.count_mutant_dna = count_mutant_dna;
        this.count_human_dna = count_human_dna;
    }

    public Integer getCount_mutant_dna() {
        return (null == count_mutant_dna) ? 0 : count_mutant_dna;
    }

    public Integer getCount_human_dna() {
        return (null == count_human_dna) ? 0 : count_human_dna;
    }

    /**
     * @return Retorna el ratio entre el numero de secuencias de ADN mutantes y humanas.
     */

    public double getRatio() {
        return (double) (count_mutant_dna == null ? 0 : count_mutant_dna) / (count_human_dna == null ? 0 : count_human_dna);
    }
}
