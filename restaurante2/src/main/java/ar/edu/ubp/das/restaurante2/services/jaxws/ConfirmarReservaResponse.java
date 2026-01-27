
package ar.edu.ubp.das.restaurante2.services.jaxws;

import ar.edu.ubp.das.restaurante2.beans.ConfirmarReservaResp;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "confirmarReservaResponse", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "confirmarReservaResponse", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
public class ConfirmarReservaResponse {

    @XmlElement(name = "ReservaResponse", namespace = "")
    private ConfirmarReservaResp reservaResponse;

    /**
     * 
     * @return
     *     returns ConfirmarReservaResp
     */
    public ConfirmarReservaResp getReservaResponse() {
        return this.reservaResponse;
    }

    /**
     * 
     * @param reservaResponse
     *     the value for the reservaResponse property
     */
    public void setReservaResponse(ConfirmarReservaResp reservaResponse) {
        this.reservaResponse = reservaResponse;
    }

}
