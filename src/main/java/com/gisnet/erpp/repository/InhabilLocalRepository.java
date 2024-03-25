package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.DiaInhabil;
import com.gisnet.erpp.domain.InhabilLocal;
import com.gisnet.erpp.domain.Oficina;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@SuppressWarnings("unused")
public interface InhabilLocalRepository extends JpaRepository<InhabilLocal,Long> {
  
	public List<InhabilLocal> findByOficinaId(Long oficinaId);
	
	public InhabilLocal findOneByDiaInhabilAndOficina(DiaInhabil diaInhabil, Oficina oficina);
}
