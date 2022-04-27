package com.meli.mutant.usecase;

import com.meli.mutant.model.Dna;
import com.meli.mutant.repository.DnaRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class MutantDetectorUseCaseTest {

    private static final String[] DNA_MUTANT = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
    private static final String[] DNA_MUTANT_MINIMUM_SIZE = {"ATGA", "CAGA", "CGAT", "AAAA"};
    private static final String[] DNA_MUTANT_CONTRA_DIAGONAL = {"ATACAA", "CAGAGC", "CCATAT", "ACACGA", "AACATA", "TCACTG"};
    private static final String[] DNA_MUTANT_DIAGONAL = {"ATAGAA", "CAGAGC", "CGATCA", "GCACAA", "AATATA", "TCACTG"};
    private static final String[] DNA_MUTANT_VERTICAL = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
    private static final String[] DNA_MUTANT_HORIZONTAL = {"ATGCGA", "CAGTGCA", "TTATGT", "AGAAGG", "CCCCTA"};
    private static final String DNA_MUTANT_TEXT = "[ATGCGA, CAGTGC, TTATGT, AGAAGG, CCCCTA, TCACTG]";
    private static final String[] DNA_MUTANT_LOWER_CASE = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAgG", "CCCCTA", "TCACTG"};
    private static final String[] DNA_HUMAMN = {"ATGCGA", "CAGTGC", "TTATTT", "AGACGG", "GCGTCA", "TCACTG"};
    private static final String[] DNA_HUMAMN_SIZE_LARGE = {"TTGCGCAGCT", "CAGTAAACCT", "TTAGAGAGGT", "ATTCGGGAAA", "CCCAAACTAG", "GGGTACTGAA","TTAGAGAGGT", "ATTCGGGAAA","TTGCGCAGCT", "CAGTAAACCT"};
    private static final String[] DNA_ERROR_IN_ROWS = {"ATGCGA", "CAGTGC", "TTATGT"};
    private static final String[] DNA_ERROR_IN_COLUMNS = {"ATGCGA", "CAGTGCA", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
    private static final int IS_MUTANT = 1;
    private static final int IS_HUMAMN = 0;

    @InjectMocks
    MutantDetectorUseCase mutantDetectorUseCase;

    @Mock
    DnaRepository dnaRepository;


    @Test
    public void WhenDnaCheckerReturnFalse() {
        StepVerifier.create(mutantDetectorUseCase.isMutant(DNA_MUTANT_LOWER_CASE))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void WhenExitsInBdIsMutantReturnTrue() {
        Dna dna = new Dna();
        dna.setId(0L);
        dna.setDna(DNA_MUTANT_TEXT);
        dna.setIsMutant(IS_MUTANT);
        dna.setAccumulate(1L);
        Mockito.when(dnaRepository.findByDna(any())).thenReturn(dna);
        StepVerifier.create(mutantDetectorUseCase.isMutant(DNA_MUTANT))
                .assertNext(Assert::assertTrue).verifyComplete();
    }

    @Test
    public void WhenNotExitsInBdIsMutantWithContraDiagonalReturnTrue() {
        Mockito.when(dnaRepository.findByDna(any())).thenReturn(null);
        StepVerifier.create(mutantDetectorUseCase.isMutant(DNA_MUTANT_CONTRA_DIAGONAL))
                .assertNext(Assert::assertTrue).verifyComplete();
    }

    @Test
    public void WhenNotExitsInBdIsMutantWithDiagonalReturnTrue() {
        Mockito.when(dnaRepository.findByDna(any())).thenReturn(null);
        StepVerifier.create(mutantDetectorUseCase.isMutant(DNA_MUTANT_DIAGONAL))
                .assertNext(Assert::assertTrue).verifyComplete();
    }
    @Test
    public void WhenNotExitsInBdIsMutantMinimunSizeReturnTrue() {
        Mockito.when(dnaRepository.findByDna(any())).thenReturn(null);
        StepVerifier.create(mutantDetectorUseCase.isMutant(DNA_MUTANT_MINIMUM_SIZE))
                .assertNext(Assert::assertTrue).verifyComplete();
    }

    @Test
    public void WhenExitsInBdIsHumanReturnFalse() {
        Dna dna = new Dna();
        dna.setId(0L);
        dna.setDna(DNA_MUTANT_TEXT);
        dna.setIsMutant(IS_HUMAMN);
        dna.setAccumulate(1L);
        Mockito.when(dnaRepository.findByDna(any())).thenReturn(dna);
        StepVerifier.create(mutantDetectorUseCase.isMutant(DNA_HUMAMN))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void WhenNotExitsInBdIsHumanReturnFalse() {
        Mockito.when(dnaRepository.findByDna(any())).thenReturn(null);
        StepVerifier.create(mutantDetectorUseCase.isMutant(DNA_HUMAMN))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void WhenNotExitsInBdIsHumanSizeLargeReturnFalse() {
        Mockito.when(dnaRepository.findByDna(any())).thenReturn(null);
        StepVerifier.create(mutantDetectorUseCase.isMutant(DNA_HUMAMN_SIZE_LARGE))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void WhenDNASecuenceCheckerIsTrue() {
        boolean chk = mutantDetectorUseCase.dnaChecker(DNA_MUTANT);
        Assert.assertTrue(chk);
    }

    @Test
    public void WhenDNASecuenceCheckerIsFalseByLowerCase() {
        boolean chk = mutantDetectorUseCase.dnaChecker(DNA_MUTANT_LOWER_CASE);
        Assert.assertFalse(chk);
    }

    @Test
    public void WhenDNASecuenceCheckerIsFalseBySizeRows() {
        boolean chk = mutantDetectorUseCase.dnaChecker(DNA_ERROR_IN_ROWS);
        Assert.assertFalse(chk);
    }

    @Test
    public void WhenDNASecuenceCheckerIsFalseBySizeColums() {
        boolean chk = mutantDetectorUseCase.dnaChecker(DNA_ERROR_IN_COLUMNS);
        Assert.assertFalse(chk);
    }

    @Test
    public void WhenhorizontalSearch() {
        int chk = mutantDetectorUseCase.numHorizontalSequences(DNA_MUTANT_HORIZONTAL);
        Assert.assertEquals(chk, 1);
    }

    @Test
    public void WhenObtenerVertical() {
        int chk = mutantDetectorUseCase.numVerticalSequences(DNA_MUTANT_VERTICAL);
        Assert.assertEquals(chk, 1);
    }
}
