package com.gisnet.erpp.vo;

import com.gisnet.erpp.domain.StatusActo;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.TipoActo;

public class ActoVO {
	
	private Long       id;
	private Long       prelacion;
	private String     hashFila;
	private Integer    orden;
	private Boolean    precesado;
	private ActoVO     actoPedioEntrada;
    private StatusActo statusActo;
	private TipoActo   tipoActo;
	
	

	public ActoVO() {}
	public ActoVO(Long actoId) {
		this.id= actoId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Long getPrelacion() {
		return prelacion;
	}
	public void setPrelacion(Long prelacion) {
		this.prelacion = prelacion;
	}
	public String getHashFila() {
		return hashFila;
	}
	public void setHashFila(String hashFila) {
		this.hashFila = hashFila;
	}
	public Integer getOrden() {
		return orden;
	}
	public void setOrden(Integer orden) {
		this.orden = orden;
	}
	public Boolean getPrecesado() {
		return precesado;
	}
	public void setPrecesado(Boolean precesado) {
		this.precesado = precesado;
	}
	
	public ActoVO getActoPredioEntrada() {
		return actoPedioEntrada;
	}
	public void setActoPredioEntrada(ActoVO actoPedioEntrada) {
		this.actoPedioEntrada = actoPedioEntrada;
	}

	public StatusActo getStatusActo() {
		return statusActo;
	}
	public void setStatusActo(StatusActo status) {
		this.statusActo = status;
	}


	public TipoActo getTipoActo() {
		return tipoActo;
	}

	public void setTipoActo(TipoActo tipoActo) {
		this.tipoActo = tipoActo;
	}



	 public static ActoVO actoTransform(Acto  acto ){
          
          ActoVO actoVO= null;
          if(acto!=null){
                actoVO= new ActoVO(); 
				actoVO.setId(acto.getId());
                actoVO.setPrelacion(acto.getPrelacion().getId());
                actoVO.setHashFila(acto.getHashFila());
                actoVO.setOrden(acto.getOrden());
				//if(actoVO.getActoPredioEntrada()!=null)
					//TODO: remplazar actopredioentrada
                	//actoVO.setActoPredioEntrada(ActoVO.actoTransform(acto.getActoPredioEntrada()));

                if(actoVO.getStatusActo()!=null)  actoVO.setStatusActo(acto.getStatusActo()) ;
                if(actoVO.getTipoActo()!=null)  actoVO.setTipoActo(acto.getTipoActo()) ;
                
          }
          

          return actoVO;
    }


	

}
