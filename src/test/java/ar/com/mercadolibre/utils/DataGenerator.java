package ar.com.mercadolibre.utils;

import java.util.Random;

public class DataGenerator {

    public static String[] generateDna(int N, boolean mutant) {

        String[] adn = new String[N];
        Random r = new Random();

        String alphabet = mutant ? "ATCG" : "QWER";

        for (int j = 0; j < N; j++) {
            StringBuffer row = new StringBuffer();

            for (int i = 0; i < N; i++) {
                row.append(alphabet.charAt(r.nextInt(alphabet.length())));
            }
            adn[j] = row.toString();
        }

        if(mutant) {
            setMutantSequence(N, adn);
        }


        //printMatrix(adn);
        return adn;
    }

    private static void setMutantSequence(int n, String[] dna) {

        // Seteo secuencia mutante en la diagonal
        for (int i = 0; i < n; i++) {
            char[] s = dna[i].toCharArray();
            s[i] = 'A';
            dna[i] = String.valueOf(s);
        }

        // Seteo secuencia mutante en la ultima secuencia/fila
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < n; i++) {
            sb.append('A');
        }
        dna[n-1] = sb.toString();
    }
}
