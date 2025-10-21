
package ar.edu.ubp.das.restaurante2.services.jaxws;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "GetInfoRestauranteRequest", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetInfoRestauranteRequest", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
public class GetInfoRestauranteRequest {

    @XmlElement(name = "id", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
    private int id;

    /**
     * 
     * @return
     *     returns int
     */
    public int getId() {
        return this.id;
    }

    /**
     * 
     * @param id
     *     the value for the id property
     */
    public void setId(int id) {
        this.id = id;
    }

}
