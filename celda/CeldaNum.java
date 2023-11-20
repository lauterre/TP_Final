package celda;

import java.util.Objects;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class CeldaNum extends Celda {
    private Number valor;

    public CeldaNum(Number valor) {
        this.valor = valor;
    }

    public CeldaBoolean transformarABoolean() {
        CeldaBoolean celdaBoolean = new CeldaBoolean(valor);
        return celdaBoolean;
    }

    @Override
    public int compareTo(Celda otraCelda) {
        CeldaNum otraCeldaNum = (CeldaNum) otraCelda;
        if (this.isNA()) {
            return -1;
        } else if (otraCelda.isNA()) {
            return 1;
        }

        double diferencia = this.valor.doubleValue() - otraCeldaNum.valor.doubleValue();
        if (diferencia < 0) {
            return -1;
        } else if (diferencia > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public Number getValor() {
        return valor;
    }

    @Override
    public void setValor(Object valor) {
        this.valor = (Number) valor;
    }

    @Override
    public CeldaNum copia() {
        valor = this.getValor();
        return new CeldaNum(valor);
    }

    @Override
    public boolean isNA() {
        if (valor == null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        if (!(valor == null)) {
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            DecimalFormat df = new DecimalFormat("###.##", simbolos);
            return df.format(valor);
        } else {
            return "NA"; // return "NA".toString()
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (this == null || this.getClass() != obj.getClass())
            return false;

        CeldaNum celda = (CeldaNum) obj;
        return celda.valor == this.valor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }
}
