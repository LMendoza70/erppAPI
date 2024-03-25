package com.gisnet.erpp.vo;

import com.gisnet.erpp.domain.Archivo;
import com.gisnet.erpp.domain.DocumentoArchivo;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.Requisito;
import com.gisnet.erpp.domain.TipoDocumento;
import java.util.List;
import java.util.ArrayList;
//import org.springframework.web.multipart.MultipartFile;


public class RequisitoVO {
	
	private Acto          acto;
	private TipoDocumento tipoDocumento;
	private Long          idDocumento;
	private Requisito     requisito;
	private Boolean       fundatorio;
	private ArrayList<Archivo>     archivos = new ArrayList<Archivo>();
	private List<DocumentoArchivo>   documentoArchivo = new ArrayList<DocumentoArchivo>();
	private Long          idActoRequisito;
    private Long          idActoDocumento;
	private Long          idServicioRequisito;
	private String        observaciones;
	private Integer       cantidad;



	public ArrayList<Archivo> getArchivos() {
		return archivos;
	}
	public void setArchivos(ArrayList<Archivo> archivos) {
		this.archivos = archivos;
	}
	
	public  Acto getActo() {
		return acto;
	}
	public void setActo( Acto acto) {
		this.acto = acto;
	}
	
	public Requisito getRequisito() {
		return requisito;
	}
	
	public void setRequisito(Requisito requisito) {
		this.requisito = requisito;
	}


	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public Boolean getFundatorio() {
		return fundatorio;
	}
	public void setFundatorio(Boolean fundatorio) {
		this.fundatorio = fundatorio;
	}

	public Long getIdDocumento() {
		return idDocumento;
	}
	public void setIdDocumento(Long idDocumento) {
		this.idDocumento = idDocumento;
	}

	public Long	getIdActoRequisito(){
		return this.idActoRequisito;
	}

	public void setIdActoRequisito(Long idActoRequisito) {
		this.idActoRequisito = idActoRequisito;
	}


	public Long	getIdActoDocumento(){
		return this.idActoDocumento;
	}

	public void setIdActoDocumento(Long idActoDocumento) {
		this.idActoDocumento = idActoDocumento;
	}


	public Long	getIdServicioRequisito(){
		return this.idServicioRequisito;
	}

	public void setIdServicioRequisito(Long idServicioRequisito) {
		this.idServicioRequisito = idServicioRequisito;
	}


	 public String getObservaciones() {
        return observaciones;
    }


    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    
    public List<DocumentoArchivo> getDocumentoArchivo() {
		return documentoArchivo;
	}
	public void setDocumentoArchivo(List<DocumentoArchivo> documentoArchivo) {
		this.documentoArchivo = documentoArchivo;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	
}
