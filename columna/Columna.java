package columna;

import java.util.List;

import celda.Celda;
import etiqueta.Etiqueta;

public abstract class Columna {

    public abstract void ordenar(String orden);

    protected void ordenarCeldasAscendente(List<? extends Celda> celdas) {
            quicksort(celdas, 0, celdas.size() - 1);
    }

    protected void ordenarCeldasDescendente(List<? extends Celda> celdas) {
        //TODO
    }

    private void quicksort(List<? extends Celda> lista, int izquierda, int derecha) {
        if (izquierda < derecha) {
            int indiceParticion = particion(lista, izquierda, derecha);
            quicksort(lista, izquierda, indiceParticion - 1);
            quicksort(lista, indiceParticion + 1, derecha);
        }
    }

    private int particion(List<? extends Celda> lista, int izquierda, int derecha) {
        Celda pivote = lista.get(derecha);
        int i = izquierda - 1;

        for (int j = izquierda; j < derecha; j++) {
            if (lista.get(j).compareTo(pivote) <= 0) {
                i++;
                swap(lista, i, j);
            }
        }

        swap(lista, i + 1, derecha);
        return i + 1;
    }

    private void swap(List<? extends Celda> lista, int i, int j) {
        Celda temp = lista.get(i);
        lista.set(i, lista.get(j));
        lista.set(j, temp);
    }

    public abstract void fijarValor(Integer indiceFila, Celda valor);

    public abstract Celda obtenerValor(Integer indiceFila);
    
    public abstract String tipoDato();

    public abstract int size();
    
}
