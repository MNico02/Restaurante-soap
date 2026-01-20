package ar.edu.ubp.das.restaurante2.endpoints;
import ar.edu.ubp.das.restaurante2.beans.*;
import ar.edu.ubp.das.restaurante2.services.Restaurante2;
import ar.edu.ubp.das.restaurante2.services.jaxws.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.ArrayList;
import java.util.List;

@Endpoint
public class Restaurante2Endpoint {
    private static final String NAMESPACE_URI =
            "http://services.restaurante2.das.ubp.edu.ar/";

    @Autowired
    private Restaurante2 service;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "confirmarReservaRequest")
    @ResponsePayload
    public ConfirmarReservaResponse confirmarReserva(@RequestPayload ConfirmarReserva request) {
        ReservaBean reserva = request.getReserva();
        String codigoReserva = service.confirmarReserva(reserva);
        ConfirmarReservaResponse response = new ConfirmarReservaResponse();
        response.setCodigoReserva(codigoReserva);

        return response;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ConsultarDisponibilidadRequest")
    @ResponsePayload
    public ConsultarDisponibilidadResponse consultarDisponibilidad(@RequestPayload ConsultarDisponibilidad request) {
        SoliHorarioBean soliHorarioBean = request.getSoliHorario();
            System.out.println(">>> solicitar disponibilidad - soliHorarioBean = " + soliHorarioBean);
        List<HorarioBean> horarios = service.obtenerHorarios(soliHorarioBean);

            System.out.println(">>> obtenerLocalidades devolvió horarios = " + horarios);
            System.out.println(">>> size = " + (horarios == null ? "null" : horarios.size()));

        ConsultarDisponibilidadResponse response = new ConsultarDisponibilidadResponse();
            if (horarios == null) {
                response.setHorariosResponse(new ArrayList<HorarioBean>());
            } else {
                response.setHorariosResponse(horarios);
            }
        return response;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetInfoRestauranteRequest")
    @ResponsePayload
    public GetInfoRestauranteResponse getRestaurante(@RequestPayload GetInfoRestauranteRequest request) throws JsonProcessingException {

            int id = request.getId();
            RestauranteBean restaurante = service.getRestaurante(id);

            GetInfoRestauranteResponse response = new GetInfoRestauranteResponse();
            response.setInfoRestaurante(restaurante);
            return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ModificarReservaRequest")
    @ResponsePayload
    public ModificarReservaResponse modificarReserva(@RequestPayload ModificarReserva request) {

        ModificarReservaReqBean req = request.getModificarReserva();
        ResponseBean resp = service.modificarReserva(req);
        ModificarReservaResponse response = new ModificarReservaResponse();
        response.setResponse(resp);

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "cancelarReservaRequest")
    @ResponsePayload
    public CancelarReservaResponse cancelarReserva(@RequestPayload CancelarReserva request) {

        CancelarReservaReqBean req = request.getCancelarReserva();
        ResponseBean resp = service.cancelarReserva(req);
        CancelarReservaResponse response = new CancelarReservaResponse();
        response.setResponse(resp);

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "registrarClicksRequest")
    @ResponsePayload
    public RegistrarClicksResponse registrarClicks(@RequestPayload RegistrarClicks request) {

        List<SoliClickBean> clicks = request.getClicks();
        ResponseBean resp = service.registrarClicks(clicks);
        RegistrarClicksResponse response = new RegistrarClicksResponse();
        response.setResponse(resp);

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "obtenerPromocionesRequest")
    @ResponsePayload
    public ObtenerPromocionesResponse obtenerPromociones(@RequestPayload ObtenerPromociones request) {

        ObtenerPromocionesReqBean req = request.getObtenerPromociones();
        List<ContenidoBean> promociones = service.obtenerPromociones(req);
        ObtenerPromocionesResponse response = new ObtenerPromocionesResponse();
        response.setPromociones(promociones);

        return response;
    }

}
