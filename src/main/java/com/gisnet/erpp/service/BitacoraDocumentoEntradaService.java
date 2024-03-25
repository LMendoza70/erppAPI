package com.gisnet.erpp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import com.gisnet.erpp.repository.BitacoraDocumentoEntradaRepository;

import com.gisnet.erpp.domain.BitacoraDocumentoEntrada;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.Documento;
import com.gisnet.erpp.domain.Archivo;



@Service
public class BitacoraDocumentoEntradaService {
    @Autowired
    BitacoraDocumentoEntradaRepository bitacoraDocumentoEntradaRepository ; 

    BitacoraDocumentoEntrada crearBitacora(Prelacion p, Usuario u,String accion,Documento d, Archivo archivo,String observaciones,Boolean es_anexo){

        BitacoraDocumentoEntrada bitacoraDocumentoEntrada = new BitacoraDocumentoEntrada();
        bitacoraDocumentoEntrada.setFecha(new Date());
        bitacoraDocumentoEntrada.setPrelacion(p);
        bitacoraDocumentoEntrada.setUsuario(u);
        bitacoraDocumentoEntrada.setAccion(accion);
        bitacoraDocumentoEntrada.setDocumento(d);
        bitacoraDocumentoEntrada.setArchivo(archivo);
        if(observaciones!=null) {
        	bitacoraDocumentoEntrada.setObservaciones(observaciones);
        } else {
        	bitacoraDocumentoEntrada.setObservaciones(null);
        }
        bitacoraDocumentoEntrada.setEs_anexo(es_anexo);
        return bitacoraDocumentoEntradaRepository.save(bitacoraDocumentoEntrada);
    }

    List<BitacoraDocumentoEntrada> findAllByPrelacionId(Long prelacionId){
        List<BitacoraDocumentoEntrada> listBde = new ArrayList<BitacoraDocumentoEntrada>();
            listBde = bitacoraDocumentoEntradaRepository.findAllByPrelacionId(prelacionId);
        return listBde;
    }


    


}