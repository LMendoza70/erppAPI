/**
 * 
 */
package com.gisnet.erpp.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.Pases;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.UnidadMedida;
import com.gisnet.erpp.repository.PasesRepository;
import com.gisnet.erpp.vo.caratula.PaseVO;
import com.gisnet.erpp.vo.fichaRegistral.PaseFichaVO;

/**
 * @author Jorge Medina
 *
 */
@Service
public class PasesService {
	
	private static final Logger log = LoggerFactory.getLogger(PasesService.class);

	@Autowired
	PasesRepository pasesRepository;
	
	@Autowired
	PrelacionService prelacionService;
	
	public List<PaseVO> findPases(Long predioId) {
		List<PaseVO> listaPasesVO = new ArrayList<PaseVO>();
		PaseVO paseVO = null;
		
		log.debug( "==> findPases, predioId {}",predioId );
		
		List<Pases> pasesList = this.pasesRepository.findByPredioOrigenId(predioId);

		log.debug( "==> findPases, pasesList {}",pasesList );
	
		if( pasesList != null ) {
			for( Pases pases : pasesList ) {
				paseVO = new PaseVO();				
				paseVO.setNoFolio( pases.getPredioSegre().getNoFolio() );
				paseVO.setSuperfice( pases.getSuperficie() );
				UnidadMedida unidadMedida = pases.getUnidadMedida();
				paseVO.setUnidadMedidaId( unidadMedida.getId() );
				paseVO.setUnidadMedidaNombre( unidadMedida.getNombre() );
				if(pases.getActo()!=null)
					 pases.getActo().setPrelacion(prelacionService.findOne(pases.getActo().getPrelacion().getId()));
				paseVO.setActo(pases.getActo());
				log.debug( "==> paseVO, {}",paseVO );
				listaPasesVO.add( paseVO );
			}
		}
		return listaPasesVO;
	}

	/*public PaseVO savePase(Predio predioOrigen, Predio predioSegregado) {
		log.debug( "===> PasesService <== " );
		
		String superficie = predioSegregado.getSuperficie();
		UnidadMedida unidadMedida = predioSegregado.getUnidadMedida();
		String fraccionCadena = predioSegregado.getFraccion();
		int fraccion = 0;
		try {
			fraccion = Integer.parseInt( fraccionCadena );
		} catch( NumberFormatException e ) {
		}

		Pases pases = new Pases();
		pases.setFraccion( fraccion );
		pases.setPredioOrigen( predioOrigen );
		pases.setPredioSegre( predioSegregado );
		pases.setSuperficie( superficie );
		pases.setUnidadMedida( unidadMedida );
		
		Pases entity = this.pasesRepository.save( pases );
		Long id = entity.getId();
		
		log.debug( "===> savePase = "+id );
		
		PaseVO paseVO = new PaseVO();
		paseVO.setNoFolio( predioSegregado.getNoFolio() );
		
		return paseVO;
	}
	*/
	
	public PaseVO savePase(Predio predioOrigen, Predio predioSegregado) {
		log.debug("===> PasesService <== ");
		Pases pases = initPase(predioOrigen, predioSegregado);
		Pases entity = this.pasesRepository.save(pases);
		Long id = entity.getId();

		log.debug("===> savePase = " + id);

		PaseVO paseVO = new PaseVO();
		paseVO.setNoFolio(predioSegregado.getNoFolio());
		return paseVO;
	}

	public PaseVO savePase(Predio predioOrigen, Predio predioSegregado, Acto acto) {
		Pases pases = initPase(predioOrigen, predioSegregado);
		pases.setActo(acto);
		Pases entity = this.pasesRepository.save(pases);
		PaseVO paseVO = new PaseVO();
		paseVO.setNoFolio(predioSegregado.getNoFolio());

		return paseVO;
	}
	
	private Pases initPase(Predio predioOrigen, Predio predioSegregado) {
		log.debug("===> PasesService <== ");

		String superficie = predioSegregado.getSuperficie();
		UnidadMedida unidadMedida = predioSegregado.getUnidadMedida();
		String fraccionCadena = predioSegregado.getFraccion();
		int fraccion = 0;
		try {
			fraccion = Integer.parseInt(fraccionCadena);
		} catch (NumberFormatException e) {
		}

		Pases pases = new Pases();
		pases.setFraccion(fraccion);
		pases.setPredioOrigen(predioOrigen);
		pases.setPredioSegre(predioSegregado);
		pases.setSuperficie(superficie);
		pases.setUnidadMedida(unidadMedida);
		return pases;
	}
	
	
	public double sumaSuperficie( List<PaseVO> listaPases ) {
		double totalSuperficieSegregada = 0d;
		
		for( PaseVO paseVO: listaPases ) {
			totalSuperficieSegregada += obtenSuperficieMetros( paseVO.getUnidadMedidaId(),""+paseVO.getSuperfice() );
		}
		
		return totalSuperficieSegregada;
	}

	public double obtenSuperficieMetros(Long unidadMedidaId, String superficie) {
		
		double superficieM2 = 0;
		
		try {
			switch (unidadMedidaId.intValue()) {
			case 1:
				superficieM2 = Double.parseDouble( superficie );
				break;
			case 2: // HECTAREAS
				String[] hectareas = superficie.split( "-" );
				superficieM2  = Double.parseDouble( hectareas[0] ) * 10000;
				superficieM2 += Double.parseDouble( hectareas[1] ) * 100;
				superficieM2 += Double.parseDouble( hectareas[2] ) * 1;
				break;
			case 3: // VARA
				superficieM2 = Double.parseDouble( superficie ) / 0.698737;
				break;
			case 4: // PIE
				superficieM2 = Double.parseDouble( superficie ) / 10.763915;
				break;
			case 5: // MILLA
				superficieM2 = Double.parseDouble( superficie ) / 0.00000038610;
				break;
			default:
				break;
			}
		} catch( Exception e ) {
			//e.printStackTrace();
		}

		return superficieM2;
	}
	
	public List<PaseFichaVO> findPasesFicha(Long predioId) {
		List<PaseFichaVO> listaPasesVO = new ArrayList<PaseFichaVO>();
		PaseFichaVO paseFichaVO = null;
		
		log.debug( "==> findPases, predioId {}",predioId );
		
		List<Pases> pasesList = this.pasesRepository.findByPredioOrigenId(predioId);

		log.debug( "==> findPases, pasesList {}",pasesList );
	
		if( pasesList != null ) {
			for( Pases pases : pasesList ) {
				paseFichaVO = new PaseFichaVO();				
				paseFichaVO.setNoFolio( pases.getPredioOrigen().getNoFolio() );
				paseFichaVO.setSuperfice( pases.getSuperficie() );
				UnidadMedida unidadMedida = pases.getUnidadMedida();
				paseFichaVO.setUnidadMedida( unidadMedida.getNombre() );
				if(pases.getPredioSegre() != null) {
					Predio segregado = pases.getPredioSegre();
					paseFichaVO.setNoFolioSegregado(segregado.getNoFolio());
					paseFichaVO.setClaveCatastralSegregado(segregado.getClaveCatastral());
					paseFichaVO.setCuentaCatastralSegregado(segregado.getCuentaCatastral());
				}
				
				log.debug( "==> paseVO, {}",paseFichaVO );
				listaPasesVO.add( paseFichaVO );
			}
		}
		return listaPasesVO;
	}
}
