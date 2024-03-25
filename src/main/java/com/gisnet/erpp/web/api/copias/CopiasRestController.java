package com.gisnet.erpp.web.api.copias;


import com.gisnet.erpp.domain.Busqueda;
import com.gisnet.erpp.domain.BusquedaResultado;
import com.gisnet.erpp.domain.FoliosrDigital;
import com.gisnet.erpp.domain.TipoActo;
import com.gisnet.erpp.security.Http401UnauthorizedEntryPoint;
import com.gisnet.erpp.service.BusquedaResultadoService;
import com.gisnet.erpp.service.CopiasService;
import com.gisnet.erpp.service.PrelacionContratanteService;
import com.gisnet.erpp.service.TipoActoService;
import com.gisnet.erpp.vo.Copias.CopiaVO;
import com.gisnet.erpp.web.api.foliosrdig.LibroDTO;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.util.ObjectUtils.isEmpty;

@RestController
@RequestMapping(value = "/api/copias", produces = MediaType.APPLICATION_JSON_VALUE)
public class CopiasRestController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String APPLICATION_PDF = "application/pdf";

    @Autowired
    CopiasService copiasService;

    @Autowired
    TipoActoService tipoActoService;

    @Autowired
    BusquedaResultadoService busquedaResultadoService;


    @PostMapping("/simple/folio")
    public ResponseEntity<?> copiasSimpleFolio (@RequestBody CopiaVO copia) {


        //return new ResponseEntity<> (copiasService.getSimpleFromFolio(), HttpStatus.OK);
        return new ResponseEntity<> (copiasService.findHistoricoByCopiasByModel(copia), HttpStatus.OK);

    }

    @PostMapping("/impresion")
    public ResponseEntity<?> impresonFolio (@RequestBody CopiaVO copia) {

        List<CopiaVO> tmp = copiasService.findFoliosByCopiasModel(copia);
        //return new ResponseEntity<> (copiasService.getSimpleFromFolio(), HttpStatus.OK);
        return new ResponseEntity<> (tmp, isEmpty(tmp) ? HttpStatus.NOT_FOUND : HttpStatus.OK);

    }

    @PostMapping("/impresion/by-antecedente")
    public ResponseEntity<?> getImpresionByAntecedente (@RequestBody CopiaVO copia) {


        Object tmp = copiasService.findFoliosParaImpresionByAntecedente(copia);

        return new ResponseEntity<> (tmp, tmp == null? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }


    @PostMapping("/simple/folios/{prelacionId}")
    public ResponseEntity<?> saveCopiasSimpleFolio (@RequestBody List <FoliosrDigital> copia, @PathVariable Long prelacionId) {



            return new ResponseEntity<> (busquedaResultadoService.guardarCopia(copia, prelacionId), HttpStatus.OK);


    }

    @PostMapping("/busqueda-antecedente/{prelacionId}")
    public ResponseEntity<?> saveCopiasBusquedaAntedecente (@PathVariable Long prelacionId, @RequestBody LibroDTO copia) {
        try {
            return new ResponseEntity<> (busquedaResultadoService.guardar(prelacionId,copia), HttpStatus.OK);
        }
        catch (Exception except) {
            log.info("Error: " + except.getMessage());
            except.printStackTrace();
            return new ResponseEntity<> ("Error: " + except.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/busqueda-antecedenteUpdate/{prelacionId}/{copiaId}")
    public ResponseEntity<?> saveCopiasBusquedaAntedecenteUpdate (@PathVariable Long prelacionId, @PathVariable Long copiaId, @RequestBody LibroDTO copia) {
        try {
            return new ResponseEntity<> (busquedaResultadoService.guardarUpdate(prelacionId, copiaId,copia), HttpStatus.OK);
        }
        catch (Exception except) {
            log.info("Error: " + except.getMessage());
            except.printStackTrace();
            return new ResponseEntity<> ("Error: " + except.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/impresion/{prelacionId}")
    public ResponseEntity<?> saveImpresionFolio (@RequestBody List <CopiaVO> copia, @PathVariable Long prelacionId) {

        return new ResponseEntity<> (busquedaResultadoService.guardarBusquedaImpresion(copia, prelacionId), HttpStatus.OK);
    }

    @PostMapping("/impresion/antecedente/{prelacionId}")
    public ResponseEntity<?> saveImpresionFromAntecedenteFolio (@RequestBody BusquedaResultado br, @PathVariable Long prelacionId) {

        return new ResponseEntity<> (busquedaResultadoService.guardarBusquedaImpresionFromAntecedente(br, prelacionId), HttpStatus.OK);
    }

    @PutMapping("/simple/folios/{prelacionId}")
    public ResponseEntity<?> updateCopiasSimpleFolio (@RequestBody List <FoliosrDigital> copia, @PathVariable Long prelacionId) {

        return new ResponseEntity<> (busquedaResultadoService.guardarCopia(copia, prelacionId), HttpStatus.OK);
    }



    @DeleteMapping("/{prelacionId}/{copiaId}")
    public ResponseEntity<?> deleteCopiaItem (@PathVariable Long prelacionId, @PathVariable Long copiaId ) {

        try {
            BusquedaResultado tmp = busquedaResultadoService.removeCopiaItem(copiaId, prelacionId);
            if (isEmpty(tmp))
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(Collections.singletonList(tmp), HttpStatus.OK);
        }
        catch (Exception except) {
            log.error(except.toString());
            return new ResponseEntity<>(except.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @DeleteMapping("/prelacion/{prelacionId}")
    public ResponseEntity<?> clearPrelacion (@PathVariable Long prelacionId) {

        try {
            Boolean tmp = busquedaResultadoService.clearBusquedaFromPrelacion( prelacionId);
            if (tmp == true)
                return new ResponseEntity<>(prelacionId, HttpStatus.OK);

            else
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        catch (Exception except) {
            log.error(except.toString());
            return new ResponseEntity<>(except.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PostMapping("/hojas-seleccionadas/{busquedaId}")
    public ResponseEntity<?> saveHojasSeleccionadas (@RequestBody List <String> hojas, @PathVariable Long busquedaId) {

            boolean resultado= copiasService.guardaHojasSeleccionadas(busquedaId, hojas);

            if(resultado){
                return new ResponseEntity<> (resultado, HttpStatus.OK);

            }else{
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
    }

    @RequestMapping(value = "/getImagenesCopias/{busqueda}", method = RequestMethod.GET, produces = APPLICATION_PDF)
    @ResponseBody HttpEntity<byte[]> getImagesFolio (HttpServletResponse response, @PathVariable Long busqueda)throws IOException {

        byte[] pdf = copiasService.getImagesBytesSeleccionadas(busqueda);

        InputStream in = new ByteArrayInputStream(pdf); 


        
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "pdf"));
        header.set("Content-Disposition", "inline; filename=" + busqueda);
        header.setContentLength(pdf.length);
        return new HttpEntity<byte[]>(pdf, header);
    }

    @RequestMapping(value = "/getImagenesCopiasByPrelacion/{prelacionId}", method = RequestMethod.GET, produces = APPLICATION_PDF)
    @ResponseBody HttpEntity<byte[]> getImagesByPrelacion (HttpServletResponse response, @PathVariable Long prelacionId)throws IOException {

        byte[] pdf = copiasService.getImagesBytesSeleccionadasByPrelacion(prelacionId);

        InputStream in = new ByteArrayInputStream(pdf); 


        
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "pdf"));
        header.set("Content-Disposition", "inline; filename=" + prelacionId);
        header.setContentLength(pdf.length);
        return new HttpEntity<byte[]>(pdf, header);
    }
}
