package com.meli.mutant.usecase;

import com.meli.mutant.repository.DnaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfiguration {

    @Bean
    public MutantDetectorUseCase mutantDetectorUseCase(DnaRepository dnaRepository) {
        return new MutantDetectorUseCase(dnaRepository);
    }

    @Bean
    public StatisticsUseCase statisticsUseCase(DnaRepository dnaRepository) {
        return new StatisticsUseCase(dnaRepository);
    }
}
