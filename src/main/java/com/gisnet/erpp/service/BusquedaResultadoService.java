package com.gisnet.erpp.service;

import com.gisnet.erpp.domain.*;
import com.gisnet.erpp.repository.*;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.CopiaActoPublicitarioVO;
import com.gisnet.erpp.vo.Copias.CopiaVO;
import com.gisnet.erpp.web.api.foliosrdig.LibroDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gisnet.erpp.vo.Copias.CopiaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class BusquedaResultadoService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BusquedaResultadoRepository  busquedaResultadoRepository;
    @Autowired
    private PrelacionService             prelacionService;

    @Autowired
    private PrelacionRepository prelacionRepository;

    @Autowired
    private PredioRepository predioRepository;

    @Autowired
    private MuebleRepository muebleRepository;

    @Autowired
    private FolioSeccionAuxiliarRepository folioSeccionAuxiliarRepository;

    @Autowired
    private PersonaJuridicaRepository personaJuridicaRepository;
    
    @Autowired
    private LibroRepository libroRepository;
    
    @Autowired
    private BusquedaRepository busquedaRepository;

    @Autowired 
	private BusquedaResultadoActoRepository busquedaResultadoActoRepository;
    
    @Autowired
    private ActoService actoService;
    
    public BusquedaResultado guardar (BusquedaResultado busqueda) {

       busquedaResultadoRepository.save(busqueda);

        return busqueda;

    }

    public Set<BusquedaResultado> guardar(List<Long> predios, Long prelacionId) {

        Set <BusquedaResultado> bResults = new HashSet<>();
        Prelacion prelacion = prelacionRepository.findOne(prelacionId);
        if (!isEmpty(predios) ) {
            for (Long predio : predios ) {
                final Predio predioTemp = predioRepository.findOne(predio);

                //log.debug("Guardando predio... {}", predioTemp);
                //log.debug(predioTemp);
                final BusquedaResultado brTemp = new BusquedaResultado();
                brTemp.setPredio( predioTemp);
                brTemp.setPrelacion(prelacion);
                brTemp.setObservaciones("");
                /*{{
                   setPrelacion(prelacion);
                   setPredio(predioTemp);
                   setObservaciones("");
                }};  */

                log.debug("Guardando Busqueda... {}", brTemp);

                this.guardar(brTemp);
                bResults.add(brTemp);
            }
        }

        return bResults;

    }


    public BusquedaResultado guardar(Long prelacionId, LibroDTO copia) {
        final BusquedaResultado busquedaResultado = new BusquedaResultado();
        BusquedaResultado busquedaResultadoSave = new BusquedaResultado();

        Libro libro =libroRepository.findOne(copia.getLibroId());
        Prelacion prelacion = prelacionRepository.findOne(prelacionId);
        
        busquedaResultado.setPrelacion(prelacion);
        busquedaResultado.setDocumento(copia.getDocumento());
        busquedaResultado.setLibro(libro);

        log.debug("Guardando Busqueda... {}", busquedaResultado);
        busquedaResultadoSave=this.guardar(busquedaResultado);

        return busquedaResultadoSave;

    }
    
    public BusquedaResultado guardarUpdate(Long prelacionId, Long copiaId, LibroDTO copia) {
        final BusquedaResultado busquedaResultado = new BusquedaResultado();
        BusquedaResultado busquedaResultadoSave = new BusquedaResultado();
        BusquedaResultado busquedaResultadoEncontrado = new BusquedaResultado();
        Libro libro =libroRepository.findOne(copia.getLibroId());
        Prelacion prelacion = prelacionRepository.findOne(prelacionId);
        
        busquedaResultadoEncontrado= busquedaResultadoRepository.findOneById(copiaId);
        if(busquedaResultadoEncontrado != null) {
        	busquedaResultadoEncontrado.setPrelacion(prelacion);
        	busquedaResultadoEncontrado.setDocumento(copia.getDocumento());
        	busquedaResultadoEncontrado.setLibro(libro);
        	log.debug("Actualizando Busqueda... {}", busquedaResultado);
        	busquedaResultadoSave = busquedaResultadoRepository.saveAndFlush(busquedaResultadoEncontrado);
        }else {
        	busquedaResultado.setPrelacion(prelacion);
            busquedaResultado.setDocumento(copia.getDocumento());
            busquedaResultado.setLibro(libro);
            log.debug("Guardando Busqueda... {}", busquedaResultado);
            busquedaResultadoSave = this.guardar(busquedaResultado);
        }

        return busquedaResultadoSave;

    }


    public HashSet <BusquedaResultado> getPrediosFromPrelacionId (Long prelacionId) {

        return busquedaResultadoRepository.findAllByPrelacionId (prelacionId);
    }


    public Set <BusquedaResultadoTipoActo> getActosFromBusquedaId (Long busquedaId) {

        BusquedaResultado bus= busquedaResultadoRepository.findOneById(busquedaId);
        if(bus!=null){

            return   bus.getListaTiposActo();
        }
        return  null;
    }

    public Set<BusquedaResultado>  guardarCopia(List<FoliosrDigital> copias, long prelacionId) {
        Set <BusquedaResultado> bResults = new HashSet<>();

        Prelacion prelacion = prelacionRepository.findOne(prelacionId);


        for (FoliosrDigital copia : copias) {
            BusquedaResultado res = new BusquedaResultado();
            res.setPrelacion(prelacion);
            res.setFolio(copia.getNumFolioRegistral());
            //List<TipoActo> tipoActos = copias.stream().map(FoliosrDigital::getTipoActo).collect(Collectors.toList());
            res.setListaTiposActo( null );
            res.setEsImpresion(false);
            res.setFoliosrDigital(copia);

            //res = this.asignarTipoFolio (res, copia);

            res.setEscritura(copia.getEscritura());
            res.setObservaciones(copia.getObservaciones());

            busquedaResultadoRepository.saveAndFlush(res);

        }



        return bResults;
    }

    public Set<BusquedaResultado>  guardarBusquedaImpresion(List<CopiaVO> copias, long prelacionId) {
        Set <BusquedaResultado> bResults = new HashSet<>();

        Prelacion prelacion = prelacionRepository.findOne(prelacionId);


        for (CopiaVO copia : copias) {
            BusquedaResultado res = new BusquedaResultado();
            res.setPrelacion(prelacion);
            res.setFolio(copia.getFolioReal());
            //List<TipoActo> tipoActos = copias.stream().map(FoliosrDigital::getTipoActo).collect(Collectors.toList());
           // res.setListaTiposActo( copias.stream().map(CopiaVO::getActos).collect(Collectors.toSet()));
            res.setEsImpresion(true);
            res.setFoliosrDigital(null);

            res = this.asignarTipoFolio (res, copia.getTipoFolio());

            res.setEscritura(copia.getEscritura());
            res.setObservaciones("Busqueda para impresión");

            busquedaResultadoRepository.saveAndFlush(res);
            bResults.add(res);

        }



        return bResults;
    }

    public BusquedaResultado  guardarBusquedaImpresionFromAntecedente(BusquedaResultado br, long prelacionId) {

        Prelacion prelacion = prelacionRepository.findOne(prelacionId);



            BusquedaResultado res = new BusquedaResultado();
            res.setPrelacion(prelacion);
            res.setFolio(br.getFolio());

            res.setEsImpresion(true);
            res.setFoliosrDigital(null);

            res = this.getTipoFolioFromAntecedente (res, br);

            res.setEscritura(null);
            res.setObservaciones("Busqueda para impresión - Antecedente");

            busquedaResultadoRepository.saveAndFlush(res);

        return res;
    }


    private BusquedaResultado asignarTipoFolio(BusquedaResultado res, TipoFolio tipo) {
        Constantes.ETipoFolio etipo = Constantes.ETipoFolio.fromLong(tipo.getId().longValue());

        assert etipo != null;
        switch (etipo) {
            case PREDIO:
                Predio tmpPredio = predioRepository.findByNoFolio(res.getFolio());
                res.setPredio(tmpPredio);
                break;
            case MUEBLE:
                Mueble tmpMueble = muebleRepository.findByNoFolio(res.getFolio());
                res.setMueble(tmpMueble);
                break;
            case PERSONAS_JURIDICAS:
                PersonaJuridica tmpJuridica = personaJuridicaRepository.findByNoFolio(res.getFolio());
                res.setPersonaJuridica(tmpJuridica);
                break;
            case PERSONAS_AUXILIARES:
                FolioSeccionAuxiliar tmpFolioSAuxiliar = folioSeccionAuxiliarRepository.findOneByNoFolio(res.getFolio());
                res.setFolioSeccionAuxiliar(tmpFolioSAuxiliar);
                break;
        }

        return res;
    }

    private BusquedaResultado getTipoFolioFromAntecedente(BusquedaResultado res, BusquedaResultado br) {

        Constantes.ETipoFolio etipo = null;


        if (!isEmpty( br.getPredio()) )
            etipo = Constantes.ETipoFolio.PREDIO;
        if (!isEmpty( br.getFolioSeccionAuxiliar()) )
            etipo = Constantes.ETipoFolio.PERSONAS_AUXILIARES;
        if (!isEmpty( br.getPersonaJuridica()) )
            etipo = Constantes.ETipoFolio.PERSONAS_JURIDICAS;
        if (!isEmpty( br.getMueble()) )
            etipo = Constantes.ETipoFolio.MUEBLE;


        switch (etipo) {
            case PREDIO:
                res.setPredio(br.getPredio());
                break;
            case MUEBLE:
                res.setMueble(br.getMueble());
                break;
            case PERSONAS_JURIDICAS:
                res.setPersonaJuridica(br.getPersonaJuridica());
                break;
            case PERSONAS_AUXILIARES:
                res.setFolioSeccionAuxiliar(br.getFolioSeccionAuxiliar());
                break;
        }

        return res;
    }


    public BusquedaResultado removeCopiaItem(Long copiaId, Long prelacionId) {
        BusquedaResultado tmp = busquedaResultadoRepository.findOne(copiaId);
        busquedaResultadoRepository.delete(copiaId);
        return tmp;
    }

    @Transactional
    public Boolean clearBusquedaFromPrelacion (Long prelacionId) throws Exception {

    	try {
			Set<BusquedaResultado> busquedaResultados = busquedaResultadoRepository.findAllByPrelacionId(prelacionId);
			if (!isEmpty(busquedaResultados)) {

				busquedaResultados.forEach(x->{
					busquedaResultadoActoRepository.deleteByBusquedaResultadoId(x.getId());
					busquedaResultadoRepository.delete(x.getId());
				});
				
				return true;
			}
			return false;

		} catch (Exception except) {
			throw new Exception("Error al tratar de eliminar los registros. " + except.getMessage());
		}
    }
    
    public  List<BusquedaResultado> findAllBusquedResultadoByPrelacionIdAndPrelacionHistorica (Long prelacionId, Long prelacionHistorica) {
    	System.out.println("BusquedaResultado : "+ busquedaResultadoRepository.findAllBusquedResultadoByPrelacionIdAndPrelacionHistoricaId(prelacionId, prelacionHistorica).size());
    	return busquedaResultadoRepository.findAllBusquedResultadoByPrelacionIdAndPrelacionHistoricaId(prelacionId, prelacionHistorica);
    	
    }
    public  List<BusquedaResultado> findAllByPrelacionIdAndPrelacionHistoricaAndActoPublicitarioId (Long prelacionId, Long prelacionHistorica,Long apId) {
    	System.out.println("BusquedaResultado : "+ busquedaResultadoRepository.findAllByPrelacionIdAndPrelacionHistoricaIdAndActoPublicitarioId(prelacionId, prelacionHistorica,apId).size());
    	return busquedaResultadoRepository.findAllByPrelacionIdAndPrelacionHistoricaIdAndActoPublicitarioId(prelacionId, prelacionHistorica,apId);
    	
    }
    //
    public CopiaActoPublicitarioVO generaCopiaActoPublicitario(BusquedaResultado br) {
    	CopiaActoPublicitarioVO copiaActoPub = new CopiaActoPublicitarioVO();
    	copiaActoPub.setEntradaActual(br.getPrelacion().getConsecutivo().toString());
    	if(br.getPrelacionHistorica()!=null)
    	 copiaActoPub.setEntrada_historica(br.getPrelacionHistorica().getConsecutivo().toString());
    	copiaActoPub.setAnio(br.getPrelacion().getAnio().toString());
    	copiaActoPub.setSubindice(br.getPrelacion().getSubindice().toString());
    	if(br.getActoPublicitario()!=null) {
    	 copiaActoPub.setNumero_acto_publicitario(String.valueOf(br.getActoPublicitario().getNumero()));
         copiaActoPub.setNombre_tipo_acto(br.getActoPublicitario().getActo().getTipoActo().getNombre());
         if(br.getActoPublicitario().getNum_folio_real()!=null)
        	 copiaActoPub.setFolioReal(""+br.getActoPublicitario().getNum_folio_real());
    	}
    	
    	return copiaActoPub;
    }
    
    @Transactional
    public Boolean savePredio(Long prelacionId,Long predioId) {
    	Prelacion prelacion =  prelacionRepository.findById(prelacionId);
    	Predio predio =  predioRepository.findById(predioId);
    	BusquedaResultado brs =  new BusquedaResultado();
    	brs.setPredio(predio);
    	brs.setPrelacion(prelacion);
    	busquedaResultadoRepository.save(brs);
    	return true;
    }
    
    @Transactional
    public Boolean savePersonaJuridica(Long prelacionId,Long pjId) {
    	Prelacion prelacion =  prelacionRepository.findById(prelacionId);    	
        PersonaJuridica pj = personaJuridicaRepository.findById(pjId);
    	BusquedaResultado brs =  new BusquedaResultado();
    	
        brs.setPersonaJuridica(pj);
    	brs.setPrelacion(prelacion);
    	busquedaResultadoRepository.save(brs);
    	return true;
    }
    
    @Transactional
    public BusquedaResultado savePredioCopia(Long prelacionId,Long predioId,Integer tipoFolio) {
    	Prelacion prelacion =  prelacionRepository.findById(prelacionId);
    	Predio predio = null;
    	PersonaJuridica pj = null;
    	
    	if(tipoFolio == Constantes.ETipoFolio.PREDIO.getTipoFolio()) {
    	  predio =  predioRepository.findById(predioId);
    	}else if (tipoFolio == Constantes.ETipoFolio.PERSONAS_JURIDICAS.getTipoFolio()) {
    	  pj = personaJuridicaRepository.findOne(predioId);
    	}
    	
    	BusquedaResultado brs =  new BusquedaResultado();
    	try {
			this.clearBusquedaFromPrelacion(prelacionId);
	    	brs.setPredio(predio);
	    	brs.setPersonaJuridica(pj);
	    	brs.setPrelacion(prelacion);
	    	busquedaResultadoRepository.save(brs);
		} catch (Exception e) {
			 brs = null;
		}
    	
    	
    	return brs;
    }
    
    @Transactional
    public Boolean deletePredio(Long prelacionId,Long predioId) {
    	List<BusquedaResultado> result =  busquedaResultadoRepository.findAllByPrelacionIdAndPredioId(prelacionId,predioId);
    	busquedaResultadoRepository.delete(result);
    	return true;
    }
    
    @Transactional
    public Boolean deletePersonaJuridica(Long prelacionId,Long pjId) {
    	List<BusquedaResultado> result =  busquedaResultadoRepository.findAllByPrelacionIdAndPersonaJuridicaId(prelacionId,pjId);
    	busquedaResultadoRepository.delete(result);
    	return true;
    }

    @Transactional
    public BusquedaResultadoActo saveActo(Long actoId,Long busquedaResultadoId) 
	{
		BusquedaResultado busqueda =  busquedaResultadoRepository.findOneById(busquedaResultadoId);
		Acto acto =  actoService.findById(actoId);
		BusquedaResultadoActo bras =  new BusquedaResultadoActo();
		bras.setActo(acto);
		bras.setBusquedaResultado(busqueda);
		busquedaResultadoActoRepository.save(bras);
		return bras;
	}
    

    public Boolean deleteActo(Long actoId,Long busquedaResultadoId) {
    	Set<BusquedaResultadoActo> result =  busquedaResultadoActoRepository.findByActoIdAndBusquedaResultadoId(actoId,busquedaResultadoId);
    	busquedaResultadoActoRepository.delete(result);
    	return true;
    }
    
    public Set<BusquedaResultadoActo> findBusquedaResultadoActos(Long busquedaResultadoId){
    	return   busquedaResultadoActoRepository.findByBusquedaResultadoId(busquedaResultadoId);
    }
}
