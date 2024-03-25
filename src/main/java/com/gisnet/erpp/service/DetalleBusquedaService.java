package com.gisnet.erpp.service;

import com.gisnet.erpp.domain.*;
import com.gisnet.erpp.repository.*;
import com.gisnet.erpp.vo.BusquedaPersonaVO;
import com.gisnet.erpp.web.api.catalogo.CatalogoRestController;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class DetalleBusquedaService {

    private final long NOMBRE = 286;
    private final long PATERNO = 287;
    private final long MATERNO = 288;
    private final long FECHA_NACIMIENTO = 289;
    private final long RFC = 290;
    private final long CURP = 291;
    private final long ESTADO_CIVIL = 292;
    private final long REGIMEN = 293;
    private final long NACIONALIDAD = 119;
    private final long TIPO_DE_PERSONA = 84;

    @Autowired
    private DetalleBusquedaRepository detalleBusquedaRepository;
    @Autowired
    private BusquedaRepository        busquedaRepository;

    @Autowired
    private NacionalidadRepository nacionalidadRepository;

    @Autowired
    private RegimenRepository regimenRepository;

    @Autowired
    private EstadoCivilRepository estadoCivilRepository;

    public HashSet<DetalleBusqueda> getDetallesFrom(Object object, Long busquedaId) {

        HashSet detalles = null;
        System.out.println(object);
        if (object.getClass() == Predio.class) {
            detalles = getDetallesFromPredio((Predio) object);

        }

        if (object instanceof BusquedaPersonaVO) {
            detalles = getDetallesFromPersona((BusquedaPersonaVO) object, busquedaId);
        }
        return detalles;
    }

    private HashSet getDetallesFromPredio(Predio object) {
        HashSet detalles = new HashSet<DetalleBusqueda>();
        Predio predio = object;


        return detalles;

    }

    private HashSet<DetalleBusqueda> getDetallesFromPersona(BusquedaPersonaVO object, Long busquedaId) {
        HashSet detalles = new HashSet<DetalleBusqueda>();
        BusquedaPersonaVO persona = object;

        //DetalleBusqueda det = null;

        Stream<String> listaStrings = Stream.of("Nombre", "Paterno", "Materno", "Rfc", "Curp");

        try {

            Class noparams[] = {};

            Class cls = Class.forName("com.gisnet.erpp.vo.BusquedaPersonaVO");

            listaStrings.forEach( property -> {
                try {
                    Method method = cls.getMethod("get"+property, noparams);
                    String str = (String )method.invoke(persona, null);

                    DetalleBusqueda det = detalleBusquedaRepository.findFirstByClaveAndBusquedaId(property.toLowerCase() , busquedaId);

                    if (!isEmpty(str)) {
                        if (det == null)
                            det = new DetalleBusqueda();
                        det.setClave(property.toLowerCase());
                        det.setValor(str);
                        detalles.add(det);
                    }
                    else {
                        if (!isEmpty(det))
                            det.setValor("");
                    }
                } catch (Exception except) {
                    except.printStackTrace();
                }
            });

        }catch (Exception except){
            except.printStackTrace();
        }
        /*DetalleBusqueda det = detalleBusquedaRepository.findFirstByClaveAndBusquedaId("nombre", busquedaId);

        if (!isEmpty(persona.getNombre())) {

            if (det == null) {
                det = new DetalleBusqueda();
            }
            det.setClave("nombre");
            det.setValor(persona.getNombre());
            detalles.add(det);
        }
        else {
            if (!isEmpty(det))
                det.setValor("");
        }

        if (!this.isEmpty(persona.getPaterno())) {
            DetalleBusqueda det = detalleBusquedaRepository.findFirstByClaveAndBusquedaId("paterno", busquedaId);
            if (det == null) {
                det = new DetalleBusqueda();
            }
            det.setClave("paterno");
            det.setValor(persona.getPaterno());
            detalles.add(det);
        }

        if (!this.isEmpty(persona.getMaterno())) {
            DetalleBusqueda det = detalleBusquedaRepository.findFirstByClaveAndBusquedaId("materno", busquedaId);
            if (det == null) {
                det = new DetalleBusqueda();
            }
            det.setClave("materno");
            det.setValor(persona.getMaterno());
            detalles.add(det);
        }

        if (!this.isEmpty(persona.getRfc())) {
            DetalleBusqueda det = detalleBusquedaRepository.findFirstByClaveAndBusquedaId("rfc", busquedaId);
            if (det == null) {
                det = new DetalleBusqueda();
            }
            det.setClave("rfc");
            det.setValor(persona.getRfc());
            detalles.add(det);
        }

        if (!this.isEmpty(persona.getCurp())) {
            DetalleBusqueda det = detalleBusquedaRepository.findFirstByClaveAndBusquedaId("curp", busquedaId);
            if (det == null) {
                det = new DetalleBusqueda();
            }
            det.setClave("curp");
            det.setValor(persona.getCurp());
            detalles.add(det);
        }
         */
        if (persona.getFechaNac() != null) {

            //Clear time part of date

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String fechaFormated = "";
            try {
                // Correcciones  a las fechas por parse de Angular a Springboot
                DateTime tempDate = new DateTime(persona.getFechaNac());
                 fechaFormated = sdf.format(tempDate.plusDays(1).toDate() );

            } catch (Exception e) {
                e.printStackTrace();
            }

            DetalleBusqueda det = detalleBusquedaRepository.findFirstByClaveAndBusquedaId("fechaNac", busquedaId);
            if (det == null) {
                det = new DetalleBusqueda();
            }
            det.setClave("fechaNac");
            det.setValor(fechaFormated);
            detalles.add(det);
        }

        if (persona.getEstadoCivil() != null) {
            DetalleBusqueda det = detalleBusquedaRepository.findFirstByClaveAndBusquedaId("estadoCivil", busquedaId);
            if (det == null) {
                det = new DetalleBusqueda();
            }
            det.setClave("estadoCivil");
            det.setValor(persona.getEstadoCivil().getId().toString());
            detalles.add(det);
        }

        if (persona.getRegimen() != null) {
            DetalleBusqueda det = detalleBusquedaRepository.findFirstByClaveAndBusquedaId("regimen", busquedaId);
            if (det == null) {
                det = new DetalleBusqueda();
            }
            det.setClave("regimen");
            det.setValor(persona.getRegimen().getId().toString());
            detalles.add(det);
        }

        if (persona.getNacionalidad() != null) {
            DetalleBusqueda det = detalleBusquedaRepository.findFirstByClaveAndBusquedaId("nacionalidad", busquedaId);
            if (det == null) {
                det = new DetalleBusqueda();
            }
            det.setClave("nacionalidad");
            det.setValor(persona.getNacionalidad().getId().toString());
            detalles.add(det);
        }

        return detalles;
    }

    public void save(Set<DetalleBusqueda> detalleBusquedas, Busqueda busqueda) {

        if (detalleBusquedas != null && detalleBusquedas.size() > 0) {
            for (DetalleBusqueda det : detalleBusquedas) {
                det.setBusqueda(busqueda);

                detalleBusquedaRepository.saveAndFlush(det);
            }
        }
    }

    public HashSet<DetalleBusqueda> getDetallesFromHashMap(HashMap<String, String> predio, Long busquedaId) {

        HashSet<DetalleBusqueda>  detalles = new HashSet<DetalleBusqueda>();
        Iterator it       = predio.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> detail = (Map.Entry) it.next();
            System.out.println("Verificando: " + detail.getKey());

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            try {
                //log.debug(ow.writeValueAsString(prelacion));
                System.out.println(ow.writeValueAsString(detail));
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        
            DetalleBusqueda det = detalleBusquedaRepository.findFirstByClaveAndBusquedaId(detail.getKey(), busquedaId);
            if (det == null) {
                det = new DetalleBusqueda();
                det.setClave(detail.getKey());
                det.setValor(detail.getValue());
                System.out.println("+ " + det.getClave() + ": " + det.getValor());
            } else {
                det.setClave(detail.getKey());
                det.setValor(detail.getValue());
                System.out.println("* " + detail.getKey() + ": " + detail.getValue());

            }

            detalles.add(det);

        }
        return detalles;
    }

    public HashSet<DetalleBusqueda> findDetallesFromBusqueda (Busqueda busqueda) {
        return detalleBusquedaRepository.findAllByBusqueda (busqueda);
    }

    public BusquedaPersonaVO getPersonaModelFromDetalles(HashSet<DetalleBusqueda> properties) {
        BusquedaPersonaVO persona = new BusquedaPersonaVO ();
        for (DetalleBusqueda tupla: properties) {
           switch (tupla.getClave()) {
               case "nombre" :      persona.setNombre (tupla.getValor()); break;
               case "materno":      persona.setMaterno (tupla.getValor()); break;
               case "paterno":      persona.setPaterno (tupla.getValor()); break;
               case "rfc":          persona.setRfc (tupla.getValor()); break;
               case "curp":         persona.setCurp (tupla.getValor()); break;
               case "estadoCivil":  persona.setEstadoCivil ( estadoCivilRepository.findOne (Long.valueOf(tupla.getValor()  ))); break;
               case "regimen":      persona.setRegimen (regimenRepository.findOne (Long.valueOf(tupla.getValor()  ))); break;
               case "nacionalidad": persona.setNacionalidad (nacionalidadRepository .findOne (Long.valueOf(tupla.getValor()  ))); break;
               case "fechaNac":     persona.setFechaNac (fechaNacimientoFromString (tupla.getValor() )); break;

               case "denominacion": persona.setDenominacion (tupla.getValor());
           }
        }
        return persona;
    }

    private EstadoCivil estadoCivilFromString(String str) {
        return estadoCivilRepository.findDistinctByNombreEquals(str);
    }

    private Regimen regimenFromString(String str) {
        return regimenRepository.findDistinctByNombreEquals(str);
    }
    private Nacionalidad nacionalidadFromString(String str) {
        return nacionalidadRepository.findDistinctByNombreEquals(str);
    }

    private Date fechaNacimientoFromString (String valor) {
        if(valor == null){
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);

        try {

            Date date = sdf.parse(valor);
            return date;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void clearEmpty(Set<DetalleBusqueda> detalleBusquedas, Busqueda busqueda) {
        HashSet<DetalleBusqueda> dets = detalleBusquedaRepository.findAllByValorOrValorIsNull("");

        if (!isEmpty(dets))
            detalleBusquedaRepository.delete(dets);

    }
}


