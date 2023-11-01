package columna;
import java.util.List;

import celda.Celda;

public abstract class Columna<T extends Celda> {
    List<Celda> celdas;

    public abstract void ordenar(String orden);

    public abstract void fijarValor(Integer indiceFila, Celda valor);

    public abstract Celda obtenerValor(Integer indiceFila);
    
    public abstract String tipoDato();

    public abstract int size();

    public abstract List<T> getCeldas();

    public abstract void agregarCelda(Celda celda);   
    
    public void eliminarCelda(int index) {
        // Agregar una implementación de IndexErrorException?
        celdas.remove(index);
    }
}
