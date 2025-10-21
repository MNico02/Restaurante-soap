package ar.edu.ubp.das.restaurante2.repositories;
import ar.edu.ubp.das.restaurante2.beans.*;
import ar.edu.ubp.das.restaurante2.components.SimpleJdbcCallFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
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
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("nro_cliente", null, Types.INTEGER)
                .addValue("apellido", data.getApellido())
                .addValue("nombre", data.getNombre())
                .addValue("correo", data.getCorreo())
                .addValue("telefonos", data.getTelefono())
                .addValue("cod_reserva", null, Types.OTHER)                 // UUID -> OTHER, se ignora en el SP
                .addValue("fecha_reserva", java.sql.Date.valueOf(data.getFechaReserva()), Types.DATE)
                .addValue("hora_reserva",  java.sql.Time.valueOf(data.getHoraReserva()),  Types.TIME) // asegurate que venga "HH:mm:00"
                .addValue("nro_restaurante", 2, Types.INTEGER)
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
                .addValue("nro_restaurante", 2, Types.INTEGER)
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

        // 2) Contenidos generales del restaurante
        List<Map<String, Object>> rs2 = castRS(out.get("#result-set-2"));
        List<ContenidoBean> contenidosRest = new ArrayList<>();
        if (rs2 != null) {
            for (Map<String, Object> row : rs2) {
                contenidosRest.add(mapContenido(row, null));
            }
        }
        restaurante.setContenidos(contenidosRest);

        // 3) Sucursales
        List<Map<String, Object>> rs3 = castRS(out.get("#result-set-3"));
        Map<Integer, SucursalBean> sucursalesMap = new LinkedHashMap<>();
        if (rs3 != null) {
            for (Map<String, Object> row : rs3) {
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
                s.setContenidos(new ArrayList<>());
                s.setZonas(new ArrayList<>());
                s.setEstilos(new ArrayList<>());
                s.setEspecialidades(new ArrayList<>());
                s.setTiposComidas(new ArrayList<>());
                sucursalesMap.put(nroSuc, s);
            }
        }

        // 4) Contenidos por sucursal
        List<Map<String, Object>> rs4 = castRS(out.get("#result-set-4"));
        if (rs4 != null) {
            for (Map<String, Object> row : rs4) {
                Integer nroSuc = getIntObj(row.get("nro_sucursal"));
                ContenidoBean c = mapContenido(row, nroSuc);
                SucursalBean s = sucursalesMap.get(nroSuc);
                if (s != null) s.getContenidos().add(c);
            }
        }

        // 5) Zonas por sucursal
        List<Map<String, Object>> rs5 = castRS(out.get("#result-set-5"));
        if (rs5 != null) {
            for (Map<String, Object> row : rs5) {
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
