package com.gisnet.erpp.domain;


import javax.persistence.*;

import java.io.Serializable;


/**
 * A StatusVis.
 */
@Entity
@Table(name = "status_vis")
public class StatusVis implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statusVisGenerator")
    @SequenceGenerator(allocationSize = 1, name = "statusVisGenerator", sequenceName="status_vis_seq")
    private Long id;

    @ManyToOne
    private Status statusInterno;
    
    @ManyToOne
    private StatusExterno statusExterno;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public Status getStatusInterno() {
		return statusInterno;
	}

	public void setStatusInterno(Status statusInterno) {
		this.statusInterno = statusInterno;
	}

	public StatusExterno getStatusExterno() {
		return statusExterno;
	}

	public void setStatusExterno(StatusExterno statusExterno) {
		this.statusExterno = statusExterno;
	}

}
