package com.gisnet.erpp.service;

import static java.lang.Math.toIntExact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.PrelacionBoleta;
import com.gisnet.erpp.repository.PrelacionBoletaRepository;
import com.gisnet.erpp.repository.TipoBoletaRepository;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.BoletaRechazoVO;
import com.gisnet.erpp.vo.PrelacionIngresoVO;
import com.gisnet.erpp.vo.prelacion.PrelacionBoletaRegistralVO;


@Service
public class PrelacionBoletaService {
	
	private final static Logger log = LoggerFactory.getLogger(PrelacionBoletaService.class);
	
	@Autowired
	private PrelacionBoletaRepository prelacionBoletaRepository;

	@Autowired
	private PrelacionService prelacionService;

	@Autowired
	private BoletaRegistralService boletaRegistralService;

	@Autowired
	private TipoBoletaRepository tipoBoletaRepository;
	
	@Autowired
	private ActoService actoService;
	
	@Autowired PrelacionBoletaService prelacionBoletaService;

	private final ObjectMapper mapper = new ObjectMapper();
	
	public PrelacionBoletaService() {
		mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}
	
	public PrelacionBoleta save(Long prelacionId, Long tipo,Prelacion _prelacion) {
		
		Prelacion prelacion = prelacionService.findOne(prelacionId);
		PrelacionBoleta prelBoleta = new PrelacionBoleta();
		

		if (tipo.equals(Constantes.tipoBoleta.BOLETA_INGRESO.getId())) {
			PrelacionIngresoVO prelIngresoVo = new PrelacionIngresoVO();
			prelIngresoVo = prelacionService.findOneVO(prelacion.getId());
			prelBoleta.setTipoBoleta(tipoBoletaRepository.findOne(Constantes.tipoBoleta.BOLETA_INGRESO.getId()));
			prelBoleta.setDatos(classToJson(prelIngresoVo));
			prelBoleta.setPrelacion(prelacion);
			prelacionBoletaRepository.save(prelBoleta);
		}

		if (tipo.equals(Constantes.tipoBoleta.BOLETA_REGISTRAL.getId())) {
			System.out.println("GUARDANDO BOLETA_REGISTRAL A JSON");
			PrelacionBoletaRegistralVO prelBoletaRegistralVo = new PrelacionBoletaRegistralVO();
			List<PrelacionBoletaRegistralVO> prelBoletaRegistralVoList = new ArrayList<PrelacionBoletaRegistralVO>();
			//if(isFusion(prelacionId)) {
			//	prelBoletaRegistralVoList = boletaRegistralService.findVOPredios(prelacion.getId(),false,null);
			//	prelBoleta.setDatos(classToJson(prelBoletaRegistralVoList));
			//}else {
				Page<Acto> actos = actoService.getActosPageReport(prelacionId,100);
				ExecutorService executor = Executors.newFixedThreadPool(actos.getTotalPages());
				CountDownLatch latch = new CountDownLatch(actos.getTotalPages());
				List<Integer> paginas =  new ArrayList<>();
				for(int i=0;i<actos.getTotalPages();i++) {
					paginas.add(i);
				}
				paginas.forEach(x->{
					Runnable runnableTask = () -> {  
						List<PrelacionBoletaRegistralVO> list = boletaRegistralService.findVOPredios(prelacion.getId(),true,x,true,_prelacion);
						//prelacionBoletaService.save(prelacion,list);
					};
					executor.execute(runnableTask);
				});
				
				executor.shutdown();
			
			//}
			
			
		}

		if (tipo.equals(Constantes.tipoBoleta.BOLETA_RECHAZO.getId())) {
			BoletaRechazoVO prelBoletaRechazoVo = new BoletaRechazoVO();
			prelBoletaRechazoVo = boletaRegistralService.findOneVOBoletaRechazo(prelacion.getId());
			prelBoleta.setTipoBoleta(tipoBoletaRepository.findOne(Constantes.tipoBoleta.BOLETA_RECHAZO.getId()));
			prelBoleta.setDatos(classToJson(prelBoletaRechazoVo));
			prelBoleta.setPrelacion(prelacion);
			prelacionBoletaRepository.save(prelBoleta);
		}

	

		return prelBoleta;
	}
	
	public void save(Prelacion prelacion,List<PrelacionBoletaRegistralVO> list){
		PrelacionBoleta pb = new PrelacionBoleta();
		pb.setTipoBoleta(tipoBoletaRepository.findOne(Constantes.tipoBoleta.BOLETA_REGISTRAL.getId()));
		pb.setPrelacion(prelacion);
		pb.setDatos(classToJson(list));
		prelacionBoletaRepository.save(pb);
	}

	public PrelacionBoleta save(Long prelacionId, Long tipo,List<PrelacionBoletaRegistralVO> findVOPredios) {
		
		Prelacion prelacion = prelacionService.findOne(prelacionId);
		PrelacionBoleta prelBoleta = new PrelacionBoleta();
		

		if (tipo.equals(Constantes.tipoBoleta.BOLETA_INGRESO.getId())) {
			PrelacionIngresoVO prelIngresoVo = new PrelacionIngresoVO();
			prelIngresoVo = prelacionService.findOneVO(prelacion.getId());
			prelBoleta.setTipoBoleta(tipoBoletaRepository.findOne(Constantes.tipoBoleta.BOLETA_INGRESO.getId()));
			prelBoleta.setDatos(classToJson(prelIngresoVo));
		}

		if (tipo.equals(Constantes.tipoBoleta.BOLETA_REGISTRAL.getId())) {
			System.out.println("GUARDANDO BOLETA_REGISTRAL A JSON");

			if(isFusion(prelacionId)) {				
				prelBoleta.setDatos(classToJson(findVOPredios));
			}else {				
				prelBoleta.setDatos(classToJson(findVOPredios));
			}
			prelBoleta.setTipoBoleta(tipoBoletaRepository.findOne(Constantes.tipoBoleta.BOLETA_REGISTRAL.getId()));
			
		}

		if (tipo.equals(Constantes.tipoBoleta.BOLETA_RECHAZO.getId())) {
			BoletaRechazoVO prelBoletaRechazoVo = new BoletaRechazoVO();
			prelBoletaRechazoVo = boletaRegistralService.findOneVOBoletaRechazo(prelacion.getId());
			prelBoleta.setTipoBoleta(tipoBoletaRepository.findOne(Constantes.tipoBoleta.BOLETA_RECHAZO.getId()));
			prelBoleta.setDatos(classToJson(prelBoletaRechazoVo));
		}

		prelBoleta.setPrelacion(prelacion);
		prelacionBoletaRepository.save(prelBoleta);

		return prelBoleta;
	}


	public List<PrelacionBoleta> findAllByPrelacion(Long prelacionId){
		return prelacionBoletaRepository.findByPrelacionIdOrderByIdAsc(prelacionId);
	}
	
	public List<PrelacionBoleta> findAllByPrelacion(Long prelacionId,Integer pagina){
		return prelacionBoletaRepository.findByPrelacionIdAndPagina(prelacionId,pagina);
	}
	
	
	public PrelacionBoleta findFirstByPrelacion(Long prelacionId,Integer pagina){
		return prelacionBoletaRepository.findFirstByPrelacionIdAndPaginaOrderByIdDesc(prelacionId,pagina);
	}

	public List<PrelacionBoleta> findAllByPrelacion(Long prelacionId,Long tipoBoleta){
		return prelacionBoletaRepository.findByPrelacionIdAndTipoBoletaId(prelacionId,tipoBoleta);
	}
	
	public Optional<PrelacionBoleta> findFirstByPrelacionIdOrderByPaginaDesc(Long prelacionId){
		return prelacionBoletaRepository.findFirstByPrelacionIdOrderByPaginaDesc(prelacionId);
	}
	public PrelacionBoleta findOne(Long id) {
		return prelacionBoletaRepository.findOne(id);
	}
	public PrelacionBoleta findBoletaEnd(Long prelacionId) {
		return prelacionBoletaRepository.findFirstByPrelacionIdOrderByIdDesc(prelacionId);
	}

	private String classToJson(PrelacionIngresoVO object) {
		return writeJson(object);
	}

	private String classToJson(PrelacionBoletaRegistralVO object) {
		return writeJson(object);
	}
	
	private String classToJson(List<PrelacionBoletaRegistralVO> object) {
		//System.out.println("classToJson 116");
		return writeJson(object);
	}

	private String classToJson(BoletaRechazoVO object) {
		return writeJson(object);
	}

	private String writeJson(Object object) {
		//ObjectMapper mapper = new ObjectMapper();
		try {
			//System.out.println("writeJson "+mapper.writeValueAsString(object));
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return e.getMessage();
		}

		//return "";
	}

	public PrelacionIngresoVO jsonTOBoletaIngreso(String json) {
		//ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
			return mapper.readValue(json, PrelacionIngresoVO.class);
		} catch (IOException e) {

		}
		return null;
	}

	public BoletaRechazoVO jsonTOBoletaRechazo(String json) {
		//ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, BoletaRechazoVO.class);
		} catch (IOException e) {

		}
		return null;
	}

	public List<PrelacionBoletaRegistralVO> jsonTOBoletaRegistral(String json) {
		//mapper = new ObjectMapper();
		log.info("jsonTOBoletaRegistral: " + json);
		try {
			return Arrays.asList(mapper.readValue(json, PrelacionBoletaRegistralVO[].class));
		} catch (IOException e) {
			log.trace(e.getCause().getMessage(), e);
		}
		return null;
	}
	
	public List<PrelacionBoletaRegistralVO> jsonTOBoletaRegistralList(String json) {
		//mapper = new ObjectMapper();
		log.info("jsonTOBoletaRegistral: " + json);
		try {
			return Arrays.asList(mapper.readValue(json, PrelacionBoletaRegistralVO[].class));
		} catch (IOException e) {
			log.trace(e.getCause().getMessage(), e);
			e.printStackTrace();
		}
		return null;
	}
	
	//public static void main(String [] args) {
	public static void principal() {
		PrelacionBoletaService pbs = new PrelacionBoletaService();
		String json = //"{\"id\":null,\"fechaIngreso\":1548098241060,\"consecutivo\":1288,\"oficina\":\"CENTRAL\",\"observaciones\":null,\"leyendaRegistro\":\"SE PRESENTÁ PARA SU REGISTRO EL 21 DE ENERO DE 2019 A LAS 1:17 p.m.\",\"inscritoEn\":null,\"ubicacion\":\"VIALIDAD: DE LOS ARBOLES, TIPO VIALIDAD: CALZADA, ASENTAMIENTO: CENTRO, MUNICIPIO: OAXACA DE JUAREZ, ESTADO: OAXACA                   \",\"registrador\":\"ANALISTA 1 OAXACA OAXACA\",\"coordinador\":\"CENTRAL REGISTRADOR REGISTRADOR\",\"textoRegistro\":\"FOLIO 3514 POR ESCRITURA PUBLICA NÚMERO 9998876 DE FECHA 21/01/2019 OTORGADO ANTE LA FE DEL LIC. CARLOS RIVERA LOPEZ NOTARIO PUBLICO 2 DE OAXACA DE JUAREZ, OAXACA.\",\"textoConsta\":\"<b>CONSTA QUE: </b> <br>Consta que...\",\"firmaRegistrador\":\"UqiBmidYCixat5oWGZlRVhzXKe5Yz+k4m40pqRC1Z2x0mNrIr/IfLdLdEhzYUdrnL7Mw0CdZS7dCL8496A8CV+4qnikJy6RZmwo34L1Qt2BHct9kDJxtpl40d8gUjyORtLnkdHgtLXPrdqoh+wX1rv2n0hyK5bg1a2o8aw5QPFlu573BY1HQgTM5G1UOTRUtxcxNOIrmWM0EVxCJDer1ZmQSqRlSsRbCkQrTqrS4XGbA52Ko7WJ0jj+WE8GZROB9FuESpNyEuGA8hHSF5YK74CjhEoGBmbaSq1nnVxD7IjvnUhXEviAWVBTE0f81VB3rpGN5ewvcWShXs79Hu97LUQ==\\r\\n\",\"firmaCoordinador\":\"qhb7RnAWwah9FmJZzLrldmdNOIpkXrHypq2foMSVpVg2wvp5njnE5jUGYQJXdAuuqWRS4eNhbt7nWEYwm5w3Y77WU8MJ/WZHQhBQcSnSuqHCe+mm+MmZ0poYnPP7/Fha9l+GZ76lv7QWTBFCPX1vgfTbTqP5RqTe29ZDQSNJoAlZ4OYMRYPDzmJ2FZwWYM32lr24g4Vwfw4i8l6ihDEHIBGAKig5QVQ7wl7hnZlWe9Ajw0UN+ASGsN5PHhSd7QKIqGElEQIivaLPYTSDbt+cJfq1S2dhXjTmy+VMlfrbR0cE2hE8YmvFM3xOmWm05LMUbWy61lIdQQCNso+rQA7W0g==\\r\\n\",\"antecedentes\":[{\"libro\":\"A4500\",\"seccion\":\"I SECCIÓN PRIMERA REGISTRO PROPIEDAD\",\"oficina\":\"01\",\"documento\":\"9083\"}],\"predios\":[{\"folio\":3514}],\"recibos\":[{\"importe\":99.0,\"folio\":\"9999\"}],\"titulares\":[{\"nombre\":\"PAULINA ESCALANTE ESTRADA\",\"tipo\":null,\"dd\":100.0,\"uv\":100.0,\"superficie\":0.0}],\"actos\":[{\"nombre\":\"CONSTITUCION DE SERVIDUMBRE DE PASO\",\"asiento\":1548098617320}],\"acreedores\":null,\"foliosResultantes\":null,\"solicitudPago\":[null],\"registroPreventivo\":false,\"anotacionRegistroPreventivo\":null,\"afavorDe\":null}";
				"{\"id\":null,\"fechaIngreso\":1548098241060,\"consecutivo\":1288,\"oficina\":\"CENTRAL\","+
				"\"observaciones\":null,\"leyendaRegistro\":\"SE PRESENTÁ PARA SU REGISTRO EL 21 DE ENERO DE 2019 A LAS 1:17 p.m.\",\"inscritoEn\":null,"+
				"\"ubicacion\":\"VIALIDAD: DE LOS ARBOLES, TIPO VIALIDAD: CALZADA, ASENTAMIENTO: CENTRO, MUNICIPIO: OAXACA DE JUAREZ, ESTADO: OAXACA                   \","+
				"\"registrador\":\"ANALISTA 1 OAXACA OAXACA\",\"coordinador\":\"CENTRAL REGISTRADOR REGISTRADOR\",\"textoRegistro\":\"FOLIO 3514 POR ESCRITURA PUBLICA NÚMERO 9998876 DE FECHA 21/01/2019 OTORGADO ANTE LA FE DEL LIC. CARLOS RIVERA LOPEZ NOTARIO PUBLICO 2 DE OAXACA DE JUAREZ, OAXACA.\","+
				"\"textoConsta\":\"<b>CONSTA QUE: </b> <br>Consta que...\",\"firmaRegistrador\":\"UqiBmidYCixat5oWGZlRVhzXKe5Yz+k4m40pqRC1Z2x0mNrIr/IfLdLdEhzYUdrnL7Mw0CdZS7dCL8496A8CV+4qnikJy6RZmwo34L1Qt2BHct9kDJxtpl40d8gUjyORtLnkdHgtLXPrdqoh+wX1rv2n0hyK5bg1a2o8aw5QPFlu573BY1HQgTM5G1UOTRUtxcxNOIrmWM0EVxCJDer1ZmQSqRlSsRbCkQrTqrS4XGbA52Ko7WJ0jj+WE8GZROB9FuESpNyEuGA8hHSF5YK74CjhEoGBmbaSq1nnVxD7IjvnUhXEviAWVBTE0f81VB3rpGN5ewvcWShXs79Hu97LUQ==\\r\\n\","+
				"\"firmaCoordinador\":\"qhb7RnAWwah9FmJZzLrldmdNOIpkXrHypq2foMSVpVg2wvp5njnE5jUGYQJXdAuuqWRS4eNhbt7nWEYwm5w3Y77WU8MJ/WZHQhBQcSnSuqHCe+mm+MmZ0poYnPP7/Fha9l+GZ76lv7QWTBFCPX1vgfTbTqP5RqTe29ZDQSNJoAlZ4OYMRYPDzmJ2FZwWYM32lr24g4Vwfw4i8l6ihDEHIBGAKig5QVQ7wl7hnZlWe9Ajw0UN+ASGsN5PHhSd7QKIqGElEQIivaLPYTSDbt+cJfq1S2dhXjTmy+VMlfrbR0cE2hE8YmvFM3xOmWm05LMUbWy61lIdQQCNso+rQA7W0g==\\r\\n\","+
				"\"antecedentes\":[{\"libro\":\"A4500\",\"seccion\":\"I SECCIÓN PRIMERA REGISTRO PROPIEDAD\",\"oficina\":\"01\",\"documento\":\"9083\"}],\"predios\":[{\"folio\":3514}],\"recibos\":[{\"importe\":99.0,\"folio\":\"9999\"}]"+
				",\"titulares\":[{\"nombre\":\"PAULINA ESCALANTE ESTRADA\",\"tipo\":null,\"dd\":100.0,\"uv\":100.0,\"superficie\":0.0}],\"actos\":[{\"nombre\":\"CONSTITUCION DE SERVIDUMBRE DE PASO\",\"asiento\":1548098617320}],\"acreedores\":null,\"foliosResultantes\":null,\"solicitudPago\":[null],\"registroPreventivo\":false,\"anotacionRegistroPreventivo\":null,\"afavorDe\":null"+
				"}";
				//"{\"id\":\"\",\"fechaIngreso\":1548098241060,\"consecutivo\":1288,\"oficina\":\"CENTRAL\",\"observaciones\":\"\",\"leyendaRegistro\":\"SE PRESENT├ü PARA SU REGISTRO EL 21 DE ENERO DE 2019 A LAS 1:17 p.m.\",\"inscritoEn\":\"\",\"ubicacion\":\"VIALIDAD: DE LOS ARBOLES, TIPO VIALIDAD: CALZADA, ASENTAMIENTO: CENTRO, MUNICIPIO: OAXACA DE JUAREZ, ESTADO: OAXACA                   \",\"registrador\":\"ANALISTA 1 OAXACA OAXACA\",\"coordinador\":\"CENTRAL REGISTRADOR REGISTRADOR\",\"textoRegistro\":\"FOLIO 3514 POR ESCRITURA PUBLICA N├ÜMERO 9998876 DE FECHA 21/01/2019 OTORGADO ANTE LA FE DEL LIC. CARLOS RIVERA LOPEZ NOTARIO PUBLICO 2 DE OAXACA DE JUAREZ, OAXACA.\",\"textoConsta\":\"<b>CONSTA QUE: </b> <br>Consta que...\",\"firmaRegistrador\":\"UqiBmidYCixat5oWGZlRVhzXKe5Yz+k4m40pqRC1Z2x0mNrIr/IfLdLdEhzYUdrnL7Mw0CdZS7dCL8496A8CV+4qnikJy6RZmwo34L1Qt2BHct9kDJxtpl40d8gUjyORtLnkdHgtLXPrdqoh+wX1rv2n0hyK5bg1a2o8aw5QPFlu573BY1HQgTM5G1UOTRUtxcxNOIrmWM0EVxCJDer1ZmQSqRlSsRbCkQrTqrS4XGbA52Ko7WJ0jj+WE8GZROB9FuESpNyEuGA8hHSF5YK74CjhEoGBmbaSq1nnVxD7IjvnUhXEviAWVBTE0f81VB3rpGN5ewvcWShXs79Hu97LUQ==\r\n\",\"firmaCoordinador\":\"qhb7RnAWwah9FmJZzLrldmdNOIpkXrHypq2foMSVpVg2wvp5njnE5jUGYQJXdAuuqWRS4eNhbt7nWEYwm5w3Y77WU8MJ/WZHQhBQcSnSuqHCe+mm+MmZ0poYnPP7/Fha9l+GZ76lv7QWTBFCPX1vgfTbTqP5RqTe29ZDQSNJoAlZ4OYMRYPDzmJ2FZwWYM32lr24g4Vwfw4i8l6ihDEHIBGAKig5QVQ7wl7hnZlWe9Ajw0UN+ASGsN5PHhSd7QKIqGElEQIivaLPYTSDbt+cJfq1S2dhXjTmy+VMlfrbR0cE2hE8YmvFM3xOmWm05LMUbWy61lIdQQCNso+rQA7W0g==\r\n\",\"antecedentes\":[{\"libro\":\"A4500\",\"seccion\":\"I SECCI├ôN PRIMERA REGISTRO PROPIEDAD\",\"oficina\":\"01\",\"documento\":\"9083\"}],\"predios\":[{\"folio\":3514}],\"recibos\":[{\"importe\":99.0,\"folio\":\"9999\"}],\"titulares\":[{\"nombre\":\"PAULINA ESCALANTE ESTRADA\",\"tipo\":\"\",\"dd\":100.0,\"uv\":100.0,\"superficie\":0.0}],\"actos\":[{\"nombre\":\"CONSTITUCION DE SERVIDUMBRE DE PASO\",\"asiento\":1548098617320}],\"acreedores\":\"\",\"foliosResultantes\":\"\",\"solicitudPago\":[\"\"],\"registroPreventivo\":false,\"anotacionRegistroPreventivo\":\"\",\"afavorDe\":\"\"}";
		log.info(json);
		log.info("-----------");
		PrelacionBoletaRegistralVO prVO = new PrelacionBoletaRegistralVO();
		//prVO = pbs.jsonTOBoletaRegistral(json);
		log.info("prVO: " + prVO);
	}
	
	public boolean isFusion(Long prelacionId) {
		List<Acto> actos = actoService.findByPrelacionId(prelacionId);
		boolean fusion=false;
		for(Acto acto : actos){
            Long tipoA = acto.getTipoActo().getId();
            if( tipoA == Constantes.FUSION_TIPO_ACTO_ID ){
            	fusion=true;
            }
		}
		return fusion;
	}

}
