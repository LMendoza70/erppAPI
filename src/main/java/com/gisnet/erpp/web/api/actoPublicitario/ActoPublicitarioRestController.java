package com.gisnet.erpp.web.api.actoPublicitario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

import net.sf.jasperreports.engine.JRDataSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.List;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import com.gisnet.erpp.domain.ActoPublicitario;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.BusquedaResultado;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.ParametroRepository;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.util.Constantes;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import com.gisnet.erpp.util.ServletUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;

import com.gisnet.erpp.vo.actopublicitario.ActoPublicitarioVO;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gisnet.erpp.service.ActoPublicitarioService;
import com.gisnet.erpp.service.ActoService;
import com.gisnet.erpp.service.BusquedaResultadoService;
import com.gisnet.erpp.service.PdfService;
import com.gisnet.erpp.service.PrelacionService;
import com.gisnet.erpp.service.UsuarioService;

@RestController
@RequestMapping(value = "/api/actopublicitario", produces = MediaType.APPLICATION_JSON_VALUE)
public class ActoPublicitarioRestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ActoPublicitarioService actoPublicitarioService;

	@Autowired
	ActoService actoService;

	@Autowired
	PrelacionService prelacionService;

	@Autowired
	PdfService pdfService;

	@Autowired
	ParametroRepository parametroRepository;

	@Autowired
	ConfigurableApplicationContext applicationContext;

	@Autowired // CV
	private BusquedaResultadoService busquedaResultadoService;
	
	@Autowired
	private UsuarioService usuarioService;

	@GetMapping(value = "/noActo/{noActo}/servicio/{servicioId}")
	public ModelAndView getPdfActoPublicitario(@PathVariable Integer noActo, @PathVariable Long servicioId, Model uiModel,
			HttpServletRequest httpServletRequest, ModelAndView modelAndView) throws IOException {

		System.out.println("getPdfAsientoActoPublicitario-----------");
		List<ActoPublicitarioVO> to = null;

		String nombreBoleta1 = "pdfActoPublicitario";

		if (noActo != -1) {
			to = actoPublicitarioService.findActoPublicitarioByNumActoPublicitarioAndServicioId(noActo, servicioId);
		}

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
		System.out.println("-----------" + path);
		parameterMap.put("datasource", ds);
		parameterMap.put("path", Constantes.IMG_PATH);
		//parameterMap.put("marcaDeAgua", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);

		log.debug(Constantes.IMG_PATH);

		log.debug(Constantes.REPORTES_PATH);

		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/actosPublicitarios/" + nombreBoleta1 + ".jasper");
		view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);

		return new ModelAndView(view, parameterMap);

	}

	@RequestMapping(value = "/asiento/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getPdfAsientobyId(@PathVariable String id) throws IOException {
		System.out.println("aps- " + id);

		HttpHeaders headers = new HttpHeaders();

		String folder = parametroRepository.findByCve(Constantes.RUTA_ASIENTOS_CVE).getValor();

		byte[] media = null;

		char[] cadena = id.toCharArray();
		Set<Long> nums = new HashSet<>();
		String n = "";

		for (char c : cadena) {
			if (c != ',') {
				n += c;
			} else {
				nums.add(Long.parseLong(n));
				n = "";
			}
		}
		int i = 0;
		for (Long num1:nums) {
		
			Acto a =actoService.findOne(num1);
			Path path = Paths.get(folder + "/MIG_" + a.getIdAsiento() + "_ASIENTO.pdf");
			if (i == 0) {
				media = actoPublicitarioService.asientoPorActo(a);
			} else {
				try {
					media = pdfService.appendPDF(media, Files.readAllBytes(path));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
            System.out.println("id "+num1);	
            i++;
        }

		
		headers.setCacheControl(CacheControl.noCache().getHeaderValue());
		headers.setContentType(MediaType.APPLICATION_PDF);

		ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
		return responseEntity;
	}


	@GetMapping(value = "/noFolio/{noFolio}/oficina/{oficina}")
	public ResponseEntity<List<ActoPublicitario>>getPdfAsientoActoPublicitarioHis(
		
	@PathVariable Integer noFolio,
	@PathVariable Long oficina
	){
		List<ActoPublicitario> aps=actoPublicitarioService.findActoPublicitarioHis(noFolio, oficina);
	
		return new ResponseEntity<>(aps,HttpStatus.OK);
	}
	
	@GetMapping(value = "/noFolio/{noFolio}")
	public ResponseEntity<List<ActoPublicitario>>getPdfAsientoActoPublicitarioHis(	
	@PathVariable Integer noFolio
	){
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		List<ActoPublicitario> aps=actoPublicitarioService.findActoPublicitarioHis(noFolio,usuario.getOficina().getId());
	
		return new ResponseEntity<>(aps,HttpStatus.OK);
	}
	
	@GetMapping(value = "/find-by-folio/{noFolio}")
	public ResponseEntity<?>getActoPublicitarioByFolio(	
	@PathVariable Integer noFolio
	){
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		List<ActoPublicitario> aps=actoPublicitarioService.findActoPublicitarioHis(noFolio,usuario.getOficina().getId());
		if(aps!=null && aps.size()>0)
		    return new ResponseEntity<>(aps,HttpStatus.OK);
		else
			return new ResponseEntity<>("FOLIO PERSONA FISICA INVALIDO!",HttpStatus.BAD_REQUEST);
			
	}
	
	@GetMapping(value = "/find-by-numero/{numeroActo}")
	public ResponseEntity<?>getActoPublicitarioByNumeroActo(	
	@PathVariable Integer numeroActo
	){
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		Optional<ActoPublicitario> aps=actoPublicitarioService.findOneByNumeroAndOficinaId(numeroActo,usuario.getOficina().getId());
		if(aps.isPresent())
		    return new ResponseEntity<>(aps.get(),HttpStatus.OK);
		else
			return new ResponseEntity<>("ACTO PUBLICITARIO INVALIDO!",HttpStatus.BAD_REQUEST);	
	}
	
	@PutMapping(value="/busqueda-guardada/{prelacionId}/{apId}")
	public ResponseEntity<?> saveBusquedaResultado(@PathVariable("prelacionId") Long prelacionId,@PathVariable("apId") Long apId) {
		BusquedaResultado br=null;	
		try {
				ActoPublicitario ap=actoPublicitarioService.findOne(apId);
				Prelacion pActual= prelacionService.findOne(prelacionId);
				if(ap!=null && pActual!=null ) {
					List<BusquedaResultado> brs= busquedaResultadoService.findAllByPrelacionIdAndPrelacionHistoricaAndActoPublicitarioId(prelacionId,ap.getActo().getPrelacion().getId(),ap.getId());
					if(brs == null || brs.size() == 0) {
						br = new BusquedaResultado(); 
						br.setPrelacionHistorica(ap.getActo().getPrelacion());
						br.setPrelacion(pActual);
						br.setActoPublicitario(ap);					
						return new ResponseEntity<> (busquedaResultadoService.guardar(br), HttpStatus.OK);
					}else {
						br = brs.get(0);
					}
				}
			}
			catch (Exception except) {
				log.info("Error: " + except.getMessage());
				except.printStackTrace();
				return new ResponseEntity<> ("Error: " + except.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<> (br, HttpStatus.OK);
		}
	
	
	
	
	@GetMapping(value = "/acto/{id}")
	public ResponseEntity<ActoPublicitario>getActoPublicitarioByActoId(@PathVariable("id") Long id){
		ActoPublicitario ap= actoPublicitarioService.findByActoId(id);
		System.out.println("ActoPublicitario "+ap);
		return new ResponseEntity<>(ap,HttpStatus.OK);
	}
	
	@RequestMapping(value = "busquedaHisPorNombre/{nombre}/{apellido_paterno}/{apellido_materno}", method = RequestMethod.GET, produces = "application/pdf")
    @ResponseBody HttpEntity<byte[]> getPdfAsientoActoPublicitarioHisPorNombre(HttpServletResponse response, @PathVariable("nombre")String nombre,@PathVariable("apellido_paterno")String apellido_paterno,@PathVariable("apellido_materno")String apellido_materno)throws IOException {
        System.out.println("getPdfAsientoActoPublicitarioHisPorNombre\n"+nombre+" ap: "+apellido_paterno+" am: "+apellido_materno); 
    	byte[] pdf = 	actoPublicitarioService.asientoPorNombre(nombre,apellido_paterno,apellido_materno);
		if(pdf!=null){
			  
			HttpHeaders header = new HttpHeaders();
			header.setContentType(new MediaType("application", "pdf"));
			header.setContentLength(pdf.length);
			return new HttpEntity<byte[]>(pdf, header);
		}else{
			return null;
		}
         
	}
    
    
    @GetMapping(value="/actos-cancelacion/{actoId}")
   public ResponseEntity<List<Acto>> getActosCancelacion(@PathVariable Long actoId){
    	Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
	   return new ResponseEntity<>(this.actoPublicitarioService.findActosCancelacion(actoId,usuario.getOficina().getId()),HttpStatus.OK);
   }

}
