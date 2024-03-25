package com.gisnet.erpp.web.api.formatos;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gisnet.erpp.domain.Colindancia;
import com.gisnet.erpp.service.*;
import com.gisnet.erpp.service.reportes.AvisoCautelarService;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.AvisoCautelarVO;
import com.gisnet.erpp.vo.caratula.*;
import com.gisnet.erpp.vo.prelacion.ActoModel;
import com.gisnet.erpp.vo.prelacion.PrelacionBoletaRegistralVO;
import com.itextpdf.text.Document;
import com.itextpdf.text.Document.*;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Paragraph.*;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.data.JRTableModelDataSource;

import org.apache.commons.io.IOUtils;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

import com.gisnet.erpp.vo.juridico.*;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.repository.ParametroRepository;
import com.gisnet.erpp.util.ServletUtil;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@RequestMapping("/api/imprime/formatos/")
@Controller
public class ImprimeFormatosController {

    @Autowired
    BusquedaService busquedaService;

    @Autowired
    private AvisoCautelarService avisoCautelarService;
    @Autowired
    CaratulaService   caratulaService;
    @Autowired
    ActoPredioService actoPredioService;
    @Autowired
    PredioService     predioService;
    @Autowired
    PdfService        pdfService;
    @Autowired
    BoletaRegistralService boletaRegistralService;

    @Autowired
	ConfigurableApplicationContext applicationContext;
    
    @Autowired
    ParametroRepository parametroRepository;
    
    @Autowired
    PrelacionService prelacionService;


    @RequestMapping(value = "printactadepositotestamento/", method = RequestMethod.GET, produces = "application/pdf")
    public ModelAndView printActaDepositoTestamento(Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {

        DepositoTestamentoVO depositoTestamento = new DepositoTestamentoVO();

        List<DepositoTestamentoVO> depositos = new ArrayList<DepositoTestamentoVO>();
        depositos.add(depositoTestamento);
        JRDataSource ds = new JRBeanCollectionDataSource(depositos);

        Map<String, Object> parameterMap = new HashMap<String, Object>();
        String              path         = ServletUtil.getContextFilePath(httpServletRequest);

        parameterMap.put("datasource", ds);
        parameterMap.put("path", Constantes.IMG_PATH);
        parameterMap.put("imgPath", Constantes.IMG_PATH);
        parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);

        JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/pdfActaDepositoTestamento.jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
	    return new ModelAndView(view, parameterMap);
    }


    @RequestMapping(value = "printactaretirotestamento/", method = RequestMethod.GET, produces = "application/pdf")
    public ModelAndView printActaRetiroTestamento(Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {

        ActaDeRetiroVO retiroTestamento = new ActaDeRetiroVO();

        List<ActaDeRetiroVO> retiros = new ArrayList<ActaDeRetiroVO>();
        retiros.add(retiroTestamento);
        JRDataSource ds = new JRBeanCollectionDataSource(retiros);

        Map<String, Object> parameterMap = new HashMap<String, Object>();
        String              path         = ServletUtil.getContextFilePath(httpServletRequest);

        parameterMap.put("datasource", ds);
        parameterMap.put("path", Constantes.IMG_PATH);
        parameterMap.put("imgPath", Constantes.IMG_PATH);
        parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);

        JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/pdfActaRetiroTestamento.jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
	    return new ModelAndView(view, parameterMap);
    }


    @RequestMapping(value = "printactarevocaciontestamento/", method = RequestMethod.GET, produces = "application/pdf")
    public ModelAndView printActaRevocacionTestamento(Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {

        ActaDeRevocacionVO revocacionTestamento = new ActaDeRevocacionVO();

        List<ActaDeRevocacionVO> revocaciones = new ArrayList<ActaDeRevocacionVO>();
        revocaciones.add(revocacionTestamento);
        JRDataSource ds = new JRBeanCollectionDataSource(revocaciones);

        Map<String, Object> parameterMap = new HashMap<String, Object>();
        String              path         = ServletUtil.getContextFilePath(httpServletRequest);

        parameterMap.put("datasource", ds);
        parameterMap.put("path", Constantes.IMG_PATH);
        parameterMap.put("imgPath", Constantes.IMG_PATH);
        parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);

        JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/pdfActaRevocacionTestamento.jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
	    return new ModelAndView(view, parameterMap);
    }


    @RequestMapping(value = "printcertificadodenoinscripcion/", method = RequestMethod.GET, produces = "application/pdf")
    public ModelAndView printCertificadoDeNoInscripcion(Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {

        CertificadoDeNoInscripcionVO certificadoNoIns = new CertificadoDeNoInscripcionVO();

        List<CertificadoDeNoInscripcionVO> certificados = new ArrayList<CertificadoDeNoInscripcionVO>();
        certificados.add(certificadoNoIns);
        JRDataSource ds = new JRBeanCollectionDataSource(certificados);

        Map<String, Object> parameterMap = new HashMap<String, Object>();
        String              path         = ServletUtil.getContextFilePath(httpServletRequest);

        parameterMap.put("datasource", ds);
        parameterMap.put("path", Constantes.IMG_PATH);
        parameterMap.put("imgPath", Constantes.IMG_PATH);
        parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);

        JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/pdfCertificadoDeNoInscripcion.jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
	    return new ModelAndView(view, parameterMap);
    }


    @RequestMapping(value = "printcertificadopersonasjuridicas/", method = RequestMethod.GET, produces = "application/pdf")
    public ModelAndView printCertificadoPersonasJuridicas(Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {

        CertificadoPersonaJuridicasVO certificadoPersonas = new CertificadoPersonaJuridicasVO();

        List<CertificadoPersonaJuridicasVO> certificados = new ArrayList<CertificadoPersonaJuridicasVO>();
        certificados.add(certificadoPersonas);
        JRDataSource ds = new JRBeanCollectionDataSource(certificados);

        Map<String, Object> parameterMap = new HashMap<String, Object>();
        String              path         = ServletUtil.getContextFilePath(httpServletRequest);

        parameterMap.put("datasource", ds);
        parameterMap.put("path", Constantes.IMG_PATH);
        parameterMap.put("imgPath", Constantes.IMG_PATH);
        parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);

        JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/pdfCertificadoPersonasJuridicas.jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
	    return new ModelAndView(view, parameterMap);
    }


    @RequestMapping(value = "printrespuestabusquedasimple/{prelacion}", method = RequestMethod.GET, produces = "application/pdf")
    public ModelAndView printRespuestaBusquedaSimple(@PathVariable("prelacion") Long prelacion, Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {

        RespuestaBusquedaSimpleVO busqueda = busquedaService.getReporteBusquedaSimple(prelacion);
        Prelacion pre = prelacionService.findOne(prelacion);
        List<RespuestaBusquedaSimpleVO> respuestas = new ArrayList<RespuestaBusquedaSimpleVO>();
        respuestas.add(busqueda);
        JRDataSource ds = new JRBeanCollectionDataSource(respuestas);

        Map<String, Object> parameterMap = new HashMap<String, Object>();
        String              path         = ServletUtil.getContextFilePath(httpServletRequest);
        
        String qR=parametroRepository.findByCve("QR_BASE_URI").getValor()+"/api/imprime/certificado/"+prelacion;
        parameterMap.put("datasource", ds);
        parameterMap.put("path", Constantes.IMG_PATH);
        parameterMap.put("imgPath", Constantes.IMG_PATH);
        parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);
        parameterMap.put("QR_BASE_URI", qR);
        System.out.println("***********************"+busqueda.getBusqueda());

        JasperReportsPdfView view = new JasperReportsPdfView();
        if (busqueda.getBusqueda() == 1) {
            view.setUrl("classpath:/reports/busquedaSimple/pdfRespuestaBusquedaSimple.jasper");
        } else {//aqui
            view.setUrl("classpath:/reports/busquedaSimple/pdfRespuestaBusquedaHistorial.jasper");
        }
        view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);

	    return new ModelAndView(view, parameterMap);
    }


    @RequestMapping(value = "printAvisoCautelar/{prelacion}", method = RequestMethod.GET, produces = "application/pdf")
    public ModelAndView printAvisoCautelar(@PathVariable("prelacion") Long prelacion, Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {

        AvisoCautelarVO aviso = avisoCautelarService.getReporteAvisoCautelar(prelacion);

        JRDataSource ds = new JRBeanCollectionDataSource(Collections.singletonList(aviso));

        Map<String, Object> parameterMap = this.getBasicParameterMap();

        parameterMap.put("datasource", ds);

        JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/avisoCautelar/pdfAvisoCautelar.jasper");	   
	    view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
	    return new ModelAndView(view, parameterMap);
    }

    @RequestMapping(value = "printCaratulaMueble/{prelacionFolio}/{oficinaId}", method = RequestMethod.GET, produces = "application/pdf")
    public ModelAndView printCaratulaMueble(@PathVariable("prelacionFolio") Integer prelacionFolio, @PathVariable("oficinaId") Long oficinaId, Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {

        MuebleVO mueble = caratulaService.getCaratulaMueble(prelacionFolio, oficinaId);

        JRDataSource ds            = new JRBeanCollectionDataSource(Collections.singletonList(mueble));
        JRDataSource muebleDS      = new JRBeanCollectionDataSource(Collections.singletonList(mueble));
        JRDataSource antecedenteDS = new JRBeanCollectionDataSource(Collections.singletonList(mueble.getAntecedente()));

        Map<String, Object> parameterMap = this.getBasicParameterMap();

        parameterMap.put("tipoFolio", "MUEBLE");
        parameterMap.put("oficina", oficinaId.toString());
        parameterMap.put("folioReal", prelacionFolio.toString());
        parameterMap.put("datasource", ds);
        parameterMap.put("muebleDS", muebleDS);
        parameterMap.put("antecedenteDS", antecedenteDS);

        JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/caratula/pdfCaratulaMueble.jasper");	   
        view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
        
        return new ModelAndView(view, parameterMap);
        
    }


    @RequestMapping(value = "printCaratulaInmueble/{prelacionFolio}/{oficinaId}", method = RequestMethod.GET, produces = "application/pdf")
    public ModelAndView printCaratulaInmueble(@PathVariable("prelacionFolio") Integer prelacionFolio, @PathVariable("oficinaId") Long oficinaId, Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {

        PredioVO predio = caratulaService.getCaratulaPredio(prelacionFolio, oficinaId);

        List<ColindanciaVO> linderos = predioService.getColindanciaVOFromPredioId(predio.getPredioId());

        Set<PropietarioVO> propietarios = actoPredioService.getPropietarioVOByPredioId(predio.getPredioId());



        JRDataSource ds                    = new JRBeanCollectionDataSource(Collections.singletonList(predio));
        JRDataSource predioDS              = new JRBeanCollectionDataSource(Collections.singletonList(predio));
        JRDataSource antecedenteDS         = new JRBeanCollectionDataSource(Collections.singletonList(predio.getAntecedente()));
        JRDataSource ubicacionDS           = new JRBeanCollectionDataSource(Collections.singletonList(predio));
        JRDataSource vialidadDS            = new JRBeanCollectionDataSource(Collections.singletonList(predio));
        JRDataSource asentamientoDS        = new JRBeanCollectionDataSource(Collections.singletonList(predio));
        JRDataSource zonaDS                = new JRBeanCollectionDataSource(Collections.singletonList(predio));
        JRDataSource entidadFederativaDS   = new JRBeanCollectionDataSource(Collections.singletonList(predio));
        JRDataSource colindanciasDS        = new JRBeanCollectionDataSource(linderos);
        JRDataSource pasesDS               = new JRBeanCollectionDataSource(predio.getPases());
        JRDataSource superficieSegregadaDS = new JRBeanCollectionDataSource(Collections.singletonList(predio));
        JRDataSource titularDS             = new JRBeanCollectionDataSource(propietarios);
        JRDataSource statusDS              = new JRBeanCollectionDataSource(Collections.singletonList(predio));
        JRDataSource devolucionesDS        = new JRBeanCollectionDataSource(predio.getDevoluciones());
        JRDataSource actosDS               = new JRBeanCollectionDataSource(predio.getSumatoriaActosPorClasifActo());
        JRDataSource tramiteDS             = new JRBeanCollectionDataSource(predio.getTramites());

        Map<String, Object> parameterMap = this.getBasicParameterMap();

        parameterMap.put("tipoFolio", "INMOBILIARIA");
        parameterMap.put("oficina", oficinaId.toString());
        parameterMap.put("folioReal", prelacionFolio.toString());
        parameterMap.put("datasource", ds);
        parameterMap.put("muebleDS", predioDS);
        parameterMap.put("antecedenteDS", antecedenteDS);
        parameterMap.put("ubicacionDS", ubicacionDS);
        parameterMap.put("vialidadDS", vialidadDS);
        parameterMap.put("asentamientoDS", asentamientoDS);
        parameterMap.put("zonaDS", zonaDS);
        parameterMap.put("entidadFederativaDS", entidadFederativaDS);
        parameterMap.put("colindanciasDS", colindanciasDS);
        parameterMap.put("pasesDS", pasesDS);
        parameterMap.put("superficieDS", superficieSegregadaDS);
        parameterMap.put("titularDS", titularDS);
        parameterMap.put("statusDS", statusDS);
        parameterMap.put("devolucionesDS", devolucionesDS);
        parameterMap.put("actosDS", actosDS);
        parameterMap.put("tramiteDS", tramiteDS);


        JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/caratula/pdfCaratulaFolio.jasper");	   
        view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
        
        return new ModelAndView(view, parameterMap);
        
    }

    @RequestMapping(value = "printCaratulaJuridico/{folio}/{oficinaId}", method = RequestMethod.GET, produces = "application/pdf")
    public ModelAndView printCaratulaJuridico(@PathVariable("folio") Integer folio, @PathVariable("oficinaId") Long oficinaId, Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {

        PersonaJuridicaVO vo = caratulaService.getCaratulaPersonaJuridica(folio, oficinaId);

        JRDataSource ds                    = new JRBeanCollectionDataSource(Collections.singletonList(vo));

        Map<String, Object> parameterMap = this.getBasicParameterMap();

        parameterMap.put("tipoFolio", "JURIDICO");
        parameterMap.put("oficina", oficinaId.toString());
        parameterMap.put("folioReal", folio.toString());
        parameterMap.put("datasource", ds);
        parameterMap.put("antecedenteDS", new JRBeanCollectionDataSource(Collections.singletonList(vo.getAntecedente())));
        parameterMap.put("ubicacionDS", new JRBeanCollectionDataSource(Collections.singletonList(vo)));
        parameterMap.put("denominacionDS", new JRBeanCollectionDataSource(Collections.singletonList(vo)));
        parameterMap.put("duracionDS", new JRBeanCollectionDataSource(Collections.singletonList(vo)));
        parameterMap.put("objetoSocialDS", new JRBeanCollectionDataSource(Collections.singletonList(vo)));
        parameterMap.put("ubicacionDS", new JRBeanCollectionDataSource(Collections.singletonList(vo)));
        parameterMap.put("apoderadosDS", new JRBeanCollectionDataSource(Collections.singletonList(vo)));

        parameterMap.put("statusDS", new JRBeanCollectionDataSource(Collections.singletonList(vo)));
        parameterMap.put("devolucionesDS", new JRBeanCollectionDataSource(vo.getDevoluciones()));
        parameterMap.put("actosDS",  new JRBeanCollectionDataSource(vo.getSumatoriaActosPorClasifActo()));
        parameterMap.put("tramiteDS", new JRBeanCollectionDataSource(vo.getTramites()));


        JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/caratula/pdfCaratulaJuridico.jasper");	   
        view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
        
	    return new ModelAndView(view, parameterMap);
    }


    @RequestMapping(value = "printCaratulaAuxiliar/{folio}/{oficinaId}", method = RequestMethod.GET, produces = "application/pdf")
    public ModelAndView printCaratulaAuxiliar(@PathVariable("folio") Integer folio, @PathVariable("oficinaId") Long oficinaId, Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {

        FolioSeccionAuxiliarVO vo = caratulaService.getCaratulaFolioSeccionAuxiliar(folio, oficinaId);

        JRDataSource ds                    = new JRBeanCollectionDataSource(Collections.singletonList(vo));

        Map<String, Object> parameterMap = this.getBasicParameterMap();

        parameterMap.put("tipoFolio", "AUXILIAR");
        parameterMap.put("oficina", oficinaId.toString());
        parameterMap.put("folioReal", folio.toString());
        parameterMap.put("datasource", ds);
        parameterMap.put("antecedenteDS", new JRBeanCollectionDataSource(Collections.singletonList(vo.getAntecedente())));

        parameterMap.put("statusDS", new JRBeanCollectionDataSource(Collections.singletonList(vo)));
        parameterMap.put("devolucionesDS", new JRBeanCollectionDataSource(vo.getDevoluciones()));
        parameterMap.put("actosDS",  new JRBeanCollectionDataSource(vo.getSumatoriaActosPorClasifActo()));
        parameterMap.put("tramiteDS", new JRBeanCollectionDataSource(vo.getTramites()));


        JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/caratula/pdfCaratulaAuxiliar.jasper");	   
        view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
        
	    return new ModelAndView(view, parameterMap);
    }

    /**
     * Funciones comunes para definir los parametros del reporte
     *
     * @return Popiedades de path, imgPath, rutaReportes  tipo Map<String, Object>
     */
    public Map<String, Object> getBasicParameterMap() {

        Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put("path", Constantes.IMG_PATH);
        parameterMap.put("imgPath", Constantes.IMG_PATH);
        parameterMap.put("rutaReportes", Constantes.REPORTES_PATH);

        return parameterMap;
    }

	/*
    @RequestMapping(value = "printtest", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<byte[]> createPdf() throws IOException {
        final String FOX = Constantes.IMG_PATH + "escudo.png";

        try {
            Document document = new Document();
            PdfWriter.getInstance(document,
                    new FileOutputStream("C://data//erpp/documentos//sample.pdf"));
            document.open();

            document.setPageSize(PageSize.A4.rotate());
            // step 2
            // adding content as Paragraph objects

            Image image = Image.getInstance(new File(FOX).getAbsolutePath());
            image.setAbsolutePosition(0, 0);
            image.setBorderWidth(0);
            //image.scaleAbsolute(PageSize.A4);

            document.add(image);
            document.close();

            InputStream           inputStream = null;
            ByteArrayOutputStream baos        = new ByteArrayOutputStream();

            inputStream = new FileInputStream("C://data//erpp/documentos//sample.pdf");

            while ((bytesRead = inputStream.read(buffer)) != -1) {
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            String filename = "output.pdf";
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(baos.toByteArray(), headers, HttpStatus.OK);
            return response;

        } catch (Exception except) {
            Log.error(except);
        }

        return null;


	}
	*/

    @RequestMapping(value = "printCaratulaActo/{acto}", method = RequestMethod.GET, produces = "application/pdf")
    public ModelAndView printCaratulaActo(@PathVariable("acto") Long actoId, Model uiModel, HttpServletRequest httpServletRequest, ModelAndView modelAndView) {

       
        Map<String, Object> parameterMap = this.getBasicParameterMap();

        parameterMap.put("tipoFolio", "AUXILIAR");
       
        JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:/reports/caratula/pdfCaratulaAuxiliar.jasper");	   
        view.setApplicationContext((org.springframework.context.ApplicationContext) applicationContext);
        
        return new ModelAndView(view, parameterMap);
        
    }    




    @RequestMapping(value = "printtest/joinPdf", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<?> joinPdf() throws IOException {
        final String FOX = Constantes.IMG_PATH + "escudo.png";

        try {

            File imageStream = new File(FOX);

            byte [] tmpPdf = boletaRegistralService.generPdfBoletaIngreso (319501L);

            byte [] img = Files.readAllBytes(imageStream.toPath());

            byte [] byterino = pdfService.appendImage(tmpPdf, img);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            String filename = "output.pdf";
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(byterino, headers, HttpStatus.OK);
            return response;

        } catch (Exception except) {
            Log.error(except);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "printtest/appendImage", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<?> appendPdf() throws IOException {

        try {

            byte [] tmpPdf1 = boletaRegistralService.generPdfBoletaIngreso (319501L);

            byte [] tmpPdf2 = boletaRegistralService.generPdfBoletaIngreso (319506L);

            byte [] byterino = pdfService.appendPDF(tmpPdf1, tmpPdf2);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            String filename = "output.pdf";
            headers.setContentDispositionFormData(filename, filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(byterino, headers, HttpStatus.OK);
            return response;

        } catch (Exception except) {
            Log.error(except);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



}