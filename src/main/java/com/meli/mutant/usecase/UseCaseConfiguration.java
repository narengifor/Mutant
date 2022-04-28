package com.meli.mutant.usecase;

import com.meli.mutant.repository.DnaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * En esta clase de configuracion se crean los bean correspondientes a los useCase.
 */

@Configuration
public class UseCaseConfiguration {

    /**
     * Configuracion Bean para el useCase de deteccion de mutantes
     * @param dnaRepository repository para acceder a la informacion de la BD
     * @return UseCase para detectar si es mutante o humano con el repository como parametro
     */

    @Bean
    public MutantDetectorUseCase mutantDetectorUseCase(DnaRepository dnaRepository) {
        return new MutantDetectorUseCase(dnaRepository);
    }

    /**
     * Configuracion Bean para las estadisticas
     * @param dnaRepository repository para acceder a la informacion de la BD
     * @return UseCase de estadisticas con el repository como parametro
     */

    @Bean
    public StatisticsUseCase statisticsUseCase(DnaRepository dnaRepository) {
        return new StatisticsUseCase(dnaRepository);
    }
}
