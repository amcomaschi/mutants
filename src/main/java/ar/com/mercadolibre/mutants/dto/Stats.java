package ar.com.mercadolibre.mutants.dto;

public class Stats {

    private long count_mutant_dna;
    private long count_human_dna;
    private double ratio;

    public Stats(long mutants, long humans, double ratio) {
        this.count_mutant_dna = mutants;
        this.count_human_dna = humans;
        this.ratio = ratio;
    }

    public String toString() {
        return "[mutants: " + count_mutant_dna + ", humans: " + count_human_dna + ", ratio: " + ratio + "]";
    }
}
