package columna;

import celda.Celda;

public abstract class Columna {

    public abstract void ordenar(String orden);

    public abstract void fijarValor(Integer indiceFila, Object valor);

    public abstract Celda obtenerValor(Integer indiceFila);

    public abstract String tipoDato();

    public abstract int size();

    public abstract void agregarCelda(Celda celda);
}
