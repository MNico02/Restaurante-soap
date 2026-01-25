package ar.edu.ubp.das.restaurante2.beans;

import java.math.BigDecimal;

public class ModificarReservaReqBean {

    private String codReservaSucursal;
    private String fechaReserva;   // yyyy-MM-dd
    private String horaReserva;    // HH:mm:ss
    private int cantAdultos;
    private int cantMenores;
    private int codZona;

    private BigDecimal costoReserva;

    public BigDecimal getCostoReserva() {
        return costoReserva;
    }

    public void setCostoReserva(BigDecimal costoReserva) {
        this.costoReserva = costoReserva;
    }

    public String getCodReservaSucursal() {
        return codReservaSucursal;
    }

    public void setCodReservaSucursal(String codReservaSucursal) {
        this.codReservaSucursal = codReservaSucursal;
    }

    public String getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(String fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getHoraReserva() {
        return horaReserva;
    }

    public void setHoraReserva(String horaReserva) {
        this.horaReserva = horaReserva;
    }

    public int getCantAdultos() {
        return cantAdultos;
    }

    public void setCantAdultos(int cantAdultos) {
        this.cantAdultos = cantAdultos;
    }

    public int getCantMenores() {
        return cantMenores;
    }

    public void setCantMenores(int cantMenores) {
        this.cantMenores = cantMenores;
    }

    public int getCodZona() {
        return codZona;
    }

    public void setCodZona(int codZona) {
        this.codZona = codZona;
    }
}
