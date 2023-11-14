import matriz.Tabla;

public class App {
    public static void main(String[] args) {
        Object[][] m1 = {
                { 11, 2, 3, 4 },
                { 4, 1, 7, 8 },
                { 5, 1, 3, 9 },
                { 5, 2, 3, 9 }
        };

        Tabla miMatriz = new Tabla(m1, false, false);
        System.out.println(miMatriz.toString());
        String[] etiquetas = { "A", "B", "C", "D" };
        String[] etiquetasFilas = { "x", "y", "z", "w" };

        miMatriz.setEtiquetasColumnas(etiquetas);
        miMatriz.setEtiquetasFilas(etiquetasFilas);
        System.out.println(miMatriz.toString());

        String[] columnasAOrdenar = { "A", "B" };

        System.out.println(miMatriz.ordernarPorColumnas(columnasAOrdenar, "ascendente"));
        System.out.println(miMatriz.ordernarPorColumnas(columnasAOrdenar, "descendiente"));
        System.out.println(miMatriz.obtenerEtiquetasFilas());
    }

}
