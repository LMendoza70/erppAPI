package com.gisnet.erpp.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A BitacoraDig.
 */
@Entity
@Table(name = "bitacora_dig")
public class BitacoraDig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bitacoraDigGenerator")
    @SequenceGenerator(allocationSize = 1, name = "bitacoraDigGenerator", sequenceName="bitacora_dig_seq")
    private Long id;

    @Column(name = "bitkey", length = 18)
    private Integer bitkey;
    
    @ManyToOne(optional = false)
    @NotNull    
    private Libro libro;

    @Size(max = 100)
    @Column(name = "documento", length = 100)
    private String documento;

    @Size(max = 100)
    @Column(name = "documento_bis", length = 100)
    private String documentoBis;


    @Column(name = "cat_obs", length = 2048)
    private String catObs;    
    
    
    
    public String getDocumentoBis() {
		return documentoBis;
	}

	public void setDocumentoBis(String documentoBis) {
		this.documentoBis = documentoBis;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBitkey() {
        return bitkey;
    }

    public void setBitkey(Integer bitkey) {
        this.bitkey = bitkey;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getCatObs() {
        return catObs;
    }

    public void setCatObs(String catObs) {
        this.catObs = catObs;
    }
    
    public Libro getLibro() {
		return libro;
	}

	public void setLibro(Libro libro) {
		this.libro = libro;
	}


	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BitacoraDig bitacoraDig = (BitacoraDig) o;
        if (bitacoraDig.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bitacoraDig.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BitacoraDig{" +
            "id=" + getId() +
            "}";
    }

}
