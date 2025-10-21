/* ============================================================
   RESET: eliminar tablas hijas -> padres (evita errores de FK)
   ============================================================*/
IF OBJECT_ID('dbo.reservas_sucursales','U') IS NOT NULL DROP TABLE dbo.reservas_sucursales;
IF OBJECT_ID('dbo.estilos_sucursales','U') IS NOT NULL DROP TABLE dbo.estilos_sucursales;
IF OBJECT_ID('dbo.especialidades_alimentarias_sucursales','U') IS NOT NULL DROP TABLE dbo.especialidades_alimentarias_sucursales;
IF OBJECT_ID('dbo.tipos_comidas_sucursales','U') IS NOT NULL DROP TABLE dbo.tipos_comidas_sucursales;
IF OBJECT_ID('dbo.zonas_turnos_sucursales','U') IS NOT NULL DROP TABLE dbo.zonas_turnos_sucursales;
IF OBJECT_ID('dbo.turnos_sucursales','U') IS NOT NULL DROP TABLE dbo.turnos_sucursales;
IF OBJECT_ID('dbo.zonas_sucursales','U') IS NOT NULL DROP TABLE dbo.zonas_sucursales;
IF OBJECT_ID('dbo.clicks_contenidos','U') IS NOT NULL DROP TABLE dbo.clicks_contenidos;

IF OBJECT_ID('dbo.contenidos','U') IS NOT NULL DROP TABLE dbo.contenidos;
IF OBJECT_ID('dbo.sucursales','U') IS NOT NULL DROP TABLE dbo.sucursales;

IF OBJECT_ID('dbo.categorias_precios','U') IS NOT NULL DROP TABLE dbo.categorias_precios;
IF OBJECT_ID('dbo.estilos','U') IS NOT NULL DROP TABLE dbo.estilos;
IF OBJECT_ID('dbo.especialidades_alimentarias','U') IS NOT NULL DROP TABLE dbo.especialidades_alimentarias;
IF OBJECT_ID('dbo.tipos_comidas','U') IS NOT NULL DROP TABLE dbo.tipos_comidas;
IF OBJECT_ID('dbo.zonas','U') IS NOT NULL DROP TABLE dbo.zonas;

IF OBJECT_ID('dbo.clientes','U') IS NOT NULL DROP TABLE dbo.clientes;
IF OBJECT_ID('dbo.restaurantes','U') IS NOT NULL DROP TABLE dbo.restaurantes;
IF OBJECT_ID('dbo.localidades','U') IS NOT NULL DROP TABLE dbo.localidades;
IF OBJECT_ID('dbo.provincias','U') IS NOT NULL DROP TABLE dbo.provincias;
GO


/* =======================
   DIMENSIONES / LOOKUPS
   =======================*/
CREATE TABLE dbo.provincias(
                               cod_provincia   INT          NOT NULL,
                               nom_provincia   NVARCHAR(80) NOT NULL,
                               CONSTRAINT PK_provincias PRIMARY KEY (cod_provincia),
                               CONSTRAINT AK_provincias_nom UNIQUE (nom_provincia)
);
GO

CREATE TABLE dbo.localidades(
                                nro_localidad   INT           NOT NULL,
                                nom_localidad   NVARCHAR(120) NOT NULL,
                                cod_provincia   INT           NOT NULL,
                                CONSTRAINT PK_localidades PRIMARY KEY (nro_localidad),
                                CONSTRAINT AK_localidades_prov_nom UNIQUE (cod_provincia, nom_localidad),
                                CONSTRAINT FK_localidades_provincias
                                    FOREIGN KEY (cod_provincia) REFERENCES dbo.provincias(cod_provincia)
);
GO

CREATE TABLE dbo.restaurantes(
                                 nro_restaurante INT           NOT NULL,
                                 razon_social    NVARCHAR(160) NOT NULL,
                                 cuit            VARCHAR(13)   NOT NULL,  -- admite con guiones
                                 CONSTRAINT PK_restaurantes PRIMARY KEY (nro_restaurante),
                                 CONSTRAINT AK_restaurantes_cuit UNIQUE (cuit)
);
GO

CREATE TABLE dbo.categorias_precios(
                                       nro_categoria INT NOT NULL,
                                       nom_categoria NVARCHAR(60) NOT NULL,
                                       CONSTRAINT PK_categorias_precios PRIMARY KEY (nro_categoria),
                                       CONSTRAINT AK_categorias_precios_nom UNIQUE (nom_categoria)
);
GO

CREATE TABLE dbo.zonas(
                          cod_zona   INT NOT NULL,
                          nom_zona   NVARCHAR(80) NOT NULL,
                          CONSTRAINT PK_zonas PRIMARY KEY (cod_zona),
                          CONSTRAINT AK_zonas_nom UNIQUE (nom_zona)
);
GO

CREATE TABLE dbo.tipos_comidas(
                                  nro_tipo_comida INT NOT NULL,
                                  nom_tipo_comida NVARCHAR(80) NOT NULL,
                                  CONSTRAINT PK_tipos_comidas PRIMARY KEY (nro_tipo_comida),
                                  CONSTRAINT AK_tipos_comidas_nom UNIQUE (nom_tipo_comida)
);
GO

CREATE TABLE dbo.especialidades_alimentarias(
                                                nro_restriccion INT NOT NULL,
                                                nom_restriccion NVARCHAR(80) NOT NULL,
                                                CONSTRAINT PK_especialidades_alimentarias PRIMARY KEY (nro_restriccion),
                                                CONSTRAINT AK_especialidades_alimentarias_nom UNIQUE (nom_restriccion)
);
GO

CREATE TABLE dbo.estilos(
                            nro_estilo INT NOT NULL,
                            nom_estilo NVARCHAR(80) NOT NULL,
                            CONSTRAINT PK_estilos PRIMARY KEY (nro_estilo),
                            CONSTRAINT AK_estilos_nom UNIQUE (nom_estilo)
);
GO

/* =======================
   OPERATIVO
   =======================*/
CREATE TABLE dbo.sucursales(
                               nro_restaurante           INT           NOT NULL,
                               nro_sucursal              INT           NOT NULL,
                               nom_sucursal              NVARCHAR(120) NOT NULL,
                               calle                     NVARCHAR(120) NOT NULL,
                               nro_calle                 NVARCHAR(10)  NOT NULL,
                               barrio                    NVARCHAR(120) NULL,
                               nro_localidad             INT           NOT NULL,
                               cod_postal                NVARCHAR(10)  NULL,
                               telefonos                 NVARCHAR(80)  NULL,
                               total_comensales          INT           NOT NULL,
                               min_tolerencia_reserva    INT           NOT NULL, -- minutos
                               nro_categoria             INT           NOT NULL,
                               CONSTRAINT PK_sucursales PRIMARY KEY (nro_restaurante, nro_sucursal),
                               CONSTRAINT AK_sucursales_nombre UNIQUE (nro_restaurante, nom_sucursal),
                               CONSTRAINT FK_sucursales_restaurantes
                                   FOREIGN KEY (nro_restaurante) REFERENCES dbo.restaurantes(nro_restaurante),
                               CONSTRAINT FK_sucursales_localidades
                                   FOREIGN KEY (nro_localidad) REFERENCES dbo.localidades(nro_localidad),
                               CONSTRAINT FK_sucursales_categorias_precios
                                   FOREIGN KEY (nro_categoria) REFERENCES dbo.categorias_precios(nro_categoria),
                               CONSTRAINT CK_sucursales_total_comensales CHECK (total_comensales >= 0),
                               CONSTRAINT CK_sucursales_tolerancia_min CHECK (min_tolerencia_reserva >= 0)
);
GO

CREATE TABLE dbo.zonas_sucursales(
                                     nro_restaurante INT NOT NULL,
                                     nro_sucursal    INT NOT NULL,
                                     cod_zona        INT NOT NULL,
                                     cant_comensales INT NOT NULL,
                                     permite_menores BIT NOT NULL CONSTRAINT DF_zs_perm_menores DEFAULT(1),
                                     habilitada      BIT NOT NULL CONSTRAINT DF_zs_habilitada DEFAULT(1),
                                     CONSTRAINT PK_zonas_sucursales PRIMARY KEY (nro_restaurante, nro_sucursal, cod_zona),
                                     CONSTRAINT FK_zs_sucursales
                                         FOREIGN KEY (nro_restaurante, nro_sucursal)
                                             REFERENCES dbo.sucursales(nro_restaurante, nro_sucursal),
                                     CONSTRAINT FK_zs_zonas
                                         FOREIGN KEY (cod_zona) REFERENCES dbo.zonas(cod_zona),
                                     CONSTRAINT CK_zs_cant_comensales CHECK (cant_comensales >= 0)
);
GO

CREATE TABLE dbo.turnos_sucursales(
                                      nro_restaurante INT      NOT NULL,
                                      nro_sucursal    INT      NOT NULL,
                                      hora_reserva    TIME(0)  NOT NULL,
                                      hora_hasta      TIME(0)  NOT NULL,
                                      habilitado      BIT      NOT NULL CONSTRAINT DF_ts_habilitado DEFAULT(1),
                                      CONSTRAINT PK_turnos_sucursales PRIMARY KEY (nro_restaurante, nro_sucursal, hora_reserva),
                                      CONSTRAINT FK_ts_sucursales
                                          FOREIGN KEY (nro_restaurante, nro_sucursal)
                                              REFERENCES dbo.sucursales(nro_restaurante, nro_sucursal),
                                      CONSTRAINT CK_ts_rango_horario CHECK (hora_hasta > hora_reserva)
);
GO

CREATE TABLE dbo.zonas_turnos_sucursales(
                                            nro_restaurante INT     NOT NULL,
                                            nro_sucursal    INT     NOT NULL,
                                            cod_zona        INT     NOT NULL,
                                            hora_reserva      TIME(0) NOT NULL,
                                            permite_menores BIT     NOT NULL CONSTRAINT DF_zts_perm_menores DEFAULT(1),
                                            CONSTRAINT PK_zonas_turnos_sucursales PRIMARY KEY (nro_restaurante, nro_sucursal, cod_zona, hora_reserva),
    -- FK a la zona de la sucursal
                                            CONSTRAINT FK_zts_zonas_sucursales
                                                FOREIGN KEY (nro_restaurante, nro_sucursal, cod_zona)
                                                    REFERENCES dbo.zonas_sucursales(nro_restaurante, nro_sucursal, cod_zona),
    -- FK al turno de la sucursal
                                            CONSTRAINT FK_zts_turnos_sucursales
                                                FOREIGN KEY (nro_restaurante, nro_sucursal,hora_reserva)
                                                    REFERENCES dbo.turnos_sucursales(nro_restaurante, nro_sucursal, hora_reserva)
);
GO

CREATE TABLE dbo.contenidos(
                               nro_restaurante     INT            NOT NULL,
                               nro_contenido       INT            NOT NULL,
                               contenido_a_publicar NVARCHAR(500) NOT NULL,
                               imagen_a_publicar   NVARCHAR(500)  NULL,
                               publicado           BIT            NOT NULL CONSTRAINT DF_cont_publicado DEFAULT(0),
                               costo_click         DECIMAL(12,2)  NULL,
                               nro_sucursal        INT            NULL, -- puede ser contenido general del restaurante
                               CONSTRAINT PK_contenidos PRIMARY KEY (nro_restaurante, nro_contenido),
                               CONSTRAINT FK_contenidos_restaurantes
                                   FOREIGN KEY (nro_restaurante) REFERENCES dbo.restaurantes(nro_restaurante),
                               CONSTRAINT FK_contenidos_sucursales
                                   FOREIGN KEY (nro_restaurante, nro_sucursal)
                                       REFERENCES dbo.sucursales(nro_restaurante, nro_sucursal)
);
GO

CREATE TABLE dbo.tipos_comidas_sucursales(
                                             nro_restaurante  INT NOT NULL,
                                             nro_sucursal     INT NOT NULL,
                                             nro_tipo_comida  INT NOT NULL,
                                             habilitado       BIT NOT NULL CONSTRAINT DF_tcs_habilitado DEFAULT(1),
                                             CONSTRAINT PK_tipos_comidas_sucursales PRIMARY KEY (nro_restaurante, nro_sucursal, nro_tipo_comida),
                                             CONSTRAINT FK_tcs_sucursales
                                                 FOREIGN KEY (nro_restaurante, nro_sucursal)
                                                     REFERENCES dbo.sucursales(nro_restaurante, nro_sucursal),
                                             CONSTRAINT FK_tcs_tipos_comidas
                                                 FOREIGN KEY (nro_tipo_comida) REFERENCES dbo.tipos_comidas(nro_tipo_comida)
);
GO

CREATE TABLE dbo.especialidades_alimentarias_sucursales(
                                                           nro_restaurante  INT NOT NULL,
                                                           nro_sucursal     INT NOT NULL,
                                                           nro_restriccion  INT NOT NULL,
                                                           habilitada       BIT NOT NULL CONSTRAINT DF_eas_habilitada DEFAULT(1),
                                                           CONSTRAINT PK_especialidades_alimentarias_sucursales PRIMARY KEY (nro_restaurante, nro_sucursal, nro_restriccion),
                                                           CONSTRAINT FK_eas_sucursales
                                                               FOREIGN KEY (nro_restaurante, nro_sucursal)
                                                                   REFERENCES dbo.sucursales(nro_restaurante, nro_sucursal),
                                                           CONSTRAINT FK_eas_especialidades
                                                               FOREIGN KEY (nro_restriccion) REFERENCES dbo.especialidades_alimentarias(nro_restriccion)
);
GO

CREATE TABLE dbo.estilos_sucursales(
                                       nro_restaurante INT NOT NULL,
                                       nro_sucursal    INT NOT NULL,
                                       nro_estilo      INT NOT NULL,
                                       habilitado      BIT NOT NULL CONSTRAINT DF_es_habilitado DEFAULT(1),
                                       CONSTRAINT PK_estilos_sucursales PRIMARY KEY (nro_restaurante, nro_sucursal, nro_estilo),
                                       CONSTRAINT FK_es_sucursales
                                           FOREIGN KEY (nro_restaurante, nro_sucursal)
                                               REFERENCES dbo.sucursales(nro_restaurante, nro_sucursal),
                                       CONSTRAINT FK_es_estilos
                                           FOREIGN KEY (nro_estilo) REFERENCES dbo.estilos(nro_estilo)
);
GO

CREATE TABLE dbo.clientes(
                             nro_cliente INT IDENTITY(1,1) NOT NULL,
                             apellido    NVARCHAR(80)  NOT NULL,
                             nombre      NVARCHAR(80)  NOT NULL,
                             correo      NVARCHAR(160) NOT NULL,
                             telefonos   NVARCHAR(80)  NULL,
                             CONSTRAINT PK_clientes PRIMARY KEY (nro_cliente),
                             CONSTRAINT AK_clientes_correo UNIQUE (correo)
);
GO

CREATE TABLE dbo.reservas_sucursales(
                                        cod_reserva      UNIQUEIDENTIFIER NOT NULL DEFAULT NEWSEQUENTIALID(),
                                        nro_cliente      INT      NOT NULL,
                                        fecha_reserva    DATE     NOT NULL,
                                        nro_restaurante  INT      NOT NULL,
                                        nro_sucursal     INT      NOT NULL,
                                        cod_zona         INT      NOT NULL,
                                        hora_reserva      TIME(0)  NOT NULL,
                                        cant_adultos     INT      NOT NULL,
                                        cant_menores     INT      NOT NULL,
                                        costo_reserva    DECIMAL(12,2) NOT NULL DEFAULT(0),
                                        cancelada        BIT      NOT NULL DEFAULT(0),
                                        fecha_cancelacion DATE    NULL,
                                        CONSTRAINT PK_reservas_sucursales PRIMARY KEY (cod_reserva),
                                        CONSTRAINT FK_rs_clientes
                                            FOREIGN KEY (nro_cliente) REFERENCES dbo.clientes(nro_cliente),
                                        CONSTRAINT FK_rs_sucursales
                                            FOREIGN KEY (nro_restaurante, nro_sucursal)
                                                REFERENCES dbo.sucursales(nro_restaurante, nro_sucursal),
    -- Asegura zona v?lida para esa sucursal
                                        CONSTRAINT FK_rs_zonas_sucursales
                                            FOREIGN KEY (nro_restaurante, nro_sucursal, cod_zona)
                                                REFERENCES dbo.zonas_sucursales(nro_restaurante, nro_sucursal, cod_zona),
    -- Asegura turno v?lido para esa sucursal
                                        CONSTRAINT FK_rs_turnos_sucursales
                                            FOREIGN KEY (nro_restaurante, nro_sucursal, hora_reserva)
                                                REFERENCES dbo.turnos_sucursales(nro_restaurante, nro_sucursal, hora_reserva),
                                        CONSTRAINT CK_rs_cantidades CHECK (cant_adultos >= 0 AND cant_menores >= 0 AND (cant_adultos + cant_menores) > 0),
                                        CONSTRAINT CK_rs_cancelacion
                                            CHECK ( (cancelada = 1 AND fecha_cancelacion IS NOT NULL) OR
                                                    (cancelada = 0 AND fecha_cancelacion IS NULL) )
);
GO

CREATE OR ALTER TRIGGER TR_zonas_sucursales_validar_capacidad
ON dbo.zonas_sucursales
AFTER INSERT, UPDATE
                                  AS
BEGIN
    SET NOCOUNT ON;

    IF EXISTS (
        SELECT 1
        FROM (
            SELECT i.nro_restaurante, i.nro_sucursal
            FROM inserted i
            UNION
            SELECT d.nro_restaurante, d.nro_sucursal
            FROM deleted d
        ) AS afectados
        JOIN dbo.sucursales s
          ON s.nro_restaurante = afectados.nro_restaurante
         AND s.nro_sucursal    = afectados.nro_sucursal
        CROSS APPLY (
            SELECT SUM(zs.cant_comensales) AS suma_zonas
            FROM dbo.zonas_sucursales zs
            WHERE zs.nro_restaurante = afectados.nro_restaurante
              AND zs.nro_sucursal    = afectados.nro_sucursal
        ) x
        WHERE x.suma_zonas > s.total_comensales
    )
BEGIN
ROLLBACK;
THROW 51001, 'La suma de cant_comensales supera el total_comensales definido en sucursales.', 1;
END
END;
GO



INSERT INTO dbo.provincias (cod_provincia, nom_provincia) VALUES
(14, N'C�rdoba');

INSERT INTO dbo.localidades (nro_localidad, nom_localidad, cod_provincia) VALUES
                                                                              (1001, N'Guemes', 14),
                                                                              (1002, N'General Paz', 14),
                                                                              (1003, N'Nueva C�rdoba', 14),
                                                                              (1004, N'Centro', 14);

/* =======================
   Categor�as de precios
   =======================*/
INSERT INTO dbo.categorias_precios (nro_categoria, nom_categoria) VALUES
                                                                      (1, N'Baja'), (2, N'Media'), (3, N'Alta');

/* =======================
   Zonas disponibles
   =======================*/
INSERT INTO dbo.zonas (cod_zona, nom_zona) VALUES
                                               (1, N'Sal�n'), (2, N'Terraza'), (3, N'Patio');

/* =======================
   Tipos de comidas (agrego la nueva)
   =======================*/
INSERT INTO dbo.tipos_comidas (nro_tipo_comida, nom_tipo_comida) VALUES
    (2, N'Fusi�n japonesa-peruana');


/* =======================
   Restaurante Perukai
   =======================*/
INSERT INTO dbo.restaurantes (nro_restaurante, razon_social, cuit) VALUES
    (2, N'Perukai', '30-98765432-1');


/* =======================
   Sucursales (4 en total)
   =======================*/
INSERT INTO dbo.sucursales
(nro_restaurante, nro_sucursal, nom_sucursal, calle, nro_calle, barrio, nro_localidad, cod_postal, telefonos,
 total_comensales, min_tolerencia_reserva, nro_categoria)
VALUES
    (2, 1, N'Nueva C�rdoba', N'Chile',      N'350',  N'Nueva C�rdoba', 1003, N'5000', N'351-5552001', 70, 10, 3),
    (2, 2, N'G�emes',        N'Belgrano',   N'1200', N'G�emes',        1002, N'5000', N'351-5552002', 60, 15, 2),
    (2, 3, N'Alta C�rdoba',  N'Rodriguez',  N'800',  N'Alta C�rdoba',  1001, N'5000', N'351-5552003', 80, 15, 2),
    (2, 4, N'Centro',        N'San Mart�n', N'400',  N'Centro',        1004, N'5000', N'351-5552004', 90, 20, 3);



/* Nueva C�rdoba: total 70 */
INSERT INTO dbo.zonas_sucursales
(nro_restaurante, nro_sucursal, cod_zona, cant_comensales, permite_menores, habilitada)
VALUES
    (2,1,1,50,1,1), (2,1,2,20,1,1);

/* G�emes: total 60 */
INSERT INTO dbo.zonas_sucursales
(nro_restaurante, nro_sucursal, cod_zona, cant_comensales, permite_menores, habilitada)
VALUES
    (2,2,1,40,1,1), (2,2,2,20,1,1);

/* Alta C�rdoba: total 80 */
INSERT INTO dbo.zonas_sucursales
(nro_restaurante, nro_sucursal, cod_zona, cant_comensales, permite_menores, habilitada)
VALUES
    (2,3,1,50,1,1), (2,3,2,30,1,1);

/* Centro: total 90 */
INSERT INTO dbo.zonas_sucursales
(nro_restaurante, nro_sucursal, cod_zona, cant_comensales, permite_menores, habilitada)
VALUES
    (2,4,1,60,1,1), (2,4,2,20,1,1), (2,4,3,10,0,1);


INSERT INTO dbo.turnos_sucursales (nro_restaurante, nro_sucursal, hora_reserva, hora_hasta, habilitado) VALUES
-- Almuerzo
(2,1,'12:00','15:00',1),(2,2,'12:00','15:00',1),(2,3,'12:00','15:00',1),(2,4,'12:00','15:00',1),
-- Cena
(2,1,'20:00','23:00',1),(2,2,'20:00','23:00',1),(2,3,'20:00','23:00',1),(2,4,'20:00','23:00',1);







/* Todas las sucursales sirven Fusi�n japonesa-peruana */
INSERT INTO dbo.tipos_comidas_sucursales (nro_restaurante, nro_sucursal, nro_tipo_comida, habilitado)
VALUES (2,1,2,1),(2,2,2,1),(2,3,2,1),(2,4,2,1);

INSERT INTO dbo.estilos (nro_estilo, nom_estilo) VALUES
                                                     (1, N'Casual'), (2, N'Familiar');

/* Todas las sucursales con estilos Casual y Familiar */
INSERT INTO dbo.estilos_sucursales (nro_restaurante, nro_sucursal, nro_estilo, habilitado)
VALUES
    (2,1,1,1),(2,1,2,1),
    (2,2,1,1),(2,2,2,1),
    (2,3,1,1),(2,3,2,1),
    (2,4,1,1),(2,4,2,1);


INSERT INTO dbo.especialidades_alimentarias (nro_restriccion, nom_restriccion) VALUES
                                                                                   (1, N'comidas a la llamas'), (2, N'comida picante');

/* Especialidades alimentarias (Vegetariano y Cel�aco habilitados) */
INSERT INTO dbo.especialidades_alimentarias_sucursales (nro_restaurante, nro_sucursal, nro_restriccion, habilitada)
VALUES
    (2,1,1,1),(2,1,2,1),
    (2,2,1,1),(2,2,2,1),
    (2,3,1,1),(2,3,2,1),
    (2,4,1,1),(2,4,2,1);



/* =======================
   Nueva C�rdoba (sucursal 1, zonas 1 y 2)
   =======================*/
INSERT INTO dbo.zonas_turnos_sucursales
(nro_restaurante, nro_sucursal, cod_zona, hora_reserva, permite_menores)
VALUES
    (2,1,1,'12:00',1),(2,1,1,'20:00',1),  -- Sal�n
    (2,1,2,'12:00',1),(2,1,2,'20:00',1);  -- Terraza

/* =======================
   G�emes (sucursal 2, zonas 1 y 2)
   =======================*/
INSERT INTO dbo.zonas_turnos_sucursales
(nro_restaurante, nro_sucursal, cod_zona, hora_reserva, permite_menores)
VALUES
    (2,2,1,'12:00',1),(2,2,1,'20:00',1),  -- Sal�n
    (2,2,2,'12:00',1),(2,2,2,'20:00',1);  -- Terraza

/* =======================
   Alta C�rdoba (sucursal 3, zonas 1 y 2)
   =======================*/
INSERT INTO dbo.zonas_turnos_sucursales
(nro_restaurante, nro_sucursal, cod_zona, hora_reserva, permite_menores)
VALUES
    (2,3,1,'12:00',1),(2,3,1,'20:00',1),  -- Sal�n
    (2,3,2,'12:00',1),(2,3,2,'20:00',1);  -- Terraza

/* =======================
   Centro (sucursal 4, zonas 1, 2 y 3)
   =======================*/
INSERT INTO dbo.zonas_turnos_sucursales
(nro_restaurante, nro_sucursal, cod_zona, hora_reserva, permite_menores)
VALUES
    (2,4,1,'12:00',1),(2,4,1,'20:00',1),  -- Sal�n
    (2,4,2,'12:00',1),(2,4,2,'20:00',1),  -- Terraza
    (2,4,3,'12:00',0),(2,4,3,'20:00',0);  -- Patio (no menores)
go

CREATE OR ALTER PROCEDURE dbo.ins_cliente_reserva_sucursal
    -- Cliente (si @nro_cliente es NULL, se inserta usando correo como clave natural)
    @nro_cliente        INT              = NULL,
    @apellido           NVARCHAR(80)     = NULL,
    @nombre             NVARCHAR(80)     = NULL,
    @correo             NVARCHAR(160)    = NULL,
    @telefonos          NVARCHAR(80)     = NULL,

    -- Reserva
    @cod_reserva        UNIQUEIDENTIFIER = NULL,  -- se ignora: la tabla tiene DEFAULT NEWSEQUENTIALID()
    @fecha_reserva      DATE,
    @hora_reserva       TIME(0),
    @nro_restaurante    INT,
    @nro_sucursal       INT,
    @cod_zona           INT,
    @cant_adultos       INT,
    @cant_menores       INT,
    @costo_reserva      DECIMAL(12,2)    = 0,
    @cancelada          BIT              = 0,
    @fecha_cancelacion  DATE             = NULL,

    -- salida
    @o_cod_reserva      UNIQUEIDENTIFIER OUTPUT
    AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

BEGIN TRY
BEGIN TRAN;

        DECLARE @cliente_id INT;

        /* 1) Resolver cliente */
        IF @nro_cliente IS NOT NULL
BEGIN
            SET @cliente_id = @nro_cliente;
END
ELSE
BEGIN
            -- Reusar por correo si ya existe
SELECT @cliente_id = c.nro_cliente
FROM dbo.clientes c
WHERE c.correo = @correo;

IF @cliente_id IS NULL
BEGIN
INSERT INTO dbo.clientes (apellido, nombre, correo, telefonos)
VALUES (@apellido, @nombre, @correo, @telefonos);

SET @cliente_id = SCOPE_IDENTITY();
END
END

       

        /* 3) Insertar reserva y capturar cod_reserva */
       /* 3) Insertar reserva y capturar cod_reserva */
        DECLARE @t TABLE (cod_reserva UNIQUEIDENTIFIER);

INSERT INTO dbo.reservas_sucursales
(nro_cliente, fecha_reserva, hora_reserva,
 nro_restaurante, nro_sucursal, cod_zona,
 cant_adultos, cant_menores, costo_reserva,
 cancelada, fecha_cancelacion)
    OUTPUT inserted.cod_reserva INTO @t
VALUES
    (@cliente_id, @fecha_reserva, @hora_reserva,
    @nro_restaurante, @nro_sucursal, @cod_zona,
    @cant_adultos, @cant_menores, @costo_reserva,
    @cancelada, @fecha_cancelacion);

COMMIT;

/* 4) Devolver por ResultSet (para leer con #result-set-1) */
SELECT cod_reserva = (SELECT TOP 1 cod_reserva FROM @t);
END TRY
BEGIN CATCH
IF XACT_STATE() <> 0 ROLLBACK;
        -- Mensaje amigable si choca UNIQUE de correo
        IF ERROR_NUMBER() IN (2627,2601) AND CHARINDEX('AK_clientes_correo', ERROR_MESSAGE()) > 0
            THROW 51030, 'El correo de cliente ya existe.', 1;

        THROW; -- re-lanzar el error original
END CATCH
END
GO

SELECT *
FROM dbo.turnos_sucursales
WHERE nro_restaurante = 2
  AND nro_sucursal    = 1
    go
SELECT *
FROM   dbo.sucursales
    go

SELECT *
FROM dbo.clientes

    INSERT INTO dbo.clientes
(apellido, nombre, correo, telefonos)
VALUES ('Ghinamoo','Estabano','aaa@aaa.com','+54 351 55512342')
    go

SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO

CREATE OR ALTER PROCEDURE dbo.get_horarios_disponibles
    @nro_restaurante INT,
    @nro_sucursal    INT,
    @cod_zona        INT,
    @fecha           DATE,
    @cant_personas   INT,
    @menores         BIT
    AS
BEGIN
    SET NOCOUNT ON;

    IF @cant_personas <= 0
BEGIN
        RAISERROR('La cantidad de personas debe ser mayor a 0.', 16, 1);
        RETURN;
END

    -- Verificar que la zona exista y est� habilitada para la sucursal
    IF NOT EXISTS (
        SELECT 1
        FROM dbo.zonas_sucursales zs
        WHERE zs.nro_restaurante = @nro_restaurante
          AND zs.nro_sucursal    = @nro_sucursal
          AND zs.cod_zona        = @cod_zona
          AND zs.habilitada      = 1
    )
BEGIN
        -- Devuelve vac�o
SELECT CAST(NULL AS TIME(0)) AS hora_reserva,
       CAST(NULL AS TIME(0)) AS hora_hasta,
       NULL AS capacidad_total,
       NULL AS ocupados,
       NULL AS cupo_disponible
    WHERE 1 = 0;
RETURN;
END

    ;WITH Turnos AS (
    /* Turnos v�lidos para la sucursal y que adem�s est�n asociados a la zona */
    SELECT t.nro_restaurante, t.nro_sucursal, t.hora_reserva, t.hora_hasta
    FROM dbo.turnos_sucursales t
             INNER JOIN dbo.zonas_turnos_sucursales zts
                        ON zts.nro_restaurante = t.nro_restaurante
                            AND zts.nro_sucursal    = t.nro_sucursal
                            AND zts.hora_reserva    = t.hora_reserva
                            AND zts.cod_zona        = @cod_zona
    WHERE t.nro_restaurante = @nro_restaurante
      AND t.nro_sucursal    = @nro_sucursal
      AND t.habilitado      = 1
),
          Zona AS (
              SELECT zs.cant_comensales, zs.permite_menores, zs.habilitada
              FROM dbo.zonas_sucursales zs
              WHERE zs.nro_restaurante = @nro_restaurante
                AND zs.nro_sucursal    = @nro_sucursal
                AND zs.cod_zona        = @cod_zona
          ),
          Ocupacion AS (
              SELECT
                  r.hora_reserva,
                  ocupados = SUM(ISNULL(r.cant_adultos,0) + ISNULL(r.cant_menores,0))
              FROM dbo.reservas_sucursales r
              WHERE r.nro_restaurante = @nro_restaurante
                AND r.nro_sucursal    = @nro_sucursal
                AND r.cod_zona        = @cod_zona
                AND r.fecha_reserva   = @fecha
                AND ISNULL(r.cancelada,0) = 0
              GROUP BY r.hora_reserva
          )

     SELECT
         t.hora_reserva,
         t.hora_hasta
     FROM Turnos t
              CROSS JOIN Zona z
              LEFT JOIN Ocupacion o
                        ON o.hora_reserva = t.hora_reserva
     WHERE
       -- Cupo suficiente
         (z.cant_comensales - ISNULL(o.ocupados,0)) >= @cant_personas
       -- Si hay menores en la solicitud, la zona debe permitir menores
       AND ( @menores = 0 OR z.permite_menores = 1 )
     ORDER BY t.hora_reserva;
END
GO


-- 1) turnos de la sucursal
SELECT * FROM dbo.turnos_sucursales
WHERE nro_restaurante = 2 AND nro_sucursal = 1;

-- 2) zonas_turnos_sucursales (zona habilitada en turno)
SELECT * FROM dbo.zonas_turnos_sucursales
WHERE nro_restaurante = 2 AND nro_sucursal = 1 AND cod_zona = 1;

-- 3) verificar capacidades (suma de zonas y total_comensales)
SELECT s.total_comensales, zs.cod_zona, zs.cant_comensales
FROM dbo.sucursales s
         JOIN dbo.zonas_sucursales zs
              ON s.nro_restaurante = zs.nro_restaurante AND s.nro_sucursal = zs.nro_sucursal
WHERE s.nro_restaurante = 2 AND s.nro_sucursal = 1;

go
CREATE OR ALTER PROCEDURE dbo.get_info_restaurante_rs
    @nro_restaurante INT
    AS
BEGIN
    SET NOCOUNT ON;

    IF NOT EXISTS (SELECT 1 FROM dbo.restaurantes WHERE nro_restaurante = @nro_restaurante)
BEGIN
        RAISERROR('El restaurante especificado no existe.', 16, 1);
        RETURN;
END;

    /* 1) Restaurante */
SELECT
    r.nro_restaurante,
    r.razon_social,
    r.cuit
FROM dbo.restaurantes r
WHERE r.nro_restaurante = @nro_restaurante;

/* 2) Contenidos (generales del restaurante; nro_sucursal IS NULL) */
SELECT
    c.nro_restaurante,
    c.nro_contenido,
    c.contenido_a_publicar,
    c.imagen_a_publicar,
    c.publicado,
    c.costo_click
FROM dbo.contenidos c
WHERE c.nro_restaurante = @nro_restaurante
  AND c.nro_sucursal IS NULL
ORDER BY c.nro_contenido;

/* 3) Sucursales */
SELECT
    s.nro_restaurante,
    s.nro_sucursal,
    s.nom_sucursal,
    s.calle,
    s.nro_calle,
    s.barrio,
    s.nro_localidad,
    l.nom_localidad,
    p.cod_provincia,
    p.nom_provincia,
    s.cod_postal,
    s.telefonos,
    s.total_comensales,
    s.min_tolerencia_reserva,
    s.nro_categoria,
    cp.nom_categoria AS categoria_precio
FROM dbo.sucursales s
         INNER JOIN dbo.localidades l ON l.nro_localidad = s.nro_localidad
         INNER JOIN dbo.provincias p  ON p.cod_provincia = l.cod_provincia
         INNER JOIN dbo.categorias_precios cp ON cp.nro_categoria = s.nro_categoria
WHERE s.nro_restaurante = @nro_restaurante
ORDER BY s.nro_sucursal;

/* 4) Contenidos por sucursal (nro_sucursal NOT NULL) */
SELECT
    c.nro_restaurante,
    c.nro_sucursal,
    c.nro_contenido,
    c.contenido_a_publicar,
    c.imagen_a_publicar,
    c.publicado,
    c.costo_click
FROM dbo.contenidos c
WHERE c.nro_restaurante = @nro_restaurante
  AND c.nro_sucursal IS NOT NULL
ORDER BY c.nro_sucursal, c.nro_contenido;

/* 5) Zonas por sucursal */
SELECT
    zs.nro_restaurante,
    zs.nro_sucursal,
    zs.cod_zona,
    z.nom_zona,
    zs.cant_comensales,
    zs.permite_menores,
    zs.habilitada
FROM dbo.zonas_sucursales zs
         INNER JOIN dbo.zonas z ON z.cod_zona = zs.cod_zona
WHERE zs.nro_restaurante = @nro_restaurante
ORDER BY zs.nro_sucursal, zs.cod_zona;

/* 6) Estilos por sucursal */
SELECT
    es.nro_restaurante,
    es.nro_sucursal,
    es.nro_estilo,
    e.nom_estilo,
    es.habilitado
FROM dbo.estilos_sucursales es
         INNER JOIN dbo.estilos e ON e.nro_estilo = es.nro_estilo
WHERE es.nro_restaurante = @nro_restaurante
ORDER BY es.nro_sucursal, es.nro_estilo;

/* 7) Especialidades alimentarias por sucursal */
SELECT
    eas.nro_restaurante,
    eas.nro_sucursal,
    eas.nro_restriccion,
    ea.nom_restriccion,
    eas.habilitada
FROM dbo.especialidades_alimentarias_sucursales eas
         INNER JOIN dbo.especialidades_alimentarias ea ON ea.nro_restriccion = eas.nro_restriccion
WHERE eas.nro_restaurante = @nro_restaurante
ORDER BY eas.nro_sucursal, eas.nro_restriccion;

/* 8) Tipos de comidas por sucursal */
SELECT
    tcs.nro_restaurante,
    tcs.nro_sucursal,
    tcs.nro_tipo_comida,
    tc.nom_tipo_comida,
    tcs.habilitado
FROM dbo.tipos_comidas_sucursales tcs
         INNER JOIN dbo.tipos_comidas tc ON tc.nro_tipo_comida = tcs.nro_tipo_comida
WHERE tcs.nro_restaurante = @nro_restaurante
ORDER BY tcs.nro_sucursal, tcs.nro_tipo_comida;
END;
GO


--EXEC dbo.get_info_restaurante_rs @nro_restaurante = 2;
