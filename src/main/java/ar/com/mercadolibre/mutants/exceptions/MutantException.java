package ar.com.mercadolibre.mutants.exceptions;

public class MutantException extends Exception {

    private int code;

    private static final long serialVersionUID = -4415279469780082174L;

    public MutantException(String message) {
        super(message);
    }

    public MutantException(String message, int code) {
        super(message);

        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
