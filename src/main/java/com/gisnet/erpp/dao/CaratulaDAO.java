package com.gisnet.erpp.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.UsuarioRol;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.service.UsuarioService;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.caratula.ActoVO;
import com.gisnet.erpp.vo.caratula.PredioVO;
import com.gisnet.erpp.vo.caratula.TramiteVO;
import com.gisnet.erpp.vo.prelacion.ActoPublictarioHisModel;
import com.gisnet.erpp.vo.prelacion.ActoPublictarioModel;
import com.gisnet.erpp.vo.prelacion.PrelacionModuloVO;

import static com.gisnet.erpp.util.Constantes.ETipoFolio.*;

@Repository
public class CaratulaDAO {
	protected Log logger = LogFactory.getLog(this.getClass());

	private NamedParameterJdbcTemplate namedparameterJdbcTemplate;
	private JdbcTemplate jdbcTemplate;
	private DataSource dataSource;
	
	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.namedparameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		this.dataSource = dataSource;
	}

	public List<ActoVO> findTotalActivosEInexitentesPorClasifActo(Long id,Constantes.ETipoFolio tipoFolio) {
		List<ActoVO> result = new ArrayList<ActoVO>();
		ActoVO vo = null;
		StringBuilder sb = new StringBuilder();
		sb.append(" select ca.id, ca.nombre ,  ");
		sb.append("   SUM(case when a.status_acto_id=:statusActoIdVigente then 1 else 0 end)  as vigentes, ");
		sb.append("   SUM(case when a.status_acto_id=:statusActoNoVigente then 1 else 0 end)  as no_vigentes ");
		sb.append(" from ( ");
		sb.append("   select p.CONSECUTIVO, a.id as actoId, MAX(ap.VERSION) as version1, MAX(a.version) as version2 ");
		sb.append("   from acto a ");
		sb.append("   join tipo_acto ta on a.tipo_acto_id=ta.id ");
		sb.append("   join clasif_acto ca on ta.clasif_acto_id=ca.id ");
		sb.append("   join acto_predio ap on ap.acto_id=a.id ");
		sb.append("   join prelacion p on p.id=a.prelacion_id ");
		sb.append("   where a.status_acto_id in (:statusActoIdVigente, :statusActoNoVigente) ");
		sb.append("         and a.remplazado is null");
		if( tipoFolio == PERSONAS_JURIDICAS ) {
			sb.append("     and ap.persona_juridica_id= :id ");
		} else if( tipoFolio == PERSONAS_AUXILIARES ) {
			sb.append("     and ap.folio_seccion_auxiliar_id= :id ");
		} else if( tipoFolio == MUEBLE ) {
			sb.append("     and ap.mueble_id= :id ");
		} else if( tipoFolio == PREDIO ) {
			sb.append("     and ap.predio_id= :id ");
		}
		sb.append("   group by p.consecutivo, a.id ");
		sb.append("   order by p.consecutivo ");
		sb.append(" ) q1, acto a ");
		sb.append(" join tipo_acto ta on a.tipo_acto_id=ta.id ");
		sb.append(" join clasif_acto ca on ta.clasif_acto_id=ca.id ");
		sb.append(" join acto_predio ap on ap.acto_id=a.id ");
		sb.append(" join prelacion p on p.id=a.prelacion_id ");
		sb.append(" where q1.consecutivo = p.consecutivo ");
		sb.append("   and q1.version1 = ap.version ");
		sb.append("   and q1.version2 = a.version ");
		sb.append("   and q1.actoId = a.id ");
		sb.append("   and a.status_acto_id in (:statusActoIdVigente, :statusActoNoVigente) ");
		sb.append("         and a.remplazado is null");
		if( tipoFolio == PERSONAS_JURIDICAS ) {
			sb.append("     and ap.persona_juridica_id= :id ");
		} else if( tipoFolio == PERSONAS_AUXILIARES ) {
			sb.append("     and ap.folio_seccion_auxiliar_id= :id ");
		} else if( tipoFolio == MUEBLE ) {
			sb.append("     and ap.mueble_id= :id ");
		} else if( tipoFolio == PREDIO ) {
			sb.append("     and ap.predio_id= :id ");
		}
		sb.append(" group by ca.id, ca.nombre ");

		Map namedParameters = new HashMap();
		namedParameters.put("statusActoIdVigente", Constantes.StatusActo.ACTIVO.getIdStatusActo());
		namedParameters.put("statusActoNoVigente", Constantes.StatusActo.INVALIDO.getIdStatusActo());
		namedParameters.put("id", id);

		SqlRowSet rs = namedparameterJdbcTemplate.queryForRowSet(sb.toString(), namedParameters);

		while (rs.next()) {
			vo = new ActoVO();
			vo.setClasifActoId(rs.getLong(1));
			vo.setNombre(rs.getString(2));
			vo.setVigentes(rs.getInt(3));
			vo.setExtintos(rs.getInt(4));
			result.add(vo);

		}
		return result;
	}
	
	public List<TramiteVO> findTramitesInscritos( Integer id, Long statusActoId, Long clasifActoId,Constantes.ETipoFolio tipoFolio ) {
		
		List<TramiteVO> result = new ArrayList<TramiteVO>();

		StringBuilder sb = new StringBuilder();
		sb.append(" select p.consecutivo, p.fecha_ingreso, status.nombre, ");
		sb.append("     case  ");
		sb.append("         when a.id is not null then  ta.nombre  ");
		sb.append("         when ps.servicio_id is not null then  s.nombre ");
		sb.append("         when ps.sub_servicio_id is not null then  ss.nombre  ");
		sb.append("         when ps.sub_sub_servicio_id is not null then  sss.nombre "); 		
		//sb.append("     end as tramite, a.id as actoId, ta.id as tipoActo, a.vf, case when d.numero is null  then  d.numero2 else d.numero::varchar(20) end , a.fecha_registro ");	
		sb.append("     end as tramite, a.id as actoId, ta.id as tipoActo, a.vf, case when d.numero is null  then  d.numero2 else d.numero::varchar(20) end , a.fecha_registro, a.copiado_modificado, q1.statusActo, q1.clasif_acto_id , ap.tipo_entrada_id ");
		sb.append(" from ( ");
		//sb.append("   select p.CONSECUTIVO, a.id as actoId, MAX(ap.VERSION) as version1, MAX(a.version) as version2, MAX(ad.version) as version3 ");
		//sb.append("   from acto a ");
		sb.append("   select p.CONSECUTIVO, a.id as actoId, MAX(ap.VERSION) as version1, MAX(a.version) as version2, MAX(ad.version) as version3 , sa.nombre as statusActo, ta.clasif_acto_id ");
		sb.append("   from acto a join status_acto sa on sa.id = a.status_acto_id");
		sb.append("   join tipo_acto ta on a.tipo_acto_id=ta.id ");
		sb.append("   join clasif_acto ca on ta.clasif_acto_id=ca.id ");
		sb.append("   join acto_predio ap on ap.acto_id=a.id ");
		sb.append("   join prelacion p on p.id=a.prelacion_id ");
		sb.append("   left join acto_documento ad on ad.acto_id = a.id ");
		if( statusActoId != null ) {
			sb.append("   where a.status_acto_id= :statusActoId ");
		} else
		{
			sb.append("   where a.status_acto_id= a.status_acto_id   ");
		}
		if( tipoFolio == PERSONAS_JURIDICAS ) {
			sb.append("     and ap.persona_juridica_id= :id ");
		} else if( tipoFolio == PERSONAS_AUXILIARES ) {
			sb.append("     and ap.folio_seccion_auxiliar_id= :id ");
		} else if( tipoFolio == MUEBLE ) {
			sb.append("     and ap.mueble_id= :id ");
		} else if( tipoFolio == PREDIO ) {
			sb.append("     and ap.predio_id= :id ");
		}

		if( clasifActoId != null ) {
			sb.append("   and ca.id= :clasifActoId ");
		} 
		sb.append("   group by p.consecutivo, a.id, sa.nombre, ta.clasif_acto_id " );
		
//		sb.append("   and ca.id= :clasifActoId ");		 
//		sb.append("   group by p.consecutivo, a.id " );
		sb.append("   order by p.consecutivo ) q1, ");
		sb.append(" acto a ");
		sb.append(" join tipo_acto ta on a.tipo_acto_id=ta.id ");
		sb.append(" join clasif_acto ca on ta.clasif_acto_id=ca.id ");
		sb.append(" join acto_predio ap on ap.acto_id=a.id ");
		sb.append(" join prelacion p on p.id=a.prelacion_id ");
		sb.append(" left join prelacion_servicio ps on ps.prelacion_id = p.id ");
		sb.append(" left join servicio s on ps.servicio_id = s.id ");
		sb.append(" left join sub_servicio ss on ps.sub_servicio_id= ss.id ");
		sb.append(" left join sub_sub_servicio sss on ps.sub_sub_servicio_id= sss.id ");
		sb.append(" left join acto_documento ad on a.id= ad.acto_id");
		sb.append(" left join documento d on ad.documento_id= d.id");
		sb.append(" join status on p.status_id=status.id ");
		sb.append(" where q1.consecutivo = p.consecutivo ");
		sb.append("   and q1.version1 = ap.version ");
		sb.append("   and q1.version2 = a.version ");
		sb.append("   and (q1.version3 = ad.version or q1.version3 is null) ");
		sb.append("   and q1.actoId = a.id ");
		//sb.append("   and a.status_acto_id= :statusActoId ");
		if( statusActoId != null ) {
			sb.append("   and a.status_acto_id= :statusActoId "); 
		} else {
			Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
			
			boolean esRolValidacion = false;
			
		    List<UsuarioRol> usuarioRoles = usuario.getUsuarioRolesParaUsuarios()
		    					.stream()
		    					.collect(Collectors.toList());
			
		    for (UsuarioRol usuRol : usuarioRoles) {
				if(usuRol.getRol().getId() == 5L) {
					esRolValidacion = true;
				} 
			}
		    
			if(esRolValidacion) {
				sb.append("   and a.vf = true  ");
			}
			
		}
		if( tipoFolio == PERSONAS_JURIDICAS ) {
			sb.append("     and ap.persona_juridica_id= :id ");
		} else if( tipoFolio == PERSONAS_AUXILIARES ) {
			sb.append("     and ap.folio_seccion_auxiliar_id= :id ");
		} else if( tipoFolio == MUEBLE ) {
			sb.append("     and ap.mueble_id= :id ");
		} else if( tipoFolio == PREDIO ) {
			sb.append("     and ap.predio_id= :id ");
		}
//		sb.append("   and ca.id= :clasifActoId ");		
//		sb.append(" order by (case when  p.fecha_ingreso is null then a.fecha_registro else p.fecha_ingreso end), p.consecutivo" );
		
		if( clasifActoId != null ) {
			sb.append("   and ca.id= :clasifActoId ");
		}

		if( statusActoId == null && clasifActoId == null ) {
			sb.append("     order by a.status_acto_id, (case when  p.fecha_ingreso is null then a.fecha_registro else p.fecha_ingreso end), p.consecutivo ");
		} else {
			sb.append(" order by (case when  p.fecha_ingreso is null then a.fecha_registro else p.fecha_ingreso end), p.consecutivo" );
		}
		
		Map namedParameters = new HashMap();
		namedParameters.put("statusActoId", statusActoId);
		namedParameters.put("id", id);
		namedParameters.put("clasifActoId", clasifActoId);

		SqlRowSet rs = namedparameterJdbcTemplate.queryForRowSet(sb.toString(), namedParameters);

		Long vf;
		TramiteVO vo;
		while (rs.next()) {
			vo = new TramiteVO();
			vo.setConsecutivo(rs.getLong(1));
			if (rs.getObject(2)!=null){
				java.sql.Timestamp ts = (java.sql.Timestamp) rs.getObject(2);
				
				try {
					vo.setFecha(new Date(ts.getTime()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			vo.setStatus(rs.getString(3));
			vo.setTramite(rs.getString(4));
			vo.setActoId(rs.getLong(5));
			vo.setTipoActo(rs.getLong(6));
			vf = rs.getBoolean(7) ? 1L : 0L;
			vo.setCopiadoModificado(rs.getBoolean(10));
			vo.setStatusActo(rs.getString(11));
			vo.setClasificacionId(rs.getLong(12));
			vo.setTipoEntrada(rs.getLong(13));
			if( vf != null && vf == 1 ) {
				vo.setLeyendaVF("(CV)");
			}
			vo.setDocumento(rs.getString(8));
			if (rs.getObject(9)!=null){
				java.sql.Timestamp ts = (java.sql.Timestamp) rs.getObject(9);
				
				try {
					vo.setFechaActo(new Date(ts.getTime()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			result.add(vo);

		}
		
		return result;
	}
	
	public List<TramiteVO> findTramitesParaPredio(Long id, Constantes.ETipoFolio tipoFolio) {
		List<TramiteVO> result = new ArrayList<TramiteVO>();
		Map<Long,List<TramiteVO>> hmResult = new HashMap<Long,List<TramiteVO>>();
		StringBuilder sb = new StringBuilder();
		
		
	
		 sb.append(" select ");
		 sb.append("  distinct ");
		  sb.append("  p.consecutivo, ");
		  sb.append("   p.fecha_ingreso, ");
		  sb.append("   s.nombre status, ");
		  sb.append("   ta.nombre tramite, ");
		   sb.append("  a.id, ");
		   sb.append("  ta.id, ");
		   sb.append("  p.status_id, ");
		   sb.append("  COALESCE(d.numero::varchar(20),d.numero2) documento, ");
		   sb.append("  false servicio   "); 
		 sb.append(" from acto_predio pp ");
		 sb.append(" left join acto a on a.id = pp.acto_id ");
		 sb.append(" left join tipo_acto ta on ta.id =  a.tipo_acto_id ");
		 sb.append(" left join acto_documento ad on ad.acto_id =  a.id ");
		 sb.append(" left join documento d on d.id =  ad.documento_id ");
		 sb.append(" left join prelacion p on p.id =  a.prelacion_id ");
		 sb.append(" left join status s on s.id =  p.status_id ");
		 switch (tipoFolio) {
			case PREDIO:
				sb.append(" where pp.predio_id = :id and p.consecutivo > 0 ");
				break;
			case MUEBLE:
				sb.append(" where pp.mueble_id = :id and p.consecutivo > 0 ");
				break;
			case PERSONAS_AUXILIARES:
				sb.append(" where pp.folio_seccion_auxiliar_id = :id and p.consecutivo > 0 ");
				
				break;
			case PERSONAS_JURIDICAS:
				sb.append(" where pp.persona_juridica_id = :id and p.consecutivo > 0 ");
				break;
			}		
		 
		 sb.append(" and (a.vf is null or a.vf = false)  ");
		 sb.append(" and (a.remplazado is null or a.remplazado= :remplazado) ");
		 sb.append(" and p.status_id in(1,2,3,4,6,5,10,7,8,15,16,17,22,23,24) ");
		 sb.append(" union  ");
		 sb.append(" select ");
		 sb.append(" distinct ");
		   sb.append("  p.consecutivo, ");
		   sb.append("  p.fecha_ingreso, ");
		   sb.append("  st.nombre status, ");
		   sb.append("  s.nombre tramite, ");
		   sb.append("  s.id, ");
		   sb.append("  p.id, ");
		   sb.append("  p.status_id, ");
		   sb.append("  null,   ");
		   sb.append("  true servicio   ");
		 sb.append(" from  prelacion_servicio ps ");
		 sb.append(" left join prelacion p on p.id =  ps.prelacion_id  ");
		 sb.append(" left join status st on st.id = p.status_id ");
		 sb.append(" left join servicio s on s.id =  ps.servicio_id  ");
		 sb.append(" left join prelacion_predio pp on pp.prelacion_id = p.id ");
		 
		 switch (tipoFolio) {
			case PREDIO:
				 sb.append(" where p.consecutivo > 0 and pp.predio_id =  :id ");
				break;
			case MUEBLE:
				 sb.append(" where p.consecutivo > 0 and pp.mueble_id =  :id ");
				break;
			case PERSONAS_AUXILIARES:
				 sb.append(" where p.consecutivo > 0 and pp.folio_seccion_auxiliar_id =  :id ");
				break;
			case PERSONAS_JURIDICAS:
				 sb.append(" where p.consecutivo > 0 and pp.persona_juridica_id =  :id ");
				break;
			}		
		
		
		 sb.append(" and p.status_id in(1,2,3,4,6,5,10,7,8,15,16,17,22,23,24) ");
		
	/*	sb.append("select p.consecutivo, p.fecha_ingreso, status.nombre, ");
		sb.append("    case ");
		sb.append("        when a.id is not null then  ta.nombre ");
		sb.append("        when ps.servicio_id is not null then  s.nombre ");
		sb.append("        when ps.sub_servicio_id is not null then  ss.nombre ");
		sb.append("        when ps.sub_sub_servicio_id is not null then  sss.nombre ");
		// NVL and to_char no exite en POSGREST--------------------------------------------28/05/2018
		//sb.append("    end as tramite, a.id, ta.id, NVL(to_char(d.numero),d.numero2) ");
		sb.append("	   end as tramite, a.id, ta.id, COALESCE(to_char(d.numero, 'FM999999999999999999'),d.numero2) ");
		sb.append("from ( ");
		sb.append("    select p.CONSECUTIVO, a.id as actoId, ps.id as prelservId, ta.id as tipoActoId, max(ad.version) as versionDoc, max(ap.version) as versionAP from prelacion p ");
		sb.append("    left join acto a on a.PRELACION_ID = p.id ");
		sb.append("    left join acto_predio ap on ap.acto_id = a.id ");
		switch (tipoFolio) {
		case PREDIO:
			sb.append("and ap.predio_id = :id ");
			break;
		case MUEBLE:
			sb.append("and ap.mueble_id = :id ");
			break;
		case PERSONAS_AUXILIARES:
			sb.append("and ap.folio_seccion_auxiliar_id = :id ");
			break;
		case PERSONAS_JURIDICAS:
			sb.append("and ap.persona_juridica_id = :id ");
			break;
		}		
		sb.append("    left join tipo_acto ta on a.tipo_acto_id = ta.id ");
		sb.append("    left join prelacion_servicio ps on ps.prelacion_id = p.id ");
		sb.append("    left join servicio s on ps.servicio_id = s.id ");
		sb.append("    left join sub_servicio ss on ps.sub_servicio_id= ss.id ");
		sb.append("    left join sub_sub_servicio sss on ps.sub_sub_servicio_id= sss.id ");
		sb.append("    left join acto_documento ad on ad.ACTO_ID = a.id ");
		switch (tipoFolio) {
		case PREDIO:
			sb.append("    left join prelacion_predio pp on pp.prelacion_id=p.id ");
			sb.append("    where pp.predio_id = :id ");
			break;
		case MUEBLE:
			sb.append("where ap.mueble_id = :id ");
			break;
		case PERSONAS_AUXILIARES:
			sb.append("where ap.folio_seccion_auxiliar_id = :id ");
			break;
		case PERSONAS_JURIDICAS:
			sb.append("where ap.persona_juridica_id = :id ");
			break;
		}
		// CONDITION = 0 no funciona en POSGREST--------------------------------------------28/05/2018
		//sb.append("    and (a.vf is null or a.vf = 0) ");
		sb.append("    and (a.vf is null or a.vf = false) ");
		sb.append("    and (a.id is not null or ps.id is not null) ");
		sb.append("    and (a.remplazado is null or a.remplazado= :remplazado) ");
		sb.append("    AND p.consecutivo > 0 ");
		sb.append("    group by p.CONSECUTIVO, a.id, ps.id , ta.id ");
		sb.append("    order by p.CONSECUTIVO ");
		sb.append(") q1, ");
		sb.append("prelacion p ");
		sb.append("left join acto a on a.prelacion_id=p.id ");
		sb.append("left join acto_predio ap on ap.acto_id = a.id ");
		switch (tipoFolio) {
		case PREDIO:
			sb.append("and ap.predio_id = :id ");
			break;
		case MUEBLE:
			sb.append("and ap.mueble_id = :id ");
			break;
		case PERSONAS_AUXILIARES:
			sb.append("and ap.folio_seccion_auxiliar_id = :id ");
			break;
		case PERSONAS_JURIDICAS:
			sb.append("and ap.persona_juridica_id = :id ");
			break;
		}		
		sb.append("left join tipo_acto ta on a.tipo_acto_id = ta.id ");
		sb.append("left join prelacion_servicio ps on ps.prelacion_id = p.id ");
		sb.append("left join servicio s on ps.servicio_id = s.id ");
		sb.append("left join sub_servicio ss on ps.sub_servicio_id= ss.id ");
		sb.append("left join sub_sub_servicio sss on ps.sub_sub_servicio_id= sss.id ");
		sb.append("left join status on p.status_id=status.id ");
		sb.append("left join acto_documento ad on ad.acto_id = a.id ");
		sb.append("left join documento d on d.id = ad.documento_id ");
		switch (tipoFolio) {
		case PREDIO:
			sb.append(" left join prelacion_predio pp on pp.prelacion_id=p.id ");
			sb.append(" where pp.predio_id = :id ");
			break;
		case MUEBLE:
			sb.append("where ap.mueble_id = :id ");
			break;
		case PERSONAS_AUXILIARES:
			sb.append("where ap.folio_seccion_auxiliar_id = :id ");
			break;
		case PERSONAS_JURIDICAS:
			sb.append("where ap.persona_juridica_id = :id ");
			break;
		}
		sb.append("and q1.CONSECUTIVO = p.CONSECUTIVO ");
		sb.append("and ( q1.actoId = a.id or q1.prelservId = ps.id ) ");
		sb.append("and ( q1.versionAP = ap.version or q1.versionAP is null ) ");
		sb.append("and ( q1.versionDoc = ad.version or q1.versionDoc is null ) ");
		// CONDITION = 0 no funciona en POSGREST--------------------------------------------28/05/2018
		//sb.append("and (a.vf is null or a.vf = 0) ");
		sb.append("and (a.vf is null or a.vf = false) ");
		sb.append("AND p.consecutivo > 0 "); // Prelaciones válidas tienen un folio asignado mayor a 0
		sb.append("order by p.fecha_ingreso, p.consecutivo");*/
		
		
		
		Map namedParameters = new HashMap();
		
		namedParameters.put("id", id);
		namedParameters.put("remplazado", false);  
		
		logger.debug(sb.toString());
		SqlRowSet rs = namedparameterJdbcTemplate.queryForRowSet(sb.toString(), namedParameters);

		agregarResultadoQueryATramites(result, rs);
		agregarTramitesOrigen(result, id);


		return result.stream().sorted(Comparator.comparingLong(TramiteVO::getActoId)).collect(Collectors.toList());
	}
	
	private void agregarResultadoQueryATramites(List<TramiteVO> result, SqlRowSet rs) {
		TramiteVO vo = null;
		
		while (rs.next()) {
			vo = new TramiteVO();
			vo.setConsecutivo(rs.getLong(1));
			if (rs.getObject(2)!=null){
				java.sql.Timestamp ts = (java.sql.Timestamp) rs.getObject(2);
				
				try {
					vo.setFecha(new Date(ts.getTime()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			vo.setStatus(rs.getString(3));
			vo.setTramite(rs.getString(4));
			vo.setActoId(rs.getLong(5));
			vo.setTipoActo(rs.getLong(6));
			vo.setDocumento( rs.getString(8) );
			if(rs.getBoolean(9)) {
				result.add(vo);
			}else {
			if(!result.stream().anyMatch(obj -> obj.getActoId() == rs.getLong(5) ))
			   result.add(vo);
			}

		}
	}
	
	private void agregarTramitesOrigen(List<TramiteVO> result, Long id) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ");
		 sb.append("  distinct ");
		  sb.append("  p.consecutivo, ");
		  sb.append("   p.fecha_ingreso, ");
		  sb.append("   s.nombre status, ");
		  sb.append("   ta.nombre tramite, ");
		   sb.append("  a.id, ");
		   sb.append("  ta.id, ");
		   sb.append("  null, ");
		   sb.append("  COALESCE(d.numero::varchar(20),d.numero2) documento, ");
		   sb.append(" false servicio ");
		 sb.append(" from acto_predio_campo apc ");
		 sb.append(" inner join acto_predio pp on pp.id = apc.acto_predio_id ");
		 sb.append(" left join acto a on a.id = pp.acto_id ");
		 sb.append(" left join tipo_acto ta on ta.id =  a.tipo_acto_id ");
		 sb.append(" left join acto_documento ad on ad.acto_id =  a.id ");
		 sb.append(" left join documento d on d.id =  ad.documento_id ");
		 sb.append(" left join prelacion p on p.id =  a.prelacion_id ");
		 sb.append(" left join status s on s.id =  p.status_id ");
		 
				sb.append(" where apc.valor = :id and apc.modulo_campo_id = 557 and p.consecutivo > 0 ");
		 
		 sb.append(" and (a.vf is null or a.vf = false)  ");
		 sb.append(" and (a.remplazado is null or a.remplazado= :remplazado) ");
		 sb.append(" union  ");
		 sb.append(" select ");
		 sb.append(" distinct ");
		   sb.append("  p.consecutivo, ");
		   sb.append("  p.fecha_ingreso, ");
		   sb.append("  st.nombre status, ");
		   sb.append("  s.nombre tramite, ");
		   sb.append("  s.id, ");
		   sb.append("  p.id, ");
		   sb.append("  null, ");
		   sb.append("  null ,  ");
		   sb.append("  true servicio   ");
		 //sb.append(" from  prelacion_servicio ps ");
		   sb.append("  from acto_predio_campo apc   ");
		   sb.append("  inner join acto_predio ap on apc.acto_predio_id = ap.id   ");
		   sb.append("  inner join acto a on ap.acto_id = a.id   ");
		   sb.append("  inner join prelacion p on a.prelacion_id = p.id   ");
		   sb.append("  inner join prelacion_servicio ps on ps.prelacion_id = p.id   ");
         
		 //sb.append(" left join prelacion p on p.id =  ps.prelacion_id  ");
		 sb.append(" left join status st on st.id = p.status_id ");
		 sb.append(" left join servicio s on s.id =  ps.servicio_id  ");
		 sb.append(" left join prelacion_predio pp on pp.prelacion_id = p.id ");
		 
		 sb.append(" where p.consecutivo > 0 and  apc.valor = :id and apc.modulo_campo_id = 557 ");

		Map namedParameters = new HashMap();

		namedParameters.put("id", String.valueOf(id));
		namedParameters.put("remplazado", false);
		SqlRowSet rs = namedparameterJdbcTemplate.queryForRowSet(sb.toString(), namedParameters);

		agregarResultadoQueryATramites(result, rs);
	}
	
	
public List<PredioVO> findFoliosResultantes(Long idPredio){
		
		List<PredioVO> result =  new ArrayList<PredioVO>();
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct pr.predio_sig_id,p.no_folio,p.superficie_m2,p.superficie,p.no_lote,p.manzana from predio_rel pr left join predio p on p.id =  pr.predio_sig_id where pr.predio_id  = :procede_de_folio");
		
		Map namedParameters = new HashMap();
		namedParameters.put("procede_de_folio",idPredio);

		SqlRowSet rs = namedparameterJdbcTemplate.queryForRowSet(sb.toString(), namedParameters);
		PredioVO vo;
		while (rs.next()) {
			vo = new PredioVO();
			vo.setNoFolio(rs.getInt(2));
			vo.setSuperficieM2(rs.getDouble(3));
			vo.setSuperficie(rs.getString(4));
			vo.setNoLote(rs.getString(5));
			vo.setManzana(rs.getString(6));
			result.add(vo);
		}
		
		return result;

}

public List<PrelacionModuloVO> findPrelacionesRechazadas(Long predioId,Long personaCivilId){
	List<PrelacionModuloVO> result =  new ArrayList<PrelacionModuloVO>();
	StringBuilder sb = new StringBuilder();
	sb.append("select distinct p.id,p.consecutivo,p.anio from prelacion_predio as pp "). 
			append(" join prelacion as p on p.id = pp.prelacion_id ").
			append(" join acto  as a   on a.prelacion_id = p.id ");
			if(predioId!=null)
				sb.append("where pp.predio_id = :predioId");
			if(personaCivilId!=null)
				sb.append("where pp.persona_juridica_id = :personaCivilId");
			sb.append(" and a.status_acto_id = 5 and p.id_entrada is null ")
			.append(" and p.status_id in (10,7,8) order by p.consecutivo asc ");
			
	
	Map namedParameters = new HashMap();
	namedParameters.put("predioId",predioId);
	namedParameters.put("personaCivilId",personaCivilId);

	SqlRowSet rs = namedparameterJdbcTemplate.queryForRowSet(sb.toString(), namedParameters);
	PrelacionModuloVO vo;
	while (rs.next()) {
		vo = new PrelacionModuloVO();
		vo.setId(rs.getLong(1));
		vo.setConsecutivo(rs.getString(2));
		vo.setAnio(rs.getString(3));
		result.add(vo);
	}
	
	return result;
}


public List<PrelacionModuloVO> findPrelacionesSuspendidas(Long predioId,Long personaCivilId){
	List<PrelacionModuloVO> result =  new ArrayList<PrelacionModuloVO>();
	StringBuilder sb = new StringBuilder();
	sb.append("select distinct p.id,p.consecutivo,p.anio from prelacion_predio as pp "). 
			append(" join prelacion as p on p.id = pp.prelacion_id ").
			append(" join acto  as a   on a.prelacion_id = p.id ");
			if(predioId!=null)
				sb.append("where pp.predio_id = :predioId");
			if(personaCivilId!=null)
				sb.append("where pp.persona_juridica_id = :personaCivilId");

			sb.append(" and p.status_id = 17 and p.id_entrada is null order by p.consecutivo asc ");
			
	
	Map namedParameters = new HashMap();
	namedParameters.put("predioId",predioId);
	namedParameters.put("personaCivilId",personaCivilId);

	SqlRowSet rs = namedparameterJdbcTemplate.queryForRowSet(sb.toString(), namedParameters);
	PrelacionModuloVO vo;
	while (rs.next()) {
		vo = new PrelacionModuloVO();
		vo.setId(rs.getLong(1));
		vo.setConsecutivo(rs.getString(2));
		vo.setAnio(rs.getString(3));
		result.add(vo);
	}
	
	return result;
}

	public List<ActoPublictarioHisModel> findActoPublicitarioHisPorNombre(String nombre){
		List<ActoPublictarioHisModel> result =  new ArrayList<ActoPublictarioHisModel>();
		StringBuilder sb = new StringBuilder();
	
		sb.append("select acto_predio_id,acto_id,nombre, paterno,materno from ( ").
		append("select apre.id acto_predio_id,a.id acto_id,").
		append(" MAX(case when apc.modulo_campo_id = 20834 then apc.valor else '' end) as nombre,").
		append(" MAX(case when apc.modulo_campo_id = 20835 then apc.valor else '' end) as paterno,"). 
		append(" MAX(case when apc.modulo_campo_id = 20836 then apc.valor else '' end) as materno").
		append(" from acto_publicitario ap").
		append(" join acto a on a.id =  ap.acto_id").
		append(" join acto_predio apre on apre.acto_id =  a.id").
		append(" join acto_predio_campo apc on apc.acto_predio_id =  apre.id").
		append(" where ap.num_folio_real is not null ").
		append(" group by apre.id,a.id").
		append(") q where replace(nombre,' ','')||replace(paterno,' ','')||replace(materno,' ','') like :n");
	
		Map namedParameters = new HashMap();
		namedParameters.put("n","%"+nombre+"%");
		
		SqlRowSet rs = namedparameterJdbcTemplate.queryForRowSet(sb.toString(), namedParameters);
		ActoPublictarioHisModel vo;
		while (rs.next()) {
			vo = new ActoPublictarioHisModel();
			vo.setActoPredioId(rs.getLong(1));
			vo.setActoId(rs.getLong(2));
			vo.setNombre(rs.getString(3));
			vo.setPaterno(rs.getString(4));
			vo.setMaterno(rs.getString(5));
			result.add(vo);
		}
		
		return result;
	}
	public List<ActoPublictarioModel> findActoPublicitarioPorNombre(String nombre,Long tipoActo,Long oficinaId){
		List<ActoPublictarioModel> result =  new ArrayList<ActoPublictarioModel>();
		StringBuilder sb = new StringBuilder();
	
		sb.append("select acto_id, acto_predio_id, ap_id, nombre, paterno, materno from ( ").
		append(" select apre.id acto_predio_id, a.id acto_id, ap.id ap_id,").
		append(" MAX(case when apc.modulo_campo_id = amb.modulo_campo_id and amb.tipo='NOMBRE' then apc.valor else '' end) as nombre,").
		append(" MAX(case when apc.modulo_campo_id = amb.modulo_campo_id and amb.tipo='PATERNO' then apc.valor else '' end) as paterno,"). 
		append(" MAX(case when apc.modulo_campo_id = amb.modulo_campo_id and amb.tipo='MATERNO' then apc.valor else '' end) as materno").
		append(" from acto_publicitario ap").
		append(" join acto a on a.id =  ap.acto_id").
		append(" join acto_predio apre on apre.acto_id =  a.id   and apre.\"version\" = (select \"version\" from acto_predio where acto_id = a.id order by version desc limit 1)").
		append(" join acto_predio_campo apc on apc.acto_predio_id =  apre.id").
		append(" join acto_publicitaro_campos_busqueda amb on amb.modulo_campo_id = apc.modulo_campo_id and amb.tipo_acto =:tipoActo").
		append(" join modulo_campo mc on mc.id =  apc.modulo_campo_id").
		append(" where a.tipo_acto_id =:tipoActo and ap.num_folio_real is  null and ap.numero is not null and ap.oficina_id =:oficinaId").
		append(" group by apre.id,a.id,ap.id,mc.modulo_id,apc.orden").
		append(" ) q where replace(nombre,' ','')||replace(paterno,' ','')||replace(materno,' ','') like '%'||:nombre||'%'");
	
	
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("nombre",nombre);
		parameters.addValue("tipoActo",tipoActo);
		parameters.addValue("oficinaId",oficinaId);
		
		SqlRowSet rs = namedparameterJdbcTemplate.queryForRowSet(sb.toString(), parameters);
		ActoPublictarioModel vo;
		while (rs.next()) {
			vo = new ActoPublictarioModel();
			vo.setActoId(rs.getLong(1));
			vo.setActoPredioId(rs.getLong(2));
			vo.setActoPublicitarioId(rs.getLong(3));
			vo.setNombre(rs.getString(4));
			vo.setPaterno(rs.getString(5));
			vo.setMaterno(rs.getString(6));
			result.add(vo);
		}
		
		return result;
	}
}
