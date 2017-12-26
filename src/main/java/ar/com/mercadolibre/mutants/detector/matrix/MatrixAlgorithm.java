package ar.com.mercadolibre.mutants.detector.matrix;

import ar.com.mercadolibre.mutants.detector.Detector;

public class MatrixAlgorithm implements Detector {

    private static final String[] MUTANT_DNA_SEQUENCES = {"AAAA", "CCCC", "GGGG", "TTTT"};
    public int N;
    private static int rowDirs[] = {-1, -1, -1, 0, 0, 1, 1, 1};
    static int colDirs[] = {-1, 0, 1, -1, 1, -1, 0, 1};
    private int count = 0;


    boolean isSafe(int i, int j) {

        if (i >= 0 && i <= (N - 1) && j >= 0 && j <= N - 1) {
            return true;
        }

        return false;
    }

    public boolean analyzeDNA(String[] adn) {
        N = adn.length;

        for (String mutantDnaSeq : MUTANT_DNA_SEQUENCES) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    findAndCountDnaSequence(adn, i, j, mutantDnaSeq);

                    if (count >= 2) {
                        return Boolean.TRUE;
                    }
                }
            }
        }

        return Boolean.FALSE;
    }

    private void findAndCountDnaSequence(String[] adn, int row, int col, String word) {

        if (adn[row].charAt(col) != word.charAt(0)) {
            return;
        }

        int len = word.length();

        for (int dir = 0; dir < 8; ++dir) {
            int rowDir = row + rowDirs[dir];
            int colDir = col + colDirs[dir];
            int i;

            for (i = 1; i <= len - 1; i++) {

                if (!isSafe(rowDir, colDir)) {
                    break;

                }

                if (!(adn[rowDir].charAt(colDir) == word.charAt(i))) {
                    break;
                }

                rowDir = rowDir + rowDirs[dir];
                colDir = colDir + colDirs[dir];
            }

            if (i == len) {
                count++;

                if (count >= 2) {
                    return;
                }
            }
        }
    }

    public static void main(String[] args) {
        String[] adn = {"ATGCGA","CCGTTC","TTATGT","AGAAGG","CACCTA","TCACTG"};

        MatrixAlgorithm ma = new MatrixAlgorithm();

        boolean mutant = ma.analyzeDNA(adn);
        System.out.println("Mutante: " + mutant);
    }
}