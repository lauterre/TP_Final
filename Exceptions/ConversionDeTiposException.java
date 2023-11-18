package Exceptions;

public class ConversionDeTiposException extends RuntimeException{
    public ConversionDeTiposException() {
        super("Error: Conversion de tipos invalida");
    }

    public ConversionDeTiposException(String mensaje) {
        super(mensaje);
    }
}
