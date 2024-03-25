package com.gisnet.erpp.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoFolioRecibo;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.Recibo;
import com.gisnet.erpp.domain.ReciboConcepto;
import com.gisnet.erpp.repository.ActoFolioReciboRepository;
import com.gisnet.erpp.repository.ReciboConceptoRepository;
import com.gisnet.erpp.repository.ReciboRepository;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.ReciboVO;


@Service
public class ReciboService {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ReciboRepository reciboRepository;
	
	@Autowired
	private ReciboConceptoRepository reciboConceptoRepository;
	
	@Autowired
	private ActoPredioService actoPredioService;
	
	@Autowired
	private ActoService actoService;
	
	@Autowired ActoFolioReciboRepository actoFolioReciboRepository;
	
	public List<Recibo> findAll() {
		return reciboRepository.findAll();
	}
	
	/**
	 * Obtiene los Recibos asignados al acto segun la version, que es la relacion de acto_folio_recibo
	 * @param actoId
	 * @param version
	 * @return
	 */
	public List<Recibo> findAsignadosActoIdAndVersion(Long actoId, Integer version){
		return reciboRepository.findByActoFolioRecibosParaRecibosActoPredioActoIdAndActoFolioRecibosParaRecibosActoPredioVersion(actoId, version);
	}
	
	/**
	 * Obtiene Recibos disponibles para la prelacion a la que pertenece el acto, estos son los recibos en acto_predio con la version 1 que no pertenescan al acto actual
	 * @param actoId
	 * @return
	 */
	public List<Recibo> findDisponiblesByActo(Long actoId, Integer version){
		Acto acto = actoService.findOne(actoId);
		return reciboRepository.findDisponiblesByActo(acto.getPrelacion().getId(), 1, acto.getId(), version);
	}
	
	@Transactional
	public void saveActoFoliosFromIds(String ids, ActoPredio actoPredio) {
		log.debug("Salvando recibos de string={}", ids);
		if (ids != null && ids.length()>0) {
			String [] strings = ids.split(",");
			for (int i = 0; i < strings.length; i++) {
				ActoFolioRecibo actoFolioRecibo = new ActoFolioRecibo();
				actoFolioRecibo.setRecibo(reciboRepository.findOne(Long.valueOf(strings[i])));
				actoFolioRecibo.setActoPredio(actoPredio);
				actoFolioReciboRepository.save(actoFolioRecibo);
			}
		}
	}


	public List<Recibo> findByPrelacionId(Long id) {
		return reciboRepository.findByPrelacionId(id);
	}

	
	public List<ReciboConcepto> findAllByPrelacionId(Long id) {
		return reciboConceptoRepository.getReciboConceptoByPrelacion(id);
	}
	
	
	public Recibo findByReciboId( long reciboId){
		return reciboRepository.findOne(reciboId);
	}

	public Recibo saveAndUpdate(Recibo recibo) {

		return reciboRepository.saveAndFlush(recibo);

	}
	
	public List<ActoFolioRecibo> saveAndUpdate( Recibo recibo, Acto[] actos ) {
		
		List<ActoFolioRecibo> reciboActoList = new ArrayList<ActoFolioRecibo>();
		
		
		
		ActoFolioRecibo reciboActo;
		for( Acto acto: actos ) {
			reciboActo = new ActoFolioRecibo();
			reciboActo.setRecibo( recibo );
			reciboActo.setActoPredio(actoPredioService.findOneByActoIdAndVersionUno(acto.getId()));
			reciboActo = actoFolioReciboRepository.saveAndFlush( reciboActo );
			reciboActoList.add( reciboActo );
		}
		
		return reciboActoList;
	}
	
	public Recibo saveAndUpdateWithVO( ReciboVO recibo  )
	{
				if(recibo!=null) {
						Recibo nuevoRecibo = new  Recibo();

						System.out.println(recibo);

						nuevoRecibo.setPrelacion(recibo.getPrelacion());
						nuevoRecibo.setFecha(recibo.getFecha());
						nuevoRecibo.setMonto(recibo.getMonto());
						nuevoRecibo.setNoRecibo(recibo.getNoRecibo());
						nuevoRecibo.setCuenta( recibo.getCuenta());
						nuevoRecibo.setReferencia(recibo.getReferencia());
						
						reciboRepository.saveAndFlush(nuevoRecibo);
						
						if(recibo.getReciboConceptosParaRecibo()!=null && recibo.getReciboConceptosParaRecibo().length>0)
						{
							for(ReciboConcepto rc : recibo.getReciboConceptosParaRecibo()) {
										System.out.println("RECIBO ->  "+ rc);	
										rc.setRecibo(nuevoRecibo);
										
										reciboConceptoRepository.saveAndFlush(rc);
							}
									
						}
						
						return nuevoRecibo;
				}else {
					
					return null;
				}
		
				
				
	}	
	

	public List<ReciboVO> findVOByPrelacionId(long prelacionId) {
		List<Recibo> recibos = this.findByPrelacionId(prelacionId);
		List<ReciboVO> listaRVO = new ArrayList<>();
		List<ReciboVO> listaRVOLimpia = new ArrayList<>();
		if (recibos != null && recibos.size() > 0)
			recibos.forEach(rec -> {
				String conceptos = "";
				final ReciboVO rvo = new ReciboVO() {{
					setNoRecibo(rec.getNoRecibo());
					setMonto(rec.getMonto());
					setFecha(rec.getFecha());
					setCuenta(rec.getCuenta());
					setReferencia(rec.getReferencia());

				}};

				if (rec.getReciboConceptosParaRecibos() != null) {

					for (ReciboConcepto conc : rec.getReciboConceptosParaRecibos())
						conceptos += conc.getConceptoPago().getNombre() + ", ";

					rvo.setConcepto(StringUtils.stripEnd(conceptos, ", "));
				}
				listaRVO.add(rvo);
			});

		Map<String, ReciboVO> map = new HashMap<String, ReciboVO>(listaRVO.size());

		for(ReciboVO r : listaRVO) {
			map.put(r.getReferencia(), r);
		}
		for(Entry<String, ReciboVO> r : map.entrySet()) {
			listaRVOLimpia.add(r.getValue());
		}
		return listaRVOLimpia;

	}

	public boolean removeRecibo(Recibo rec)  {
		try {
			actoFolioReciboRepository.delete(rec.getActoFolioRecibosParaRecibos());
			reciboRepository.delete(rec.getId());
		} catch (Exception except) {
			except.printStackTrace();
			return false;
		} finally {
			log.debug("Termina el try-catch y eliminó correctamente");
		}
		return true;
	}


	/*public boolean valida_cuenta(Integer cuenta) {
		
		List<Recibo> recibos = reciboRepository.findByCuenta( cuenta );
		log.debug("El valor de la cuenta internamente es: " + cuenta);
		Long idStatus;
		for( Recibo recibo : recibos ) {
			idStatus = recibo.getPrelacion().getStatus().getId();
			if( idStatus == Constantes.Status.DEVUELTO_A_ANALISTA_CYVF.getIdStatus() ||
				idStatus == Constantes.Status.RECIBIDO_PENDIENTE_TURNAR.getIdStatus() ) {
				return true;
			}
		} 
		
		return !(recibos.size() > 0);
	} */
	public String valida_referencia(String referencia) {
		//La siguiente función va y busca en la DB si se encuentra almacenado gracias al modelo
		List<Recibo> recibos = reciboRepository.findByReferenciaAndPrelacionConsecutivoIsNotNull( referencia);
		int n=0;
		String entradasAsociadas="";
		//Si se encuentra entradas a la referencia las retorna
		for( Recibo recibo : recibos ) {
			if(n==0){
				entradasAsociadas=recibo.getPrelacion().getConsecutivo().toString();
			} else {
				entradasAsociadas=entradasAsociadas+",  "+recibo.getPrelacion().getConsecutivo().toString();
			}			
			if(recibo.getPrelacion().getAnio()!=null){
				entradasAsociadas=entradasAsociadas+"-"+recibo.getPrelacion().getAnio().toString();				
			}
			if(recibo.getPrelacion().getOficina()!=null){
				entradasAsociadas=entradasAsociadas+"-"+recibo.getPrelacion().getOficina().getNombre();
			}
			n++;
		}
		return entradasAsociadas;
	}

	public ReciboVO convertVO(Recibo rec, Acto[] actos) {
		ReciboVO reciboVO = new ReciboVO();
		reciboVO.setId( rec.getId() );
		reciboVO.setNoRecibo( rec.getNoRecibo() );
		reciboVO.setMonto( rec.getMonto() );
		reciboVO.setFecha( rec.getFecha() );
		reciboVO.setCuenta( rec.getCuenta() );
		reciboVO.setReferencia( rec.getReferencia() );
		reciboVO.setReciboConceptosParaRecibo( rec.getReciboConceptosParaRecibos().toArray( new ReciboConcepto[0] ) );
		//reciboVO.setPrelacion( rec.getPrelacion() );
		reciboVO.setActos( actos );
		return reciboVO;
	}
	
	public Recibo UpdateRecibo(Recibo recibo) {
		Recibo reciboO = this.findByReciboId(recibo.getId());
		if(reciboO != null) {
			reciboO.setFecha(recibo.getFecha());
			reciboO.setMonto(recibo.getMonto());
			reciboO.setNoRecibo(recibo.getNoRecibo());
			
			this.saveAndUpdate(reciboO);
		}
		return reciboO;
	}
}