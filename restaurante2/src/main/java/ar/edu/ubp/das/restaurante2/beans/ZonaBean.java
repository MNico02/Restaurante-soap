package ar.edu.ubp.das.restaurante2.beans;



public class ZonaBean {
    private int codZona;
    private String nomZona;
    private int cantComensales;
    private boolean permiteMenores;
    private boolean habilitada;

    public int getCodZona() { return codZona; }
    public void setCodZona(int codZona) { this.codZona = codZona; }
    public String getNomZona() { return nomZona; }
    public void setNomZona(String nomZona) { this.nomZona = nomZona; }
    public int getCantComensales() { return cantComensales; }
    public void setCantComensales(int cantComensales) { this.cantComensales = cantComensales; }
    public boolean isPermiteMenores() { return permiteMenores; }
    public void setPermiteMenores(boolean permiteMenores) { this.permiteMenores = permiteMenores; }
    public boolean isHabilitada() { return habilitada; }
    public void setHabilitada(boolean habilitada) { this.habilitada = habilitada; }
}
