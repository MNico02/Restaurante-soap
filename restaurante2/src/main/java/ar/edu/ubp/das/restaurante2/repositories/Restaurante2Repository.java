package ar.edu.ubp.das.restaurante2.repositories;
import ar.edu.ubp.das.restaurante2.beans.*;
import ar.edu.ubp.das.restaurante2.components.SimpleJdbcCallFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class Restaurante2Repository {

    @Autowired
    private SimpleJdbcCallFactory jdbcCallFactory;

    public String insReserva(ReservaBean data) {
        String horaString = data.getHoraReserva();

        // VALIDAR antes de convertir
        if (horaString == null || horaString.trim().isEmpty()) {
            throw new IllegalArgumentException("La hora de reserva no puede ser null o vacía");
        }

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("nro_cliente", null, Types.INTEGER)
                .addValue("apellido", data.getApellido())
                .addValue("nombre", data.getNombre())
                .addValue("correo", data.getCorreo())
                .addValue("telefonos", data.getTelefono())
                .addValue("cod_reserva", null, Types.OTHER)                 // UUID -> OTHER, se ignora en el SP
                .addValue("fecha_reserva", java.sql.Date.valueOf(data.getFechaReserva()), Types.DATE)
                .addValue("hora_reserva",  java.sql.Time.valueOf(data.getHoraReserva()),  Types.TIME) // asegurate que venga "HH:mm:00"
                .addValue("nro_restaurante", 1, Types.INTEGER)
                .addValue("nro_sucursal", data.getIdSucursal(), Types.INTEGER)
                .addValue("cod_zona", data.getCodZona(), Types.INTEGER)
                .addValue("cant_adultos", data.getCantAdultos(), Types.INTEGER)
                .addValue("cant_menores", data.getCantMenores(), Types.INTEGER)
                .addValue("costo_reserva", data.getCostoReserva(), Types.DECIMAL)
                .addValue("cancelada", 0, Types.BIT)
                .addValue("fecha_cancelacion", null, Types.DATE);

        Map<String, Object> out =
                jdbcCallFactory.executeWithOutputs("ins_cliente_reserva_sucursal", "dbo", params);

        @SuppressWarnings("unchecked")
        java.util.List<java.util.Map<String, Object>> rs =
                (java.util.List<java.util.Map<String, Object>>) out.get("#result-set-1");

        if (rs != null && !rs.isEmpty()) {
            Object v = rs.get(0).get("cod_reserva");     // nombre de columna del SELECT del SP
            return (v instanceof java.util.UUID) ? v.toString() : String.valueOf(v);
        }
        return null; // o lanzar excepción si preferís
    }

    public List<HorarioBean> getHorarios(SoliHorarioBean data) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("nro_restaurante", 1, Types.INTEGER)
                .addValue("nro_sucursal", data.getIdSucursal(), Types.INTEGER)
                .addValue("cod_zona", data.getCodZona(), Types.INTEGER)
                .addValue("fecha",java.sql.Date.valueOf(data.getFecha()), Types.DATE)
                .addValue("cant_personas",data.getCantComensales(), Types.INTEGER)
                .addValue("menores",data.isMenores(), Types.BIT);
        return jdbcCallFactory.executeQuery("get_horarios_disponibles", "dbo", params, "", HorarioBean.class);
    }

    public RestauranteBean getInfoRestaurante(int nroRestaurante) throws JsonProcessingException {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("nro_restaurante", nroRestaurante, Types.INTEGER);

        Map<String, Object> out =
                jdbcCallFactory.executeWithOutputs("get_info_restaurante_rs", "dbo", params);

        // 1) Restaurante
        List<Map<String, Object>> rs1 = castRS(out.get("#result-set-1"));
        if (rs1 == null || rs1.isEmpty()) return null;
        Map<String, Object> r1 = rs1.get(0);

        RestauranteBean restaurante = new RestauranteBean();
        restaurante.setNroRestaurante(getInt(r1.get("nro_restaurante")));
        restaurante.setRazonSocial(getStr(r1.get("razon_social")));
        restaurante.setCuit(getStr(r1.get("cuit")));

        // OJO: ya no cargamos contenidos acá
        restaurante.setContenidos(new ArrayList<>());

        // 2) Sucursales
        List<Map<String, Object>> rs2 = castRS(out.get("#result-set-2"));
        Map<Integer, SucursalBean> sucursalesMap = new LinkedHashMap<>();

        if (rs2 != null) {
            for (Map<String, Object> row : rs2) {
                int nroSuc = getInt(row.get("nro_sucursal"));

                SucursalBean s = new SucursalBean();
                s.setNroSucursal(nroSuc);
                s.setNomSucursal(getStr(row.get("nom_sucursal")));
                s.setCalle(getStr(row.get("calle")));
                s.setNroCalle(getStr(row.get("nro_calle")));
                s.setBarrio(getStr(row.get("barrio")));
                s.setNroLocalidad(getInt(row.get("nro_localidad")));
                s.setNomLocalidad(getStr(row.get("nom_localidad")));
                s.setCodProvincia(getInt(row.get("cod_provincia")));
                s.setNomProvincia(getStr(row.get("nom_provincia")));
                s.setCodPostal(getStr(row.get("cod_postal")));
                s.setTelefonos(getStr(row.get("telefonos")));
                s.setTotalComensales(getInt(row.get("total_comensales")));
                s.setMinTolerenciaReserva(getInt(row.get("min_tolerencia_reserva")));
                s.setNroCategoria(getInt(row.get("nro_categoria")));
                s.setCategoriaPrecio(getStr(row.get("categoria_precio")));

                // Colecciones
                s.setContenidos(new ArrayList<>());      // queda vacío, se carga por otro SP
                s.setZonas(new ArrayList<>());
                s.setTurnos(new ArrayList<>());          // IMPORTANTE: tu SucursalBean debe tenerlo
                s.setZonasTurnos(new ArrayList<>());     // idem
                s.setEstilos(new ArrayList<>());
                s.setEspecialidades(new ArrayList<>());
                s.setTiposComidas(new ArrayList<>());

                sucursalesMap.put(nroSuc, s);
            }
        }

        // 3) Zonas por sucursal
        List<Map<String, Object>> rs3 = castRS(out.get("#result-set-3"));
        if (rs3 != null) {
            for (Map<String, Object> row : rs3) {
                int nroSuc = getInt(row.get("nro_sucursal"));

                ZonaBean z = new ZonaBean();
                z.setCodZona(getInt(row.get("cod_zona")));
                z.setNomZona(getStr(row.get("nom_zona")));
                z.setCantComensales(getInt(row.get("cant_comensales")));
                z.setPermiteMenores(getBool(row.get("permite_menores")));
                z.setHabilitada(getBool(row.get("habilitada")));

                SucursalBean s = sucursalesMap.get(nroSuc);
                if (s != null) s.getZonas().add(z);
            }
        }

        // 4) Turnos por sucursal
        List<Map<String, Object>> rs4 = castRS(out.get("#result-set-4"));
        if (rs4 != null) {
            for (Map<String, Object> row : rs4) {
                int nroSuc = getInt(row.get("nro_sucursal"));

                TurnoBean t = new TurnoBean();
                t.setHoraDesde(row.get("hora_reserva") != null ? row.get("hora_reserva").toString() : null);
                t.setHoraHasta(row.get("hora_hasta") != null ? row.get("hora_hasta").toString() : null);
                t.setHabilitado(getBool(row.get("habilitado")));

                SucursalBean s = sucursalesMap.get(nroSuc);
                if (s != null) s.getTurnos().add(t);//prueba
            }
        }

        // 5) Zonas-Turnos por sucursal
        List<Map<String, Object>> rs5 = castRS(out.get("#result-set-5"));
        if (rs5 != null) {
            for (Map<String, Object> row : rs5) {
                int nroSuc = getInt(row.get("nro_sucursal"));

                ZonaTurnoBean zt = new ZonaTurnoBean();
                zt.setCodZona(getInt(row.get("cod_zona")));
                zt.setHoraDesde(row.get("hora_reserva") != null ? row.get("hora_reserva").toString() : null);
                zt.setPermiteMenores(getBool(row.get("permite_menores")));

                SucursalBean s = sucursalesMap.get(nroSuc);
                if (s != null) s.getZonasTurnos().add(zt);
            }
        }

        // 6) Estilos por sucursal
        List<Map<String, Object>> rs6 = castRS(out.get("#result-set-6"));
        if (rs6 != null) {
            for (Map<String, Object> row : rs6) {
                int nroSuc = getInt(row.get("nro_sucursal"));

                EstiloBean e = new EstiloBean();
                e.setNroEstilo(getInt(row.get("nro_estilo")));
                e.setNomEstilo(getStr(row.get("nom_estilo")));
                e.setHabilitado(getBool(row.get("habilitado")));

                SucursalBean s = sucursalesMap.get(nroSuc);
                if (s != null) s.getEstilos().add(e);
            }
        }

        // 7) Especialidades por sucursal
        List<Map<String, Object>> rs7 = castRS(out.get("#result-set-7"));
        if (rs7 != null) {
            for (Map<String, Object> row : rs7) {
                int nroSuc = getInt(row.get("nro_sucursal"));

                EspecialidadBean e = new EspecialidadBean();
                e.setNroRestriccion(getInt(row.get("nro_restriccion")));
                e.setNomRestriccion(getStr(row.get("nom_restriccion")));
                e.setHabilitada(getBool(row.get("habilitada")));

                SucursalBean s = sucursalesMap.get(nroSuc);
                if (s != null) s.getEspecialidades().add(e);
            }
        }

        // 8) Tipos de comidas por sucursal
        List<Map<String, Object>> rs8 = castRS(out.get("#result-set-8"));
        if (rs8 != null) {
            for (Map<String, Object> row : rs8) {
                int nroSuc = getInt(row.get("nro_sucursal"));

                TipoComidaBean tc = new TipoComidaBean();
                tc.setNroTipoComida(getInt(row.get("nro_tipo_comida")));
                tc.setNomTipoComida(getStr(row.get("nom_tipo_comida")));
                tc.setHabilitado(getBool(row.get("habilitado")));

                SucursalBean s = sucursalesMap.get(nroSuc);
                if (s != null) s.getTiposComidas().add(tc);
            }
        }

        restaurante.setSucursales(new ArrayList<>(sucursalesMap.values()));
        return restaurante;
    }


    public ResponseBean modificarReserva(ModificarReservaReqBean data) {

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("cod_reserva_sucursal", data.getCodReservaSucursal(), Types.VARCHAR)
                .addValue("fecha_reserva", java.sql.Date.valueOf(data.getFechaReserva()), Types.DATE)
                .addValue("hora_reserva", java.sql.Time.valueOf(data.getHoraReserva()), Types.TIME)
                .addValue("cod_zona", data.getCodZona(), Types.INTEGER)
                .addValue("cant_adultos", data.getCantAdultos(), Types.INTEGER)
                .addValue("cant_menores", data.getCantMenores(), Types.INTEGER)
                .addValue("costo_reserva",data.getCostoReserva(), Types.DECIMAL);

        try {
            Map<String, Object> out =
                    jdbcCallFactory.executeWithOutputs("modificar_reserva_por_codigo_sucursal", "dbo", params);

            @SuppressWarnings("unchecked")
            java.util.List<java.util.Map<String, Object>> rs =
                    (java.util.List<java.util.Map<String, Object>>) out.get("#result-set-1");

            if (rs == null || rs.isEmpty()) {
                return new ResponseBean(false, "ERROR", "El SP no devolvió resultado (#result-set-1 vacío).");
            }

            java.util.Map<String, Object> row = rs.get(0);

            // success (bit) puede venir como Boolean o Number
            boolean success = false;
            Object vSuccess = row.get("success");
            if (vSuccess instanceof Boolean) success = (Boolean) vSuccess;
            else if (vSuccess instanceof Number) success = ((Number) vSuccess).intValue() == 1;
            else if (vSuccess != null) success = "1".equals(vSuccess.toString()) || "true".equalsIgnoreCase(vSuccess.toString());

            String status = row.get("status") != null ? row.get("status").toString() : null;
            String message = row.get("message") != null ? row.get("message").toString() : null;

            // ✅ devolvemos tal cual el SP
            return new ResponseBean(success, status, message);

        } catch (Exception e) {
            return new ResponseBean(false, "ERROR", e.getMessage());
        }
    }

    public ResponseBean cancelarReservaPorCodigoSucursal(String codReservaSucursal) {

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("cod_reserva_sucursal", codReservaSucursal);

        List<Map<String, Object>> rs = jdbcCallFactory.executeQueryAsMap(
                "cancelar_reserva_por_codigo_sucursal",
                "dbo",
                params,
                "result"
        );

        if (rs.isEmpty()) {
            return new ResponseBean(
                    false,
                    "ERROR",
                    "SP no devolvió resultado."
            );
        }

        Map<String, Object> row = rs.get(0);

        boolean success = false;
        Object vSuccess = row.get("success");
        if (vSuccess instanceof Boolean) success = (Boolean) vSuccess;
        else if (vSuccess instanceof Number)
            success = ((Number) vSuccess).intValue() == 1;
        else if (vSuccess != null)
            success = "1".equals(vSuccess.toString())
                    || "true".equalsIgnoreCase(vSuccess.toString());

        String status = row.get("status") != null
                ? row.get("status").toString()
                : null;

        String message = row.get("message") != null
                ? row.get("message").toString()
                : null;

        return new ResponseBean(success, status, message);
    }

    public ResponseBean insClickLote(List<SoliClickBean> clicks) {

        if (clicks == null || clicks.isEmpty()) {
            return new ResponseBean(false, "ERROR", "No hay clicks para registrar.");
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode clicksArray = mapper.createArrayNode();

            System.out.println("=== PROCESANDO CLICKS ===");
            for (SoliClickBean data : clicks) {
                ContenidoKey key = parseCodContenidoRestauranteSplit(data.getCodContenidoRestaurante());

                System.out.println(String.format(
                        "Click: Rest=%d, Cont=%d, Correo=%s",
                        key.nroRestaurante(),
                        key.nroContenido(),
                        data.getCorreo_cliente()
                ));

                ObjectNode clickNode = mapper.createObjectNode();
                clickNode.put("nro_restaurante", 1);
                clickNode.put("nro_contenido", key.nroContenido());

                // Manejo correcto de null
                if (data.getCorreo_cliente() != null && !data.getCorreo_cliente().trim().isEmpty()) {
                    clickNode.put("correo_cliente", data.getCorreo_cliente());
                } else {
                    clickNode.putNull("correo_cliente");
                }

                clickNode.putNull("fecha_hora_registro");

                clicksArray.add(clickNode);
            }

            String json = mapper.writeValueAsString(clicksArray);

            System.out.println("=== JSON GENERADO ===");
            System.out.println(json);

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("clicks_json", json, Types.NVARCHAR);

            jdbcCallFactory.execute("sp_clicks_contenidos_insertar_lote", "dbo", params);

            return new ResponseBean(
                    true,
                    "SUCCESS",
                    "Se registraron " + clicks.size() + " clicks correctamente."
            );

        } catch (Exception e) {
            System.err.println("ERROR COMPLETO: " + e.getMessage());
            e.printStackTrace();

            return new ResponseBean(
                    false,
                    "ERROR",
                    "Error al registrar clicks: " + e.getMessage()
            );
        }
    }

    /** "123-45" -> (nroContenido=123, nroRestaurante=45) */
    public static ContenidoKey parseCodContenidoRestauranteSplit(String code) {
        if (code == null) throw new IllegalArgumentException("code null");
        String[] parts = code.trim().split("-", 2); // límite 2
        if (parts.length != 2) {
            throw new IllegalArgumentException("Formato inválido: esperado 'contenido-restaurante'. Recibido: " + code);
        }
        int nroContenido   = Integer.parseInt(parts[1].trim());
        int nroRestaurante = Integer.parseInt(parts[0].trim());
        return new ContenidoKey(nroContenido, nroRestaurante);
    }
    public record ContenidoKey(int nroContenido, int nroRestaurante) {}

    public List<ContenidoBean> getPromociones(int nroRestaurante) {
        //System.out.println(">>> REPO nro_restaurante = [" + nroRestaurante + "]");
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("nro_restaurante", 1, Types.INTEGER);

        Map<String, Object> out =
                jdbcCallFactory.executeWithOutputs(
                        "get_contenidos",
                        "dbo",
                        params
                );

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> rs =
                (List<Map<String, Object>>) out.get("#result-set-1");

        List<ContenidoBean> contenidos = new ArrayList<>();

        if (rs != null) {
            for (Map<String, Object> row : rs) {
                ContenidoBean c = new ContenidoBean();
                c.setNroSucursal(getIntObj(row.get("nro_sucursal")));
                c.setNroContenido(getInt(row.get("nro_contenido")));
                c.setContenidoAPublicar(getStr(row.get("contenido_a_publicar")));
                c.setImagenAPublicar(getStr(row.get("imagen_a_publicar")));
                c.setPublicado(false);
                c.setCostoClick(getBigDec(row.get("costo_click")));
                contenidos.add(c);
            }
        }

        return contenidos;
    }

    public UpdPublicarContenidosRespBean notificarContenidos(NotiRestReqBean req){
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("nro_restaurante", req.getNroRestaurante(), Types.INTEGER)
                .addValue("costo_click",req.getCostoAplicado(),Types.DECIMAL)
                .addValue("nro_contenidos",req.getNroContenidos(),Types.VARCHAR);

        List<UpdPublicarContenidosRespBean> result =
                jdbcCallFactory.executeQuery(
                        "upd_publicar_contenidos_lote",
                        "dbo",
                        params,
                        "resultado",   // nombre del ResultSet
                        UpdPublicarContenidosRespBean.class
                );

        return result.isEmpty() ? null : result.get(0);

    }




    // --------- helpers de mapeo seguros ---------

    @SuppressWarnings("unchecked")
    private static List<Map<String, Object>> castRS(Object o) {
        return (o instanceof List) ? (List<Map<String, Object>>) o : null;
    }

    private static String getStr(Object o) {
        return (o == null) ? null : o.toString();
    }

    private static int getInt(Object o) {
        if (o == null) return 0;
        if (o instanceof Integer) return (Integer) o;
        if (o instanceof Long) return ((Long) o).intValue();
        if (o instanceof Short) return ((Short) o).intValue();
        if (o instanceof BigDecimal) return ((BigDecimal) o).intValue();
        return Integer.parseInt(o.toString());
    }

    private static Integer getIntObj(Object o) {
        return (o == null) ? null : getInt(o);
    }

    private static boolean getBool(Object o) {
        if (o == null) return false;
        if (o instanceof Boolean) return (Boolean) o;
        if (o instanceof Number) return ((Number) o).intValue() != 0;
        return Boolean.parseBoolean(o.toString());
    }

    private static BigDecimal getBigDec(Object o) {
        if (o == null) return null;
        if (o instanceof BigDecimal) return (BigDecimal) o;
        if (o instanceof Number) return BigDecimal.valueOf(((Number) o).doubleValue());
        try { return new BigDecimal(o.toString()); } catch (Exception e) { return null; }
    }

    private ContenidoBean mapContenido(Map<String, Object> row, Integer nroSucursal) {
        ContenidoBean c = new ContenidoBean();
        c.setNroSucursal(nroSucursal);
        c.setNroContenido(getInt(row.get("nro_contenido")));
        c.setContenidoAPublicar(getStr(row.get("contenido_a_publicar")));
        c.setImagenAPublicar(getStr(row.get("imagen_a_publicar")));
        c.setPublicado(getBool(row.get("publicado")));
        c.setCostoClick(getBigDec(row.get("costo_click")));
        return c;
    }


}
