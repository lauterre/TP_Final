package celda;

public class CeldaString extends Celda implements Comparable<CeldaString>{
    private String valor;

    public CeldaString(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
    
    public int compareTo(CeldaString otraCelda) {
        if (this.isNA()) {
            return 1;
        } else if (otraCelda.isNA()) {
            return -1;
        }

        return this.valor.compareTo(otraCelda.getValor());
    }

    @Override
    public boolean isNA() {
        if (this.valor.equals("NA") || this.valor == null) {
            return true;
        } else return false;
    }
}
