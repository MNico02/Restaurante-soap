package ar.edu.ubp.das.restaurante2.services;
import ar.edu.ubp.das.restaurante2.beans.*;
import ar.edu.ubp.das.restaurante2.repositories.Restaurante2Repository;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    @Autowired
    private ReservaService reservaService;

    @WebMethod(operationName = "confirmarReserva")
    @RequestWrapper( localName = "confirmarReservaRequest",
            targetNamespace = "http://services.restaurante2.das.ubp.edu.ar/",
            className = "ar.edu.ubp.das.restaurante2.services.jaxws.ConfirmarReservaRequest"
    )
    @ResponseWrapper(
            localName = "confirmarReservaResponse",
            targetNamespace = "http://services.restaurante2.das.ubp.edu.ar/",
            className = "ar.edu.ubp.das.restaurante2.services.jaxws.ConfirmarReservaResponse"
    )
    @WebResult(name = "ReservaResponse")
    public ConfirmarReservaResp confirmarReserva(@WebParam(name = "reservaRestaurante")
            ReservaRestauranteBean req) {
       return reservaService.confirmarReserva(req);
    }

    @WebMethod(operationName = "ConsultarDisponibilidad")
    @RequestWrapper(localName = "ConsultarDisponibilidadRequest")
    @ResponseWrapper(localName = "ConsultarDisponibilidadResponse")
    @WebResult(name = "HorariosResponse")
    public List<HorarioBean> obtenerHorarios(@WebParam(name = "soliHorario") SoliHorarioBean soliHorarioBean) {
        return restaurante2Repository.getHorarios(soliHorarioBean);
    }


    @WebMethod(operationName = "GetInfoRestaurante")
    @RequestWrapper(localName = "GetInfoRestauranteRequest", targetNamespace = "http://services.restaurante2.das.ubp.edu.ar/", className = "ar.edu.ubp.das.restaurante2.services.jaxws.GetInfoRestauranteRequest")
    @ResponseWrapper(localName = "GetInfoRestauranteResponse", targetNamespace = "http://services.restaurante2.das.ubp.edu.ar/", className = "ar.edu.ubp.das.restaurante2.services.jaxws.GetInfoRestauranteResponse")
    @WebResult(name = "InfoRestaurante", targetNamespace = "http://services.restaurante2.das.ubp.edu.ar/")
    public RestauranteBean getRestaurante(@WebParam(name = "id", targetNamespace = "http://services.restaurante2.das.ubp.edu.ar/") int id) throws JsonProcessingException {
        return restaurante2Repository.getInfoRestaurante(id);
    }

    @WebMethod(operationName = "ModificarReserva")
    @RequestWrapper(localName = "ModificarReservaRequest")
    @ResponseWrapper(localName = "ModificarReservaResponse")
    @WebResult(name = "Response")
    public ResponseBean modificarReserva(
            @WebParam(name = "ModificarReserva") ModificarReservaReqBean req) {

        return restaurante2Repository.modificarReserva(req);
    }

    @WebMethod(operationName = "cancelarReserva")
    @RequestWrapper(localName = "cancelarReservaRequest")
    @ResponseWrapper(localName = "cancelarReservaResponse")
    @WebResult(name = "Response")
    public ResponseBean cancelarReserva(
            @WebParam(name = "CancelarReserva")
            CancelarReservaReqBean req) {

        return restaurante2Repository
                .cancelarReservaPorCodigoSucursal(
                        req.getCodReservaSucursal()
                );
    }

    @WebMethod(operationName = "registrarClicks")
    @RequestWrapper(localName = "registrarClicksRequest")
    @ResponseWrapper(localName = "registrarClicksResponse")
    @WebResult(name = "Response")
    public ResponseBean registrarClicks(
            @WebParam(name = "Clicks")
            List<SoliClickBean> clicks) {

        return restaurante2Repository.insClickLote(clicks);
    }

    @WebMethod(operationName = "obtenerPromociones")
    @RequestWrapper(localName = "obtenerPromocionesRequest")
    @ResponseWrapper(localName = "obtenerPromocionesResponse")
    @WebResult(name = "Promociones")
    public List<ContenidoBean> obtenerPromociones(
            @WebParam(name = "ObtenerPromociones")
            ObtenerPromocionesReqBean req) {


        //System.out.println(">>> SOAP obtenerPromociones ID = [" + req.getId() + "]");
        return restaurante2Repository.getPromociones(req.getId());
    }

    @WebMethod(operationName = "notificarRestaurante")
    @RequestWrapper(
            localName = "notificarRestauranteRequest",
            targetNamespace = "http://services.restaurante2.das.ubp.edu.ar/",
            className = "ar.edu.ubp.das.restaurante2.services.jaxws.NotificarRestauranteRequest"
    )
    @ResponseWrapper(
            localName = "notificarRestauranteResponse",
            targetNamespace = "http://services.restaurante2.das.ubp.edu.ar/",
            className = "ar.edu.ubp.das.restaurante2.services.jaxws.NotificarRestauranteResponse"
    )
    @WebResult(name = "Notificacion")
    public UpdPublicarContenidosRespBean notificarRestaurante(
            @WebParam(name = "NotiRestReqBean")
            NotiRestReqBean req
    ) {
        return restaurante2Repository.notificarContenidos(req);
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
