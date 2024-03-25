package com.gisnet.erpp.service;

import com.gisnet.erpp.domain.*;
import com.gisnet.erpp.repository.*;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.AntecedenteVO;
import com.gisnet.erpp.vo.Copias.CopiaVO;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.InputStream;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import javax.print.Doc;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class CopiasService {
    private static final Logger log = LoggerFactory.getLogger(CopiasService.class);

    @Autowired
    FoliosrDigRepository foliosrDigRepository;

    @Autowired
    FoliosrDigService foliosrDigService;

    @Autowired
    PdfService pdfService;

    @Autowired
    PredioService predioService;
    @Autowired
    PersonaJuridicaService personaJuridicaService;
    @Autowired
    PersonaAuxiliarService personaAuxiliarService;
    @Autowired
    MuebleService muebleService;
    @Autowired
    ActoService actoService;
    @Autowired
    ActoPredioService actoPredioService;

    @Autowired
    DocumentoService documentoService;

    @Autowired
    AntecedenteService antecedenteService;

    @Autowired
    BusquedaResultadoService busquedaResultadoService;

    @Autowired
    BusquedaResultadoRepository busquedaResultadoRepository;

    @Autowired
    private PredioRepository predioRepository;
    @Autowired
    private MuebleRepository muebleRepository;
    @Autowired
    private FolioSeccionAuxiliarRepository folioSeccionAuxiliarRepository;
    @Autowired
    private PersonaJuridicaRepository personaJuridicaRepository;
    @Autowired
    private LibroService libroService;

    @Autowired
    private AntecedenteRepository antecedenteRepository;

    @Autowired
    CertificadoService certificadoService;
    
    @Autowired
    private PrelacionService prelacionService;


    public List<FoliosrDigital> getSimpleFromFolio() {
        Long tipoFolioId = 4L;
        Integer folio = 2049935;
        String escritura  = "11571";
        Long oficinaId = 1L; // guadalajara
        long [] actosId = new long[1];
        actosId[0] = 50;

        return foliosrDigService.findByTipoFolioIdAndNumFolioRegistral(tipoFolioId,folio );


    }


    public List<Object> getSimpleFromDocumento() {
        String tipoLibro = "";
        Integer libro = 2050513;
        String libroBis = "0";  // 0: unico, 1: bis
        String escritura  = "11571";
        String oficinaId = "1"; // guadalajara
        String seccionId = "1"; // guadalajara
        String doc = "1"; // guadalajara
        String docbis = "1"; //

        Boolean tipoDoc = true; // documento / inscripcion

        Antecedente ante = antecedenteService.findByDatosAntecedente(doc, docbis, libro, libroBis, seccionId, oficinaId );

        foliosrDigService.findByTipoFolioIdAndNumFolioRegistral(4L,2050513 );

        return null;
    }


    /**
     * Método que busca los datos del DTO en los datos históricos de predios
     * considerando Lista de tipos de Actos y Escrituras
     * @param copiaVO
     * @return List<FoliosrDigital>
     */
    public List <FoliosrDigital> findHistoricoByCopiasByModel (CopiaVO copiaVO) {
        TipoFolio   tipoFolio = copiaVO.getTipoFolio();
        Integer     folio     = copiaVO.getFolioReal();
        String      escritura = copiaVO.getEscritura();
        Oficina     oficina   = copiaVO.getOficina();
        List <TipoActo> actos   = null;

        actos = copiaVO.getActos();

        List <FoliosrDigital> listaResultado = new ArrayList<>();
        try {
            if (isEmpty(escritura) && isEmpty(actos))
                return foliosrDigRepository.findAllByNumFolioRegistralAndTipoFolioAndOficina(folio, tipoFolio, oficina);

            if (isEmpty(actos))
                return foliosrDigRepository.findAllByNumFolioRegistralAndTipoFolioAndOficinaAndEscritura(folio, tipoFolio, oficina, escritura);

            if (isEmpty(escritura)) {

                for (TipoActo acto : actos) {
                    List<FoliosrDigital> tmpRes = foliosrDigRepository.findAllByNumFolioRegistralAndTipoFolioAndOficinaAndTipoActo(folio, tipoFolio, oficina, acto);

                    // Tienen que encontrarse todos los actos, de lo contrario
                    if (!isEmpty( tmpRes ))
                        listaResultado.addAll( tmpRes );
                    else {
                        log.info("Copias: No se encontraron todos los tipos de Actos {} - {}", acto.getId(), acto.getNombre());
                        listaResultado.clear();
                        break;
                    }

                }

                return listaResultado;
            }

            for (TipoActo acto : actos) {
                List<FoliosrDigital> tmpRes = foliosrDigRepository.findAllByNumFolioRegistralAndTipoFolioAndOficinaAndEscrituraAndTipoActo(folio, tipoFolio, oficina, escritura, acto);

                // Tienen que encontrarse todos los actos, de lo contrario
                if (!isEmpty( tmpRes ))
                    listaResultado.addAll( tmpRes );
                else {
                    log.info("Copias: No se encontraron todos los tipos de Actos {} - {}", acto.getId(), acto.getNombre());
                    listaResultado.clear();
                    break;
                }

            }

            return listaResultado;

        }
        catch (Exception except) {
            Log.error(except);
            return null;
        }

    }

    public List <CopiaVO> findFoliosByCopiasModel (CopiaVO copiaVO) {
        TipoFolio   tipoFolio = copiaVO.getTipoFolio();
        Integer     folio     = copiaVO.getFolioReal();
        Oficina     oficina   = copiaVO.getOficina();
        Integer     escritura = null;
        List <TipoActo> actos   = null;

        escritura = getIntegerFromString (copiaVO.getEscritura());
        actos = copiaVO.getActos();
        List <Object> listaResultado = new ArrayList<>();
        try {
            Constantes.ETipoFolio etipo = Constantes.ETipoFolio.fromLong(tipoFolio.getId().longValue());

            assert etipo != null;
            switch (etipo) {
                case PERSONAS_JURIDICAS:
                    /*PersonaJuridica pj;
                    if (isEmpty(escritura) && isEmpty(actos)) {
                        pj = personaJuridicaRepository.findByNoFolioAndOficinaId(folio, oficina.getId());
                        listaResultado.add(pj);
                        return listaResultado;
                    }

                    if (isEmpty(actos)) {
                        pj = personaJuridicaRepository.findByNoFolioAndOficinaId(folio, oficina.getId());
                        // Find escritura
                        if (true == true)
                            listaResultado.add(pj);

                        return listaResultado;
                    }

                    if (isEmpty(escritura)) {


                        pj = personaJuridicaRepository.findByNoFolioAndOficinaId(folio,  oficina.getId());
                        List<Acto> actosPj =  actoService.findByActoPrediosParaActosPersonaJuridicaId(pj.getId());
                        actosContienenTiposActo(actos, listaResultado, pj, actosPj);
                        return listaResultado;

                    }

                    // Estan todos los campos del form
                    pj = personaJuridicaRepository.findByNoFolioAndOficinaId(folio,  oficina.getId());
                    List<Acto> actosPj =  actoService.findByActoPrediosParaActosPersonaJuridicaId(pj.getId());
                    if (!isEmpty(actosPj)) {
                        Set<TipoActo> listTipoActo = actosPj.stream().map(Acto::getTipoActo).collect(Collectors.toSet());
                        boolean actosValidos = true;
                        for (TipoActo tipoActo: actos) {
                            if (listTipoActo.contains(tipoActo))
                                continue;
                            else {
                                log.info("Copias: No se encontraron todos los tipos de Actos {} - {}", tipoActo.getId(), tipoActo.getNombre());
                                actosValidos = false;
                                break;
                            }
                        }
                        boolean escrituraEncontrada = false;

                        Set<Documento> docs = actoPredioService.findDocumentoByPredioAndEscritura(pj, escritura);
                        if (!isEmpty(docs) ) {
                                escrituraEncontrada = true;
                        }

                        if(actosValidos && escrituraEncontrada)
                            listaResultado.add(pj);
                    }
                    else {
                        log.info("Copias: No se encontraron Actos para la persona juridica con los Tipos de Actos especificados ");
                    }
                    return listaResultado;
                */

                    break;
                case PERSONAS_AUXILIARES:
                    break;
                case MUEBLE:
                    break;
                case PREDIO:
                    Predio predio;
                    Integer finalEscritura = escritura;
                    List<TipoActo> finalActos = actos;
                    if (isEmpty(escritura) && isEmpty(actos)) {
                        predio = predioRepository.findByNoFolioAndOficinaId(folio, oficina.getId());
                        listaResultado.add(predio);

                        return listaResultado.stream().map(pre -> this.transformPredioToCopiaVo ((Predio) pre, tipoFolio, finalEscritura, finalActos) ).collect(Collectors.toList());
                    }

                    if (isEmpty(actos)) {
                        predio = predioRepository.findByNoFolioAndOficinaId(folio, oficina.getId());

                        boolean escrituraEncontrada = false;
                        // Find escritura
                        Set<Documento> docs = actoPredioService.findDocumentoByPredioAndEscritura(predio, escritura);
                        if (!isEmpty(docs) ) {
                            escrituraEncontrada = true;
                            listaResultado.add(predio);
                        }

                        return listaResultado.stream().map(pre -> this.transformPredioToCopiaVo ((Predio) pre, tipoFolio, finalEscritura, finalActos) ).collect(Collectors.toList());                    }

                    if (isEmpty(escritura)) {
                        predio = predioRepository.findByNoFolioAndOficinaId(folio, oficina.getId());

                        List<Acto> actosPredio =  actoService.findByActoPrediosParaActosPredioId(predio.getId());
                        if (actosContienenTiposActo (actosPredio, actos))
                            listaResultado.add(predio);

                        return listaResultado.stream().map(pre -> this.transformPredioToCopiaVo ((Predio) pre, tipoFolio, finalEscritura, finalActos) ).collect(Collectors.toList());

                    }

                    // Estan todos los campos del form
                    predio = predioRepository.findByNoFolioAndOficinaId(folio, oficina.getId());
                    boolean actosValidos = false;
                    boolean escrituraEncontrada = false;

                    List<Acto> actosPredio =  actoService.findByActoPrediosParaActosPredioId(predio.getId());

                    if (actosContienenTiposActo (actosPredio, actos))
                       actosValidos = true;

                    Set<Documento> docs = actoPredioService.findDocumentoByPredioAndEscritura(predio, escritura);
                    if (!isEmpty(docs) ) {
                       escrituraEncontrada = true;
                    }

                    if(actosValidos && escrituraEncontrada)
                        listaResultado.add(predio);


                    return listaResultado.stream().map(pre -> this.transformPredioToCopiaVo ((Predio) pre, tipoFolio, finalEscritura, finalActos) ).collect(Collectors.toList());
            }

            return null;

        }
        catch (Exception except) {
            Log.error(except);
            return null;
        }
    }

    private CopiaVO transformPredioToCopiaVo(Predio pre, TipoFolio tipo, Integer escritura, List<TipoActo> actos) {
        CopiaVO copia = new CopiaVO();
                copia.setId(pre.getId());
                copia.setFolioReal(pre.getNoFolio());
                copia.setOficina(pre.getOficina());
                copia.setTipoFolio(tipo);
                if (!isEmpty(escritura))
                    copia.setEscritura(escritura.toString());
                copia.setActos(actos);

       return copia;
    }



    private Integer getIntegerFromString(String escritura) {
        try {
            if (!isEmpty(escritura))
            return Integer.parseInt(escritura);
        } catch (NumberFormatException e) {

        }
        return null;
    }

    private boolean actosContienenTiposActo(List<Acto> actosPredio, List<TipoActo> actos) {
        boolean       actosValidos = false;

        if (!isEmpty(actosPredio)) {
            Set<TipoActo> listTipoActo = actosPredio.stream().map(Acto::getTipoActo).collect(Collectors.toSet());
            for (TipoActo tipoActo: actos) {
                if (listTipoActo.contains(tipoActo)) {
                    actosValidos = true;
                    break;
                }
            }
        }
        else {
            log.info("Copias: No se encontraron Actos para la persona juridica con los Tipos de Actos especificados ");
        }

        return actosValidos;
    }

    /**
     * Guarda el resultado de una busqueda para copias basado en datos históricos ({@link FoliosrDigital})
     * @param copiaVO
     * @param prelacion
     * @return
     */
    public List<BusquedaResultado> guardarResultadoHistoricoCopias (List<CopiaVO> copiaVO, Prelacion prelacion){
        List<BusquedaResultado> listaResbus = new ArrayList<BusquedaResultado>() ;
        BusquedaResultado resBus;
        for (CopiaVO copia : copiaVO) {
            resBus = this.createFromCopiasVO (copia);
            resBus.setFolio(copia.getFolioReal());
            resBus.setEscritura(copia.getEscritura());
            resBus.setEsImpresion(false);

            if (!isEmpty(copia.getActos()))

            resBus.setListaTiposActo(getBusquedaResultadoTipoActo (copia));

            resBus.setPrelacion(prelacion);

            listaResbus.add(busquedaResultadoService.guardar(resBus) );

        }

        return listaResbus;

    }

    /**
     * Crea un objeto para relación BusquedaResultado con Tipos de Acto para posteriro Almacenamiento
     * @param copia CopiaVO
     * @return
     */
    private Set<BusquedaResultadoTipoActo> getBusquedaResultadoTipoActo(CopiaVO copia) {

        Set<TipoActo> tipoActoSet = new HashSet<TipoActo>(copia.getActos());
        Set<BusquedaResultadoTipoActo> tmp = copia.getActos().stream()
                .map(tipoActo -> {
                    BusquedaResultadoTipoActo br = new BusquedaResultadoTipoActo();
                    br.setTipoActo(tipoActo);
                    return br;
                }).collect(Collectors.toSet());

        return tmp;
    }

    /**
     * Crea Objeto iniciar para BusquedaResultado a partir de CopiaVO y determina el tipo de Folio inicial a crear.
     * @param copia
     * @return
     */
    private BusquedaResultado createFromCopiasVO(CopiaVO copia) {
        BusquedaResultado res = new BusquedaResultado();
        res.setFoliosrDigital(copia.getFoliosrDigital());
        return res;
    }

    public Object findFoliosParaImpresionByAntecedente(CopiaVO copiaVO) {

        Libro     libro     = libroService.findByNumeroLibro( copiaVO.getLibro());
        String     libroBis     = copiaVO.getLibroBis();
        Seccion seccion = copiaVO.getSeccion ();
        Oficina     oficina   = copiaVO.getOficina();

        Boolean esDocumento = copiaVO.getEsDocumento ();
        String noDocumento = copiaVO.getNoDocumento ();

        String  tipoDocumento = copiaVO.getDocumentoBis();

        AntecedenteVO anteVO = new AntecedenteVO() {{
            setLibro(copiaVO.getLibro());
           setNumero(copiaVO.getLibro().toString());
           setDocumento(noDocumento);
           setDocumentoBis(tipoDocumento);
           setLibroBis(libroBis);
           setSeccion(seccion.getId().toString());
           setOficina(oficina.getId().toString());
           setTipoDoc(esDocumento);
        }};



        Constantes.ETipoFolio etipo;
        for (Integer i=1; i <= 4; i++) {
            anteVO.setTipoBusqueda(i.toString());
            etipo = Constantes.ETipoFolio.fromLong(i.longValue());
            Object ante = libroService.findOneLibroFromVentanilla(anteVO);
            if (!isEmpty(ante)) {
                BusquedaResultado res = new BusquedaResultado();


                try {
                    switch (etipo) {
                        case PERSONAS_JURIDICAS:
                            PjAnte pjAnte = (PjAnte) ante;
                            res = new BusquedaResultado();

                            res.setId(pjAnte.getId());
                            res.setPersonaJuridica(pjAnte.getPersonaJuridica());
                            res.setFolio(pjAnte.getPersonaJuridica().getNoFolio());
                            res.setEsImpresion(true);

                            return res;

                        case PERSONAS_AUXILIARES:
                            AuxiliarAnte auxAnte = (AuxiliarAnte) ante;
                            res = new BusquedaResultado();

                            res.setId(auxAnte.getId());
                            res.setFolioSeccionAuxiliar(auxAnte.getFolioSeccionAuxiliar());
                            res.setFolio(auxAnte.getFolioSeccionAuxiliar().getNoFolio());
                            res.setEsImpresion(true);

                            return res;

                        case MUEBLE:
                            MuebleAnte mueAnte = (MuebleAnte) ante;
                            res = new BusquedaResultado();

                            res.setId(mueAnte.getId());
                            res.setMueble(mueAnte.getMueble());
                            res.setFolio(mueAnte.getMueble().getNoFolio());
                            res.setEsImpresion(true);

                            return res;

                        case PREDIO:
                            PredioAnte preAnte = (PredioAnte) ante;
                            res = new BusquedaResultado();

                            res.setId(preAnte.getId());
                            res.setPredio(preAnte.getPredio());
                            res.setFolio(preAnte.getPredio().getNoFolio());
                            res.setEsImpresion(true);

                            return res;
                    }



                } catch (Exception except) {
                    Log.error(except);
                    return null;
                }
            }
        }

        return null;


    }

    private Constantes.ETipoFolio getTipoFromAntecedente(Object ante) {

        String className = ante.getClass().getName();
        switch (className) {
            case "PjAnte":
                return Constantes.ETipoFolio.PERSONAS_JURIDICAS;
            case "AuxiliarAnte":
                return Constantes.ETipoFolio.PERSONAS_AUXILIARES;
            case "Mueble":
                return Constantes.ETipoFolio.MUEBLE;
            case "Predio":
                return Constantes.ETipoFolio.PREDIO;
            default:
                return null;
        }
    }

    public Antecedente findByDatosAntecedente(String doc, String docbis,Integer libro, String librobis,String seccion, String oficina){

        return	antecedenteRepository.findByDatosAntecedente(doc, docbis, libro, librobis, seccion, oficina);

    }

    public Boolean guardaHojasSeleccionadas(Long resultadoBusquedaId, List<String> seleccionadas ){


        BusquedaResultado  busqueda  =  busquedaResultadoRepository.findOneById(resultadoBusquedaId);

        if(busqueda!=null){

            String cadenaHS= String.join(",",seleccionadas);
            if(cadenaHS!=null){
                busqueda.setHojasSeleccionadas(cadenaHS);
                busquedaResultadoRepository.save(busqueda);

                return true;
            }else{
                return false;
            }   
        }else{
            return false;
        }    
    }

    //IHM METODO AGREGADO
    public byte[] getImagesBytesSeleccionadasByPrelacion(Long prelacionId){
    	byte[] hojas = null;
    	byte[] caratula = null;
        byte[] nuevo = null;
        System.out.println("la caratula copia es " + caratula);

        BusquedaResultado  busqueda  =  busquedaResultadoRepository.findOneByPrelacionId(prelacionId);
        if(busqueda.getPrelacionHistorica()!=null) { 
        	hojas=prelacionService.getImagesConsultaBySeleccionadasByEntradaOrFolio(busqueda);
        	caratula= caratulaCopiaEntrada(busqueda);
        }else if(busqueda.getPredio()!=null || busqueda.getPersonaJuridica()!=null){
        	hojas = prelacionService.getImagesConsultaBySeleccionadasByFolio(busqueda);
        	caratula= caratulaCopiaEntrada(busqueda);
        }else {
        	hojas = foliosrDigService.getImagesConsultaBySeleccionadas(busqueda.getLibro(),busqueda.getDocumento(),getHojasSeleccionadas(busqueda.getId()));
        	caratula = caratulaCopia(busqueda.getPrelacion().getOficina().getNombre(),busqueda.getLibro().getAnio(), busqueda.getLibro().getNumLibro(),busqueda.getLibro().getLibroBis(),busqueda.getLibro().getSeccionesPorOficina().getSeccion().getNombre(),busqueda.getLibro().getVolumen(),busqueda.getDocumento(),busqueda.getPrelacion().getConsecutivo());
        }        
       
        
        try {
            nuevo= pdfService.appendPDF(caratula,hojas); 
        } catch (Exception e) {           
           System.out.println("Error:"+e.getMessage());
        } 
        
        return nuevo;
    }

    public byte[] getImagesBytesSeleccionadas(Long resultadoBusquedaId){
        
        BusquedaResultado  busqueda  =  busquedaResultadoRepository.findOneById(resultadoBusquedaId);
        
        //IHM CAMBIAR 
        byte[] hojas = foliosrDigService.getImagesConsultaBySeleccionadas(busqueda.getLibro(),busqueda.getDocumento(),getHojasSeleccionadas(resultadoBusquedaId));
        
       // byte[] caratula = caratulaCopia("--",busqueda.getLibro().toString());
       byte[] caratula = caratulaCopia(busqueda.getPrelacion().getOficina().getNombre(),busqueda.getLibro().getAnio(), busqueda.getLibro().getNumLibro(),busqueda.getLibro().getLibroBis(),busqueda.getLibro().getSeccionesPorOficina().getSeccion().getNombre(),busqueda.getLibro().getVolumen(),busqueda.getDocumento(),busqueda.getPrelacion().getConsecutivo());
       System.out.println("la caratula copia es " + caratula);
        
        byte[] nuevo = null;
        
        try {
            nuevo = pdfService.appendPDF(caratula,hojas);
            Prelacion prel = busqueda.getPrelacion();
            log.info("Prelacion " + prel);
            if( prel != null ) {
            	nuevo = pdfService.appendPDF(nuevo, certificadoService.getCertificadoCopias(prel.getId()));
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
    }
return nuevo;
    }


    //IHM METODO AGREGADO
    public byte[] caratulaCopia( 
    String oficina, 
    Integer anio,
    String tomo,
    String libro,
    String seccion,
    String volumen,
    String inscripcion,
    Integer entrada){

        System.out.println("La oficina es" + oficina);
        System.out.println("El año es " + anio);
        System.out.println("El Tomo es" + tomo);
        System.out.println("El Libro es" + libro);
        System.out.println("La seccion es" + seccion);
        System.out.println("El volumen es" + volumen);
        System.out.println("La inscripcion es" + inscripcion);
        System.out.println("La entrada es" + entrada);
        
        byte[] data = null;
                    JasperPrint jasperPrint = null;
                    JasperReport jasperReport= null;
                    InputStream jasperStream = null;
                    JRDataSource ds =new JREmptyDataSource();
                    Map<String, Object> parameterMap = new HashMap<String, Object>();
                    
                    parameterMap.put("inscripcion", inscripcion);
                    parameterMap.put("oficina", oficina);
                    parameterMap.put("anio", anio);
                    parameterMap.put("numeroLibro", tomo);
                    parameterMap.put("libro", libro);
                    parameterMap.put("seccion", seccion);
                    parameterMap.put("volumen", volumen ); 
                    parameterMap.put("folio", entrada );                    
                    parameterMap.put("path", Constantes.IMG_PATH);
                    parameterMap.put("imgPath", Constantes.IMG_PATH);
                    parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
                    parameterMap.put("path", Constantes.IMG_PATH);
                    jasperStream = this.getClass().getResourceAsStream("/reports/caratulaCopias.jasper");

                    try {
						jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
					
                        jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);
                        data=  JasperExportManager.exportReportToPdf(jasperPrint);
					} catch (JRException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                
                   

            return data;
    }

     //IHM METODO AGREGADO
    public String getHojasSeleccionadas(Long resultadoBusquedaId){

        BusquedaResultado  busqueda  =  busquedaResultadoRepository.findOneById(resultadoBusquedaId);

        if(busqueda!=null){            
            Pattern pattern = Pattern.compile(",");
            String hojasSeleccionadas = busqueda.getHojasSeleccionadas();            
            if(hojasSeleccionadas!=null){

                return hojasSeleccionadas;
            }
            else{
                return null;
            }
        }else{
            return null;
        }
    }
    
    public byte[] caratulaCopiaEntrada( BusquedaResultado busqueda){   	        
        byte[] data = null;
                    JasperPrint jasperPrint = null;
                    JasperReport jasperReport= null;
                    InputStream jasperStream = null;
                    JRDataSource ds =new JREmptyDataSource();
                    Map<String, Object> parameterMap = new HashMap<String, Object>();
                    if(busqueda.getPrelacionHistorica()!=null) {
                    	 parameterMap.put("entrada_historica", busqueda.getPrelacionHistorica().getConsecutivo());
                         parameterMap.put("anio", busqueda.getPrelacionHistorica().getAnio());
                         parameterMap.put("subindice", busqueda.getPrelacionHistorica().getSubindice());
                    }
                  
                    parameterMap.put("entradaActual", busqueda.getPrelacion().getConsecutivo() );
                    if(busqueda.getPredio()!=null) {
                    	 parameterMap.put("noFolio", busqueda.getPredio().getNoFolio());
                    	 parameterMap.put("numeroFolioReal", busqueda.getPredio().getNumeroFolioReal());
                    	 parameterMap.put("auxiliar", busqueda.getPredio().getAuxiliar());
                    }
                    parameterMap.put("path", Constantes.IMG_PATH);
                    parameterMap.put("imgPath", Constantes.IMG_PATH);
                    parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
                    parameterMap.put("path", Constantes.IMG_PATH);
                    jasperStream = this.getClass().getResourceAsStream("/reports/caratulaCopiasEntrada.jasper");

                    try {
						jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
					
                        jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);
                        data=  JasperExportManager.exportReportToPdf(jasperPrint);
					} catch (JRException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                
                   

            return data;
    }

}
