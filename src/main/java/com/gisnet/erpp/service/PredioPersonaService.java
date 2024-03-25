package com.gisnet.erpp.service;

import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.Libro;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.PredioAnte;
import com.gisnet.erpp.domain.PredioPersona;
import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.Antecedente;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.PrelacionAnte;
import com.gisnet.erpp.domain.PrelacionAnteCapHist;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.AntecedenteRepository;
import com.gisnet.erpp.repository.LibroRepository;
import com.gisnet.erpp.repository.PersonaRepository;
import com.gisnet.erpp.repository.PersonaRepositoryCustom;
import com.gisnet.erpp.repository.PredioPersonaRepository;
import com.gisnet.erpp.repository.PrelacionPredioRepository;
import com.gisnet.erpp.repository.PredioAnteRepository;
import com.gisnet.erpp.repository.impl.PersonaRepositoryImpl;
import com.gisnet.erpp.repository.impl.PredioRepositoryImpl;
import com.gisnet.erpp.repository.PrelacionAnteCapHistRepository;
import com.gisnet.erpp.repository.PrelacionAnteRepository;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.AntecedenteVO;
import com.gisnet.erpp.vo.BusquedaPredioVO;
import com.gisnet.erpp.vo.BusquedaVO;
import com.gisnet.erpp.vo.caratula.PropietarioVO;
import com.gisnet.erpp.vo.BusquedaHistoricaVO;
import com.gisnet.erpp.vo.FoliosAnteVO;
import com.gisnet.erpp.vo.PredioVO;
import com.gisnet.erpp.vo.PersonaVO;

import com.gisnet.erpp.web.api.prelacionPredio.PrelacionPredioDTO;
import com.querydsl.core.NonUniqueResultException;

import org.apache.lucene.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Service
public class PredioPersonaService {

	private static final Logger log = LoggerFactory.getLogger(PredioPersonaService.class);

    @Autowired
    PredioPersonaRepository predioPersonaRepository;

	@Autowired
	PrelacionPredioRepository prelacionPredioRepository;
	
	@Autowired
	LibroRepository libroRepository;
	
	@Autowired
	PersonaRepositoryImpl personaRepositoryImpl;
	
	@Autowired
	ActoPredioRepository actoPredioRepository;
	
	@Autowired
	PredioRepositoryImpl predioRepositoryImpl;

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	PredioAnteRepository predioAnteRepository;

	@Autowired
	PrelacionAnteRepository prelacionAnteRepository;

	@Autowired
	PrelacionAnteCapHistRepository prelacionAnteCapHistRepository;

	@Transactional (readOnly = true)
	public Set<PredioPersona> getPredioPersona(Long predioId, Long prelacionId ) {
		return predioPersonaRepository.findAllByPrelacionIdAndPredioId(predioId, prelacionId);
	}
	
	public List<BusquedaHistoricaVO> getPredioPersonaByPersona(Persona persona){

		persona.setNombre(persona.getNombre().replaceAll("\\s+", ""));
		persona.setPaterno(persona.getPaterno().replaceAll("\\s+", ""));
		persona.setMaterno(persona.getMaterno().replaceAll("\\s+", ""));

		System.out.println(persona);
		Oficina oficina=usuarioService.getUsuario().getOficina();
		
		List <Persona> personas = personaRepositoryImpl.findPersonaByParams(persona);

		if(CollectionUtils.isEmpty(personas)) {
			personas = personaRepositoryImpl.findPersonasByNombre(persona);
		}

		if(CollectionUtils.isEmpty(personas)) {
			personas = personaRepositoryImpl.findPersonaByApellidoPaterno(persona);
		}

		if(CollectionUtils.isEmpty(personas)) {
			personas = personaRepositoryImpl.findPersonaByApellidoMaterno(persona);
		}

		if(CollectionUtils.isEmpty(personas)) {
			personas = personaRepositoryImpl.findPersonaByApellidoMaterno(persona);
		}

		List <Long> ids = new ArrayList<Long>();
		List<PredioPersona> predioPersona = new ArrayList<PredioPersona>();
	
		for(Persona per:personas) {
			ids.add(per.getId());
			
			if(ids.size() == 1000){	
				predioPersona.addAll(predioPersonaRepository.findAllByPersonaIdInAndActoPredioActoStatusActoIdAndActoPredioActoPrelacionOficinaId(ids, 1L,oficina.getId()));
				List<PredioPersona> pp = new ArrayList<PredioPersona>();				
				pp = predioPersonaRepository.findAllFoliosPrecaptura(ids,1L,oficina.getId());
				if (pp!=null && pp.size()>0){					
					predioPersona.addAll(pp);
				}
				ids.clear();				
			}
			
		}
				
		if(ids.size() < 1000 && personas.size()>0){
			predioPersona.addAll(predioPersonaRepository.findAllByPersonaIdInAndActoPredioActoStatusActoIdAndActoPredioActoPrelacionOficinaId(ids, 1L,oficina.getId()));		
			List<PredioPersona> pp1 = new ArrayList<PredioPersona>();					
			pp1 = predioPersonaRepository.findAllFoliosPrecaptura(ids,1L,oficina.getId());
			if (pp1!=null && pp1.size()>0){
				predioPersona.addAll(pp1);
			}						
		}
		System.out.println("Tamaño de Predio persona ->" + predioPersona.size());

		List<BusquedaHistoricaVO> busquedasHistoricoVO = new ArrayList<BusquedaHistoricaVO>();		
		busquedasHistoricoVO=obtenBusquedasHistoricoVObyPredioPersona(predioPersona);
		
		System.out.println("Tamaño de busquedasHistoricoVO 2->" + busquedasHistoricoVO.size());
		return busquedasHistoricoVO;
	}
	
	public List<BusquedaHistoricaVO> getPredioPersonaByLibro(AntecedenteVO ante){
		
		System.out.println("Libro NUMERO----> " + ante.getNumero());
		System.out.println("Libro LIBRO----> " + ante.getLibro());
		System.out.println("Libro LIBROBIS----> " + ante.getLibroBis());
		System.out.println("Libro Seccion----> " + ante.getSeccion());
		System.out.println("Libro Oficina----> " + ante.getOficina());

		System.out.println("Libro Año----> " + ante.getAnio()); //Año
		System.out.println("Libro Volumen----> " + ante.getVolumen());
		System.out.println("Libro DOC----> " + ante.getDocumento()); //Inscripción

		System.out.println("Libro DOCBIS----> " + ante.getDocumentoBis());		
		System.out.println("Libro Tipo DOc----> " + ante.isTipoDoc());

		boolean activaBusq = false;
		if(ante.getNumero() == null && ante.getLibro() == null
				&& ante.getLibroBis() == null
				&& ante.getSeccion() == null && ante.getOficina() == null
				&& (ante.getAnio() != null && ante.getDocumento() != null) //Cuando se ocupe validar solo se ingresa aquí
				&& ante.getVolumen() == null && ante.getDocumentoBis() == null
				&& ante.isTipoDoc() == null){
				activaBusq = true; //Esto es solo cuando se mete el anio y el documento.
				ante.setLibroBis("");
				ante.setSeccion("0");
				ante.setOficina("0");
		}

		
		List<BusquedaHistoricaVO> busquedasHistorica  =new ArrayList<BusquedaHistoricaVO>();
		List<PredioPersona> prediPer = new ArrayList<PredioPersona>();
		List<PredioAnte> prediosAnte = new ArrayList<PredioAnte>();
		PredioAnte predAnte = null;
		prediosAnte = null;

		
		if(ante.getLibroBis() != null && ante.getLibroBis().equals("UNICO")) {
			ante.setLibroBis("0");
		}
		if(ante.getLibroBis() != null && ante.getLibroBis().equals("BIS")) {
			ante.setLibroBis("1");
		}
		
		try {
			if(activaBusq){
				prediosAnte = libroRepository.findAllPredioAnteByAnteLibro(ante.getNumero(), ante.getDocumentoBis(),
						ante.getDocumento(), ante.getAnio(),  ante.getVolumen(), ante.getLibro(), ante.getLibroBis(),
						Long.parseLong(ante.getSeccion()), Long.parseLong(ante.getOficina()));
				System.out.println("Busqcamos por anio y dcoto inscripción");
			}else{
				prediosAnte = libroRepository.findAllPredioAnteByAnteLibro(ante.getNumero(), ante.getDocumentoBis(),
						ante.getDocumento(), ante.getAnio(),  ante.getVolumen(), ante.getLibro(), ante.getLibroBis(),
						Long.parseLong(ante.getSeccion()), Long.parseLong(ante.getOficina()));
				System.out.println("Seguimos busqueda original");
			}

						
			if (prediosAnte!= null) {			
				System.out.println(" prediosAnte.size(): "+ prediosAnte.size());
				for(int i=0;i<prediosAnte.size();i++){
					BusquedaHistoricaVO busquedaHistorica = new BusquedaHistoricaVO();
					FoliosAnteVO folios = new FoliosAnteVO();	
					predAnte=prediosAnte.get(i);
					if(predAnte.getPredio()!=null){
						folios.setPredio(getPredioVObyPedio(predAnte.getPredio()));
						if(predAnte.getPredio().getStatusActo().getId()==3){
								
								StringBuilder sb = new StringBuilder();
								sb.append("");
								List<PrelacionAnte> listPrelacionAnte = prelacionAnteRepository.findByPredioId(predAnte.getPredio().getId());

								if(listPrelacionAnte!=null && listPrelacionAnte.size()>0) {
									for(PrelacionAnte pa:listPrelacionAnte){
										sb.append("EN PROCESO, ENTRADA "+ 
										pa.getPrelacion().getConsecutivo()+"-"+
										pa.getPrelacion().getAnio()+"-"+
										pa.getPrelacion().getSubindice()+"\n"
										);
									}									
								}
								
								PrelacionAnteCapHist pach = null;
								pach = prelacionAnteCapHistRepository.findByPredioIdAndTipoFolioId(predAnte.getPredio().getId(), java.lang.Long.valueOf(4));
								if(pach!=null){
									sb.append("EN PROCESO, CARGA: "+ 
									pach.getCargaTrabajo()+"\n"
									);
								}

								folios.setEnProceso(sb.toString());
						}
					}
					if(predAnte.getAntecedente()!=null){
						folios.setAntecedente(getAntecedenteVObyAntecedente(predAnte.getAntecedente()));
					}
					busquedaHistorica.setFoliosAnteVO(folios);
					
					System.out.println(" predAnte.getPredio().getId(): "+ predAnte.getPredio().getId()+" - "+ predAnte.getPredio().getNoFolio());
					prediPer = predioPersonaRepository.findByActoPredioPredioIdAndActoPredioActoStatusActoId(predAnte.getPredio().getId(),1L);
					System.out.println(" prediPer.size(): "+prediPer.size());
					if (prediPer.size() == 0) {
						System.out.println("SIZE a--->" + prediPer.size());
						PredioPersona pred = new PredioPersona();
						ActoPredio actoPred = new ActoPredio();
						actoPred.setPredio(predAnte.getPredio());
						pred.setActoPredio(actoPred);
						prediPer.add(pred);
					}
					if(prediPer!=null){
						busquedaHistorica.setPersonaVO(getListPersonaVObyPredioPersona(prediPer));
					}
					busquedasHistorica.add(busquedaHistorica);
				}
			}
		} catch (NonUniqueResultException uniqueException) {
			throw uniqueException;
		}
		
	return busquedasHistorica;
	}

	List<BusquedaHistoricaVO> obtenBusquedasHistoricoVObyPredioPersona(List<PredioPersona> predioPersona){
		List<BusquedaHistoricaVO> busquedasHistoricoVO = new ArrayList<BusquedaHistoricaVO>();
		Set<Predio> predios = new LinkedHashSet<Predio>();
		for(PredioPersona pp:predioPersona) {
			if(pp.getActoPredio().getPredio()!=null) {
				if(pp.getActoPredio().getPredio().getBloqueado()!=null){
					if(pp.getActoPredio().getPredio().getBloqueado()==false){
						predios.add(pp.getActoPredio().getPredio());
					}
				}else {
					predios.add(pp.getActoPredio().getPredio());
				}
			}
		}
		for(Predio p:predios){
			BusquedaHistoricaVO busquedaH = new BusquedaHistoricaVO();			
			FoliosAnteVO folios = new FoliosAnteVO();
			folios.setPredio(getPredioVObyPedio(p));
			Optional<PredioAnte> pa = predioAnteRepository.findFirstByPredioIdOrderByAntecedenteLibroAnioDesc(p.getId());
			if(pa.isPresent()) {
				folios.setAntecedente(getAntecedenteVObyAntecedente(pa.get().getAntecedente()));
			}
			busquedaH.setFoliosAnteVO(folios);
			List<PredioPersona> prediPer = predioPersonaRepository.findByActoPredioPredioIdAndActoPredioActoStatusActoId(p.getId(),1L);			
			busquedaH.setPersonaVO(getListPersonaVObyPredioPersona(prediPer));
			busquedasHistoricoVO.add(busquedaH);
		}
		return busquedasHistoricoVO; 
	}

	public PredioVO getPredioVObyPedio(Predio predio) {
		PredioVO predioVO = new PredioVO();
		predioVO.setNoFolio(predio.getNoFolio()!=null?predio.getNoFolio():null);
		predioVO.setNumeroFolioReal(predio.getNumeroFolioReal()!=null?predio.getNumeroFolioReal():null);
		predioVO.setAuxiliar(predio.getAuxiliar()!=null?predio.getAuxiliar():null);
		predioVO.setStatusActo(predio.getStatusActo()!=null?predio.getStatusActo():null);
		predioVO.setTipoVialidad(predio.getTipoVialidad()!=null?predio.getTipoVialidad():null);
		predioVO.setVialidad(predio.getVialidad()!=null?predio.getVialidad():"");
		predioVO.setNumExt(predio.getNumExt()!=null?predio.getNumExt():"");
		predioVO.setNumInt(predio.getNumInt()!=null?predio.getNumInt():"");
		predioVO.setNoLote(predio.getNoLote()!=null?predio.getNoLote():"");
		predioVO.setFraccion(predio.getFraccion()!=null?predio.getFraccion():null);
		predioVO.setManzana(predio.getManzana()!=null?predio.getManzana():"");
		predioVO.setTipoAsent(predio.getTipoAsent()!=null?predio.getTipoAsent():null);
		predioVO.setAsentamiento(predio.getAsentamiento()!=null?predio.getAsentamiento():"");
		predioVO.setMunicipio(predio.getMunicipio()!=null?predio.getMunicipio():null);
		predioVO.setProcedeDeFolio(predio.getProcedeDeFolio()!=null?predio.getProcedeDeFolio():null);
		predioVO.setPrimerRegistro(predio.getPrimerRegistro()!=null?predio.getPrimerRegistro():null);
		predioVO.setBloqueado(predio.getBloqueado()!=null?predio.getBloqueado():null);
		return predioVO;		
	}

	public AntecedenteVO getAntecedenteVObyAntecedente(Antecedente antecedente) {
		AntecedenteVO antecedenteVO = new AntecedenteVO();
		antecedenteVO.setNumero(antecedente.getLibro().getNumLibro());
		antecedenteVO.setLibro(antecedente.getLibro().getLibroBis());
		antecedenteVO.setSeccion(antecedente.getLibro().getSeccionesPorOficina().getSeccion().getNombre());
		antecedenteVO.setOficina(antecedente.getLibro().getSeccionesPorOficina().getOficina().getNombre());
		antecedenteVO.setAnio(antecedente.getLibro().getAnio());
		antecedenteVO.setVolumen(antecedente.getLibro().getVolumen());
		antecedenteVO.setDocumento(antecedente.getDocumento());
		return antecedenteVO;
	}

	public List<PersonaVO> getListPersonaVObyPredioPersona(List<PredioPersona> predioPersonas){
		List<PersonaVO> prediosPer = new ArrayList<PersonaVO>();
		Acto aAnte = null;
		for(PredioPersona pp:predioPersonas){
			PersonaVO p = new PersonaVO();			
			Acto a = pp.getActoPredio().getActo();

			if(pp.getPersona()!=null){
				if(aAnte!=null){
					if(aAnte.getId()!=a.getId()) {
						p.setNombre(pp.getPersona().getNombre()!=null?pp.getPersona().getNombre():"");
						p.setPaterno(pp.getPersona().getPaterno()!=null?pp.getPersona().getPaterno():"");
						p.setMaterno(pp.getPersona().getMaterno()!=null?pp.getPersona().getMaterno():"");
						prediosPer.add(p);
					}
				} else{
					p.setNombre(pp.getPersona().getNombre()!=null?pp.getPersona().getNombre():"");
					p.setPaterno(pp.getPersona().getPaterno()!=null?pp.getPersona().getPaterno():"");
					p.setMaterno(pp.getPersona().getMaterno()!=null?pp.getPersona().getMaterno():"");
					prediosPer.add(p);
				}								
			}			
			aAnte=a;
		}
		return prediosPer;
	}

	public List<BusquedaHistoricaVO> getPredioPersonaByBusquedaVo(BusquedaVO busquedaVO) {

		List<BusquedaHistoricaVO> busquedasHistoricoVO = new ArrayList<BusquedaHistoricaVO>();

		BusquedaPredioVO det = new BusquedaPredioVO();

		List<PredioPersona> prediP = new ArrayList<PredioPersona>();
		List<PredioPersona> prediPer = new ArrayList<PredioPersona>();

		det.setTipoInmueble(busquedaVO.getPredio().getTipoInmueble());
		det.setNumInt(busquedaVO.getPredio().getNumInt());
		det.setNivel(busquedaVO.getPredio().getNivel());
		det.setEdificio(
				busquedaVO.getPredio().getEdificio() != null ? busquedaVO.getPredio().getEdificio().toString() : null);
		det.setLocalidadSector(busquedaVO.getPredio().getLocalidadSector());
		det.setNoLote(busquedaVO.getPredio().getNoLote());
		det.setFraccion(busquedaVO.getPredio().getFraccion());
		det.setManzana(busquedaVO.getPredio().getManzana());
		det.setMacroManzana(busquedaVO.getPredio().getMacroManzana());
		det.setTipoVialidad(busquedaVO.getPredio().getTipoVialidad());
		det.setVialidad(busquedaVO.getPredio().getVialidad());
		det.setNumExt(busquedaVO.getPredio().getNumExt());
		det.setEnlaceVialidad(busquedaVO.getPredio().getEnlaceVialidad());
		det.setTipoVialidad2(busquedaVO.getPredio().getTipoVialidad2());
		det.setVialidad2(busquedaVO.getPredio().getVialidad2());
		det.setNumExt2(busquedaVO.getPredio().getNumExt2());
		det.setTipoAsent(busquedaVO.getPredio().getTipoAsent());
		det.setAsentamiento(busquedaVO.getPredio().getAsentamiento());
		det.setFracOCondo(busquedaVO.getPredio().getFracOCondo());
		det.setNombreFracOCondo(busquedaVO.getPredio().getNombreFracOCondo());
		det.setEtapaOSeccion(busquedaVO.getPredio().getEtapaOSeccion());
		det.setZona(busquedaVO.getPredio().getZona());
		det.setClaveCatastral(busquedaVO.getPredio().getClaveCatastral());
		det.setCuentaCatastral(busquedaVO.getPredio().getClaveCatastral());
		det.setClaveCatastralEstandard(busquedaVO.getPredio().getClaveCatastralEstandard());
		det.setMunicipio(busquedaVO.getPredio().getMunicipio());
		det.setCodigoPostal(busquedaVO.getPredio().getCodigoPostal());
		det.setSuperficie(busquedaVO.getPredio().getSuperficie());
		det.setUnidadMedida(busquedaVO.getPredio().getUnidadMedida());
		det.setUsoSuelo(busquedaVO.getPredio().getUsoSuelo());
		// det.setco(busquedaVO.getPredio().getColindanciasParaPredioRels());
		// det.setVarianteMunicipio();

		List<Predio> predios = predioRepositoryImpl.findPrediosByBusqueda(det);
		List<Long> prediosIds = new ArrayList<Long>();

		for (Predio predio : predios) {
			prediosIds.add(predio.getId());
		}

		prediPer = predioPersonaRepository.findByActoPredioPredioIdInAndActoPredioActoStatusActoId(prediosIds, 1L);

		if (prediPer.size() == 0 && predios.size() > 0) {
			for (Predio predio : predios) {
				PredioPersona pred = new PredioPersona();
				ActoPredio actoPred = new ActoPredio();
				actoPred.setPredio(predio);
				pred.setActoPredio(actoPred);
				prediP.add(pred);
			}
		}

		if ((prediPer.size() != predios.size()) && (prediPer.size() > 0 && predios.size() > 0)) {

			List<Predio> prediosTemp = new ArrayList<Predio>();

			for (PredioPersona predioPe : prediPer) {prediosTemp.add(predioPe.getActoPredio().getPredio());}
			predios.removeAll(prediosTemp);

			for (Predio predio : predios) {
				PredioPersona pred = new PredioPersona();
				ActoPredio actoPred = new ActoPredio();
				actoPred.setPredio(predio);
				pred.setActoPredio(actoPred);
				prediP.add(pred);
			}

		}

		prediPer.addAll(prediP);
		busquedasHistoricoVO=obtenBusquedasHistoricoVObyPredioPersona(prediPer);

		return busquedasHistoricoVO;
	}

	@Transactional (readOnly = true)
	public List<PredioPersona> findPropietariosActuales(Long predioId, boolean primerRegistro ){	
		List<ActoPredio> actoPredios = actoPredioRepository.findByFolioIdAndTipoFolioAndStatusActoAndTipoEntradaId(predioId, Constantes.ETipoFolio.PREDIO, Constantes.StatusActo.ACTIVO, Constantes.TipoEntrada.SALIDA  );				
		
		List<Long> ids = new ArrayList<Long>();
		for(ActoPredio ap : actoPredios) {
			ids.add(ap.getId());
		}		
		List<PredioPersona> predioPersonasFiltrado = new ArrayList<PredioPersona>();

		List<PredioPersona> predioPersonas = predioPersonaRepository.findAllByActoPredioIdIn(ids);
		
		if(predioPersonas!=null && predioPersonas.size()>0)
		 predioPersonas =  predioPersonas.stream().filter(x->x.getExtinto() == null || x.getExtinto() == false ).collect(Collectors.toList());
		
		if( primerRegistro ) {
			return predioPersonas; 
		}
		
		for( PredioPersona pp : predioPersonas ) {
			//if( pp.getPrimerRegistro() == null || !pp.getPrimerRegistro() )
			predioPersonasFiltrado.add(pp);
		}
		
		return predioPersonasFiltrado;
	}
	
	@Transactional (readOnly = true)
	public List<PredioPersona> findPropietariosActualesConExtintos(Long predioId){	
		List<ActoPredio> actoPredios = actoPredioRepository.findByFolioIdAndTipoFolioAndStatusActoAndTipoEntradaId(predioId, Constantes.ETipoFolio.PREDIO, Constantes.StatusActo.ACTIVO, Constantes.TipoEntrada.SALIDA  );				
		
		List<Long> ids = new ArrayList<Long>();
		for(ActoPredio ap : actoPredios) {
			ids.add(ap.getId());
		}		
		List<PredioPersona> predioPersonasFiltrado = new ArrayList<PredioPersona>();

		List<PredioPersona> predioPersonas = predioPersonaRepository.findAllByActoPredioIdIn(ids);
		
		for( PredioPersona pp : predioPersonas ) {
			predioPersonasFiltrado.add(pp);
		}
		
		return predioPersonasFiltrado;
	}
	
	@Transactional (readOnly = true)
	public List<PropietarioVO> findPropietariosIniciales(Long predioId){
		List<PropietarioVO> result = new ArrayList<PropietarioVO>();
		List<ActoPredio> actoPredios = actoPredioRepository.findByFolioIdAndTipoFolioAndStatusActoAndTipoEntradaIdAndPrimerRegistro(predioId, Constantes.ETipoFolio.PREDIO, Constantes.StatusActo.ACTIVO, Constantes.TipoEntrada.SALIDA, true  );
				
		List<Long> ids = new ArrayList<Long>();
		for(ActoPredio ap : actoPredios) {
			ids.add(ap.getId());
		}
		List<PredioPersona> predioPersonas = predioPersonaRepository.findAllByActoPredioIdIn(ids);
		
		for(PredioPersona predioPersona : predioPersonas) {
			result.add(PropietarioVO.converToPropietarioVO(predioPersona));
		}
		return result;
	}

	public PredioPersona savePredioPersona(PredioPersona predioPersona) {
		return predioPersonaRepository.saveAndFlush( predioPersona );
	}

	public List<BusquedaHistoricaVO> getPredioPersonaByNoFolio(Integer noFolio){
		List<PredioPersona> prediPer = new ArrayList<PredioPersona>();
		List<BusquedaHistoricaVO> busquedasHistoricoVO = new ArrayList<BusquedaHistoricaVO>();
		System.out.println("Folio ----> " + noFolio);

		try {

			prediPer = predioPersonaRepository.findByActoPredioPredioNoFolioAndActoPredioActoStatusActoId(noFolio,1L);

				/*if (prediPer.size() == 0) {
					System.out.println("SIZE a--->" + prediPer.size());
					PredioPersona pred = new PredioPersona();
					ActoPredio actoPred = new ActoPredio();					
					pred.setActoPredio(actoPred);
					prediPer.add(pred);

				}*/			
			busquedasHistoricoVO=obtenBusquedasHistoricoVObyPredioPersona(prediPer);						

		} catch (NonUniqueResultException uniqueException) {
			throw uniqueException;
		}

		return busquedasHistoricoVO;
	}

	public List<PredioPersona> findPropietariosInicialesMantenimiento(Long predioId){
		
		List<ActoPredio> actoPredios = actoPredioRepository.findByFolioIdAndTipoFolioAndTipoEntradaIdAndPrimerRegistro(predioId, Constantes.ETipoFolio.PREDIO,  Constantes.TipoEntrada.SALIDA, true  );
				
		List<Long> ids = new ArrayList<Long>();
		for(ActoPredio ap : actoPredios) {
			ids.add(ap.getId());
		}		

		List<PredioPersona> predioPersonas = predioPersonaRepository.findAllByActoPredioIdIn(ids);
		

		return predioPersonas;
	}

	public List<PredioPersona> findPropietariosActuales(Long predioId, boolean primerRegistro,List<ActoPredio> actoPredioModificatorio ){
		
		List<Long> ids = new ArrayList<Long>();
		for(ActoPredio ap : actoPredioModificatorio) {
			ids.add(ap.getId());
		}		
		List<PredioPersona> predioPersonasFiltrado = new ArrayList<PredioPersona>();

		List<PredioPersona> predioPersonas = predioPersonaRepository.findAllByActoPredioIdIn(ids);
		if( primerRegistro ) {
			return predioPersonas; 
		}
		
		for( PredioPersona pp : predioPersonas ) {
			if( pp.getPrimerRegistro() == null || !pp.getPrimerRegistro() )
				predioPersonasFiltrado.add(pp);
		}
		
		return predioPersonasFiltrado;
	}
	
	public void deleteById(Long id) {
		predioPersonaRepository.delete(id);
	}
	


}
