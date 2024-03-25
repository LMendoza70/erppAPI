package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Antecedente.
 */
@Entity
@Table(name = "prel_ante_cap_hist", uniqueConstraints={@UniqueConstraint(columnNames = {"documento","numero_predio", "documento_bis", "libro_id"})})
public class PrelacionAnteCapHist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prelacionAnteCapHistGenerator")
    @SequenceGenerator(allocationSize = 1, name = "prelacionAnteCapHistGenerator",sequenceName="prel_ante_cap_hist_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "documento", length = 100)
    private String documento;

    @Size(max = 100)
    @Column(name = "documento_bis", length = 100)
    private String documentoBis;
    
    @Size(max = 100)
    @Column(name = "carga_trabajo", length = 100)
    private String cargaTrabajo;


    @Column(name = "numero_predio", length = 3)
    private Integer numeroPredio;
    
    @NotNull
    private Boolean indVigencia;
    
    
    @Column(name = "matriz")
    private Boolean matriz;
    
    @ManyToOne
    private StatusAntecedente statusAntecedente;

    @ManyToOne
    private Libro libro;
    
    @ManyToOne(optional = true)
    private Mueble mueble;
    
    @ManyToOne(optional = true)
    private Prelacion prelacion;
    
    @ManyToOne(optional = true)
    private TipoFolio tipoFolio;
    
    @ManyToOne(optional = true)
    private Predio predio;
    
    @ManyToOne(optional = true)
    private PersonaJuridica personaJuridica;
    
    @ManyToOne(optional = true)
    private FolioSeccionAuxiliar folioSeccionAuxiliar;
       
    @ManyToOne(optional = true)
    private Usuario usuarioCaptura;
    
    @ManyToOne(optional = true)
    private Usuario usuarioValidacion;

         
    /*@ManyToOne
    private Mueble mueble;*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumento() {
        return documento;
    }

    public PrelacionAnteCapHist documento(String documento) {
        this.documento = documento;
        return this;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }
    
    public String getDocumentoBis() {
        return documentoBis;
    }

    public PrelacionAnteCapHist documentoBis(String documentoBis) {
        this.documentoBis = documentoBis;
        return this;
    }

    public void setDocumentoBis(String documentoBis) {
        this.documentoBis = documentoBis;
    }
    
    public Libro getLibro() {
        return libro;
    }

    public PrelacionAnteCapHist libro(Libro libro) {
        this.libro = libro;
        return this;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }
    
    public Boolean getMatriz() {
  		return matriz;
  	}

  	public void setMatriz(Boolean matriz) {
  		this.matriz = matriz;
  	}
        
    public Mueble getMueble () {
        return this.mueble;
    }

    public PrelacionAnteCapHist mueble(Mueble mueble) {
        this.mueble = mueble;
        return this;
    }

    public void setMueble(Mueble mueble) {
        this.mueble = mueble;
    }
    
    public Prelacion getPrelacion () {
        return this.prelacion;
    }

    public PrelacionAnteCapHist prelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
        return this;
    }

    public void setPrelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
    }

    public TipoFolio getTipoFolio () {
        return this.tipoFolio;
    }

    public PrelacionAnteCapHist tipoFolio(TipoFolio tipoFolio) {
        this.tipoFolio = tipoFolio;
        return this;
    }

    public void setTipoFolio(TipoFolio tipoFolio) {
        this.tipoFolio = tipoFolio;
    }
    
    public Predio getPredio () {
        return this.predio;
    }

    public PrelacionAnteCapHist predio(Predio predio) {
        this.predio = predio;
        return this;
    }

    public void setPredio(Predio predio) {
        this.predio = predio;
    }
    
    public PersonaJuridica getPersonaJuridica () {
        return this.personaJuridica;
    }

    public PrelacionAnteCapHist personaJuridica(PersonaJuridica personaJuridica) {
        this.personaJuridica = personaJuridica;
        return this;
    }

    public void setPersonaJuridica(PersonaJuridica personaJuridica) {
        this.personaJuridica = personaJuridica;
    }
    
    public FolioSeccionAuxiliar getFolioSeccionAuxiliar () {
        return this.folioSeccionAuxiliar;
    }

    public PrelacionAnteCapHist folioSeccionAuxiliar(FolioSeccionAuxiliar folioSeccionAuxiliar) {
        this.folioSeccionAuxiliar = folioSeccionAuxiliar;
        return this;
    }

    public void setFolioSeccionAuxiliar(FolioSeccionAuxiliar folioSeccionAuxiliar) {
        this.folioSeccionAuxiliar = folioSeccionAuxiliar;
    }
    
    
    public Usuario getUsuarioCaptura() {
        return this.usuarioCaptura;
    }

    public PrelacionAnteCapHist usuarioCaptura(Usuario usuarioCaptura) {
        this.usuarioCaptura = usuarioCaptura;
        return this;
    }

    public void setUsuarioCaptura(Usuario usuarioCaptura) {
        this.usuarioCaptura = usuarioCaptura;
        
    }
    
    public Usuario getUsuarioValidacion() {
        return this.usuarioValidacion;
    }

    public PrelacionAnteCapHist usuarioValidacion(Usuario usuarioValidacion) {
        this.usuarioValidacion = usuarioValidacion;
        return this;
    }

    public void setUsuarioValidacion(Usuario usuarioValidacion) {
        this.usuarioValidacion = usuarioValidacion;
    }
    
    public String getCargaTrabajo() {
        return this.cargaTrabajo;
    }

    public PrelacionAnteCapHist cargaTrabajo(String cargaTrabajo) {
        this.cargaTrabajo = cargaTrabajo;
        return this;
    }

    public void setCargaTrabajo(String cargaTrabajo) {
        this.cargaTrabajo = cargaTrabajo;
    }
    
    public Boolean getIndVigencia() {
        return this.indVigencia;
    }

    public PrelacionAnteCapHist indVigencia(Boolean indVigencia) {
        this.indVigencia = indVigencia;
        return this;
    }

    public void setIndVigencia(Boolean indVigencia) {
        this.indVigencia = indVigencia;
    }
    
    public StatusAntecedente getStatusAntecedente() {
        return this.statusAntecedente;
    }

    public PrelacionAnteCapHist statusAntecedente(StatusAntecedente statusAntecedente) {
        this.statusAntecedente = statusAntecedente;
        return this;
    }

    public void setStatusAntecedente(StatusAntecedente statusAntecedente) {
        this.statusAntecedente = statusAntecedente;
    }
    
    public Integer getNumeroPredio() {
  		return numeroPredio;
  	}

  	public void setNumeroPredio(Integer numeroPredio) {
  		this.numeroPredio = numeroPredio;
  	}
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PrelacionAnteCapHist antecedente = (PrelacionAnteCapHist) o;
        if (antecedente.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), antecedente.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PrelacionAnteCapHist{" +
            "id=" + getId() +
            ", documento='" + getDocumento() + "'" +
            ", documentoBis='" + getDocumentoBis() + "'" +

            "}";
    }

}
