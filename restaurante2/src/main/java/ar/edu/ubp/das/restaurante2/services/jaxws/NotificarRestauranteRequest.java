
package ar.edu.ubp.das.restaurante2.services.jaxws;

import ar.edu.ubp.das.restaurante2.beans.NotiRestReqBean;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlRootElement(name = "notificarRestauranteRequest", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "notificarRestauranteRequest", namespace = "http://services.restaurante2.das.ubp.edu.ar/")
public class NotificarRestauranteRequest {

    @XmlElement(name = "NotiRestReqBean", namespace = "")
    private NotiRestReqBean notiRestReqBean;

    /**
     * 
     * @return
     *     returns NotiRestReqBean
     */
    public NotiRestReqBean getNotiRestReqBean() {
        return this.notiRestReqBean;
    }

    /**
     * 
     * @param notiRestReqBean
     *     the value for the notiRestReqBean property
     */
    public void setNotiRestReqBean(NotiRestReqBean notiRestReqBean) {
        this.notiRestReqBean = notiRestReqBean;
    }

}
