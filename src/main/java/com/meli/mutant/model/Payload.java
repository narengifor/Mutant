package com.meli.mutant.model;

import lombok.Getter;
import lombok.Setter;
/**
 * Este modelo permite obtener el json enviado en el request body del metodo post
 * @Param dna Es el array de strings con la secuencia de ADN
 */
@Getter
@Setter
public class Payload {
    private String [] dna;

}
