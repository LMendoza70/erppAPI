package com.gisnet.erpp.service;

import com.gisnet.erpp.domain.*;
import com.gisnet.erpp.repository.ActoFirmaRepository;
import com.gisnet.erpp.repository.ActoPredioCampoRepository;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.AntecedenteRepository;
import com.gisnet.erpp.repository.DetalleBusquedaRepository;
import com.gisnet.erpp.repository.PersonaRepository;
import com.gisnet.erpp.repository.PersonaJuridicaRepository;
import com.gisnet.erpp.repository.PredioPersonaRepository;
import com.gisnet.erpp.repository.PredioRepository;
import com.gisnet.erpp.repository.PrelacionPredioRepository;
import com.gisnet.erpp.repository.PrelacionUsuarioDefRepository;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.repository.BusquedaRepository;
import com.gisnet.erpp.repository.OrientacionRepository;
import com.gisnet.erpp.repository.ParametroRepository;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.ServletUtil;
import com.gisnet.erpp.vo.juridico.DatosLibroVO;
import com.gisnet.erpp.vo.juridico.FoliosRegistralesVO;
import com.gisnet.erpp.vo.juridico.RespuestaBusquedaSimpleVO;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import com.gisnet.erpp.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class BusquedaService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BusquedaRepository           busquedaRepository;
    @Autowired
    private PrelacionService             prelacionService;
    @Autowired
    private DetalleBusquedaService       detalleBusquedaService;
    @Autowired
    private DetalleBusquedaPredioService detalleBusquedaPredioService;
    @Autowired
    private TipoPersonaService tipoPersonaService;

    @Autowired
    private DetalleBusquedaRepository detalleBusquedaRepository;

    @Autowired
    private PredioRepository predioRepository;
    @Autowired
    private ActoRepository actoRepository;

    @Autowired
    private ActoPredioRepository actoPredioRepository;

    @Autowired
    private ActoPredioCampoRepository actoPredioCampoRepository;

    @Autowired
    private AntecedenteRepository antecedenteRepository;

    @Autowired
    private PredioPersonaRepository predioPersonaRepository;


    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private PersonaJuridicaRepository personaJuridicaRepository;

    @Autowired
    private PredioService predioService;

    @Autowired
    private AntecedenteService antecedenteService;

    @Autowired
    BoletaRegistralService boletaRegistralService;

    @Autowired
	PrelacionPredioRepository prelacionPredioRepository;

    @Autowired
    private BusquedaResultadoService busquedaResultadoService;

    @Autowired 
    private OrientacionRepository orientacionRepository;
    
    @Autowired 
    private ColindanciaService colindanciaService;
    
    @Autowired
	PrelacionUsuarioDefRepository prelacionUsuarioDefRepository;
    
    @Autowired
    PredioPersonaService predioPersonaService;
    
    @Autowired
    UsuarioService usuarioService;
    
    @Autowired
    ParametroRepository parametroRepository;
    
    @Autowired
    ActoFirmaRepository actoFirmaRepository;

    public Busqueda savePredio (HashMap predio, Long prelacionId) {

        Busqueda busqueda = this.createBusqueda(prelacionId);

        busqueda.setTipoBusqueda(1); 

        busqueda.setDetalleBusquedas(detalleBusquedaService.getDetallesFromHashMap(predio, -1L));

        this.saveBusqueda(busqueda);

        return busqueda;
        
    }


    public Busqueda savePersona (BusquedaPersonaVO persona, Long prelacionId) {

        Busqueda busqueda = this.createBusqueda(prelacionId);

        busqueda.setTipoBusqueda(this.getTipoPersonaFromPersona (persona));

        busqueda.setDetalleBusquedas(detalleBusquedaService.getDetallesFrom(persona, -1l));

        this.saveBusqueda (busqueda);

        return busqueda;

    }

    public Busqueda savePersonaJuridica (BusquedaPersonaVO persona, Long prelacionId) {

        Busqueda busqueda = this.createBusqueda(prelacionId);

        busqueda.setTipoBusqueda(4);

        busqueda.setDetalleBusquedas(detalleBusquedaService.getDetallesFrom(persona, -1l));

        this.saveBusqueda (busqueda);

        return busqueda;

    }

    public Busqueda updatePersona (BusquedaPersonaVO persona, Long busquedaId) {

        Busqueda busqueda = busquedaRepository.findOne (busquedaId );

        try {

            busqueda.setTipoBusqueda(this.getTipoPersonaFromPersona (persona));
            busqueda.setDetalleBusquedas(detalleBusquedaService.getDetallesFrom(persona, busquedaId) );

            System.out.println("Actualizando                ->  " + busqueda.getDetalleBusquedas().size() + " detalles");
            this.saveBusqueda(busqueda);

            System.out.println("Eliminando registros vacios ->  " + busqueda.getDetalleBusquedas().size() + " detalles");
            this.clearEmpty(busqueda);

        }
        catch (Exception except)  {

        }
        return busqueda;

    }

    public Busqueda updatePersonaJuridica (BusquedaPersonaVO persona, Long busquedaId) {

        Busqueda busqueda = busquedaRepository.findOne (busquedaId );

        try {

            busqueda.setTipoBusqueda(4);
            busqueda.setDetalleBusquedas(detalleBusquedaService.getDetallesFrom(persona, busquedaId) );

            System.out.println("Actualizando                ->  " + busqueda.getDetalleBusquedas().size() + " detalles");
            this.saveBusqueda(busqueda);

            System.out.println("Eliminando registros vacios ->  " + busqueda.getDetalleBusquedas().size() + " detalles");
            this.clearEmpty(busqueda);

        }
        catch (Exception except)  {

        }
        return busqueda;

    }

    private void saveBusqueda(Busqueda busqueda) {
        if (busqueda.getId()== null)
            busquedaRepository.saveAndFlush(busqueda);
        detalleBusquedaService.save (busqueda.getDetalleBusquedas(), busqueda);

    }

    private void clearEmpty(Busqueda busqueda) {

        detalleBusquedaService.clearEmpty (busqueda.getDetalleBusquedas(), busqueda);

    }

    private Busqueda createBusqueda (Long prelacionId) {
        Busqueda bus = new Busqueda();
        bus.setPrelacion(prelacionService.findOne(prelacionId) );

        return bus;
    }


    private Integer getTipoPersonaFromPersona (BusquedaPersonaVO persona) {
        /*switch (persona.getTipoPersona().getNombre()) {
            case "FISICA": return 2;
            case "MORAL" : return 3;

        }
        */
        return persona.getTipoPersona().getId().intValue() + 1;

    }

    public Object updatePredio(HashMap<String, String> mapPredio, Long prelacionId, Long busquedaId) {
        Busqueda busqueda = busquedaRepository.findOne (busquedaId );

        try {
            String json = "";

            HashSet<DetalleBusqueda> detail = detalleBusquedaService.getDetallesFromHashMap(mapPredio, busquedaId);

            busqueda.setDetalleBusquedas(detail);

            System.out.println("Actualizando ->  " + busqueda.getDetalleBusquedas().size() + " detalles");

            this.saveBusqueda(busqueda);
        }
        catch (Exception except)  {

        }
        return busqueda;
    }

    public Set<BusquedaPersonaVO> getPersonasFromPrelacion(Long prelacionId, Constantes.ETipoPersona tipoPersona) {
        Set <BusquedaPersonaVO> models= new HashSet<BusquedaPersonaVO>();
        Set <Busqueda> busquedas = busquedaRepository.findByPrelacionIdAndTipoBusqueda (prelacionId, tipoPersona.getAsInteger() );
        System.out.println("--------------BUSCANDO EN TABLA BUSQUEDA------------");
        System.out.println(" prelacionId "+ prelacionId);
        System.out.println("tipoPersona "+ tipoPersona);
        if (isEmpty(busquedas))
            return null;

        for (Busqueda busqueda : busquedas ) {
            HashSet<DetalleBusqueda> properties = detalleBusquedaService.findDetallesFromBusqueda(busqueda);
            if (properties != null && properties.size()!=0) {                
                BusquedaPersonaVO model=  detalleBusquedaService.getPersonaModelFromDetalles (properties);
                model.setFisica( tipoPersona == Constantes.ETipoPersona.FISICA );
                if (! model.isFisica())
                    model.setDenominacion( model.getNombre());
                model.setBusquedaId( busqueda.getId() );

                models.add(model);

            }
        }

        return models;
    }


    public  HashSet<DetalleBusqueda> getPersonasHashFromPrelacion(Long prelacionId, Constantes.ETipoPersona tipoPersona) {
        HashSet<DetalleBusqueda> models= new HashSet<DetalleBusqueda>();
        Set <Busqueda> busquedas = busquedaRepository.findByPrelacionIdAndTipoBusqueda (prelacionId, tipoPersona.getAsInteger() );

        if (busquedas == null)
            return models;

        for (Busqueda busqueda : busquedas ) {
            models=  detalleBusquedaService.findDetallesFromBusqueda(busqueda);
            
            
        }

        return models;
    }

    public HashSet<DetalleBusqueda> getInmueblesHashFromPrelacion(Long prelacionId) {
        HashSet<DetalleBusqueda>  models= new HashSet<DetalleBusqueda>();
        Set <Busqueda> busquedas = busquedaRepository.findByPrelacionIdAndTipoBusqueda (prelacionId, 1);

        if (isEmpty(busquedas) )
            return models;


        Busqueda busqueda = busquedas.iterator().next();
        models = detalleBusquedaService.findDetallesFromBusqueda(busqueda);
            

        return models;
    }

    public BusquedaPredioVO getInmueblesFromPrelacion(Long prelacionId) {
        BusquedaPredioVO models= new BusquedaPredioVO();
        Set <Busqueda> busquedas = busquedaRepository.findByPrelacionIdAndTipoBusqueda (prelacionId, 1);
        
        if (isEmpty(busquedas) )
            return null;
        

        Busqueda busqueda = busquedas.iterator().next();
            HashSet<DetalleBusqueda> properties = detalleBusquedaService.findDetallesFromBusqueda(busqueda);
            if (properties != null) {
                BusquedaPredioVO model=  detalleBusquedaPredioService.getPredioModelFromDetalles (properties);

                model.setBusquedaId( busqueda.getId() );

                models =model;
            }

        return models;
    }


    public HashMap<String, Integer> getTipoBusquedasFromPrelacion(Long prelacionId) {

        HashMap<String, Integer> res = new HashMap<>();

        Set <Busqueda> busquedasPredios = busquedaRepository.findByPrelacionIdAndTipoBusqueda (prelacionId, 1);
        Set <Busqueda> busquedasFisicos = busquedaRepository.findByPrelacionIdAndTipoBusqueda (prelacionId, 2);
        Set <Busqueda> busquedasMorales = busquedaRepository.findByPrelacionIdAndTipoBusqueda (prelacionId, 3);
        Set <Busqueda> busquedasJuridico = busquedaRepository.findByPrelacionIdAndTipoBusqueda (prelacionId, 4);

        if (busquedasPredios != null)
            res.put("INMUEBLE", busquedasPredios.size() );
        
        if (busquedasFisicos != null )
            res.put("FISICA", busquedasFisicos.size() );

        if (busquedasMorales != null)
            res.put("MORAL", busquedasMorales.size() );
        
        if (busquedasJuridico != null)
            res.put("JURIDICO", busquedasJuridico.size());

        return res;
    }

    public RespuestaBusquedaSimpleVO getReporteBusquedaSimple(Long prelacionId){
            SimpleDateFormat formatterFecha     = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatterHora      = new SimpleDateFormat("HH:mm:ss");
            RespuestaBusquedaSimpleVO reporte   = new RespuestaBusquedaSimpleVO();
            List<ActoFirma> aFi = new ArrayList<ActoFirma>();
               Prelacion prelacion= prelacionService.findOne(prelacionId);
               Predio p = null;
               if(prelacion!=null&&prelacion.getOficina()!=null){
                    reporte.setOficina(prelacion.getOficina().getNombre());
                }
               ArrayList<ActoPredioCampo> actoPredioCampos = new ArrayList();
               List<Acto> acto =  actoRepository.findByPrelacionId(prelacionId);
               if(acto!=null && !acto.isEmpty()){
                    for(Acto a:acto){
                    	aFi = actoFirmaRepository.findAllByActoIdAndEsActoOrderByIdDesc(a.getId(),true);
                    	
                        actoPredioCampos=getActoPredioCampo(a);
                        for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                            ModuloCampo moduloCampo = actoPredioCampo.getModuloCampo();
                            int id_modulo_campo =  moduloCampo.getId().intValue();
                            if (id_modulo_campo == 2752) {// no oficio
                            	if(actoPredioCampo.getValor() != null) {
                            		 reporte.setNoOficio(actoPredioCampo.getValor());
                            	}
                            }
                            if (id_modulo_campo == 20770) {//  fecha oficio
                                String s=actoPredioCampo.getValor().toString();
								String anio = s.substring(0,s.indexOf('-'));            
								s=s.substring(s.indexOf('-')+1,s.length());		
								String m = s.substring(0,s.indexOf('-'));         
								s=s.substring(s.indexOf('-')+1,s.length());			
								String d=s;
								s = d+"/"+m+"/"+anio;
                                reporte.setFechaOficio(s);
                            }
                            if (id_modulo_campo == 20771) {//   año inicial
                                reporte.setAnioInicial(actoPredioCampo.getValor());
                            }
                            if (id_modulo_campo == 20772) {//   año final
                                reporte.setAnioFinal(actoPredioCampo.getValor());
                            }
                        }
                    }
                }



               reporte.setNumPrelacion(prelacion.getConsecutivo());
               reporte.setColindancias(null);
                             
               List<Busqueda> listBusquedas = busquedaRepository.findByPrelacionId(prelacion.getId());

               if(listBusquedas!=null){
                   for(Busqueda b:listBusquedas){
                       reporte.setTipoB(b.getTipoBusqueda());
                   }
               }

               Set<PrelacionServicio> pServicio= prelacion.getPrelacionServiciosParaPrelacions();
               
               for(PrelacionServicio  ps :pServicio){

                    if(Constantes.Servicio.BUSQUEDA_SIMPLE.getIdServicio()       ==  ps.getServicio().getId()){

                        reporte.setTipoBusqueda("BUSQUEDA SIMPLE");
                        reporte.setBusqueda(1);

                    }
                    if(Constantes.Servicio.BUSQUEDA_HISTORIAL.getIdServicio()    ==  ps.getServicio().getId()){
                        reporte.setBusqueda(2);
                        reporte.setTipoBusqueda("BUSQUEDA CON HISTORIAL");

                        reporte.setHistorial( this.getCuentaCatastral(prelacion) );

                    }
               }
               if(prelacion.getUsuarioSolicitan().getId() == 0) {
            	   PrelacionUsuarioDef def = prelacionUsuarioDefRepository.findOneByPrelacionId(prelacionId);
            	   if (def != null) {
            		   reporte.setSolicitud(def.getNombre() + " " + def.getPaterno() + " " + def.getMaterno());
            	   }
               }else {
            	   reporte.setSolicitud(prelacion.getUsuarioSolicitan().getNombreCompleto());
               }
               reporte.setFecha(formatterFecha.format(prelacion.getFechaIngreso()));
               reporte.setHora(formatterHora.format(prelacion.getFechaIngreso()));
               
               reporte.setLeyendaLocalizado("");

               String referencia="";
               String refInmueble = "";

               //HashSet<DetalleBusqueda> propertiesInmueble=    getInmueblesHashFromPrelacion(prelacion.getId());
              // HashSet<String,String> propertiesInmueble=  getBusquedaAsHashSetFromPrelacion(prelacion.getId());


                List<String> propertiesInmueble= getBusquedaAsHashSetFromPrelacion(prelacion.getId());
                for(String s:propertiesInmueble){
                    refInmueble+=s;
                }
                //HashMap<String, String> propertiesPersonaF=    getBusquedaPersonaAsHashSetFromPrelacion(prelacion.getId());
                HashSet<String> propertiesPersonaF=    getBusquedaPersonaAsStringFromPrelacion(prelacion.getId());

                if (!propertiesPersonaF.isEmpty()) {
                    Set<String> entradasF=new HashSet<String>(propertiesPersonaF);
                    for ( String entry : entradasF ) {
                        if (!isEmpty(entry ))
                        referencia += entry + "\n";

                    }
                }

                HashMap<String, String> propertiesPersonaM=    getBusquedaMoralAsHashSetFromPrelacion(prelacion.getId());

                if (!propertiesPersonaM.isEmpty()) {
                    Map<String,String> entradasM=new HashMap<String, String>(propertiesPersonaM);
                    for (Map.Entry<String,String> entry : entradasM.entrySet()) {
                        if (!isEmpty(entry.getValue() ))
                            referencia+=entry.getValue()+" ";
                    }
                }

                HashMap<String, String> propertiesPJ=    getBusquedaPJAsHashSetFromPrelacion(prelacion.getId());

                if (!propertiesPJ.isEmpty()) {
                    Map<String,String> entradasPJ=new HashMap<String, String>(propertiesPJ);
                    for (Map.Entry<String,String> entry : entradasPJ.entrySet()) {
                        if (!isEmpty(entry.getValue() ))
                            referencia+=entry.getValue()+" ";
                    }
                }



                reporte.setDatosPFM(referencia);
                reporte.setDatosPredio(refInmueble);
                
               reporte.setReferencia(referencia);
        
               Integer tipoBusqueda=null;
               List<Busqueda> busquedas = busquedaRepository.findByPrelacionId(prelacion.getId());
               for (Busqueda b:busquedas){
                    if(b.getTipoBusqueda()!=null){
                        tipoBusqueda = b.getTipoBusqueda();
                    }
               }

               /*
                * si hay datos guardados y prelacion esta en calificador
                * obtener datos de la lista BusquedaResultado   (ResultadoBusqueda) y comparar con la lista de PredioAnteVO
                * */
                HashSet<BusquedaResultado> resultadosBusqueda = busquedaResultadoService.getPrediosFromPrelacionId(prelacionId);
                        

               switch (tipoBusqueda){
                   case 1:  // BUSQUEDA POR INMUEBLE
                   case 2:  // BUSQUEDA POR PERSONA FISICA
                   case 3:  // BUSQUEDA POR PERSONA MORAL
                        List<PredioAnteVO>  result= listPredioAnteFromBusqueda(prelacion.getId()); 
                        if(result!=null && result.size()>0){
                            //FILTRA SOLO PREDIOS SELECIONADOS 
                            result  = result.stream().filter(x->x.getSeleccionado()!= null && x.getSeleccionado()).collect(Collectors.toList());

                            List<FoliosRegistralesVO> folios= new ArrayList<FoliosRegistralesVO>(); 
                            List<DatosLibroVO> libros= new ArrayList<DatosLibroVO>(); 
                            StringBuilder sb = new StringBuilder();
                            reporte.setUbicacion("NO SE ENCONTRÓ NINGÚN PREDIO ASOCIADO");
                            for( PredioAnteVO  paVO : result) {
                                System.out.println("prelacion "+ prelacion+" paVO: "+ paVO+" resultadosBusqueda " +resultadosBusqueda.size());
                                System.out.println(" paVO.getAntecedente() "+  paVO.getAntecedente());

                                p= predioRepository.findOne(paVO.getPredio().getId());
                                reporte.setUbicacion(null);

                                String colindanciaObservacion = "";
                                //log.debug("BRS401-ESTO ES PREDIO:{}", predio1);
                                Set<Colindancia> colindancias = colindanciaService.findColindancias(p.getId());
                                log.debug("BRS403 ESTO ES colindancias{}", colindancias);

                                if(colindancias!=null){                                        
                                    for (Colindancia colindancia : colindancias) {                            
                                        if (colindancia.getOrientacion().getId() == 17L) {
                                            colindanciaObservacion +="\n\nOBSERVACIONES:\n"+colindancia.getNombre()+"\n";
                                        }else {
                                            colindanciaObservacion+="\n"+ colindancia.getOrientacion().getNombre()+" "+ colindancia.getNombre();                         
                                        }

                                    }
                                }

                                sb.append("\n");
                                sb.append("FOLIO :");
                                sb.append(p.getNoFolio());
                                sb.append("\n");

                                StringBuilder antedecente = new StringBuilder();
                                Optional<PredioAnte> antecedente =   predioService.findFirstByPredioIdOrderByIdDesc(p.getId());

                                if (!antecedente.isPresent() ) {  //El folio no tiene Antecedentes
                                    if(paVO.getPredio().getProcedeDeFolio()!=null) {
                                        antedecente.append("\nPROCEDE DE FOLIO: ").append(paVO.getPredio().getProcedeDeFolio());
                                    }else{
                                        antedecente.append("\nPRIMER REGISTRO");	
                                    }

                                }
                                else  {// El folio tiene antecedentes
                                    Antecedente  ante = antecedente.get().getAntecedente();
                                    AntecedenteVO anteVO =   AntecedenteVO.antecedenteTransform(ante);	
                                    antedecente.append("\nANTECEDENTE:").append("\n");
                                    antedecente.append(
                                            "Tomo: "+ anteVO.getLibro()+
                                            "   Libro: "+anteVO.getLibroBis()+
                                            "   Seccion: "+ anteVO.getSeccion()+
                                            "   Oficina: "+anteVO.getOficina()+
                                            "   Año: "+ anteVO.getAnio()+
                                            "   Volumen: "+anteVO.getVolumen()+
                                            "   Inscripcion: "+ anteVO.getDocumento()
                                            );
                                }

                                sb.append(antedecente.toString());

                                List<PredioPersona> prediosPersona = predioPersonaService.findPropietariosActuales(p.getId(), false);
                                sb.append("\n\nTITULARES: ");
                                prediosPersona.forEach(x->{
                                    sb.append("\n");
                                    sb.append(x.getPersona().getNombre()).append(" ");
                                    sb.append(x.getPersona().getPaterno()!=null ? x.getPersona().getPaterno() : "").append(" ");
                                    sb.append(x.getPersona().getMaterno()!=null ? x.getPersona().getMaterno() : "");
                                    sb.append(" DD: ").append(x.getPorcentajeDd()!=null && x.getPorcentajeDd() > 0 ? x.getPorcentajeDd() : "0" ).append("%").append(" ");
                                    sb.append(" UV: ").append(x.getPorcentajeUv()!=null && x.getPorcentajeUv() > 0 ? x.getPorcentajeUv() : "0" ).append("%");
                                });

                                if(p.getDireccionComplete() != null || colindanciaObservacion != "") {
                                    sb.append("\n\nUBICACIÓN:");
                                    sb.append("\n");
                                    sb.append(p.getDireccionComplete());
                                    sb.append("\n");
                                    sb.append(colindanciaObservacion);
                                    sb.append("\n");
                                }
                                reporte.setColindancias(sb.toString());
                            }  
                        }
                    else {
                        reporte.setUbicacion("NO SE ENCONTRÓ NINGÚN PREDIO ASOCIADO");
                        List<FoliosRegistralesVO> folios= new ArrayList<FoliosRegistralesVO>();
                        List<DatosLibroVO> libros= new ArrayList<DatosLibroVO>();

                        FoliosRegistralesVO folTemp = new FoliosRegistralesVO();

                        folTemp.setFolio("");
                        folTemp.setOficina("");
                        folTemp.setSeccion("");
                        folios.add(folTemp);

                        DatosLibroVO libroTemp = new DatosLibroVO();

                        libroTemp.setDocumento("");
                        libroTemp.setLibro("");
                        libroTemp.setOficinaLibro("");
                        libroTemp.setSeccion("");
                        libroTemp.setTipoLibro("");
                        libroTemp.setTipoDoc("");

                        libros.add(libroTemp);

                        reporte.setFoliosRegistrales(folios);
                        reporte.setDatosLibros(libros);
                    }
                    break;
                   case 4:  //BUSQUEDA DE FOLIO DE JURIDICO
                        
                        StringBuilder sb2 = new StringBuilder();
                        reporte.setUbicacion("NO SE ENCONTRÓ PERSONA MORAL");
                        if(resultadosBusqueda!=null && resultadosBusqueda.size()>0){
                            for(BusquedaResultado br:resultadosBusqueda){
                                if(br.getPersonaJuridica()!=null){
                                    reporte.setUbicacion(null);
                                    sb2.append("\n");
                                    sb2.append("FOLIO :");
                                    sb2.append(br.getPersonaJuridica().getNoFolio());
                                    sb2.append("\n");

                                    if(br.getPersonaJuridica().getDenominacionNombre()!=null) {
                                        sb2.append("\n\nNOMBRE O DENOMINACION:");
                                        sb2.append("\n");
                                        sb2.append(br.getPersonaJuridica().getDenominacionNombre());
                                        sb2.append("\n");
                                    }

                                    if(br.getPersonaJuridica().getMunicipio()!=null && br.getPersonaJuridica().getMunicipio().getEstado()!=null) {
                                        sb2.append("\n\nUBICADA EN:");
                                        sb2.append("\n");
                                        sb2.append(br.getPersonaJuridica().getMunicipio().getNombre());
                                        sb2.append(",");
                                        sb2.append(br.getPersonaJuridica().getMunicipio().getEstado().getNombre());                                        
                                        sb2.append("\n");
                                    }
                                }
                                reporte.setColindancias(sb2.toString());
                            }
                        }
                        else {
                            reporte.setUbicacion("NO SE ENCONTRÓ PERSONA MORAL");
                            List<FoliosRegistralesVO> folios= new ArrayList<FoliosRegistralesVO>();
                            List<DatosLibroVO> libros= new ArrayList<DatosLibroVO>();
    
                            FoliosRegistralesVO folTemp = new FoliosRegistralesVO();
    
                            folTemp.setFolio("");
                            folTemp.setOficina("");
                            folTemp.setSeccion("");
                            folios.add(folTemp);
    
                            DatosLibroVO libroTemp = new DatosLibroVO();
    
                            libroTemp.setDocumento("");
                            libroTemp.setLibro("");
                            libroTemp.setOficinaLibro("");
                            libroTemp.setSeccion("");
                            libroTemp.setTipoLibro("");
                            libroTemp.setTipoDoc("");
    
                            libros.add(libroTemp);
    
                            reporte.setFoliosRegistrales(folios);
                            reporte.setDatosLibros(libros);
                        }
                    break;
               }

               Integer cantResultado = 0;
                
               System.out.println("ResultadoCount "+cantResultado);
               reporte.setResultadoCount(cantResultado);
               
               //reporte.setHistorial("");
               //reporte.setFirmaBoleta("");
               //reporte.setFirmaBusqueda(prelacion.getFirma());
               
               
               if( aFi != null && aFi.size()>0 ) {
               	reporte.setReviso(boletaRegistralService.buildUsuario( aFi.get(0).getUsuario() ));
               }else {
            	   reporte.setReviso(boletaRegistralService.buildUsuario( prelacion.getUsuarioAnalista() ));
               }

                // FIRMA ELECTRONICA REGISTRADOR
                reporte.setFirmaBusqueda(boletaRegistralService.buildStringFirmaActos(prelacionId));

                // FIRMA ELECTRONICA COORDINADOR
                
                reporte.setAutorizo(boletaRegistralService.buildUsuario( prelacion.getUsuarioCoordinador()));
                String firmaCoord = "-SIN FIRMA -";
                if (prelacion.getFirma() != null )
                    firmaCoord = prelacion.getFirma() + "\r\n";
                reporte.setFirmaBoleta(firmaCoord);               

            return reporte;
    }

    private String getCuentaCatastral(Prelacion prelacion) {
        Set<Acto> actos= prelacion.getActosParaPrelacions();

        String  cuentaCatastral = "", claveCatastral  = "";


        if(actos!=null) {
            for(Acto a :actos) {

                for(ActoPredio ap : a.getActoPrediosParaActos()) {
                    Set<ActoPredioCampo> actoPredioCampos = ap.getActoPredioCamposParaActoPredios();
                    for (ActoPredioCampo actoPredioCampo : actoPredioCampos) {
                        long campoId = actoPredioCampo.getModuloCampo().getCampo().getId();
                        log.debug("{} = {}", campoId, actoPredioCampo.getValor());

                        if (campoId == 509)
                            cuentaCatastral = actoPredioCampo.getValor();
                        if (campoId == 510)
                            claveCatastral = actoPredioCampo.getValor();
                    }
                }
            }
        }

        return cuentaCatastral + "      clave " + claveCatastral;

    }


    private boolean esPosibleAgregarPredio(Prelacion prelacion, PredioAnteVO paVO, HashSet<BusquedaResultado> resultadosBusqueda) {

        if (isPrelacionInEstadoCalificador(prelacion)) {
            return (isPredioInBusquedaGuardada (paVO, resultadosBusqueda));
        } else
            return true;

    }

    private boolean isPredioInBusquedaGuardada(PredioAnteVO paVO, HashSet<BusquedaResultado> resultadosBusqueda) {
        if (isEmpty(resultadosBusqueda))
            return false;

        for (BusquedaResultado busRes : resultadosBusqueda) {
            if (busRes.getPredio().getId() == paVO.getPredio().getId())
                return true;
        }

        return false;
    }

    private boolean isPrelacionInEstadoCalificador(Prelacion prelacion) {
        Status statusEnCalificador = new Status();
        statusEnCalificador.setId( Constantes.Status.ASIGNADO_A_CALIFICADOR.getIdStatus());

        return (prelacion.getStatus().getId() == statusEnCalificador.getId());
    }


    /*  Consultas de busqueda a los predios  */


    public Page<Predio> listPredioFromBusqueda (Long prelacionId, Pageable pageable) {

    	Prelacion prelacion =  prelacionService.findOne(prelacionId);
        BusquedaPredioVO busquedaVo= getInmueblesFromPrelacion(prelacionId);
        Set<BusquedaPersonaVO> busquedaPersonaVO = getPersonasFromPrelacion(prelacionId, Constantes.ETipoPersona.FISICA);
        Set<BusquedaPersonaVO> personaMoral = getPersonasFromPrelacion(prelacionId, Constantes.ETipoPersona.MORAL);
        //Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
        if (isEmpty (busquedaVo)) {
                if (isEmpty(busquedaPersonaVO) && isEmpty(personaMoral)) {
                    log.debug("Se omite busqueda, personas sin criterios");
                    return null;
                }
                else {
                    if(personaMoral!=null){
                        List<Predio> predios= new ArrayList<Predio>();
                        for(BusquedaPersonaVO bP:personaMoral){
                        	if(bP.getNombre()!=null && !bP.getNombre().trim().isEmpty()) {
                        	      TipoPersona tP = tipoPersonaService.findOne(2L);
                                  Set<Persona> pMrls =personaRepository.findByNombreContainingIgnoreCaseAndTipoPersona(bP.getNombre(),tP);
                              
                                 
                                  for(Persona p:pMrls){
                                      List<PredioPersona> predioPersonas=predioPersonaRepository.findAllByPersonaId(p.getId());
                                      System.out.println("predioPersonas "+predioPersonas.size());
                                      for(PredioPersona pp: predioPersonas){
                                         // Acto acto = pp.getActoPredio().getActo();
                                      	if(pp.getActoPredio().getPredio()!=null) {	                                	
      	                                	if(pp.getActoPredio().getPredio().getOficina()!=null) {
      	                                		if((pp.getActoPredio().getPredio().getStatusActo().getId()==1L) &&
                                                          pp.getActoPredio().getPredio().getOficina().getId().equals(prelacion.getOficina().getId()) &&
                                                          pp.getActoPredio().getActo().getStatusActo().getId()==1
                                                          ){
      	                                				Predio predio = pp.getActoPredio().getPredio();                                       
      	                                				predios.add(predio);                                        
      	                                		}
      	                                	}
                                      	}
                                          
                                      } 
                                  }
                        	}
                      
                        }
                        //log.info(" predios.size():"+predios.size());
                        final Page<Predio> page = new PageImpl<Predio>(predios,pageable,predios.size());
                        //log.info(" ->page.getSize():"+page.getSize());
                        return page;
                    }
                    log.debug("Busqueda por personas, predio sin criterios de busqueda");
                    busquedaVo = new BusquedaPredioVO();
                }
        }
        
       /* if (isEmpty(busquedaVo)) {
			if (isEmpty(busquedaPersonaVO) && isEmpty(personaMoral)) {
				log.debug("Se omite busqueda, personas sin criterios");
				return null;
			} else {
				log.debug("Busqueda por personas, predio sin criterios de busqueda");
				busquedaVo = new BusquedaPredioVO();
			}
		}*/


        return predioRepository.findPrediosByBusquedaVO (busquedaVo, busquedaPersonaVO, personaMoral,prelacion.getOficina().getId(), pageable);

    }

    public Page<PersonaJuridica> listPersonaJuridicaFromBusqueda (Long prelacionId, Pageable pageable) {
        Page<PersonaJuridica> page = null;      
        Set<BusquedaPersonaVO> personaMoral = getPersonasFromPrelacion(prelacionId, Constantes.ETipoPersona.JURIDICO);        
        if (isEmpty(personaMoral)) {
            log.debug("Se omite busqueda, personasJuridica sin criterios");
            return null;
        }
        else {
            if(personaMoral!=null){
                List<PersonaJuridica> personasJuridicas= new ArrayList<PersonaJuridica>();
                    for(BusquedaPersonaVO bP:personaMoral){
                        if(bP.getNombre()!=null && !bP.getNombre().trim().isEmpty()) {                        	      
                            personasJuridicas =personaJuridicaRepository.findByDenominacionNombreContainingIgnoreCase(bP.getNombre());      
                        }
                    }                        
                    page = new PageImpl<PersonaJuridica>(personasJuridicas,pageable,personasJuridicas.size());                                                
            }
            //log.debug("Busqueda por personas, predio sin criterios de busqueda");
        }          
        return page;
    }

    public List<String> getBusquedaAsHashSetFromPrelacion (Long prelacionId) {


            BusquedaPredioVO busquedaVo= getInmueblesFromPrelacion(prelacionId);
            
            HashMap <String, String> temp = new HashMap<>();
            HashMap<String,String> linderosOk;
            if (!isEmpty (busquedaVo)) {
                if (busquedaVo.getTipoVialidad() != null)
                    temp.put("TIPO DE VIALIDAD",busquedaVo.getTipoVialidad().getNombre());
                if (busquedaVo.getVialidad() != null)
                    temp.put("VIALIDAD",busquedaVo.getVialidad());
                if (busquedaVo.getNumExt()!= null)
                    temp.put("NUM EXT.",busquedaVo.getNumExt());
                if (busquedaVo.getNumInt() != null)
                    temp.put("NUM INT.",busquedaVo.getNumInt());
                if (busquedaVo.getEdificio()!= null)
                    temp.put("EDIFICIO",busquedaVo.getEdificio());
                if (busquedaVo.getNivel() != null)
                    temp.put("NIVEL",busquedaVo.getNivel().getNombre());
                if (busquedaVo.getTipoAsent() != null)
                    temp.put("TIPO ASENT.",busquedaVo.getTipoAsent().getNombre());
                if (busquedaVo.getAsentamiento() != null)
                    temp.put("ASENTAMINETO",busquedaVo.getAsentamiento());
                if (busquedaVo.getEstado() != null)
                    temp.put("ESTADO",busquedaVo.getEstado().getNombre());
                if (busquedaVo.getMunicipio() != null)
                    temp.put("MUNICIPIO",busquedaVo.getMunicipio().getNombre());
                if (busquedaVo.getCodigoPostal() != null)
                    temp.put("CODIGO POSTAL",busquedaVo.getCodigoPostal());
                if (busquedaVo.getClaveCatastral() != null)
                    temp.put("CLAVE CATASTRAL",busquedaVo.getClaveCatastral());
                if (busquedaVo.getCuentaCatastral() != null)
                    temp.put("CUENTA CATASTRAL",busquedaVo.getCuentaCatastral());
                if (busquedaVo.getClaveCatastralEstandard() != null)
                    temp.put("CLAVE CATASTRAL ESTANDARD",busquedaVo.getClaveCatastralEstandard());
                if (busquedaVo.getNoLote() != null)
                    temp.put("NUM LOTE",busquedaVo.getNoLote());
                if (busquedaVo.getLocalidadSector() != null)
                    temp.put("LOCALIDAD SECTOR",busquedaVo.getLocalidadSector());
                if (busquedaVo.getManzana() != null)
                    temp.put("MANZANA",busquedaVo.getManzana());
                if (busquedaVo.getZona() != null)
                    temp.put("ZONA",busquedaVo.getZona());
                if (busquedaVo.getSuperficie() != null)
                    temp.put("SUPERFICIE",busquedaVo.getSuperficie());
                if (busquedaVo.getUnidadMedida() != null)
                    temp.put("UNIDAD MEDIDA",busquedaVo.getUnidadMedida().getNombre());
                if (busquedaVo.getUsoSuelo() != null)
                    temp.put("USO SUELO",busquedaVo.getUsoSuelo().getNombre());
                if (busquedaVo.getTipoInmueble() != null)
                    temp.put("TIPO INMUEBLE",busquedaVo.getTipoInmueble().getNombre());
                if (busquedaVo.getFraccion() != null)
                    temp.put("FRACCIÓN",busquedaVo.getFraccion());
                if (busquedaVo.getMacroManzana() != null)
                    temp.put("MACRO MANZANA",busquedaVo.getMacroManzana());
                if (busquedaVo.getTipoVialidad2() != null)
                    temp.put("TIPO VIALIDAD 2",busquedaVo.getTipoVialidad2().getNombre());
                if (busquedaVo.getVialidad2() != null)
                    temp.put("VIALIDAD 2",busquedaVo.getVialidad2());
                if (busquedaVo.getNumExt2() != null)
                    temp.put("NUM EXT.2",busquedaVo.getNumExt2());
                if (busquedaVo.getEnlaceVialidad() != null)
                    temp.put("ENLACE VIALIDAD",busquedaVo.getEnlaceVialidad().getNombre());
                if (busquedaVo.getFracOCondo() != null)
                    temp.put("FRACC/CONDO",busquedaVo.getFracOCondo().getNombre());
                if (busquedaVo.getNombreFracOCondo() != null)
                    temp.put("NOMBRE FRACC/CONDO",busquedaVo.getNombreFracOCondo());
                if (busquedaVo.getEtapaOSeccion() != null)
                    temp.put("ETAPA o SECCION",busquedaVo.getEtapaOSeccion().getNombre());
                if (busquedaVo.getVarianteMunicipio() != null)
                    temp.put("VARIANTE MUNICIPIO",busquedaVo.getVarianteMunicipio().getNombre());
                if (busquedaVo.getVarianteTipoAsent() != null)
                    temp.put("VARIANTE TIPO ASENT",busquedaVo.getVarianteTipoAsent().getNombre());

                if (busquedaVo.getLinderos() != null){
               
                    linderosOk= new HashMap<String,String>();
                    busquedaVo.getLinderos().forEach((k,v) ->{
                     Orientacion lind = orientacionRepository.findOne(Long.parseLong(k));
                     if(lind!=null){
                         linderosOk.put("-"+lind.getNombre(), v);
                     }
                    });
                    busquedaVo.setLinderos(linderosOk);
                    temp.putAll(busquedaVo.getLinderos());
                }


            }
                ArrayList<String> tempSort = new ArrayList<String>();
                List<String> v = new ArrayList<>(temp.keySet());
                List<String> orientacion = new ArrayList<>();
                
                Collections.sort(v);
                for(String s:v){
                    System.out.println("s "+s);
                    temp.forEach((k,valor) ->{
                        if(k==s){
                            System.out.println("(k.indexOf: "+k.indexOf("-"));
                            if(k.startsWith("-")){
                                if(orientacion.size()==0){
                                    orientacion.add("ORIENTACIONES:\r\n");
                                    orientacion.add(k+": "+valor+"\r\n");
                                }else{
                                    orientacion.add(k+": "+valor+"\r\n");
                                }
                                
                            }else{
                                System.out.println("ordenado"+k+": "+valor+" \r\n");
                                tempSort.add(k+": "+valor+" \r\n");
                            }
                           
                        }
                    });
                }
                System.out.println("orientacion tamaño "+orientacion.size());
                for(String o:orientacion){
                    tempSort.add(o);
                }
        
        return tempSort;

    }

    public HashMap<String, String> getBusquedaPersonaAsHashSetFromPrelacion (Long prelacionId) {

   
        Set<BusquedaPersonaVO> busquedaPersonaVO = getPersonasFromPrelacion(prelacionId, Constantes.ETipoPersona.FISICA);
        HashMap <String, String> temp = new HashMap<>();
        if (!isEmpty(busquedaPersonaVO) ) {
            for (BusquedaPersonaVO vo: busquedaPersonaVO ) {

                if (vo.getNombre() != null)
                    temp.put("Nombre",vo.getNombre());
                if (vo.getPaterno() != null)
                    temp.put("Paterno",vo.getPaterno());
                if (vo.getMaterno() != null)
                    temp.put("Materno",vo.getMaterno());
                if (vo.getFechaNac() != null)
                    temp.put("Fecha Nac",vo.getFechaNac().toString());
                if (vo.getRfc() != null)
                    temp.put("RFC",vo.getRfc());
                if (vo.getCurp() != null)
                    temp.put("CURP",vo.getCurp());
                if (vo.getEstadoCivil() != null)
                    temp.put("Estado Civil",vo.getEstadoCivil().getNombre());
                if (vo.getRegimen() != null)
                    temp.put("Regimen",vo.getNombre());

            }
        }


     

        return temp;

    }


    public HashMap<String, String> getBusquedaMoralAsHashSetFromPrelacion (Long prelacionId) {

        Set<BusquedaPersonaVO> personaMoral = getPersonasFromPrelacion(prelacionId, Constantes.ETipoPersona.MORAL);

        HashMap <String, String> temp = new HashMap<>();


        if (!isEmpty(personaMoral) ) {
            for (BusquedaPersonaVO vo: personaMoral ) {
                if (vo.getDenominacion() != null)
                    temp.put("denominacion", vo.getDenominacion());
                if (vo.getRfc() != null)
                    temp.put("rfc", vo.getRfc());
            }
        }

    return temp;

    }

    public HashMap<String, String> getBusquedaPJAsHashSetFromPrelacion (Long prelacionId) {

        Set<BusquedaPersonaVO> personaMoral = getPersonasFromPrelacion(prelacionId, Constantes.ETipoPersona.JURIDICO);

        HashMap <String, String> temp = new HashMap<>();


        if (!isEmpty(personaMoral) ) {
            for (BusquedaPersonaVO vo: personaMoral ) {
                if (vo.getDenominacion() != null)
                    temp.put("denominacion", vo.getDenominacion());
                if (vo.getRfc() != null)
                    temp.put("rfc", vo.getRfc());
            }
        }

    return temp;

    }

    public HashSet<String> getBusquedaPersonaAsStringFromPrelacion (Long prelacionId) {

        Set<BusquedaPersonaVO> busquedaPersonaVO = getPersonasFromPrelacion(prelacionId, Constantes.ETipoPersona.FISICA);

        SimpleDateFormat formatterFecha     = new SimpleDateFormat("dd/MM/yyyy");

        HashSet <String> temp = new HashSet<>();
        if (!isEmpty(busquedaPersonaVO) ) {
            for (BusquedaPersonaVO vo: busquedaPersonaVO ) {

                String str = "";
                if ( !isEmpty(vo.getNombre()) )
                    str = vo.getNombre();
                if ( !isEmpty(vo.getPaterno()) )
                    str += " " + vo.getPaterno();
                if ( !isEmpty(vo.getMaterno()) )
                    str += " " + vo.getMaterno();
                if ( !isEmpty(vo.getRfc()) )
                    str += " " + "  RFC: " + vo.getRfc();
                if ( !isEmpty(vo.getCurp()) )
                    str += " " + "  CURP: "+ vo.getCurp();
                if ( !isEmpty(vo.getFechaNac()) )
                    str += " " + "  Fecha Nac: " + formatterFecha.format( vo.getFechaNac()) .toString();
                if ( !isEmpty(vo.getEstadoCivil()) )
                    str += " " + "  Estado Civil: " + vo.getEstadoCivil().getNombre();
                if ( !isEmpty(vo.getRegimen()) )
                    str += " " + "  Régimen: " + vo.getRegimen().getNombre();
                if ( !isEmpty(vo.getNacionalidad()) )
                    str += " " + ", " + vo.getNacionalidad().getNombre();

                temp.add(str);

            }
        }

        return temp;

    }


    public List<PredioAnteVO> listPredioAnteFromBusqueda (Long prelacionId) {
    	
    	Pageable pageable = new PageRequest(0, 1000);
//      PredioVO     predioVO   = null;
//      PredioAnteVO predioAnteVO = null;
		Page<Predio> pagePredios = listPredioFromBusqueda(prelacionId, pageable);
            //log.info("pagePredios.getSize():"+pagePredios.getSize());
		    List<PredioAnteVO> listaPredioVo = null;
			listaPredioVo = new ArrayList<>();
			List<Predio> listPredios = pagePredios!=null ?  pagePredios.getContent() : new ArrayList<>();
			listaPredioVo = this.getPrediosAnteVO(listPredios,prelacionId);
            log.debug("Transformado a PredioAnte: {}", listaPredioVo);
            pagePredios = null;

		return listaPredioVo;
    }
    
    public List<PersonaJuridicaAnteVO> listPersonaJuridicaFromBusqueda (Long prelacionId) {
        List<PersonaJuridicaAnteVO> listaPersonaJuridicaVo = null;
        listaPersonaJuridicaVo = new ArrayList<>();

        Pageable pageable = new PageRequest(0, 1000);
        Page<PersonaJuridica> pagePersonaJuridica= listPersonaJuridicaFromBusqueda(prelacionId, pageable);

        List<PersonaJuridica> listPersonasJuridicas = pagePersonaJuridica!=null ?  pagePersonaJuridica.getContent() : new ArrayList<>();
		listaPersonaJuridicaVo = this.getPersonasJuridicasAnteVO(listPersonasJuridicas,prelacionId);
        log.debug("Transformado a PredioAnte: {}", listaPersonaJuridicaVo);
        pagePersonaJuridica = null;

        return listaPersonaJuridicaVo;
    }

    private List<PredioAnteVO> getPrediosAnteVO(List<Predio> listPredios,Long prelacionId) {
		List<PredioAnteVO> listaPredioVo = new ArrayList<PredioAnteVO>();
		Antecedente antecedente = null;
		AntecedenteVO antecedenteVO = null;
		PredioVO predioVO = null;
		PredioAnteVO predioAnteVO = null;
		Set<BusquedaResultado> prediosSelected =  busquedaResultadoService.getPrediosFromPrelacionId(prelacionId);
		for(BusquedaResultado resul: prediosSelected) {
			predioVO = new PredioVO();
			predioService.copyProperties(resul.getPredio(), predioVO);
			predioAnteVO = new PredioAnteVO();
			predioAnteVO.setPredio(predioVO);
			predioAnteVO.setAntecedente(antecedenteVO);
			predioAnteVO.setSeleccionado(true);
			listaPredioVo.add(predioAnteVO);
		}
		
		for (Predio pre : listPredios) {
			Optional<PredioAnteVO> exist = listaPredioVo.stream().filter(x->x.getPredio().getId().equals(pre.getId())).findAny();
			if(!exist.isPresent() && pre.getStatusActo().getId()==1L) {
				predioVO = new PredioVO();
				predioService.copyProperties(pre, predioVO);
				//antecedente = antecedenteRepository.findAntecedenteByPredioId(pre.getId());
				//antecedenteVO = AntecedenteVO.antecedenteTransform(antecedente);
				// antecedenteService.copyProperties(antecedente, antecedenteVO);
				predioAnteVO = new PredioAnteVO();
				predioAnteVO.setPredio(predioVO);
				predioAnteVO.setAntecedente(antecedenteVO);
				predioAnteVO.setSeleccionado(false);
				listaPredioVo.add(predioAnteVO);
			}
			
		}
		return listaPredioVo;
	}

    private List<PersonaJuridicaAnteVO> getPersonasJuridicasAnteVO(List<PersonaJuridica> listPersonasJuridicas,Long prelacionId) {
		List<PersonaJuridicaAnteVO> listaPersonaJuridicaVo = new ArrayList<PersonaJuridicaAnteVO>();
		AntecedenteVO antecedenteVO = null;
		
		PersonaJuridicaAnteVO personaJuridicaAnteVO = null;
		Set<BusquedaResultado> prediosSelected =  busquedaResultadoService.getPrediosFromPrelacionId(prelacionId);
		for(BusquedaResultado resul: prediosSelected) {	
			personaJuridicaAnteVO = new PersonaJuridicaAnteVO();
            personaJuridicaAnteVO.setPersonaJuridica(resul.getPersonaJuridica());			
			personaJuridicaAnteVO.setAntecedente(antecedenteVO);
			personaJuridicaAnteVO.setSeleccionado(true);
			listaPersonaJuridicaVo.add(personaJuridicaAnteVO);
		}
		
		for (PersonaJuridica pj : listPersonasJuridicas) {
			Optional<PersonaJuridicaAnteVO> exist = listaPersonaJuridicaVo.stream().filter(x->x.getPersonaJuridica().getId().equals(pj.getId())).findAny();
			if(!exist.isPresent() && pj.getBloqueado()==false) {				
				personaJuridicaAnteVO = new PersonaJuridicaAnteVO();
				personaJuridicaAnteVO.setPersonaJuridica(pj);
				personaJuridicaAnteVO.setAntecedente(antecedenteVO);
				personaJuridicaAnteVO.setSeleccionado(false);
				listaPersonaJuridicaVo.add(personaJuridicaAnteVO);
			}			
		}
		return listaPersonaJuridicaVo;
	}

    public Busqueda deleteBusquedaPersonasFromPrelacion(Long busquedaId) {
        Busqueda busqueda = busquedaRepository.findOne(busquedaId);
        HashSet<DetalleBusqueda> detalles = detalleBusquedaRepository.findAllByBusqueda(busqueda);
        if (!isEmpty(detalles)) {
            detalleBusquedaRepository.delete(detalles);
        }
        busquedaRepository.delete(busqueda);

        return busqueda;
    }
    private  ArrayList<ActoPredioCampo> getActoPredioCampo(Acto acto){
        ArrayList<ActoPredioCampo> actoPredioCampos = null;
        if(acto!=null){    
            actoPredioCampos =new ArrayList();
            ActoPredio actoPredio = actoPredioRepository.findActoPredioByLastVersion(acto.getId());
            log.debug("BusquedaServ:810 {}", actoPredio);  
                for(ActoPredioCampo apc:actoPredioCampoRepository.findByActoPredioId(actoPredio.getId())){
                    actoPredioCampos.add(apc);
                    log.debug("BusquedaServ:810 {}", apc);
                    }
                }
    return actoPredioCampos;
    }
    
    
    private Boolean isPredioSelected(Long predioId,Set<BusquedaResultado> prediosSelected) {
    	if(prediosSelected != null && prediosSelected.size() > 0)
    		return prediosSelected.stream().filter(x-> (x.getPredio()!= null && x.getPredio().getId().equals(predioId))).count()>0;
    	else
    		return false;
    }
    
    
    @Transactional
	public Boolean deleteBusqueda(Long prelacionId) {
		List<Busqueda> busquedas = busquedaRepository.findByPrelacionId(prelacionId);
		busquedas.forEach(x -> {
			this.deleteBusquedaPersonasFromPrelacion(x.getId());
		});
		return true;
	}
    
    
    public byte[] getPdfBusqueda(Long prelacionId,boolean marcaAgua) throws JRException {
    	 RespuestaBusquedaSimpleVO busqueda = this.getReporteBusquedaSimple(prelacionId);
         List<RespuestaBusquedaSimpleVO> respuestas = new ArrayList<RespuestaBusquedaSimpleVO>();
         respuestas.add(busqueda);
         JRDataSource ds = new JRBeanCollectionDataSource(respuestas);

         Map<String, Object> parameterMap = new HashMap<String, Object>();
         String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+prelacionId;
         parameterMap.put("datasource", ds);
         parameterMap.put("path", Constantes.IMG_PATH);
         parameterMap.put("imgPath", Constantes.IMG_PATH);
         parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
         parameterMap.put("QR_BASE_URI", qR);
         if(marcaAgua)
        	 parameterMap.put("marcaDeAgua", Constantes.IMG_PATH);
         
         InputStream jasperStream = this.getClass().getResourceAsStream(
 				"/reports/busquedaSimple/pdfRespuestaBusquedaHistorial.jasper");

 		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
 		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, ds);

 		return JasperExportManager.exportReportToPdf(jasperPrint);
    }


}
