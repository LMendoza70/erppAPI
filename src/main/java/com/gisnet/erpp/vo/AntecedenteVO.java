package com.gisnet.erpp.vo;

import com.gisnet.erpp.domain.Antecedente;
import com.gisnet.erpp.domain.TipoFolio;


public class AntecedenteVO {    
    private Long id;
    private String documento;
    private String numero;
    private String documentoBis;
    private String observaciones;
    private String libro;
    private String libroBis;
    private String seccion;
    private String oficina;
    private Integer anio;
    private String volumen;
private Boolean tipoDoc;
    private Long   prelacionAnte;
    private String tipoBusqueda;
    private Long prelacionId;
    private TipoFolio tipoFolio;
    private String nombreLibro;
    
    public Long getId(){
        return this.id;
    }


    public void setId(Long id){
      this.id=id;
    }



    public String getDocumento(){
        return this.documento;
    }


    public void setDocumento(String documento){
      this.documento=documento;
    }


     public String getDocumentoBis(){
        return this.documentoBis;
    }


    public void setDocumentoBis(String documentoBis){
      this.documentoBis=documentoBis;
    }

     public String getObservaciones(){
        return this.observaciones;
    }


    public void setObservaciones(String observaciones){
      this.observaciones=observaciones;
    }


     public String getLibro(){
        return this.libro;
    }


    public void setLibro(String libro){
      this.libro=libro;
    }




     public String getLibroBis(){
        return this.libroBis;
    }


    public void setLibroBis(String libroBis){
      this.libroBis=libroBis;
    }


    public String getSeccion(){
        return this.seccion;
    }


    public void setSeccion(String seccion){
      this.seccion=seccion;
    }


     public String getOficina(){
        return this.oficina;
    }


    public void setOficina(String oficina){
      this.oficina=oficina;
    }


    public Boolean isTipoDoc() {
		return tipoDoc;
	}


	public void setTipoDoc(Boolean tipoDoc) {
		this.tipoDoc = tipoDoc;
	}


	public Long getPrelacionAnte(){
        return this.prelacionAnte;
    }


    public void setPrelacionAnte(Long prelacionAnte){
      this.prelacionAnte=prelacionAnte;
    }


    public static AntecedenteVO antecedenteTransform(Antecedente  ante ){
          
          AntecedenteVO anteVO= null;
          if(ante!=null){
                anteVO= new AntecedenteVO(); 
                anteVO.setDocumento(ante.getDocumento());
                anteVO.setDocumentoBis(ante.getDocumentoBis());
                anteVO.setObservaciones(ante.getObservaciones());
                anteVO.setId(ante.getId());
                if(ante.getLibro()!=null)  {
                	anteVO.setLibro(ante.getLibro().getNumLibro()) ;
                	anteVO.setTipoDoc(ante.getLibro().getTipoDoc());
                	anteVO.setLibroBis(ante.getLibro().getLibroBis()) ;
                	anteVO.setOficina(ante.getLibro().getSeccionesPorOficina().getOficina().getNombre()  ) ;
                    anteVO.setSeccion(ante.getLibro().getSeccionesPorOficina().getSeccion().getNombre() ) ;
                    anteVO.setAnio(ante.getLibro().getAnio());
                    anteVO.setVolumen(ante.getLibro().getVolumen());
                }
               
          }
          

          return anteVO;
    }
    
    


	public Integer getAnio() {
		return anio;
	}


	public void setAnio(Integer anio) {
		this.anio = anio;
	}


	public String getTipoBusqueda() {
		return tipoBusqueda;
	}


	public void setTipoBusqueda(String tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}


	public Long getPrelacionId() {
		return prelacionId;
	}


	public void setPrelacionId(Long prelacionId) {
		this.prelacionId = prelacionId;
	}


	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}


	public TipoFolio getTipoFolio() {
		return tipoFolio;
	}


	public void setTipoFolio(TipoFolio tipoFolio) {
		this.tipoFolio = tipoFolio;
	}


	public String getNombreLibro() {
		return nombreLibro;
	}


	public void setNombreLibro(String nombreLibro) {
		this.nombreLibro = nombreLibro;
	}


	public String getVolumen() {
		return volumen;
	}


	public void setVolumen(String volumen) {
		this.volumen = volumen;
	}
	
	

}
