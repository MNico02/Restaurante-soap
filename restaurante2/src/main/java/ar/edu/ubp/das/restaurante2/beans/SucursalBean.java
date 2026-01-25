package ar.edu.ubp.das.restaurante2.beans;


import java.util.List;

public class SucursalBean {
    private int nroSucursal;
    private String nomSucursal;
    private String calle;
    private String nroCalle;
    private String barrio;
    private int nroLocalidad;
    private String nomLocalidad;
    private int codProvincia;
    private String nomProvincia;
    private String codPostal;
    private String telefonos;
    private int totalComensales;
    private int minTolerenciaReserva; // (así está escrito en DB)
    private int nroCategoria;
    private String categoriaPrecio;

    private List<ContenidoBean> contenidos;       // Contenidos de esta sucursal (puede estar vacío)
    private List<ZonaBean> zonas;
    private List<EstiloBean> estilos;
    private List<EspecialidadBean> especialidades;
    private List<TipoComidaBean> tiposComidas;
    private List<TurnoBean> turnos;
    private List<ZonaTurnoBean> zonasTurnos;

    public List<TurnoBean> getTurnos() {
        return turnos;
    }

    public void setTurnos(List<TurnoBean> turnos) {
        this.turnos = turnos;
    }

    public List<ZonaTurnoBean> getZonasTurnos() {
        return zonasTurnos;
    }

    public void setZonasTurnos(List<ZonaTurnoBean> zonasTurnos) {
        this.zonasTurnos = zonasTurnos;
    }

    public int getNroSucursal() { return nroSucursal; }
    public void setNroSucursal(int nroSucursal) { this.nroSucursal = nroSucursal; }
    public String getNomSucursal() { return nomSucursal; }
    public void setNomSucursal(String nomSucursal) { this.nomSucursal = nomSucursal; }
    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }
    public String getNroCalle() { return nroCalle; }
    public void setNroCalle(String nroCalle) { this.nroCalle = nroCalle; }
    public String getBarrio() { return barrio; }
    public void setBarrio(String barrio) { this.barrio = barrio; }
    public int getNroLocalidad() { return nroLocalidad; }
    public void setNroLocalidad(int nroLocalidad) { this.nroLocalidad = nroLocalidad; }
    public String getNomLocalidad() { return nomLocalidad; }
    public void setNomLocalidad(String nomLocalidad) { this.nomLocalidad = nomLocalidad; }
    public int getCodProvincia() { return codProvincia; }
    public void setCodProvincia(int codProvincia) { this.codProvincia = codProvincia; }
    public String getNomProvincia() { return nomProvincia; }
    public void setNomProvincia(String nomProvincia) { this.nomProvincia = nomProvincia; }
    public String getCodPostal() { return codPostal; }
    public void setCodPostal(String codPostal) { this.codPostal = codPostal; }
    public String getTelefonos() { return telefonos; }
    public void setTelefonos(String telefonos) { this.telefonos = telefonos; }
    public int getTotalComensales() { return totalComensales; }
    public void setTotalComensales(int totalComensales) { this.totalComensales = totalComensales; }
    public int getMinTolerenciaReserva() { return minTolerenciaReserva; }
    public void setMinTolerenciaReserva(int minTolerenciaReserva) { this.minTolerenciaReserva = minTolerenciaReserva; }
    public int getNroCategoria() { return nroCategoria; }
    public void setNroCategoria(int nroCategoria) { this.nroCategoria = nroCategoria; }
    public String getCategoriaPrecio() { return categoriaPrecio; }
    public void setCategoriaPrecio(String categoriaPrecio) { this.categoriaPrecio = categoriaPrecio; }
    public List<ContenidoBean> getContenidos() { return contenidos; }
    public void setContenidos(List<ContenidoBean> contenidos) { this.contenidos = contenidos; }
    public List<ZonaBean> getZonas() { return zonas; }
    public void setZonas(List<ZonaBean> zonas) { this.zonas = zonas; }
    public List<EstiloBean> getEstilos() { return estilos; }
    public void setEstilos(List<EstiloBean> estilos) { this.estilos = estilos; }
    public List<EspecialidadBean> getEspecialidades() { return especialidades; }
    public void setEspecialidades(List<EspecialidadBean> especialidades) { this.especialidades = especialidades; }
    public List<TipoComidaBean> getTiposComidas() { return tiposComidas; }
    public void setTiposComidas(List<TipoComidaBean> tiposComidas) { this.tiposComidas = tiposComidas; }
}

