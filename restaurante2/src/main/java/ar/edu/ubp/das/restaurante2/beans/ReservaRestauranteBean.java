package ar.edu.ubp.das.restaurante2.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservaRestauranteBean {
    private SolicitudClienteBean solicitudCliente;
    private ReservaSolicitudBean reserva;

    public SolicitudClienteBean getSolicitudCliente() { return solicitudCliente; }
    public void setSolicitudCliente(SolicitudClienteBean solicitudCliente) { this.solicitudCliente = solicitudCliente; }

    public ReservaSolicitudBean getReserva() { return reserva; }
    public void setReserva(ReservaSolicitudBean reserva) { this.reserva = reserva; }
}
