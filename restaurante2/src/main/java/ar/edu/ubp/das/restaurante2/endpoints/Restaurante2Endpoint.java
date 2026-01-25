package ar.edu.ubp.das.restaurante2.endpoints;
import ar.edu.ubp.das.restaurante2.beans.*;
import ar.edu.ubp.das.restaurante2.services.ReservaService;
import ar.edu.ubp.das.restaurante2.services.Restaurante2;
import ar.edu.ubp.das.restaurante2.services.jaxws.*;
import ar.edu.ubp.das.restaurante2.services.jaxws.ConfirmarReservaResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    private ReservaService reservaService;


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "confirmarReservaRequest")
    @ResponsePayload
    public ConfirmarReservaResponse confirmarReserva(@RequestPayload ConfirmarReservaRequest request) {

        ReservaRestauranteBean req = request.getReservaRestaurante();
        ConfirmarReservaResp resp = reservaService.confirmarReserva(req);
       ConfirmarReservaResponse response = new ConfirmarReservaResponse();
       response.setReservaResponse(resp);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ConsultarDisponibilidadRequest")
    @ResponsePayload
    public ObtenerHorariosResponse consultarDisponibilidad(@RequestPayload ObtenerHorarios request) {
        SoliHorarioBean soliHorarioBean = request.getSoliHorario();

        /*System.out.println("=================================");
        System.out.println("ID SUCURSAL     = " + soliHorarioBean.getIdSucursal());
        System.out.println("COD ZONA        = " + soliHorarioBean.getCodZona());
        System.out.println("FECHA           = " + soliHorarioBean.getFecha());
        System.out.println("CANT COMENSALES = " + soliHorarioBean.getCantComensales());
        System.out.println("MENORES         = " + soliHorarioBean.isMenores());
        System.out.println("=================================");*/
        List<HorarioBean> horarios = service.obtenerHorarios(soliHorarioBean);

            /*System.out.println(">>> obtenerLocalidades devolvió horarios = " + horarios);
            System.out.println(">>> size = " + (horarios == null ? "null" : horarios.size()));*/

        ObtenerHorariosResponse response = new ObtenerHorariosResponse();
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
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "notificarRestauranteRequest")
    @ResponsePayload
    public NotificarRestauranteResponse notificarRestaurante(
            @RequestPayload NotificarRestauranteRequest request) {

        NotiRestReqBean data = request.getNotiRestReqBean();

        UpdPublicarContenidosRespBean result =
                service.notificarRestaurante(data);

        NotificarRestauranteResponse response =
                new NotificarRestauranteResponse();
        response.setNotificacion(result);

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
