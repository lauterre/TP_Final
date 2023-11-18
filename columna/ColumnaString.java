package columna;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Exceptions.ConversionDeTiposException;
import Exceptions.ValorInvalidoException;
import celda.Celda;
import celda.CeldaString;

public class ColumnaString extends Columna<CeldaString> {
    private List<CeldaString> celdas;

    public ColumnaString(List<CeldaString> celdas) {
        this.celdas = celdas;
    }

    @Override
    public void ordenar(String orden) {
        Collections.sort(celdas);

        if ("descendente".equals(orden)) {
            Collections.reverse(celdas);
        }
    }

    @Override
    public Celda obtenerValor(Integer indiceFila) {
        // Asegúrate de que el índice de fila sea válido
        if (indiceFila >= 0 && indiceFila < celdas.size()) {
            return celdas.get(indiceFila); // celdas.get(indiceFila).getValor()?
        } else {
            throw new ValorInvalidoException("Índice de fila fuera de rango");
        }
    }

    @Override
    public void fijarValor(Integer indiceFila, Object valor) {
        if (!(valor instanceof String))
            throw new ValorInvalidoException("Tipo de valor invalido");
        if (indiceFila >= 0 && indiceFila < celdas.size()) {
            celdas.get(indiceFila).setValor((String) valor);
        } else {
            throw new ValorInvalidoException("Índice de fila fuera de rango");
        }
    }

    @Override
    public String tipoDato() {
        return "String";
    }

    @Override
    public int size() {
        return celdas.size();
    }

    public List<CeldaString> getCeldas() {
        return celdas;
    }

    public void setCeldas(List<CeldaString> celdas) {
        this.celdas = celdas;
    }

    @Override
    public void agregarCelda(Celda celda) {
        try {
            this.celdas.add((CeldaString) celda);
        } catch (ConversionDeTiposException e) {
            System.out.println("Error de conversión de tipos: " + e.getMessage());
        }
    }

    // para probar cosas:
    public String toString() {
        String inicial = "";
        for (CeldaString celda : celdas) {
            inicial += celda;
            inicial += ", ";
        }
        return inicial;
    }

    public static void main(String[] args) {
        CeldaString celda1 = new CeldaString("c");
        CeldaString celda2 = new CeldaString("a");
        CeldaString celda3 = new CeldaString("d");
        CeldaString celda4 = new CeldaString("NA");
        CeldaString celda5 = new CeldaString(null);
        CeldaString celda6 = new CeldaString("na");

        List<CeldaString> listaCeldas = new ArrayList<>();
        listaCeldas.add(celda1);
        listaCeldas.add(celda2);
        listaCeldas.add(celda3);
        listaCeldas.add(celda4);
        listaCeldas.add(celda5);
        listaCeldas.add(celda6);

        ColumnaString col = new ColumnaString(listaCeldas);

        System.out.println("original: " + col);

        col.ordenar("descendente");

        System.out.println("desc: " + col);

        col.ordenar(null);

        System.out.println("asc: " + col);

    }
}
