package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Parametro;
import com.gisnet.erpp.domain.ParametroCotizadorCosto;
import com.gisnet.erpp.repository.ParametroRepository;
import com.gisnet.erpp.util.Constantes;

@Service
public class ParametroService {

	@Autowired
	private ParametroRepository parametroRepository;
	
	public Parametro findByCve(String cve){
		return parametroRepository.findByCve(cve);
	}
	
	public Parametro[] loadMensajeCotizador(){
		return new Parametro[]{parametroRepository.findByCve(Constantes.COTIZADOR_MENSAJE_CVE), parametroRepository.findByCve(Constantes.FUNDAMENTO_JURIDICO_CVE)}; 
	}
	
	public List<Parametro> findAll(){
		return parametroRepository.findAll();
	}
	
	public List<Parametro> updateParametro(List <Parametro> parametros){
		parametroRepository.save(parametros);
		
		return parametros;
	}
	
	public boolean save(Parametro parametro) {
		Boolean status= false;
		String clave =parametro.getCve().toUpperCase();
		parametro.setCve(clave);
		String cve = parametro.getCve().replaceAll(" ", "");
		System.out.println("Clave : " + parametro.getCve());
		try {
			if((parametro.getCve() != null)) {
				if(parametro.getValor() != null) {
			 List<Parametro> listParametro = parametroRepository.findParametroBycve(cve);
			 System.out.println("Longitud : " + listParametro.size());
			 if(listParametro.size()== 0) {
					parametroRepository.save(parametro);
					status=  true;
				}else {
					status=  false;
				}
			}else {
				status= false;
			}
			}else {
				status = false;
			}
		} catch (Exception e){
			
		}

       return status;
	}
}
