package com.gisnet.erpp.service;

import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.gisnet.erpp.domain.BloqueoFolio;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.Motivo;
import com.gisnet.erpp.domain.Mueble;
import com.gisnet.erpp.domain.PersonaJuridica;
import com.gisnet.erpp.repository.BloqueoFolioRepository;
import com.gisnet.erpp.repository.FolioSeccionAuxiliarRepository;
import com.gisnet.erpp.repository.MuebleRepository;
import com.gisnet.erpp.repository.PersonaJuridicaRepository;
import com.gisnet.erpp.vo.PredioVO;
import com.gisnet.erpp.vo.BloqueoFolioVO;
import org.apache.commons.beanutils.BeanUtils;
import java.lang.reflect.InvocationTargetException;
import com.gisnet.erpp.domain.Colindancia;
import com.gisnet.erpp.domain.FolioSeccionAuxiliar;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.util.Constantes;

@Service
public class BloqueoFolioService{
	@Autowired
    BloqueoFolioRepository bloqueoFolioRepository;
    
    @Autowired
	UsuarioService usuarioService;
    
    @Autowired
	private PredioService predioService;
    
    @Autowired
	private PersonaJuridicaRepository personaJuridicaRepository;
    
    @Autowired
	private MuebleRepository muebleRepository;
    
    @Autowired
	private FolioSeccionAuxiliarRepository folioSeccionAuxiliarRepository;

	public  List<BloqueoFolioVO> findAllByPredio(Long idBusqueda , Long tipo) {
        List<BloqueoFolioVO> bloqueos=new ArrayList<BloqueoFolioVO>(); 
        List<BloqueoFolio> listBloqueos = new ArrayList<BloqueoFolio>(); 
        
        if ((int)(long)tipo == Constantes.ETipoFolio.PERSONAS_JURIDICAS.getTipoFolio() ) {
        	listBloqueos = bloqueoFolioRepository.findAllByPersonaJuridicaIdOrderByVersionAsc(idBusqueda);
        }
        
        if ((int)(long)tipo == Constantes.ETipoFolio.PERSONAS_AUXILIARES.getTipoFolio() ) {
        	listBloqueos = bloqueoFolioRepository.findAllByFolioSeccionAuxiliarIdOrderByVersionAsc(idBusqueda);
        }
        
        if ((int)(long)tipo == Constantes.ETipoFolio.MUEBLE.getTipoFolio() ) {
        	listBloqueos = bloqueoFolioRepository.findAllByMuebleIdOrderByVersionAsc(idBusqueda);
        }
        
        if ((int)(long)tipo == Constantes.ETipoFolio.PREDIO.getTipoFolio() ) {
        	listBloqueos = bloqueoFolioRepository.findAllByPredioIdOrderByVersionAsc(idBusqueda);	
        }
        System.out.println("Lista de bloqueos ---> " +  listBloqueos);
        
            for(BloqueoFolio b :listBloqueos){

                BloqueoFolioVO tem= new BloqueoFolioVO();
                tem.setId(b.getId());
                if ((int)(long)tipo == Constantes.ETipoFolio.PREDIO.getTipoFolio() ) {
                	tem.setPredio(copyProperties(b.getPredio()));	
                }
       
                tem.setMotivo(b.getMotivo());
                tem.setVersion(b.getVersion());
                tem.setObservaciones(b.getObservaciones());
                tem.setUsuario(b.getUsuario());
                bloqueos.add(tem);
            }

        return bloqueos;
    }

    public BloqueoFolio saveAndUpdate(BloqueoFolio bloqueoFolio) {
        
        Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
        bloqueoFolio.setUsuario(usuario);
        bloqueoFolioRepository.saveAndFlush(bloqueoFolio);
        return bloqueoFolio;
    }

    public BloqueoFolio  getLastVersion(Long idPredio){
    
        List<BloqueoFolio>  bloqueos = bloqueoFolioRepository.findAllByPredioIdAndVersionIsNotNullOrderByVersionDesc(idPredio);
        
        if(bloqueos!=null && bloqueos.size()>0){
            return bloqueos.get(0);
        }else{
            return null;
        }
        
    }
    
    public BloqueoFolio  getLastVersionMueble(Long idMueble){
        
        List<BloqueoFolio>  bloqueos = bloqueoFolioRepository.findAllByMuebleIdAndVersionIsNotNullOrderByVersionDesc(idMueble);
        
        if(bloqueos!=null && bloqueos.size()>0){
            return bloqueos.get(0);
        }else{
            return null;
        }
        
    }
    
    public BloqueoFolio  getLastVersionSeccionAuxiliar(Long idAuxiliar){
        
        List<BloqueoFolio>  bloqueos = bloqueoFolioRepository.findAllByFolioSeccionAuxiliarIdAndVersionIsNotNullOrderByVersionDesc(idAuxiliar);
        
        if(bloqueos!=null && bloqueos.size()>0){
            return bloqueos.get(0);
        }else{
            return null;
        }
        
    }
    
    public BloqueoFolio  getLastVersionPersonaJuridica(Long idPersonaJuridica){
        
        List<BloqueoFolio>  bloqueos = bloqueoFolioRepository.findAllByPersonaJuridicaIdAndVersionIsNotNullOrderByVersionDesc(idPersonaJuridica);
        
        if(bloqueos!=null && bloqueos.size()>0){
            return bloqueos.get(0);
        }else{
            return null;
        }
        
    }

    public BloqueoFolio findOne(Long id) {
		return bloqueoFolioRepository.findOne(id);
    } 
    
    public BloqueoFolio updateMotivo(BloqueoFolio bloqueoFolio) {
        bloqueoFolio.setId(null);
        Integer version=bloqueoFolioRepository.maxByPredio(bloqueoFolio.getPredio().getId());

        if(version==null)
            version=1;
        else{
            version++;
                
        }   
        bloqueoFolio.setVersion(version);
        bloqueoFolioRepository.saveAndFlush(bloqueoFolio);

        return bloqueoFolio;
    }


    public  PredioVO copyProperties(Predio predio){
        PredioVO predioVo= null;
		try {
            predioVo= new PredioVO();
            BeanUtils.copyProperties(predioVo, predio);
            
           
            if(predio.getColindanciasParaPredios() != null && !predio.getColindanciasParaPredios().isEmpty()){
                predioVo.setColindancias(predio.getColindanciasParaPredios().toArray(new Colindancia[predio.getColindanciasParaPredios().size()]));
            }
            return predioVo;
		} catch (IllegalAccessException e) {
            e.printStackTrace();
            return predioVo;
		} catch (InvocationTargetException e) {
            e.printStackTrace();
            return predioVo;
		}

		
	}
    
    public ResponseEntity<BloqueoFolioVO> bloquearPredio(BloqueoFolioVO bloqueoFolio){
    	
		Predio predio = predioService.findOne(bloqueoFolio.getPredio().getId());
		
		BloqueoFolio bloqueoFolioOrig=null;
		
		if(predio!=null){
			
			if(bloqueoFolio.getId()!=null){
				bloqueoFolioOrig=bloqueoFolioRepository.findOne(bloqueoFolio.getId());
				if(bloqueoFolioOrig!=null){

					Integer version=bloqueoFolioRepository.maxByPredio(bloqueoFolio.getPredio().getId());

					if(version==null)
						version=1;
					else
						version++;
					//bloqueoFolioOrig.setId(null);
					bloqueoFolioOrig.setVersion(version);
					bloqueoFolioOrig.setMotivo(bloqueoFolio.getMotivo());
					bloqueoFolioOrig.setObservaciones(bloqueoFolio.getObservaciones());
					saveAndUpdate(bloqueoFolioOrig);
				}
			}else{
				predio.setBloqueado(!bloqueoFolio.getPredio().getBloqueado());
				predioService.saveAndUpdate(predio);
				bloqueoFolioOrig=new BloqueoFolio();
				Integer version=bloqueoFolioRepository.maxByPredio(bloqueoFolio.getPredio().getId());

					if(version==null)
						version=1;
					else
						version++;
					bloqueoFolioOrig.setId(null);
					bloqueoFolioOrig.setVersion(version);
				bloqueoFolioOrig.setMotivo(bloqueoFolio.getMotivo());
				bloqueoFolioOrig.setObservaciones(bloqueoFolio.getObservaciones());
				bloqueoFolioOrig.setPredio(predio);
				saveAndUpdate(bloqueoFolioOrig);
				bloqueoFolio.setId(bloqueoFolioOrig.getId());
				bloqueoFolio.setUsuario(bloqueoFolioOrig.getUsuario());
			}
		}else{
			return new ResponseEntity<>(bloqueoFolio, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(bloqueoFolio, HttpStatus.OK);
	}
    
	public ResponseEntity<BloqueoFolioVO> bloquearPersonaJuridica(BloqueoFolioVO bloqueoFolio){
    	
		PersonaJuridica PJ = personaJuridicaRepository.findOne(bloqueoFolio.getPersonaJuridica().getId());
		
		BloqueoFolio bloqueoFolioOrig=null;
		
		if(PJ!=null){
			
			if(bloqueoFolio.getId()!=null){
				bloqueoFolioOrig=bloqueoFolioRepository.findOne(bloqueoFolio.getId());
				if(bloqueoFolioOrig!=null){

					Integer version=bloqueoFolioRepository.maxByPersonaJuridica(bloqueoFolio.getPersonaJuridica().getId());

					if(version==null)
						version=1;
					else
						version++;
					//bloqueoFolioOrig.setId(null);
					bloqueoFolioOrig.setVersion(version);
					bloqueoFolioOrig.setMotivo(bloqueoFolio.getMotivo());
					bloqueoFolioOrig.setObservaciones(bloqueoFolio.getObservaciones());
					saveAndUpdate(bloqueoFolioOrig);
				}
			}else{
				PJ.setBloqueado(!bloqueoFolio.getPersonaJuridica().getBloqueado());
				personaJuridicaRepository.saveAndFlush(PJ);
				bloqueoFolioOrig=new BloqueoFolio();
				Integer version=bloqueoFolioRepository.maxByPersonaJuridica(bloqueoFolio.getPersonaJuridica().getId());

					if(version==null)
						version=1;
					else
						version++;
					bloqueoFolioOrig.setId(null);
					bloqueoFolioOrig.setVersion(version);
				bloqueoFolioOrig.setMotivo(bloqueoFolio.getMotivo());
				bloqueoFolioOrig.setObservaciones(bloqueoFolio.getObservaciones());
				bloqueoFolioOrig.setPersonaJuridica(PJ);
				saveAndUpdate(bloqueoFolioOrig);
				bloqueoFolio.setId(bloqueoFolioOrig.getId());
				bloqueoFolio.setUsuario(bloqueoFolioOrig.getUsuario());
			}
		}else{
			return new ResponseEntity<>(bloqueoFolio, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(bloqueoFolio, HttpStatus.OK);
	}
    
   public ResponseEntity<BloqueoFolioVO> bloquearMueble(BloqueoFolioVO bloqueoFolio){
    	
		Mueble mueble = muebleRepository.findOne(bloqueoFolio.getMueble().getId());
		
		BloqueoFolio bloqueoFolioOrig=null;
		
		if(mueble!=null){
			
			System.out.println("Bloqueo Folio ----->> " + bloqueoFolio.getId());
			if(bloqueoFolio.getId()!=null){
				bloqueoFolioOrig=bloqueoFolioRepository.findOne(bloqueoFolio.getId());
				if(bloqueoFolioOrig!=null){

					Integer version=bloqueoFolioRepository.maxByMueble(bloqueoFolio.getMueble().getId());
					System.out.println("Version Mueble 1 : "+bloqueoFolio.getMueble().getId()+"  : ---- > " + version);
					if(version==null)
						version=1;
					else
						version++;
					//bloqueoFolioOrig.setId(null);
					bloqueoFolioOrig.setVersion(version);
					bloqueoFolioOrig.setMotivo(bloqueoFolio.getMotivo());
					bloqueoFolioOrig.setObservaciones(bloqueoFolio.getObservaciones());
					saveAndUpdate(bloqueoFolioOrig);
				}
			}else{
				//Actualiza Mueble
				mueble.setBloqueado(!bloqueoFolio.getMueble().getBloqueado());
				muebleRepository.saveAndFlush(mueble);
				
				bloqueoFolioOrig=new BloqueoFolio();
				Integer version=bloqueoFolioRepository.maxByMueble(bloqueoFolio.getMueble().getId());
				System.out.println("Version Mueble 2 : "+bloqueoFolio.getMueble().getId()+"  : ---- > " + version);
					if(version==null)
						version=1;
					else
						version++;
					bloqueoFolioOrig.setId(null);
					bloqueoFolioOrig.setVersion(version);
				bloqueoFolioOrig.setMotivo(bloqueoFolio.getMotivo());
				bloqueoFolioOrig.setObservaciones(bloqueoFolio.getObservaciones());
				bloqueoFolioOrig.setMueble(mueble);
				saveAndUpdate(bloqueoFolioOrig);
				bloqueoFolio.setId(bloqueoFolioOrig.getId());
				bloqueoFolio.setUsuario(bloqueoFolioOrig.getUsuario());
				
			}
		}else{
			return new ResponseEntity<>(bloqueoFolio, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(bloqueoFolio, HttpStatus.OK);
	}
   
   public ResponseEntity<BloqueoFolioVO> bloquearFolioSeccionAuxiliar(BloqueoFolioVO bloqueoFolio){
   	
		FolioSeccionAuxiliar folioSA = folioSeccionAuxiliarRepository.findOne(bloqueoFolio.getFolioSeccionAuxiliar().getId());
		
		BloqueoFolio bloqueoFolioOrig=null;
		
		if(folioSA!=null){
			
			if(bloqueoFolio.getId()!=null){
				bloqueoFolioOrig=bloqueoFolioRepository.findOne(bloqueoFolio.getId());
				if(bloqueoFolioOrig!=null){

					Integer version=bloqueoFolioRepository.maxByFolioSeccionAuxiliar(bloqueoFolio.getFolioSeccionAuxiliar().getId());

					if(version==null)
						version=1;
					else
						version++;
					//bloqueoFolioOrig.setId(null);
					bloqueoFolioOrig.setVersion(version);
					bloqueoFolioOrig.setMotivo(bloqueoFolio.getMotivo());
					bloqueoFolioOrig.setObservaciones(bloqueoFolio.getObservaciones());
					saveAndUpdate(bloqueoFolioOrig);
				}
			}else{
				folioSA.setBloqueado(!bloqueoFolio.getFolioSeccionAuxiliar().getBloqueado());
				folioSeccionAuxiliarRepository.saveAndFlush(folioSA);
				bloqueoFolioOrig=new BloqueoFolio();
				Integer version=bloqueoFolioRepository.maxByPersonaJuridica(bloqueoFolio.getFolioSeccionAuxiliar().getId());

					if(version==null)
						version=1;
					else
						version++;
					bloqueoFolioOrig.setId(null);
					bloqueoFolioOrig.setVersion(version);
				bloqueoFolioOrig.setMotivo(bloqueoFolio.getMotivo());
				bloqueoFolioOrig.setObservaciones(bloqueoFolio.getObservaciones());
				bloqueoFolioOrig.setFolioSeccionAuxiliar(folioSA);
				saveAndUpdate(bloqueoFolioOrig);
				bloqueoFolio.setId(bloqueoFolioOrig.getId());
				bloqueoFolio.setUsuario(bloqueoFolioOrig.getUsuario());
			}
		}else{
			return new ResponseEntity<>(bloqueoFolio, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(bloqueoFolio, HttpStatus.OK);
	}
    


}
