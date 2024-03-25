package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A TipoActo.
 */
@Entity
@Table(name = "tipo_acto")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TipoActo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoActoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoActoGenerator", sequenceName="tipo_acto_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "ponderacion")
    private Integer ponderacion;

    @Column(name = "dias_tramitar")
    private Integer diasTramitar;

    @NotNull
    @Size(max = 1)
    @Column(name = "predios_entrada", length = 1, nullable = false)
    private String prediosEntrada;

    @NotNull
    @Size(max = 1)
    @Column(name = "predios_salida", length = 1, nullable = false)
    private String prediosSalida;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "primer_registro")
    private Boolean primerRegistro;

    @Column(name = "requiere_documento")
    private Boolean requiereDocumento;

    @Column(name = "requiere_captura")
    private Boolean requiereCaptura;

    @Column(name = "replica_por_fusion")
    private Boolean replicaPorFusion;
    
    @Column(name = "sub_clasificacion")
    private Integer subClasificacion;

    @Column(name = "ind_detalle")
    private Boolean ind_detalle;
    
    @OneToMany(mappedBy = "tipoActo")
    @JsonIgnore
    private Set<Acto> actosParaTipoActos = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tipoActo")
    @JsonIgnore
    @OrderBy("orden ASC")
    private Set<ModuloTipoActo> moduloTipoActosParaTipoActos = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tipoActo")
    @JsonIgnore
    private Set<Servicio> serviciosParaTipoActos = new HashSet<>();

    @OneToMany(mappedBy = "tipoActo")
    @JsonIgnore
    private Set<ServicioCosto> servicioCostosParaTipoActos = new HashSet<>();

    @OneToMany(mappedBy = "tipoActo")
    @JsonIgnore
    private Set<SubTipoActo> subTipoActosParaTipoActos = new HashSet<>();

    @OneToMany(mappedBy = "tipoActo")
    @JsonIgnore
    private Set<TipoDocTipoActo> tipoDocTipoActosParaTipoActos = new HashSet<>();
    
    @OneToMany(mappedBy = "tipoActoPadre")
    @JsonIgnore
    private Set<TipoActoTipoActo> tipoActoTipoActoParaTipoActoPadre = new LinkedHashSet<>();

    @OneToMany(mappedBy = "tipoActo")
    @JsonIgnore
    private Set<TipoActoTipoActo> tipoActoTipoActoParaTipoActo = new LinkedHashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private ClasifActo clasifActo;

    @Column(name = "es_testamento")
    private Boolean esTestamento;

    @Column(name = "usa_antecedente")
    private Boolean usaAntecedente;

    @Column(name = "usa_folio")
    private Boolean usaFolio;    

    @Column(name = "clave", length = 10)
    private String clave;

    @Column(name = "masivo")
    private Boolean masivo;
    
    public Boolean getMasivo() {
		return masivo;
	}

	public void setMasivo(Boolean masivo) {
		this.masivo = masivo;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoActo nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPonderacion() {
        return ponderacion;
    }

    public TipoActo ponderacion(Integer ponderacion) {
        this.ponderacion = ponderacion;
        return this;
    }

    public void setPonderacion(Integer ponderacion) {
        this.ponderacion = ponderacion;
    }

    public Integer getDiasTramitar(){
        return diasTramitar;
    }

    public void setDiasTramitar(Integer diasTramitar){
        this.diasTramitar = diasTramitar;
    }

    public String getPrediosEntrada() {
        return prediosEntrada;
    }

    public TipoActo prediosEntrada(String prediosEntrada) {
        this.prediosEntrada = prediosEntrada;
        return this;
    }

    public void setPrediosEntrada(String prediosEntrada) {
        this.prediosEntrada = prediosEntrada;
    }

    public String getPrediosSalida() {
        return prediosSalida;
    }

    public TipoActo prediosSalida(String prediosSalida) {
        this.prediosSalida = prediosSalida;
        return this;
    }

    public void setPrediosSalida(String prediosSalida) {
        this.prediosSalida = prediosSalida;
    }

    public Boolean isActivo() {
        return activo;
    }

    public TipoActo activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean isPrimerRegistro() {
        return primerRegistro;
    }

    public TipoActo primerRegistro(Boolean primerRegistro) {
        this.primerRegistro = primerRegistro;
        return this;
    }

    public void setPrimerRegistro(Boolean primerRegistro) {
        this.primerRegistro = primerRegistro;
    }

    public Boolean isRequiereDocumento() {
        return requiereDocumento == null? false: requiereDocumento;
    }

    public TipoActo requiereDocumento(Boolean requiereDocumento) {
        this.requiereDocumento = requiereDocumento;
        return this;
    }

    public void setRequiereDocumento(Boolean requiereDocumento) {
        this.requiereDocumento = requiereDocumento;
    }


    public Boolean isRequiereCaptura() {
        return requiereCaptura == null? false: requiereCaptura;
    }

    public TipoActo requiereCaptura(Boolean requiereCaptura) {
        this.requiereCaptura = requiereCaptura;
        return this;
    }

    public void setRequiereCaptura(Boolean requiereCaptura) {
        this.requiereCaptura = requiereCaptura;
    }

    public Set<Acto> getActosParaTipoActos() {
        return actosParaTipoActos;
    }

    public TipoActo actosParaTipoActos(Set<Acto> actos) {
        this.actosParaTipoActos = actos;
        return this;
    }

    public TipoActo addActosParaTipoActo(Acto acto) {
        this.actosParaTipoActos.add(acto);
        acto.setTipoActo(this);
        return this;
    }

    public TipoActo removeActosParaTipoActo(Acto acto) {
        this.actosParaTipoActos.remove(acto);
        acto.setTipoActo(null);
        return this;
    }

    public void setActosParaTipoActos(Set<Acto> actos) {
        this.actosParaTipoActos = actos;
    }

    public Set<ModuloTipoActo> getModuloTipoActosParaTipoActos() {
        return moduloTipoActosParaTipoActos;
    }

    public TipoActo moduloTipoActosParaTipoActos(Set<ModuloTipoActo> moduloTipoActos) {
        this.moduloTipoActosParaTipoActos = moduloTipoActos;
        return this;
    }

    public TipoActo addModuloTipoActosParaTipoActo(ModuloTipoActo moduloTipoActo) {
        this.moduloTipoActosParaTipoActos.add(moduloTipoActo);
        moduloTipoActo.setTipoActo(this);
        return this;
    }

    public TipoActo removeModuloTipoActosParaTipoActo(ModuloTipoActo moduloTipoActo) {
        this.moduloTipoActosParaTipoActos.remove(moduloTipoActo);
        moduloTipoActo.setTipoActo(null);
        return this;
    }

    public void setModuloTipoActosParaTipoActos(Set<ModuloTipoActo> moduloTipoActos) {
        this.moduloTipoActosParaTipoActos = moduloTipoActos;
    }

    public Set<Servicio> getServiciosParaTipoActos() {
        return serviciosParaTipoActos;
    }

    public TipoActo serviciosParaTipoActos(Set<Servicio> servicios) {
        this.serviciosParaTipoActos = servicios;
        return this;
    }

    public TipoActo addServiciosParaTipoActo(Servicio servicio) {
        this.serviciosParaTipoActos.add(servicio);
        servicio.setTipoActo(this);
        return this;
    }

    public TipoActo removeServiciosParaTipoActo(Servicio servicio) {
        this.serviciosParaTipoActos.remove(servicio);
        servicio.setTipoActo(null);
        return this;
    }

    public void setServiciosParaTipoActos(Set<Servicio> servicios) {
        this.serviciosParaTipoActos = servicios;
    }

    public Set<ServicioCosto> getServicioCostosParaTipoActos() {
        return servicioCostosParaTipoActos;
    }

    public TipoActo servicioCostosParaTipoActos(Set<ServicioCosto> servicioCostos) {
        this.servicioCostosParaTipoActos = servicioCostos;
        return this;
    }

    public TipoActo addServicioCostosParaTipoActo(ServicioCosto servicioCosto) {
        this.servicioCostosParaTipoActos.add(servicioCosto);
        servicioCosto.setTipoActo(this);
        return this;
    }

    public TipoActo removeServicioCostosParaTipoActo(ServicioCosto servicioCosto) {
        this.servicioCostosParaTipoActos.remove(servicioCosto);
        servicioCosto.setTipoActo(null);
        return this;
    }

    public void setServicioCostosParaTipoActos(Set<ServicioCosto> servicioCostos) {
        this.servicioCostosParaTipoActos = servicioCostos;
    }

    public Set<SubTipoActo> getSubTipoActosParaTipoActos() {
        return subTipoActosParaTipoActos;
    }

    public TipoActo subTipoActosParaTipoActos(Set<SubTipoActo> subTipoActos) {
        this.subTipoActosParaTipoActos = subTipoActos;
        return this;
    }

    public TipoActo addSubTipoActosParaTipoActo(SubTipoActo subTipoActo) {
        this.subTipoActosParaTipoActos.add(subTipoActo);
        subTipoActo.setTipoActo(this);
        return this;
    }

    public TipoActo removeSubTipoActosParaTipoActo(SubTipoActo subTipoActo) {
        this.subTipoActosParaTipoActos.remove(subTipoActo);
        subTipoActo.setTipoActo(null);
        return this;
    }

    public void setSubTipoActosParaTipoActos(Set<SubTipoActo> subTipoActos) {
        this.subTipoActosParaTipoActos = subTipoActos;
    }

    public Set<TipoDocTipoActo> getTipoDocTipoActosParaTipoActos() {
        return tipoDocTipoActosParaTipoActos;
    }

    public TipoActo tipoDocTipoActosParaTipoActos(Set<TipoDocTipoActo> tipoDocTipoActos) {
        this.tipoDocTipoActosParaTipoActos = tipoDocTipoActos;
        return this;
    }

    public TipoActo addTipoDocTipoActosParaTipoActo(TipoDocTipoActo tipoDocTipoActo) {
        this.tipoDocTipoActosParaTipoActos.add(tipoDocTipoActo);
        tipoDocTipoActo.setTipoActo(this);
        return this;
    }

    public TipoActo removeTipoDocTipoActosParaTipoActo(TipoDocTipoActo tipoDocTipoActo) {
        this.tipoDocTipoActosParaTipoActos.remove(tipoDocTipoActo);
        tipoDocTipoActo.setTipoActo(null);
        return this;
    }

    public void setTipoDocTipoActosParaTipoActos(Set<TipoDocTipoActo> tipoDocTipoActos) {
        this.tipoDocTipoActosParaTipoActos = tipoDocTipoActos;
    }

    public ClasifActo getClasifActo() {
        return clasifActo;
    }

    public TipoActo clasifActo(ClasifActo clasifActo) {
        this.clasifActo = clasifActo;
        return this;
    }

    public void setClasifActo(ClasifActo clasifActo) {
        this.clasifActo = clasifActo;
    }

    public Integer getSubClasificacion() {
		return subClasificacion;
	}

	public void setSubClasificacion(Integer subClasificacion) {
		this.subClasificacion = subClasificacion;
	}

  public Boolean getEsTestamento() {
      return esTestamento;
  }

  public TipoActo esTestamento(Boolean esTestamento) {
      this.esTestamento = esTestamento;
      return this;
  }

  public void setEsTestamento(Boolean esTestamento) {
      this.esTestamento = esTestamento;
  }

  public Boolean getUsaAntecedente() {
      return usaAntecedente;
  }

  public TipoActo usaAntecedente(Boolean usaAntecedente) {
      this.usaAntecedente = usaAntecedente;
      return this;
  }

  public void setUsaAntecedente(Boolean usaAntecedente) {
      this.usaAntecedente = usaAntecedente;
  }


  public Boolean getUsaFolio() {
    return usaFolio;
}

public TipoActo usaFolio(Boolean usaFolio) {
    this.usaFolio = usaFolio;
    return this;
}

public void setUsaFolio(Boolean usaFolio) {
    this.usaFolio = usaFolio;
}

   public String getClave() {
        return clave;
    }
   
    public void setClave(String clave) {
        this.clave = clave;
    }
    
	public Set<TipoActoTipoActo> getTipoActoTipoActoParaTipoActoPadre() {
		return tipoActoTipoActoParaTipoActoPadre;
	}

	public void setTipoActoTipoActoParaTipoActoPadre(Set<TipoActoTipoActo> tipoActoTipoActoParaTipoActoPadre) {
		this.tipoActoTipoActoParaTipoActoPadre = tipoActoTipoActoParaTipoActoPadre;
	}
	
	public Set<TipoActoTipoActo> getTipoActoTipoActoParaTipoActo() {
		return tipoActoTipoActoParaTipoActo;
	}

	public void setTipoActoTipoActoParaTipoActo(Set<TipoActoTipoActo> tipoActoTipoActoParaTipoActo) {
		this.tipoActoTipoActoParaTipoActo = tipoActoTipoActoParaTipoActo;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoActo tipoActo = (TipoActo) o;
        if (tipoActo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoActo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoActo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", ponderacion='" + getPonderacion() + "'" +
            ", prediosEntrada='" + getPrediosEntrada() + "'" +
            ", prediosSalida='" + getPrediosSalida() + "'" +
            ", activo='" + isActivo() + "'" +
            ", primerRegistro='" + isPrimerRegistro() + "'" +
            ", requiereDocumento='" + isRequiereDocumento() + "'" +
            "}";
    }
	public Boolean getReplicaPorFusion() {
		return replicaPorFusion==null?false:replicaPorFusion;
	}

 	public void setReplicaPorFusion(Boolean replicaPorFusion) {
		this.replicaPorFusion = replicaPorFusion;	
	}
    

}
