package com.gisnet.erpp.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.service.UsuarioService;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.ServletUtil;
import com.gisnet.erpp.vo.actopublicitario.Acto54Model;
import com.gisnet.erpp.vo.actopublicitario.ActoPublicitarioVO;
import com.gisnet.erpp.vo.prelacion.ActoPublictarioHisModel;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import com.gisnet.erpp.dao.CaratulaDAO;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.domain.ActoPublicitario;
import com.gisnet.erpp.domain.Area;
import com.gisnet.erpp.domain.AreaClasifActo;
import com.gisnet.erpp.domain.BusquedaResultado;
import com.gisnet.erpp.domain.CampoValores;
import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.Servicio;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.PrelacionRepository;
import com.gisnet.erpp.repository.ServicioRepository;
import com.gisnet.erpp.repository.ActoPredioCampoRepository;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.ActoPublicitarioRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.ParametroRepository;

@Service
public class ActoPublicitarioService {
	
	private final long TIPO_BUSQUEDA_CAMPO_ID = 5068;
	

	@Autowired
	ActoRepository actoRepository;
	
	@Autowired
	CaratulaDAO caratulaDAO;
	
	@Autowired
	ActoPredioRepository actoPredioRepository;

	@Autowired
	ActoPredioCampoRepository actoPredioCampoRepository;

	@Autowired
	ActoPublicitarioRepository actoPublicitarioRepository;
	
	@Autowired
	ParametroRepository parametroRepository;

	@Autowired
	PrelacionRepository prelacionRepository;

	@Autowired
	ServicioRepository servicioRepository;

	@Autowired
	CampoValorService campoValorService;
	
	@Autowired
	BoletaRegistralService boletaRegistralService;
		
	@Autowired
	PdfService pdfService;


	@Autowired
	UsuarioService usuarioService;

	@Autowired
	PrelacionService prelacionService;

	@Autowired
	AreaClasifActoService areaClasifActoService;

	
	@Autowired
	BusquedaResultadoService busquedaResultadoService;

	public List<ActoPublicitarioVO> findActoPublicitario(Integer noActo, Long servicioId) {

		ActoPublicitarioVO resultado = null;
		List<ActoPublicitarioVO> resultados = new ArrayList();

		try {
			Usuario usuario = usuarioService.getLoguedUser();
			Oficina oficina = usuario.getOficina();
			if (oficina != null) {
				ActoPublicitario ap = actoPublicitarioRepository
						.findActoPublicitarioByOficinaIdAndNumActoPublicitario(oficina.getId(), noActo);
				if (ap != null) {
					resultados = prelacionService.getActoPublicitarioById(ap.getId());
				}
			}

			if (resultados == null || resultados.isEmpty()) {
				resultado = new ActoPublicitarioVO();
				resultado.setOficina(usuarioService.getUsuario().getOficina().getNombre());
				System.out.println(usuarioService.getUsuario());
				resultado.setNoRegistrosEncontrados("NO SE ENCONTRARON \n REGISTROS");
				resultados.add(resultado);
			}
			return resultados;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public List<ActoPublicitarioVO> findActoPublicitarioByNumActoPublicitarioAndServicioId(Integer noActo, Long servicioId) {

		ActoPublicitarioVO resultado = null;
		List<ActoPublicitarioVO> resultados = new ArrayList();

		try {
			Usuario usuario = usuarioService.getLoguedUser();
			Oficina oficina = usuario.getOficina();
			if (oficina != null) {
				ActoPublicitario ap = actoPublicitarioRepository
						.findActoPublicitarioByOficinaIdAndNumActoPublicitario(oficina.getId(), noActo);

				boolean isServicioIdPresent = ap.getActo().getTipoActo().getServiciosParaTipoActos()
						.stream().anyMatch(servicio -> servicio.getId().equals(servicioId));

				if (ap != null && isServicioIdPresent) {
					resultados = prelacionService.getActoPublicitarioById(ap.getId());
				}
			}

			if (resultados == null || resultados.isEmpty()) {
				resultado = new ActoPublicitarioVO();
				resultado.setOficina(usuarioService.getUsuario().getOficina().getNombre());
				System.out.println(usuarioService.getUsuario());
				resultado.setNoRegistrosEncontrados("NO SE ENCONTRARON \n REGISTROS");
				resultados.add(resultado);
			}
			return resultados;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public List<ActoPublicitario> findActoPublicitarioHis(Integer noFolio,long idOficina) {

		Usuario usuario;
		try {
			usuario = usuarioService.getLoguedUser();
			Oficina oficina=usuario.getOficina();
			if (idOficina!=-1L) {
				List<ActoPublicitario> ap = actoPublicitarioRepository.findActoPublicitarioHis(idOficina, noFolio);
				return ap;
			}else{
				List<ActoPublicitario> ap = actoPublicitarioRepository.findActoPublicitarioHis(oficina.getId(), noFolio);
				return ap;
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return null;
	}

	public ActoPublicitario findOne(Long id){
		return actoPublicitarioRepository.findOne(id);
	}
	public ActoPublicitario findByActoId(Long id){
		Acto a= actoRepository.findOne(id);
		ActoPublicitario ap=null;
		HashSet <BusquedaResultado> brs=busquedaResultadoService.getPrediosFromPrelacionId(a.getPrelacion().getId());
		for(BusquedaResultado br:brs){
			if(br.getActoPublicitario()!=null){
				ap=br.getActoPublicitario();
			}
		}
		return ap;
	}
	
	public byte[] asientoPorActo(Acto a) {
		byte[] media = null;
		String folder = parametroRepository.findByCve(Constantes.RUTA_ASIENTOS_CVE).getValor();
		
		Path path = Paths.get(folder + "/MIG_" + a.getIdAsiento() + "_ASIENTO.pdf");
		
		try {
			media = Files.readAllBytes(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return media;
	}
	public byte[] asientoPorNombre(String nombre, String apellido_paterno, String apellido_materno) {
		
		
		byte[] asientoHis = null;
		
		if(apellido_paterno.equals("_") && apellido_materno.equals("_")) {
			apellido_paterno=""; apellido_materno="";
		}
		String nombreCompleto = nombre.trim()+""+apellido_paterno.trim()+""+apellido_materno.trim();
		System.out.println(nombreCompleto);
		List<ActoPublictarioHisModel> apsHis =caratulaDAO.findActoPublicitarioHisPorNombre(nombreCompleto);
		//----------------------------
				
			
			if(apsHis!=null && !apsHis.isEmpty()) {
			
				byte[] detalle = null;
				byte[] asiento = null;
				byte[] aux = null;
				int boletas = 0;
				
				for(ActoPublictarioHisModel apH: apsHis) {
					Optional<ActoPublicitario> actoPub=actoPublicitarioRepository.findFirstByActoId(apH.getActoId());
					if(actoPub.isPresent()) {
						System.out.println("Acto pub encontrado "+actoPub.get().toString());
						ActoPublicitario ap = actoPub.get();
						
						try {
							detalle = this.detalleHis(ap);						
							asiento = this.asientoPorActo(ap.getActo());	
							if(boletas == 0) {		
								asientoHis =  pdfService.appendPDF(detalle, asiento);
							}else {
								aux = pdfService.appendPDF(detalle, asiento);
								asientoHis =  pdfService.appendPDF(asientoHis, aux);
							}
							boletas++;
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						//
					}
				}
			}
		
		if(asientoHis==null){
			try {
				asientoHis = this.detalleHis(null);
			} catch (JRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return asientoHis;
	}
	
	private byte[] detalleHis(ActoPublicitario ap) throws JRException {
		
		JasperPrint jasperPrint = null;
        JasperReport jasperReport = null;
        InputStream jasperStream = null;
        JRDataSource ds = null;
        Map<String, Object> parameterMap = new HashMap<String, Object>();
        Prelacion p =null;
    	ActoPublicitarioVO resultado = new ActoPublicitarioVO();	
		List<ActoPublicitarioVO> resultados = new ArrayList<ActoPublicitarioVO>();
		resultado.setOficina(usuarioService.getUsuario().getOficina().getNombre());
		if(ap!=null) {
			 p = ap.getActo().getPrelacion();
			Integer folioReal = ap.getNum_folio_real();
			Integer consecutivo = p.getConsecutivo();
			Integer anio = p.getAnio();
			Integer subindice= p.getSubindice().intValue();
			String oficina = ap.getOficina().getNombre();
			
			String detalle = "ENTRADA: "+consecutivo+"\nAÑO: "
			+ anio+ "\nSUBINDICE "+ subindice+"\nNÚMERO DE FOLIO REAL: "+folioReal+ " \nOFICINA: "+oficina;
			
			resultado.setNoRegistrosEncontrados(detalle);
		}else {
			resultado.setNoRegistrosEncontrados("NO SE ENCONTRARON \n REGISTROS");	
		}
		
		resultados.add(resultado);
		ds = new JRBeanCollectionDataSource(resultados);

        parameterMap.put("datasource", ds);
        parameterMap.put("path", Constantes.IMG_PATH);
		parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
		parameterMap.put("marcaDeAgua", Constantes.IMG_PATH);
		if(ap!=null) {
			String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+p.getId();
			parameterMap.put("QR_BASE_URI", qR);
		}
        jasperStream = this.getClass().getResourceAsStream("/reports/actosPublicitarios/pdfActoPublicitario.jasper");
        jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
        jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);

        return JasperExportManager.exportReportToPdf(jasperPrint);

	}
	
	
	public Optional<ActoPublicitario> findOneByNumeroAndOficinaId(Integer numero,Long oficinaId) {
		return actoPublicitarioRepository.findOneByNumeroAndOficinaId(numero, oficinaId);
	}
	
	public List<Acto> findActosCancelacion(Long actoCancelacionId,Long oficinaId){
		
		List<Acto> actos =  new ArrayList<>();
		Optional<ActoPredio> actoPredio  = actoPredioRepository.findFirstByActoIdOrderByVersionDesc(actoCancelacionId);
		Integer[] folio = {null};
		Integer[] numeroActo   = {null};
		
		if(actoPredio.isPresent()) {
			Set<ActoPredioCampo> actoPredioCampos =  actoPredio.get().getActoPredioCamposParaActoPredios();
			actoPredioCampos.forEach(x->{
				long tipoCampoId =  x.getModuloCampo().getCampo().getTipoCampo().getId();
				
				if(tipoCampoId == Constantes.TipoCampo.NUMERO_ACTO_PUBLICITARIO.getIdTipoCampo()) {
					numeroActo[0] = x.getValor()!=null && !x.getValor().isEmpty() ? Integer.parseInt(x.getValor()) : null;
				}
				if(tipoCampoId == Constantes.TipoCampo.FOLIO_PERSONA_FISICA.getIdTipoCampo()) {
					folio[0] = x.getValor()!=null && !x.getValor().isEmpty() ? Integer.parseInt(x.getValor()) : null;
				}
			});
			
		}
		
		if(numeroActo[0]!=null) {
			Optional<ActoPublicitario> acto = this.findOneByNumeroAndOficinaId(numeroActo[0], oficinaId);
			if(acto.isPresent())
				actos.add(acto.get().getActo());
		}
		
		if(folio[0]!=null) {
			List<ActoPublicitario> actosPub =  this.findActoPublicitarioHis(folio[0], oficinaId);
			if(actosPub!=null && actosPub.size()>0)
				 actosPub.forEach(x->{
					 if(x.getActo().getStatusActo().getId().equals(Constantes.StatusActo.ACTIVO.getIdStatusActo()))
						 actos.add(x.getActo());
				 });
		}
		
		
		return actos;
	}
	
	public String mostrarDetalle(List<ActoPredioCampo> apcs,int moduloIdDetalle){
		String detalle = "";
		for(ActoPredioCampo apc:apcs){
			Integer moduloId = apc.getModuloCampo().getModulo().getId().intValue();		
			if(moduloId == moduloIdDetalle ) {
				detalle += boletaRegistralService.modoDeImpresion(apc);
			}				
		}
		boletaRegistralService.quitarUltimoCaracter(detalle);
		return detalle;
	}
}
