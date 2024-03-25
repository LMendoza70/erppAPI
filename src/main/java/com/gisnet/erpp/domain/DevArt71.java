package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Date;

/**
 * A DevArt71.
 */
@Entity
@Table(name = "dev_art_71")
public class DevArt71 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "devArt71Generator")
    @SequenceGenerator(allocationSize = 1, name = "devArt71Generator", sequenceName="dev_art_71_seq")
    private Long id;

    @ManyToOne
    private Predio predio;

    @ManyToOne
    private Oficina oficina;

    @ManyToOne
    private Prelacion prelacion;

    @ManyToOne
    private Usuario usuario;

    @Column(name="FECHA_INGRESO")
    private Date fechaIngreso;

    @Column(name="EXPEDIENTE", length=10)
    private String expediente;

    @Column(name="SOLICITANTE", length = 400)
    private String solicitante;

    @Column(name="CAUSA_RECHAZO", length=2000)
    private String causaRechazo;

    @Column(name="FECHA_NOTA")
    private Date fechaNota;
    
    @Column(name="SOLICITUD", length=400)
    private String solicitud;
    
    @ManyToOne
    private PersonaJuridica personaJuridica;
    
    @ManyToOne
    private FolioSeccionAuxiliar folioSeccionAuxiliar;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Predio getPredio(){
        return predio;
    }

    public void setPredio(Predio predio){
        this.predio = predio;
    }

    public Oficina getOficina(){
        return oficina;
    }

    public void setOficina(Oficina oficina){
        this.oficina = oficina;
    }

    public Prelacion getPrelacion(){
        return prelacion;
    }

    public void setPrelacion(Prelacion prelacion){
        this.prelacion = prelacion;
    }

    public Usuario getUsuario(){
        return usuario;
    }

    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
    }

    public Date getFechaIngreso(){
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso){
        this.fechaIngreso = fechaIngreso;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getCausaRechazo() {
        return causaRechazo;
    }

    public void setCausaRechazo(String causaRechazo) {
        this.causaRechazo = causaRechazo;
    }

    public Date getFechaNota() {
        return fechaNota;
    }

    public void setFechaNota(Date fechaNota) {
        this.fechaNota = fechaNota;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DevArt71 devArt71 = (DevArt71) o;
        if (devArt71.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), devArt71.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "devArt71{" +
            "id=" + getId() +
            "}";
    }

	public String getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(String solicitud) {
		this.solicitud = solicitud;
	}

	public PersonaJuridica getPersonaJuridica() {
		return personaJuridica;
	}

	public void setPersonaJuridica(PersonaJuridica personaJuridica) {
		this.personaJuridica = personaJuridica;
	}

	public FolioSeccionAuxiliar getFolioSeccionAuxiliar() {
		return folioSeccionAuxiliar;
	}

	public void setFolioSeccionAuxiliar(FolioSeccionAuxiliar folioSeccionAuxiliar) {
		this.folioSeccionAuxiliar = folioSeccionAuxiliar;
	}
}
