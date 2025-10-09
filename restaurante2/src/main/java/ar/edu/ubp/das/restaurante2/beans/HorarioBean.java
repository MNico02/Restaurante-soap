package ar.edu.ubp.das.restaurante2.beans;

import java.sql.Time;

public class HorarioBean {

    private Time horaReserva;
    private Time horaHasta;

    public Time getHoraReserva() {
        return horaReserva;
    }

    public void setHoraReserva(Time horaReserva) {
        this.horaReserva = horaReserva;
    }

    public Time getHoraHasta() {
        return horaHasta;
    }

    public void setHoraHasta(Time horaHasta) {
        this.horaHasta = horaHasta;
    }
}
