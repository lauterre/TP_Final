package columna;

import java.util.ArrayList;
import java.util.List;

import celda.Celda;
import celda.CeldaBoolean;
import celda.CeldaNum;
import celda.CeldaString;

public abstract class Columna<T extends Celda> {
    List<Celda> celdas;

    public abstract void ordenar(String orden);

    public abstract void fijarValor(Integer indiceFila, Object valor);

    public abstract Celda obtenerValor(Integer indiceFila);

    public abstract String tipoDato();

    public abstract int size();

    public abstract List<T> getCeldas();

    public abstract void agregarCelda(Celda celda);

    public abstract List<Integer> indicesNA();

    // public abstract <T extends Celda> List<T> getCeldas();

    public static ColumnaNum crear(Number[] e) {
        List<CeldaNum> celdas = new ArrayList<>();
        for (Number valor : e) {
            celdas.add(new CeldaNum(valor));
        }
        return new ColumnaNum(celdas);
    }

    public static ColumnaString crear(String[] e) {
        List<CeldaString> celdas = new ArrayList<>();
        for (String valor : e) {
            celdas.add(new CeldaString(valor));
        }
        return new ColumnaString(celdas);
    }

    public static ColumnaBoolean crear(Boolean[] e) {
        List<CeldaBoolean> celdas = new ArrayList<>();
        for (Boolean valor : e) {
            celdas.add(new CeldaBoolean(valor));
        }
        return new ColumnaBoolean(celdas);
    }

}
