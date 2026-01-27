
package ar.edu.ubp.das.restaurante2.services.jaxws;

import ar.edu.ubp.das.restaurante2.beans.ReservaRestauranteBean;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "confirmarReservaRequest", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "confirmarReservaRequest", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
public class ConfirmarReservaRequest {

    @XmlElement(name = "reservaRestaurante", namespace = "")
    private ReservaRestauranteBean reservaRestaurante;

    /**
     * 
     * @return
     *     returns ReservaRestauranteBean
     */
    public ReservaRestauranteBean getReservaRestaurante() {
        return this.reservaRestaurante;
    }

    /**
     * 
     * @param reservaRestaurante
     *     the value for the reservaRestaurante property
     */
    public void setReservaRestaurante(ReservaRestauranteBean reservaRestaurante) {
        this.reservaRestaurante = reservaRestaurante;
    }

}
