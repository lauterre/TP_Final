package groupby;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import celda.Celda;
import celda.CeldaString;
import columna.Columna;
import columna.ColumnaNum;
import etiqueta.Etiqueta;
import matriz.Tabla;
import groupby.exceptions.EtiquetasDistintasException;

public class GroupBy {
    Tabla tablaOrigen;
    List<Etiqueta> etiquetasParaAgrupar;
    List<Etiqueta> otrasEtiquetas;
    Map<Map<Etiqueta, String>, Tabla> agrupado = new LinkedHashMap<>();

    public GroupBy(Tabla tabla, List<String> listaEtiquetasParaAgrupar) {
        this.tablaOrigen = tabla;
        String[] etiquetasParaAgruparArrayStrings = listaEtiquetasParaAgrupar.toArray(new String[0]);
        List<Etiqueta> etiquetasParaAgrupar = tabla.convertirAEtiqueta(etiquetasParaAgruparArrayStrings);
        this.etiquetasParaAgrupar = etiquetasParaAgrupar;
        otrasEtiquetas = new ArrayList<>();
        otrasEtiquetas = tabla.obtenerEtiquetasColumnas().stream()
                .filter(etiqueta -> !etiquetasParaAgrupar.contains(etiqueta))
                .collect(Collectors.toList());

        try {
            validarColumnas();
        } catch (EtiquetasDistintasException e) {
            e.printStackTrace();
        }

        this.agrupado = agrupar();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Tabla> groupByColumna(Tabla subsetTabla, Etiqueta etiqueta) {

        Columna<CeldaString> laColumnaIndicada = (Columna<CeldaString>) subsetTabla
                .obtenerColumna(etiqueta.getNombre().toString());

        List<? extends Celda> celdasColumna = laColumnaIndicada.getCeldas();

        List<Etiqueta> etiquetasFilas = subsetTabla.obtenerEtiquetasFilas();

        Map<Etiqueta, String> valoresFilas = new LinkedHashMap<>();
        Map<String, Tabla> resultadoAgrupado = new LinkedHashMap<>();

        for (int i = 0; i < etiquetasFilas.size(); i++) {
            valoresFilas.put(etiquetasFilas.get(i), celdasColumna.get(i).getValor().toString());
        }

        Map<Object, Map<Object, Object>> mapaAgrupado = valoresFilas.entrySet()
                .stream()
                .collect(Collectors.groupingBy(
                        entry -> entry.getValue(),
                        Collectors.toMap(entry -> entry.getKey(),
                                entry -> entry.getValue())));

        for (Object key : mapaAgrupado.keySet()) {
            String keyString = (String) key.toString();
            Set filasGrupoSet = mapaAgrupado.get(keyString).keySet();
            List<Etiqueta> filasGrupoLista = new ArrayList<>();
            filasGrupoLista.addAll(filasGrupoSet);
            Tabla subsetAgrupado = subsetTabla.filtrarFilas(filasGrupoLista);
            resultadoAgrupado.put(keyString, subsetAgrupado);
        }

        return resultadoAgrupado;
    }

    public Map<Map<Etiqueta, String>, Tabla> groupByRecursivo(Tabla tabla, List<Etiqueta> etiquetasUtilizadas) {
        if (etiquetasUtilizadas.size() == 1) {
            Etiqueta ultimaEtiqueta = etiquetasUtilizadas.get(0);
            Map<String, Tabla> agrupadoUnaColumna = groupByColumna(tabla, ultimaEtiqueta);
            Map<Map<Etiqueta, String>, Tabla> resultadoProvisorio = new LinkedHashMap<>();
            for (String key : agrupadoUnaColumna.keySet()) {
                Map<Etiqueta, String> mapaEtiquetaValor = new LinkedHashMap<>();
                mapaEtiquetaValor.put(ultimaEtiqueta, key);
                resultadoProvisorio.put(mapaEtiquetaValor, agrupadoUnaColumna.get(key));
            }
            return resultadoProvisorio;
        } else {
            Map<Map<Etiqueta, String>, Tabla> resultadoProvisorio;
            Etiqueta etiquetaActual = etiquetasUtilizadas.get(0);
            etiquetasUtilizadas.remove(0);
            resultadoProvisorio = groupByRecursivo(tabla, etiquetasUtilizadas);

            Map<Map<Etiqueta, String>, Tabla> nuevoResultadoProvisorio = new LinkedHashMap<>();
            for (Map.Entry<Map<Etiqueta, String>, Tabla> entry : resultadoProvisorio.entrySet()) {
                Tabla valoresAgrupados = entry.getValue();
                Map<Etiqueta, String> mapaEtiquetaValor = entry.getKey();
                Map<String, Tabla> agrupadoNuevaColumna = groupByColumna(valoresAgrupados, etiquetaActual);

                for (Map.Entry<String, Tabla> entryInterior : agrupadoNuevaColumna.entrySet()) {
                    Tabla valoresAgrupadosInterno = entryInterior.getValue();
                    String valorEspecifico = entryInterior.getKey();

                    Map<Etiqueta, String> nuevoMapaEtiquetaValor = new LinkedHashMap<>();
                    for (Etiqueta etiquetaInterior : mapaEtiquetaValor.keySet()) {
                        nuevoMapaEtiquetaValor.put(etiquetaInterior, mapaEtiquetaValor.get(etiquetaInterior));
                    }
                    nuevoMapaEtiquetaValor.put(etiquetaActual, valorEspecifico);
                    nuevoResultadoProvisorio.put(nuevoMapaEtiquetaValor, valoresAgrupadosInterno);
                }
            }
            return nuevoResultadoProvisorio;
        }
    }

    public void validarColumnas()
            throws EtiquetasDistintasException {
        if (!tablaOrigen.obtenerEtiquetasColumnas().containsAll(etiquetasParaAgrupar)) {
            throw new EtiquetasDistintasException("No todas las etiquetas se encuentran en las columnas.");
        }
    }

    public Map<Map<Etiqueta, String>, Tabla> agrupar() {
        tablaOrigen.obtenerEtiquetasColumnas().containsAll(etiquetasParaAgrupar);
        List<Etiqueta> etiquetasUtilizadas = new ArrayList<>();
        etiquetasUtilizadas.addAll(etiquetasParaAgrupar);
        Map<Map<Etiqueta, String>, Tabla> resultadoAgrupado = groupByRecursivo(tablaOrigen, etiquetasUtilizadas);
        this.agrupado = resultadoAgrupado;
        return resultadoAgrupado;
    }

    public Map<Map<Etiqueta, String>, Tabla> obtenerGrupos() {
        return agrupado;
    }

    private StringBuilder imprimirEtiquetas(Map<Etiqueta, String> etiquetasGrupo) {
        StringBuilder out = new StringBuilder();
        String sep = " | ";
        int anchoEtiqueta = etiquetasGrupo.keySet().stream()
                .map(etiqueta -> etiqueta.toString())
                .map(string -> Integer.valueOf(string.length()))
                .max(Comparator.comparing(Integer::intValue)).get();
        if (anchoEtiqueta < 10) {
            anchoEtiqueta = 10;
        }
        int anchoValor = etiquetasGrupo.values().stream()
                .map(string -> Integer.valueOf(string.length()))
                .max(Comparator.comparing(Integer::intValue)).get();
        if (anchoValor < 7) {
            anchoValor = 7;
        }

        int anchoTotal = anchoEtiqueta + anchoValor + 3;

        out.append("\n");
        out.append(String.format("%-" + anchoEtiqueta + "s", "Etiqueta")).append(sep);
        out.append(String.format("%-" + anchoValor + "s", "Valor")).append("\n");
        out.append("-".repeat(anchoTotal)).append("\n");
        for (Etiqueta etiqueta : etiquetasGrupo.keySet()) {
            out.append(String.format("%-" + anchoEtiqueta + "s", etiqueta.toString())).append(sep);
            out.append(String.format("%-" + anchoValor + "s", etiquetasGrupo.get(etiqueta))).append("\n");
        }
        return out;
    }

    public void count() {
        StringBuilder out;
        for (Map.Entry<Map<Etiqueta, String>, Tabla> grupo : agrupado.entrySet()) {
            Map<Etiqueta, String> mapaTablaAgrupada = (Map<Etiqueta, String>) grupo.getKey();
            out = imprimirEtiquetas(mapaTablaAgrupada);

            Tabla tabla = (Tabla) grupo.getValue();
            out.append("Cantidad de filas: " + tabla.obtenerCantidadFilas() + "\n");
            System.out.println(out.toString());
        }

    }

    public void suma() {
        StringBuilder out;
        for (Map.Entry<Map<Etiqueta, String>, Tabla> grupo : agrupado.entrySet()) {

            Map<Etiqueta, String> mapaTablaAgrupada = (Map<Etiqueta, String>) grupo.getKey();
            out = imprimirEtiquetas(mapaTablaAgrupada);

            Tabla tabla = (Tabla) grupo.getValue();
            for (Etiqueta etiqueta : tabla.obtenerEtiquetasColumnas()) {
                Columna<? extends Celda> columna = tabla.obtenerColumna(etiqueta);
                if (columna.tipoDato() == "Number") {
                    ColumnaNum columnaNum = (ColumnaNum) columna;
                    out.append("Suma en la columna " + etiqueta + ": " + columnaNum.suma() + "\n");
                    out.append("\n");
                }
            }
            System.out.println(out.toString());
        }

    }

    public void mediana() {
        StringBuilder out;
        for (Map.Entry<Map<Etiqueta, String>, Tabla> grupo : agrupado.entrySet()) {

            Map<Etiqueta, String> mapaTablaAgrupada = (Map<Etiqueta, String>) grupo.getKey();
            out = imprimirEtiquetas(mapaTablaAgrupada);

            Tabla tabla = (Tabla) grupo.getValue();
            for (Etiqueta etiqueta : tabla.obtenerEtiquetasColumnas()) {
                Columna<? extends Celda> columna = tabla.obtenerColumna(etiqueta);
                if (columna.tipoDato() == "Number") {
                    ColumnaNum columnaNum = (ColumnaNum) columna;
                    out.append("La mediana en la columna " + etiqueta + " es: " + columnaNum.mediana() + "\n");
                    out.append("\n");
                }
            }
            System.out.println(out.toString());
        }

    }

    public void promedio() {
        StringBuilder out;
        for (Map.Entry<Map<Etiqueta, String>, Tabla> grupo : agrupado.entrySet()) {

            Map<Etiqueta, String> mapaTablaAgrupada = (Map<Etiqueta, String>) grupo.getKey();
            out = imprimirEtiquetas(mapaTablaAgrupada);

            Tabla tabla = (Tabla) grupo.getValue();
            for (Etiqueta etiqueta : tabla.obtenerEtiquetasColumnas()) {
                Columna<? extends Celda> columna = tabla.obtenerColumna(etiqueta);
                if (columna.tipoDato() == "Number") {
                    ColumnaNum columnaNum = (ColumnaNum) columna;
                    out.append("El promedio en la columna " + etiqueta + " es: " + columnaNum.promedio() + "\n");
                    out.append("\n");
                }
            }
            System.out.println(out.toString());
        }
    }

    public void max() {
        StringBuilder out;
        for (Map.Entry<Map<Etiqueta, String>, Tabla> grupo : agrupado.entrySet()) {

            Map<Etiqueta, String> mapaTablaAgrupada = (Map<Etiqueta, String>) grupo.getKey();
            out = imprimirEtiquetas(mapaTablaAgrupada);

            Tabla tabla = (Tabla) grupo.getValue();
            for (Etiqueta etiqueta : tabla.obtenerEtiquetasColumnas()) {
                Columna<? extends Celda> columna = tabla.obtenerColumna(etiqueta);
                if (columna.tipoDato() == "Number") {
                    ColumnaNum columnaNum = (ColumnaNum) columna;
                    out.append("El valor máximo en la columna " + etiqueta + " es: " + columnaNum.max() + "\n");
                    out.append("\n");
                }
            }
            System.out.println(out.toString());
        }

    }

    public void min() {
        StringBuilder out;
        for (Map.Entry<Map<Etiqueta, String>, Tabla> grupo : agrupado.entrySet()) {

            Map<Etiqueta, String> mapaTablaAgrupada = (Map<Etiqueta, String>) grupo.getKey();
            out = imprimirEtiquetas(mapaTablaAgrupada);

            Tabla tabla = (Tabla) grupo.getValue();
            for (Etiqueta etiqueta : tabla.obtenerEtiquetasColumnas()) {
                Columna<? extends Celda> columna = tabla.obtenerColumna(etiqueta);
                if (columna.tipoDato() == "Number") {
                    ColumnaNum columnaNum = (ColumnaNum) columna;
                    out.append("El valor minimo en la columna " + etiqueta + " es: " + columnaNum.min() + "\n");
                    out.append("\n");
                }
            }
            System.out.println(out.toString());
        }

    }

    public void varianza() {
        StringBuilder out;
        for (Map.Entry<Map<Etiqueta, String>, Tabla> grupo : agrupado.entrySet()) {

            Map<Etiqueta, String> mapaTablaAgrupada = (Map<Etiqueta, String>) grupo.getKey();
            out = imprimirEtiquetas(mapaTablaAgrupada);

            Tabla tabla = (Tabla) grupo.getValue();
            for (Etiqueta etiqueta : tabla.obtenerEtiquetasColumnas()) {
                Columna<? extends Celda> columna = tabla.obtenerColumna(etiqueta);
                if (columna.tipoDato() == "Number") {
                    ColumnaNum columnaNum = (ColumnaNum) columna;
                    out.append("La varianza en la columna " + etiqueta + " es: " + columnaNum.varianza() + "\n");
                    out.append("\n");
                }
            }
            System.out.println(out.toString());
        }

    }

    public void desvioEstandar() {
        StringBuilder out;
        for (Map.Entry<Map<Etiqueta, String>, Tabla> grupo : agrupado.entrySet()) {

            Map<Etiqueta, String> mapaTablaAgrupada = (Map<Etiqueta, String>) grupo.getKey();
            out = imprimirEtiquetas(mapaTablaAgrupada);

            Tabla tabla = (Tabla) grupo.getValue();
            for (Etiqueta etiqueta : tabla.obtenerEtiquetasColumnas()) {
                Columna<? extends Celda> columna = tabla.obtenerColumna(etiqueta);
                if (columna.tipoDato() == "Number") {
                    ColumnaNum columnaNum = (ColumnaNum) columna;
                    out.append("El desvío estándar en la columna " + etiqueta + " es: " + columnaNum.desvioEstandar()
                            + "\n");
                    out.append("\n");
                }

            }
            System.out.println(out.toString());
        }

    }

    public void summarize() {
        StringBuilder out;
        for (Map.Entry<Map<Etiqueta, String>, Tabla> grupo : agrupado.entrySet()) {

            Map<Etiqueta, String> mapaTablaAgrupada = (Map<Etiqueta, String>) grupo.getKey();
            out = imprimirEtiquetas(mapaTablaAgrupada);

            Tabla tabla = (Tabla) grupo.getValue();
            out.append("Cantidad de filas: " + tabla.obtenerCantidadFilas() + "\n\n");

            for (Etiqueta etiqueta : tabla.obtenerEtiquetasColumnas()) {
                Columna<? extends Celda> columna = tabla.obtenerColumna(etiqueta);
                if (columna.tipoDato() == "Number") {
                    ColumnaNum columnaNum = (ColumnaNum) columna;
                    out.append("Suma en la columna " + etiqueta + ": " + columnaNum.suma() + "\n");
                    out.append("La mediana en la columna " + etiqueta + " es: " + columnaNum.mediana() + "\n");
                    out.append("El promedio en la columna " + etiqueta + " es: " + columnaNum.promedio() + "\n");
                    out.append("El valor máximo en la columna " + etiqueta + " es: " + columnaNum.max() + "\n");
                    out.append("El valor minimo en la columna " + etiqueta + " es: " + columnaNum.min() + "\n");
                    out.append("La varianza en la columna " + etiqueta + " es: " + columnaNum.varianza() + "\n");
                    out.append("El desvío estándar en la columna " + etiqueta + " es: " + columnaNum.desvioEstandar()
                            + "\n");
                    out.append("\n");
                }

            }
            System.out.println(out.toString());
        }

    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        String sep = " | ";
        for (Map.Entry tablaAgrupada : agrupado.entrySet()) {

            Map<Etiqueta, String> mapaTablaAgrupada = (Map<Etiqueta, String>) tablaAgrupada.getKey();

            int anchoEtiqueta = mapaTablaAgrupada.keySet().stream()
                    .map(etiqueta -> etiqueta.toString())
                    .map(string -> Integer.valueOf(string.length()))
                    .max(Comparator.comparing(Integer::intValue)).get();
            if (anchoEtiqueta < 10) {
                anchoEtiqueta = 10;
            }

            int anchoValor = mapaTablaAgrupada.values().stream()
                    .map(string -> Integer.valueOf(string.length()))
                    .max(Comparator.comparing(Integer::intValue)).get();
            if (anchoValor < 7) {
                anchoValor = 7;
            }

            int anchoTotal = anchoEtiqueta + anchoValor + 3;

            out.append("\n");
            out.append(String.format("%-" + anchoEtiqueta + "s", "Etiqueta")).append(sep);
            out.append(String.format("%-" + anchoValor + "s", "Valor")).append("\n");
            out.append("-".repeat(anchoTotal)).append("\n");
            for (Etiqueta etiqueta : mapaTablaAgrupada.keySet()) {
                out.append(String.format("%-" + anchoEtiqueta + "s", etiqueta.toString())).append(sep);
                out.append(String.format("%-" + anchoValor + "s", mapaTablaAgrupada.get(etiqueta))).append("\n");
            }
            out.append("\nTabla agrupada: \n");
            out.append(tablaAgrupada.getValue().toString()).append("\n\n\n");
        }
        return out.toString();
    }

}
