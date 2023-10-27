package celda;

public abstract class Celda implements Comparable<Celda>{

    public abstract boolean isNA();

    @Override
    public abstract int compareTo(Celda o);
}
