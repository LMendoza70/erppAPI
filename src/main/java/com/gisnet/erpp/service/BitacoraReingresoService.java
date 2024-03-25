package com.gisnet.erpp.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.gisnet.erpp.domain.BitacoraReingreso;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.repository.BitacoraReingresoRepository;


@Service
public class BitacoraReingresoService {

    @Autowired
    private BitacoraReingresoRepository bitacoraReingresoRepository;
    
    public List<BitacoraReingreso>findBitacoraReingresoByprelacion (Prelacion prelacion){
        return bitacoraReingresoRepository.findByPrelacionIdOrderByFechaDesc(prelacion.getId());

    }
    
}