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
import jakarta.xml.ws.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Service
@WebService(serviceName = "Restaurante2", targetNamespace =
        "http://services.restaurante2.das.ubp.edu.ar/")
public class Restaurante2 {


    @Autowired
    private Restaurante2Repository restaurante2Repository;
   /* @Autowired
    private ReservaService reservaService;*/
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
        ConfirmarReservaResponse resp = restaurante2Repository.insReserva(jsonRequest);
        return gson.toJson(resp);
    }

    @WebMethod(operationName = "ConsultarDisponibilidad")
    @RequestWrapper(localName = "ConsultarDisponibilidadRequest")
    @ResponseWrapper(localName = "ConsultarDisponibilidadResponse")
    @WebResult(name = "jsonResponse")
    public String obtenerHorarios(@WebParam(name = "jsonRequest") String jsonRequest) {
        List<HorarioBean> response =  restaurante2Repository.getHorarios(jsonRequest);
        return gson.toJson(response);
    }


    @WebMethod(operationName = "GetInfoRestaurante")
    @RequestWrapper(localName = "GetInfoRestauranteRequest", targetNamespace = "http://services.restaurante2.das.ubp.edu.ar/", className = "ar.edu.ubp.das.restaurante2.services.jaxws.GetInfoRestauranteRequest")
    @ResponseWrapper(localName = "GetInfoRestauranteResponse", targetNamespace = "http://services.restaurante2.das.ubp.edu.ar/", className = "ar.edu.ubp.das.restaurante2.services.jaxws.GetInfoRestauranteResponse")
    @WebResult(name = "jsonResponse" , targetNamespace = "http://services.restaurante2.das.ubp.edu.ar/")
    public String getRestaurante() throws JsonProcessingException {
        RestauranteBean response = restaurante2Repository.getInfoRestaurante();
        /*System.out.println("=== DEBUG SERVIDOR ===");
        System.out.println("ID recibido: " + id);
        System.out.println("Restaurante obtenido: " + response.getSucursales().get(0).getZonas().get(0).getNomZona());
        System.out.println("=====================");*/

        String jsonResponse = gson.toJson(response);

        System.out.println("JSON Response: " + jsonResponse);

        return jsonResponse;
    }

    @WebMethod(operationName = "ModificarReserva")
    @RequestWrapper(localName = "ModificarReservaRequest")
    @ResponseWrapper(localName = "ModificarReservaResponse")
    @WebResult(name = "jsonResponse")
    public String modificarReserva( @WebParam(name = "jsonRequest") String jsonRequest) {
        ResponseBean response = restaurante2Repository.modificarReserva(jsonRequest);
        return gson.toJson(response);
    }

    @WebMethod(operationName = "cancelarReserva")
    @RequestWrapper(localName = "cancelarReservaRequest")
    @ResponseWrapper(localName = "cancelarReservaResponse")
    @WebResult(name = "jsonResponse")
    public String cancelarReserva( @WebParam(name = "jsonRequest") String jsonRequest) {
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(jsonRequest, JsonObject.class);
        String cod = json.get("codReservaSucursal").getAsString();
        ResponseBean response = restaurante2Repository.cancelarReservaPorCodigoSucursal(cod);
        return gson.toJson(response);
    }

    @WebMethod(operationName = "registrarClicks")
    @RequestWrapper(localName = "registrarClicksRequest")
    @ResponseWrapper(localName = "registrarClicksResponse")
    @WebResult(name = "jsonResponse")
    public String registrarClicks( @WebParam(name = "jsonRequest") String jsonRequest) {

        String resultado   =  restaurante2Repository.insClickLote(jsonRequest);
        ResponseBean resp = new ResponseBean();
        resp.setSuccess(true);
        resp.setMessage(resultado);
        resp.setStatus("OK");
        return gson.toJson(resp);
    }

    @WebMethod(operationName = "obtenerPromociones")
    @RequestWrapper(localName = "obtenerPromocionesRequest", targetNamespace = "http://services.restaurante2.das.ubp.edu.ar/", className = "ar.edu.ubp.das.restaurante2.services.jaxws.ObtenerPromociones")
    @ResponseWrapper(localName = "obtenerPromocionesResponse", targetNamespace = "http://services.restaurante2.das.ubp.edu.ar/", className = "ar.edu.ubp.das.restaurante2.services.jaxws.ObtenerPromocionesResponse")
    @WebResult(name = "jsonResponse")
    public String obtenerPromociones() {
        List<ContenidoBean> response = restaurante2Repository.getContenidos();
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
        UpdPublicarContenidosRespBean response = restaurante2Repository.notificarContenidos(jsonRequest);
        return gson.toJson(response);
    }







}
