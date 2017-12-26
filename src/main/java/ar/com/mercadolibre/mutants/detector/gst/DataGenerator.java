package ar.com.mercadolibre.mutants.detector.gst;

import java.util.Random;

public class DataGenerator {


    public static String[] generateMatix(int N) {


        String[] adn = new String[N];

        Random r = new Random();

        String alphabet = "ATCG";
        for (int j = 0; j < N; j++) {
            StringBuffer row = new StringBuffer();

            for (int i = 0; i < N; i++) {
                row.append(alphabet.charAt(r.nextInt(alphabet.length())));
            }
            adn[j] = row.toString();
        }
        return adn;
    }
}