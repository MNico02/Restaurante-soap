
package ar.edu.ubp.das.restaurante2.services.jaxws;

import ar.edu.ubp.das.restaurante2.beans.SoliHorarioBean;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ConsultarDisponibilidadRequest", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConsultarDisponibilidadRequest", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
public class ObtenerHorarios {

    @XmlElement(name = "soliHorario", namespace = "")
    private SoliHorarioBean soliHorario;

    /**
     * 
     * @return
     *     returns SoliHorarioBean
     */
    public SoliHorarioBean getSoliHorario() {
        return this.soliHorario;
    }

    /**
     * 
     * @param soliHorario
     *     the value for the soliHorario property
     */
    public void setSoliHorario(SoliHorarioBean soliHorario) {
        this.soliHorario = soliHorario;
    }

}
