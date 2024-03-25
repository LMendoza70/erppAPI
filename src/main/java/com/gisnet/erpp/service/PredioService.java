package com.gisnet.erpp.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import com.gisnet.erpp.vo.caratula.ColindanciaVO;
import com.gisnet.erpp.vo.caratula.PropietarioVO;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.domain.ActoPredioMonto;
import com.gisnet.erpp.domain.Antecedente;
import com.gisnet.erpp.domain.Asentamiento;
import com.gisnet.erpp.domain.Bitacora;
import com.gisnet.erpp.domain.Colindancia;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.PredioAnte;
import com.gisnet.erpp.domain.PredioBitacora;
import com.gisnet.erpp.domain.PredioBitacoraColindancia;
import com.gisnet.erpp.domain.PredioExtinto;
import com.gisnet.erpp.domain.PredioPersona;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.domain.Status;
import com.gisnet.erpp.domain.StatusActo;
import com.gisnet.erpp.domain.TipoFolio;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.FoliosFracCond;
import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.PredioAnteRepository;
import com.gisnet.erpp.repository.PredioBitacoraRepository;
import com.gisnet.erpp.repository.PredioBitacoraColindanciaRepository;
import com.gisnet.erpp.repository.PredioPersonaRepository;
import com.gisnet.erpp.repository.PredioRepository;
import com.gisnet.erpp.repository.PredioExtintoRepository;
import com.gisnet.erpp.repository.PrelacionContratanteRepository;
import com.gisnet.erpp.repository.PrelacionPredioRepository;
import com.gisnet.erpp.repository.StatusActoRepository;
import com.gisnet.erpp.repository.TipoAsentRepository;
import com.gisnet.erpp.repository.TipoEntradaRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.AsentamientoRepository;
import com.gisnet.erpp.repository.ColindanciaRepository;
import com.gisnet.erpp.repository.FoliosFracCondRepository;
import com.gisnet.erpp.repository.MunicipioRepository;
import com.gisnet.erpp.repository.OficinaRepository;
import com.gisnet.erpp.repository.PersonaRepository;
import com.gisnet.erpp.repository.TipoFolioRepository;
import com.gisnet.erpp.repository.TipoRelPersonaRepository;
import com.gisnet.erpp.repository.VialidadRepository;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.service.materializacion.ActoPorcentajeVO;
import com.gisnet.erpp.service.materializacion.MaterializacionActoModificatorioService;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.AntecedenteVO;
import com.gisnet.erpp.vo.FolioSeccionAuxiliarAnteVO;
import com.gisnet.erpp.vo.FoliosAnteVO;
import com.gisnet.erpp.vo.MuebleAnteVO;
import com.gisnet.erpp.vo.PersonaJuridicaAnteVO;
import com.gisnet.erpp.vo.PredioAnteVO;
import com.gisnet.erpp.vo.PredioVO;
import com.gisnet.erpp.web.api.predio.FolioDTO;
import com.gisnet.erpp.web.api.predio.PredioDTO;
import com.gisnet.erpp.repository.ActoDocumentoRepository;
import com.gisnet.erpp.repository.ActoPredioCampoRepository;

import javax.validation.constraints.NotNull;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@Service
public class PredioService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PredioRepository predioRepository;

	@Autowired
	private PredioRepository predioRepository2;
	@Autowired
	private PredioBitacoraColindanciaRepository pBitacoraColindanciaRepositoy;

	@Autowired
	private PredioAnteRepository predioAnteRepository;
	@Autowired
	PrelacionContratanteRepository prelacionContratanteRepository;

	@Autowired
	private PrelacionPredioRepository prelacionPredioRepository;

	@Autowired
	private TipoFolioRepository tipoFolioRepository;

	@Autowired
	private ActoRepository actoRepository;

	@Autowired
	ActoPredioCampoRepository actoPredioCampoRepository;

	@Autowired
	private AsentamientoRepository asentamientoRepository;

	@Autowired
	private VialidadRepository vialidadRepository;

	@Autowired
	private MunicipioRepository municipioRepository;

	@Autowired
	private TipoAsentRepository tipoAsentRepository;

	@Autowired
	private StatusActoRepository statusActoRepository;

	@Autowired
	private ColindanciaRepository colindanciaRepository;

	@Autowired
	private FoliosFracCondRepository foliosFracCondRepository;

	@Autowired
	private OficinaRepository oficinaRepository;

	@Autowired
	private PredioBitacoraService predioBitacoraService;

	@Autowired
	private PredioBitacoraRepository predioBitacoraRepository;

	@Autowired
	private MuebleService muebleService;

	@Autowired
	FolioSeccionAuxiliarService folioSeccionAuxiliarService;

	@Autowired
	PersonaJuridicaService personaJuridicaService;

	@Autowired
	private ActoDocumentoRepository actoDocumentoRepository;

	@Autowired
	private PersonaRepository personaRepository;

	@Autowired
	private TipoRelPersonaRepository tipoRelPersonaRepository;

	@Autowired
	private PredioPersonaRepository predioPersonaRepository;

	@Autowired
	private ActoPredioRepository actoPredioRepository;

	@Autowired
	private TipoEntradaRepository tipoEntradaRepository;

	@Autowired
	private ActoService actoService;

	@Autowired
	private TurnadorService turnadorService;

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	private PredioBitacoraColindanciaRepository predioBitacoraColindanciaRepository;

	@Autowired
	private PaseFracCondService paseFracCondService;

	@Autowired
	PredioExtintoRepository predioExtintoRepository;

	@Autowired
	MaterializacionActoModificatorioService materializacionActoModificatorioService;

	@Autowired
	BitacoraMantenimientoService bitacoraMantenimientoService;

	@Transactional
	public Predio savePredio(PredioDTO predioDTO) {
		Predio predio = null;
		Predio bPredio = new Predio();

		if (predioDTO.getId() != null) {
			System.out.println("ES GESTION");
			predio = predioRepository.findOne(predioDTO.getId());
			bPredio = predioRepository2.findOne(predioDTO.getId());
			System.out.println("*************---{} " + predioDTO.getColindancias());
			Set<Colindancia> colindanciaBitacora = colindanciaRepository.findByPredioId(bPredio.getId());
			System.out.println("*************---{} " + colindanciaBitacora);
			predioBitacoraService.saveBitacoraPredio(bPredio, colindanciaBitacora, null);

		} else {
			predio = new Predio();
		}

		System.out.println("PS:681" + predioDTO.getProcedeDeFolio());
		if (predioDTO.getProcedeDeFolio() != null) {
			predio.setProcedeDeFolio(predioDTO.getProcedeDeFolio());
			System.out.println("PS:681" + predioDTO.getProcedeDeFolio());
		}
		////////////////////////////////////////////////////////////////////////////
		predio.setDocumento(predioDTO.getDocumento());
		predio.setLibro(predioDTO.getLibro());

		////////////////////////////////////////////////////////////////////////////
		predio.setTipoInmueble(predioDTO.getTipoInmueble());
		predio.setNumInt(predioDTO.getNumInt() == null ? predioDTO.getNumInt() : predioDTO.getNumInt().toUpperCase());
		predio.setNivel(predioDTO.getNivel());
		predio.setEdificio(
				predioDTO.getEdificio() == null ? predioDTO.getEdificio() : predioDTO.getEdificio().toUpperCase());
		predio.setLocalidadSector(predioDTO.getLocalidadSector() == null ? predioDTO.getLocalidadSector()
				: predioDTO.getLocalidadSector().toUpperCase());
		predio.setNoLote(predioDTO.getNoLote() == null ? predioDTO.getNoLote() : predioDTO.getNoLote().toUpperCase());
		predio.setFraccion(
				predioDTO.getFraccion() == null ? predioDTO.getFraccion() : predioDTO.getFraccion().toUpperCase());
		predio.setManzana(
				predioDTO.getManzana() == null ? predioDTO.getManzana() : predioDTO.getManzana().toUpperCase());
		predio.setMacroManzana(predioDTO.getMacroManzana());

		predio.setTipoVialidad(predioDTO.getTipoVialidad());
		predio.setVialidad(
				predioDTO.getVialidad() == null ? predioDTO.getVialidad() : predioDTO.getVialidad().toUpperCase());
		predio.setNumExt(predioDTO.getNumExt() == null ? predioDTO.getNumExt() : predioDTO.getNumExt().toUpperCase());
		predio.setEnlaceVialidad(predioDTO.getEnlaceVialidad());
		predio.setTipoVialidad2(predioDTO.getTipoVialidad2());
		predio.setVialidad2(
				predioDTO.getVialidad2() == null ? predioDTO.getVialidad2() : predioDTO.getVialidad2().toUpperCase());
		predio.setNumExt2(
				predioDTO.getNumExt2() == null ? predioDTO.getNumExt2() : predioDTO.getNumExt2().toUpperCase());

		predio.setTipoAsent(predioDTO.getTipoAsent());
		predio.setAsentamiento(predioDTO.getAsentamiento() == null ? predioDTO.getAsentamiento()
				: predioDTO.getAsentamiento().toUpperCase());
		predio.setFracOCondo(predioDTO.getFracOCondo());
		predio.setNombreFracOCondo(predioDTO.getNombreFracOCondo() == null ? predioDTO.getNombreFracOCondo()
				: predioDTO.getNombreFracOCondo().toUpperCase());
		predio.setEtapaOSeccion(predioDTO.getEtapaOSeccion());

		predio.setZona(predioDTO.getZona() == null ? predioDTO.getZona() : predioDTO.getZona().toUpperCase());
		predio.setClaveCatastral(predioDTO.getClaveCatastral());
		predio.setCuentaCatastral(predioDTO.getCuentaCatastral());
		predio.setClaveCatastralEstandard(predioDTO.getClaveCatastralEstandard());

		predio.setMunicipio(predioDTO.getMunicipio());
		predio.setCodigoPostal(predioDTO.getCodigoPostal());
		predio.setSuperficie(predioDTO.getSuperficie());
		predio.setSuperficieM2(predioDTO.getSuperficieM2());
		predio.setUnidadMedida(predioDTO.getUnidadMedida());
		predio.setUsoSuelo(predioDTO.getUsoSuelo());
		predio.setHeredaActo(predioDTO.getHeredaActo());
		Colindancia[] colindancias = predioDTO.getColindancias();

		if (predio.getId() == null) {
			predio.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.TEMPORAL.getIdStatusActo()));

		}

		predioRepository.save(predio);
		System.out.println("PREDIO GUARDADO...\nBORRANDO COLINDANCIAS");
		colindanciaRepository.deleteByPredioId(predio.getId());
		if (colindancias != null && colindancias.length > 0) {
			for (Colindancia col : colindancias) {
				col.setPredio(predio);
				colindanciaRepository.save(col);
				System.out.println("COLINDANCIAS GUARDADAS...");
			}
		}

		// titulares iniciales
		if (predioDTO.getTitulares() != null) {

			PropietarioVO[] titulares = predioDTO.getTitulares();

			List<Acto> actos = actoRepository.findByActoPrediosParaActosPredioId(predio.getId());
			for (Acto acto : actos) {
				actoService.delete(acto.getId());
			}

			Acto acto = new Acto();
			acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
			acto.setVersion(1);
			acto.setPrimerRegistro(true);
			actoRepository.save(acto);

			ActoPredio actoPredio = new ActoPredio();
			actoPredio.setActo(acto);
			actoPredio.setPredio(predio);
			actoPredio.setVersion(1);
			actoPredio.setTipoEntrada(tipoEntradaRepository.findOne(Constantes.TipoEntrada.SALIDA.getIdTipoEntrada()));
			actoPredio.setTipoFolio(tipoFolioRepository.findOne((long) Constantes.ETipoFolio.PREDIO.getTipoFolio()));
			actoPredioRepository.save(actoPredio);

			for (PropietarioVO propietarioVO : titulares) {
				Persona persona = new Persona();
				persona.setTipoPersona(propietarioVO.getTipoPersona());
				persona.setNombre(propietarioVO.getNombre());
				persona.setPaterno(propietarioVO.getPaterno());
				persona.setMaterno(propietarioVO.getMaterno());
				persona.setActivo(true);

				personaRepository.save(persona);

				PredioPersona predioPersona = new PredioPersona();
				predioPersona.setPersona(persona);
				predioPersona.setActoPredio(actoPredio);
				predioPersona.setPorcentajeDd(propietarioVO.getPorcentajeDd());
				predioPersona.setPorcentajeUv(propietarioVO.getPorcentajeUv());
				predioPersona.setTipoRelPersona(
						tipoRelPersonaRepository.findOne(Constantes.TipoRelPersona.PROPIETARIO.getIdTipoRelPersona()));

				predioPersonaRepository.save(predioPersona);
			}
		}
		return predio;
	}

	@Transactional
	public Predio saveTitularesIniciales(Long predioId, List<PropietarioVO> titulares) {
		Predio predio = predioRepository.findOne(predioId);
		List<Acto> actos = actoRepository.findByActoPrediosParaActosPredioId(predio.getId());
		System.out.println(" actos " + actos.size());
		for (Acto acto : actos) {
			System.out.println("eliminando acto " + acto);
			actoService.deleteActo(acto.getId());
		} // fin for

		Acto acto = new Acto();
		acto.setStatusActo(statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo()));
		acto.setVersion(1);
		acto.setPrimerRegistro(true);
		actoRepository.save(acto);

		ActoPredio actoPredio = new ActoPredio();
		actoPredio.setActo(acto);
		actoPredio.setPredio(predio);
		actoPredio.setVersion(1);
		actoPredio.setTipoEntrada(tipoEntradaRepository.findOne(Constantes.TipoEntrada.SALIDA.getIdTipoEntrada()));
		actoPredio.setTipoFolio(tipoFolioRepository.findOne((long) Constantes.ETipoFolio.PREDIO.getTipoFolio()));
		actoPredioRepository.save(actoPredio);

		for (PropietarioVO propietarioVO : titulares) {
			Persona persona = new Persona();
			// persona.setTipoPersona(propietarioVO.getTipoPersona());
			persona.setTipoPersona(propietarioVO.getTipoPersona());
			persona.setNombre(propietarioVO.getNombre());
			persona.setPaterno(propietarioVO.getPaterno());
			persona.setMaterno(propietarioVO.getMaterno());
			persona.setActivo(true);

			personaRepository.save(persona);

			PredioPersona predioPersona = new PredioPersona();
			predioPersona.setPersona(persona);
			predioPersona.setActoPredio(actoPredio);
			predioPersona.setPorcentajeDd(propietarioVO.getPorcentajeDd());
			predioPersona.setPorcentajeUv(propietarioVO.getPorcentajeUv());
			predioPersona.setTipoRelPersona(
					tipoRelPersonaRepository.findOne(Constantes.TipoRelPersona.PROPIETARIO.getIdTipoRelPersona()));

			predioPersonaRepository.save(predioPersona);
		}
		return predio;
	}

	public PredioVO findPredioByAntecedente(Long idAntecedente) {
		PredioVO predioVO = null;
		System.out.println("*********************------------" + predioRepository.findByIdAntecedente(idAntecedente));
		Optional<Predio> predio = Optional.of(predioRepository.findByIdAntecedente(idAntecedente));
		if (predio.isPresent()) {
			predioVO = new PredioVO();
			copyProperties(predio.get(), predioVO);
		}
		return predioVO;
	}

	public List<PredioVO> findPrediosByAntecedente(Long idAntecedente) {
		ArrayList<PredioVO> predioVOs = new ArrayList<>();
		PredioVO predioVO = null;
		List<Predio> predios = predioRepository.findPrediosByIdAntecedente(idAntecedente);
		for (Predio pred : predios) {
			Optional<Predio> predio = Optional.of(pred);
			if (predio.isPresent()) {
				predioVO = new PredioVO();
				copyProperties(predio.get(), predioVO);
			}
			predioVOs.add(predioVO);
		}
		System.out.println("*******************wq**------------" + predioVOs);

		return predioVOs;
	}

	public PredioAnteVO findPredioByFolio(Integer idFolio) {
		PredioAnteVO result = null;
		Predio predio = predioRepository.findByNoFolio(idFolio);

		if (predio != null) {
			PredioVO predioVO = new PredioVO();
			result = new PredioAnteVO();
			copyProperties(predio, predioVO);
			log.debug("Entro hacer la counslita del antecendente ");

			Optional<PredioAnte> predioAnte = predioAnteRepository.findFirstByPredioIdOrderByIdDesc(predio.getId());
			if (predioAnte.isPresent()) {
				result.setAntecedente(AntecedenteVO.antecedenteTransform(predioAnte.get().getAntecedente()));
			}
			result.setPredio(predioVO);
		}

		return result;
	}

	public Optional<PredioAnte> findFirstByPredioIdOrderByIdDesc(Long predioId) {
		return predioAnteRepository.findFirstByPredioIdOrderByIdDesc(predioId);
	}

	public Predio findPredioByNoFolioOficina(Integer noFolio) throws Exception {
		Predio predio = predioRepository.findByNoFolio(noFolio);
		Usuario userLogin = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		if (predio != null) {
			if (!userLogin.getOficina().getId().equals(predio.getOficina().getId())) {
				throw new Exception("El predio no pertenece a la oficina");
			}
		} else {
			throw new Exception("El predio no fue indentificado");
		}

		return predio;
	}

	@Transactional
	public Long createPredio(PredioVO predioVo) {
		Predio predio = null;
		StatusActo statusPredio = new StatusActo();
		PrelacionPredio prelacionPredio = null;
		TipoFolio folio = null;

		if (predioVo.getId() != null) {
			predio = predioRepository.findOne(predioVo.getId());
		} else {
			predio = new Predio();
		}

		copyProperties(predioVo, predio);
		if (predioVo.getId() == null) {
			statusPredio.setId(Constantes.StatusPredio.NO_VIGENTE.getIdStatusPredio());
			predio.setStatusActo(statusPredio);
		}
		predioRepository.save(predio);

		if (findPredioByPrelacion(predioVo.getPrelacion().getId()) == null) {
			prelacionPredio = new PrelacionPredio();
			folio = new TipoFolio();
			folio.setId(Long.valueOf(Constantes.ETipoFolio.PREDIO.idTipoFolio.intValue()));
			prelacionPredio.setPredio(predio);
			prelacionPredio.setTipoFolio(folio);
			prelacionPredio.setPrelacion(predioVo.getPrelacion());
			prelacionPredioRepository.save(prelacionPredio);
		}

		return predio.getId();
	}

	@Transactional
	public Long createFolioPredio(Long idPredio, long oficinaId, boolean validaTengaActoPredioCampos) {
		String message = null;
		Long folio = null;
		Optional<Predio> predio = Optional.of(predioRepository.getOne(idPredio));
		if (predio.isPresent()) {
			message = validacionCreacionFolio(predio.get(), validaTengaActoPredioCampos);
			if (message == null) {
				folio = predioRepository.getFolioFromPredioSequence();
				predio.get().setNoFolio(Integer.valueOf(folio.intValue()));
				predio.get().setOficina(oficinaRepository.findOne(oficinaId));
				predio.get().setBloqueado(false);
				predio.get().setCaratulaActualizada(true);
				/*
				 * StatusActo statusActo = new StatusActo(); statusActo.setId(1L);
				 * predio.get().setStatusActo(statusActo); predioRepository.save(predio.get());
				 */
				predio.get().setStatusActo(statusActoRepository.findOne(1L));
				predioRepository.save(predio.get());

			} else {
				throw new RuntimeException(message);
			}

		}
		return folio;
	}

	public Predio findOne(Long id) {
		return predioRepository.findOne(id);
	}

	public List<Colindancia> findOneColindancias(Long id) {
		return predioRepository.findOneColindancias(id);
	}

	public FoliosFracCond findOneFolioFracCond(Long id) {
		return predioRepository.findOneFolioFracCond(id);
	}

	@Transactional
	public Long finalizaPredioCyvf(PredioVO predioVO) {
		Long idPredio = null;
		Optional<Predio> predio = Optional.of(predioRepository.findOne(predioVO.getId()));
		if (predio.isPresent()) {
			predio.get().setStatusActo(predioVO.getStatusActo());
			predioRepository.save(predio.get());
			idPredio = predio.get().getId();
		}
		return idPredio;
	}

	public void copyProperties(PredioVO predioVo, Predio predio) {
		Set<Colindancia> colindancias = null;
		/*
		 * if(predioVo.getAsentamiento() != null){
		 * predioVo.getVialidad().setMunicipio(predioVo.getAsentamiento().getMunicipio()
		 * ); }
		 */
		try {
			BeanUtils.copyProperties(predio, predioVo);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		if (predioVo.getPredioAntesParaPredio() != null) {
			predioVo.getPredioAntesParaPredio().setPredio(predio);
		}
		/*
		 * if(predioVo.getVialidad() != null && (predioVo.getVialidad().getNombre()==
		 * null || predioVo.getVialidad().getMunicipio() == null ||
		 * predioVo.getVialidad().getMunicipio().getId() == null ||
		 * predioVo.getVialidad().getTipoVialidad() == null)){ predio.setVialidad(null);
		 * }
		 */

		if (predio.getId() != null) {
			/*
			 * if(predioVo.getAsentamiento() != null && predioVo.getAsentamiento().getId()
			 * != null){ predio.setAsentamiento(asentamientoRepository.findOne(predioVo.
			 * getAsentamiento().getId())); if(predioVo.getAsentamiento().getMunicipio() !=
			 * null){
			 * predio.getAsentamiento().setMunicipio(municipioRepository.findOne(predioVo.
			 * getAsentamiento().getMunicipio().getId())); }
			 * if(predioVo.getAsentamiento().getTipoAsent() != null){
			 * predio.getAsentamiento().setTipoAsent(tipoAsentRepository.findOne(predioVo.
			 * getAsentamiento().getTipoAsent().getId()));
			 * predio.getAsentamiento().setNombre(predioVo.getAsentamiento().getNombre()); }
			 * } if(predioVo.getVialidad() != null && predioVo.getVialidad().getId() !=
			 * null){
			 * predio.setVialidad(vialidadRepository.findOne(predioVo.getVialidad().getId())
			 * ); predio.getVialidad().setNombre(predioVo.getVialidad().getNombre()); }
			 * 
			 */

		}

		if (predioVo.getColindancias() != null && predioVo.getColindancias().length > 0) {
			colindancias = new HashSet<>();
			for (Colindancia colindancia : predioVo.getColindancias()) {
				if (colindancia.getNombre() != null && colindancia.getOrientacion() != null) {
					colindancia.setPredio(predio);
					colindancias.add(colindancia);
				}
			}
			predio.setColindanciasParaPredios(colindancias);
		}
	}

	public void copyProperties(Predio predio, PredioVO predioVo) {
		if (predio != null) {
			try {
				BeanUtils.copyProperties(predioVo, predio);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

			if (predio.getColindanciasParaPredios() != null && !predio.getColindanciasParaPredios().isEmpty()) {
				predioVo.setColindancias(predio.getColindanciasParaPredios()
						.toArray(new Colindancia[predio.getColindanciasParaPredios().size()]));
			}
		}
	}

	public List<PrelacionPredio> findByPrelacionId(Long id) {

		return prelacionPredioRepository.findByPrelacionIdAndVersionId(id);

	}

	public List<PrelacionPredio> findByPrelacionIdAndTipoFolioId(Long id, Long tipo) {

		return prelacionPredioRepository.findByPrelacionIdAndTipoFolioId(id, tipo);

	}

	public PredioVO savePredioPrelacion(PredioVO predioVO, Prelacion prelacion) {

		try {
			System.out.println("Predio  : " + predioVO.getId() + " prelacion :  " + prelacion.getId());
			PrelacionPredio prelaPredio = new PrelacionPredio();

			Predio predio = predioRepository.findOne(predioVO.getId());
			prelaPredio.setPredio(predio);

			TipoFolio tipoFolio = tipoFolioRepository.findOne((long) Constantes.ETipoFolio.PREDIO.getTipoFolio());

			prelaPredio.setPrelacion(prelacion);
			prelaPredio.setTipoFolio(tipoFolio);
			prelaPredio.setVersion(1);
			prelaPredio.setEstatus(1);
			prelacionPredioRepository.saveAndFlush(prelaPredio);

			prelaPredio.setRequiereValidacion(turnadorService.isCaratulaActualizada(prelaPredio));
			prelaPredio.setIdVersion(prelaPredio.getId().toString() + prelaPredio.getPrelacion().getId().toString());
			prelacionPredioRepository.saveAndFlush(prelaPredio);
			copyProperties(predio, predioVO);
			predioVO.setIdPrelacionPredio(prelaPredio.getId());
		} catch (Exception e) {
			System.out.println("ocurrio un error");
			e.printStackTrace();
		}

		return predioVO;
	}

	public Predio deletePredioPrelacion(long preladionId, long predioId) {
		Predio predio = null;
		try {
			//
		} catch (Exception ex) {
			//
		}
		return predio;
	}

	public Predio findPredioByNoFolio(Integer numFolio) {
		return predioRepository.findByNoFolio(numFolio);
	}

	public Predio findPredioByNoFolioAnterior(Integer numFolio, Long oficinaId) {
		return predioRepository.findByNumeroFolioRealAndOficinaIdAndAuxiliarIsNull(numFolio, oficinaId);
	}

	public Predio findPredioByNoFolioAnteriorAndAuxiliar(Integer numFolio, Integer auxiliar, Long oficinaId) {
		return predioRepository.findByNumeroFolioRealAndAuxiliarAndOficinaId(numFolio, auxiliar, oficinaId);
	}

	public Optional<PredioAnte> findPredioAnteByPredioId(Long predioId) {
		return predioAnteRepository.findByPredioId(predioId);
	}

	/*
	 * public PredioAnte findPredioAnteByAntecedenteId(Long antecedenteId) { return
	 * predioAnteRepository.findByAntecedenteId(antecedenteId); }
	 */

	public List<Acto> findActosHerencia(PrelacionPredio prelacionPredio) {
		List<Acto> actos = new ArrayList<Acto>();
		int tipoFolio = (int) (long) prelacionPredio.getTipoFolio().getId();

		if (tipoFolio == Constantes.ETipoFolio.PERSONAS_JURIDICAS.idTipoFolio) {
			return actoRepository.findActosHerenciaPJ(prelacionPredio.getPersonaJuridica().getId());
		}
		if (tipoFolio == Constantes.ETipoFolio.PERSONAS_AUXILIARES.idTipoFolio) {
			return actoRepository.findActosHerenciaPA(prelacionPredio.getFolioSeccionAuxiliar().getId());
		}
		if (tipoFolio == Constantes.ETipoFolio.MUEBLE.idTipoFolio) {
			return actoRepository.findActosHerenciaM(prelacionPredio.getMueble().getId());
		}
		if (tipoFolio == Constantes.ETipoFolio.PREDIO.idTipoFolio) {
			return actoRepository.findActosHerenciaP(prelacionPredio.getPredio().getId());
		}
		return actos;

	}

	public PredioVO findPredioByByNoFolio(Integer noFolio) {
		PredioVO predioVO = null;
		Optional<Predio> predio = Optional.of(predioRepository.findByNoFolio(noFolio));
		if (predio.isPresent()) {
			predioVO = new PredioVO();
			copyProperties(predio.get(), predioVO);
		}
		return predioVO;
	}

	private String validacionCreacionFolio(Predio predio, boolean validaTengaActoPredioCampos) {
		String message = null;

		if (validaTengaActoPredioCampos) {
			if (predio.getActoPrediosParaPredios() == null || predio.getActoPrediosParaPredios().isEmpty()) {
				message = "El predio no tiene actos capturados";
			}
			if (message == null) {
				if (validaTengaActoPredioCampos) {
					for (ActoPredio actoPredio : predio.getActoPrediosParaPredios()) {
						if (actoPredio.getActo().getTipoActo() != null && actoPredio.getTipoEntrada().getId() == 2L
								&& (actoPredio.getActoPredioCamposParaActoPredios() == null
										|| actoPredio.getActoPredioCamposParaActoPredios().isEmpty())) {
							message = "El predio no tiene acto predio campo capturados";
							break;
						}
					}
				}
				/*
				 * if(message ==null){ for(ActoPredio
				 * actoPredio:predio.getActoPrediosParaPredios()){
				 * if(actoPredio.getActo().getActoDocumentosParaActos() == null ||
				 * actoPredio.getActo().getActoDocumentosParaActos().isEmpty()){ message =
				 * "El predio tiene actos sin documentos"; break; } } } if(message ==null){
				 * for(ActoPredio actoPredio:predio.getActoPrediosParaPredios()){
				 * if(actoPredio.getActo().getActoFirmasParaActos() == null ||
				 * actoPredio.getActo().getActoFirmasParaActos().isEmpty()){ message =
				 * "El predio tiene actos sin firmar"; break; } } }
				 */
			}
		}
		return message;
	}

	@Transactional(readOnly = true)
	public Page<Predio> findAllPageable(Integer noFolio, Pageable pageable) {
		Usuario u;
		try {
			u = this.usuarioService.getLoguedUser();
			Oficina o = u.getOficina();
			return predioRepository.findAllPageable(noFolio, pageable,o.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	/// Predios-Fraccionamiento
	@Transactional(readOnly = true)
	public Page<Predio> findAllPrediosFraccionamientoPageable(Long actoId, Integer noFolio, String manzana, String lote,
			Pageable pageable) {
		return predioRepository.findAllPrediosFraccionamientoPageable(actoId, noFolio, manzana, lote, pageable);
	}
	
	@Transactional(readOnly = true)
	public Page<Predio> findAllPrediosFraccionamientoPageable(Long paseId, String manzana, String lote,
			Pageable pageable) {
		return predioRepository.findAllPrediosFraccionamientoPageable(paseId, manzana, lote, pageable);
	}

	public Double findTotalSuperficie(Long actoId) {
		return predioRepository.findTotalSuperficie(actoId);
	}

	public Double findTotalIndivisos(Long actoId) {
		return predioRepository.findTotalIndivisos(actoId);
	}

	@Transactional
	public Predio savePredioFracCond(PredioDTO predioDTO, Long actoId) {
		Predio predio = null;
		FoliosFracCond folioFracCond = null;

		if (predioDTO.getId() != null) {
			predio = predioRepository.findOne(predioDTO.getId());
		} else {
			predio = new Predio();
		}

		predio.setTipoInmueble(predioDTO.getTipoInmueble());
		predio.setNumInt(predioDTO.getNumInt());
		predio.setNivel(predioDTO.getNivel());
		predio.setEdificio(predioDTO.getEdificio());
		predio.setNoLote(predioDTO.getNoLote());
		predio.setLocalidadSector(predioDTO.getLocalidadSector());
		predio.setFraccion(predioDTO.getFraccion());
		predio.setManzana(predioDTO.getManzana());
		predio.setMacroManzana(predioDTO.getMacroManzana());
		
		predio.setTipoVialidad(predioDTO.getTipoVialidad());
		predio.setVialidad(predioDTO.getVialidad());
		predio.setNumExt(predioDTO.getNumExt());
		predio.setEnlaceVialidad(predioDTO.getEnlaceVialidad());
		predio.setTipoVialidad2(predioDTO.getTipoVialidad2());
		predio.setVialidad2(predioDTO.getVialidad2());
		predio.setNumExt2(predioDTO.getNumExt2());

		predio.setTipoAsent(predioDTO.getTipoAsent());
		predio.setAsentamiento(predioDTO.getAsentamiento());
		predio.setFracOCondo(predioDTO.getFracOCondo());
		predio.setNombreFracOCondo(predioDTO.getNombreFracOCondo());
		predio.setEtapaOSeccion(predioDTO.getEtapaOSeccion());

		predio.setZona(predioDTO.getZona());
		predio.setClaveCatastral(predioDTO.getClaveCatastral());
		predio.setCuentaCatastral(predioDTO.getCuentaCatastral());
		predio.setClaveCatastralEstandard(predioDTO.getClaveCatastralEstandard());

		predio.setMunicipio(predioDTO.getMunicipio());
		predio.setCodigoPostal(predioDTO.getCodigoPostal());
		predio.setSuperficie(predioDTO.getSuperficie());
		predio.setSuperficieM2(predioDTO.getSuperficieM2());
		predio.setUnidadMedida(predioDTO.getUnidadMedida());
		predio.setUsoSuelo(predioDTO.getUsoSuelo());
		predio.setNoFolio(null);

		Colindancia[] colindancias = predioDTO.getColindancias();

		if (predio.getId() == null) {
			predio.setStatusActo(statusActoRepository.findOne(2l));
			folioFracCond = new FoliosFracCond();
			folioFracCond.setActo(actoRepository.findOne(actoId));
			folioFracCond.setPredio(predio);
			folioFracCond.setProcedente(true);
			folioFracCond.setNoPredio(predioDTO.getNoFolio());
		}

		predioRepository.save(predio);

		colindanciaRepository.deleteByPredioId(predio.getId());
		for (Colindancia col : colindancias) {
			col.setPredio(predio);
			colindanciaRepository.save(col);
		}

		if (folioFracCond != null) {
			foliosFracCondRepository.save(folioFracCond);
		}

		return predio;
	}

	@Transactional
	public Long deletePrediosFracCond(Long actoId) {
		Long deleted = 0l;
		List<FoliosFracCond> predios = foliosFracCondRepository.findByIdActo(actoId);
		deleted = deleteFracCond(predios);
		return deleted;
	}
	
	
	@Transactional
	public Long deleteFracCond(List<FoliosFracCond> predios) {
		Long deleted = 0l;
		for (FoliosFracCond pre : predios) {
			List<PredioBitacora> pbList = predioBitacoraRepository.findByPredio(pre.getPredio().getId());
			log.info("pbList" + pbList.size());
			for (PredioBitacora pb : pbList) {
				Set<PredioBitacoraColindancia> colindancias = pb.getColindancias();
				predioBitacoraColindanciaRepository.delete(colindancias);
				predioBitacoraRepository.delete(pb);
			}
			colindanciaRepository.deleteByPredioId(pre.getPredio().getId());
			foliosFracCondRepository.deleteByPredioId(pre.getPredio().getId());
			predioRepository.delete(pre.getPredio());
			deleted++;
		}

		log.debug("TOTAL PREDIO ELIMINADOS " + deleted);
		return deleted;
	}

	@Transactional
	public Long changeProcedentePrediosFracCond(Long folioFracCondId) {
		FoliosFracCond folio = foliosFracCondRepository.findById(folioFracCondId);

		if (folio != null) {
			folio.setProcedente(!folio.getProcedente());
			foliosFracCondRepository.save(folio);
		}

		return 1l;
	}
	/// FIN Predios-Fraccionamiento

	public PredioVO findPredioByPrelacion(Long idPrelacion) {
		PredioVO predioVO = null;
		List<Predio> lstPredios = predioRepository.findByPrelacionPrediosParaPrediosPrelacionId(idPrelacion);
		if (lstPredios != null && !lstPredios.isEmpty()) {
			predioVO = new PredioVO();
			copyProperties(lstPredios.get(0), predioVO);
		}
		return predioVO;
	}

	public Predio findPredioByFolioBloqueo(Integer folio) {
		Predio predio = null;
		List<Predio> lstPredios = predioRepository.findPredioByFolio(folio);
		if (lstPredios != null && !lstPredios.isEmpty()) {
			predio = lstPredios.get(0);
		}
		return predio;
	}

	public Predio saveAndUpdate(Predio predio) {
		predioRepository.saveAndFlush(predio);
		return predio;
	}

	@NotNull
	public List<ColindanciaVO> getColindanciaVOFromPredioId(Long predioId) {
		List<ColindanciaVO> linderos = new ArrayList<>();

		List<Colindancia> cols = this.findOneColindancias(predioId);

		if (cols != null) {
			linderos = cols.stream().map(ColindanciaVO::converToColindanciaVO).collect(Collectors.toList());
		}

		return linderos;

	}

	public List<PredioBitacora> findPredioBitacoraByPredioID(Long id){
		List<PredioBitacora> pb=predioBitacoraRepository.findAllByPredioId(id);
		System.out.println("predioBita{} "+pb);
		return pb;
	}
	public List<PredioBitacoraColindancia>findColindanciasByPredioBitacoraId(Long id){
		List<PredioBitacoraColindancia> pbcs= pBitacoraColindanciaRepositoy.findByPredioBitacoraId(id);
		System.out.println("predioBitaColind{} "+pbcs);
		return pbcs;
	}
	public Long findNumFolio(Long prelacionId) {
		Long numFolio = prelacionPredioRepository.findPredioIdByPrelacionId(prelacionId);
		 return  numFolio;
	 }
	
	public List<PredioAnte> validaFoliosMasivo(MultipartFile file, Long tipo ){

		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		
		log.debug(extension);
		
		List<FolioDTO> folios = new ArrayList<FolioDTO>();
		
		switch (extension) {
		case "xlsx":
			log.debug("entro xlsx");
			folios = getFoliosMasivosXLSX(file);
			HashSet<FolioDTO> hashSet = new HashSet<FolioDTO>(folios);
			folios.clear();
			folios.addAll(hashSet);
			//se comenta debido a que cambia la estructura del listado folios
			//Collections.sort(folios);
			break;
		case "xls":
			log.debug("entro xls");
			folios = getFoliosMasivosXLS(file);
			HashSet<FolioDTO> hashSet2 = new HashSet<FolioDTO>(folios);
			folios.clear();
			folios.addAll(hashSet2);
			//se comenta debido a que cambia la estructura del listado folios
			//Collections.sort(folios);
			break;
		
		}
		

		
		List<PredioAnte> listPredioAnte = new ArrayList<PredioAnte>();
		
		String foliosNoEncontrados = "";
		//System.out.println("Folios longitud : " + folios.size());
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		for (FolioDTO folio : folios) {
			Predio predio = null;
			if(folio.getNoFolioReal()!=null) {
				if(folio.getAuxiliar()==0) {
					predio = findByNoFolioRealAndOficina(folio.getNoFolioReal(), usuario.getOficina());
				}else {
				    predio = findPredioByNoFolioRealAndAuxiliarAndOficina(folio.getNoFolioReal(), folio.getAuxiliar(), usuario.getOficina());
				}
			}else 
			{
						predio= findByNoFolioAndOficina(folio.getNoFolio(), usuario.getOficina());
			}
			PredioAnte prea = null;
			if (predio != null) {			
							try 
							{
								prea = findPredioAnteByPredioId(predio.getId()).orElse(null) ;	
							}catch(Exception e) {
							   prea = null;	
							}
							
							
							if (prea != null ) {
								listPredioAnte.add(prea);
							} else {
								Antecedente antecedente = new Antecedente();
								PredioAnte predioAnte = new PredioAnte();
								predioAnte.setAntecedente( antecedente );
								predioAnte.setPredio( predio );
								listPredioAnte.add(predioAnte);
								//predioAnte
								
							}
							
							
							log.debug("Folio validado " + folio.getNoFolio());
						} else {
							log.debug("No se encontro el folio " + folio.getNoFolio());
							if(folio.getNoFolio() !=null) {
								  foliosNoEncontrados = foliosNoEncontrados + "," + folio.getNoFolio();
							}else if(folio.getNoFolioReal()!=null)
							  foliosNoEncontrados = foliosNoEncontrados + "," + folio.getNoFolioReal();
							
						}
		}
		
		if(listPredioAnte.size() > 0)
		{
			System.out.println("predioAnte");
			listPredioAnte.get(0).setFoliosNoEncontradosMasiva(foliosNoEncontrados);
		}
		
				
		return listPredioAnte;
		
	}
	
	
	
	private List<FolioDTO> getFoliosMasivosXLSX(MultipartFile file_mul) {
		
		List<FolioDTO> folios = new ArrayList<FolioDTO>();
		
		
		try (InputStream file = (InputStream) file_mul.getInputStream()) {
			// leer archivo excel
			XSSFWorkbook worbook = new XSSFWorkbook(file);
			//obtener la hoja que se va leer
			XSSFSheet sheet = worbook.getSheetAt(0);
			//obtener todas las filas de la hoja excel
			Iterator<Row> rowIterator = sheet.iterator();
 
			Row row;
			// se recorre cada fila hasta el final
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				System.out.println("ROW EXCEL: "+row.getRowNum());
				
				//se obtiene las celdas por fila
				Iterator<Cell> cellIterator = row.cellIterator();
				Cell cell;
				//se recorre cada celda
				while (cellIterator.hasNext()) {
					// se obtiene la celda en específico y se la imprime
					cell = cellIterator.next();
					
					FolioDTO folDTO = new FolioDTO();
					folDTO = generarFolioDTO(cell);
					if(folDTO.getFol()!=null)
					folios.add(folDTO);
					
					/*if (cell.getCellType() == 0) {
						//System.out.println("valores en celda : " + cell.getNumericCellValue());
						folios.add( (int) cell.getNumericCellValue());
					}*/
				}
				
			}
			//log.debug(folios);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return folios;
	
	}

	private List<FolioDTO> getFoliosMasivosXLS(MultipartFile file_mul) {
		
		//String nombreArchivo = "Folios.xlsx";
		//String rutaArchivo = "C:\\Users\\Hai lyn\\Desktop\\Jalisco\\Captura Masiva\\" + nombreArchivo;
		List<FolioDTO> folios = new ArrayList<FolioDTO>();
		
		try (InputStream file = (InputStream) file_mul.getInputStream()) {
			// leer archivo excel
			HSSFWorkbook worbook = new HSSFWorkbook(file);
			//obtener la hoja que se va leer
			HSSFSheet sheet = worbook.getSheetAt(0);
			//obtener todas las filas de la hoja excel
			Iterator<Row> rowIterator = sheet.iterator();
 
			Row row;
			// se recorre cada fila hasta el final
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				System.out.println("ROW EXCEL: "+row.getRowNum());
				//se obtiene las celdas por fila
				Iterator<Cell> cellIterator = row.cellIterator();
				Cell cell;
				//se recorre cada celda
				while (cellIterator.hasNext()) {
					// se obtiene la celda en específico y se la imprime
					cell = cellIterator.next();
					
					FolioDTO folDTO = new FolioDTO();
					
					folDTO = generarFolioDTO(cell);
					if(folDTO.getFol()!=null)
					folios.add(folDTO);
					/*
					 if (cell.getCellType() == 0) {
						folios.add( (int) cell.getNumericCellValue());
					}
					 */

				}
				
			}
			//log.debug(folios);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return folios;
	
	}
	
	public Predio findPredioByNoFolioRealAndAuxiliarAndOficina(Integer numFolioReal, Integer auxiliar, Oficina oficina) {
		return predioRepository.findByNoFolioRealAndAuxiliarAndOficina(numFolioReal,auxiliar,oficina);
	}
	
	public Predio findPredioByNoFolioRealAndOficina(Integer numFolioReal, Oficina oficina) {
		return predioRepository.findByNoFolioRealAndOficina(numFolioReal,oficina);
	}
	public FolioDTO generarFolioDTO(Cell cell) {
		String data=null;
		if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
		   data =  String.valueOf(cell.getNumericCellValue());
		if(cell.getCellType() == Cell.CELL_TYPE_STRING)
			data =  cell.getStringCellValue();
		
		FolioDTO folDTO = new FolioDTO();
		
		if(data!=null && !data.trim().isEmpty()) {
			folDTO.setFol(data);
			String folioMigrado = data;
			String [] datos = folioMigrado.split("-");
			if(datos.length==2) {
				folDTO.setNoFolioReal(Integer.parseInt(datos[0]));
				folDTO.setAuxiliar(Integer.parseInt(datos[1]));
			System.out.println("FOLIO REAL : " + folDTO.getNoFolioReal());
			}else {
				folDTO.setNoFolio(Integer.parseInt(data));
			}
		}else {
			folDTO = null;
		}
	
		return folDTO;
	}
	
	public Predio findByNoFolioAndOficina(Integer numFolio, Oficina oficina) {
		return predioRepository.findByNoFolioAndOficina(numFolio, oficina);
	}
	
	public Predio findByNoFolioRealAndOficina(Integer numFolio, Oficina oficina) {
		return predioRepository.findByNoFolioRealAndOficina(numFolio, oficina);
	}
	

	@Transactional
	public Predio replicarPredio(Long predioId, Long paseId) throws IllegalAccessException, InvocationTargetException {
		Predio predioSource = this.findOne(predioId);

		PredioDTO predio = new PredioDTO();

		predio.setTipoInmueble(predioSource.getTipoInmueble());
		predio.setNumInt(predioSource.getNumInt());
		predio.setNivel(predioSource.getNivel());
		predio.setEdificio(predioSource.getEdificio());
		predio.setNoLote(predioSource.getNoLote());
		predio.setFraccion(predioSource.getFraccion());
		predio.setManzana(predioSource.getManzana());
		predio.setMacroManzana(predioSource.getMacroManzana());
		predio.setLocalidadSector(predioSource.getLocalidadSector());
		predio.setTipoVialidad(predioSource.getTipoVialidad());
		predio.setVialidad(predioSource.getVialidad());
		predio.setNumExt(predioSource.getNumExt());
		predio.setEnlaceVialidad(predioSource.getEnlaceVialidad());
		predio.setTipoVialidad2(predioSource.getTipoVialidad2());
		predio.setVialidad2(predioSource.getVialidad2());
		predio.setNumExt2(predioSource.getNumExt2());

		predio.setTipoAsent(predioSource.getTipoAsent());
		predio.setAsentamiento(predioSource.getAsentamiento());
		predio.setFracOCondo(predioSource.getFracOCondo());
		predio.setNombreFracOCondo(predioSource.getNombreFracOCondo());
		predio.setEtapaOSeccion(predioSource.getEtapaOSeccion());

		predio.setZona(predioSource.getZona());
		predio.setClaveCatastral(predioSource.getClaveCatastral());
		predio.setCuentaCatastral(predioSource.getCuentaCatastral());
		predio.setClaveCatastralEstandard(predioSource.getClaveCatastralEstandard());

		predio.setMunicipio(predioSource.getMunicipio());
		predio.setCodigoPostal(predioSource.getCodigoPostal());
		predio.setSuperficie(predioSource.getSuperficie());
		predio.setSuperficieM2(predioSource.getSuperficieM2());
		predio.setUnidadMedida(predioSource.getUnidadMedida());
		predio.setUsoSuelo(predioSource.getUsoSuelo());
		predio.setHeredaActo(predioSource.getHeredaActo());
		Set<Colindancia> colindancias = predioSource.getColindanciasParaPredios();

		if (colindancias != null && colindancias.size() > 0) {
			Colindancia[] _colindancias = new Colindancia[colindancias.size()];
			int i = 0;
			List<Colindancia> colindanciasOrder = colindancias.stream().sorted(Comparator.comparingLong(Colindancia::getId)).collect(Collectors.toList());
			for (Colindancia col : colindanciasOrder) {
				Colindancia cld =  new Colindancia();
				cld.setNombre(col.getNombre());
				cld.setOrientacion(col.getOrientacion());
				cld.setTipoColin(col.getTipoColin());
				_colindancias[i] =  cld;
				i++;
			}

			predio.setColindancias(_colindancias);
		}

		Predio _predio = this.savePredio(predio);

		FoliosFracCond folioFrac = new FoliosFracCond();
		folioFrac.setPredio(_predio);
		folioFrac.setPaseFracCond(paseFracCondService.findPase(paseId));
		paseFracCondService.saveFolioFracCond(folioFrac);

		return _predio;
	}
	


	public Predio updateStatusExtinto(Long predioId, String fundamento, boolean extinguir) {
		Predio newPredio = null;
		PredioExtinto extinto = new PredioExtinto();
		Predio pre = predioRepository.findById(predioId);
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		StatusActo statusPredio = new  StatusActo();
		if(pre == null) {
			return pre;
		}else {
			if(extinguir) {
				statusPredio = statusActoRepository.findOne(Constantes.StatusActo.INVALIDO.getIdStatusActo());
			}else {
				statusPredio = statusActoRepository.findOne(Constantes.StatusActo.ACTIVO.getIdStatusActo());
			}
			
			pre.setStatusActo(statusPredio);
			predioRepository.saveAndFlush(pre);
			
			extinto.setPredio(pre);
			extinto.setFundamento(fundamento);
			extinto.setUsuario(usuario);
			extinto.setFecha(new Date());
			predioExtintoRepository.save(extinto);
			newPredio= pre;
			
		}
		return newPredio;
	}
	
	public Boolean existAvisosVigentes(Long predioId) {
		List<ActoPredio> vigentes = actoPredioRepository.findAvisosVigentes(predioId);
		return vigentes != null && vigentes.size()>0;
	}
	
	public FoliosAnteVO busquedaFolios(Integer idFolio, Integer tipo, Long oficinaId) {
		FoliosAnteVO foliosAnte = new FoliosAnteVO();
		foliosAnte.setValores(false);
		PersonaJuridicaAnteVO
		
		pjVO = null;
		FolioSeccionAuxiliarAnteVO fsaVO = null;
		MuebleAnteVO mVO = null;
		PredioAnteVO pVO = null;
		Long tipol = Long.valueOf(tipo.longValue());

		if((int)(long)tipol == Constantes.ETipoFolio.PERSONAS_JURIDICAS.idTipoFolio){
			pjVO =  personaJuridicaService.findPersonaJuridicaByFolio(idFolio, oficinaId);
			if(pjVO != null) {
				foliosAnte.setAntecedente(pjVO.getAntecedente());
				foliosAnte.setPersonaJuridica(pjVO.getPersonaJuridica());
				foliosAnte.setValores(true);
			}
		}
		if((int)(long)tipol == Constantes.ETipoFolio.PERSONAS_AUXILIARES.idTipoFolio){
			fsaVO = folioSeccionAuxiliarService.findFolioSeccionAuxiliarByFolio(idFolio, oficinaId);
			if(fsaVO != null) {
				foliosAnte.setAntecedente(fsaVO.getAntecedente());
				foliosAnte.setFolioSeccionAuxiliar(fsaVO.getFolioSeccionAuxiliar());
				foliosAnte.setValores(true);
			}
		}
		if((int)(long)tipol == Constantes.ETipoFolio.MUEBLE.idTipoFolio){
			mVO = muebleService.findMuebleByFolio(idFolio);
			if(mVO != null) {
				foliosAnte.setAntecedente(mVO.getAntecedente());
				foliosAnte.setMueble(mVO.getMueble());
				foliosAnte.setValores(true);
			}

		}
		if((int)(long)tipol == Constantes.ETipoFolio.PREDIO.idTipoFolio){
			pVO = this.findPredioByFolio(idFolio);
			if(pVO != null) {
				foliosAnte.setAntecedente(pVO.getAntecedente());
				foliosAnte.setPredio(pVO.getPredio());
				foliosAnte.setValores(true);
			}

		}
		return foliosAnte;
	}
	
	@Transactional
	public Boolean heredarActos(List<Acto> actos,Long predioTargetId) {
		Predio predio = this.predioRepository.findOne(predioTargetId);
		Usuario usuario =  this.usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		for(Acto item: actos) {
			HashMap<Integer, ActoPorcentajeVO> actosSeleccionados =  new HashMap<>();
			Acto acto  = this.actoRepository.findOne(item.getId());
			Optional<ActoPredio> actoPredio =  this.actoPredioRepository.findFirstByActoIdOrderByVersionDesc(acto.getId());
			ActoPorcentajeVO _acto =  new ActoPorcentajeVO();
			_acto.actoPredioMonto = new ActoPredioMonto();
			_acto.actoPredioMonto.setActo(acto);
			_acto.actoSeleccionado = true;
			actosSeleccionados.put(0,_acto);
			materializacionActoModificatorioService.saveActosSeleccionados(actosSeleccionados,acto.getPrelacion(),predio, null);
			bitacoraMantenimientoService.create(usuario,"HEREDA ACTO ("+acto.getId()+")-" + acto.getTipoActo().getNombre() + " DEL FOLIO "+actoPredio.get().getPredio().getNoFolio(),"HEREDA_ACTO",predio);
		}
		return true;
	}
	
	public List<PredioExtinto> findPredioExtinto(Long predioId) {
		return  predioExtintoRepository.findByPredioIdOrderByFechaDesc(predioId);
	}
}
