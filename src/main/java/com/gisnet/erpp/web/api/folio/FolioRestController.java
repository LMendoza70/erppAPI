package com.gisnet.erpp.web.api.folio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;


import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.service.CaratulaService;
import com.gisnet.erpp.service.FormasPreCodificadasService;
import com.gisnet.erpp.service.PredioService;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.caratula.PredioVO;
import com.gisnet.erpp.web.api.imprimeprelacion.caratula.CaratulaVO;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperExportManager;

import org.springframework.context.ConfigurableApplicationContext;

@RestController
@RequestMapping("/api/folio/")
public class FolioRestController {

	@Autowired
	private CaratulaService caratulaService;

	@Autowired
	private PredioService predioService;

	@Autowired
	private FormasPreCodificadasService formasService;	

	@Autowired
	ConfigurableApplicationContext applicationContext;

	@RequestMapping(value = "imprimir/{folioId}/{oficinaId}", method = RequestMethod.GET,  produces = "application/pdf")
	public ModelAndView printFolio(
			@PathVariable("folioId") Integer folioId,
			@PathVariable("oficinaId") Long oficinaId,
			Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {


		PredioVO predioVO = caratulaService.getCaratulaPredio(folioId, oficinaId);
		Predio predio = predioService.findOne(predioVO.getPredioId());
		CaratulaVO caratula = new CaratulaVO(predioVO, predio);
		List<CaratulaVO> caratulaList = new ArrayList<CaratulaVO>();

		Long[] statusActos = { 1L, 4L };
		caratula.setValoresActos(formasService.obtenerValoresActos(predio, statusActos));
		caratulaList.add(caratula);

		JRDataSource ds = new JRBeanCollectionDataSource(caratulaList);

		Map<String, Object> parameterMap = new HashMap<String, Object>();

		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("imgPath", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);

		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/pdfImpresionActos.jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
	    return new ModelAndView(view, parameterMap);
	}
	
	@RequestMapping(value = "imprimir-folios/{prelacionId}", method = RequestMethod.GET,  produces = "application/pdf")	
	@ResponseBody HttpEntity<byte[]> printFoliosParaPrelacion(HttpServletResponse response, @PathVariable Long prelacionId)throws IOException {	
		
		byte[] pdf = caratulaService.genBoletaImpresionFolios(prelacionId,false);
         InputStream in = new ByteArrayInputStream(pdf); 
         
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "pdf"));
        header.set("Content-Disposition", "inline; filename= prelacion" + prelacionId);
        header.setContentLength(pdf.length);
        return new HttpEntity<byte[]>(pdf, header);
	}

	@RequestMapping(value = "valida-folios/{noFolio}/{noFolioAnterior}/{auxiliar}/TipoFolio/{tipo}/Oficina/{oficinaId}/validate/{validate}", method = RequestMethod.GET,  produces = "application/pdf")		
	public ResponseEntity<?> validaFolios(HttpServletResponse response, @PathVariable Integer noFolio,@PathVariable Integer noFolioAnterior,@PathVariable Integer auxiliar,@PathVariable Integer tipo,@PathVariable Long oficinaId, @PathVariable boolean validate)throws IOException {	
		boolean valido=false;
		Predio predio=null;
		Long id = null;
		Long tipol = Long.valueOf(tipo.longValue());
		for (Constantes.ETipoFolio tp : Constantes.ETipoFolio.values()) {
			if ((int)(long)tp.idTipoFolio == tipol) {
					valido = true;
			}
		}
		if(valido){
			switch(tipol.intValue()){
				case 1: // PERSONA JURIDICA

				break;
				case 4:  // PREDIO
					System.out.println("IHM FOLIO de PROPIEDAD");
					predio =caratulaService.validaFolioPredio(noFolio,noFolioAnterior, auxiliar,oficinaId);					
					if (predio!=null){
						System.out.println("IHM predioId:"+predio.getId());
						id=predio.getId();
						return new ResponseEntity<>("{\"predioId\": \""+predio.getId()+"\"}", HttpStatus.OK);
					}
				break;
			}			
		}		

		return new ResponseEntity<>("{\"mensaje\":\"NO SE ENCUENTRA EL FOLIO\"}", HttpStatus.NOT_FOUND);
	}
	

	@RequestMapping(value = "preview-imprimir-folios-propiedad/{predioId}", method = RequestMethod.GET,  produces = "application/pdf")		
	@ResponseBody HttpEntity<byte[]> printPreviewImprimirFoliosPropiedad(HttpServletResponse response, @PathVariable Integer predioId)throws IOException {	
		Predio predio = null;

		predio = predioService.findOne(predioId.longValue());		

		byte[] pdf = caratulaService.genPreviewImpresionFoliosPropiedad(predio.getId(),false);
         InputStream in = new ByteArrayInputStream(pdf); 
         
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "pdf"));
        header.set("Content-Disposition", "inline; filename= predioId" + predioId);
        header.setContentLength(pdf.length);
        return new HttpEntity<byte[]>(pdf, header);
	}
	


}
