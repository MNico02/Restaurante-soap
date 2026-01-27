
package ar.edu.ubp.das.restaurante2.services.jaxws;

import ar.edu.ubp.das.restaurante2.beans.CancelarReservaReqBean;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "cancelarReservaRequest", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cancelarReservaRequest", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
public class CancelarReserva {

    @XmlElement(name = "CancelarReserva", namespace = "")
    private CancelarReservaReqBean cancelarReserva;

    /**
     * 
     * @return
     *     returns CancelarReservaReqBean
     */
    public CancelarReservaReqBean getCancelarReserva() {
        return this.cancelarReserva;
    }

    /**
     * 
     * @param cancelarReserva
     *     the value for the cancelarReserva property
     */
    public void setCancelarReserva(CancelarReservaReqBean cancelarReserva) {
        this.cancelarReserva = cancelarReserva;
    }

}
