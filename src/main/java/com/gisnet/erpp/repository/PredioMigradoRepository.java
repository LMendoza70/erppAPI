package com.gisnet.erpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.FolioSeccionAuxiliar;
import com.gisnet.erpp.domain.PersonaJuridica;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.PredioMigrado;

/**
 * Spring Data JPA repository for the PredioMigrado entity.
 */
@SuppressWarnings("unused")
public interface PredioMigradoRepository extends JpaRepository<PredioMigrado,Long> {

	public PredioMigrado findAllByPredio(Predio predio);

	public PredioMigrado findAllByPersonaJuridica(PersonaJuridica personaJuridica);

	public PredioMigrado findAllByFolioSeccionAuxiliar(FolioSeccionAuxiliar folioSeccionAuxiliar);
}
