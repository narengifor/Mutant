package com.meli.mutant.usecase;

import com.meli.mutant.repository.DnaRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class UseCaseConfigurationTest {

    @InjectMocks
    UseCaseConfiguration useCaseConfiguration;

    @Mock
    DnaRepository dnaRepository;

    @Before
    public void before() {
        useCaseConfiguration = new UseCaseConfiguration();
    }

    @Test
    public void testConfiguration() {
        MutantDetectorUseCase mutantDetectorUseCase = useCaseConfiguration.mutantDetectorUseCase(dnaRepository);
        Assert.assertNotNull(mutantDetectorUseCase);

        StatisticsUseCase statisticsUseCase = useCaseConfiguration.statisticsUseCase(dnaRepository);
        Assert.assertNotNull(statisticsUseCase);
    }
}
