package com.gisnet.erpp.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.mariuszgromada.math.mxparser.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.CampoCotizador;
import com.gisnet.erpp.domain.ConceptoPago;
import com.gisnet.erpp.domain.ServicioConceptoPago;
import com.gisnet.erpp.domain.ServiciosCotizador;
import com.gisnet.erpp.domain.UsuarioOfiAreas;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.TipoActo;
import com.gisnet.erpp.domain.ConcPagoTipoActo;
import com.gisnet.erpp.repository.ConcPagoTipoActoRepository;
import com.gisnet.erpp.repository.ConceptoPagoRepository;
import com.gisnet.erpp.repository.ParametroCotizadorCostoRepository;
import com.gisnet.erpp.repository.ParametroRepository;
import com.gisnet.erpp.repository.ServicioClasifConceptoRepository;
import com.gisnet.erpp.repository.ServicioConceptoPagoRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.PrelacionRepository;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.UtilFecha;
import com.gisnet.erpp.vo.ConceptosPagosVO;
import com.gisnet.erpp.web.api.cotizador.ConceptoPagoDTO;
import com.gisnet.erpp.web.api.cotizador.ConceptoPagoDetalleDTO;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class ConceptoPagoService {

	private static final Logger log = LoggerFactory.getLogger(ConceptoPagoService.class);
	
	@Autowired
	private ParametroCotizadorCostoRepository paramCotizaCostoRepository;
	
	@Autowired
	private ConceptoPagoRepository conceptoPagoRepository;
	
	@Autowired
	private ConcPagoTipoActoRepository concPagoTipoActoRepository;
	
	@Autowired
	private ServicioConceptoPagoRepository servicioConceptoPagoRepository;
	
	@Autowired
	private ServicioClasifConceptoRepository servicioClasifConceptoRepository; 
	
	@Autowired
	private ParametroRepository parametroRepository;

	@Autowired
    private ActoRepository actoRepository;

	@Autowired
    private PrelacionRepository prelacionRepository;
	
	@Autowired
	private ParametroCotizadorCostoService parametroCotizadorCostoService;
	
	public ConceptoPago findOne(Long id){
		return conceptoPagoRepository.findOne(id);
	}
	
	public ConceptoPagoDTO cotizarServicio(ConceptoPagoDTO conceptoPagoDTO){
		StringBuffer descParametros = null;
		ConceptoPagoDTO result = new ConceptoPagoDTO();
		ConceptoPago conceptoPago = conceptoPagoRepository.findOne(conceptoPagoDTO.getId());
		
		result.setId(conceptoPagoDTO.getId());
		result.setNombre(conceptoPago.getNombre());
		result.setTarifaUnitaria(conceptoPago.getTarifaUnitaria());
		result.setClasificacionConceptoNombre(conceptoPagoDTO.getClasificacionConceptoNombre());
		result.setServicioCotizadorNombre(conceptoPagoDTO.getServicioCotizadorNombre());
		
		if(conceptoPagoDTO.getCampos() != null && conceptoPagoDTO.getCampos().length > 0){
			descParametros = new StringBuffer();
			for(CampoCotizador campoCotizador : conceptoPagoDTO.getCampos()){
				descParametros.append(campoCotizador.getNombreCampo()).append(": ").append(campoCotizador.getValor()).append(";");
			}
			result.setDescParametros(descParametros.toString());
		}
		
		List<ConceptoPagoDetalleDTO> detalle = new ArrayList<ConceptoPagoDetalleDTO>();
		
		//calculo para principal
		detalle.add(calcula(conceptoPagoDTO));
		
		//calculo para conceptos adicionales
		if (conceptoPago.getConceptosAdicionales()!=null){
			String[] ids = conceptoPago.getConceptosAdicionales().split(",");
			for (String id : ids){
				ConceptoPago adicional = conceptoPagoRepository.findOne(Long.parseLong(id));
				conceptoPagoDTO.setId(adicional.getId());
				conceptoPagoDTO.setTarifaUnitaria(adicional.getTarifaUnitaria());
				ConceptoPagoDetalleDTO conceptoPagoDetalleDTO = calcula(conceptoPagoDTO);
				if (conceptoPagoDetalleDTO.getCantidad()>0 ) {
					detalle.add(conceptoPagoDetalleDTO);
				}
			}			
		}
		
		result.setDetalle(detalle);
		return result;		
	}
	
	/**
	 * Recibe ids de concepto pago, carga formula y calcula deacuerdo a campos y parametros
	 * @param conceptoPagoVO
	 * @return
	 */
	private ConceptoPagoDetalleDTO calcula(ConceptoPagoDTO conceptoPagoVO){
		ConceptoPago conceptoPago = conceptoPagoRepository.findOne(conceptoPagoVO.getId());
		
		ConceptoPagoDetalleDTO result = new ConceptoPagoDetalleDTO();
		result.setId(conceptoPago.getId());
		result.setNombre(conceptoPago.getNombre());
		result.setClave(conceptoPago.getCveConceptoFinanzas());
		result.setTarifaUnitaria(conceptoPago.getTarifaUnitaria());
	
		String formula = conceptoPago.getFormula();
		
		if (conceptoPago.getExcentoPago()){
			result.setMensaje("EL CONCEPTO DE PAGO ESTA EXENTO DE PAGO");
			result.setCosto(BigDecimal.ZERO);
			result.setCantidad(1);
			return result;
		}
		
		Hashtable<String, String> campos = new Hashtable<String, String>();
		for (int i=0; i < conceptoPagoVO.getCampos().length; i++ ){
			CampoCotizador campoCotizador = conceptoPagoVO.getCampos()[i];
			campos.put(campoCotizador.getPropiedad(), campoCotizador.getValor());
		}
			
		Hashtable<String, BigDecimal> valores = parametroCotizadorCostoService.findAllByAnio(UtilFecha.getCurrentYear());
		
		double costo = this.evaluaFormula(formula, valores, campos);
		
		if (conceptoPago.getTarifaUnitaria()){
			if (conceptoPago.getFormulaCantidad()!=null && conceptoPago.getFormulaCantidad().length()>0) {
				result.setCantidad((int) this.evaluaFormula(conceptoPago.getFormulaCantidad(), valores, campos));
			} else {
				result.setCantidad(1);
			}
			
		}
	    
	    log.debug("costo={}", costo);
	    
	    String mensaje=null;
	    
	    if (conceptoPago.getPagoMinimo()){
	    	log.debug("valores.get(Constantes.PAGO_MINIMO_KEY = '{}'", valores.get(Constantes.PAGO_MINIMO_KEY));
	    	if (costo< valores.get(Constantes.PAGO_MINIMO_KEY).doubleValue()){
	    		costo = valores.get(Constantes.PAGO_MINIMO_KEY).doubleValue();
	    		mensaje= "EL CONCEPTO REQUIERE UN PAGO MÍNIMO";
	    	}
	    }
	    if (conceptoPago.getPagoTope()){
	    	if (costo> valores.get(Constantes.PAGO_TOPE_KEY).doubleValue()){
	    		costo = valores.get(Constantes.PAGO_TOPE_KEY).doubleValue();
	    		mensaje="EL CONCEPTO TIENE UN TOPE MÁXIMO";
	    	}
	    }
	    
	    
	    result.setCosto(new BigDecimal(costo));
	    result.setMensaje(mensaje);
	    return result;
		
	}
	
	private double evaluaFormula(String formula, Hashtable<String, BigDecimal> valores, Hashtable<String, String> campos) {
		log.debug("formula={}", formula);
		
		String regex = "\\bPARAMETRO_\\w*";
	    Pattern p = Pattern.compile(regex);

	    Matcher m = p.matcher(formula);
	    String val = null;
	    while (m.find()) {
	      val = m.group();	      
	      log.debug("buscando='{}' ", val.substring(10));
	      BigDecimal nuevoValor = valores.get(val.substring(10));
	      log.debug("match='{}' por {} ", val, nuevoValor);
	      formula = formula.replace(val, nuevoValor.toString());
	      //e.setArgumentValue(val, nuevoValor.doubleValue());
	    }	    
	    
	    log.debug("formula substituida PARAM={}", formula);
	    
	    regex = "\\bCAMPO_\\w*";
	    p = Pattern.compile(regex);

	    m = p.matcher(formula);
	    val = null;
	    while (m.find()) {
	      val = m.group();	      
	      String nuevoValor = campos.get(val.substring(6));
	      log.debug("match='{}' por {} ", val, nuevoValor);
	      formula = formula.replace(val, nuevoValor);
	      //e.setArgumentValue(val, Double.parseDouble(nuevoValor));
	    }	
	    
	    Expression e = new Expression(formula);
	    
	    log.debug("formula substituida CAMPO={}, expresion={} ", formula, e.getExpressionString());

	    return e.calculate();
	}

	
	public List<ConceptoPago> findAllByActoPrelacion(Long idPrelacion){
		final List<ConceptoPago> conceptos = new ArrayList<>();
		try {


		    List<Acto> actos = actoRepository.findAllByPrelacionIdOrderByOrdenAsc(idPrelacion);

		    if (!isEmpty(actos)) {
				List<TipoActo> tipos = new ArrayList<>();

				actos.stream().forEach(acto -> tipos.add(acto.getTipoActo()));
				List<ConcPagoTipoActo> conc = concPagoTipoActoRepository.findAllByTipoActoIn(tipos);

				if (!isEmpty(conc))
					conc.stream().forEach(co -> conceptos.add(co.getConceptoPago()));
			}

		}
		catch (Exception except) {
			log.debug(except.getMessage());
		}
		return new ArrayList(new HashSet(conceptos));
	}
	
	public List<ServicioConceptoPago> findAllByServicioPrelacion(Long idPrelacion){
		
		List<ServicioConceptoPago> conceptos = servicioConceptoPagoRepository.findAllByServiciosPrelacion(idPrelacion);
	
		return conceptos;
	}


	public List<ConceptoPago> findAll(){
		return conceptoPagoRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Page<ConceptoPago> findAllBy(Pageable pageable, String nombre, Long areaId, Long clasifActoId) {
		return conceptoPagoRepository.findAllBy(pageable, nombre, areaId, clasifActoId);
	}
	
	@Transactional(rollbackFor=Exception.class)
	public Long save(ConceptoPago conceptoPago) throws UnsupportedOperationException {
		try {
			conceptoPagoRepository.saveAndFlush(conceptoPago);
		}
		
		catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}
		return conceptoPago.getId();
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void delete(Long id) throws UnsupportedOperationException {
		try {
			conceptoPagoRepository.delete(id);
		}
		catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}
	}

	public List<ConceptoPago> findByAreaId( Long areaId){
		return conceptoPagoRepository.findByAreaId(areaId);
	}
	
	public List<ConceptoPago> findByAreaIdAndClasifActoIsNull( Long areaId){
		return conceptoPagoRepository.findByAreaIdAndClasifActoIsNull(areaId);
	}
	
	public List<ConceptoPago> findByAreaIdAndClasifActoId(ConceptoPagoDTO conceptoPago){
		//return conceptoPagoRepository.findByAreaIdAndClasifActoId(conceptoPago.getArea().getId(), conceptoPago.getClasifActo().getId());
		return null;
	}
	
	public List<ConceptoPago> findByAreaIdAndClasifActoIdAndTipoActoId(ConceptoPagoDTO conceptoPago){
		List<ConceptoPago> result = new ArrayList<>();
		/*List<ConcPagoTipoActo> concPagoTipoActos = concPagoTipoActoRepository.findByTipoActoIdAndConceptoPagoAreaIdAndConceptoPagoClasifActoId(conceptoPago.getTipoActo().getId(), conceptoPago.getArea().getId(), conceptoPago.getClasifActo().getId());
		if(concPagoTipoActos != null && !concPagoTipoActos.isEmpty()){
			for(ConcPagoTipoActo concPagoTipoActo:concPagoTipoActos){
				result.add(concPagoTipoActo.getConceptoPago());
			}
		}*/
		return result;
	}
	

	public byte[] generateMapPdfCotizacion(ConceptosPagosVO conceptosPago,String path){
		List<ConceptoPagoDTO> conceptos = null;
		Map<String,Object> parameters = new HashMap<String, Object>();
		conceptos = Arrays.stream(conceptosPago.getConceptosPago()).collect(Collectors.toList());

		JRDataSource ds = new JRBeanCollectionDataSource(conceptos);

		byte[] pdf = null;
		parameters.put("datasource", ds);
		parameters.put("reportsPath", Constantes.REPORTES_PATH);		
		parameters.put("imgPath", Constantes.IMG_PATH);
		parameters.put("cotizadorMensaje",parametroRepository.findByCve(Constantes.COTIZADOR_MENSAJE_CVE).getValor());
		parameters.put("fundamentoJuridico",parametroRepository.findByCve(Constantes.FUNDAMENTO_JURIDICO_CVE).getValor());
		parameters.put("documentoFundatorio",conceptosPago.getDocumentoFundatorio());
		
		InputStream jasperStream = this.getClass().getResourceAsStream("/reports/cotizador/pdfCotizacion.jasper");

		try {
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ds);
			pdf = JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (JRException e) {
			e.printStackTrace();
		}
		return pdf;
	}
	
	public List<ConceptoPago> findByServicioCotizador( Long servicioCotizadorId){
		return conceptoPagoRepository.findByServiciosConceptoPagoServiciosCotizadorIdOrderByNombre(servicioCotizadorId);
	}
	
	public List<ConceptoPago> findByServicioCotizadorClasificacionConcepto(Long servicioCotizadorId, Long clasificacionConceptoId){
		return conceptoPagoRepository.findByServicioClasifConceptoServiciosCotizadorIdAndServicioClasifConceptoClasificacionConceptoIdOrderByNombre(servicioCotizadorId, clasificacionConceptoId);
	}
	
	public List<ConceptoPago> updateConceptoPago(List<ConceptoPago> conceptoPago) {

		System.out.println("Nombre CP--> : " + conceptoPago.get(0).getNombre());

		conceptoPagoRepository.save(conceptoPago);

		return conceptoPago;
	}
	
	public List<ConceptoPago> findConceptosAdicionales(){
		List <String> conceptAdi= null;
	   List<ConceptoPago> CP = conceptoPagoRepository.findDistinctByConceptosAdicionales();
	   System.out.println("RESPUESTA: " + CP.size());
	  
		return CP;
		
		 /*System.out.println("Hola llego aqui1");
		Set <String> concepAdi = new HashSet<>();
		List <ConceptoPago> conceptopago = (List<ConceptoPago>) conceptoPagoRepository.findAll();
		System.out.println("Hola llego aqui2");
		//conceptopago.getConceptosAdicionales();
		for(ConceptoPago list : conceptopago) {
			//sString conceptosAdicionales = list.getConceptosAdicionales();
			concepAdi.add(list.getConceptosAdicionales());
			System.out.println("Hola llego aqui3");
			
		}
		
		concepAdi.toString();
			System.out.println("Conceptos Adicionales : " + concepAdi.size());
		
		return (List<String>) concepAdi;*/
		//Set<String> conceptosAdiciona = new HashSet<>();
		
	}
}
