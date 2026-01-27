
package ar.edu.ubp.das.restaurante2.services.jaxws;

import ar.edu.ubp.das.restaurante2.beans.RestauranteBean;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "GetInfoRestauranteResponse", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetInfoRestauranteResponse", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
public class GetInfoRestauranteResponse {

    @XmlElement(name = "InfoRestaurante", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
    private RestauranteBean infoRestaurante;

    /**
     * 
     * @return
     *     returns RestauranteBean
     */
    public RestauranteBean getInfoRestaurante() {
        return this.infoRestaurante;
    }

    /**
     * 
     * @param infoRestaurante
     *     the value for the infoRestaurante property
     */
    public void setInfoRestaurante(RestauranteBean infoRestaurante) {
        this.infoRestaurante = infoRestaurante;
    }

}
