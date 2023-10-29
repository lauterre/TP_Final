package NoUsar.celdaGenerica.columnaGenerica;

import java.util.ArrayList;
import java.util.List;

import NoUsar.celdaGenerica.CeldaNum;
import etiqueta.Etiqueta;
import etiqueta.EtiquetaString;

//estaria bueno que los na queden siempre ultimos al ordenar sin importar el orden

public class ColumnaNum extends Columna<CeldaNum> {
    
    public ColumnaNum(Etiqueta etiqueta, List<CeldaNum> celdas) {
        this.etiqueta = etiqueta;
        this.celdas = celdas;
    }

    public ColumnaNum(List<CeldaNum> celdas) {
        this.celdas = celdas;
    }

    @Override
    public String tipoDato() {
        return "Number".toString();
    }

    //para probar cosas:
    public String toString() {
        String inicial = "";
        for (CeldaNum celdaNum : celdas) {
            inicial += celdaNum.getValor();
            inicial += ", ";
        }
        return inicial;
    }

    public static void main(String[] args) {
        CeldaNum celda1 = new CeldaNum(3);
        CeldaNum celda2 = new CeldaNum(1);
        CeldaNum celda3 = new CeldaNum(null);
        CeldaNum celda4 = new CeldaNum(2);

        List<CeldaNum> listaCeldas = new ArrayList<>();
        listaCeldas.add(celda1);
        listaCeldas.add(celda2);
        listaCeldas.add(celda3);
        listaCeldas.add(celda4);

        EtiquetaString etiqueta = new EtiquetaString("columna");

        ColumnaNum col = new ColumnaNum(etiqueta, listaCeldas);

        System.out.println("original: " + col);

        col.ordenar("descendente");

        System.out.println("desc: "+ col);

        col.ordenar(null);

        System.out.println("asc: " + col);

    }
}
