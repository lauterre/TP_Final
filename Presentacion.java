import java.util.ArrayList;
import java.util.List;

import matriz.Tabla;

public class Presentacion {
    static Tabla tabla;

    public static void crearTablaDesdeMatriz(Object[][] matriz, boolean tieneEncabezadosColumnas,
            boolean tieneEncabezadosFilas) {
        tabla = new Tabla(matriz, tieneEncabezadosColumnas, tieneEncabezadosFilas);
    }

    public static void mostrarTabla() {
        System.out.println(tabla);
        System.out
                .println("Cantidad de columnas: " + tabla.obtenerCantidadColumnas() + "\nColumnas: "
                        + tabla.obtenerEtiquetasColumnas() + "\nCantidad de filas: " + tabla.obtenerCantidadFilas()
                        + "\nFilas: " + tabla.obtenerEtiquetasFilas());
    }

    public static void agregarColumna() {
        List<Object> columna = new ArrayList<>();
        columna.add(10);
        columna.add(5);
        columna.add(7);
        tabla.agregarColumna(columna, "Nota");
        System.out.println("Se agrego la columna a la tabla\n" + tabla);
    }

    public static void guardarCSV() {
        tabla.guardarCSV("E:\\java_workspace\\TP_Final\\Estudiantes.csv");
        System.out.println("Archivo CSV guardado en E:\\java_workspace\\TP_Final\\Estudiantes.csv");
    }

    public static void cargarCSV() {
        tabla = new Tabla("E:/java_workspace/TP_Final/Pokemon.csv", true, false);
    }

    public static void verColumna() {
        System.out.println(tabla.obtenerColumna("Name"));
        tabla.convertirANum("Attack");
        System.out.println(tabla.obtenerTipoDeDato("Attack"));
    }

    public static void verFila() {
        tabla.verFila(3);
    }

    public static void imputarPorPromedio() {
        Number promedio = tabla.promedio("Attack");
        System.out.println("El promedio de la columna Attack es: " + promedio);
        tabla.imputar(promedio, "Attack");
        tabla.verFila(3);
    }

    public static void verCelda() {
        System.out.println(tabla.obtenerValor(5, "Name"));
    }

    public static void copia() {
        Tabla tabla2 = Tabla.copiarTabla(tabla);
        tabla.cambiarValor(2, "Name", "Algoritmos");
        tabla.verFila(2);
        tabla2.verFila(2);
    }

    public static void main(String[] args) {
        Object[][] matriz = new Object[4][3];

        matriz[0][0] = "Nombre";
        matriz[0][1] = "Apellido";
        matriz[0][2] = "Edad";
        matriz[1][0] = "Martín";
        matriz[1][1] = "Gutierrez";
        matriz[1][2] = 23;
        matriz[2][0] = "Javier";
        matriz[2][1] = "Moreno";
        matriz[2][2] = 24;
        matriz[3][0] = "Matias";
        matriz[3][1] = "Fernandez";
        matriz[3][2] = 29;

        crearTablaDesdeMatriz(matriz, true, false);
        mostrarTabla();

        agregarColumna();

        guardarCSV();

        cargarCSV();
        mostrarTabla();

        verColumna();
        verFila();

        // imputarPorPromedio();

        // verCelda();

        // copia();
    }
}
