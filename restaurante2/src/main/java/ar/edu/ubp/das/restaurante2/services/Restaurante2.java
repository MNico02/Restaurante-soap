package ar.edu.ubp.das.restaurante2.services;
import ar.edu.ubp.das.restaurante2.beans.HorarioBean;
import ar.edu.ubp.das.restaurante2.beans.ReservaBean;
import ar.edu.ubp.das.restaurante2.beans.SoliHorarioBean;
import ar.edu.ubp.das.restaurante2.repositories.Restaurante2Repository;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.xml.ws.RequestWrapper;
import jakarta.xml.ws.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@WebService(serviceName = "Restaurante2", targetNamespace =
        "http://services.restaurante2.das.ubp.edu.ar/")
public class Restaurante2 {
    @Autowired
    private Restaurante2Repository restaurante2Repository;

    @WebMethod(operationName = "InsertarReserva")
    @RequestWrapper(localName = "InsertarReservaRequest")
    @ResponseWrapper(localName = "InsertarReservaResponse")
    @WebResult(name = "CodigoReserva")
    public String insertarReserva(@WebParam(name = "Reserva")
                                  ReservaBean reserva) {
        return restaurante2Repository.insReserva(reserva);
    }

    @WebMethod(operationName = "ConsultarDisponibilidad")
    @RequestWrapper(localName = "ConsultarDisponibilidadRequest")
    @ResponseWrapper(localName = "ConsultarDisponibilidadResponse")
    @WebResult(name = "HorariosResponse")
    public List<HorarioBean> obtenerLocalidades(@WebParam(name = "soliHorario") SoliHorarioBean soliHorarioBean) {
        return restaurante2Repository.getHorarios(soliHorarioBean);
    }

//
//    @WebMethod(operationName = "ObtenerProvincias")
//    @RequestWrapper(localName = "ObtenerProvinciasRequest")
//    @ResponseWrapper(localName = "ObtenerProvinciasResponse")
//    @WebResult(name = "ProvinciasResponse")
//    public List<ProvinciaBean> obtenerProvincias(@WebParam(name =
//            "codPais") String codPais) {
//        return localidadesRepository.getProvincias(codPais);
//    }
//
//    @WebMethod(operationName = "ObtenerLocalidades")
//    @RequestWrapper(localName = "ObtenerLocalidadesRequest")
//    @ResponseWrapper(localName = "ObtenerLocalidadesResponse")
//    @WebResult(name = "LocalidadesResponse")
//    public List<LocalidadBean> obtenerLocalidades(@WebParam(name =
//            "criteria") LocalidadCriteriaBean criteria) {
//        return localidadesRepository.getLocalidades(criteria);
//    }
//
//    @WebMethod(operationName = "InsertarLocalidad")
//    @RequestWrapper(localName = "InsertarLocalidadRequest")
//    @ResponseWrapper(localName = "InsertarLocalidadResponse")
//    @WebResult(name = "LocalidadResponse")
//    public LocalidadBean insertarLocalidad(@WebParam(name = "Localidad")
//                                           LocalidadBean localidad) {
//        return localidadesRepository.insLocalidad(localidad);
//    }

}
