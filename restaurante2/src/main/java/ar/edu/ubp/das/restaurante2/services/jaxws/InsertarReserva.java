
package ar.edu.ubp.das.restaurante2.services.jaxws;

import ar.edu.ubp.das.restaurante2.beans.ReservaBean;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "InsertarReservaRequest", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InsertarReservaRequest", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
public class InsertarReserva {

    @XmlElement(name = "Reserva", namespace = "")
    private ReservaBean reserva;

    /**
     * 
     * @return
     *     returns ReservaBean
     */
    public ReservaBean getReserva() {
        return this.reserva;
    }

    /**
     * 
     * @param reserva
     *     the value for the reserva property
     */
    public void setReserva(ReservaBean reserva) {
        this.reserva = reserva;
    }

}
