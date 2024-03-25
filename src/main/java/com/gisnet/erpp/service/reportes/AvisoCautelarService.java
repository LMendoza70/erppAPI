package com.gisnet.erpp.service.reportes;

import com.gisnet.erpp.domain.*;
import com.gisnet.erpp.repository.CampoRepository;
import com.gisnet.erpp.service.*;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.UtilFecha;
import com.gisnet.erpp.vo.AvisoCautelarVO;
import com.gisnet.erpp.vo.PredioVO;
import com.gisnet.erpp.vo.prelacion.TitularModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class AvisoCautelarService {


    private static long FECHA_CAUTELAR = 3L;
    private static long NOTARIO = 4L;
    private static long CAMPO_NOMBRE = 286L;
    private static long CAMPO_PRIMER_APELLIDO = 287L;
    private static long CAMPO_SEGUNDO_APELLIDO = 288L;
    private static long EN_CALIDAD = 279L;


    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ActoService actoService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    NotarioService notarioService;

    @Autowired
    PrelacionService prelacionService;

    @Autowired
    CampoRepository campoRepository;

    @Autowired
    PredioService predioService;

    @Autowired
    PrelacionPredioService prelacionPredioService;

    @Autowired
    ActoPredioService actoPredioService;

    @Autowired
    DocumentoService documentoService;

    public AvisoCautelarVO getReporteAvisoCautelar(Long prelacionId) {

        Prelacion prelacion = prelacionService.findOne(prelacionId);

        Acto actoAviso = getActoAviso(prelacion);

        //Acto _rtmp9_ = actoService.findOne(47050L);
        Set<Long> camposId = new HashSet<Long>(Arrays.asList(FECHA_CAUTELAR, NOTARIO, CAMPO_NOMBRE, CAMPO_PRIMER_APELLIDO, CAMPO_SEGUNDO_APELLIDO, EN_CALIDAD));
        Map<Long, String> maps = this.getDatosCampo(actoAviso, camposId);

        Notario notario = getNotario(maps.get(NOTARIO));

        AvisoCautelarVO acvo = new AvisoCautelarVO();
        acvo.setFolio(prelacion.getConsecutivo());
        acvo = setFechaIngreso(acvo, prelacion.getFechaIngreso());
        acvo = setFechaCautelar(acvo, getFechaCautelar((maps.get(FECHA_CAUTELAR))));

        Antecedente antecedente = getAntecedente(prelacion);

        acvo = setAntecedentes(acvo, antecedente);

        acvo.setEscrituraNo(this.getEscritura(actoAviso));

        acvo.setMunicipio(getMunicipio(prelacion));
        acvo.setEstado(getEstado(prelacion));

        acvo.setAdquirientes(getTitulares(actoAviso));

        acvo.setLicenciado(notario != null ? notario.getNombreCompleto() : "");
        acvo.setNoFedatario(notario != null ? notario.getNoNotario().toString() : "");

        acvo.setEnajenante(maps.get(CAMPO_NOMBRE) + " " + maps.get(CAMPO_PRIMER_APELLIDO) + " " + maps.get(CAMPO_SEGUNDO_APELLIDO));
        acvo.setEnCalidad(getEnCalidad(maps.get(EN_CALIDAD)));

        acvo.setReviso(this.getReviso(prelacion));


        return acvo;
    }

    private String getEscritura(Acto actoAviso) {
        String escrituras = "";
            List<Long> prediosId = getPrediosFromActo(actoAviso);

            if (prediosId.size() == 0 || prediosId.size() > 0 && prediosId.get(0) == -1)
                return "";

            Set<ActoDocumento> actoDocs = actoPredioService.findEscrituraByPredioId(prediosId.get(0));

            final List<String> listaEscrituras = actoDocs.stream()
                    .map(adoc -> documentoService.getEscrituraFromActoDocumento(adoc))
                    .filter(str -> str != "")
                    .collect(Collectors.toList());

            escrituras = String.join(", ", listaEscrituras);




        return escrituras;
    }

    private List<Long> getPrediosFromActo(Acto actoAviso) {
        if (!isEmpty(actoAviso.getActoPrediosParaActos())) {

            return actoAviso.getActoPrediosParaActos().stream()
                    .map(x -> isEmpty(x.getPredio()) ? -1 : x.getPredio().getId())
                    .filter(pId -> pId != -1)
                    .distinct()
                    .collect(Collectors.toList());
        }
        else
            return new ArrayList<Long>();
    }

    private AvisoCautelarVO setAntecedentes(AvisoCautelarVO acvo, Antecedente ante) {
        try {
            if (ante != null) {

                if (!isEmpty(ante.getLibro()))
                    acvo.setLibro(ante.getLibro().getNumLibro().toString());
                else
                    acvo.setLibro("");

            }
        } catch (Exception except) {

        }

        return acvo;
    }

    private Antecedente getAntecedente(Prelacion prelacion) {
        Constantes.ETipoFolio tipo = prelacionService.getTipoFolio(prelacion);
        List<PrelacionPredio> pp = prelacionPredioService.findAllPrelacionPredioFromPrelacion(prelacion);
        if (!isEmpty(pp))
            return prelacionPredioService.findAntecedentesByPrelacionPredio(pp.get(0), tipo);

        return null;

    }


    private String getReviso(Prelacion prelacion) {
        String usuarioAnalista = "";
        if (prelacion.getUsuarioAnalista() != null)
            usuarioAnalista = prelacion.getUsuarioAnalista().getNombreCompleto();
        return usuarioAnalista;
    }

    private Acto getActoAviso(Prelacion prelacion) {
        Set<Acto> actos = prelacion.getActosParaPrelacions();
        Acto actoAviso = null;
        for (Acto acto : actos) {

            if (acto.getTipoActo().getId().longValue() == Constantes.TIPO_ACTO_CERTIFICADO_LG_CAUTELAR_INSERTO.longValue())
                actoAviso = acto;
        }

        return actoAviso;
    }

    private AvisoCautelarVO setFechaIngreso(AvisoCautelarVO acvo, Date fecha) {
        Locale espanol = new Locale("es", "ES");

        DateFormat diaNumero = new SimpleDateFormat("dd");
        DateFormat mesLetra = new SimpleDateFormat("MMMM", espanol);
        DateFormat anio = new SimpleDateFormat("yyyy");

        acvo.setDiaRegistro(diaNumero.format(fecha));
        acvo.setMesLetraRegistro(mesLetra.format(fecha));
        acvo.setAnioRegistro(anio.format(fecha));

        acvo.setFecha(fecha);

        return acvo;
    }

    private AvisoCautelarVO setFechaCautelar(AvisoCautelarVO acvo, Date fecha) {
        Locale espanol = new Locale("es", "ES");

        DateFormat diaNumero = new SimpleDateFormat("dd");
        DateFormat mesLetra = new SimpleDateFormat("MMMM", espanol);
        DateFormat anio = new SimpleDateFormat("yyyy");
        acvo.setDiaNumero(diaNumero.format(fecha));
        acvo.setMesLetra(mesLetra.format(fecha));
        acvo.setAnio(anio.format(fecha));

        acvo.setFecha(fecha);

        return acvo;

    }

    private String getEnCalidad(String s) {
        Campo campo = campoRepository.findOne(Long.valueOf(s));
        return campo.getNombre();
    }

    private Notario getNotario(String strNotario) {

        if (!isEmpty(strNotario))
            return notarioService.findOne(Long.valueOf(strNotario));
        else
            return null;

    }

    private Date getFechaCautelar(String s) {
        return UtilFecha.toDateTime(s, "yyyy-MM-dd");
    }

    private String getMunicipio(Prelacion prelacion) {
        if (!isEmpty(prelacion.getMunicipio()))
            return prelacion.getMunicipio().getNombre();
        else
            return "";
    }

    private String getEstado(Prelacion prelacion) {
        if (!isEmpty(prelacion.getMunicipio()) && !isEmpty(prelacion.getMunicipio().getEstado()))
            return prelacion.getMunicipio().getEstado().getNombre();
        else
            return "";
    }

    private List<TitularModel> getTitulares(Acto acto) {

        List<Long> prediosId = getPrediosFromActo(acto);

        if (isEmpty(prediosId))
            return new ArrayList<TitularModel>() ;

        Predio predio = predioService.findOne(prediosId.get(0));

        List<TitularModel> titulares = new ArrayList<>();

        if (!isEmpty(predio)) {
            Set<PredioPersona> actoDocs = actoPredioService.findPersonasbyPredioId(predio.getId());
            if (!isEmpty(actoDocs)) {

                titulares = actoDocs.stream()
                        //.filter(ad -> ad.getTipoRelPersona().getNombre().contains("TITULAR"))
                        //.distinct()
                        .map(this::convertToTitularModel)
                        .collect(Collectors.toList());
            }
        }

        return titulares;

    }


    private HashMap<Long, String> getDatosCampo(Acto acto, Set<Long> camposId) {
        HashMap<Long, String> map = new HashMap<>();

        if (isEmpty(acto))
            return map;

        for (ActoPredio ap : acto.getActoPrediosParaActos()) {
            Set<ActoPredioCampo> actoPredioCampos = ap.getActoPredioCamposParaActoPredios();
            for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                long tempCampoId = actoPredioCampo.getModuloCampo().getCampo().getId();
                if (camposId.contains(tempCampoId))
                    map.put(tempCampoId, actoPredioCampo.getValor());
            }
        }

        return map;
    }


    private TitularModel convertToTitularModel(PredioPersona predioPersona) {
        TitularModel titularModel = new TitularModel();
        titularModel.setNombre(predioPersona.getPersona().getNombre() + " " + predioPersona.getPersona().getPaterno() + " " + predioPersona.getPersona().getMaterno());
        titularModel.setDd(predioPersona.getPorcentajeDd().doubleValue());
        titularModel.setUv(predioPersona.getPorcentajeUv().doubleValue());

        //titularModel.setTipo(predioPersona.getTipoRelPersona().getNombre());

        return titularModel;
    }

}