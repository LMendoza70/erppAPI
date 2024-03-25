package com.gisnet.erpp.service;

import com.gisnet.erpp.domain.*;
import com.gisnet.erpp.repository.*;
import com.gisnet.erpp.vo.BusquedaPersonaVO;
import com.gisnet.erpp.vo.BusquedaPredioVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DetalleBusquedaPredioService {


    @Autowired
    private DetalleBusquedaRepository detalleBusquedaRepository;
    @Autowired
    private BusquedaRepository        busquedaRepository;

    @Autowired private TipoVialidadRepository tipoVialidadRepository;
    @Autowired private NivelRepository nivelRepository;
    @Autowired private TipoAsentRepository tipoAsentRepository;
    @Autowired private EstadoRepository estadoRepository;
    @Autowired private MunicipioRepository municipioRepository;
    @Autowired private UnidadMedidaRepository unidadMedidaRepository;
    @Autowired private UsoSueloRepository usoSueloRepository;
    @Autowired private TipoInmuebleRepository tipoInmuebleRepository;

    @Autowired private EnlaceVialidadRepository enlaceVialidadRepository;
    @Autowired private FracOCondoRepository fracOCondoRepository;
    @Autowired private EtapaOSeccionRepository etapaOSeccionRepository;
    @Autowired private OrientacionRepository orientacionRepository;
    private final Logger log = LoggerFactory.getLogger(this.getClass());


    private boolean isEmpty(String str) {
        return str == null || str == "";
    }

    public BusquedaPredioVO getPredioModelFromDetalles(HashSet<DetalleBusqueda> properties) {
        BusquedaPredioVO        predio   = new BusquedaPredioVO();
        HashMap<String, String> linderos = new HashMap<>();
        for (DetalleBusqueda tupla: properties) {

            if (isEmpty(tupla.getValor()) )
                continue;

            switch (tupla.getClave()) {
                case "tipoVialidad"             : predio.setTipoVialidad ( tipoVialidadRepository.findOne( Long.valueOf(tupla.getValor()) )); break;
                case "vialidad"                 : predio.setVialidad( tupla.getValor() ); break;
                case "numExt"                   : predio.setNumExt( tupla.getValor() ); break;
                case "numInt"                   : predio.setNumInt( tupla.getValor() ); break;
                case "edificio"                 : predio.setEdificio( tupla.getValor() ); break;
                case "nivel"                    : predio.setNivel(  nivelRepository.findOne( Long.valueOf(tupla.getValor()) )); break;
                case "tipoAsent"                : predio.setTipoAsent( tipoAsentRepository.findOne( Long.valueOf(tupla.getValor()) ) ); break;
                case "asentamiento"             : predio.setAsentamiento( tupla.getValor() ); break;
                case "estado"                   : predio.setEstado(       estadoRepository.findOne( Long.valueOf(tupla.getValor()) ) ); break;
                case "municipio"                : predio.setMunicipio( municipioRepository.findOne( Long.valueOf(tupla.getValor()) ) ); break;
                case "codigoPostal"             : predio.setCodigoPostal( tupla.getValor() ); break;
                case "claveCatastral"           : predio.setClaveCatastral( tupla.getValor() ); break;
                case "cuentaCatastral"          : predio.setCuentaCatastral( tupla.getValor() ); break;
                case "claveCatastralEstandard"  : predio.setClaveCatastralEstandard( tupla.getValor() ); break;
                case "noLote"                   : predio.setNoLote( tupla.getValor() ); break;
                case "manzana"                  : predio.setManzana( tupla.getValor() ); break;
                case "zona"                     : predio.setZona( tupla.getValor() ); break;
                case "superficie"               : predio.setSuperficie( tupla.getValor() ); break;
                case "unidadMedida"             : predio.setUnidadMedida(  unidadMedidaRepository.findOne( Long.valueOf(tupla.getValor()) )); break;
                case "usoSuelo"                 : predio.setUsoSuelo( usoSueloRepository.findOne( Long.valueOf(tupla.getValor()) )) ; break;
                case "tipoInmueble"             : predio.setTipoInmueble(  tipoInmuebleRepository.findOne( Long.valueOf(tupla.getValor()) )); break;
                case "variante-municipio"       : predio.setVarianteMunicipio(municipioRepository.findOne( Long.valueOf(tupla.getValor()) )); break;
                case "variante-tipoAsent"       : predio.setVarianteTipoAsent(tipoAsentRepository.findOne( Long.valueOf(tupla.getValor()) )); break;

                case "enlaceVialidad"           : predio.setEnlaceVialidad( enlaceVialidadRepository.findOne( Long.valueOf(tupla.getValor())) ); break;
                case "fracOCondo"               : predio.setFracOCondo( fracOCondoRepository.findOne( Long.valueOf(tupla.getValor())) ); break;
                case "nombreFracOCondo"         : predio.setNombreFracOCondo(tupla.getValor()); break;
                case "etapaOSeccion"            : predio.setEtapaOSeccion( etapaOSeccionRepository.findOne( Long.valueOf(tupla.getValor())) ); break;
                case "tipoVialidad2"            : predio.setTipoVialidad2( tipoVialidadRepository.findOne( Long.valueOf(tupla.getValor())) ); break;
                case "vialidad2"                : predio.setVialidad2(tupla.getValor()); break;
                case "numExt2"                  : predio.setNumExt2(tupla.getValor()); break;
                case "fraccion"                 : predio.setFraccion(tupla.getValor()); break;
                case "macroManzana"             : predio.setMacroManzana(tupla.getValor()); break;
                case "localidadSector"          : predio.setLocalidadSector(tupla.getValor()); break;

                default :
                    log.debug("Tupla no definida: {}", tupla);
                    String lindero = extraerLinderoDetalle (tupla.getClave() );
                    if ( ! isEmpty(lindero) ) {
                        Orientacion lind = orientacionRepository.findOneByNombre(lindero);
                        linderos.put(lind.getId().toString(), tupla.getValor());
                    }
            }

        }

        predio.setLinderos(linderos);

        return predio;
    }

    private String extraerLinderoDetalle (String tupla) {
        log.debug("Revisando tupla: {}", tupla);
        if (tupla.startsWith("lind-")) {
            log.debug("Lindero encontrado: {}", tupla.substring(5));
            return tupla.substring(5);
        }

        return "";
    }

}


