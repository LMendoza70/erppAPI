package com.gisnet.erpp.service;

import java.util.ArrayList;
import java.util.List;

//import com.gisnet.erpp.domain.*;
import com.gisnet.erpp.domain.Servicio;
import com.gisnet.erpp.domain.SubServicio;
import com.gisnet.erpp.domain.SubSubServicio;
import com.gisnet.erpp.domain.Certificado;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.PrelacionServicio;
import com.gisnet.erpp.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.repository.CertificadoRepository;
import com.gisnet.erpp.repository.PrelacionServicioRepository;
import com.gisnet.erpp.repository.ServicioRepository;
import com.gisnet.erpp.repository.SubServicioRepository;
import com.gisnet.erpp.repository.SubSubServicioRepository;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.vo.ServicioAndSubVO;


@Service
public class ServicioService {

	@Autowired
	private ServicioRepository servicioRepository;
	@Autowired
	private SubServicioRepository subServicioRepository;
	@Autowired
	private SubSubServicioRepository subSubServicioRepository;
	@Autowired
	private PrelacionServicioRepository prelacionServicioRepository;
	@Autowired
	UsuarioService usuarioService;
	@Autowired
	CertificadoRepository certificadoRepository;
	@Autowired
	CertificadoService certificadoService;
	
	public List<Servicio> findByAreaId(Long areaId){
		Usuario user= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		System.out.println(user);
		return servicioRepository.findByAreaId(areaId);
	}

	public List<Servicio> findByClasifActoId(Long areaId){
		Usuario user= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		System.out.println(user);
		return servicioRepository.findByClasifActoId(areaId);
	}
	
	public List<Servicio> findAll(){
		Usuario user= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		System.out.println(user);
		return servicioRepository.findAll();
	}
	
	public List<Servicio> findByAreaIdAndClasifActoId(Servicio servicio){
		return servicioRepository.findByAreaIdAndClasifActoId(servicio.getArea().getId(),servicio.getClasifActo().getId());
	}
	
	public List<Servicio> findByAreaIdAndClasifActoIdAndTipoActoId(Servicio servicio){
		return servicioRepository.findByAreaIdAndClasifActoIdAndTipoActoId(servicio.getArea().getId(),servicio.getClasifActo().getId(),servicio.getTipoActo().getId());
	}
	
	public List<Servicio> findAllByTipoUsuario(String acceso){
		Usuario user= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());	
		return servicioRepository.findAllByTipoUsuario(user.getTipoUsuario().getId(),acceso);
	}
	
	public List<Servicio> findAllByTipoUsuarioAndArea(String acceso, Long areaId){
		Usuario user= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());	 
		return servicioRepository.findAllByTipoUsuario(user.getTipoUsuario().getId(),acceso ,areaId);
	}

	public List<ServicioAndSubVO> findByPrelacionId(Long id){
		List<ServicioAndSubVO> servicios= null;
		List<PrelacionServicio> serviciosPrela= prelacionServicioRepository.findByPrelacionId(id);
		if(serviciosPrela!=null){
			servicios= new ArrayList<ServicioAndSubVO>();
			for(PrelacionServicio ps : serviciosPrela){
				servicios.add(prelacionServicioTransform(ps));		
			}
		}
		return servicios;
	}

	public ServicioAndSubVO  saveServicioAndSubPrelacion( ServicioAndSubVO  servicioVO, Prelacion prelacion ){
		PrelacionServicio prelaServi=new PrelacionServicio();
		if(servicioVO.getIdSubServicio()!=null){					
			SubServicio subServicio= subServicioRepository.findOneById(servicioVO.getIdSubServicio());
			prelaServi.setSubServicio(subServicio);
			prelaServi.setServicio(subServicio.getServicio());
		}else{
			Servicio servicio= servicioRepository.findOne(servicioVO.getIdServicio());
			prelaServi.setServicio(servicio);
		}
		prelaServi.setPrelacion(prelacion);

		prelacionServicioRepository.saveAndFlush(prelaServi);
		return prelacionServicioTransform(prelaServi);
	}

	public  ServicioAndSubVO prelacionServicioTransform(PrelacionServicio prelaServi){
		ServicioAndSubVO servicioVO=null;
		if(prelaServi!=null){
			servicioVO= new ServicioAndSubVO();
			servicioVO.setId(prelaServi.getId());
			if(prelaServi.getSubServicio()!=null){
				servicioVO.setIdServicio(prelaServi.getServicio().getId());
				servicioVO.setServicio(prelaServi.getServicio().getNombre());
				servicioVO.setIdSubServicio(prelaServi.getSubServicio().getId());
				servicioVO.setSubServicio(prelaServi.getSubServicio().getNombre());
			}else{

				servicioVO.setIdServicio(prelaServi.getServicio().getId());
				servicioVO.setServicio(prelaServi.getServicio().getNombre());
				servicioVO.setArea(prelaServi.getServicio().getArea());
				
			}
		}
		
		return servicioVO;
	}

	public boolean deleteServicioPrelacion(long prelacionId) {
		List<PrelacionServicio> preServ = null;
		preServ= prelacionServicioRepository.findAllByPrelacionId(prelacionId);
		if (preServ!= null) {
			preServ.forEach(element -> {
				deleteCertificado(element);
				prelacionServicioRepository.delete(element);
			});
		}
		else
			return false;

		return true;
	}
	
	public boolean deleteCertificado(PrelacionServicio prelacionServicio) {
		List<Certificado> cert = certificadoRepository.findByPrelacionServicioId(prelacionServicio.getId());
		if(cert != null && cert.size()>0) {
			for(Certificado c:cert) {
				certificadoService.delete(c);
			}
			return true;
		}
		return false;
	}




	public Servicio findOne(Long id) {
		return servicioRepository.findOne(id);
	}
	public SubServicio findOneSub(Long id) {
		return subServicioRepository.findOne(id);
	}
	public SubSubServicio findOneSubSub(Long id) {
		return subSubServicioRepository.findOne(id);
	}
	
	@Transactional(readOnly = true)
	public Page<Servicio> getAllServicios(Pageable pageable, String nombre) {
		return servicioRepository.getAllServicios(pageable, nombre);
	}
	@Transactional(readOnly = true)
	public List<SubServicio> findSubServicios(long servicioId){
		return subServicioRepository.findByServicioId(servicioId);
	}
	@Transactional(readOnly = true)
	public List<SubSubServicio> findSubSubServicios(long subServicioId){
		return subSubServicioRepository.findBySubServicioId(subServicioId);
	}
	
	@Transactional
	public Long saveServicio(Servicio servicio) {
		servicioRepository.save(servicio);
		return servicio.getId();
	}
	@Transactional
	public Long saveSubServicio(SubServicio subServicio) {
		subServicioRepository.save(subServicio);
		return subServicio.getId();
	}
	@Transactional
	public Long saveSubSubServicio(SubSubServicio subSubServicio) {
		subSubServicioRepository.save(subSubServicio);
		return subSubServicio.getId();
	}
	
	@Transactional
	public Long deleteServicio(Servicio servicio) {
		servicioRepository.delete(servicio);
		return servicio.getId();
	}
	@Transactional
	public Long deleteSubServicio(SubServicio subServicio) {
		subServicioRepository.delete(subServicio);
		return subServicio.getId();
	}
	@Transactional
	public Long deleteSubSubServicio(SubSubServicio subSubServicio) {
		subSubServicioRepository.delete(subSubServicio);
		return subSubServicio.getId();
	}
} 