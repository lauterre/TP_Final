package Exceptions;

public class EtiquetaExistenteException extends Exception {
    public EtiquetaExistenteException() {
        super("La etiqueta indicada ya esta definida en la tabla");
    }
}
