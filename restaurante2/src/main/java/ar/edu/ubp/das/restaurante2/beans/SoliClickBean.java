package ar.edu.ubp.das.restaurante2.beans;

public class SoliClickBean {

    private int nroClick;
    private int nroRestaurante;
    private String correo_cliente;
    private String fechaHoraRegistro;
    private float costoClick;
    private boolean notificado;
    private String codContenidoRestaurante;

    public int getNroClick() {
        return nroClick;
    }

    public void setNroClick(int nroClick) {
        this.nroClick = nroClick;
    }

    public int getNroRestaurante() {
        return nroRestaurante;
    }

    public void setNroRestaurante(int nroRestaurante) {
        this.nroRestaurante = nroRestaurante;
    }

    public String getCorreo_cliente() {
        return correo_cliente;
    }

    public void setCorreo_cliente(String correo_cliente) {
        this.correo_cliente = correo_cliente;
    }

    public String getFechaHoraRegistro() {
        return fechaHoraRegistro;
    }

    public void setFechaHoraRegistro(String fechaHoraRegistro) {
        this.fechaHoraRegistro = fechaHoraRegistro;
    }

    public float getCostoClick() {
        return costoClick;
    }

    public void setCostoClick(float costoClick) {
        this.costoClick = costoClick;
    }

    public boolean isNotificado() {
        return notificado;
    }

    public void setNotificado(boolean notificado) {
        this.notificado = notificado;
    }

    public String getCodContenidoRestaurante() {
        return codContenidoRestaurante;
    }

    public void setCodContenidoRestaurante(String codContenidoRestaurante) {
        this.codContenidoRestaurante = codContenidoRestaurante;
    }
}
