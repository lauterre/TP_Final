package NoUsar.celdaGenerica.columnaGenerica;

import java.util.Collections;
import java.util.List;

import NoUsar.celdaGenerica.Celda;
import etiqueta.Etiqueta;

public abstract class Columna<T extends Celda> {
    protected Etiqueta etiqueta;
    protected List<T> celdas;

    public abstract String tipoDato();

    public void ordenar(String orden) {
        if (orden == null || orden.equalsIgnoreCase("ascendente")) {
            quicksortAscendente(0, celdas.size() - 1);
        } else if (orden.equalsIgnoreCase("descendente")) {
            quicksortDescendente(0, celdas.size() - 1);
        } else {
            throw new IllegalArgumentException("Orden no válido. Debe ser 'ascendente' o 'descendente'.");
        }
    }

    private void quicksortAscendente(int low, int high) {
        if (low < high) {
            int pi = partitionAscendente(low, high);
            quicksortAscendente(low, pi - 1);
            quicksortAscendente(pi + 1, high);
        }
    }

    private int partitionAscendente(int low, int high) {
        T pivot = celdas.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (celdas.get(j).compareTo(pivot) <= 0) {
                i++;
                Collections.swap(celdas, i, j);
            }
        }
        Collections.swap(celdas, i + 1, high);
        return i + 1;
    }

    private void quicksortDescendente(int low, int high) {
        if (low < high) {
            int pi = partitionDescendente(low, high);
            quicksortDescendente(low, pi - 1);
            quicksortDescendente(pi + 1, high);
        }
    }

    private int partitionDescendente(int low, int high) {
        T pivot = celdas.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (celdas.get(j).compareTo(pivot) >= 0) {
                i++;
                Collections.swap(celdas, i, j);
            }
        }
        Collections.swap(celdas, i + 1, high);
        return i + 1;
    }

    public void fijarValor(Integer indiceFila, T valor) {
        if (indiceFila >= 0 && indiceFila < celdas.size()) {
            celdas.set(indiceFila, valor);
        } else {
            throw new IllegalArgumentException("Índice de fila fuera de rango");
        }
    }

    public T obtenerValor(Integer indiceFila) {
        if (indiceFila >= 0 && indiceFila < celdas.size()) {
            return celdas.get(indiceFila);
        } else {
            throw new IllegalArgumentException("Índice de fila fuera de rango");
        }
    }

    public int size() {
        return celdas.size();
    }

    public Etiqueta getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(Etiqueta etiqueta) {
        this.etiqueta = etiqueta;
    }

    public List<T> getCeldas() {
        return celdas;
    }

    public void setCeldas(List<T> celdas) {
        this.celdas = celdas;
    }
}
