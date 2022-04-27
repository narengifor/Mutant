package com.meli.mutant.model;

import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

@Getter
@Setter
public class Stat {
    private Integer count_mutant_dna;
    private Integer count_human_dna;
    private double ratio;

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

    public double getRatio() {
        return (double) (count_mutant_dna == null ? 0 : count_mutant_dna) / (count_human_dna == null ? 0 : count_human_dna);
    }
}
