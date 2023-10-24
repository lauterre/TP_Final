package celdaGenerica;

public abstract class Celda<T> implements Comparable<Celda<T>>{

    protected T valor;
    
    public abstract boolean isNA();

    public T getValor() {
        return valor;
    }

    public void setValor(T valor) {
        this.valor = valor;
    }

    @Override
    public abstract int compareTo(Celda<T> otraCelda);
}
