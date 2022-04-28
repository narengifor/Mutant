package com.meli.mutant.usecase;

import com.meli.mutant.model.Dna;
import com.meli.mutant.repository.DnaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UseCase con la logica para recorrer secuencias de ADN e identificar si la secuencia de ADN es mutante o humana.
 */

@RequiredArgsConstructor
public class MutantDetectorUseCase {
    /**
     * variables globales
     */
    private static final int IS_MUTANT = 1;
    private static final int IS_HUMAN = 0;
    private static final int LENGTH_EQUAL_LETTERS = 4;
    private static final Pattern pattern = Pattern.compile("AAAA|CCCC|GGGG|TTTT");
    private int numAccumulatedSequences = 0;

    @Autowired
    private final DnaRepository dnaRepository;

    /**
     * Metodo para identificar si es mutante haciendo uso de otros metodos para verificar las secuencias segun su orientacion.
     * @param dna Secuencia de ADN a verificar,
     * @return Mono boleano en donde si la secuencia es mutante retorna true y en caso de ser humana se retorna mono vacio.
     */
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

    /**
     * Guarda el objeto DNA en la BD
     * @param dna secuendia de ADN
     * @param type identificados de la secuencian de ADN (1 es mutante) (0  es humano)
     */
    void saveDna(String[] dna, int type) {
        Dna newDna = new Dna();
        String strDna = Arrays.toString(dna);
        newDna.setDna(strDna);
        newDna.setIsMutant(type);
        newDna.setAccumulate(1L);
        dnaRepository.saveAndFlush(newDna);
    }

    /**
     * Actualiza la secuencia de ADN incrementando el valor en uno si la secuancia de ADN ya fue consultada.
     * @param dna Objeto con la informacion de la secuencia del ADN, identificador y valor acumulado.
     */
    void updateDna(Dna dna) {
        dna.setAccumulate(dna.getAccumulate() + 1);
        dnaRepository.saveAndFlush(dna);
    }

    /**
     * Verifica que la secuandia de ADN sea NxN, tamaño de filas y columnas, que solo tenga las letras en mayuscula y que no sean diferentes de ATCG
     * @param dna secuencia de ADN
     * @return Si la matrix cumple con las verificaciones retorna true, de lo contrario false.
     */
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

    /**
     * Busca en la BD si al secuencia de ADN ya fue ingresada
     * @param dna Secuandia de ADN a buscar
     * @return En caso de encontrarla retonar objeto DNA
     */
    Dna dnaSequenceExists(String[] dna) {
        String strDna = Arrays.toString(dna);
        return dnaRepository.findByDna(strDna);
    }

    /**
     * Cuenta el numero de matches de la expresion regular dada, es decir el numero de estructuras mutantes
     * encontradas en la secuencia de ADN de manera horizontal
     * @param dna Secuencia de ADN
     * @return Numero de estructuras mutantes encontradas
     */
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
    /**
     * Cuenta el numero de matches de la expresion regular dada, es decir el numero de estructuras mutantes
     * encontradas en la secuencia de ADN de manera vertical
     * @param dna Secuencia de ADN
     * @return Numero de estructuras mutantes encontradas
     */
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
    /**
     * Cuenta el numero de matches de la expresion regular dada, es decir el numero de estructuras mutantes
     * encontradas en la secuencia de ADN de manera contraDiagonal
     * @param dna Secuencia de ADN
     * @return Numero de estructuras mutantes encontradas
     */
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

    /**
     * Cuenta el numero de matches de la expresion regular dada, es decir el numero de estructuras mutantes
     * encontradas en la secuencia de ADN de manera diagonal
     * @param dna Secuencia de ADN
     * @return Numero de estructuras mutantes encontradas
     */
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

    /**
     * Arma la secuencia contraDiagonal del ADN a analizar
     * @param startRow Posicion inicial de las filas
     * @param startColum Posicion inicial de las columnas
     * @param size Tamaño de la secuencia de ADN
     * @param dna Secuencia de ADN a analizar
     * @param orientation Identificador de posicion de inicio para la secuencia en contraDiagonal
     * @return Cadena con la secuencia de ADN con los bases nitrogenas encontradas en secuencia contraDiagonal
     */
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

    /**
     * Arma la secuencia diagonal del ADN a analizar
     * @param startRow Posicion inicial de las filas
     * @param startColum Posicion inicial de las columnas
     * @param size Tamaño de la secuencia de ADN
     * @param dna Secuencia de ADN a analizar
     * @param orientation Identificador de posicion de inicio para la secuencia en diagonal
     * @return Cadena con la secuencia de ADN con los bases nitrogenas encontradas en secuencia diagonal
     */
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

    /**
     * Meotodo para validar si exite mas de una estructura mutante dentro de la secuencia de ADN
     * @param num Numero de estructuras mutantes encontradas en la secuencia de ADN
     * @return Valor boleano true para indicar que existe mas de una estructura mutante de los contrario es falso.
     */
    Boolean numSequencesFound(int num) {
        numAccumulatedSequences = numAccumulatedSequences + num;
        if (numAccumulatedSequences > 1) {
            numAccumulatedSequences = 0;
            return true;
        }
        return false;
    }

}
