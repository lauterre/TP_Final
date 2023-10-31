package celda;

public abstract class Celda implements Comparable<Celda>{

    public abstract boolean isNA();

    public abstract Object getValor();

    @Override
    public abstract int compareTo(Celda o);

    public abstract Celda copia();
}
