package com.meli.mutant.usecase;

import com.meli.mutant.model.Dna;
import com.meli.mutant.repository.DnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class MutantDetectorUseCase {

    private static final int IS_MUTANT = 1;
    private static final int IS_HUMAN = 0;
    private static final int LENGTH_EQUAL_LETTERS = 4;
    private static final Pattern pattern = Pattern.compile("AAAA|CCCC|GGGG|TTTT");
    private int numAccumulatedSequences = 0;

    @Autowired
    private final DnaRepository dnaRepository;

    public Mono<Boolean> isMutant(String[] dna) {
        if (!dnaChecker(dna)) {
            return Mono.empty();
        }
        Dna existingDna = dnaSequenceExists(dna);
        if (null != existingDna) {
            updateDna(existingDna);
            if (IS_MUTANT == existingDna.getIsMutant()) {
                return Mono.just(true);
            } else {
                return Mono.empty();
            }
        } else {
            if (numSequencesFound(numHorizontalSequences(dna))
                    || numSequencesFound(numVerticalSequences(dna))
                    || numSequencesFound(numDiagonalSequences(dna))
                    || numSequencesFound(numContraDiagonalSequences(dna))) {
                saveDna(dna, IS_MUTANT);
                return Mono.just(true);
            } else {
                saveDna(dna, IS_HUMAN);
            }
        }
        return Mono.empty();
    }

    void saveDna(String[] dna, int type) {
        Dna newDna = new Dna();
        String strDna = Arrays.toString(dna);
        newDna.setDna(strDna);
        newDna.setIsMutant(type);
        newDna.setAccumulate(1L);
        dnaRepository.saveAndFlush(newDna);
    }

    void updateDna(Dna dna) {
        dna.setAccumulate(dna.getAccumulate() + 1);
        dnaRepository.saveAndFlush(dna);
    }

    public Boolean dnaChecker(String[] dna) {
        int DNArows = dna.length;
        Pattern pattern = Pattern.compile("[ATCG]+");
        if (DNArows < LENGTH_EQUAL_LETTERS) {
            return false;
        } else {
            for (String s : dna) {

                if (s.length() != DNArows || !pattern.matcher(s).matches()) {
                    return false;
                }
            }
        }
        return true;
    }

    Dna dnaSequenceExists(String[] dna) {
        String strDna = Arrays.toString(dna);
        return dnaRepository.findByDna(strDna);
    }

    public int numHorizontalSequences(String[] dna) {
        int cont = 0;
        for (String s : dna) {
            Matcher matcher = pattern.matcher(s);
            while (matcher.find()) {
                ++cont;
            }
        }
        return cont;
    }

    public int numVerticalSequences(String[] dna) {
        int cont = 0;
        int DNArows = dna.length;
        for (int column = 0; column < DNArows; column++) {
            String strColumn = "";
            for (String s : dna) {
                strColumn = strColumn.concat(String.valueOf(s.charAt(column)));
            }
            Matcher matcher = pattern.matcher(strColumn);
            while (matcher.find()) {
                ++cont;
            }
        }
        return cont;
    }

    int numContraDiagonalSequences(String[] dna) {
        int cont = 0;
        int size = dna.length;
        String strDiagonal;
        for (int column = 0; column < (size - (LENGTH_EQUAL_LETTERS - 1)); column++) {
            strDiagonal = getContraDiagonal(0, column, size, dna, "COLUMN");
            Matcher matcher = pattern.matcher(strDiagonal);
            while (matcher.find()) {
                ++cont;
            }
        }
        for (int row = 1; row < (size - (LENGTH_EQUAL_LETTERS - 1)); row++) {
            strDiagonal = getContraDiagonal(row, 0, size, dna, "ROW");
            Matcher matcher = pattern.matcher(strDiagonal);
            while (matcher.find()) {
                ++cont;
            }
        }
        return cont;
    }

    int numDiagonalSequences(String[] dna) {
        int cont = 0;
        int size = dna.length;
        String strDiagonal;
        for (int row = (LENGTH_EQUAL_LETTERS - 1); row < size; row++) {
            strDiagonal = getDiagonal(row, 0, size, dna, "ROW");
            Matcher matcher = pattern.matcher(strDiagonal);
            while (matcher.find()) {
                ++cont;
            }
        }
        for (int column = 0; column < (size - LENGTH_EQUAL_LETTERS); column++) {
            strDiagonal = getDiagonal(size - 1, column + 1, size - 1, dna, "COLUMN");
            Matcher matcher = pattern.matcher(strDiagonal);
            while (matcher.find()) {
                ++cont;
            }
        }
        return cont;
    }

    String getContraDiagonal(int startRow, int startColum, int size, String[] dna, String orientation) {
        String diagonal = "";
        switch (orientation) {
            case "COLUMN": {
                int row = startRow;
                for (int column = startColum; column < size; column++) {
                    diagonal = diagonal.concat(String.valueOf(dna[row].charAt(column)));
                    if (row < size) {
                        row++;
                    }
                }
                break;
            }
            case "ROW": {
                int column = startColum;
                for (int row = startRow; row < size; row++) {
                    diagonal = diagonal.concat(String.valueOf(dna[row].charAt(column)));
                    if (column < size) {
                        column++;
                    }
                }
            }
            break;
        }
        return diagonal;
    }

    String getDiagonal(int startRow, int startColum, int size, String[] dna, String orientation) {
        String diagonal = "";
        switch (orientation) {
            case "COLUMN": {
                int row = startRow;
                for (int column = startColum; column <= size; column++) {
                    diagonal = diagonal.concat(String.valueOf(dna[row].charAt(column)));
                    if (row > 0) {
                        row--;
                    }
                }
                break;
            }
            case "ROW": {
                int column = startColum;
                for (int row = startRow; row >= 0; row--) {
                    diagonal = diagonal.concat(String.valueOf(dna[row].charAt(column)));
                    if (column < size) {
                        column++;
                    }
                }
            }
            break;
        }
        return diagonal;
    }

    Boolean numSequencesFound(int num) {
        numAccumulatedSequences = numAccumulatedSequences + num;
        if (numAccumulatedSequences > 1) {
            numAccumulatedSequences = 0;
            return true;
        }
        return false;
    }

}
