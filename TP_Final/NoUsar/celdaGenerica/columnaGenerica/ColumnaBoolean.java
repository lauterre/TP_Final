package NoUsar.celdaGenerica.columnaGenerica;

import java.util.ArrayList;
import java.util.List;

import NoUsar.celdaGenerica.CeldaBoolean;
import etiqueta.Etiqueta;
import etiqueta.EtiquetaString;

public class ColumnaBoolean extends Columna<CeldaBoolean> {
    
    public ColumnaBoolean(Etiqueta etiqueta, List<CeldaBoolean> celdas) {
        this.etiqueta = etiqueta;
        this.celdas = celdas;
    }

    @Override
    public String tipoDato() {
        return "Booleana".toString();
    }

    //para probar cosas:
    public String toString() {
        String inicial = "";
        for (CeldaBoolean celda : celdas) {
            inicial += celda.getValor();
            inicial += ", ";
        }
        return inicial;
    }

    public static void main(String[] args) {
        CeldaBoolean celda1 = new CeldaBoolean(true);
        CeldaBoolean celda2 = new CeldaBoolean(false);
        CeldaBoolean celda3 = new CeldaBoolean(1);
        CeldaBoolean celda4 = new CeldaBoolean(0);
        CeldaBoolean celda5 = new CeldaBoolean((Integer) null);
        CeldaBoolean celda6 = new CeldaBoolean((Boolean) null);

        List<CeldaBoolean> listaCeldas = new ArrayList<>();
        listaCeldas.add(celda1);
        listaCeldas.add(celda2);
        listaCeldas.add(celda3);
        listaCeldas.add(celda4);
        listaCeldas.add(celda5);
        listaCeldas.add(celda6);

        EtiquetaString etiqueta = new EtiquetaString("columna");

        ColumnaBoolean col = new ColumnaBoolean(etiqueta, listaCeldas);

        System.out.println("original: " + col);

        col.ordenar("descendente");

        System.out.println("desc: "+ col);

        col.ordenar(null);

        System.out.println("asc: " + col);

    }
}
