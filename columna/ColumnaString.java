package columna;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import celda.Celda;
import celda.CeldaString;
import etiqueta.Etiqueta;
import etiqueta.EtiquetaString;

public class ColumnaString extends Columna{
    private Etiqueta etiqueta;
    private List<CeldaString> celdas;

    public ColumnaString(Etiqueta etiqueta, List<CeldaString> celdas) {
        this.etiqueta = etiqueta;
        this.celdas = celdas;
    }

    @Override
    public void ordenar(String orden){
        Collections.sort(celdas);

        if ("descendente".equals(orden)) {
            Collections.reverse(celdas);
        }
    }

    @Override
    public Celda obtenerValor(Integer indiceFila){
        // Asegúrate de que el índice de fila sea válido
        if (indiceFila >= 0 && indiceFila < celdas.size()) {
            return celdas.get(indiceFila); //celdas.get(indiceFila).getValor()?
        } else {
            throw new IllegalArgumentException("Índice de fila fuera de rango");
        }
    }

    @Override
    public void fijarValor(Integer indiceFIla, Celda valor) {
        // Asegúrate de que el índice de fila sea válido
        if (indiceFIla >= 0 && indiceFIla < celdas.size()) {
            celdas.set(indiceFIla, (CeldaString) valor);
        } else {
            throw new IllegalArgumentException("Índice de fila fuera de rango");
        }
    }

    @Override
    public String tipoDato() {
        return "String".toString();
    }

    @Override
    public int size() {
        return celdas.size();
    }

    public Etiqueta getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(Etiqueta etiqueta) {
        this.etiqueta = etiqueta;
    }

    public List<CeldaString> getCeldas() {
        return celdas;
    }

    public void setCeldas(List<CeldaString> celdas) {
        this.celdas = celdas;
    }

    //para probar cosas:
    public String toString() {
        String inicial = "";
        for (CeldaString celda : celdas) {
            inicial += celda.getValor();
            inicial += ", ";
        }
        return inicial;
    }

    public static void main(String[] args) {
        CeldaString celda1 = new CeldaString("c");
        CeldaString celda2 = new CeldaString("a");
        CeldaString celda3 = new CeldaString("d");
        CeldaString celda4 = new CeldaString("b");

        List<CeldaString> listaCeldas = new ArrayList<>();
        listaCeldas.add(celda1);
        listaCeldas.add(celda2);
        listaCeldas.add(celda3);
        listaCeldas.add(celda4);

        EtiquetaString etiqueta = new EtiquetaString("columna");

        ColumnaString col = new ColumnaString(etiqueta, listaCeldas);

        System.out.println("original: " + col);

        col.ordenar("descendente");

        System.out.println("desc: "+ col);

        col.ordenar(null);

        System.out.println("asc: " + col);

    }
}
