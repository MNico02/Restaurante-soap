package ar.edu.ubp.das.restaurante2.beans;

import jakarta.xml.bind.annotation.*;

import java.math.BigDecimal;

public class ContenidoBean {

    private Integer nroSucursal; // null si es general del restaurante

    private int nroContenido;

    private String contenidoAPublicar;

    private String imagenAPublicar;

    private boolean publicado;

    private BigDecimal costoClick;


    public Integer getNroSucursal() { return nroSucursal; }
    public void setNroSucursal(Integer nroSucursal) { this.nroSucursal = nroSucursal; }
    public int getNroContenido() { return nroContenido; }
    public void setNroContenido(int nroContenido) { this.nroContenido = nroContenido; }
    public String getContenidoAPublicar() { return contenidoAPublicar; }
    public void setContenidoAPublicar(String contenidoAPublicar) { this.contenidoAPublicar = contenidoAPublicar; }
    public String getImagenAPublicar() { return imagenAPublicar; }
    public void setImagenAPublicar(String imagenAPublicar) { this.imagenAPublicar = imagenAPublicar; }
    public boolean isPublicado() { return publicado; }
    public void setPublicado(boolean publicado) { this.publicado = publicado; }
    public BigDecimal getCostoClick() { return costoClick; }
    public void setCostoClick(BigDecimal costoClick) { this.costoClick = costoClick; }
}
