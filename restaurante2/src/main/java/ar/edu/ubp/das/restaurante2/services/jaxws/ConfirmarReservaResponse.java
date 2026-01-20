
package ar.edu.ubp.das.restaurante2.services.jaxws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "confirmarReservaResponse", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "confirmarReservaResponse", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
public class ConfirmarReservaResponse {

    @XmlElement(name = "CodigoReserva", namespace = "")
    private String codigoReserva;

    /**
     * 
     * @return
     *     returns String
     */
    public String getCodigoReserva() {
        return this.codigoReserva;
    }

    /**
     * 
     * @param codigoReserva
     *     the value for the codigoReserva property
     */
    public void setCodigoReserva(String codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

}
