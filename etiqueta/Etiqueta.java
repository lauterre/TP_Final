package etiqueta;

import columna.Columna;

public abstract class Etiqueta {
    private Columna columna;

    public Columna getColumna() {
        return columna;
    }

    public void setColumna(Columna columna) {
        this.columna = columna;
    }
}
