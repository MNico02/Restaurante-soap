package ar.edu.ubp.das.restaurante2.beans;

public class ZonaTurnoBean {
    private int codZona;
    private String horaDesde;
    private boolean permiteMenores;

    public String getHoraDesde() {
        return horaDesde;
    }

    public void setHoraDesde(String horaDesde) {
        this.horaDesde = horaDesde;
    }

    public boolean isPermiteMenores() {
        return permiteMenores;
    }

    public void setPermiteMenores(boolean permiteMenores) {
        this.permiteMenores = permiteMenores;
    }

    public int getCodZona() {
        return codZona;
    }

    public void setCodZona(int codZona) {
        this.codZona = codZona;
    }
}
