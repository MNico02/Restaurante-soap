package ar.edu.ubp.das.restaurante2.beans;


public class ConfirmarReservaResponse {
    private boolean success;
    private String estado;     // "CONFIRMADA" | "RECHAZADA"
    private String mensaje;

    private String codReserva;


    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getCodReserva() { return codReserva; }
    public void setCodReserva(String codReserva) { this.codReserva = codReserva; }


}
