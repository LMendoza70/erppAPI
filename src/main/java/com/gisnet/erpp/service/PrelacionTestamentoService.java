package com.gisnet.erpp.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.PrelacionTestamento;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.repository.PrelacionTestamentoRepository;


@Service
public class PrelacionTestamentoService {

	@Autowired
	private PrelacionTestamentoRepository prelacionTestamentoRepository;

  /**
   *
   * @param prelacionTestamento
   */
  public PrelacionTestamento saveAndUpdate(PrelacionTestamento prelacionTestamento) {
    prelacionTestamentoRepository.saveAndFlush(prelacionTestamento);
    return prelacionTestamento;
  }

}
