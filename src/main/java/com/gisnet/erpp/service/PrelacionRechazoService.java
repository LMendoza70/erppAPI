package com.gisnet.erpp.service;

import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.PrelacionRepository;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.util.Constantes;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class PrelacionRechazoService {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    PrelacionRepository prelacionRepository;

    @Transactional(readOnly = true)
    public Page<Prelacion> findAllRechazadosByCriteria(
            Long tipo,
            Long folio,
            String fechaIngreso,
            String fechaEnvioFirma,
            Long usSolicitanteId,
            Long notarioId,
            Long calificadorId,
            Pageable pageable,
            Integer estado,
            Integer tipoUsuarioSeleccionado
    ) {

        Date fechaI=null, fechaEF=null, fechaB= null;

        try {
            if (fechaIngreso != null && fechaIngreso != "")
                fechaI = DateTime.parse(fechaIngreso).toDate();
            if (fechaEnvioFirma != null && fechaEnvioFirma != "")
                fechaEF = DateTime.parse(fechaEnvioFirma).toDate();
        }
        catch (Exception except) {
            return null;
        }

        Usuario usuario = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());

        Usuario solicitante = usSolicitanteId == null ? null: usuarioService.findOne(usSolicitanteId);
        Usuario notario = notarioId == null ? null: usuarioService.findOne(notarioId);
        Usuario calificador= calificadorId == null ? null: usuarioService.findOne(calificadorId);


        //return prelacionRepository.findAllByActosRechazados(usuario, tipo, folio, fechaI, fechaEF, solicitante, notario, calificador, pageable);\
        return null;
    }

}
