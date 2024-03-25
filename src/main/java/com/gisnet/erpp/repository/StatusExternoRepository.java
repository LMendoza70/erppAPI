package com.gisnet.erpp.repository;


import com.gisnet.erpp.domain.StatusExterno;


import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface StatusExternoRepository extends JpaRepository<StatusExterno,Long> {

}
