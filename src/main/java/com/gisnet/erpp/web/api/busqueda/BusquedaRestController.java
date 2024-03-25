package com.gisnet.erpp.web.api.busqueda;

import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.PredioAnte;
import com.gisnet.erpp.service.BusquedaResultadoService;
import com.gisnet.erpp.service.BusquedaService;

import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.BusquedaVO;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
//import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@RestController
@RequestMapping(value = "/api/busqueda", produces = MediaType.APPLICATION_JSON_VALUE)
public class BusquedaRestController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private BusquedaService busquedaService;

    @Autowired
    private BusquedaResultadoService busquedaResultadoService;


    @PostMapping(value="/predio/")
    public ResponseEntity<?> savePredio (@RequestBody BusquedaVO busqueda) {
        try {
            if (busqueda.predioStr == null)
                return new ResponseEntity<> ("Error: Se necesitan datos del predio", HttpStatus.INTERNAL_SERVER_ERROR);

            HashMap<String,String> mapPredio = new Gson().fromJson( busqueda.predioStr, new TypeToken<HashMap<String, String>>(){}.getType());
            Long prelacionId = busqueda.prelacion;
            return new ResponseEntity<> (busquedaService.savePredio (mapPredio, prelacionId), HttpStatus.OK);
        }
        catch (Exception except) {
            except.printStackTrace();
            return new ResponseEntity<> ("Error: " + except.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value="/predio/")
    public ResponseEntity<?> updatePredio (@RequestBody BusquedaVO busqueda) {
        try {
            if (busqueda.predioStr == null)
                return new ResponseEntity<> ("Error: Se necesitan datos del predio", HttpStatus.INTERNAL_SERVER_ERROR);

            HashMap<String,String> mapPredio = new Gson().fromJson(busqueda.predioStr, new TypeToken<HashMap<String, String>>(){}.getType());
            Long prelacionId = busqueda.prelacion;
            return new ResponseEntity<> (busquedaService.updatePredio (mapPredio, prelacionId, busqueda.busquedaPredioId), HttpStatus.OK);
        }
        catch (Exception except) {
            except.printStackTrace();
            return new ResponseEntity<> ("Error: " + except.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping(value="/persona")
    public ResponseEntity<?> savePersona(@RequestBody BusquedaVO busqueda) {
        try {

            Long prelacionId = busqueda.prelacion;

            return new ResponseEntity<> (busquedaService.savePersona (busqueda.getPersona(), prelacionId ), HttpStatus.OK);
        }
        catch (Exception except) {
            log.info("Error: " + except.getMessage());
            except.printStackTrace();
            return new ResponseEntity<> ("Error: " + except.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value="/persona-juridica")
    public ResponseEntity<?> savePersonaJuridica(@RequestBody BusquedaVO busqueda) {
        try {

            Long prelacionId = busqueda.prelacion;

            return new ResponseEntity<> (busquedaService.savePersonaJuridica (busqueda.getPersona(), prelacionId ), HttpStatus.OK);
        }
        catch (Exception except) {
            log.info("Error: " + except.getMessage());
            except.printStackTrace();
            return new ResponseEntity<> ("Error: " + except.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value="/persona")
    public ResponseEntity<?> updatePersona(@RequestBody BusquedaVO busqueda) {
        try {

            Long busquedaId = busqueda.getBusquedaPersonaId();

            return new ResponseEntity<> (busquedaService.updatePersona (busqueda.getPersona(), busquedaId ), HttpStatus.OK);
        }
        catch (Exception except) {
            log.info("Error: " + except.getMessage());
            except.printStackTrace();
            return new ResponseEntity<> ("Error: " + except.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value="/persona-juridica")
    public ResponseEntity<?> updatePersonaJuridica(@RequestBody BusquedaVO busqueda) {
        try {

            Long busquedaId = busqueda.getBusquedaPersonaId();

            return new ResponseEntity<> (busquedaService.updatePersonaJuridica (busqueda.getPersona(), busquedaId ), HttpStatus.OK);
        }
        catch (Exception except) {
            log.info("Error: " + except.getMessage());
            except.printStackTrace();
            return new ResponseEntity<> ("Error: " + except.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value="/personas-fisicas/{prelacionId}")
    public ResponseEntity<?> getPersonasFisicas(@PathVariable("prelacionId")Long prelacionId) {

        if (prelacionId == null) {
            return new ResponseEntity<> ( HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<> (busquedaService.getPersonasFromPrelacion (prelacionId, Constantes.ETipoPersona.FISICA), HttpStatus.OK);
    }

    @DeleteMapping(value="/persona-fisica/{busquedaId}")
    public ResponseEntity<?> deletePersonasFisicas(@PathVariable("busquedaId")Long busquedaId) {

        if (busquedaId == null) {
            return new ResponseEntity<> ( HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<> (busquedaService.deleteBusquedaPersonasFromPrelacion (busquedaId), HttpStatus.OK);
    }

    @DeleteMapping(value="/persona-moral/{busquedaId}")
    public ResponseEntity<?> deletePersonasMorales(@PathVariable("busquedaId")Long busquedaId) {

        return this.deletePersonasFisicas(busquedaId);

    }


    @GetMapping(value="/personas-morales/{prelacionId}")
    public ResponseEntity<?> getPersonasMorales(@PathVariable("prelacionId")Long prelacionId) {

        if (prelacionId == null) {
            return new ResponseEntity<> ( HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<> (busquedaService.getPersonasFromPrelacion (prelacionId, Constantes.ETipoPersona.MORAL), HttpStatus.OK);
    }

    @GetMapping(value="/personas-juridico/{prelacionId}")
    public ResponseEntity<?> getPersonasJuridico(@PathVariable("prelacionId")Long prelacionId) {

        if (prelacionId == null) {
            return new ResponseEntity<> ( HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<> (busquedaService.getPersonasFromPrelacion (prelacionId, Constantes.ETipoPersona.JURIDICO), HttpStatus.OK);
    }

    @GetMapping(value="/inmuebles/{prelacionId}")
    public ResponseEntity<?> getInmuebles(@PathVariable("prelacionId")Long prelacionId) {

        if (prelacionId == null) {
            return new ResponseEntity<> ( HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<> (busquedaService.getInmueblesFromPrelacion (prelacionId), HttpStatus.OK);
    }

    @GetMapping(value="/verifica/{prelacionId}")
    public ResponseEntity<?> verificaBusquedas(@PathVariable("prelacionId")Long prelacionId) {

        if (prelacionId == null) {
            return new ResponseEntity<> ( HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<> (busquedaService.getTipoBusquedasFromPrelacion (prelacionId), HttpStatus.OK);
    }
    @GetMapping(value="/predio-by-prelacion/{prelacionId}")
    public ResponseEntity<?> buscarPorPrelacion(Pageable pageable,@PathVariable("prelacionId")Long prelacionId) {

        if (prelacionId == null) {
            return new ResponseEntity<> ( HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<> (busquedaService.listPredioFromBusqueda (prelacionId, pageable), HttpStatus.OK);
    }

    @GetMapping(value="/predioante-by-prelacion/{prelacionId}")
    public ResponseEntity<?> buscarPredioAntePorPrelacion(@PathVariable("prelacionId")Long prelacionId) {

        if (prelacionId == null) {
            return new ResponseEntity<> ( HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<> (busquedaService.listPredioAnteFromBusqueda (prelacionId), HttpStatus.OK);
    }



    @PostMapping(value="/busqueda-guardada/{prelacionId}")
    public ResponseEntity<?> saveBusquedaResultado(@PathVariable("prelacionId") Long prelacionId, @RequestBody List<Long> prediosAnte) {
        try {

            List<Long> predios  = new ArrayList<>();
            for (Long predioAnte : prediosAnte ) {
                predios.add(predioAnte);
            }

            if (isEmpty (predios) ) {
                return new ResponseEntity<> ("Error: Faltan datos de los predios a guardar" , HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<> (busquedaResultadoService.guardar(predios, prelacionId), HttpStatus.OK);
        }
        catch (Exception except) {
            log.info("Error: " + except.getMessage());
            except.printStackTrace();
            return new ResponseEntity<> ("Error: " + except.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping(value="/busqueda-guardada/{prelacionId}")
    public ResponseEntity<?> buscarBusquedasGuardadas(@PathVariable("prelacionId")Long prelacionId) {

        if (prelacionId == null) {
            return new ResponseEntity<> ( HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<> (busquedaResultadoService.getPrediosFromPrelacionId(prelacionId), HttpStatus.OK);
    }




    @GetMapping(value="/busqueda-guardada-actos/{busquedaId}")
    public ResponseEntity<?> buscarBusquedasGuardadasActos(@PathVariable("busquedaId")Long busquedaId) {

        if (busquedaId == null) {
            return new ResponseEntity<> ( HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<> (busquedaResultadoService.getActosFromBusquedaId(busquedaId), HttpStatus.OK);
    }
    
    @PostMapping(value="/save-predio/{prelacionId}/{predioId}")
    public ResponseEntity<?> updateBusquedaPrediosResult(@PathVariable("prelacionId")Long prelacionId,@PathVariable("predioId")Long predioId){
    	return new ResponseEntity<>(busquedaResultadoService.savePredio(prelacionId, predioId), HttpStatus.OK);
    }
    
    @PostMapping(value="/save-pj/{prelacionId}/{pjId}")
    public ResponseEntity<?> updateBusquedaPersonasJuridicasResult(@PathVariable("prelacionId")Long prelacionId,@PathVariable("pjId")Long pjId){
    	return new ResponseEntity<>(busquedaResultadoService.savePersonaJuridica(prelacionId, pjId), HttpStatus.OK);
    }
    
    @DeleteMapping(value="/delete-predio/{prelacionId}/{predioId}")
    public ResponseEntity<?> deleteBusquedaPrediosResult(@PathVariable("prelacionId")Long prelacionId,@PathVariable("predioId")Long predioId){
    	return new ResponseEntity<>(busquedaResultadoService.deletePredio(prelacionId, predioId), HttpStatus.OK);
    }
    
    @DeleteMapping(value="/delete-pj/{prelacionId}/{pjId}")
    public ResponseEntity<?> deleteBusquedaPersonaJuridicaResult(@PathVariable("prelacionId")Long prelacionId,@PathVariable("pjId")Long pjId){
    	return new ResponseEntity<>(busquedaResultadoService.deletePersonaJuridica(prelacionId, pjId), HttpStatus.OK);
    }

    @DeleteMapping(value="/delete-busqueda-resultado/{prelacionId}")
    public ResponseEntity<?> deleteBusquedasResultadoByPrelacion(@PathVariable("prelacionId")Long prelacionId){
    	try {
			return new ResponseEntity<>(busquedaResultadoService.clearBusquedaFromPrelacion(prelacionId), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(false, HttpStatus.CONFLICT);
		}
    }



}