package NoUsar.celdaGenerica.columnaGenerica;

import java.util.ArrayList;
import java.util.List;

import NoUsar.celdaGenerica.CeldaString;
import etiqueta.Etiqueta;
import etiqueta.EtiquetaString;

public class ColumnaString extends Columna<CeldaString> {

    public ColumnaString(Etiqueta etiqueta, List<CeldaString> celdas) {
        this.etiqueta = etiqueta;
        this.celdas = celdas;
    }

    public ColumnaString(List<CeldaString> celdas) {
        this.celdas = celdas;
    }

    @Override
    public String tipoDato() {
        return "String".toString();
    }

    //para probar cosas:
    @Override
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

        EtiquetaString etiqueta = new EtiquetaString("columna");

        ColumnaString col = new ColumnaString(etiqueta, listaCeldas);

        System.out.println("original: " + col);

        col.ordenar("descendente");

        System.out.println("desc: "+ col);

        col.ordenar(null);

        System.out.println("asc: " + col);

    }
}
