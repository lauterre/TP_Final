package celda;

public abstract class Celda implements Comparable<Celda> {

    public abstract boolean isNA();

    public abstract Object getValor();

    @Override
    public abstract int compareTo(Celda o);

    public abstract Celda copia();

    public static CeldaNum crear(int e) {
        return new CeldaNum(e);
    }

    public static CeldaString crear(String e) {
        return new CeldaString(e);
    }

    public static CeldaBoolean crear(boolean e) {
        return new CeldaBoolean(e);
    }
}
