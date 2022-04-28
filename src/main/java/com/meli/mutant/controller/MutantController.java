package com.meli.mutant.controller;

import com.meli.mutant.model.Payload;
import com.meli.mutant.model.Stat;
import com.meli.mutant.repository.DnaRepository;
import com.meli.mutant.usecase.MutantDetectorUseCase;
import com.meli.mutant.usecase.StatisticsUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controlador donde se configura los endpoints del proyecto
 */

@RestController
public class MutantController {

    @Autowired
    private final MutantDetectorUseCase mutantDetector;
    @Autowired
    private final StatisticsUseCase statistics;
    @Autowired
    private final DnaRepository dnaRepository;

    /**
     *
     * @param mutantDetector UseCase donde se procesa la logica para detectar si la secuencia es mutante o humana.
     * @param statistics UseCase donde se obtienen las estadisticas de la informacion registrada en la base de datos.
     * @param dnaRepository Nos permite acceder a metodos para gestionar la informacion con la base de datos.
     */

    public MutantController(MutantDetectorUseCase mutantDetector, StatisticsUseCase statistics, DnaRepository dnaRepository) {
        this.mutantDetector = mutantDetector;
        this.statistics = statistics;
        this.dnaRepository = dnaRepository;
    }

    /**
     * Endpoint para recepcionar las secuencias de ADN
     * @param payload Objeto para facilitar la rececion del JSON enviado en el requestBody
     * @return Si la verificacion es mutante retorna Status OK y si es humana retorna Status Forbidden.
     */

    @PostMapping(value = "/mutant/")
    public Mono<ResponseEntity<String>> postIsMutant(@RequestBody() Payload payload) {
        return mutantDetector.isMutant(payload.getDna()).map(mutant -> ResponseEntity.status(HttpStatus.OK).body(""))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.FORBIDDEN).body(""));
    }

    /**
     * Endpoint para procesar las estadisticas de las secuencias de ADN.
     * @return Retorna objeto Stat con las estadisticas almacenadas en la BD.
     */
    @GetMapping(value = "/stats")
    public Mono<Stat> getStatistics() {
        return statistics.getStats();
    }
}
