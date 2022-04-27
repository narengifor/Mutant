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

@RestController
public class MutantController {

    @Autowired
    private final MutantDetectorUseCase mutantDetector;
    @Autowired
    private final StatisticsUseCase statistics;
    @Autowired
    private final DnaRepository dnaRepository;

    public MutantController(MutantDetectorUseCase mutantDetector, StatisticsUseCase statistics, DnaRepository dnaRepository) {
        this.mutantDetector = mutantDetector;
        this.statistics = statistics;
        this.dnaRepository = dnaRepository;
    }

    @PostMapping(value = "/mutant/")
    public Mono<ResponseEntity<String>> postIsMutant(@RequestBody() Payload payload) {
        return mutantDetector.isMutant(payload.getDna()).map(mutant -> ResponseEntity.status(HttpStatus.OK).body(""))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.FORBIDDEN).body(""));
    }

    @GetMapping(value = "/stats")
    public Mono<Stat> getStatistics() {
        return statistics.getStats();
    }
}
