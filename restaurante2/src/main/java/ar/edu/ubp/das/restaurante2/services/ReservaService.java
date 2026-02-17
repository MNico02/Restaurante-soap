//package ar.edu.ubp.das.restaurante2.services;
//
//
//
//import ar.edu.ubp.das.restaurante2.beans.*;
//import ar.edu.ubp.das.restaurante2.repositories.Restaurante2Repository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//public class ReservaService {
//    @Autowired
//    private Restaurante2Repository restaurante2Repository;
//
//    public ConfirmarReservaResponse confirmarReserva(ReservaRestauranteBean req) {
//
//        ConfirmarReservaResponse resp = new ConfirmarReservaResponse();
//
//        if (req == null || req.getSolicitudCliente() == null || req.getReserva() == null) {
//            resp.setSuccess(false);
//            resp.setEstado("RECHAZADA");
//            resp.setMensaje("Solicitud inválida: faltan datos de cliente o reserva.");
//            return resp;
//        }
//
//        SolicitudClienteBean cli = req.getSolicitudCliente();
//        ReservaSolicitudBean sol = req.getReserva(); // lo que llega desde Ristorino
//
//        // Validaciones mínimas (lo fuerte lo valida el SP)
//        if (sol.getIdSucursal() <= 0 || sol.getCodZona() <= 0
//                || sol.getFechaReserva() == null || sol.getHoraReserva() == null) {
//            resp.setSuccess(false);
//            resp.setEstado("RECHAZADA");
//            resp.setMensaje("Datos de reserva incompletos o inválidos.");
//            return resp;
//        }
//
//        String correo = firstNonBlank(sol.getCorreo(), cli.getCorreo());
//        if (correo.isBlank()) {
//            resp.setSuccess(false);
//            resp.setEstado("RECHAZADA");
//            resp.setMensaje("El correo es obligatorio.");
//            return resp;
//        }
//
//        // Mapear ReservaSolicitudBean -> ReservaBean (la que usa el repo/SP)
//        ReservaBean reserva = new ReservaBean();
//        reserva.setNombre(cli.getNombre());
//        reserva.setApellido(cli.getApellido());
//        reserva.setTelefono(cli.getTelefonos());
//        reserva.setCorreo(correo);
//
//        reserva.setIdSucursal(sol.getIdSucursal());
//        reserva.setFechaReserva(sol.getFechaReserva());
//        reserva.setHoraReserva(sol.getHoraReserva());
//        reserva.setCantAdultos(sol.getCantAdultos());
//        reserva.setCantMenores(sol.getCantMenores());
//        reserva.setCodZona(sol.getCodZona());
//        reserva.setCostoReserva(sol.getCostoReserva());
//
//
//        try {
//            String codReserva = restaurante2Repository.insReserva(reserva);
//
//            if (codReserva == null || codReserva.isBlank()) {
//                resp.setSuccess(false);
//                resp.setEstado("RECHAZADA");
//                resp.setMensaje("No se pudo confirmar la reserva.");
//                return resp;
//            }
//
//            resp.setSuccess(true);
//            resp.setEstado("CONFIRMADA");
//            resp.setMensaje("Reserva confirmada.");
//            resp.setCodReserva(codReserva);
//            return resp;
//
//        } catch (Exception e) {
//            //log.error("Error confirmando reserva: {}", e.getMessage(), e);
//            resp.setSuccess(false);
//            resp.setEstado("RECHAZADA");
//            resp.setMensaje(e.getMessage());
//            return resp;
//        }
//
//    }
//
//    private String firstNonBlank(String a, String b) {
//        String x = (a == null) ? "" : a.trim();
//        if (!x.isBlank()) return x;
//        String y = (b == null) ? "" : b.trim();
//        return y;
//    }
//}
