
package ar.edu.ubp.das.restaurante2.services.jaxws;

import ar.edu.ubp.das.restaurante2.beans.UpdPublicarContenidosRespBean;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "notificarRestauranteResponse", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "notificarRestauranteResponse", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
public class NotificarRestauranteResponse {

    @XmlElement(name = "Notificacion", namespace = "")
    private UpdPublicarContenidosRespBean notificacion;

    /**
     * 
     * @return
     *     returns UpdPublicarContenidosRespBean
     */
    public UpdPublicarContenidosRespBean getNotificacion() {
        return this.notificacion;
    }

    /**
     * 
     * @param notificacion
     *     the value for the notificacion property
     */
    public void setNotificacion(UpdPublicarContenidosRespBean notificacion) {
        this.notificacion = notificacion;
    }

}
