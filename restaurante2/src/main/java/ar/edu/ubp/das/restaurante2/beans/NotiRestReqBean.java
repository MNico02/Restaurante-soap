package ar.edu.ubp.das.restaurante2.beans;


import java.math.BigDecimal;


public class NotiRestReqBean {
    int nroRestaurante;
    BigDecimal costoAplicado;
    String nroContenidos;

    public int getNroRestaurante() {
        return nroRestaurante;
    }

    public void setNroRestaurante(int nroRestaurante) {
        this.nroRestaurante = nroRestaurante;
    }

    public BigDecimal getCostoAplicado() {
        return costoAplicado;
    }

    public void setCostoAplicado(BigDecimal costoAplicado) {
        this.costoAplicado = costoAplicado;
    }

    public String getNroContenidos() {
        return nroContenidos;
    }

    public void setNroContenidos(String nroContenidos) {
        this.nroContenidos = nroContenidos;
    }
}
