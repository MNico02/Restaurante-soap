
package ar.edu.ubp.das.restaurante2.services.jaxws;

import ar.edu.ubp.das.restaurante2.beans.ObtenerPromocionesReqBean;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "obtenerPromocionesRequest", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obtenerPromocionesRequest", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
public class ObtenerPromociones {

    @XmlElement(name = "ObtenerPromociones", namespace = "")
    private ObtenerPromocionesReqBean obtenerPromociones;

    /**
     * 
     * @return
     *     returns ObtenerPromocionesReqBean
     */
    public ObtenerPromocionesReqBean getObtenerPromociones() {
        return this.obtenerPromociones;
    }

    /**
     * 
     * @param obtenerPromociones
     *     the value for the obtenerPromociones property
     */
    public void setObtenerPromociones(ObtenerPromocionesReqBean obtenerPromociones) {
        this.obtenerPromociones = obtenerPromociones;
    }

}
