------------------------------------------------------
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
    -- Asegura zona válida para esa sucursal
                                        CONSTRAINT FK_rs_zonas_sucursales
                                            FOREIGN KEY (nro_restaurante, nro_sucursal, cod_zona)
                                                REFERENCES dbo.zonas_sucursales(nro_restaurante, nro_sucursal, cod_zona),
    -- Asegura turno válido para esa sucursal
                                        CONSTRAINT FK_rs_turnos_sucursales
                                            FOREIGN KEY (nro_restaurante, nro_sucursal, hora_reserva)
                                                REFERENCES dbo.turnos_sucursales(nro_restaurante, nro_sucursal, hora_reserva),
                                        CONSTRAINT CK_rs_cantidades CHECK (cant_adultos >= 0 AND cant_menores >= 0 AND (cant_adultos + cant_menores) > 0),
                                        CONSTRAINT CK_rs_cancelacion
                                            CHECK ( (cancelada = 1 AND fecha_cancelacion IS NOT NULL) OR
                                                    (cancelada = 0 AND fecha_cancelacion IS NULL) )
);
GO

CREATE TABLE dbo.clicks_contenidos (
                                       nro_restaurante       INT           NOT NULL,
                                       nro_contenido         INT           NOT NULL,
                                       nro_click             INT           NOT NULL,
                                       fecha_hora_registro   DATETIME2(3)  NOT NULL
        CONSTRAINT DF_clicks_contenidos_fecha DEFAULT (SYSUTCDATETIME()),
                                       nro_cliente           INT           NULL,
                                       costo_click           DECIMAL(12,2) NOT NULL,
                                       CONSTRAINT PK_clicks_contenidos
                                           PRIMARY KEY (nro_restaurante, nro_contenido, nro_click),
                                       CONSTRAINT FK_clicks_contenidos_contenidos
                                           FOREIGN KEY (nro_restaurante, nro_contenido)
                                               REFERENCES dbo.contenidos (nro_restaurante, nro_contenido),
                                       CONSTRAINT FK_clicks_contenidos_clientes
                                           FOREIGN KEY (nro_cliente)
                                               REFERENCES dbo.clientes (nro_cliente),
                                       CONSTRAINT CK_clicks_contenidos_costo_nonneg
                                           CHECK (costo_click >= 0)
);
GO

/*---------------------------------------------------------

        
						sección inserts

-----------------------------------------------------------*/
/* =======================
   Provincias y Localidades
   =======================*/
INSERT INTO dbo.provincias  (cod_provincia, nom_provincia)
VALUES (1,N'Córdoba');

INSERT INTO dbo.localidades (nro_localidad, nom_localidad, cod_provincia)
VALUES (1,N'Córdoba Capital',1);


/* =======================
   Categorías de precios
   =======================*/
INSERT INTO dbo.categorias_precios (nro_categoria, nom_categoria)
VALUES
    (1,N'Media'),(2,N'Alta');

/* =======================
   Zonas disponibles
   =======================*/
INSERT INTO dbo.zonas (cod_zona, nom_zona)
VALUES
    (1,N'Salón Nikkei'),
    (2,N'Terraza Zen'),
    (3,N'Bar Sushi');

/* =======================
   Tipos de comidas
   =======================*/
INSERT INTO dbo.tipos_comidas (nro_tipo_comida, nom_tipo_comida)
VALUES
    (1,N'Fusión japonesa-peruana');

/* =======================
   Especialidades alimentarias
   =======================*/
INSERT INTO dbo.especialidades_alimentarias (nro_restriccion, nom_restriccion)
VALUES
    (1, N'Vegetariano'),
    (2, N'Sin gluten'),
    (3, N'Pescetariano'),
    (4, N'Vegano');



/*INSERT INTO dbo.especialidades_alimentarias (nro_restriccion, nom_restriccion)
VALUES
		(5, N'vinagreta'),
		(6, N'caldoseno'),
		(7, N'ajimolido');
INSERT INTO dbo.especialidades_alimentarias_sucursales (nro_restaurante, nro_sucursal, nro_restriccion, habilitada)
VALUES
    (1,1,5,1),
	(1,1,6,1),
    (1,2,7,1);*/



/* =======================
   Estilos
=======================*/
INSERT INTO dbo.estilos (nro_estilo, nom_estilo)
VALUES
    (1,N'Moderno'),
    (2,N'Gourmet'),
    (3,N'Minimalista');


/* =======================
   Restaurante principal
   =======================*/
INSERT INTO dbo.restaurantes (nro_restaurante, razon_social, cuit)
VALUES
    (1,N'Perukai',N'30-88997766-4');

/* =======================
   Sucursales 
   =======================*/
INSERT INTO dbo.sucursales (nro_restaurante, nro_sucursal, nom_sucursal, calle, nro_calle, barrio, nro_localidad, cod_postal, telefonos,
                            total_comensales, min_tolerencia_reserva, nro_categoria)
VALUES
    (1,1,N'Perukai Nueva Córdoba',N'H. Yrigoyen',N'550',N'Nueva Córdoba',1,N'5000',N'3516003001',80,10,2),
    (1,2,N'Perukai Güemes',N'Belgrano',N'820',N'Güemes',1,N'5000',N'3516003002',100,15,2);

INSERT INTO dbo.zonas_sucursales
(nro_restaurante, nro_sucursal, cod_zona, cant_comensales, permite_menores, habilitada)
VALUES
    (1,1,1,40,1,1),(1,1,2,25,1,1),(1,1,3,15,0,1),
    (1,2,1,50,1,1),(1,2,2,30,1,1),(1,2,3,20,0,1);

INSERT INTO dbo.turnos_sucursales (nro_restaurante, nro_sucursal, hora_reserva, hora_hasta, habilitado)
VALUES
    (1,1,'12:00','13:30',1),(1,1,'13:30','15:00',1),
    (1,1,'20:00','22:00',1),(1,1,'22:00','23:59',1),
    (1,2,'12:00','13:30',1),(1,2,'13:30','15:00',1),
    (1,2,'20:00','22:00',1),(1,2,'22:00','23:59',1);

INSERT INTO dbo.zonas_turnos_sucursales (nro_restaurante, nro_sucursal, cod_zona, hora_reserva, permite_menores)
VALUES
    (1,1,1,'12:00',1),(1,1,1,'13:30',1),(1,1,1,'20:00',1),(1,1,1,'22:00',1),
    (1,1,2,'12:00',1),(1,1,2,'13:30',1),(1,1,2,'20:00',1),(1,1,2,'22:00',1),
    (1,1,3,'20:00',0),(1,1,3,'22:00',0),

    (1,2,1,'12:00',1),(1,2,1,'13:30',1),(1,2,1,'20:00',1),(1,2,1,'22:00',1),
    (1,2,2,'12:00',1),(1,2,2,'13:30',1),(1,2,2,'20:00',1),(1,2,2,'22:00',1),
    (1,2,3,'20:00',0),(1,2,3,'22:00',0);

INSERT INTO dbo.tipos_comidas_sucursales
(nro_restaurante, nro_sucursal, nro_tipo_comida, habilitado)
VALUES
    -- Sucursal 1
    (1,1,1,1),
    -- Sucursal 2
    (1,2,1,1);

INSERT INTO dbo.estilos_sucursales
(nro_restaurante, nro_sucursal, nro_estilo, habilitado)
VALUES
    -- Sucursal 1
    (1,1,1,1),
    (1,1,2,1),
    (1,1,3,1),

    -- Sucursal 2
    (1,2,1,1),
    (1,2,2,1),
    (1,2,3,1);

/* Especialidades alimentarias (solo vegetariano y celíaco habilitados) */
INSERT INTO dbo.especialidades_alimentarias_sucursales (nro_restaurante, nro_sucursal, nro_restriccion, habilitada)
VALUES
    (1,1,4,1),(1,1,1,1),
    (1,2,3,1),(1,2,2,1);

//* ============================================
   CONTENIDOS PROMOCIONALES — RESTAURANTE 2
   ============================================*/

INSERT INTO dbo.contenidos (nro_restaurante, nro_contenido, contenido_a_publicar, imagen_a_publicar, publicado, costo_click, nro_sucursal)
VALUES
(1,1,N'Nigiri nikkei premium con salsa anticuchera',N'https://www.800.cl/galeriasitios/Och/2019/11/27/Och__temple4.jpg',0,0.15,1),
(1,2,N'Roll acevichado fusión – plato estrella',N'https://theobjective.com/wp-content/uploads/2025/03/PORTADA-GASTRONOMIA-BRENDA-2-6-1024x576.jpg',0,0.15,2),
(1,3,N'Tataki de atún con reducción de maracuyá',N'https://img-global.cpcdn.com/recipes/fe633f06b025af31/1200x630cq80/photo.jpg',0,0.20,2),
(1,4,N'sushi aumado',N'https://www.ahumadosdominguez.es/wp-content/uploads/2023/07/sushi-salmon-ahumado.jpeg',0,0.15,1);
GO
/*
update contenidos
set publicado=0 where publicado=1*/






/*---------------------------------------------------------------
				sección procedimientos
-----------------------------------------------------------------*/

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


SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO

SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
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

    -- Verificar que la zona exista y está habilitada para la sucursal
    IF NOT EXISTS (
        SELECT 1
        FROM dbo.zonas_sucursales zs
        WHERE zs.nro_restaurante = @nro_restaurante
          AND zs.nro_sucursal    = @nro_sucursal
          AND zs.cod_zona        = @cod_zona
          AND zs.habilitada      = 1
    )
BEGIN
        -- Devuelve vacío
SELECT CAST(NULL AS TIME(0)) AS hora_reserva,
       CAST(NULL AS TIME(0)) AS hora_hasta,
       NULL AS capacidad_total,
       NULL AS ocupados,
       NULL AS cupo_disponible
    WHERE 1 = 0;
RETURN;
END

    ;WITH Turnos AS (
    /* Turnos válidos para la sucursal y que además estén asociados a la zona */
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

/* 2) Sucursales */
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

/* 3) Zonas por sucursal */
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

/* 4) Turnos por sucursal */
SELECT
    t.nro_restaurante,
    t.nro_sucursal,
    t.hora_reserva,
    t.hora_hasta,
    t.habilitado
FROM dbo.turnos_sucursales t
WHERE t.nro_restaurante = @nro_restaurante
ORDER BY t.nro_sucursal, t.hora_reserva;

/* 5) Zonas-Turnos por sucursal */
SELECT
    zts.nro_restaurante,
    zts.nro_sucursal,
    zts.cod_zona,
    zts.hora_reserva,
    zts.permite_menores
FROM dbo.zonas_turnos_sucursales zts
WHERE zts.nro_restaurante = @nro_restaurante
ORDER BY zts.nro_sucursal, zts.cod_zona, zts.hora_reserva;

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


CREATE OR ALTER PROCEDURE dbo.sp_clicks_contenidos_insertar_lote
    @clicks_json NVARCHAR(MAX)
    AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

    DECLARE @ahora DATETIME2 = SYSUTCDATETIME();
    DECLARE @error_message NVARCHAR(4000);

CREATE TABLE #ClicksTemp (
                             id_temp INT IDENTITY(1,1),  -- ID temporal para ordenar
                             nro_restaurante INT,
                             nro_contenido INT,
                             correo_cliente NVARCHAR(160),
                             fecha_hora_registro DATETIME2,
                             nro_cliente INT NULL,
                             costo_click DECIMAL(12,2) NULL,
                             nro_click INT NULL
);

-- 1) Parsear JSON
INSERT INTO #ClicksTemp (nro_restaurante, nro_contenido, correo_cliente, fecha_hora_registro)
SELECT
    nro_restaurante,
    nro_contenido,
    correo_cliente,
    ISNULL(CAST(fecha_hora_registro AS DATETIME2), @ahora)
FROM OPENJSON(@clicks_json)
    WITH (
    nro_restaurante INT '$.nro_restaurante',
    nro_contenido INT '$.nro_contenido',
    correo_cliente NVARCHAR(160) '$.correo_cliente',
    fecha_hora_registro NVARCHAR(50) '$.fecha_hora_registro'
    );

-- 2) Resolver clientes
UPDATE t
SET t.nro_cliente = c.nro_cliente
    FROM #ClicksTemp t
    INNER JOIN dbo.clientes c ON c.correo = t.correo_cliente
WHERE t.correo_cliente IS NOT NULL;

SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
BEGIN TRAN;

BEGIN TRY
        -- 3) Obtener costos
UPDATE t
SET t.costo_click = c.costo_click
    FROM #ClicksTemp t
        INNER JOIN dbo.contenidos c WITH (UPDLOCK, HOLDLOCK)
ON c.nro_restaurante = t.nro_restaurante
    AND c.nro_contenido = t.nro_contenido;

-- Validar contenidos
IF EXISTS (SELECT 1 FROM #ClicksTemp WHERE costo_click IS NULL)
BEGIN
            DECLARE @rest INT, @cont INT;
            DECLARE @msg_temp NVARCHAR(MAX) = '';

            DECLARE contenidos_faltantes CURSOR FOR
SELECT DISTINCT nro_restaurante, nro_contenido
FROM #ClicksTemp
WHERE costo_click IS NULL;

OPEN contenidos_faltantes;
FETCH NEXT FROM contenidos_faltantes INTO @rest, @cont;

WHILE @@FETCH_STATUS = 0
BEGIN
                SET @msg_temp = @msg_temp + 'Rest:' + CAST(@rest AS VARCHAR) +
                                ' Cont:' + CAST(@cont AS VARCHAR) + '; ';
FETCH NEXT FROM contenidos_faltantes INTO @rest, @cont;
END

CLOSE contenidos_faltantes;
DEALLOCATE contenidos_faltantes;

            SET @error_message = 'Contenidos no encontrados: ' + @msg_temp;

ROLLBACK;
DROP TABLE IF EXISTS #ClicksTemp;
RAISERROR(@error_message, 16, 1);
            RETURN;
END

        -- 4) Calcular nro_click CORREGIDO
        DECLARE @nro_rest INT, @nro_cont INT, @max_click INT;

        DECLARE click_cursor CURSOR LOCAL FAST_FORWARD FOR
SELECT DISTINCT nro_restaurante, nro_contenido
FROM #ClicksTemp
ORDER BY nro_restaurante, nro_contenido;

OPEN click_cursor;
FETCH NEXT FROM click_cursor INTO @nro_rest, @nro_cont;

WHILE @@FETCH_STATUS = 0
BEGIN
            -- Obtener el máximo actual de la tabla principal
SELECT @max_click = ISNULL(MAX(nro_click), 0)
FROM dbo.clicks_contenidos WITH (UPDLOCK, HOLDLOCK)
WHERE nro_restaurante = @nro_rest
  AND nro_contenido = @nro_cont;

-- CORRECCIÓN: Asignar números secuenciales usando ROW_NUMBER
-- ordenados por id_temp para mantener el orden de inserción
UPDATE t
SET t.nro_click = @max_click + rn
    FROM #ClicksTemp t
            INNER JOIN (
                SELECT
                    id_temp,
                    ROW_NUMBER() OVER (ORDER BY id_temp) as rn
                FROM #ClicksTemp
                WHERE nro_restaurante = @nro_rest
                  AND nro_contenido = @nro_cont
            ) AS numbered ON t.id_temp = numbered.id_temp;

FETCH NEXT FROM click_cursor INTO @nro_rest, @nro_cont;
END

CLOSE click_cursor;
DEALLOCATE click_cursor;

        -- 5) Insertar
INSERT INTO dbo.clicks_contenidos
(nro_restaurante, nro_contenido, nro_click, fecha_hora_registro, nro_cliente, costo_click)
SELECT
    nro_restaurante, nro_contenido, nro_click,
    fecha_hora_registro, nro_cliente, costo_click
FROM #ClicksTemp
ORDER BY id_temp;  -- Mantener orden de inserción

COMMIT;

-- 6) Retornar resultados
SELECT
    nro_restaurante, nro_contenido, nro_click,
    fecha_hora_registro, nro_cliente, correo_cliente, costo_click
FROM #ClicksTemp
ORDER BY id_temp;

END TRY
BEGIN CATCH
IF @@TRANCOUNT > 0 ROLLBACK;
DROP TABLE IF EXISTS #ClicksTemp;
THROW;
END CATCH

DROP TABLE IF EXISTS #ClicksTemp;
END;
GO

CREATE OR ALTER PROCEDURE dbo.get_contenidos
    @nro_restaurante INT
    AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

BEGIN TRY
BEGIN TRAN;

        -- 1) Obtener contenidos no publicados /
SELECT
    c.nro_restaurante,
    c.nro_contenido,
    c.contenido_a_publicar,
    c.imagen_a_publicar,
    c.publicado,
    c.costo_click,
    c.nro_sucursal
FROM dbo.contenidos c
WHERE c.nro_restaurante = @nro_restaurante
  AND c.publicado = 0;

-- 2) Marcar como publicados

COMMIT;
END TRY
BEGIN CATCH
IF XACT_STATE() <> 0
            ROLLBACK;

        THROW;
END CATCH
END;

--para el procesamiento batch
select * from dbo.contenidos
update dbo.contenidos
set publicado=0

    go
CREATE OR ALTER PROCEDURE dbo.upd_publicar_contenidos_lote
    @nro_restaurante INT,
    @costo_click     DECIMAL(12,2),
    @nro_contenidos  NVARCHAR(MAX)  -- Lista de IDs separados por coma: '1,2,3,4'
    AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

BEGIN TRY
BEGIN TRAN;

        ------------------------------------------------------------
        -- 1) Crear tabla temporal con los IDs
        ------------------------------------------------------------
CREATE TABLE #contenidos_a_publicar (nro_contenido INT);

INSERT INTO #contenidos_a_publicar (nro_contenido)
SELECT CAST(value AS INT)
FROM STRING_SPLIT(@nro_contenidos, ',');

DECLARE @total_solicitado INT = (SELECT COUNT(*) FROM #contenidos_a_publicar);

        ------------------------------------------------------------
        -- 2) Actualizar todos los contenidos del lote
        ------------------------------------------------------------
UPDATE c
SET
    c.costo_click = @costo_click,
    c.publicado = 1
    FROM dbo.contenidos c
        INNER JOIN #contenidos_a_publicar tmp
ON c.nro_contenido = tmp.nro_contenido
WHERE c.nro_restaurante = @nro_restaurante
  AND c.publicado = 0;

DECLARE @registros_actualizados INT = @@ROWCOUNT;

        ------------------------------------------------------------
        -- 3) Retornar mensaje de resultado
        ------------------------------------------------------------
SELECT
    resultado = CASE
                    WHEN @registros_actualizados = @total_solicitado
                        THEN 'Éxito: Se publicaron ' + CAST(@registros_actualizados AS VARCHAR) + ' contenidos correctamente.'
                    WHEN @registros_actualizados > 0
                        THEN 'Parcial: Se publicaron ' + CAST(@registros_actualizados AS VARCHAR) + ' de ' + CAST(@total_solicitado AS VARCHAR) + ' contenidos (algunos ya estaban publicados).'
                    ELSE 'Advertencia: No se publicó ningún contenido (ya estaban publicados o no existen).'
        END,
    registros_actualizados = @registros_actualizados,
    registros_solicitados = @total_solicitado,
    costo_aplicado = @costo_click;

DROP TABLE #contenidos_a_publicar;

COMMIT;
END TRY
BEGIN CATCH
IF XACT_STATE() <> 0
            ROLLBACK;

        -- Retornar mensaje de error
SELECT
    resultado = 'Error: ' + ERROR_MESSAGE(),
    registros_actualizados = 0,
    registros_solicitados = 0,
    costo_aplicado = NULL;
END CATCH
END;
GO

CREATE OR ALTER PROCEDURE dbo.ins_cliente_reserva_sucursal
    -- Cliente
    @nro_cliente        INT              = NULL,
    @apellido           NVARCHAR(80)     = NULL,
    @nombre             NVARCHAR(80)     = NULL,
    @correo             NVARCHAR(160)    = NULL,
    @telefonos          NVARCHAR(80)     = NULL,

    -- Reserva
    @cod_reserva        UNIQUEIDENTIFIER = NULL,  -- ignorado (DEFAULT NEWSEQUENTIALID())
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

    -- salida (si la querés usar desde Java como OUTPUT)
    @o_cod_reserva      UNIQUEIDENTIFIER OUTPUT
    AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

BEGIN TRY
BEGIN TRAN;

        ------------------------------------------------------------
        -- 0) Validaciones básicas
        ------------------------------------------------------------
        DECLARE @cant_personas INT = ISNULL(@cant_adultos,0) + ISNULL(@cant_menores,0);

        IF @cant_personas <= 0
            THROW 51010, 'La cantidad de personas debe ser mayor a 0.', 1;

        IF @correo IS NULL AND @nro_cliente IS NULL
            THROW 51011, 'Debe informar nro_cliente o correo.', 1;

        ------------------------------------------------------------
        -- 1) Validar zona existe y habilitada + cupo base + permite menores
        ------------------------------------------------------------
        DECLARE @capacidad_total INT;
        DECLARE @permite_menores BIT;
        DECLARE @zona_habilitada BIT;

SELECT
    @capacidad_total = zs.cant_comensales,
    @permite_menores = zs.permite_menores,
    @zona_habilitada = zs.habilitada
FROM dbo.zonas_sucursales zs
WHERE zs.nro_restaurante = @nro_restaurante
  AND zs.nro_sucursal    = @nro_sucursal
  AND zs.cod_zona        = @cod_zona;

IF @capacidad_total IS NULL OR @zona_habilitada <> 1
            THROW 51012, 'La zona no existe o no está habilitada para la sucursal.', 1;

        IF @cant_menores > 0 AND @permite_menores <> 1
            THROW 51013, 'La zona no permite menores.', 1;

        ------------------------------------------------------------
        -- 2) Validar turno existe/habilitado y asociado a la zona
        ------------------------------------------------------------
        IF NOT EXISTS (
            SELECT 1
            FROM dbo.turnos_sucursales t
            WHERE t.nro_restaurante = @nro_restaurante
              AND t.nro_sucursal    = @nro_sucursal
              AND t.hora_reserva    = @hora_reserva
              AND t.habilitado      = 1
        )
            THROW 51014, 'El turno no existe o no está habilitado para la sucursal.', 1;

        IF NOT EXISTS (
            SELECT 1
            FROM dbo.zonas_turnos_sucursales zts
            WHERE zts.nro_restaurante = @nro_restaurante
              AND zts.nro_sucursal    = @nro_sucursal
              AND zts.cod_zona        = @cod_zona
              AND zts.hora_reserva    = @hora_reserva
        )
            THROW 51015, 'La zona no está habilitada para ese turno.', 1;

        -- (Opcional) Si querés validar menores por turno:
        -- IF @cant_menores > 0 AND EXISTS(
        --     SELECT 1 FROM dbo.zonas_turnos_sucursales zts
        --     WHERE zts.nro_restaurante=@nro_restaurante AND zts.nro_sucursal=@nro_sucursal
        --       AND zts.cod_zona=@cod_zona AND zts.hora_reserva=@hora_reserva
        --       AND ISNULL(zts.permite_menores,1)=0
        -- )
        --     THROW 51016, 'Ese turno/zona no permite menores.', 1;

        ------------------------------------------------------------
        -- 3) Resolver cliente (reusar por correo o insertar)
        ------------------------------------------------------------
        DECLARE @cliente_id INT;

        IF @nro_cliente IS NOT NULL
BEGIN
            SET @cliente_id = @nro_cliente;
END
ELSE
BEGIN
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

        ------------------------------------------------------------
        -- 4) Validar disponibilidad REAL (concurrencia controlada)
        --    Lockear reservas existentes del mismo turno/fecha/zona
        ------------------------------------------------------------
        DECLARE @ocupados INT;

SELECT
    @ocupados = SUM(ISNULL(r.cant_adultos,0) + ISNULL(r.cant_menores,0))
FROM dbo.reservas_sucursales r WITH (UPDLOCK, HOLDLOCK)
WHERE r.nro_restaurante = @nro_restaurante
  AND r.nro_sucursal    = @nro_sucursal
  AND r.cod_zona        = @cod_zona
  AND r.fecha_reserva   = @fecha_reserva
  AND r.hora_reserva    = @hora_reserva
  AND ISNULL(r.cancelada,0) = 0;

SET @ocupados = ISNULL(@ocupados, 0);

        IF (@capacidad_total - @ocupados) < @cant_personas
            THROW 51020, 'No hay cupo disponible para ese turno y zona.', 1;

        ------------------------------------------------------------
        -- 5) Insertar reserva y devolver cod_reserva
        ------------------------------------------------------------
        DECLARE @t TABLE (cod_reserva UNIQUEIDENTIFIER);

INSERT INTO dbo.reservas_sucursales
(
    nro_cliente, fecha_reserva, hora_reserva,
    nro_restaurante, nro_sucursal, cod_zona,
    cant_adultos, cant_menores, costo_reserva,
    cancelada, fecha_cancelacion
)
    OUTPUT inserted.cod_reserva INTO @t
VALUES
    (
    @cliente_id, @fecha_reserva, @hora_reserva,
    @nro_restaurante, @nro_sucursal, @cod_zona,
    @cant_adultos, @cant_menores, @costo_reserva,
    @cancelada, @fecha_cancelacion
    );

-- set output param
SELECT @o_cod_reserva = (SELECT TOP 1 cod_reserva FROM @t);

COMMIT;

-- Resultset para Java
SELECT cod_reserva = @o_cod_reserva;

END TRY
BEGIN CATCH
IF XACT_STATE() <> 0 ROLLBACK;

        -- Mensaje amigable si choca UNIQUE de correo
        IF ERROR_NUMBER() IN (2627,2601) AND CHARINDEX('AK_clientes_correo', ERROR_MESSAGE()) > 0
            THROW 51030, 'El correo de cliente ya existe.', 1;

        THROW;
END CATCH
END
GO

CREATE OR ALTER PROCEDURE dbo.cancelar_reserva_por_codigo_sucursal
    @cod_reserva_sucursal NVARCHAR(80)
    AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

    DECLARE
@pos INT,
        @cod_reserva_str NVARCHAR(60),
        @nro_sucursal_str NVARCHAR(20),
        @cod_reserva UNIQUEIDENTIFIER,
        @nro_sucursal INT,
        @nro_restaurante INT,
        @fecha_reserva DATE,
        @hora_reserva TIME(0),
        @inicio_reserva DATETIME,
        @min_tolerencia INT,
        @ahora DATETIME = GETDATE(),
        @minutos_antes INT,
        @cancelada BIT;

BEGIN TRY
        /* 1) Validación formato: "codReserva-nroSucursal" */
SET @pos = LEN(@cod_reserva_sucursal) - CHARINDEX('-', REVERSE(@cod_reserva_sucursal)) + 1;
        IF (@pos <= 0)
BEGIN
            THROW 51000, 'Formato inválido. Se espera: codReserva-nroSucursal', 1;
END

        SET @cod_reserva_str = LEFT(@cod_reserva_sucursal, @pos - 1);
        SET @nro_sucursal_str = SUBSTRING(@cod_reserva_sucursal, @pos + 1, 80);

        /* 2) Parse GUID e INT */
        SET @cod_reserva = TRY_CONVERT(UNIQUEIDENTIFIER, @cod_reserva_str);
        SET @nro_sucursal = TRY_CONVERT(INT, @nro_sucursal_str);

        IF (@cod_reserva IS NULL OR @nro_sucursal IS NULL)
BEGIN
            THROW 51000, 'No se pudo interpretar codReserva o nroSucursal desde codReservaSucursal.', 1;
END

        /* 3) Buscar reserva */
SELECT
    @nro_restaurante = rs.nro_restaurante,
    @fecha_reserva   = rs.fecha_reserva,
    @hora_reserva    = rs.hora_reserva,
    @cancelada       = rs.cancelada
FROM dbo.reservas_sucursales rs
WHERE rs.cod_reserva = @cod_reserva
  AND rs.nro_sucursal = @nro_sucursal;

IF (@nro_restaurante IS NULL)
BEGIN
SELECT CAST(0 AS BIT) AS success,
       'NOT_FOUND' AS status,
       'Reserva no encontrada.' AS message;
RETURN;
END

        /* 4) Idempotencia: si ya está cancelada */
        IF (@cancelada = 1)
BEGIN
SELECT CAST(1 AS BIT) AS success,
       'ALREADY_CANCELLED' AS status,
       'La reserva ya estaba cancelada.' AS message;
RETURN;
END

        /* 5) Obtener tolerancia mínima */
SELECT @min_tolerencia = s.min_tolerencia_reserva
FROM dbo.sucursales s
WHERE s.nro_restaurante = @nro_restaurante
  AND s.nro_sucursal = @nro_sucursal;

IF (@min_tolerencia IS NULL)
            SET @min_tolerencia = 0;

        /* 6) Comparación: minutos antes del inicio de la reserva */
        SET @inicio_reserva = DATEADD(SECOND, 0,
                             DATEADD(DAY, DATEDIFF(DAY, 0, @fecha_reserva),
                             CAST(@hora_reserva AS DATETIME)));

        SET @minutos_antes = DATEDIFF(MINUTE, @ahora, @inicio_reserva);

        IF (@minutos_antes < @min_tolerencia)
BEGIN
SELECT CAST(0 AS BIT) AS success,
       'NOT_ALLOWED' AS status,
       CONCAT('No se puede cancelar: tolerancia mínima ', @min_tolerencia,
              ' min. Faltan ', @minutos_antes, ' min para la reserva.') AS message;
RETURN;
END

        /* 7) Cancelar */
BEGIN TRAN;

UPDATE dbo.reservas_sucursales
SET cancelada = 1,
    fecha_cancelacion = CAST(@ahora AS DATE)
WHERE cod_reserva = @cod_reserva
  AND nro_sucursal = @nro_sucursal
  AND cancelada = 0;

COMMIT;

SELECT CAST(1 AS BIT) AS success,
       'CANCELLED' AS status,
       'Cancelación exitosa.' AS message;

END TRY
BEGIN CATCH
IF @@TRANCOUNT > 0 ROLLBACK;

        DECLARE @msg NVARCHAR(4000) = ERROR_MESSAGE();
SELECT CAST(0 AS BIT) AS success,
       'ERROR' AS status,
       @msg AS message;
END CATCH
END;
GO

CREATE OR ALTER PROCEDURE dbo.modificar_reserva_por_codigo_sucursal
    @cod_reserva_sucursal NVARCHAR(80),

    -- Nuevos datos
    @fecha_reserva  DATE,
    @hora_reserva   TIME(0),
    @cod_zona       INT,
    @cant_adultos   INT,
    @cant_menores   INT,
    @costo_reserva  Decimal(12,2)
    AS
BEGIN
    SET NOCOUNT ON;
    SET XACT_ABORT ON;

    DECLARE
@pos               INT,
        @cod_reserva_str   NVARCHAR(60),
        @nro_sucursal_str  NVARCHAR(20),
        @cod_reserva       UNIQUEIDENTIFIER,
        @nro_sucursal      INT,

        -- datos de la reserva actual
        @nro_restaurante   INT,
        @fecha_actual      DATE,
        @hora_actual       TIME(0),
        @cancelada         BIT,

        -- tolerancia
        @min_tolerencia    INT,
        @ahora             DATETIME = GETDATE(),
        @inicio_reserva    DATETIME,
        @minutos_antes     INT,

        -- validaciones zona/turno/cupo
        @cant_personas     INT,
        @capacidad_total   INT,
        @permite_menores   BIT,
        @zona_habilitada   BIT,
        @ocupados          INT;

BEGIN TRY
        ------------------------------------------------------------
        -- 1) Validación formato "GUID-nroSucursal"
        ------------------------------------------------------------
SET @pos = LEN(@cod_reserva_sucursal) - CHARINDEX('-', REVERSE(@cod_reserva_sucursal)) + 1;
        IF (@pos <= 0)
BEGIN
SELECT CAST(0 AS BIT) AS success, 'BAD_FORMAT' AS status,
       'Formato inválido. Se espera: codReserva-nroSucursal' AS message;
RETURN;
END

        SET @cod_reserva_str  = LEFT(@cod_reserva_sucursal, @pos - 1);
        SET @nro_sucursal_str = SUBSTRING(@cod_reserva_sucursal, @pos + 1, 80);

        SET @cod_reserva  = TRY_CONVERT(UNIQUEIDENTIFIER, @cod_reserva_str);
        SET @nro_sucursal = TRY_CONVERT(INT, @nro_sucursal_str);

        IF (@cod_reserva IS NULL OR @nro_sucursal IS NULL)
BEGIN
SELECT CAST(0 AS BIT) AS success, 'BAD_FORMAT' AS status,
       'No se pudo interpretar codReserva o nroSucursal desde codReservaSucursal.' AS message;
RETURN;
END

        ------------------------------------------------------------
        -- 2) Buscar reserva actual
        ------------------------------------------------------------
SELECT
    @nro_restaurante = rs.nro_restaurante,
    @fecha_actual    = rs.fecha_reserva,
    @hora_actual     = rs.hora_reserva,
    @cancelada       = rs.cancelada
FROM dbo.reservas_sucursales rs
WHERE rs.cod_reserva = @cod_reserva
  AND rs.nro_sucursal = @nro_sucursal;

IF (@nro_restaurante IS NULL)
BEGIN
SELECT CAST(0 AS BIT) AS success, 'NOT_FOUND' AS status,
       'Reserva no encontrada.' AS message;
RETURN;
END

        IF (@cancelada = 1)
BEGIN
SELECT CAST(0 AS BIT) AS success, 'ALREADY_CANCELLED' AS status,
       'No se puede modificar: la reserva está cancelada.' AS message;
RETURN;
END

        ------------------------------------------------------------
        -- 3) Validaciones básicas de entrada
        ------------------------------------------------------------
        SET @cant_personas = ISNULL(@cant_adultos,0) + ISNULL(@cant_menores,0);

        IF @cant_personas <= 0
BEGIN
SELECT CAST(0 AS BIT) AS success, 'INVALID' AS status,
       'La cantidad de personas debe ser mayor a 0.' AS message;
RETURN;
END

        IF @fecha_reserva IS NULL OR @hora_reserva IS NULL
BEGIN
SELECT CAST(0 AS BIT) AS success, 'INVALID' AS status,
       'Debe informar fecha_reserva y hora_reserva.' AS message;
RETURN;
END

        ------------------------------------------------------------
        -- 4) Validar tolerancia mínima (como cancelar)
        --    Se calcula contra la FECHA/HORA ACTUAL de la reserva
        ------------------------------------------------------------
SELECT @min_tolerencia = s.min_tolerencia_reserva
FROM dbo.sucursales s
WHERE s.nro_restaurante = @nro_restaurante
  AND s.nro_sucursal    = @nro_sucursal;

IF (@min_tolerencia IS NULL) SET @min_tolerencia = 0;

        SET @inicio_reserva =
            DATEADD(SECOND, 0,
              DATEADD(DAY, DATEDIFF(DAY, 0, @fecha_actual),
              CAST(@hora_actual AS DATETIME)));

        SET @minutos_antes = DATEDIFF(MINUTE, @ahora, @inicio_reserva);

        IF (@minutos_antes < @min_tolerencia)
BEGIN
SELECT CAST(0 AS BIT) AS success, 'NOT_ALLOWED' AS status,
       CONCAT('No se puede modificar: tolerancia mínima ', @min_tolerencia,
              ' min. Faltan ', @minutos_antes, ' min para la reserva.') AS message;
RETURN;
END

        ------------------------------------------------------------
        -- 5) Validar zona existe/habilitada + capacidad + menores
        ------------------------------------------------------------
SELECT
    @capacidad_total = zs.cant_comensales,
    @permite_menores = zs.permite_menores,
    @zona_habilitada = zs.habilitada
FROM dbo.zonas_sucursales zs
WHERE zs.nro_restaurante = @nro_restaurante
  AND zs.nro_sucursal    = @nro_sucursal
  AND zs.cod_zona        = @cod_zona;

IF @capacidad_total IS NULL OR @zona_habilitada <> 1
BEGIN
SELECT CAST(0 AS BIT) AS success, 'INVALID_ZONE' AS status,
       'La zona no existe o no está habilitada para la sucursal.' AS message;
RETURN;
END

        IF @cant_menores > 0 AND @permite_menores <> 1
BEGIN
SELECT CAST(0 AS BIT) AS success, 'NO_MINORS' AS status,
       'La zona no permite menores.' AS message;
RETURN;
END

        ------------------------------------------------------------
        -- 6) Validar turno existe/habilitado y asociado a la zona
        ------------------------------------------------------------
        IF NOT EXISTS (
            SELECT 1
            FROM dbo.turnos_sucursales t
            WHERE t.nro_restaurante = @nro_restaurante
              AND t.nro_sucursal    = @nro_sucursal
              AND t.hora_reserva    = @hora_reserva
              AND t.habilitado      = 1
        )
BEGIN
SELECT CAST(0 AS BIT) AS success, 'INVALID_TURNO' AS status,
       'El turno no existe o no está habilitado para la sucursal.' AS message;
RETURN;
END

        IF NOT EXISTS (
            SELECT 1
            FROM dbo.zonas_turnos_sucursales zts
            WHERE zts.nro_restaurante = @nro_restaurante
              AND zts.nro_sucursal    = @nro_sucursal
              AND zts.cod_zona        = @cod_zona
              AND zts.hora_reserva    = @hora_reserva
        )
BEGIN
SELECT CAST(0 AS BIT) AS success, 'INVALID_ZONE_TURNO' AS status,
       'La zona no está habilitada para ese turno.' AS message;
RETURN;
END

        ------------------------------------------------------------
        -- 7) Validar disponibilidad REAL (concurrencia controlada)
        --    Excluye ESTA reserva del conteo
        ------------------------------------------------------------
BEGIN TRAN;

SELECT
    @ocupados = SUM(ISNULL(r.cant_adultos,0) + ISNULL(r.cant_menores,0))
FROM dbo.reservas_sucursales r WITH (UPDLOCK, HOLDLOCK)
WHERE r.nro_restaurante = @nro_restaurante
  AND r.nro_sucursal    = @nro_sucursal
  AND r.cod_zona        = @cod_zona
  AND r.fecha_reserva   = @fecha_reserva
  AND r.hora_reserva    = @hora_reserva
  AND ISNULL(r.cancelada,0) = 0
  AND r.cod_reserva <> @cod_reserva;

SET @ocupados = ISNULL(@ocupados, 0);

        IF (@capacidad_total - @ocupados) < @cant_personas
BEGIN
ROLLBACK;
SELECT CAST(0 AS BIT) AS success, 'NO_CAPACITY' AS status,
       'No hay cupo disponible para ese turno y zona.' AS message;
RETURN;
END

        ------------------------------------------------------------
        -- 8) Actualizar reserva
        ------------------------------------------------------------
UPDATE dbo.reservas_sucursales
SET
    fecha_reserva = @fecha_reserva,
    hora_reserva  = @hora_reserva,
    cod_zona      = @cod_zona,
    cant_adultos  = @cant_adultos,
    cant_menores  = @cant_menores,
    costo_reserva = @costo_reserva
WHERE cod_reserva = @cod_reserva
  AND nro_sucursal = @nro_sucursal
  AND ISNULL(cancelada,0) = 0;

IF (@@ROWCOUNT = 0)
BEGIN
ROLLBACK;
SELECT CAST(0 AS BIT) AS success, 'NOT_UPDATED' AS status,
       'No se pudo modificar la reserva (puede estar cancelada o no existir).' AS message;
RETURN;
END

COMMIT;

SELECT CAST(1 AS BIT) AS success, 'UPDATED' AS status,
       'Reserva modificada correctamente.' AS message;

END TRY
BEGIN CATCH
IF @@TRANCOUNT > 0 ROLLBACK;

        DECLARE @msg NVARCHAR(4000) = ERROR_MESSAGE();

SELECT CAST(0 AS BIT) AS success, 'ERROR' AS status,
       @msg AS message;
END CATCH
END;
GO