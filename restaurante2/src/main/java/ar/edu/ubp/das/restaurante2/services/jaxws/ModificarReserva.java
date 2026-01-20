
package ar.edu.ubp.das.restaurante2.services.jaxws;

import ar.edu.ubp.das.restaurante2.beans.ModificarReservaReqBean;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ModificarReservaRequest", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ModificarReservaRequest", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
public class ModificarReserva {

    @XmlElement(name = "ModificarReserva", namespace = "")
    private ModificarReservaReqBean modificarReserva;

    /**
     * 
     * @return
     *     returns ModificarReservaReqBean
     */
    public ModificarReservaReqBean getModificarReserva() {
        return this.modificarReserva;
    }

    /**
     * 
     * @param modificarReserva
     *     the value for the modificarReserva property
     */
    public void setModificarReserva(ModificarReservaReqBean modificarReserva) {
        this.modificarReserva = modificarReserva;
    }

}
