package lector;

import java.util.List;

import lector.exceptions.ArchivoNoEncontradoException;

public abstract class Lector {

    public abstract List<String> leer(String ruta) throws ArchivoNoEncontradoException;

}
