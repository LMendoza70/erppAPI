package com.gisnet.erpp.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "serv_cotiz_tipo_acto")
public class ServCotizTipoActo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "servCotizTipoActoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "servCotizTipoActoGenerator", sequenceName="serv_cotiz_tipo_acto_seq")
    private Long id;
    
    @ManyToOne(optional = false)
    @NotNull
    private TipoActo tipoActo;

    @ManyToOne
    private ServiciosCotizador serviciosCotizador;
    
    @ManyToOne
    private ServicioClasifConcepto servicioClasifConcepto;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public ServiciosCotizador getServiciosCotizador() {
        return serviciosCotizador;
    }

    public void setServiciosCotizador(ServiciosCotizador serviciosCotizador) {
        this.serviciosCotizador = serviciosCotizador;
    }

    public TipoActo getTipoActo() {
		return tipoActo;
	}

	public void setTipoActo(TipoActo tipoActo) {
		this.tipoActo = tipoActo;
	}

	public ServicioClasifConcepto getServicioClasifConcepto() {
		return servicioClasifConcepto;
	}

	public void setServicioClasifConcepto(ServicioClasifConcepto servicioClasifConcepto) {
		this.servicioClasifConcepto = servicioClasifConcepto;
	}

	@Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServCotizTipoActo{" +
            "id=" + getId() +
            "}";
    }
}
