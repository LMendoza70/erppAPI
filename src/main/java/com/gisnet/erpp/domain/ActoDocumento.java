package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ActoDocumento.
 */
@Entity
@Table(name = "acto_documento")
public class ActoDocumento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actoDocumentoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "actoDocumentoGenerator", sequenceName="acto_documento_seq")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private Acto acto;

    @ManyToOne(optional = false)
    @NotNull
    private Documento documento;
    
    @Column(name = "version", length = 3, nullable = false)
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Acto getActo() {
        return acto;
    }

    public ActoDocumento acto(Acto acto) {
        this.acto = acto;
        return this;
    }

    public void setActo(Acto acto) {
        this.acto = acto;
    }

    public Documento getDocumento() {
        return documento;
    }

    public ActoDocumento documento(Documento documento) {
        this.documento = documento;
        return this;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }
    
    public Integer getVersion() {
        return version;
    }

    public ActoDocumento version(Integer version) {
        this.version = version;
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ActoDocumento actoDocumento = (ActoDocumento) o;
        if (actoDocumento.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), actoDocumento.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActoDocumento{" +
            "id=" + getId() +
            "}";
    }
}
