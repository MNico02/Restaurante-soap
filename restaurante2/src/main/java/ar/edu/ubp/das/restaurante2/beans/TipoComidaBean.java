package ar.edu.ubp.das.restaurante2.beans;



public class TipoComidaBean {
    private int nroTipoComida;
    private String nomTipoComida;
    private boolean habilitado;

    public int getNroTipoComida() { return nroTipoComida; }
    public void setNroTipoComida(int nroTipoComida) { this.nroTipoComida = nroTipoComida; }
    public String getNomTipoComida() { return nomTipoComida; }
    public void setNomTipoComida(String nomTipoComida) { this.nomTipoComida = nomTipoComida; }
    public boolean isHabilitado() { return habilitado; }
    public void setHabilitado(boolean habilitado) { this.habilitado = habilitado; }
}
