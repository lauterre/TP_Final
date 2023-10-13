package celda;

public class CeldaString extends Celda{
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
    
    @Override
    public int compareTo(Celda otraCelda) {
        CeldaString otraCeldaString = (CeldaString) otraCelda;
        if (this.isNA()) {
            return 1;
        } else if (otraCelda.isNA()) {
            return -1;
        }

        return this.valor.compareTo(otraCeldaString.getValor());
    }

    @Override
    public boolean isNA() {
        if (this.valor.equals("NA") || this.valor == null) {
            return true;
        } else return false;
    }
}
