package lector.exceptions;

import java.io.IOException;

public class ArchivoNoEncontradoException extends IOException {
    public ArchivoNoEncontradoException() {
        super("Archivo No Encontrado");
    }
}
