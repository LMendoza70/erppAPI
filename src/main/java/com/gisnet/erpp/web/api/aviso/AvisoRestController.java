package com.gisnet.erpp.web.api.aviso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.domain.Aviso;
import com.gisnet.erpp.domain.AvisoActoValorTipoActo;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.repository.ActoPredioRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.AvisoActoValorTipoActoRepository;
import com.gisnet.erpp.service.ActoPredioCampoService;
import com.gisnet.erpp.service.ActoPredioService;
import com.gisnet.erpp.service.ActoService;
import com.gisnet.erpp.service.AvisoService;
import com.gisnet.erpp.service.PredioService;
import com.gisnet.erpp.service.PrelacionPredioService;
import com.gisnet.erpp.service.PrelacionService;

@RestController
@RequestMapping(value = "/api/avisos", produces = MediaType.APPLICATION_JSON_VALUE)
public class AvisoRestController {

	@Autowired
	private ActoService aService;
	
	@Autowired
	private AvisoService service;

	@Autowired
	private ActoPredioService apService;

	@Autowired
	private PredioService predService;

	@Autowired
	private PrelacionService prelacionService;

	@Autowired
	private PrelacionPredioService ppService;

	@Autowired
	private ActoPredioCampoService apcService;

	@Autowired
	private AvisoActoValorTipoActoRepository avisoActoValorRepo;

	@Autowired
	private ActoPredioRepository actoPredioRepo;

	@Autowired
	private ActoService actoService;

	@GetMapping("/acto/{actoId}")
	public ResponseEntity<AvisoDTO> obtenerPorActo(@PathVariable Long actoId) {
		return ResponseEntity.ok(new AvisoDTO(service.buscarPorActoId(actoId)));
	}

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@GetMapping("/predio/folio/{noFolio}")
	public ResponseEntity<List<AvisoDTO>> obtenerPorFolio(@PathVariable Integer noFolio) {
		Predio predio = predService.findPredioByNoFolio(noFolio);
		if (predio == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		System.out.println("/predio/folio/{noFolio}");
		return ResponseEntity.ok(obtenerDePredio(predio));
	}
	
	private List<AvisoDTO> obtenerDePrelacion(Long prelacionId) {
		ArrayList<AvisoDTO>aDTOS=null;
		System.out.println("buscando actos con vf false y prelacion id:  "+prelacionId);
		List<Acto> actos =aService.findByPrelacionId(prelacionId);
		System.out.println("actos encontrados: "+actos.size());
		if(actos!=null && actos.size()!=0) {
			aDTOS = new ArrayList<AvisoDTO>();
			for(Acto a:actos) {
				if(a.getTipoActo()!=null) {
					boolean esAviso=actoEsAviso(a);
					if(esAviso) {
						Aviso aviso=service.buscarPorActoId(a.getId());//aqui graba
						if(aviso!=null) {
							AvisoDTO aDTO= new AvisoDTO(aviso);
							aDTOS.add(aDTO);
						}
					}
				}
			}
			return aDTOS;
		}else {
			return aDTOS;
		}
	}

	private List<AvisoDTO> obtenerDePredio(Predio predio) {
		Long[] status = {1L};
		List<AvisoDTO> avisos =  new ArrayList<AvisoDTO>();
		if(predio!=null) {
			System.out.println("Buscando o Grabando avisos por predio...");
			Set<Acto> actosPredio = apService.findAllActosbyPredioAndStatusId(predio.getId(), status);
			
			 avisos = actosPredio.stream()
					.filter(this::actoEsAviso)
					.map(Acto::getId)
					.map(service::buscarPorActoId)
					.map(AvisoDTO::new)
					.collect(Collectors.toList());
		}
		return avisos;
	}

	@GetMapping("/prelacion/{prelacionId}")
	public ResponseEntity<List<AvisoDTO>> obtenerPorPrelacion(@PathVariable Long prelacionId) {
	System.out.println("obtenerPorPrelacion");
		Prelacion prelacion = prelacionService.findOne(prelacionId);
		if (prelacion == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		List<Predio> predios = ppService.findAllPrelacionPredioFromPrelacion(prelacion).stream()
				.map(PrelacionPredio::getPredio)
				.collect(Collectors.toList());

		List<AvisoDTO> avisos = new ArrayList<>();

		for(Predio p : predios) {
			System.out.println("/prelacion/{prelacionId}");
			List<AvisoDTO> avisosPredio = obtenerDePredio(p);
			avisos.addAll(avisosPredio);
		}

		return ResponseEntity.ok(avisos);
	}

	@GetMapping("/cumple-aviso/actopredio/{idActoPredio}")
	public ResponseEntity<CumpleAvisoDTO> predioCumpleAvisoPorActoPredio(@PathVariable Long idActoPredio) {
		CumpleAvisoDTO res = new CumpleAvisoDTO();
		res.setCumple(true);
		ActoPredio ap = apService.getAP(idActoPredio);
		Predio predio = ap.getPredio();//validar si predio viene nulo
		List<AvisoDTO> avisos=null;
		System.out.println("/cumple-aviso/actopredio/{idActoPredio} "+idActoPredio);
		if(predio!=null){

		 avisos = obtenerDePredio(predio);
		}else{
			System.out.println("Predio Null avisos null");
		}

		if (avisos == null || avisos.isEmpty()) {
			System.out.println("res.getCumple()" +res.getCumple());
			System.out.println("res.getCautelar()" +res.getCautelar());
			System.out.println("res.getPreventivo()" +res.getPreventivo());
			return ResponseEntity.ok(res);
		}

		boolean cumple = true;
		for(AvisoDTO aviso : avisos) {
			if (aviso != null && aviso.getVigente() != null && aviso.getVigente()) {
				ActoPredio actoPredio = aviso.getActo().getActoPrediosParaActos().stream()
						.filter(apa -> apa.getPredio() != null && apa.getPredio().getId().equals(predio.getId()))
						.max((apa1, apa2) -> apa1.getVersion().compareTo(apa2.getVersion()))
						.orElse(null);

				if (actoPredio == null) continue;

				List<ActoPredioCampo> campos = apcService.getActoPredioCampoByActoPredio(actoPredio);

				for (ActoPredioCampo campo : campos) {
					int moduloCampo = campo.getModuloCampo().getId().intValue();

					if(moduloCampo==3416 || moduloCampo==39){cumple = cumple && revisarPredioCumplaActo(predio, campo.getValor());}
					if(moduloCampo==3417 || moduloCampo==40){cumple = cumple && revisarPredioCumplaActo(predio, campo.getValor());}
					if(moduloCampo==3418 || moduloCampo==38){cumple = cumple && revisarPredioCumplaActo(predio, campo.getValor());}
					if(moduloCampo==3419 || moduloCampo==37){cumple = cumple && revisarPredioCumplaActo(predio, campo.getValor());}
					if(moduloCampo==3420 || moduloCampo==41){cumple = cumple && revisarPredioCumplaActo(predio, campo.getValor());}
					if(moduloCampo==3421 || moduloCampo==42){cumple = cumple && revisarPredioCumplaActo(predio, campo.getValor());}
				/* 	switch (moduloCampo) {
					case 3416:
					case 3417:
					case 3418:
					case 3419:
					case 3420:
					case 3421:
						cumple = cumple && revisarPredioCumplaActo(predio, campo.getValor());
					} */

					if (!cumple) break;
				}

				if (!cumple) {
					if (aviso.getActo().getTipoActo().getId().equals(49L)) res.setPreventivo(true);
					else res.setCautelar(true);
					break;
				}
			}
		}
		System.out.println("predioCumpleAvisoPorActoPredio ?: " +cumple);
		
		res.setCumple(cumple);
		return ResponseEntity.ok(res);
	}

	private boolean revisarPredioCumplaActo(Predio predio, String valor) {
		System.out.println("valor "+valor);
		System.out.println("predio "+predio);
		List<AvisoActoValorTipoActo> actoValores = avisoActoValorRepo.findByCampoValoresId(Long.parseLong(valor));

		List<Long> tiposActosRequeridos = actoValores.stream()
				.map(av -> av.getTipoActo().getId())
				.collect(Collectors.toList());
	 	System.out.println("tiposActosRequeridos{}"+tiposActosRequeridos);
		Long[] status = { 1L, 3L };
		Set<Acto> actosPredio = apService.findAllActosbyPredioAndStatusId(predio.getId(), status);
		boolean esAviso=false;
		for(Acto a : actosPredio){
			if(a.getTipoActo().getId()==50L || a.getTipoActo().getId()==49L|| a.getTipoActo().getId()==210L){
				esAviso=true;
			}
		}
		System.out.println("actospredio{}"+actosPredio);
		if(esAviso){return true;}
		else{
			return actosPredio.stream()
			.anyMatch(a ->
				tiposActosRequeridos.stream().anyMatch(tipo -> tipo.equals(a.getTipoActo().getId()))
				);
		}

		
	}

	private boolean actoEsAviso(Acto acto) {
		int tipoActo = acto.getTipoActo().getId().intValue();
		switch (tipoActo) {
		case 49:
		case 50:
		case 210:
		case 124:
			return true;
		default:
			return false;
		}
	}

	@GetMapping("/cumple-aviso/prelacion/{prelacionId}")
	public ResponseEntity<List<RequisitosAviso>> cumpleAvisoPorPrelacion(@PathVariable Long prelacionId) {
		Prelacion prelacion = prelacionService.findOne(prelacionId);
		if (prelacion == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		List<Predio> predios = ppService.findAllPrelacionPredioFromPrelacion(prelacion).stream()
				.map(PrelacionPredio::getPredio)
				.collect(Collectors.toList());

		List<RequisitosAviso> requisitos = new ArrayList<>();

		for(Predio p : predios) {
			System.out.println("/cumple-aviso/prelacion/{prelacionId}");
			List<AvisoDTO> avisosPredio = obtenerDePredio(p);
			if (avisosPredio == null || avisosPredio.isEmpty()) continue;

			List<ActoPredio> actosPredio = actoPredioRepo.findAllMaxVersionByActoPrelacionIdAndPredioId(prelacionId, p.getId());

			for (AvisoDTO aviso : avisosPredio) {

				if (aviso != null && aviso.getVigente() != null && aviso.getVigente()) {
					ActoPredio actoPredioAviso = aviso.getActo().getActoPrediosParaActos().stream()
							.filter(apa -> apa.getPredio() != null && apa.getPredio().getId().equals(p.getId()))
							.max((apa1, apa2) -> apa1.getVersion().compareTo(apa2.getVersion()))
							.orElse(null);

					if (actoPredioAviso == null) continue;

					requisitos.add(service.prelacionCumpleRequisitoAviso(actosPredio, aviso, actoPredioAviso));
				}
			}

		}

		return ResponseEntity.ok(requisitos);
	}


	@PostMapping("/extinguir-avisos/prelacion/{prelacionId}")
	public ResponseEntity<List<RequisitosAviso>> extinguirAvisosPorPrelacion(@PathVariable Long prelacionId) {
		Prelacion prelacion = prelacionService.findOne(prelacionId);
		if (prelacion == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		ArrayList<Long> tipoDeAvisos =new ArrayList<Long>();
		tipoDeAvisos.add(49L);
		tipoDeAvisos.add(210L);
		Optional<Acto> primerAvisoDePrelacion=actoService.findPrimerAvisolByPrelacion(prelacionId,tipoDeAvisos);
		
		List<Predio> predios = ppService.findAllPrelacionPredioFromPrelacion(prelacion).stream()
				.map(PrelacionPredio::getPredio)
				.collect(Collectors.toList());

		List<RequisitosAviso> requisitos = new ArrayList<>();
		
		for(Predio p : predios) {
			List<AvisoDTO> avisosPredio = obtenerDePredio(p);
			if (avisosPredio == null || avisosPredio.isEmpty()) continue;

			List<ActoPredio> actosPredio = actoPredioRepo.findAllMaxVersionByActoPrelacionIdAndPredioId(prelacionId, p.getId());

			for (AvisoDTO aviso : avisosPredio) {
				System.out.println("AVISO: "+aviso.getId());
				if (aviso != null && aviso.getVigente() != null && aviso.getVigente()) {
									
					ActoPredio actoPredioAviso = aviso.getActo().getActoPrediosParaActos().stream()
							.filter(apa -> apa.getPredio() != null && apa.getPredio().getId().equals(p.getId()))
							.max((apa1, apa2) -> apa1.getVersion().compareTo(apa2.getVersion()))
							.orElse(null);

					if (actoPredioAviso == null) continue;
					System.out.println("primerAvisoDePrelacion.isPresent() "+primerAvisoDePrelacion.isPresent());
					
					if(!primerAvisoDePrelacion.isPresent()){
						if (prelacionId!=aviso.getActo().getPrelacion().getId() && service.prelacionCumpleRequisitoAviso(actosPredio, aviso, actoPredioAviso).cumpleAviso()) {
						actoService.cancelarActo(aviso.getActo());
						}
					}
					
				}
			}

		}

		return ResponseEntity.ok(requisitos);
	}


/* LOS CANCELA|GRABA DESDE VENTANILLA
	@PostMapping("/extinguir-avisos/prelacion/{prelacionId}")
	public ResponseEntity<List<RequisitosAviso>> extinguirAvisosPorPrelacion(@PathVariable Long prelacionId) {
		Prelacion prelacion = prelacionService.findOne(prelacionId);
		if (prelacion == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		List<Predio> predios = ppService.findAllPrelacionPredioFromPrelacion(prelacion).stream()
				.map(PrelacionPredio::getPredio)
				.collect(Collectors.toList());

		List<RequisitosAviso> requisitos = new ArrayList<>();
		System.out.println("/extinguir-avisos/prelacion/");

		System.out.println("predios size"+ predios.size());
		if(prelacion.getStatus().getId()==2L && predios.isEmpty()){
			System.out.println("sin predio y prelacion con status 2 ");
			obtenerDePrelacion(prelacionId);
		}else{
			for(Predio p : predios) {
				System.out.println("predioId: "+p.getId());
				
				Set<ActoPredio> actosPredioSet =new HashSet<ActoPredio>();;
				
				System.out.println("buscando actos vigententes en acto predio con predio id: "+p.getId());
				List<ActoPredio> actosPredioSinFiltrar = actoPredioRepo.findAllByPredio( p.getId(),1L);

				System.out.println("encontro actosPredio Sin Filtrar "+actosPredioSinFiltrar.size());
				for(ActoPredio ap:actosPredioSinFiltrar){
					if(ap.getActo()!=null && ap.getActo().getTipoActo()!=null) {
						Long tipo=ap.getActo().getTipoActo().getId();
						if(tipo==49L||tipo==50L||tipo==210L){
							ActoPredio apOk=actoPredioRepo.findActoPredioByLastVersion(ap.getActo().getId());
							System.out.println("Agregando acto predio con aviso "+apOk.getId());
							actosPredioSet.add(ap);
						}
					}
				}
				System.out.println("actos con Aviso encontrados en ActoPredio "+actosPredioSet.size());
				Set<AvisoDTO> avisosPredio=new HashSet<AvisoDTO>();//Set en lugar de List
				if(actosPredioSet.isEmpty()){
					obtenerDePrelacion(prelacionId);
				}else{
					for(ActoPredio ap:actosPredioSet){
						Aviso aviso= service.buscarAvisoPorActoId(ap.getActo().getId());
						if(aviso!=null){
							System.out.println("agregando aviso id "+aviso.getId());
							avisosPredio.add(new AvisoDTO(aviso));
						}else{
							System.out.println("No encontro aviso con acto id: "+ap.getActo().getId());
						}
						
					}
				
					System.out.println(avisosPredio==null || avisosPredio.isEmpty()?"no encontro avisos":"encontro avisos: "+avisosPredio.size());
				}
				//actosPredio= actoPredioRepo.findAllMaxVersionByActoPrelacionIdAndPredioId(prelacionId, p.getId());//AUN NO TIENE PREDIO ASOCIADO
				List<ActoPredio> actosPredio = new ArrayList();
				
				for(Acto a : aService.findByPrelacionId(prelacion.getId())){
					for(ActoPredio ap:actoPredioRepo.findLastVersionByActo(a.getId())){
						actosPredio.add(ap);
					}
				
				}

				for(ActoPredio ap:actosPredio){
					System.out.println("acto predio  de esta prelacion"+prelacion.getConsecutivo()+" es: "+ap.getId());
					if(ap.getActo()!=null && ap.getActo().getTipoActo()!=null) {
						Long tipo=ap.getActo().getTipoActo().getId();
						System.out.println("El acto de esta prelacion es un Aviso ?");
						if(tipo==49L||tipo==50L||tipo==210L){
							System.out.println("Si \n Verificando si ya esta grabado en la tabla avisos ...");
							service.buscarPorActoId(ap.getActo().getId());//aqui graba
						}else{System.out.println("No");}
					}else {
						System.out.println("tipo de acto null :"+ap);
					}					
				}
			
			
				if (avisosPredio == null || avisosPredio.isEmpty()) continue;
				for (AvisoDTO aviso : avisosPredio) {
					System.out.println("aviso id: "+aviso.getId());
					System.out.println("aviso vigente "+aviso.getVigente());
					if (aviso != null && aviso.getVigente() != null && aviso.getVigente()) {
					 	ActoPredio actoPredioAviso = aviso.getActo().getActoPrediosParaActos().stream()
								.filter(apa -> apa.getPredio() != null && apa.getPredio().getId().equals(p.getId()))
								.max((apa1, apa2) -> apa1.getVersion().compareTo(apa2.getVersion()))
								.orElse(null); 
						log.info("actoPredioAviso: {}",actoPredioAviso);
						if (actoPredioAviso == null) continue;
					
						if (service.prelacionCumpleRequisitoAviso(actosPredio, aviso, actoPredioAviso).cumpleAviso()) {
							actoService.cancelarActo(aviso.getActo());
						}
					}
				}
	
			}
		}
		return ResponseEntity.ok(requisitos);
	} */

}
