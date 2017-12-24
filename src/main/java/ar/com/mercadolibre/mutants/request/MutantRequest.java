package ar.com.mercadolibre.mutants.request;

import ar.com.mercadolibre.mutants.handler.Validable;

public class MutantRequest implements Validable {

    private String[] dna;

    public MutantRequest(String[] dna) {
        this.dna = dna;
    }

    public String[] getDna() {
        return dna;
    }

    public String toString() {

        StringBuffer sb = new StringBuffer();
        sb.append("[ ");

        for (String dnaItem : dna) {
            sb.append(dnaItem).append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean isValid() {

        if (dna != null) {
            for (int i = 0; i < dna.length; i++) {
                String s = dna[i];

                if (s.length() != dna.length) {
                    return Boolean.FALSE;
                }
            }
        } else {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }
}
