package com.gisnet.erpp.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.Suspension;
import com.gisnet.erpp.domain.SuspensionBitacora;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.SuspensionBitacoraRepository;
import com.gisnet.erpp.vo.BoletaSuspensionVO;
import com.gisnet.erpp.vo.certificado.CertificadoLibertadOGravamenVO;


@Service
public class SuspensionBitacoraService {

	@Autowired
	SuspensionBitacoraRepository suspensionBitacoraRepository;
	
	@Autowired
	BoletaRegistralService boletaRegistralService;
	
	
	public void create(Suspension suspension, Prelacion prelacion,Usuario usuario) {
		
       BoletaSuspensionVO boleta =		boletaRegistralService.findOneVOBoletaSuspension(prelacion);
	   SuspensionBitacora suspensionBitacora = new SuspensionBitacora();
	   suspensionBitacora.setDatos(dataToJSON(boleta));
	   suspensionBitacora.setEstatusSuspension(suspension.getEstatusSuspension());
	   suspensionBitacora.setFechaSuspension(suspension.getFechaSuspension());
	   suspensionBitacora.setFechaCalculo(suspension.getFechaCalculo());
	   suspensionBitacora.setPrelacion(prelacion);
	   suspensionBitacora.setUsuario(usuario);
	   suspensionBitacora.setFundamento(boleta.getFundamento());
	   suspensionBitacora.setMotivo(boleta.getRazonSuspension());
	   suspensionBitacora.setObservaciones(boleta.getComentarios());
	   suspensionBitacoraRepository.save(suspensionBitacora);
		
	}
	
	
	public List<SuspensionBitacora> suspensionesByPrelacion(Long prelacionId){
	  return this.suspensionBitacoraRepository.findAllByPrelacionIdOrderByIdDesc(prelacionId); 
	}
	
	public BoletaSuspensionVO boletaSuspension(Long suspensionBitacoraId)
	{
		SuspensionBitacora suspension =  suspensionBitacoraRepository.findOne(suspensionBitacoraId);
		return this.jsonTOBoleta(suspension.getDatos());
	}
	
	
	private String dataToJSON(BoletaSuspensionVO clg) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(clg);
		} catch (JsonProcessingException e) {
		}

		return "";
	}
	
	
	private BoletaSuspensionVO jsonTOBoleta(String json) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(json, BoletaSuspensionVO.class);
		} catch (IOException e) {
			
		}

		return null;
	}
}
