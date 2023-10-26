package columna;

import java.util.List;

import celda.Celda;
import etiqueta.Etiqueta;

public abstract class Columna {

    public abstract void ordenar(String orden);

    public abstract void fijarValor(Integer indiceFila, Celda valor);

    public abstract Celda obtenerValor(Integer indiceFila);
    
    public abstract String tipoDato();

    public abstract int size();
    
}
