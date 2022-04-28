package com.meli.mutant.usecase;

import com.meli.mutant.model.Stat;
import com.meli.mutant.repository.DnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

/**
 * UseCase para obtener las estadisiticas de las secuencias de ADN almacenadas en la BD
 */
@RequiredArgsConstructor
public class StatisticsUseCase {

    private static final int IS_MUTANT = 1;
    private static final int IS_HUMAN = 0;

    @Autowired
    private final DnaRepository dnaRepository;

    /**
     * Metodo para obtener estadisticas
     * @return Mono con objeto tipo Stat donde se setea informacion de las estadisticas obtenidas de la BD a traves del dnaRepository.
     */
    public Mono<Stat> getStats() {
        return Mono.just(new Stat(dnaRepository.getAccumulatedByType(IS_MUTANT), dnaRepository.getAccumulatedByType(IS_HUMAN)));
    }
}
