package ar.edu.ubp.das.restaurante2.services;
import ar.edu.ubp.das.restaurante2.beans.*;
import ar.edu.ubp.das.restaurante2.repositories.Restaurante2Repository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.xml.ws.RequestWrapper;
import jakarta.xml.ws.Response;
import jakarta.xml.ws.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
@WebService(serviceName = "Restaurante2", targetNamespace =
        "http://services.restaurante2.das.ubp.edu.ar/")
public class Restaurante2 {


    @Autowired
    private Restaurante2Repository restaurante2Repository;
    @Autowired
    private ReservaService reservaService;
    private final Gson gson = new Gson();



    @WebMethod(operationName = "confirmarReserva")
    @RequestWrapper(localName = "confirmarReservaRequest",
            targetNamespace = "http://services.restaurante2.das.ubp.edu.ar/",
            className = "ar.edu.ubp.das.restaurante2.services.jaxws.ConfirmarReservaRequest"
    )
    @ResponseWrapper(
            localName = "confirmarReservaResponse",
            targetNamespace = "http://services.restaurante2.das.ubp.edu.ar/",
            className = "ar.edu.ubp.das.restaurante2.services.jaxws.ConfirmarReservaResponse"
    )
    @WebResult(name = "jsonResponse")
    public String confirmarReserva(@WebParam(name = "jsonRequest") String jsonRequest) {

        ReservaRestauranteBean req = gson.fromJson(jsonRequest, ReservaRestauranteBean.class);

        ConfirmarReservaResp resp = reservaService.confirmarReserva(req);

        return gson.toJson(resp);
    }

    @WebMethod(operationName = "ConsultarDisponibilidad")
    @RequestWrapper(localName = "ConsultarDisponibilidadRequest")
    @ResponseWrapper(localName = "ConsultarDisponibilidadResponse")
    @WebResult(name = "jsonResponse")
    public String obtenerHorarios(@WebParam(name = "jsonRequest") String jsonRequest) {

        SoliHorarioBean request = gson.fromJson(jsonRequest, SoliHorarioBean.class);
        System.out.println("request date:"+request.getFecha());
        List<HorarioBean> response =  restaurante2Repository.getHorarios(request);

        return gson.toJson(response);
    }


    @WebMethod(operationName = "GetInfoRestaurante")
    @RequestWrapper(localName = "GetInfoRestauranteRequest", targetNamespace = "http://services.restaurante2.das.ubp.edu.ar/", className = "ar.edu.ubp.das.restaurante2.services.jaxws.GetInfoRestauranteRequest")
    @ResponseWrapper(localName = "GetInfoRestauranteResponse", targetNamespace = "http://services.restaurante2.das.ubp.edu.ar/", className = "ar.edu.ubp.das.restaurante2.services.jaxws.GetInfoRestauranteResponse")
    @WebResult(name = "jsonResponse" , targetNamespace = "http://services.restaurante2.das.ubp.edu.ar/")
    public String getRestaurante(@WebParam(name = "jsonRequest", targetNamespace = "http://services.restaurante2.das.ubp.edu.ar/") String jsonRequest) throws JsonProcessingException {
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(jsonRequest, JsonObject.class);
         int id = json.get("id").getAsInt();
        RestauranteBean response = restaurante2Repository.getInfoRestaurante(id);
        System.out.println("=== DEBUG SERVIDOR ===");
        System.out.println("ID recibido: " + id);
        System.out.println("Restaurante obtenido: " + response.getSucursales().get(0).getZonas().get(0).getNomZona());
        System.out.println("=====================");

        // ⚠️ CRÍTICO: Verifica que retornas el JSON en el response
        String jsonResponse = gson.toJson(response);

        System.out.println("JSON Response: " + jsonResponse);

        return jsonResponse;
    }

    @WebMethod(operationName = "ModificarReserva")
    @RequestWrapper(localName = "ModificarReservaRequest")
    @ResponseWrapper(localName = "ModificarReservaResponse")
    @WebResult(name = "jsonResponse")
    public String modificarReserva( @WebParam(name = "jsonRequest") String jsonRequest) {
        ModificarReservaReqBean request = gson.fromJson(jsonRequest, ModificarReservaReqBean.class);
        ResponseBean response = restaurante2Repository.modificarReserva(request);
        return gson.toJson(response);
    }

    @WebMethod(operationName = "cancelarReserva")
    @RequestWrapper(localName = "cancelarReservaRequest")
    @ResponseWrapper(localName = "cancelarReservaResponse")
    @WebResult(name = "jsonResponse")
    public String cancelarReserva( @WebParam(name = "jsonRequest") String jsonRequest) {
        CancelarReservaReqBean request = gson.fromJson(jsonRequest, CancelarReservaReqBean.class);
        ResponseBean response = restaurante2Repository.cancelarReservaPorCodigoSucursal(request.getCodReservaSucursal());
        return gson.toJson(response);
    }

    @WebMethod(operationName = "registrarClicks")
    @RequestWrapper(localName = "registrarClicksRequest")
    @ResponseWrapper(localName = "registrarClicksResponse")
    @WebResult(name = "jsonResponse")
    public String registrarClicks( @WebParam(name = "jsonRequest") String jsonRequest) {
        Type type = new TypeToken<List<SoliClickBean>>(){}.getType();
        List<SoliClickBean> request = gson.fromJson(jsonRequest, type);
        ResponseBean response =  restaurante2Repository.insClickLote(request);
        return gson.toJson(response);
    }

    @WebMethod(operationName = "obtenerPromociones")
    @RequestWrapper(localName = "obtenerPromocionesRequest", targetNamespace = "http://services.restaurante2.das.ubp.edu.ar/", className = "ar.edu.ubp.das.restaurante2.services.jaxws.ObtenerPromociones")
    @ResponseWrapper(localName = "obtenerPromocionesResponse", targetNamespace = "http://services.restaurante2.das.ubp.edu.ar/", className = "ar.edu.ubp.das.restaurante2.services.jaxws.ObtenerPromocionesResponse")
    @WebResult(name = "jsonResponse")
    public String obtenerPromociones(@WebParam(name = "jsonRequest") String jsonRequest) {
        ObtenerPromocionesReqBean request = gson.fromJson(jsonRequest, ObtenerPromocionesReqBean.class);
        System.out.println("request "+request);
        //System.out.println(">>> SOAP obtenerPromociones ID = [" + req.getId() + "]");
        List<ContenidoBean> response = restaurante2Repository.getPromociones(request.getId());
        return gson.toJson(response);
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
    @WebResult(name = "jsonResponse")
    public String notificarRestaurante(@WebParam(name = "jsonRequest") String jsonRequest) {
        NotiRestReqBean request = gson.fromJson(jsonRequest, NotiRestReqBean.class);
        System.out.println("request "+request.getNroRestaurante()+" "+request.getCostoAplicado()+" "+request.getNroContenidos());
        UpdPublicarContenidosRespBean response = restaurante2Repository.notificarContenidos(request);
        return gson.toJson(response);
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
