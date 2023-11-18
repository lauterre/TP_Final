package Exceptions;

public class ValorInvalidoException extends RuntimeException{
    public ValorInvalidoException() {
        super("Error: Tipo de valor invalido");
    }

    public ValorInvalidoException(String mensaje) {
        super(mensaje);
    }
}
