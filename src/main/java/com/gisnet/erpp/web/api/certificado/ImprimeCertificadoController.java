package com.gisnet.erpp.web.api.certificado;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;
import com.gisnet.erpp.service.excepciones.RequerimientoException;
import com.gisnet.erpp.repository.ParametroRepository;
import com.gisnet.erpp.service.BoletaRegistralService;
import com.gisnet.erpp.service.CertificadoService;
import com.gisnet.erpp.service.PdfService;
import com.gisnet.erpp.service.PrelacionBoletaService;
import com.gisnet.erpp.service.PrelacionService;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.ServletUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.PrelacionBoleta;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.Certificado;
import com.gisnet.erpp.vo.certificado.CertificadoHistorialVO;
import com.gisnet.erpp.vo.certificado.CertificadoLibertadOGravamenVO;
import com.gisnet.erpp.vo.juridico.CertificadoDeNoInscripcionVO;
import com.gisnet.erpp.vo.juridico.CertificadoPersonaJuridicasVO;
import com.gisnet.erpp.vo.prelacion.PrelacionBoletaRegistralVO;
import com.gisnet.erpp.vo.certificado.CertificadoCopiasVO;
import com.gisnet.erpp.vo.prelacion.ActoModel;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.springframework.http.MediaType;

@RequestMapping("/api/imprime/certificado/")
@Controller
public class ImprimeCertificadoController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	CertificadoService certificadoService;

	@Autowired
	PrelacionService prelacionService;
	
	@Autowired
	ParametroRepository parametroRepository;

	@Autowired
	ConfigurableApplicationContext applicationContext;


	@Autowired
	PrelacionBoletaService prelacionBoletaService;

	@Autowired
	BoletaRegistralService boletaRegistralService;

	
	@Autowired
	PdfService pdfService;


	@RequestMapping(value = "/{prelacionId}", method = RequestMethod.GET, produces = "application/pdf")
	public HttpEntity<byte[]>  imprimeCertificadoCLG(@PathVariable("prelacionId") Long prelacionId, Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {
		byte[] result = null;
		try {
			result =  boletaRegistralService.generPdfRespuesta(prelacionId,false,true,true,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     HttpHeaders header = new HttpHeaders();
	     header.setContentType(new MediaType("application", "pdf"));
	     header.set("Content-Disposition", "inline; filename= prelacion" + prelacionId);
	     header.setContentLength(result.length);
	     return new HttpEntity<byte[]>(result, header);
	}
	@RequestMapping(value = "/{prelacionId}/predio/{predioId}", method = RequestMethod.GET, produces = "application/pdf")
	public HttpEntity<byte[]>  imprimeCertificadoCLGConPredio(@PathVariable("prelacionId") Long prelacionId,@PathVariable("predioId") Long predioId, Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {
		byte[] result = null;
		try {
			result =  boletaRegistralService.generPdfRespuesta(prelacionId,false,true,true,predioId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     HttpHeaders header = new HttpHeaders();
	     header.setContentType(new MediaType("application", "pdf"));
	     header.set("Content-Disposition", "inline; filename= prelacion" + prelacionId);
	     header.setContentLength(result.length);
	     return new HttpEntity<byte[]>(result, header);
	}



	/**
	 * Certificado de libertad de gravemen  
	**/
	@RequestMapping(value = "/lg/{prelacion}", method = RequestMethod.GET, produces = "application/pdf")
	public HttpEntity<byte[]> imprimeCertificado(@PathVariable("prelacion") Long prelacion, Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {
		
		Prelacion pre = prelacionService.findOne(prelacion);
		byte[] pdf =  null;
		byte[] pdfRechazos = prelacionService.generaPdfBoletaRechazos(pre.getId(),false);
		try{
			pdf =  certificadoService.getPdfCertificadoLg(pre,false);
			
				if(pdf!=null && pdfRechazos!=null) {
					pdf =  pdfService.appendPDF(pdf,pdfRechazos);
				}
				if(pdfRechazos != null && pdf == null) {
					pdf = pdfRechazos;
				}
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

	/**
	 * Certificado de libertad de gravemen con AVISO Dic-2018 
	**/
	@RequestMapping(value = "/lg/aviso/{prelacion}", method = RequestMethod.GET, produces = "application/pdf")
	public ModelAndView imprimeCertificadoLgAviso(@PathVariable("prelacion") Long prelacion, Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {
		
		Prelacion pre = prelacionService.findOne(prelacion);
		
		List<CertificadoLibertadOGravamenVO> certificados = new ArrayList<CertificadoLibertadOGravamenVO>();

		try{
									 //IHM getCertificado();
			certificados=  certificadoService.getCertificadoDeLivertadIGravamen(pre);
		}
		catch(Exception ex){

			log.debug("Excepcion de al generar certificado " +ex.getMessage());
			return null;

		}

		JRDataSource ds = new JRBeanCollectionDataSource(certificados);

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("reportsPath", Constantes.REPORTES_PATH);		
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		parameterMap.put( JRParameter.REPORT_LOCALE, Constantes.locale );
		parameterMap.put("path", Constantes.IMG_PATH);
		String qR = parametroRepository.findByCve("QR_BASE_URI").getValor() + "/api/imprime/certificado/"+ prelacion;
		parameterMap.put("QR_BASE_URI", qR);
		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/certificadoLibertadOGravamen/pdfCertificadoLibertadOGravamenAviso.jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
	    return new ModelAndView(view, parameterMap);
	}

	/**
	 * Certificado con historial 
	**/
	@RequestMapping(value = "/lg/historico/{prelacion}", method = RequestMethod.GET, produces = "application/pdf")
	public ModelAndView imprimeCertificadoHistorico(@PathVariable("prelacion") Long prelacion, Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {
		
		Prelacion pre = prelacionService.findOne(prelacion);
		
		CertificadoHistorialVO certificadoHistorial=null;
		CertificadoLibertadOGravamenVO clg= null;
		try{

			//certificadoHistorial= certificadoService.generaCertificadoHistorial(pre);
									//IHM getCertificado();
			clg=  null;//certificadoService.getCertificadoDeLivertadIGravamen(pre);
		}
		catch(Exception ex){

			log.debug("Excepcion de al generar certificado " +ex.getMessage());
			return null;

		}
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			/*System.out.println("Certificado : ");
			System.out.println(ow.writeValueAsString(clg));*/
	          log.debug(ow.writeValueAsString(clg));
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		List<CertificadoLibertadOGravamenVO> certificados = new ArrayList<CertificadoLibertadOGravamenVO>();
		certificados.add(clg);
		JRDataSource ds = new JRBeanCollectionDataSource(certificados);

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		String path = ServletUtil.getContextFilePath(httpServletRequest);

		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("reportsPath", Constantes.REPORTES_PATH);		
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		parameterMap.put( JRParameter.REPORT_LOCALE, Constantes.locale );

		parameterMap.put("QR_BASE_URI", parametroRepository.findByCve("QR_BASE_URI").getValor());

		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/certificadoLibertadOGravamen/pdfCertificadoLibertadOGravamenHistorial.jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
	    return new ModelAndView(view, parameterMap);
	}





	/**
	 * Certificado con juridico 
	**/
	@RequestMapping(value = "/lg/juridico/{prelacion}", method = RequestMethod.GET, produces = "application/pdf")
	public ModelAndView imprimeCertificadoJuridico(@PathVariable("prelacion") Long prelacion, Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {
		
		Prelacion pre = prelacionService.findOne(prelacion);
		
		CertificadoPersonaJuridicasVO  clg= null;
		try{

			clg=  certificadoService.generaCertificadoDeLivertadIGravamenJuridico(pre);
		}
		catch(RequerimientoException ex){

			log.debug("Excepcion de al generar certificado " +ex.getMessage());
			return null;

		}

		
		List<CertificadoPersonaJuridicasVO> certificados = new ArrayList<CertificadoPersonaJuridicasVO>();
		certificados.add(clg);
		JRDataSource ds = new JRBeanCollectionDataSource(certificados);

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		String path = ServletUtil.getContextFilePath(httpServletRequest);

		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
		parameterMap.put("path", Constantes.IMG_PATH);

		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/pdfCertificadoPersonasJuridicas.jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
	    return new ModelAndView(view, parameterMap);
	}

	



	/**
	 * Certificado con inserto  
	**/
	@RequestMapping(value = "/lg/inserto/{prelacionId}", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody HttpEntity<byte[]> imprimeCertificadoInserto(HttpServletResponse response, @PathVariable Long prelacionId)throws IOException {
         byte[] pdf = certificadoService.genBoletaCertificadoInserto(prelacionId,false);
         InputStream in = new ByteArrayInputStream(pdf); 
         
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "pdf"));
        header.set("Content-Disposition", "inline; filename= prelacion" + prelacionId);
        header.setContentLength(pdf.length);
        return new HttpEntity<byte[]>(pdf, header);
	}

	/**
	 * Certificado de  no inscripcion  
	**/
	@RequestMapping(value = "/lg/no-inscripcion/{prelacion}", method = RequestMethod.GET, produces = "application/pdf")
	public ModelAndView imprimeCertificadoNoInscripcion(@PathVariable("prelacion") Long prelacion, Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {
		
		Prelacion pre = prelacionService.findOne(prelacion);
		
		CertificadoDeNoInscripcionVO certificadoNoIns = null;

		try{
			
			certificadoNoIns=  certificadoService.generaCertificadoNoInscripcion(pre);
		}
		catch(RequerimientoException ex){

			log.debug("Excepcion de al generar certificado " +ex.getMessage());
			return null;

		}
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			//log.debug(ow.writeValueAsString(prelacion));
			log.debug(ow.writeValueAsString(certificadoNoIns));
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		//CertificadoDeNoInscripcionVO certificadoNoIns = new CertificadoDeNoInscripcionVO();

		List<CertificadoDeNoInscripcionVO> certificados = new ArrayList<CertificadoDeNoInscripcionVO>();
		certificados.add(certificadoNoIns);
		JRDataSource ds = new JRBeanCollectionDataSource(certificados);

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		String path = ServletUtil.getContextFilePath(httpServletRequest);

		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
	
		String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+pre.getId();
		parameterMap.put("QR_BASE_URI", qR);
		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/pdfCertificadoNoInscripcion.jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
	    return new ModelAndView(view, parameterMap);
	}

	/**
	 * Certificado de  inscripcion  
	**/
	@RequestMapping(value = "/lg/inscripcion/{prelacion}", method = RequestMethod.GET, produces = "application/pdf")
	public ModelAndView imprimeCertificadoInscripcion(@PathVariable("prelacion") Long prelacion, Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {
		log.debug("controlerr lg");
		Prelacion pre = prelacionService.findOne(prelacion);
		
		CertificadoDeNoInscripcionVO certificadoIns = null;

		try{
			
			certificadoIns=  certificadoService.generaCertificadoInscripcion(pre);
		}
		catch(RequerimientoException ex){

			log.debug("Excepcion de al generar certificado " +ex.getMessage());
			return null;

		}
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			//log.debug(ow.writeValueAsString(prelacion));
			log.debug(ow.writeValueAsString(certificadoIns));
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		//CertificadoDeNoInscripcionVO certificadoNoIns = new CertificadoDeNoInscripcionVO();

		List<CertificadoDeNoInscripcionVO> certificados = new ArrayList<CertificadoDeNoInscripcionVO>();
		certificados.add(certificadoIns);
		JRDataSource ds = new JRBeanCollectionDataSource(certificados);

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		String path = ServletUtil.getContextFilePath(httpServletRequest);

		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);

		String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+pre.getId();
		parameterMap.put("QR_BASE_URI", qR);
       
		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/pdfCertificadoInscripcion.jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
	    return new ModelAndView(view, parameterMap);
	}
	
	/**
	 * Certificado de No Propiedad
	 */
	@RequestMapping(value = "/lg/no-propiedad/{prelacion}", method = RequestMethod.GET, produces = "application/pdf")
	public ModelAndView imprimeCertificadoNoPropiedad(@PathVariable("prelacion") Long prelacion, Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {
		
		Prelacion pre = prelacionService.findOne(prelacion);
		
		CertificadoDeNoInscripcionVO certificadoNoIns = null;

		try{
			
			certificadoNoIns=  certificadoService.generaCertificadoNoInscripcion(pre);
		}
		catch(RequerimientoException ex){

			log.debug("Excepcion de al generar certificado " +ex.getMessage());
			return null;

		}
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			//log.debug(ow.writeValueAsString(prelacion));
			log.debug(ow.writeValueAsString(certificadoNoIns));
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		//CertificadoDeNoInscripcionVO certificadoNoIns = new CertificadoDeNoInscripcionVO();

		List<CertificadoDeNoInscripcionVO> certificados = new ArrayList<CertificadoDeNoInscripcionVO>();
		certificados.add(certificadoNoIns);
		JRDataSource ds = new JRBeanCollectionDataSource(certificados);

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		String path = ServletUtil.getContextFilePath(httpServletRequest);

		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);

		String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+pre.getId();
		parameterMap.put("QR_BASE_URI", qR);
		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/pdfCertificadoNoPropiedad.jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
	    return new ModelAndView(view, parameterMap);
	}
	
	@RequestMapping(value = "/lg/propiedad/{prelacion}", method = RequestMethod.GET, produces = "application/pdf")
	public ModelAndView imprimeCertificadoPropiedad(@PathVariable("prelacion") Long prelacion, Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {
		
		Prelacion pre = prelacionService.findOne(prelacion);
		
		CertificadoDeNoInscripcionVO certificadoNoIns = null;

		try{
			
			certificadoNoIns=  certificadoService.generaCertificadoNoInscripcion(pre);
		}
		catch(RequerimientoException ex){

			log.debug("Excepcion de al generar certificado " +ex.getMessage());
			return null;

		}
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			//log.debug(ow.writeValueAsString(prelacion));
			log.debug(ow.writeValueAsString(certificadoNoIns));
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		//CertificadoDeNoInscripcionVO certificadoNoIns = new CertificadoDeNoInscripcionVO();

		List<CertificadoDeNoInscripcionVO> certificados = new ArrayList<CertificadoDeNoInscripcionVO>();
		certificados.add(certificadoNoIns);
		JRDataSource ds = new JRBeanCollectionDataSource(certificados);

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		String path = ServletUtil.getContextFilePath(httpServletRequest);

		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);

		String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+pre.getId();
		parameterMap.put("QR_BASE_URI", qR);
		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/pdfCertificadoPropiedad.jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
	    return new ModelAndView(view, parameterMap);
	}

	/**
	 * Certificado de copias
	**/
	@RequestMapping(value = "/copias/{prelacion}", method = RequestMethod.GET, produces = "application/pdf")
	public ModelAndView imprimeCertificadoCopias(@PathVariable("prelacion") Long prelacion, Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {
			Prelacion pre = prelacionService.findOne(prelacion);
			String certifica = " ";
		   CertificadoCopiasVO certificado = null;

		try{

			certificado=  certificadoService.generaCertificadoCopias(pre);
			certifica=  certificadoService.generaTextoCertifica(pre);
		}
		catch(RequerimientoException ex){

			log.debug("Excepcion de al generar certificado " +ex.getMessage());
			return null;

		}

		List<CertificadoCopiasVO> certificados = new ArrayList<CertificadoCopiasVO>();
		certificados.add(certificado);
		JRDataSource ds = new JRBeanCollectionDataSource(certificados);

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		String path = ServletUtil.getContextFilePath(httpServletRequest);
		String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+pre.getId();
		parameterMap.put("QR_BASE_URI", qR);
		
	    parameterMap.put("certifica", certifica);
		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);

		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/pdfCopiasCertificadas.jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
	    return new ModelAndView(view, parameterMap);
	}
	
	/**
	 * Certificado de Impresion de Folios  
	**/
	@RequestMapping(value = "/impresionFolios/{prelacion}", method = RequestMethod.GET, produces = "application/pdf")
	public ModelAndView imprimeCertificadoImpresionFolios(@PathVariable("prelacion") Long prelacion, Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {
		
		Prelacion pre = prelacionService.findOne(prelacion);
		
		CertificadoLibertadOGravamenVO clg= null;

		try{
									 //IHM getCertificado();
			clg=  certificadoService.getCertificadoDeImpresionFolios(pre);
		}
		catch(RequerimientoException ex){

			log.debug("Excepcion de al generar certificado " +ex.getMessage());
			return null;

		}

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			System.out.println("Certificado : ");
			System.out.println(ow.writeValueAsString(clg));
	          log.debug(ow.writeValueAsString(clg));
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		

		List<CertificadoLibertadOGravamenVO> certificados = new ArrayList<CertificadoLibertadOGravamenVO>();
		certificados.add(clg);
		JRDataSource ds = new JRBeanCollectionDataSource(certificados);

		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("reportsPath", Constantes.REPORTES_PATH);		
		parameterMap.put("imgPath", Constantes.IMG_PATH);

		parameterMap.put( JRParameter.REPORT_LOCALE, Constantes.locale );
		String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+pre.getId();
		parameterMap.put("QR_BASE_URI", qR);

		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/certificadoLibertadOGravamen/pdfCertificadoImpresionFolios.jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
	    return new ModelAndView(view, parameterMap);
	}
	

}
