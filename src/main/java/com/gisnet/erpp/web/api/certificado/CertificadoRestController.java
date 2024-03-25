package com.gisnet.erpp.web.api.certificado;


import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.LongStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gisnet.erpp.domain.*;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.PrelacionPredioRepository;

import com.gisnet.erpp.repository.SeccionRepository;
import com.gisnet.erpp.service.*;
import com.gisnet.erpp.vo.prelacion.ModificarPrelacionVO;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.PrelacionIngresoVO;
import com.gisnet.erpp.vo.RequisitoVO;
import com.gisnet.erpp.vo.ResultPrelacionVO;
import com.gisnet.erpp.vo.AntecedenteVO;
import com.gisnet.erpp.vo.PrelacionVO;
import com.gisnet.erpp.vo.ReciboVO;
import com.gisnet.erpp.vo.ActoVO;
import com.gisnet.erpp.vo.ServicioAndSubVO;
import com.gisnet.erpp.vo.PredioVO;
import com.gisnet.erpp.vo.ArchivoFirmaVO;
import com.gisnet.erpp.vo.CadenaOriginalVO;
import com.gisnet.erpp.vo.ConsultaPrelacionVO;

import org.springframework.web.multipart.MultipartFile;
import com.gisnet.erpp.web.api.documentos.UploadModel;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.HttpHeaders;

import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.gisnet.erpp.service.excepciones.RequerimientoException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gisnet.erpp.domain.Certificado;
import com.gisnet.erpp.domain.Documento;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.TipoAclaracion;
import com.gisnet.erpp.vo.ArchivoFirmaVO;

import com.gisnet.erpp.service.PrelacionService;
import com.gisnet.erpp.service.CertificadoService;

import java.io.IOException;
import net.sf.jasperreports.engine.JRException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/api/certificado", produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificadoRestController {

    
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
	private CertificadoService certificadoService;

    @Autowired
    private PrelacionService prelacionService;
    


    @GetMapping("/{idCert}")
	public ResponseEntity<?> getCertificado(@PathVariable("idCert") Long idCertificado){
        Certificado cer=null;
        try {

            if (idCertificado != null) {
                    
                        cer = certificadoService.findByPrelacionId(idCertificado);
                    
                        if(cer==null){
                            return new ResponseEntity<>("No se pudo recuperar  el certificado con los datos de la prelacion",HttpStatus.BAD_REQUEST);

                        }   
            }else{
                return new ResponseEntity<>("No existen datos  el certificado son incorrectos",HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception err) {
            err.printStackTrace();
            System.out.println(err.getMessage());
            return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
        }
    
        return new ResponseEntity<>(cer, HttpStatus.OK);
    }


    @PostMapping("/")
	public ResponseEntity<?> creaCertificado(@RequestBody Prelacion prelacion){
        Certificado cer=null;
        try {

		if (prelacion != null) {
                    prelacion = prelacionService.findOne(prelacion.getId());
    			    cer = certificadoService.guardarCertificado(prelacion,0);
				
                    if(cer==null){
                        return new ResponseEntity<>("No se pudo generar el certificado con los datos de la prelacion",HttpStatus.BAD_REQUEST);

                    }   
		}else{
			return new ResponseEntity<>("Los datos de ingreso para guardar el certificado son incorrectos",HttpStatus.BAD_REQUEST);
		}

    }
    catch (Exception err) {
        err.printStackTrace();
        System.out.println(err.getMessage());
        return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
    }

		return new ResponseEntity<>(cer, HttpStatus.OK);
	}
    
    @PostMapping("/{tipoRespuesta}")
  	public ResponseEntity<?> creaCertificado(@RequestBody Prelacion prelacion,@PathVariable("tipoRespuesta") Integer tipoRespuesta){
          Certificado cer=null;
          try {

  		if (prelacion != null) {
                      prelacion = prelacionService.findOne(prelacion.getId());
      			    cer = certificadoService.guardarCertificado(prelacion,tipoRespuesta);
  				
                      if(cer==null){
                          return new ResponseEntity<>("No se pudo generar el certificado con los datos de la prelacion",HttpStatus.BAD_REQUEST);

                      }   
  		}else{
  			return new ResponseEntity<>("Los datos de ingreso para guardar el certificado son incorrectos",HttpStatus.BAD_REQUEST);
  		}

      }
      catch (Exception err) {
          err.printStackTrace();
          System.out.println(err.getMessage());
          return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
      }

  		return new ResponseEntity<>(cer, HttpStatus.OK);
  	}


    @PutMapping("/")
	public ResponseEntity<?> guardaHistorialCertificado(@RequestBody Certificado certificado) throws URISyntaxException {
        
		try { 
            if(certificado!=null)
                certificado= certificadoService.updateCertificado(certificado);
            else
                return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);


		}
		catch (Exception err) {
			err.printStackTrace();
            System.out.println(err.getMessage());
            return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
	    }
		
		return new ResponseEntity<>(certificado, HttpStatus.OK);
	}
    
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCertificado(@PathVariable("id") Long id){
    	try {
    		certificadoService.delete(id);
    		return new ResponseEntity<>(true, HttpStatus.OK);
    	}catch(Exception e) {
    		return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
    	}
    }


}