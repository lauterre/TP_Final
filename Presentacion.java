import matriz.Tabla;

public class Presentacion {
    public static Tabla crearTablaDesdeMatriz(Object[][] matriz, boolean tieneEncabezadosColumnas, boolean tieneEncabezadosFilas) {
        return new Tabla(matriz, tieneEncabezadosColumnas, tieneEncabezadosFilas);
    }

    public static void mostrarTabla(Tabla tabla) {
        System.out.println(tabla);
        System.out.println("Columnas: " + tabla.obtenerEtiquetasColumnas());
        System.out.println("Filas: " + tabla.obtenerEtiquetasFilas());
    }
}
