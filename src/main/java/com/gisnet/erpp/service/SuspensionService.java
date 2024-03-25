package com.gisnet.erpp.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.SHACheckSum;
import com.gisnet.erpp.util.SignerUtilBouncyCastle;

import com.gisnet.erpp.domain.Bitacora;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.Status;
import com.gisnet.erpp.domain.Suspension;
import com.gisnet.erpp.domain.SuspensionFirma;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.BitacoraRepository;
import com.gisnet.erpp.repository.PrelacionRepository;
import com.gisnet.erpp.repository.ResultadoRepository;
import com.gisnet.erpp.repository.SuspensionRepository;
import com.gisnet.erpp.repository.SuspensionFirmaRepository;
import com.gisnet.erpp.repository.StatusRepository;

import com.gisnet.erpp.vo.ArchivoFirmaVO;

import net.bull.javamelody.internal.common.LOG;

import com.gisnet.erpp.service.UsuarioService;

import com.gisnet.erpp.security.SecurityUtils;

//JADV-SUSPENSION
@Service
public class SuspensionService {

	@Autowired
	private PrelacionRepository prelacionRepository;

	@Autowired
	private SuspensionRepository suspencionRepository;

	@Autowired
	private SuspensionFirmaRepository suspensionFirmaRepository;

	@Autowired
	private StatusRepository statusRepository;
	
	@Autowired
	private BitacoraRepository bitacoraRepository;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private PrelacionService prelacionService;

	@Autowired
	private BitacoraService bitacoraService;
	
	@Autowired
	private ResultadoRepository resultadoRepository;
	
	@Autowired
	private SuspensionBitacoraService suspensionBitacoraService;

	public Suspension saveSuspensionFirma(
		ArchivoFirmaVO archivoVO
		){
		
		
		Usuario user= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());		
		Prelacion prelacion = prelacionRepository.findById(archivoVO.getId());
		Suspension suspension= suspencionRepository.findOneByestatusSuspensionAndPrelacion(2L, prelacion);
		

		if(suspension!=null){	
				
				if(suspension.getSuspensionFirma()!=null) {
					suspensionFirmaRepository.delete(suspension.getSuspensionFirma().getId());
				}
				SuspensionFirma suspensionFirma= new SuspensionFirma();
				suspensionFirma.setSuspension(suspension);
				
				suspensionFirma.setPkcs7( archivoVO.getPkcs7());
				System.out.println("getPcs7: "+archivoVO.getPkcs7());
				System.out.println("SignerUtilBouncyCastle.getEncryptedDigest: "+SignerUtilBouncyCastle.getEncryptedDigest(archivoVO.getPkcs7()));
				suspensionFirma.setFirma(SignerUtilBouncyCastle.getEncryptedDigest(archivoVO.getPkcs7()));					
				suspensionFirma.setSecuencia( archivoVO.getSecuencia()); 
				suspensionFirma.setPolitica( archivoVO.getPolitica());
				suspensionFirma.setDigestion( archivoVO.getDigestion()) ;
				suspensionFirma.setSecuenciaTS( archivoVO.getSecuenciaTS());
				suspensionFirma.setEstampilla(archivoVO.getEstampilla()); 
				suspensionFirma.setFechaFirma(new Date());
				suspensionFirma.setUsuario(user);
				suspensionFirmaRepository.save(suspensionFirma);
				
				// Obtiene Bitacora de SUSPENDIDA POR CALIFICADOR
				Bitacora bitacoraSuspendidaCalificador=bitacoraService.findOneBitacoraByPrelacionIdAndStatusId(prelacion);
				// Cambia estatus de Prelacion a SUSPENDIDA POR REGISTRADOR
				prelacion.setStatus(statusRepository.findOne(17L));
				prelacion.setResultado(resultadoRepository.findOne(Constantes.Resultado.SUSPENDIDO.getIdResultado()));
				prelacionRepository.save(prelacion);
				// Generar registro en BITACORA
				Bitacora bitacora=prelacionService.createBitacoraCompleta(prelacion,null,null,null,null,bitacoraSuspendidaCalificador.getObservaciones());	
				bitacoraRepository.save(bitacora);
			
				//CREA BITACORA SUSPENSION
				
				suspensionBitacoraService.create(suspension, prelacion, user);
				
				
				
		}
		
		
		return suspension;
	}
	
	public boolean quitarSuspension(
		Prelacion prelacion
		){
		boolean res=false;				
		
		Suspension suspension= suspencionRepository.findOneByestatusSuspensionAndPrelacion(2L, prelacion);
		SuspensionFirma suspensionFirma= null;		
		
		if(suspension!=null){	
				//Borrar Suspension Firma
				suspensionFirma=suspensionFirmaRepository.findOneBySuspension(suspension);
				if(suspensionFirma!=null){
					suspensionFirmaRepository.delete(suspensionFirma);
				}
				//Borrar Suspension
				suspencionRepository.delete(suspension);
				res=true;
		} 
		return res;

	}
	
}
