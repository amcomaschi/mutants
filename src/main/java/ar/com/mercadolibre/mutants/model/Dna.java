package ar.com.mercadolibre.mutants.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

@Entity
public class Dna {

    @Id
    private ObjectId id;

    @Indexed(options = @IndexOptions(unique = true))
    private Sequence sequence;

    private boolean mutant;

    public Dna(String[] dna, boolean isMutant) {
        this.sequence = new Sequence(dna);
        this.mutant = isMutant;
    }

    public String toString() {

        StringBuffer sb = new StringBuffer();
        sb.append("{ ")
                .append(this.sequence)
                .append(", mutant:")
                .append(mutant)
                .append(" }");

        return sb.toString();
    }

    private static class Sequence {
        private String[] dna;

        public Sequence(String[] seq) {
            this.dna = seq;
        }

        public String toString() {

            StringBuffer sb = new StringBuffer();
            sb.append("[ dna: ");

            for (String dnaItem : dna) {
                sb.append(dnaItem).append(" ");
            }
            sb.append("]");

            return sb.toString();
        }
    }
}