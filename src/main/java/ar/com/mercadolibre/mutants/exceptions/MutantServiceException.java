package ar.com.mercadolibre.mutants.exceptions;

public class MutantServiceException extends  MutantException{

    private static final long serialVersionUID = 6415109464142082174L;

    private int code;

    public MutantServiceException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
