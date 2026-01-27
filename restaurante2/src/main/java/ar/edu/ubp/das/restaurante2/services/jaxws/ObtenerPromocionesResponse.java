
package ar.edu.ubp.das.restaurante2.services.jaxws;

import java.util.List;
import ar.edu.ubp.das.restaurante2.beans.ContenidoBean;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "obtenerPromocionesResponse", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obtenerPromocionesResponse", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
public class ObtenerPromocionesResponse {

    @XmlElement(name = "Promociones", namespace = "")
    private List<ContenidoBean> promociones;

    /**
     * 
     * @return
     *     returns List<ContenidoBean>
     */
    public List<ContenidoBean> getPromociones() {
        return this.promociones;
    }

    /**
     * 
     * @param promociones
     *     the value for the promociones property
     */
    public void setPromociones(List<ContenidoBean> promociones) {
        this.promociones = promociones;
    }

}
