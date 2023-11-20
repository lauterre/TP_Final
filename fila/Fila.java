package fila;

import java.util.List;

import celda.Celda;

public class Fila implements Comparable<Fila> {
    private List<Celda> celdas;

    public Fila(List<Celda> celdas) {
        this.celdas = celdas;
    }

    public List<Celda> getCeldas() {
        return celdas;
    }

    @Override
    public int compareTo(Fila fila) {
        if (fila == null)
            throw new NullPointerException();

        int comparacion = 0;
        int i = 0;
        while (comparacion == 0 && i < celdas.size() && i < fila.celdas.size()) {
            comparacion = celdas.get(i).compareTo(fila.celdas.get(i));
            i++;
        }
        return comparacion;
    }

    @Override
    public String toString() {
        String out = "[";
        for (int i = 0; i < celdas.size(); i++) {
            out += celdas.get(i).toString();
            if (!(i == celdas.size() - 1))
                out += ", ";
        }
        out += "]";
        return out;
    }

}
