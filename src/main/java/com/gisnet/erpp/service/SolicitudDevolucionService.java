package com.gisnet.erpp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.PrelacionUsuarioDef;
import com.gisnet.erpp.domain.Recibo;
import com.gisnet.erpp.domain.SolicitudDevolucion;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.PrelacionUsuarioDefRepository;
import com.gisnet.erpp.repository.ReciboRepository;
import com.gisnet.erpp.repository.SolicitudDevolucionRepository;
import com.gisnet.erpp.repository.StatusRepository;
import com.gisnet.erpp.repository.UsuarioRepository;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.ReciboVO;
import com.gisnet.erpp.vo.SolicitudDevolucionVO;

@Service
public class SolicitudDevolucionService {
	@Autowired
	SolicitudDevolucionRepository solicitudDevolucionRepository;
	@Autowired
	PrelacionUsuarioDefRepository prelacionUsuarioDefRepository;
	@Autowired
	UsuarioRepository usuarioRepository;
	@Autowired
	UsuarioService usuarioService;
	@Autowired
	PrelacionService prelacionService;
	@Autowired
	ReciboRepository reciboRepository;
	@Autowired
	StatusRepository statusRepository;

	@Transactional
	public SolicitudDevolucion create(SolicitudDevolucion solicitudDevolucion) throws Exception {
		SolicitudDevolucion solicitud = new SolicitudDevolucion();
		Usuario recepcion = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());

		this.validate(solicitudDevolucion);

		solicitud.setPrelacion(solicitudDevolucion.getPrelacion());
		solicitud.setSolicitante(usuarioRepository.findOne(solicitudDevolucion.getSolicitante().getId()));
		solicitud.setFechaIngreso(new Date());
		solicitud.setRecepcion(recepcion);
		solicitud.setFundamentoRecepcion(solicitudDevolucion.getFundamentoRecepcion());

		solicitud = solicitudDevolucionRepository.save(solicitud);

		// SI ES USUARIO DEFAULT GUARDAMOS PRELACIONUSUARIODEF
		if (solicitudDevolucion.getSolicitante().getId().equals(0l)) {
			Persona persona = solicitudDevolucion.getSolicitante().getPersona();
			PrelacionUsuarioDef pud = new PrelacionUsuarioDef();
			pud.setNombre(persona.getNombre());
			pud.setPaterno(persona.getPaterno());
			pud.setMaterno(persona.getMaterno());

			if (persona.getTelefonosParaPersonas() != null && persona.getTelefonosParaPersonas().size() > 0)
				pud.setTelefono(persona.getTelefonosParaPersonas().iterator().next().getNumTelefono());
			if (persona.getIdentificacionesParaPersonas() != null
					&& persona.getIdentificacionesParaPersonas().size() > 0)
				pud.setTipoIden(persona.getIdentificacionesParaPersonas().iterator().next().getTipoIden());

			pud.setSolicitudDevolucion(solicitud);
			prelacionUsuarioDefRepository.save(pud);
		}

		if (solicitudDevolucion.getRecibosParaSolicitudDevolucion() != null
				&& solicitudDevolucion.getRecibosParaSolicitudDevolucion().size() > 0) {

			for (Recibo x : solicitudDevolucion.getRecibosParaSolicitudDevolucion()) {
				Recibo recibo = new Recibo();
				recibo.setPrelacion(solicitudDevolucion.getPrelacion());
				recibo.setReferencia(x.getReferencia());
				recibo.setMonto(x.getMonto());
				recibo.setFecha(x.getFecha());
				recibo.setSolicitudDevolucion(solicitud);
				recibo.setNoRecibo(x.getNoRecibo());
				recibo.setCuenta(x.getCuenta());
				this.reciboRepository.save(recibo);

			}
		}

		String sequence = "solicitud_devolucion_neg_" + recepcion.getOficina().getNumOficina();
		
		if(recepcion.getOficina().getReq_digitalizar()) {
			prelacionService.updatePrelacionEstado(solicitudDevolucion.getPrelacion().getId(),
					Constantes.Status.DIGITALIZA_DEVOLUCION_DOCUMENTO, "DIGITALIZA DEVOLUCION DOCUMENTOS", null, null);
		}else {
			prelacionService.updatePrelacionEstado(solicitudDevolucion.getPrelacion().getId(),
					Constantes.Status.PENDIENTE_DEVOLUCION_DOCUMENTO, "RECEPCIÓN DEVOLUCION DOCUMENTOS", null, null);
		}
		solicitud.setConsecutivo(solicitudDevolucionRepository.getConsecutivo(sequence));
		solicitudDevolucionRepository.save(solicitud);

		return solicitud;
	}

	private void validate(SolicitudDevolucion solicitud) throws Exception {
		if (solicitud.getSolicitante() == null || solicitud.getSolicitante().getId() == null)
			throw new Exception("Seleccione o registre un solicitante");
	}
	
	
	public SolicitudDevolucionVO getSolicitudDevolucionVO(Long solicitudId) {
		SolicitudDevolucion solicitud =  this.solicitudDevolucionRepository.findOne(solicitudId);
		SolicitudDevolucionVO result  = new SolicitudDevolucionVO();
		List<ReciboVO> recibos  = new ArrayList<>();
		
		if(solicitud!=null) {
			result.setConsecutivo(solicitud.getConsecutivo().toString());
			result.setEntrada(solicitud.getPrelacion().getConsecutivo().toString());
			result.setOficina(solicitud.getPrelacion().getOficina().getNombre());
			result.setFechaIngreso(solicitud.getFechaIngreso());
			result.setObservaciones(solicitud.getFundamentoRecepcion());
						
			//SI ES USUARIO DEFAULT LO TOMAMOS DE PRELACION_USUARIO_DEF
			if(solicitud.getSolicitante().getId().equals(Constantes.USUARIO_ID_DEFAULT)) {
				result.setEmailSolicita("");
			 PrelacionUsuarioDef personaDef =  this.prelacionUsuarioDefRepository.findFirstBySolicitudDevolucionId(solicitudId);
			 result.setNombreSolicita(personaDef.getNombreCompleto());
			}else {
				result.setEmailSolicita(solicitud.getSolicitante().getEmail());
				result.setNombreSolicita(solicitud.getSolicitante().getNombreCompleto());
			}
			
			solicitud.getRecibosParaSolicitudDevolucion().forEach(x->{
				ReciboVO recibo = new ReciboVO(x.getId(),x.getReferencia(),x.getMonto(),x.getFecha());
				recibos.add(recibo);
			});
			
			result.setRecibos(recibos);
			result.setUsuarioRecepciono(solicitud.getRecepcion().getNombreCompleto());
			
			
		}
		
		
		
		
		return result;
	}
	
	
	public SolicitudDevolucion findByPrelacion(Long prelacionId)
	{
		return solicitudDevolucionRepository.findFirstByPrelacionIdOrderByIdDesc(prelacionId);
	}
	
	
	@Transactional
	public SolicitudDevolucion autorizaDevolucion(SolicitudDevolucion solicitud) throws Exception {
		Usuario usuario = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		SolicitudDevolucion _solicitud =  this.solicitudDevolucionRepository.findOne(solicitud.getId());
		_solicitud.setFundamentoDevolucion(solicitud.getFundamentoDevolucion());
		_solicitud.setFechaAutorizaDevolucion(new Date());
		_solicitud.setRegistrador(usuario);
		solicitudDevolucionRepository.save(_solicitud);
		prelacionService.prelacionDevolucion(solicitud.getPrelacion().getId(), solicitud.getFundamentoDevolucion());
		prelacionService.dematerializaActos(_solicitud.getPrelacion().getId());
		return _solicitud;
	}
	
	@Transactional
	public void entragaSolicitudDevolucion(Long prelacionId) {
		SolicitudDevolucion solicitud =  this.findByPrelacion(prelacionId);
		solicitud.setFechaEntrega(new Date());
		Prelacion prelacion =  solicitud.getPrelacion();
		this.prelacionService.createBitacoraCompleta(prelacion, null, null, null, null, "ENTRAGA SOLICITUD DEVOLUCIÓN " +solicitud.getConsecutivo());
		prelacion.setStatus(statusRepository.findOne(Constantes.Status.DEVOLUCION_DOCUMENTO_ENTREGADO.getIdStatus()));
		prelacionService.saveAndUpdate(prelacion);
		solicitudDevolucionRepository.save(solicitud);
	}
}
