package ar.edu.ubp.das.restaurante2.endpoints;
import ar.edu.ubp.das.restaurante2.services.Restaurante2;
import ar.edu.ubp.das.restaurante2.services.jaxws.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;


@Endpoint
public class Restaurante2Endpoint {
    private static final String NAMESPACE_URI =
            "http://services.restaurante2.das.ubp.edu.ar/";

    @Autowired
    private Restaurante2 service;
    /*@Autowired
    private ReservaService reservaService;*/


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "confirmarReservaRequest")
    @ResponsePayload
    public ConfirmarReservaResponse confirmarReserva(@RequestPayload ConfirmarReservaRequest request) {

        String jsonRequest = request.getJsonRequest();

        String jsonResponse = service.confirmarReserva(jsonRequest);

        ConfirmarReservaResponse response = new ConfirmarReservaResponse();
        response.setJsonResponse(jsonResponse);

        return response;
    }
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ConsultarDisponibilidadRequest")
    @ResponsePayload
    public ObtenerHorariosResponse consultarDisponibilidad(@RequestPayload ObtenerHorarios request) {
        String jsonRequest = request.getJsonRequest();
        System.out.println("JSON request: " + jsonRequest);
        /*System.out.println("=================================");
        System.out.println("ID SUCURSAL     = " + soliHorarioBean.getIdSucursal());
        System.out.println("COD ZONA        = " + soliHorarioBean.getCodZona());
        System.out.println("FECHA           = " + soliHorarioBean.getFecha());
        System.out.println("CANT COMENSALES = " + soliHorarioBean.getCantComensales());
        System.out.println("MENORES         = " + soliHorarioBean.isMenores());
        System.out.println("=================================");*/
        String jsonResponse = service.obtenerHorarios(jsonRequest);

            /*System.out.println(">>> obtenerLocalidades devolvió horarios = " + horarios);
            System.out.println(">>> size = " + (horarios == null ? "null" : horarios.size()));*/

        ObtenerHorariosResponse response = new ObtenerHorariosResponse();
        response.setJsonResponse(jsonResponse);
        return response;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetInfoRestauranteRequest")
    @ResponsePayload
    public GetInfoRestauranteResponse getRestaurante() throws JsonProcessingException {

            String jsonResponse = service.getRestaurante();

            GetInfoRestauranteResponse response = new GetInfoRestauranteResponse();
            response.setJsonResponse(jsonResponse);
            return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ModificarReservaRequest")
    @ResponsePayload
    public ModificarReservaResponse modificarReserva(@RequestPayload ModificarReserva request) {
        String jsonRequest = request.getJsonRequest();

        String jsonResponse = service.modificarReserva(jsonRequest);
        ModificarReservaResponse response = new ModificarReservaResponse();
        response.setJsonResponse(jsonResponse);

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "cancelarReservaRequest")
    @ResponsePayload
    public CancelarReservaResponse cancelarReserva(@RequestPayload CancelarReserva request) {
        String jsonRequest = request.getJsonRequest();
        System.out.println("JSON request: " + jsonRequest);
        String jsonResponse = service.cancelarReserva(jsonRequest);
        CancelarReservaResponse response = new CancelarReservaResponse();
        response.setJsonResponse(jsonResponse);

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "registrarClicksRequest")
    @ResponsePayload
    public RegistrarClicksResponse registrarClicks(@RequestPayload RegistrarClicks request) {

        String jsonRequest = request.getJsonRequest();
        String jsonResponse = service.registrarClicks(jsonRequest);
        RegistrarClicksResponse response = new RegistrarClicksResponse();
        response.setJsonResponse(jsonResponse);

        return response;
    }
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "notificarRestauranteRequest")
    @ResponsePayload
    public NotificarRestauranteResponse notificarRestaurante( @RequestPayload NotificarRestauranteRequest request) {

        String jsonRequest = request.getJsonRequest();
        System.out.println("JSON request: " + jsonRequest);
        String jsonResponse = service.notificarRestaurante(jsonRequest);
        NotificarRestauranteResponse response = new NotificarRestauranteResponse();
        response.setJsonResponse(jsonResponse);

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "obtenerPromocionesRequest")
    @ResponsePayload
    public ObtenerPromocionesResponse obtenerPromociones() {
        String jsonResponse = service.obtenerPromociones();
        ObtenerPromocionesResponse response = new ObtenerPromocionesResponse();
        response.setJsonResponse(jsonResponse);
        return response;
    }

}
