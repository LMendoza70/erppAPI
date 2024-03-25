package com.gisnet.erpp.service;

import com.gisnet.erpp.domain.Prioridad;
import com.gisnet.erpp.repository.PrioridadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PrioridadService {
    @Autowired
    PrioridadRepository prioridadRepository;


    @Transactional(readOnly = true)
    public Prioridad findOne(Long prioridadId) {
        return prioridadRepository.findOne(prioridadId);
    }

    @Transactional(readOnly = true)
    public List<Prioridad> findAll() {
        return prioridadRepository.findAll();
    }
}
