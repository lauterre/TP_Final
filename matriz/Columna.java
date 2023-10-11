package matriz;

import java.util.List;

import celda.Celda;

public class Columna {
    List<Celda> celdas;
    Etiqueta etiqueta;

    public int size() {
        return 0;
    }

    public Columna(List<Celda> celdas) {
        this.celdas = celdas;
    }

    public Celda obtenerValor(Integer indiceFila) {
        // Asegúrate de que el índice de fila sea válido
        if (indiceFila >= 0 && indiceFila < celdas.size()) {
            return celdas.get(indiceFila);
        } else {
            throw new IllegalArgumentException("Índice de fila fuera de rango");
        }
    }


    public void fijarValor(int indiceFila, Celda valor) {
        // Asegúrate de que el índice de fila sea válido
        if (indiceFila >= 0 && indiceFila < celdas.size()) {
            celdas.set(indiceFila, valor);
        } else {
            throw new IllegalArgumentException("Índice de fila fuera de rango");
        }
    
        
    }

}
