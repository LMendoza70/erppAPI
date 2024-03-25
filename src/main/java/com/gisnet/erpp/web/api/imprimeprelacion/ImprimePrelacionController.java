package com.gisnet.erpp.web.api.imprimeprelacion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.service.UsuarioService;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.prelacion.ActoModel;
import com.gisnet.erpp.vo.prelacion.PredioModel;
import com.gisnet.erpp.vo.prelacion.PrelacionBoletaRegistralVO;
import com.gisnet.erpp.vo.boletin.*;

import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gisnet.erpp.vo.BoletaRechazoVO;
import com.gisnet.erpp.vo.BoletaSuspensionVO;
import com.gisnet.erpp.vo.CopiaActoPublicitarioVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

import com.gisnet.erpp.vo.PrelacionIngresoVO;
import com.gisnet.erpp.vo.SolicitudDevolucionVO;
import com.gisnet.erpp.vo.actopublicitario.ActoPublicitarioVO;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.domain.ActoPublicitario;
import com.gisnet.erpp.domain.BusquedaResultado;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.PrelacionBoleta;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.Oficina;

import com.gisnet.erpp.repository.ColindanciaRepository;
import com.gisnet.erpp.repository.ParametroRepository;

import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.ActoPublicitarioRepository;
import com.gisnet.erpp.repository.BusquedaResultadoRepository;
import com.gisnet.erpp.repository.ActoPredioCampoRepository;
import com.gisnet.erpp.vo.PredioVO;
import com.gisnet.erpp.service.BoletaRegistralService;
import com.gisnet.erpp.service.BusquedaResultadoService;
import com.gisnet.erpp.service.CertificadoService;
import com.gisnet.erpp.service.PredioService;
import com.gisnet.erpp.service.PrelacionBoletaService;
import com.gisnet.erpp.service.PrelacionService;
import com.gisnet.erpp.service.SolicitudDevolucionService;
import com.gisnet.erpp.service.SuspensionBitacoraService;
import com.gisnet.erpp.service.ActoService;
import com.gisnet.erpp.service.OficinaService;
import com.gisnet.erpp.service.PdfService;

import java.util.Set;
import com.gisnet.erpp.util.ServletUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.context.ConfigurableApplicationContext;

import org.springframework.http.HttpHeaders;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;

@RequestMapping("/api/imprime/prelacion/")
@Controller
public class ImprimePrelacionController {

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	PrelacionService prelacionService;

	@Autowired
	PrelacionBoletaService prelacionBoletaService;

	@Autowired
	ActoPredioRepository actoPredioRepository;

	@Autowired
	ActoPredioCampoRepository actoPredioCampoRepository;

	@Autowired
	ActoPublicitarioRepository actoPublicitarioRepository;
	
	@Autowired
	ActoService actoService;

	@Autowired
	BoletaRegistralService boletaRegistralService;

	@Autowired
	OficinaService oficinaService;

	@Autowired
	ParametroRepository parametroRepository;

	@Autowired
	ConfigurableApplicationContext applicationContext;
	
	@Autowired
	BusquedaResultadoRepository busquedaResultadoRepository;
	
	@Autowired
	BusquedaResultadoService busquedaResultadoService;

	@Autowired
	CertificadoService certificadoService;
	
	@Autowired
	PdfService pdfService;
	
	@Autowired
	SolicitudDevolucionService solicitudDevolucionService;
	
	
	@Autowired
	SuspensionBitacoraService suspensionBitacoraService;
	
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@RequestMapping(value = "printfichaingreso/{prelacionId}", method = RequestMethod.GET, produces = "application/pdf")
	public ModelAndView printFichaIngreso(@PathVariable("prelacionId") Long prelacionId, Model uiModel,
			HttpServletRequest httpServletRequest, ModelAndView modelAndView) {
		Prelacion prelacion = prelacionService.findOne(prelacionId);

		List<Prelacion> prelacions = new ArrayList<Prelacion>();
		prelacions.add(prelacion);
		JRDataSource ds = new JRBeanCollectionDataSource(prelacions);

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		String path = ServletUtil.getContextFilePath(httpServletRequest);

		parameterMap.put("datasource", ds);
		parameterMap.put("path", path);
		parameterMap.put("imgPath", Constantes.IMG_PATH);

		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/pdfIngreso.jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
	    return new ModelAndView(view, parameterMap);
	}

	@RequestMapping(value = "printboletaingreso/{prelacionId}", method = RequestMethod.GET, produces = "application/pdf")
	public ModelAndView printBoletaIngreso(@PathVariable("prelacionId") Long prelacionId, Model uiModel,
			HttpServletRequest httpServletRequest, ModelAndView modelAndView) {

		PrelacionIngresoVO prelacion = prelacionService.findOneVO(prelacionId);

		List<PrelacionIngresoVO> prelacions = new ArrayList<PrelacionIngresoVO>();
		prelacions.add(prelacion);
		JRDataSource ds = new JRBeanCollectionDataSource(prelacions);

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		String path = ServletUtil.getContextFilePath(httpServletRequest);

		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);

		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/pdfIngresoPrelacion.jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
	    return new ModelAndView(view, parameterMap);
	}
	
	@RequestMapping(value = "solicitud-devolucion/{solicitudDevolucionId}", method = RequestMethod.GET, produces = "application/pdf")
	public ModelAndView printSolicitudDevolucion(@PathVariable("solicitudDevolucionId") Long solicitudDevolucionId, Model uiModel,
			HttpServletRequest httpServletRequest, ModelAndView modelAndView) {

		SolicitudDevolucionVO solicitud = solicitudDevolucionService.getSolicitudDevolucionVO(solicitudDevolucionId);
		List<SolicitudDevolucionVO> solicitudes = new ArrayList<SolicitudDevolucionVO>();
		solicitudes.add(solicitud);
		JRDataSource ds = new JRBeanCollectionDataSource(solicitudes);

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);

		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/pdfSolicitudDevolucion.jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
	    return new ModelAndView(view, parameterMap);
	}

	@RequestMapping(value = "boletaregistral/{tipoId}/{prelacionId}/{pagina}", method = RequestMethod.GET, produces = "application/pdf")
	@Transactional(timeout = 72000)
	@ResponseBody HttpEntity<byte[]> printBoletaRegistral(HttpServletResponse response,@PathVariable("prelacionId") Long prelacionId,@PathVariable("pagina") Integer pagina) {

		log.debug("Prelacion registral :::: " + prelacionId);

		List<PrelacionBoletaRegistralVO> prelacions = new ArrayList<PrelacionBoletaRegistralVO>();
		
		// busca tipo acto

		Prelacion pre=prelacionService.findOne(prelacionId);
		byte[] pdf = null;
		byte[] pdfRechazos = prelacionService.generaPdfBoletaRechazos(prelacionId,false);
		
		if(pre!=null && pre.getStatus().getId()==7L||pre.getStatus().getId()==8L){
			
			List<PrelacionBoleta> prelacionBoletas= prelacionBoletaService.findAllByPrelacion(prelacionId,pagina);
			 
			if(prelacionBoletas!=null && prelacionBoletas.size()>0){
				for(PrelacionBoleta pb: prelacionBoletas){
					log.info("prelacionBoletaId encontrado: "+pb.getId());
					if(pb.getDatos()!=null && !pb.getDatos().isEmpty()){
						List<PrelacionBoletaRegistralVO> list=prelacionBoletaService.jsonTOBoletaRegistral(pb.getDatos());//JSON
						pdf=prelacionService.getPdfBoletaRegistralPorActoJSON(list,pagina,false);
									
					}
				}
	
			}
			else{
				log.info("no se encontro JSON en prelacionBoleta para prelacionId: "+prelacionId);
				pdf = prelacionService.getPdfBoletaRegistralPorActo(prelacionId,true,pagina);
				//System.out.println("prelacions{} "+prelacions);
				//Prelacion prelacionCurrent = prelacionService.findOne(prelacionId);
				/*PrelacionBoleta prelacionBoleta = prelacionBoletaService.save(prelacionId,Constantes.tipoBoleta.BOLETA_REGISTRAL.getId(),prelacions);
				if(prelacionBoleta!=null) {
					log.info("JSON guardado con exito PrelacionBoletaId: "+prelacionBoleta.getId());
					prelacionBoletaService.jsonTOBoletaRegistral(prelacionBoleta.getDatos());
					if(prelacions==null){
						log.info("error en la coversion de JSON para prelacionBoletaId: "+prelacionBoleta.getId());
					}
				}else {
					log.info("no fue posible genearar JSON para prelacionId: "+prelacionId);
				}*/
			}
		}
		else{
			log.info("status distinto a 7 o 8");
			pdf = prelacionService.getPdfBoletaRegistralPorActo(prelacionId,true,pagina);
		}
		
	
			try {
				if(pdf!=null && pdfRechazos!=null) {
					pdf =  pdfService.appendPDF(pdf,pdfRechazos);
				}
				if(pdfRechazos != null && pdf == null) {
					pdf = pdfRechazos;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "pdf"));
		header.set("Content-Disposition", "inline; filename= prelacion" + prelacionId);
		header.setContentLength(pdf.length);
 
	  return new HttpEntity<byte[]>(pdf, header);
	}
	
	@RequestMapping(value = "boletaregistral/{tipoId}/{prelacionId}", method = RequestMethod.GET, produces = "application/pdf")
	@Transactional(timeout = 72000)
	@ResponseBody HttpEntity<byte[]> printBoletaRegistral(HttpServletResponse response,@PathVariable("prelacionId") Long prelacionId) {

		log.debug("Prelacion registral :::: " + prelacionId);

		List<PrelacionBoletaRegistralVO> prelacions = new ArrayList<PrelacionBoletaRegistralVO>();
		
		// busca tipo acto

		Prelacion pre=prelacionService.findOne(prelacionId);
		byte[] pdf = null;
		byte[] pdfRechazos = prelacionService.generaPdfBoletaRechazos(prelacionId,false);
		
		if(pre!=null && pre.getStatus().getId()==7L||pre.getStatus().getId()==8L){
			
			List<PrelacionBoleta> prelacionBoletas= prelacionBoletaService.findAllByPrelacion(prelacionId,0);
			 
			if(prelacionBoletas!=null && prelacionBoletas.size()>0){
				for(PrelacionBoleta pb: prelacionBoletas){
					log.info("prelacionBoletaId encontrado: "+pb.getId());
					if(pb.getDatos()!=null && !pb.getDatos().isEmpty()){
						List<PrelacionBoletaRegistralVO> list=prelacionBoletaService.jsonTOBoletaRegistral(pb.getDatos());//JSON
						pdf=prelacionService.getPdfBoletaRegistralPorActoJSON(list,0,false);
									
					}
				}
	
			}
			else{
				log.info("no se encontro JSON en prelacionBoleta para prelacionId: "+prelacionId);
				pdf = prelacionService.getPdfBoletaRegistralPorActo(prelacionId,true,0);
				//System.out.println("prelacions{} "+prelacions);
				//Prelacion prelacionCurrent = prelacionService.findOne(prelacionId);
				/*PrelacionBoleta prelacionBoleta = prelacionBoletaService.save(prelacionId,Constantes.tipoBoleta.BOLETA_REGISTRAL.getId(),prelacions);
				if(prelacionBoleta!=null) {
					log.info("JSON guardado con exito PrelacionBoletaId: "+prelacionBoleta.getId());
					prelacionBoletaService.jsonTOBoletaRegistral(prelacionBoleta.getDatos());
					if(prelacions==null){
						log.info("error en la coversion de JSON para prelacionBoletaId: "+prelacionBoleta.getId());
					}
				}else {
					log.info("no fue posible genearar JSON para prelacionId: "+prelacionId);
				}*/
			}
		}
		else{
			log.info("status distinto a 7 o 8");
			pdf = prelacionService.getPdfBoletaRegistralPorActo(prelacionId,true,0);
		}
		
	
			try {
				if(pdf!=null && pdfRechazos!=null) {
					pdf =  pdfService.appendPDF(pdf,pdfRechazos);
				}
				if(pdfRechazos != null && pdf == null) {
					pdf = pdfRechazos;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "pdf"));
		header.set("Content-Disposition", "inline; filename= prelacion" + prelacionId);
		header.setContentLength(pdf.length);
 
	  return new HttpEntity<byte[]>(pdf, header);
	}

	
	@RequestMapping(value = "boletasrechazos/{actoId}/confirma/{tipo}", method = RequestMethod.GET, produces = "application/pdf")
	public ModelAndView printBoletasRechazos(@PathVariable("actoId") List<Long> actoId, @PathVariable("tipo") Long tipo,
			Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {

		log.debug("Acto para rechazo :::: " + actoId);
		List<BoletaRechazoVO> prelacions = new ArrayList<BoletaRechazoVO>();
		for(Long id:actoId) {
			System.out.println(id);
			BoletaRechazoVO boleta = boletaRegistralService.findOneVOBoletaRechazo(id);
			System.out.println(boleta);
			prelacions.add(boleta);

	

			if (tipo == 0)
				//actoService.removerRechazo(actoId);

			//ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			try {
				// log.debug(ow.writeValueAsString(prelacion));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			

			
		}
		JRDataSource ds = new JRBeanCollectionDataSource(prelacions);

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		String path = ServletUtil.getContextFilePath(httpServletRequest);

		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);

		log.debug("Ruta de las imagenes");
		log.debug(Constantes.IMG_PATH);

		log.debug("Ruta de los reportes");
		log.debug(Constantes.REPORTES_PATH);

		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/pdfBoletaRechazo.jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
	    return new ModelAndView(view, parameterMap);
	}
	@RequestMapping(value = "boletarechazo/{actoId}/confirma/{tipo}", method = RequestMethod.GET, produces = "application/pdf")
	public ModelAndView printBoletaRechazo(@PathVariable("actoId") Long actoId, @PathVariable("tipo") Long tipo,
			Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {

		log.debug("Acto para rechazo :::: " + actoId);

		BoletaRechazoVO boleta = boletaRegistralService.findOneVOBoletaRechazo(actoId);
		List<BoletaRechazoVO> prelacions = new ArrayList<BoletaRechazoVO>();
		prelacions.add(boleta);

		JRDataSource ds = new JRBeanCollectionDataSource(prelacions);

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		String path = ServletUtil.getContextFilePath(httpServletRequest);
		Acto act = actoService.findById(actoId);		
		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
		String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+act.getPrelacion().getId();
		parameterMap.put("QR_BASE_URI", qR);
		log.debug("Ruta de las imagenes");
		log.debug(Constantes.IMG_PATH);

		log.debug("Ruta de los reportes");
		log.debug(Constantes.REPORTES_PATH);

		if (tipo == 0)
			actoService.removerRechazo(actoId);

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			// log.debug(ow.writeValueAsString(prelacion));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/pdfBoletaRechazo.jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
	    return new ModelAndView(view, parameterMap);
	}
	
	@RequestMapping(value = "actopublicitario/{nombre}/{apellido_paterno}/{apellido_materno}/{servicioId}", method = RequestMethod.GET, produces = "application/pdf")
	public ModelAndView printActoPublicitario(@PathVariable("nombre")String nombre,@PathVariable("apellido_paterno")String apellido_paterno,@PathVariable("apellido_materno")String apellido_materno,@PathVariable("servicioId")Long servicioId, Model uiModel,HttpServletRequest httpServletRequest, ModelAndView modelAndView) {
		
		List<ActoPublicitarioVO> to=null;
		log.debug("NOMBRE :::: "+nombre);
		log.debug("APELLIDO PATERNO :::: "+apellido_paterno);
		log.debug("APELLIDO MATERNO :::: "+apellido_materno);
		String nombreBoleta1 = "pdfActoPublicitario";
	   to=prelacionService.getActoPublicitario(nombre,apellido_paterno,apellido_materno,servicioId);
	   ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
	   try {
		   // log.debug(ow.writeValueAsString(prelacion));
		   log.debug(ow.writeValueAsString(to));
	   } catch (Exception ex) {
		   ex.printStackTrace();
	   }
	   JRDataSource ds = new JRBeanCollectionDataSource(to);

	   Map<String, Object> parameterMap = new HashMap<String, Object>();
	   String path = ServletUtil.getContextFilePath(httpServletRequest);
	   System.out.println("-----------"+path);
	   parameterMap.put("datasource", ds);
	   parameterMap.put("path", Constantes.IMG_PATH);
	   parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);

	   log.debug(Constantes.IMG_PATH);

	   log.debug(Constantes.REPORTES_PATH);

	   JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/actosPublicitarios/"+nombreBoleta1+".jasper");	   
		view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
		
	    return new ModelAndView(view, parameterMap);	
	}
	

	@RequestMapping(value = "boletindenegado/{oficinaId}/{fecha}", method = RequestMethod.GET, produces = "application/pdf")
	public ModelAndView printBoletinDenegado(@PathVariable("oficinaId")Long oficinaId,@PathVariable("fecha")Date fecha, Model uiModel,HttpServletRequest httpServletRequest, ModelAndView modelAndView) {
		Oficina oficina = oficinaService.findById(oficinaId);
		String nombreBoleta = "pdfBoletinRegistral";
		List<BoletinDenegadoVO> boletines = prelacionService.getBoletinDenegado(fecha,oficina);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			log.debug(ow.writeValueAsString(boletines));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		JRDataSource ds = new JRBeanCollectionDataSource(boletines);

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		String path = ServletUtil.getContextFilePath(httpServletRequest);
		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/"+nombreBoleta+".jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
		
		return new ModelAndView(view, parameterMap);
	}
	
	
	
	//
	@RequestMapping(value = "boletinsuspendido/{oficinaId}/{fecha}", method = RequestMethod.GET, produces = "application/pdf")
	public ModelAndView printBoletinSuspendido(@PathVariable("oficinaId")Long oficinaId,@PathVariable("fecha")Date fecha, Model uiModel,HttpServletRequest httpServletRequest, ModelAndView modelAndView) {
		log.debug("Prelacion boletin  Suspendido:::: "+fecha);		
		Oficina oficina = oficinaService.findById(oficinaId);

		// PrelacionBoletaRegistralVO prelacion =
		// prelacionService.findOneVOSI(prelacionId);
		String nombreBoleta = "pdfBoletinRegistral";
		List<BoletinDenegadoVO> boletines = prelacionService.getBoletinSuspendido(fecha,oficina);
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			// log.debug(ow.writeValueAsString(prelacion));
			log.debug(ow.writeValueAsString(boletines));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		JRDataSource ds = new JRBeanCollectionDataSource(boletines);

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		String path = ServletUtil.getContextFilePath(httpServletRequest);

		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);

		log.debug(Constantes.IMG_PATH);

		log.debug(Constantes.REPORTES_PATH);

		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/"+nombreBoleta+".jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
		
		return new ModelAndView(view, parameterMap);
	}

	@RequestMapping(value = "boletasuspension/{prelacionId}", method = RequestMethod.GET, produces = "application/pdf")
	public ModelAndView printBoletaSuspension(@PathVariable("prelacionId") Long prelacionId, Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {
		log.debug("Prelacion Suspension :::: "+prelacionId);
		
		Prelacion prelacion = prelacionService.findOne(prelacionId);
		
		List<BoletaSuspensionVO> listBoletas  = new ArrayList<BoletaSuspensionVO>();
		
		BoletaSuspensionVO prelacions = boletaRegistralService.findOneVOBoletaSuspension(prelacion);
		listBoletas.add(prelacions);
	    
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String uri= parametroRepository.findByCve("QR_BASE_URI").getValor() + "/api/imprime/prelacion/boletasuspension/{prelacionId}";


		JRDataSource ds = new JRBeanCollectionDataSource(listBoletas);

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		String path = ServletUtil.getContextFilePath(httpServletRequest);

		parameterMap.put("datasource", ds);
		parameterMap.put("reportsPath", Constantes.REPORTES_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		parameterMap.put( JRParameter.REPORT_LOCALE, Constantes.locale );
		parameterMap.put("QR_BASE_URI", parametroRepository.findByCve("QR_BASE_URI").getValor());
		
		//se manda parametro de uri //  
		parameterMap.put("uri", uri);
		log.debug(Constantes.IMG_PATH);

		log.debug(Constantes.REPORTES_PATH);

		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/pdfBoletaSuspension.jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
	    return new ModelAndView(view, parameterMap);
	}
	
	
	@RequestMapping(value = "boleta-suspension-bitacora/{suspensionBitacoraId}", method = RequestMethod.GET, produces = "application/pdf")
	public ModelAndView printBoletaSuspensionBitacora(@PathVariable("suspensionBitacoraId") Long suspensionBitacoraId, Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {
		
		List<BoletaSuspensionVO> listBoletas  = new ArrayList<BoletaSuspensionVO>();
		
		BoletaSuspensionVO boleta =  suspensionBitacoraService.boletaSuspension(suspensionBitacoraId);
		listBoletas.add(boleta);
	    
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String uri= parametroRepository.findByCve("QR_BASE_URI").getValor() + "/api/imprime/prelacion/boleta-suspension-bitacora/{suspensionBitacoraId}";

		JRDataSource ds = new JRBeanCollectionDataSource(listBoletas);
		
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		String path = ServletUtil.getContextFilePath(httpServletRequest);

		parameterMap.put("datasource", ds);
		parameterMap.put("reportsPath", Constantes.REPORTES_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		parameterMap.put( JRParameter.REPORT_LOCALE, Constantes.locale );
		parameterMap.put("QR_BASE_URI", parametroRepository.findByCve("QR_BASE_URI").getValor());
		
		//se manda parametro de uri //  
		parameterMap.put("uri", uri);
		

		log.debug(Constantes.IMG_PATH);

		log.debug(Constantes.REPORTES_PATH);

		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/pdfBoletaSuspension.jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
	    return new ModelAndView(view, parameterMap);
	}
	
	
	
	
	
	@RequestMapping(value = "findActoPublicitarioId/{actoPublicitarioId}", method = RequestMethod.GET, produces = "application/pdf")
	public ModelAndView printActoPublicitarioByActoPublicitarioId(@PathVariable("actoPublicitarioId") Long actoPublicitarioId, Model uiModel,HttpServletRequest httpServletRequest, ModelAndView modelAndView) {
		List<ActoPublicitarioVO> to=null;
		
		String nombreBoleta1 = "pdfActoPublicitario";
	   to=prelacionService.getActoPublicitarioById(actoPublicitarioId);
	   ActoPublicitario apub=actoPublicitarioRepository.findOne(actoPublicitarioId);
	   JRDataSource ds = new JRBeanCollectionDataSource(to);

	   Map<String, Object> parameterMap = new HashMap<String, Object>();
	   String path = ServletUtil.getContextFilePath(httpServletRequest);
	   System.out.println("-----------"+path);
	   parameterMap.put("datasource", ds);
	   parameterMap.put("path", Constantes.IMG_PATH);
	   parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
	 //  parameterMap.put("marcaDeAgua", Constantes.IMG_PATH);
		if((to !=null && !to.isEmpty()) && apub!=null) {
			String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/";
			parameterMap.put("QR_BASE_URI", qR);	
		}else {
			parameterMap.put("QR_BASE_URI", "");
		}
	     

	   log.debug(Constantes.IMG_PATH);

	   log.debug(Constantes.REPORTES_PATH);

	   JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/actosPublicitarios/"+nombreBoleta1+".jasper");	   
		view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
		
	    return new ModelAndView(view, parameterMap);	
	}
	
	@RequestMapping(value = "findCaratulaCopiaEntrada/{prelacionId}", method = RequestMethod.GET, produces = "application/pdf")
	public ModelAndView findCaratulaCopiaEntrada(@PathVariable("prelacionId") Long prelacionId, Model uiModel,HttpServletRequest httpServletRequest, ModelAndView modelAndView) {
	
	 //  List<BusquedaResultado>  busqueda  =  busquedaResultadoRepository.findAllBusquedResultadoByPrelacionId(prelacionId);
       BusquedaResultado  busquedaR  =  busquedaResultadoRepository.findOneByPrelacionId(prelacionId);

       CopiaActoPublicitarioVO copiaActoPublicitario = busquedaResultadoService.generaCopiaActoPublicitario(busquedaR);

		List<CopiaActoPublicitarioVO> CopiaActoPublicitariosVO = new ArrayList<CopiaActoPublicitarioVO>();
		
		CopiaActoPublicitariosVO.add(copiaActoPublicitario);

	   JRDataSource ds = new JRBeanCollectionDataSource(CopiaActoPublicitariosVO);

	   Map<String, Object> parameterMap = new HashMap<String, Object>();
	   String path = ServletUtil.getContextFilePath(httpServletRequest);
	   System.out.println("-----------"+path);
	   parameterMap.put("datasource", ds);
	   parameterMap.put("path", Constantes.IMG_PATH);
	   parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
	   String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+prelacionId;
	   parameterMap.put("QR_BASE_URI", qR);
	   log.debug(Constantes.IMG_PATH);

	   log.debug(Constantes.REPORTES_PATH);
	    
	   JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/caratulaCopiasCertificadasDeActoPublicitario.jasper");	   
		view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
		
	    return new ModelAndView(view, parameterMap);	
	}
	
	@RequestMapping(value = "pdfFindCaratulaCopiaCertificadaActoPublicitario/{prelacionId}", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody HttpEntity<byte[]> imprimeCaratulaCopiaCertificadaActoPublicitario(HttpServletResponse response, @PathVariable Long prelacionId)throws IOException {
         byte[] pdf = certificadoService.printActoPublicitarioCertificadoByActoPublicitarioId(prelacionId,false);
         InputStream in = new ByteArrayInputStream(pdf); 
         
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "pdf"));
        header.set("Content-Disposition", "inline; filename= prelacion" + prelacionId);
        header.setContentLength(pdf.length);
        return new HttpEntity<byte[]>(pdf, header);
	}
    
	@RequestMapping(value = "/general/{prelacion}", method = RequestMethod.GET, produces = "application/pdf")
	public HttpEntity<byte[]> imprimeCertificado(@PathVariable("prelacion") Long prelacion, Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {
		
		Prelacion pre = prelacionService.findOne(prelacion);
		byte[] pdf =  null;
		
		try{
			pdf  = boletaRegistralService.generPdfRespuesta(prelacion,false,false,false,null);
		 }
		 catch(Exception ex){
			 return null;
		 }
		
	        HttpHeaders header = new HttpHeaders();
	        header.setContentType(new MediaType("application", "pdf"));
	        header.set("Content-Disposition", "inline; filename= prelacion" + pre.getId());
	        header.setContentLength(pdf.length);
	        return new HttpEntity<byte[]>(pdf, header);

	}
}
