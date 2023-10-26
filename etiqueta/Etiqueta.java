package etiqueta;

import columna.Columna;

public abstract class Etiqueta {
    private Columna columna;
    private int fila;

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public Columna getColumna() {
        return columna;
    }

    public void setColumna(Columna columna) {
        this.columna = columna;
    }
}
