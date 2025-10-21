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

        @PayloadRoot(namespace = NAMESPACE_URI, localPart =
            "InsertarReservaRequest")
    @ResponsePayload
    public InsertarReservaResponse insertarReserva(@RequestPayload
                                                       InsertarReserva request) {
        ReservaBean reserva = request.getReserva();
        String codigoReserva = service.insertarReserva(reserva);

        InsertarReservaResponse response = new InsertarReservaResponse();
        response.setCodigoReserva(codigoReserva);
        return response;
    }

        @PayloadRoot(namespace = NAMESPACE_URI, localPart =
            "ConsultarDisponibilidadRequest")
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

       @PayloadRoot(namespace = NAMESPACE_URI, localPart =
            "GetInfoRestauranteRequest")
        @ResponsePayload
    public GetInfoRestauranteResponse getRestaurante(@RequestPayload GetInfoRestauranteRequest request) throws JsonProcessingException {

            int id = request.getId();
            RestauranteBean restaurante = service.getRestaurante(id);

            GetInfoRestauranteResponse response = new GetInfoRestauranteResponse();
            response.setInfoRestaurante(restaurante);
            return response;
        }




//    @PayloadRoot(namespace = NAMESPACE_URI, localPart =
//            "ObtenerPaisesRequest")
//    @ResponsePayload
//    public ObtenerPaisesResponse obtenerPaises(@RequestPayload
//                                               ObtenerPaises request) {
//        List<PaisBean> paises = service.obtenerPaises();
//
//        ObtenerPaisesResponse response = new ObtenerPaisesResponse();
//        response.setPaisesResponse(paises);
//        return response;
//    }
//
//    @PayloadRoot(namespace = NAMESPACE_URI, localPart =
//            "ObtenerProvinciasRequest")
//    @ResponsePayload
//    public ObtenerProvinciasResponse obtenerProvincias(@RequestPayload
//                                                       ObtenerProvincias request) {
//        String codPais = request.getCodPais();
//        List<ProvinciaBean> provincias =
//                service.obtenerProvincias(codPais);
//
//        ObtenerProvinciasResponse response = new
//                ObtenerProvinciasResponse();
//        response.setProvinciasResponse(provincias);
//        return response;
//    }
//
//    @PayloadRoot(namespace = NAMESPACE_URI, localPart =
//            "ObtenerLocalidadesRequest")
//    @ResponsePayload
//    public ObtenerLocalidadesResponse obtenerLocalidades(@RequestPayload
//                                                         ObtenerLocalidades request) {
//        LocalidadCriteriaBean criteria = request.getCriteria();
//        List<LocalidadBean> localidades =
//                service.obtenerLocalidades(criteria);
//
//        ObtenerLocalidadesResponse response = new
//                ObtenerLocalidadesResponse();
//        response.setLocalidadesResponse(localidades);
//        return response;
//    }
//
//    @PayloadRoot(namespace = NAMESPACE_URI, localPart =
//            "InsertarLocalidadRequest")
//    @ResponsePayload
//    public InsertarLocalidadResponse insertarLocalidad(@RequestPayload
//                                                       InsertarLocalidad request) {
//        LocalidadBean localidad = request.getLocalidad();
//        LocalidadBean insertedLocalidad =
//                service.insertarLocalidad(localidad);
//
//        InsertarLocalidadResponse response = new
//                InsertarLocalidadResponse();
//        response.setLocalidadResponse(insertedLocalidad);
//        return response;
//    }
}
