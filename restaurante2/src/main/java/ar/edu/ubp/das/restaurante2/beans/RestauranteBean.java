package ar.edu.ubp.das.restaurante2.beans;

import java.util.List;

public class RestauranteBean {
    private int nroRestaurante;
    private String razonSocial;
    private String cuit;

    private List<ContenidoBean> contenidos;
    private List<SucursalBean> sucursales;

    public int getNroRestaurante() { return nroRestaurante; }
    public void setNroRestaurante(int nroRestaurante) { this.nroRestaurante = nroRestaurante; }
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
    public String getCuit() { return cuit; }
    public void setCuit(String cuit) { this.cuit = cuit; }
    public List<ContenidoBean> getContenidos() { return contenidos; }
    public void setContenidos(List<ContenidoBean> contenidos) { this.contenidos = contenidos; }
    public List<SucursalBean> getSucursales() { return sucursales; }
    public void setSucursales(List<SucursalBean> sucursales) { this.sucursales = sucursales; }
}
