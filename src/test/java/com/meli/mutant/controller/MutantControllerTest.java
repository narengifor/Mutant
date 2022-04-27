package com.meli.mutant.controller;

import com.meli.mutant.model.Payload;
import com.meli.mutant.model.Stat;
import com.meli.mutant.usecase.MutantDetectorUseCase;
import com.meli.mutant.usecase.StatisticsUseCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class MutantControllerTest {

    @InjectMocks
    private MutantController mutantController;

    @Mock
    private StatisticsUseCase statisticsUseCase;

    @Mock
    private MutantDetectorUseCase mutantDetectorUseCase;

    @Test
    public void testNotNullController() {
        Assert.assertNotNull(mutantController);
    }

    @Test
    public void testIsMutant() {

        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        Payload payload = new Payload();
        payload.setDna(dna);
        Mockito.when(mutantDetectorUseCase.isMutant(any())).thenReturn(Mono.just(true));
        StepVerifier.create(mutantController.postIsMutant(payload))
                .assertNext(response -> Assert.assertEquals(HttpStatus.OK, response.getStatusCode())).verifyComplete();

    }


    @Test
    public void testGetStatistics() {
        Stat stat = new Stat(4, 2);
        Mockito.when(statisticsUseCase.getStats()).thenReturn(Mono.just(stat));
        StepVerifier.create(mutantController.getStatistics())
                .assertNext(stats -> {
                    Assert.assertEquals(Integer.valueOf(4), stats.getCount_mutant_dna());
                    Assert.assertEquals(Integer.valueOf(2), stats.getCount_human_dna());
                    Assert.assertEquals(2.0, stats.getRatio(), 1);
                }).verifyComplete();

    }

}

