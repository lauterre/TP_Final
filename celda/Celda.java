package celda;

public abstract class Celda implements Comparable<Celda> {

    public abstract boolean isNA();

    public abstract Object getValor();

    @Override
    public abstract int compareTo(Celda o);

    public abstract Celda copia();

    public static Celda crear(Object valor) {
        if (valor instanceof Boolean) {
            return new CeldaBoolean((Boolean) valor);
        } else if (valor instanceof Number) {
            return new CeldaNum((Number) valor);
        } else if (valor instanceof String) {
            return new CeldaString((String) valor);
        } else {
            throw new IllegalArgumentException("Tipo de valor no compatible para crear celda");
        }
    }

}
