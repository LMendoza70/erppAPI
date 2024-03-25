package com.gisnet.erpp.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;

import org.springframework.core.io.ClassPathResource;

public class Constantes {
	public static final long ID_TIPO_ACLARACION                        = 1;
	public static final long OBSERVACION_ACLARACION                    = 1;
	public static final Long TIPO_ACTO_CERTIFICADO_LG_CAUTELAR_INSERTO = 210L;
	public static final Long CONSTITUCION_PERSONA_JURIDICA_TIPO_ACTO_ID = 110L;
	public static final Long CONDOMINIO_TIPO_ACTO_ID = 39L;
	public static final Long FRACCIONAMIENTO_TIPO_ACTO_ID = 40L;
	public static final Long LOTIFICACION_TIPO_ACTO_ID = 245L;
	public static final Long RELOTIFICACION_TIPO_ACTO_ID = 246L;
	public static final Long TIPO_ACTO_FIDEICOMISO_ID = 14L;
	public static final Long PERMUTA_TIPO_ACTO_ID = 13L;
	public static final Long TIPO_ACTO_AVISO_CAUTELAR                  = 123L;
	public static final Long ID_TIPO_DOCUMENTO_ESCRITURA                  = 1L;
	public static final Long TIPO_ACTO_HIPOTECA		                   = 9L;
	public static final Long TIPO_ACTO_ANOTACION_DOMINIO_PARCIAL	   = 220L;
	public static final Long TIPO_ACTO_MODIFICACION_MEDIDAS_LINDEROS   = 43L;
	public static final Long TIPO_ACTO_ANOTACION					   = 233L;
	public static final Long REGIMEN_PROPIEDAD_CONDOMINIO				=570L;
	public static final Long TIPO_ACTO_AUTORIZACION_FRACCIONAMIENTO		=571L;
	public static final Long TIPO_ACTO_AUTORIZACION_LOTIFICACION 		=572L;
	public static final Long TIPO_ACTO_AUTORIZACION_SUBDIVICION 		=573L;
	public static final Long MODULO_CAMPO_DATOS_PREDIO					=20230L;
	public static final Long MODULO_CAMPO_NOMBRE_SOLICITANDO			=106L;
	public static final Long MODULO_CAMPO_PRIMER_APELLIDO				=107L;
	public static final Long MODULO_CAMPO_SEGUNDO_APELLIDO				=108L;
	public static final Long MODULO_CAMPO_NOMBRE_SOLICITANDO_CLG		=1300L;
	public static final Long MODULO_CAMPO_PRIMER_APELLIDO_CLG			=476L;
	public static final Long MODULO_CAMPO_SEGUNDO_APELLIDO_CLG			=477L;
	public static final Long MODULO_CAMPO_DESCRIPCION_ANOTACION 		=1668L;
	public static final Long MODULO_CAMPO_ANIO_INSCRIPCION 		        =20722L;
	public static final Long MODULO_CAMPO_MES_INSCRIPCION 		        =20721L;
	public static final Long MODULO_CAMPO_DIA_INSCRIPCION 		        =20720L;
	public static final Long MODULO_CAMPO_CHECK_ACTO 		            =20762L;
	public static final Long MODULO_CAMPO_ACTO 		                    =20761L;
	public static final String TIPO_TRAMITE_NOMBRE 						="ANOTACIÓN";
	public static final Long TIPO_ACTO_CEDULAS_HIPOTECARIAS 			= 504L;
	public static final Long TIPO_ACTO_EMBARGOS 						= 505L;
	public static final Long TIPO_ACTO_INSTITUCIONES_CREDITO 			= 509L;
	public static final Long TIPO_ACTO_FIRA 							= 510L;
	public static final Long TIPO_ACTO_RECONOCIMIENTO_DE_ADEUDO  		= 511L;
	public static final Long TIPO_ACTO_PROCAMPO 						= 512L;
	public static final Long TIPO_ACTO_CREDITO_SIMPLE 					= 513L;
	public static final Long TIPO_ACTO_COPIA_CERTIFICADA				= 515L;
	public static final Long TIPO_ACTO_COPIA_SIMPLE						= 516L;
	public static final Long TIPO_ACTO_APORTACIONES_DE_BIENES			= 521L;
	public static final Long TIPO_ACTO_ARRENDAMIENTO 					= 543L;
	public static final Long TIPO_ACTO_GARANTIA_REAL 					= 544L;
	public static final Long TIPO_ACTO_SUBROGACION 						= 545L;
	public static final Long TIPO_ACTO_COMODATO 						= 546L;
	public static final Long TIPO_ACTO_PROPIEDAD_POSESION 				= 547L;
	public static final Long TIPO_ACTO_FIDEICOMISO 						= 548L;
	public static final Long TIPO_ACTO_TRANSMICION_DE_PROPIEDAD 		= 549L;
	public static final Long TIPO_ACTO_FIANZAS							= 550L;
	public static final Long TIPO_ACTO_CREDITO_INSCRITO 				= 551L;
	public static final Long FUSION_TIPO_ACTO_ID = 41L;//CV
	public static final Long TIPO_ACTO_FRUTOS_PENDIENTES 				= 552L;
	public static final Long TIPO_ACTO_USUFRUCTO 						= 219L;
	public static final Long TIPO_ACTO_RESARVA_DOMINIO					= 223L;
	public static final Long TIPO_ACTO_DIVISION 						= 554L;
	public static final Long TIPO_ACTO_EMISIONES_DE_ACCIONES			= 559L;
	public static final Long TIPO_ACTO_AUTO_DE_DESIGNACIÓN				= 562L;
	public static final Long TIPO_ACTO_CONVENIOS_JUDICIALES 			= 589L;
	public static final Long TIPO_ACTO_SERVIDUMBRE 						= 590L;
	public static final Long TIPO_ACTO_CONTRATOS_ENTRE_PARTICULARES 	= 591L;
	public static final Long TIPO_ACTO_CREDITO_AGRICOLA 				= 594L;
	public static final Long TIPO_ACTO_INFONAVIT 						= 597L;
	public static final Long TIPO_ACTO_CONSTRUCCION_MANIFESTACION_OBRA 	= 518L;
	public static final Long TIPO_ACTO_INFORMACION_POSESORA				= 528L;
	public static final Long TIPO_ACTO_LIBERTAD_GRAVAMEN 				= 575L;
	public static final Long TIPO_ACTO_CERTIFICADO_LG					= 203L;
	public static final Long TIPO_ACTO_CERTIFICADO_LG_PRIMER_AVISO 		= 123L;
	public static final Long TIPO_ACTO_PRIMER_AVISO 					= 210L;
	public static final Long TIPO_ACTO_PRIMER_AVISO2 					= 49L;
	public static final Long TIPO_ACTO_SEGUNDO_AVISO 					= 50L;
	public static final Long TIPO_ACTO_ANOTACION_RECTIFICACION 			= 45L;
	public static final String TIPO_ANTECEDENTE							= "ANTECEDENTE";
	public static final String TIPO_LIBRO								= "LIBRO";
	public static final Long CREAR_ARCHIVO								= 1L;
	public static final Long UPDATE_ARCHIVO								= 2L;
	public static final Long ROL_ADMIN									= 1L;
	public static final Long ROL_DIR_GRAL								= 2L;
	public static final Long ROL_DIR_OFI								= 3L;
	public static final Long IMPRESION 									= 215L;
    public static final Long COPIAS_SIMPLES 							= 214L;
    public static final Long COPIAS_CERTIFICADAS 						= 202L;
    //CAMPOS PARA el clg en el reporte
    public static final Long OBSERVACIONES 								= 431L;
    public static final Long TIPO_DE_SOLICITANTE 						= 201258L;
    
    public static final Long NO_NOTARIO									=20730L;
    public static final Long ESTADO										=20731L;
    public static final Long MUNICIPIO									=20732L;
    public static final Long NOMBRE										=20727L; 
    public static final Long PATERNO									=20728L;
    public static final Long MATERNO									=20729L;
	///////////FIN
	// CAMPOS PARA CLG y AVISO
	public static final Long NOTARIO									=1310L;
	public static final Long FECHA_AVISO								=1311L;
	public static final long MAYOR_DE_EDAD_CAMPO_VALOR_ID = 432;
	public static final long MENOR_DE_EDAD_CAMPO_VALOR_ID = 431;
	public static final long AVISO_MODULO_CAMPO_ID_NOMBRE = 526L;
	public static final long AVISO_MODULO_CAMPO_ID_PATERNO = 994L;
	public static final long AVISO_MODULO_CAMPO_ID_MATERNO = 995L;

	public static final Long RECTIFICACION_MEDIDAS_TIPO_ACTO_ID = 43L;
	
	//ACTOS PUBLICITARIOS
	public static final long AFECTA_FOLIO_CAMPO_ID = 314;
	public  static final long FOLIO_AFECTADO_CAMPO_ID = 51;
	
	public static  int PAGE_SIZE_REPORT = 20;
    
	public enum ClasifActo{	TRASLATIVOS(1L), 
		
		GRAMAVAMES_LIMITATANES(2L),
		CONVENIOS_MODIFICATORIOS(3L),
		ACTOS_MODIFICATORIOS_DEL_INMUEBLE(4L), 
		CANCELACIONES(5L),
		ANOTACIONES(6L),
		TRAMITES_MIXTOS(7L), 
		PERSONAS_JURIDICAS(8L), 
		CERTIFICACIONES(9L),
		REGISTRO_POR_ORDENAMIENTO_DE_AUTORIDAD(17L),
		AVISOS(23L),
		ACTOS_PUBLICITARIOS(30L), 
		ACTOS_PUBLICITARIOSHIS(33L);
	private Long idClasifActo;
	private ClasifActo(Long c){
		idClasifActo= c;
	}

	public Long getIdClasifActo(){
		return idClasifActo;
	}
	private static HashMap<Long, ClasifActo> map = new HashMap<Long, ClasifActo>(20);
    static
    {
        for (ClasifActo  type : ClasifActo.values())
        {
        	map.put(type.idClasifActo, type);
        }
    }


    public static ClasifActo getClasifActo(long idClasifActo)
    {
        return map.get(idClasifActo);
    }

	@Override
	public String toString() {
		return idClasifActo.toString();
	}
	};

	public enum Status{	INGRESO_POR_VENTANILLA(0L),RECIBIDO_POR_VENTANILLA(1L), ASIGNADOR_A_VALIDADOR_DE_PREDIOS(2L), EN_PROCESO_ASIGNACION(3L), ASIGNADO_A_ANALISTA(4L),
		ASIGNADO_A_CALIFICADOR(5L), ASIGNADO_A_COORDINADOR(6L), LISTO_PARA_ENTREGARSE(7L),  ENTREGADO_AL_USUARIO(8L),DEVUELTO_A_ANALISTA_CYVF(9L),RECIBIDO_PENDIENTE_TURNAR(3L),
		EN_ACLARACION_ANTECEDENTE(11L), ANALISTA_VIRTUAL(14L), SUSPENDIDA_ANALISTA(15L), SUSPENDIDA_CALIFICADOR(16L), SUSPENDIDA_REGISTRADOR(17L),
		SOPORTE_A_OPERACION(20L), PENDIENTE_DEVOLUCION_DOCUMENTO(22L),DEVOLUCION_DOCUMENTO_AUTORIZADO(23L),DEVOLUCION_DOCUMENTO_ENTREGADO(24L), DIGITALIZA_DEVOLUCION_DOCUMENTO(25L),
		ERROR_SIENDO_ATENDIDO_POR_EL_SISTEMA(1000L),SOPORTE_OPERACION_MIGRACION(21L),
		RECHAZADO(10L)//CV
		;
	private Long idStatus;
	private Status(Long c){
		idStatus= c;
	}

	public Long getIdStatus(){
		return idStatus;
	}
	public static Status fromLong(long i) {
		for (Status b : Status.values()) {
			if (b.getIdStatus() == i) { return b; }
		}
		return null;
	}
	
	private static HashMap<Long, Status> map = new HashMap<Long, Status>(20);
    static
    {
        for (Status  type : Status.values())
        {
        	map.put(type.idStatus, type);
        }
    }


    public static Status getStatus(long idStatus)
    {
        return map.get(idStatus);
    }

	@Override
	public String toString() {
		return idStatus.toString();
	}
	};

	public enum StatusActo{	ACTIVO(1L), EN_PROCESO(2L), TEMPORAL(3L), INVALIDO(4L), RECHAZADO(5L);
	private Long idStatusActo;
	private StatusActo(Long c){
		idStatusActo= c;
	}

	public Long getIdStatusActo(){
		return idStatusActo;
	}
	private static HashMap<Long, StatusActo> map = new HashMap<Long, StatusActo>(20);
    static
    {
        for (StatusActo  type : StatusActo.values())
        {
        	map.put(type.idStatusActo, type);
        }
    }


    public static StatusActo getStatusActo(Long idStatusActo)
    {
        return map.get(idStatusActo);
    }

	@Override
	public String toString() {
		return idStatusActo.toString();
	}
	};
	
	public enum Area{	PROPIEDAD(1L), AVISOS(2L), CERTIFICACIONES(3L), COPIAS(4L), BUSQUEDAS(6L),ACTOS_PUBLICITARIOS(12L),PERSONAS_MORALES(9l);
		private Long idArea;
		private Area(Long c){
			idArea= c;
		}

		public Long getIdArea(){
			return idArea;
		}
		private static HashMap<Long, Area> map = new HashMap<Long, Area>(20);
	    static
	    {
	        for (Area  type : Area.values())
	        {
	        	map.put(type.idArea, type);
	        }
	    }


	    public static Area getArea(Long idArea)
	    {
	        return map.get(idArea);
	    }

		@Override
		public String toString() {
			return idArea.toString();
		}
		};
		
	public enum TipoInconformidad{ SUSPENCION(1L),DENEGACION(2L);
		private Long idTipoInconformidad;
		private TipoInconformidad(Long c) {
			idTipoInconformidad = c;
		}
		public Long getIdTipoInconformidad() {
			return idTipoInconformidad;
		}
		private static HashMap<Long, TipoInconformidad> map = new HashMap<Long, TipoInconformidad>(10);
	    static
	    {
	        for (TipoInconformidad  type : TipoInconformidad.values())
	        {
	        	map.put(type.idTipoInconformidad, type);
	        }
	    }

	    public static TipoInconformidad getTipoInconformidad(int idTipoInconformidad)
	    {
	        return map.get(idTipoInconformidad);
	    }

	}	
	public enum TipoRelPersona{	PROPIETARIO(1L), BENEFICIARIO(2L), APODERADO(3L), SOCIO(4L), ORGANO_ADMINISTRACION(5L);
	private Long idTipoRelPersona;
	private TipoRelPersona(Long c){
		idTipoRelPersona= c;
	}

	public Long getIdTipoRelPersona(){
		return idTipoRelPersona;
	}
	private static HashMap<Long, TipoRelPersona> map = new HashMap<Long, TipoRelPersona>(20);
    static
    {
        for (TipoRelPersona  type : TipoRelPersona.values())
        {
        	map.put(type.idTipoRelPersona, type);
        }
    }


    public static TipoRelPersona getTipoRelPersona(int idTipoRelPersona)
    {
        return map.get(idTipoRelPersona);
    }

	@Override
	public String toString() {
		return idTipoRelPersona.toString();
	}
	};




	public enum Prioridad{	NORMAL(1L), AVISO(2L), ALTA(3L), CRITICA(4L);
	private Long idPrioridad;
	private Prioridad(Long c){
		idPrioridad= c;
	}

	public Long getIdPrioridad(){
		return idPrioridad;
	}
	private static HashMap<Long, Prioridad> map = new HashMap<Long, Prioridad>(20);
    static
    {
        for (Prioridad  type : Prioridad.values())
        {
        	map.put(type.idPrioridad, type);
        }
    }


    public static Prioridad getPrioridad(int idPrioridad)
    {
        return map.get(idPrioridad);
    }

	@Override
	public String toString() {
		return idPrioridad.toString();
	}
	};

	public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";
	public static final String REPORTES_PATH= new ClassPathResource("/reports/").getPath();
	public static final String OFICIOS_PATH= new ClassPathResource("/reports/").getPath();
	public static final String IMG_PATH= new ClassPathResource("/static/img/").getPath();
	public static final Long USUARIO_ID_SISTEMA = 0L;
	public static final Long USUARIO_ID_DEFAULT = 0L;
	public static final Locale locale = new Locale( "es", "MX" );

	public static final Character SI = 'S';
	public static final Character NO = 'N';

	public static final String STR_NO = "NO";
	public static final String STR_SI = "SI";
		public static final String DATE_PATTERN="dd/MM/yyyy";

	public static final String DATETIME_PATTERN="dd/MM/yyyy HH:mm";

	public static final String PAGO_MINIMO_KEY = "PAGO_MINIMO";
	public static final String PAGO_TOPE_KEY = "PAGO_TOPE";

	public static final int SIZE_NUM_FOLIO = 6;
	public static final int SIZE_DOCUMENTO = 4;
	public static final String CHAR_PADDING_NUM_FOLIO = "0";
	public static final String QUEUE_NAME_TURNADOR = "turnador";
	public static final String QUEUE_NAME_ANALISADO_POR_SISTEMA = "analisado_por_sistema";
	public static final String KEY_OFICINA_SELECTOR = "idOficina";
	public static final Integer PONDERACION_INICIAL = Integer.valueOf(0);
	public static final Integer TURNAR_TODOS = Integer.valueOf(1);
	public static final String DOC_APERTURA = "09980";
	public static final String DOC_CIERRE = "09990";
	public static final String RUTA_FOLIO_CVE = "RUTA_FOLIO";
	public static final String RUTA_LIBRO_CVE = "RUTA_LIBRO_";
	public static final String RUTA_PRELACION = "RUTA_PRELACION";
	public static final String RUTA_ASIENTOS_CVE = "RUTA_ASIENTOS_PDF";
	public static final String COTIZADOR_MENSAJE_CVE = "COTIZADOR_MENSAJE";
	public static final String FUNDAMENTO_JURIDICO_CVE = "FUNDAMENTO_JURIDICO";
	public static final int SIZE_FOLIO = 10;
	public static final Integer PONDERACION_CYVF = Integer.valueOf(1);
	public static final String CAPETA_INSCRIPCIONES = "1";

	public static final String PROD_ENVIRONMENT = "prod";

	public static final String SUPPORT_TYPES[] = {"JPG","JPEG", "PNG", "GIF","PDF"};
	public static final String FECHA_FORMATO_CAMPO_VALOR = "yyyy-mm-dd";
	public static final String RUTA_DOCTO_CVE = "RUTA_DOCTO";


	public enum StatusPredio{	VIGENTE(1L), NO_VIGENTE(2L);
	private Long idStatusPredio;
	private StatusPredio(Long c){
		idStatusPredio= c;
	}

	public Long getIdStatusPredio(){
		return idStatusPredio;
	}
	private static HashMap<Long, StatusPredio> map = new HashMap<Long, StatusPredio>(2);
    static
    {
        for (StatusPredio  type : StatusPredio.values())
        {
        	map.put(type.idStatusPredio, type);
        }
    }


    public static StatusPredio getStatusPredio(int idStatusPredio)
    {
        return map.get(idStatusPredio);
    }

	@Override
	public String toString() {
		return idStatusPredio.toString();
	}
	};

	public static final long SERVICIO_CERTIFICADO_LIBERTAD_GRAVAMEN_ID=11L;

	public enum Servicio{								
					COPIAS_SIMPLE(20L),COPIAS_CERTIFICADA(23L),IMPRESION_FOLIOS(16L),BUSQUEDA_SIMPLE(29L),BUSQUEDA_HISTORIAL(9L),CERTIFICADO_HISTORIAL(1L),CERTIFICADO_JURIDICO(14L),CERTIFICADO_LIBERTAD_GRAVAMEN_ID(11L),CERTIFICADO_LIBERTAD_NO_INSCRIPCION(10L),CERTIFICADO_LIBERTAD_GRAVAMEN_CAUTELAR_ID(12L),CERTIFICADO_LIBERTAD_NO_PROPIEDAD(13L), COPIA_CERTIFICADA_DE_ACTOS_PUBLICITARIOS(46L);							
		
		private Long idServicio;
		private Servicio(Long c){
			idServicio= c;
		}

		public Long getIdServicio(){
			return idServicio;
		}
	};


	public enum TipoCampo {
		TEXTO(1L), ENTERO(2L), FLOTANTE(3L), DINERO(4L), FECHA(5L), PORCENTAJE(6L), EMAIL(7L), BIT(8L), ESTADO(9L), MUNICIPIO(10L), TIENE_BENEFICIARIO(11L), EN_CALIDAD_DE(15L), PROPIETARIO(16L),
		PREDIO(17L), PERSONA(19L), DESIGNANTE_DE_ADQUIRIENTES(20L), USUFRUCTO_VITALICIO(21L), DOMINIO_DIRECTO(22L), BOOLEAN_(23L), LISTA_CON_VALORES(24L), TIPO_PERSONA(25L), CAMPO_VALORES(26L),
		DISPLAY(27L), PARENTESCO(28L), PERSONA_PREDIO_TITULAR(29L), PERSONA_PREDIO_ADQUIRIENTE(30L), PERSONA_NOMBRE(31L), DESIGNANTE_DE_TITULAR(32L), USUFRUCTO_VITALICIO_ADQUIRIENTE(33L),
		DOMINIO_DIRECTO_ADQUIRIENTE(34L), USUFRUCTO_VITALICIO_TITULAR_A_TRANSMITIR(35L), DOMINIO_DIRECTO_TITULAR_A_TRANSMITIR(36L), NOTARIO(37L), DOMINIO_DIRECTO_BENEFICIARIO(38L),
		USUFRUCTO_VITALICIO_BENEFICIARIO(39L), DATOS_DEL_OFICIO(40L), PERSONA_NACIONALIDAD(41L), PERSONA_ACREDITADO(42L), JUEZ(43L), UNIDAD_ADMINISTRATIVA(44L), ACTO(45L),
		TIPO_CANCELACION(46L), PREDIO_FUSIONAR(47L), SUPERFICIE(48L), SUPERFICIE_SUBDIVIDIDA(49L), FRACCION_SUBDIVIDIDA(50L), COLINDANCIA(51L), LISTA_COLINDANCIAS(52L),
		ORIENTACION(53L), TIPO_COLINDANCIA(54L), PREDIO_DETALLE_FORMA(55L), NOMBRE(56L),PRIMER_APELLIDO(57L), SEGUNDO_APELLIDO(58L), CURP(59L), RFC(60L), FECHA_DE_NACIMIENTO(61L),		
		PRELACION_CONCECUTIVO(66L),	
		SUPERFICE_A_TRANSMITIR(74L), ASOCIA_ACTO_RECIBO(75L), LISTA_ACTOS_POR_TIPO_ACTO_TIPO_ACTO(76L), FECHA_REGISTRO_ACTO(77L),
		PREDIOS_PERMUTA(110L),ACTIVACION_TRANSMITENTE(78L),OBJETO_PJ(120L),FOLIO_MATRIZ(121L),NUMERO_ACTO_PUBLICITARIO(124L),FOLIO_PERSONA_FISICA(125L);
		private Long idTipoCampo;

		private TipoCampo(Long c) {
			idTipoCampo = c;
		}

		public Long getIdTipoCampo() {
			return idTipoCampo;
		}

		private static HashMap<Long, TipoCampo> map = new HashMap<Long, TipoCampo>(50);
		static {
			for (TipoCampo type : TipoCampo.values()) {
				map.put(type.idTipoCampo, type);
			}
		}

		public static TipoCampo getTipoCampo(long idTipoCampo) {
			return map.get(idTipoCampo);
		}

		@Override
		public String toString() {
			return idTipoCampo.toString();
		}
	};



	public enum TipoEntrada{	ENTRADA(1L), SALIDA(2L), SECUNDARIO(3L);
	private Long idTipoEntrada;
	private TipoEntrada(Long c){
		idTipoEntrada= c;
	}

	public Long getIdTipoEntrada(){
		return idTipoEntrada;
	}
	private static HashMap<Long, TipoEntrada> map = new HashMap<Long, TipoEntrada>(2);
    static
    {
        for (TipoEntrada  type : TipoEntrada.values())
        {
        	map.put(type.idTipoEntrada, type);
        }
    }


    public static TipoEntrada getTipoEntrada(long idTipoEntrada)
    {
        return map.get(idTipoEntrada);
    }

	@Override
	public String toString() {
		return idTipoEntrada.toString();
	}
	};
	
	public enum TipoVisorImagen{	FOLIO(1L), LIBRO(2L), ANTECEDENTE(3L);
		private Long idTipoVisorImagen;
		private TipoVisorImagen(Long c){
			idTipoVisorImagen= c;
		}

		public Long getIdTipoVisorImagen(){
			return idTipoVisorImagen;
		}
		private static HashMap<Long, TipoVisorImagen> map = new HashMap<Long, TipoVisorImagen>(3);
	    static
	    {
	        for (TipoVisorImagen  type : TipoVisorImagen.values())
	        {
	        	map.put(type.idTipoVisorImagen, type);
	        }
	    }


	    public static TipoVisorImagen getTipoVisorImagen(int idTipoVisorImagen)
	    {
	        return map.get(idTipoVisorImagen);
	    }

		@Override
		public String toString() {
			return idTipoVisorImagen.toString();
		}
		};



	public enum Rol{	ANALISTA(6L),VALIDADOR(5L),REGISTRADOR(19L), CALIFICADOR(7L);
		private Long idRol;
		private Rol(Long idRol){
			this.idRol= idRol;
		}

		public Long getIdRol(){
			return this.idRol;
		}

	};

	public enum ETipoFolio {  PREDIO(4), MUEBLE(3), PERSONAS_AUXILIARES(2), PERSONAS_JURIDICAS(1),;
		public Integer idTipoFolio;
		private ETipoFolio (Integer idTipoFolio) {
			this.idTipoFolio = idTipoFolio;
		}

		public Integer getTipoFolio () {
			return this.idTipoFolio;
		}

		public static ETipoFolio fromInt(int i) {
			for (ETipoFolio b : ETipoFolio.values()) {
				if (b.getTipoFolio() == i) { return b; }
			}
			return null;
		}
		
		public static ETipoFolio fromLong(long i) {
			for (ETipoFolio b : ETipoFolio.values()) {
				if (b.getTipoFolio() == i) { return b; }
			}
			return null;
		}


		private static HashMap<Integer, ETipoFolio> map = new HashMap<Integer, ETipoFolio>(4);
		static
		{
			for (ETipoFolio  type : ETipoFolio.values())
			{
				map.put(type.idTipoFolio, type);
			}
		}
	}

	public enum TipoPersona{ FISICA(1L), MORAL(2L);
		private Long idTipoPersona;
		private TipoPersona(Long c){
			idTipoPersona= c;
		}

		public Long getIdTipoPersona(){
			return idTipoPersona;
		}
		private static HashMap<Long, TipoPersona> map = new HashMap<Long, TipoPersona>(2);
	    static
	    {
	        for (TipoPersona  type : TipoPersona.values())
	        {
	        	map.put(type.idTipoPersona, type);
	        }
	    }


	    public static TipoPersona getTipoPersona(int idTipoPersona)
	    {
	        return map.get(idTipoPersona);
	    }

		@Override
		public String toString() {
			return idTipoPersona.toString();
		}
	};

	public enum ETipoPersona {  FISICA(2 ), MORAL(3), JURIDICO(4);
		private Integer idTipoPersona;
		private ETipoPersona (Integer idTipoPersona) {
			this.idTipoPersona = idTipoPersona;
		}

		public Integer getAsInteger () {
			return this.idTipoPersona;
		}

		public static ETipoPersona valueOf(int i) {
			for (ETipoPersona b : ETipoPersona.values()) {
				if (b.getAsInteger() == i) { return b; }
			}
			return null;
		}

		private static HashMap<Integer, ETipoPersona> map = new HashMap<Integer, ETipoPersona>(4);
		static
		{
			for (ETipoPersona  type : ETipoPersona.values())
			{
				map.put(type.idTipoPersona, type);
			}
		}

	}
	//CV
	public enum tipoBoleta {

		BOLETA_INGRESO(1L),BOLETA_REGISTRAL(2L),BOLETA_RECHAZO(3L);

		private Long id;

		private tipoBoleta(Long c){
		id= c;
		}

		public Long getId() {
			return id;
		}
	};
	
	public enum Resultado{ TERMINADO (1L), RECHAZADO(2L), PARCIALMENTE_RECHAZADO(3L), SUSPENDIDO(4L);
		private Long idResultado;
		private Resultado(Long c){
			idResultado= c;
		}

		public Long getIdResultado(){
			return idResultado;
		}
		private static HashMap<Long, Resultado> map = new HashMap<Long, Resultado>(3);
	    static
	    {
	        for (Resultado  type : Resultado.values())
	        {
	        	map.put(type.idResultado, type);
	        }
	    }


	    public static Resultado getResultado(int idResultado)
	    {
	        return map.get(idResultado);
	    }

		@Override
		public String toString() {
			return idResultado.toString();
		}
	};
	
	public enum TipoUsuario{	ERPP(8L), NOTARIO(2L);
	private Long idTipoUsuario;
	private TipoUsuario(Long c){
		idTipoUsuario= c;
	}

	public Long getIdTipoUsuario(){
		return idTipoUsuario;
	}
	private static HashMap<Long, TipoUsuario> map = new HashMap<Long, TipoUsuario>(20);
    static
    {
        for (TipoUsuario  type : TipoUsuario.values())
        {
        	map.put(type.idTipoUsuario, type);
        }
    }


    public static TipoUsuario getTipoUsuario(int idTipoUsuario)
    {
        return map.get(idTipoUsuario);
    }

	@Override
	public String toString() {
		return idTipoUsuario.toString();
	}
	};
}
