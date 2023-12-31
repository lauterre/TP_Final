package escritor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Exceptions.EtiquetaInvalidaException;
import etiqueta.Etiqueta;
import fila.Fila;
import matriz.Tabla;

public class EscritorCSV extends Escritor {
    Tabla tabla;
    String direccion;

    public EscritorCSV(Tabla tabla, String direccion) {
        this.tabla = tabla;
        this.direccion = direccion;
    }

    public void escribirDatos() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(direccion))) {

            if (tabla.tieneEncabezadosColumnas()) {
                List<String> encabezados = new ArrayList<>();
                if (tabla.tieneEtiquetasFilas()) {
                    encabezados.add("Índice");
                }

                tabla.obtenerEtiquetasColumnas().stream()
                        .map(x -> x.toString())
                        .forEach(x -> encabezados.add(x));
                bufferedWriter.write(String.join(",", encabezados));
                bufferedWriter.newLine();
            }

            List<Etiqueta> etiquetasColumnas = tabla.obtenerEtiquetasColumnas();
            // Esto no debería ser necesario
            Etiqueta[] arrayEtiquetasColumnas = etiquetasColumnas.toArray(new Etiqueta[etiquetasColumnas.size()]);

            List<Etiqueta> etiquetas = tabla.obtenerEtiquetasFilas();
            for (int i = 0; i < etiquetas.size(); i++) {
                Etiqueta etiqueta = etiquetas.get(i);
                Fila fila = tabla.getFila(etiqueta, arrayEtiquetasColumnas);
                List<String> valoresFila = new ArrayList<>();
                if (tabla.tieneEtiquetasFilas()) {
                    valoresFila.add(etiqueta.getNombre().toString());
                }
                fila.getCeldas().stream()
                        .map(x -> x.getValor().toString())
                        .forEach(x -> valoresFila.add(x));
                bufferedWriter.write(String.join(",", valoresFila));
                bufferedWriter.newLine();
            }

        } catch (IOException | EtiquetaInvalidaException e) {
            e.printStackTrace();
        }
    }

}