
package ar.edu.ubp.das.restaurante2.services.jaxws;

import ar.edu.ubp.das.restaurante2.beans.ResponseBean;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ModificarReservaResponse", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ModificarReservaResponse", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
public class ModificarReservaResponse {

    @XmlElement(name = "Response", namespace = "")
    private ResponseBean response;

    /**
     * 
     * @return
     *     returns ResponseBean
     */
    public ResponseBean getResponse() {
        return this.response;
    }

    /**
     * 
     * @param response
     *     the value for the response property
     */
    public void setResponse(ResponseBean response) {
        this.response = response;
    }

}
