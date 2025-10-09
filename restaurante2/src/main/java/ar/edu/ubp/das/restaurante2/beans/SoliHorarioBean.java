package ar.edu.ubp.das.restaurante2.beans;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;


public class SoliHorarioBean {
    private int idSucursal;
    private int codZona;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;
    private int cantComensales;
    private boolean menores;

    public int getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public int getCodZona() {
        return codZona;
    }

    public void setCodZona(int codZona) {
        this.codZona = codZona;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getCantComensales() {
        return cantComensales;
    }

    public void setCantComensales(int cantComensales) {
        this.cantComensales = cantComensales;
    }

    public boolean isMenores() {
        return menores;
    }

    public void setMenores(boolean menores) {
        this.menores = menores;
    }
}

