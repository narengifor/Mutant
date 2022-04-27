package com.meli.mutant.usecase;

import com.meli.mutant.repository.DnaRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.test.StepVerifier;

@RunWith(MockitoJUnitRunner.class)
public class StatisticsUseCaseTest {

    private static final int IS_MUTANT = 1;
    private static final int IS_HUMAN = 0;

    @InjectMocks
    StatisticsUseCase statisticsUseCase;

    @Mock
    DnaRepository dnaRepository;

    @Before
    public void before() {

    }


    @Test
    public void testGetStats() {

        Mockito.when(dnaRepository.getAccumulatedByType(IS_MUTANT)).thenReturn(1);
        Mockito.when(dnaRepository.getAccumulatedByType(IS_HUMAN)).thenReturn(5);

        StepVerifier.create(statisticsUseCase.getStats())
                .assertNext(stat -> {
                    Assert.assertEquals(Integer.valueOf(1), stat.getCount_mutant_dna());
                    Assert.assertEquals(Integer.valueOf(5), stat.getCount_human_dna());
                    Assert.assertEquals(0.2, stat.getRatio(),1);
                }).verifyComplete();
    }

    @Test
    public void testGetStatsNaN() {

        Mockito.when(dnaRepository.getAccumulatedByType(IS_MUTANT)).thenReturn(0);
        Mockito.when(dnaRepository.getAccumulatedByType(IS_HUMAN)).thenReturn(0);

        StepVerifier.create(statisticsUseCase.getStats())
                .assertNext(stat -> {
                    Assert.assertEquals(Integer.valueOf(0), stat.getCount_mutant_dna());
                    Assert.assertEquals(Integer.valueOf(0), stat.getCount_human_dna());
                    Assert.assertEquals(Double.NaN, stat.getRatio(),1);
                }).verifyComplete();
    }
}
