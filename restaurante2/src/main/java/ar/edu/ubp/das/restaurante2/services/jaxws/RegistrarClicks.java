
package ar.edu.ubp.das.restaurante2.services.jaxws;

import java.util.List;
import ar.edu.ubp.das.restaurante2.beans.SoliClickBean;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "registrarClicksRequest", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registrarClicksRequest", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
public class RegistrarClicks {

    @XmlElement(name = "Clicks", namespace = "")
    private List<SoliClickBean> clicks;

    /**
     * 
     * @return
     *     returns List<SoliClickBean>
     */
    public List<SoliClickBean> getClicks() {
        return this.clicks;
    }

    /**
     * 
     * @param clicks
     *     the value for the clicks property
     */
    public void setClicks(List<SoliClickBean> clicks) {
        this.clicks = clicks;
    }

}
