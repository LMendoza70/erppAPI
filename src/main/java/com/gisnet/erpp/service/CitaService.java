package com.gisnet.erpp.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Cita;
import com.gisnet.erpp.domain.DiaInhabil;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.repository.CitaRepository;
import com.gisnet.erpp.repository.DiaInhabilRepository;
import java.text.SimpleDateFormat;
import java.text.DateFormat;


@Service
public class CitaService {

	@Autowired
	private CitaRepository citaRepository;
	@Autowired
	private DiaInhabilRepository diaInhabilRepository;

  public List<Cita> buscarCitaByFechaAndOficina(Cita cita) {
    return citaRepository.buscarCitaByFechaAndOficina(cita.getFecha(),cita.getOficina().getId());
  }

  public Cita findCitaByPrelacion(Long idPrelacion){
  	return citaRepository.findByPrelacionId(idPrelacion);
  }

	public boolean findDiasInhabilesByDate(Cita cita) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String fechaString = df.format(cita.getFecha());
		List<DiaInhabil> diasInhabiles=diaInhabilRepository.findDiasInhabilesByDate(fechaString);
		if(diasInhabiles.size() > 0){
			return true;
		}
		return false;
	}

  /**
   *
   * @param cita
   */
  public Cita saveAndUpdate(Cita cita) {
    citaRepository.saveAndFlush(cita);
    return cita;
  }

}
