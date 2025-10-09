
package ar.edu.ubp.das.restaurante2.services.jaxws;

import java.util.List;
import ar.edu.ubp.das.restaurante2.beans.HorarioBean;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ConsultarDisponibilidadResponse", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConsultarDisponibilidadResponse", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
public class ConsultarDisponibilidadResponse {

    @XmlElement(name = "HorariosResponse", namespace = "")
    private List<HorarioBean> horariosResponse;

    /**
     * 
     * @return
     *     returns List<HorarioBean>
     */
    public List<HorarioBean> getHorariosResponse() {
        return this.horariosResponse;
    }

    /**
     * 
     * @param horariosResponse
     *     the value for the horariosResponse property
     */
    public void setHorariosResponse(List<HorarioBean> horariosResponse) {
        this.horariosResponse = horariosResponse;
    }

}
