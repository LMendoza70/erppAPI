package com.gisnet.erpp.service;

import java.util.List;
import java.util.ArrayList;

import org.springframework.transaction.annotation.Transactional;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Seccion;
import com.gisnet.erpp.domain.Documento;
import com.gisnet.erpp.domain.DocumentoArchivo;
import com.gisnet.erpp.domain.Archivo;
import com.gisnet.erpp.domain.ActoDocumento;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.Requisito;
import com.gisnet.erpp.domain.ActoRequisito;
import com.gisnet.erpp.domain.ReqTipoActo;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.vo.RequisitoVO;
import com.gisnet.erpp.repository.ParametroRepository;
import com.gisnet.erpp.repository.RequisitoRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.ActoRequisitoRepository;
import com.gisnet.erpp.repository.ArchivoRepository;
import com.gisnet.erpp.repository.DocumentoArchivoRepository;
import com.gisnet.erpp.repository.ReqTipoActoRepository;
import com.gisnet.erpp.repository.ActoDocumentoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ActoRequisitoService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private String UPLOADED_FOLDER = "c://tmp//";
	@Autowired
	private ActoRequisitoRepository actoRequisitoRepository;

	@Autowired
	ActoRepository actoRepository;

	@Autowired
	RequisitoRepository requisitoRepository;

	@Autowired
	ArchivoRepository archivoRepository;

	@Autowired
	ActoDocumentoRepository actoDocumentoRepository;

	@Autowired
	ReqTipoActoRepository reqTipoActoRepository;

	@Autowired
	ParametroRepository parametroRepository;

	@Autowired
	DocumentoArchivoRepository documentoArchivoRepository;

	public List<Requisito> findAll() {
		return requisitoRepository.findAll();
	}

	@Transactional
	public ActoRequisito saveActoRequisito(ActoRequisito actoRequisito) throws Exception {
		ActoRequisito actoreq = new ActoRequisito();
		actoreq.setActo(actoRequisito.getActo());
		actoreq.setArchivo(actoRequisito.getArchivo());
		actoreq.setRequisito(actoRequisito.getRequisito());
		actoRequisitoRepository.saveAndFlush(actoreq);
		return actoreq;
	}

	public List<RequisitoVO> saveActoRequisitoWithFile(Requisito reqIngreso, MultipartFile file, Acto acto,
		Boolean esPresencial, Boolean isReingreso) throws Exception {
		List<RequisitoVO> requisitos = null;
		Archivo archivo = new Archivo();
		archivo = prepareArchivo(file, archivo);
		archivo.setReingreso(isReingreso);
		archivoRepository.saveAndFlush(archivo);
		System.out.println("here on save with file the archivo " + archivo);

		if (acto == null) {
			throw new NoSuchElementException();
		}
		ActoRequisito actReq = actoRequisitoRepository.findByActoAndRequisitoAndArchivoIsNull(acto, reqIngreso);
		if (actReq == null) {
			actReq = new ActoRequisito();
		}

		actReq.setActo(acto);
		actReq.setRequisito(reqIngreso);
		actReq.setArchivo(archivo);
		actoRequisitoRepository.saveAndFlush(actReq);

		saveUploadedFiles(Arrays.asList(file), archivo.getNombre());
		requisitos = getRequisitosByActo(acto, esPresencial);

		return requisitos;
	}

	private void saveUploadedFiles(List<MultipartFile> files, String nombre) throws IOException {
		for (MultipartFile file : files) {

			if (file.isEmpty()) {
				continue; // next pls
			}
			UPLOADED_FOLDER = parametroRepository.findByCve("RUTA_REQUISITO").getValor();
			byte[] bytes = file.getBytes();
			System.out.println("saveUploadedFiles" + UPLOADED_FOLDER);
			Path path = Paths.get(UPLOADED_FOLDER + nombre);
			Files.write(path, bytes);
		}
	}

	private Archivo prepareArchivo(MultipartFile file, Archivo archivo) {
		if (archivo == null) {
			archivo = new Archivo();
		}
		UPLOADED_FOLDER = parametroRepository.findByCve("RUTA_REQUISITO").getValor();
		System.out.println("prepareArchivo" + UPLOADED_FOLDER);
		String _sufijo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String extencion = file.getOriginalFilename().split("\\.")[1];
		String nombre = file.getOriginalFilename().split("\\.")[0];
		String fileName = nombre + "_" + _sufijo + "." + extencion;

		archivo.setRuta(UPLOADED_FOLDER);
		archivo.setTipo(file.getContentType());
		archivo.setNombre(fileName);

		return archivo;
	}

	@Transactional
	public Boolean eliminarRequisito(Long idActo, Long idArchivo, Long idRequisito) {
		actoRequisitoRepository
				.delete(actoRequisitoRepository.findByActoIdAndRequisitoIdAndArchivoId(idActo, idRequisito, idArchivo));
		return true;
	}

	public List<RequisitoVO> getRequisitosActoByPrelacion(Prelacion prelacion) {

		List<Acto> actos = actoRepository.findAllByPrelacion(prelacion);

		List<RequisitoVO> reqs = new ArrayList<RequisitoVO>();
		List<ReqTipoActo> requi = null;
		for (Acto a : actos) {

			requi = reqTipoActoRepository.findAllByTipoActo(a.getTipoActo());

			for (ReqTipoActo r : requi) {

				RequisitoVO temReqVO = new RequisitoVO();
				temReqVO.setRequisito(r.getRequisito());
				temReqVO.setActo(a);

				List<ActoRequisito> arList = actoRequisitoRepository.findByActoAndRequisito(a, r.getRequisito());
				for (ActoRequisito ar : arList) {
					if (ar != null) {
						temReqVO.getArchivos().add(ar.getArchivo());

					}
				}
				reqs.add(temReqVO);
			}

		}

		return reqs;
	}

	public List<RequisitoVO> getRequisitosByActo(Acto acto, boolean esPresencial) {

		List<RequisitoVO> reqs = new ArrayList<RequisitoVO>();
		List<ReqTipoActo> requi = null;

		try {

			List<ActoRequisito> reqpPrelacion = actoRequisitoRepository.findAllByPrelacion(acto.getPrelacion().getId());
			if (esPresencial) {
				requi = reqTipoActoRepository.findAllByTipoActo(acto.getTipoActo());
			} else {
				requi = reqTipoActoRepository.findAllByTipoActoVE(acto.getTipoActo().getId());
			}

			for (ReqTipoActo r : requi) {

				List<ActoRequisito> arList = actoRequisitoRepository.findByActoAndRequisito(acto, r.getRequisito());


				RequisitoVO temReqVO = new RequisitoVO();
				temReqVO.setRequisito(r.getRequisito());
				temReqVO.setActo(acto);
				temReqVO.setFundatorio(false);
				temReqVO.setCantidad(r.getCantidad());
				for (ActoRequisito ar : arList) {
					if (ar.getArchivo() != null) {
						temReqVO.getArchivos().add(ar.getArchivo());
					} else {

						for (ActoRequisito actoReq : reqpPrelacion) {
							if (actoReq.getRequisito().equals(r.getRequisito())) {
								if (actoReq.getArchivo() != null) {
									temReqVO.getArchivos().add(actoReq.getArchivo());
								}
							}
						}

					}
				}
				reqs.add(temReqVO);
			}

			List<ActoDocumento> documentos = actoDocumentoRepository.getAllActoDocumentoByActoId(acto.getId());

			if (documentos != null) {
				for (ActoDocumento ad : documentos) {

					RequisitoVO temReqVO = new RequisitoVO();
					List<DocumentoArchivo> documentosArchivo = new ArrayList<DocumentoArchivo>();
					documentosArchivo = documentoArchivoRepository.findByDocumentoId(ad.getDocumento().getId());
					temReqVO.setTipoDocumento(ad.getDocumento().getTipoDocumento());
					temReqVO.setIdDocumento(ad.getDocumento().getId());
					temReqVO.setDocumentoArchivo(documentosArchivo);
					if (ad.getDocumento().getArchivo() != null) {
						temReqVO.getArchivos().add(ad.getDocumento().getArchivo());
					}

					temReqVO.setActo(ad.getActo());
					temReqVO.setFundatorio(true);
					temReqVO.setObservaciones(ad.getDocumento().getObservaciones());
					reqs.add(temReqVO);
				}
			}

		} catch (Exception err) {
			/// err.printStackTrace();
			System.out.println(err.getMessage());
		}

		return reqs;
	}
}
