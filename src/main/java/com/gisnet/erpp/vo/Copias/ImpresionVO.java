package com.gisnet.erpp.vo.Copias;

import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.TipoActo;
import com.gisnet.erpp.domain.TipoFolio;

import java.util.List;

public class ImpresionVO {
    private Long           id;
    private Integer        folioReal;
    private TipoFolio      tipoFolio;
    private Oficina        oficina;
    private List<TipoActo> actos;
    private String         escritura;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFolioReal() {
        return folioReal;
    }

    public void setFolioReal(Integer folioReal) {
        this.folioReal = folioReal;
    }

    public TipoFolio getTipoFolio() {
        return tipoFolio;
    }

    public void setTipoFolio(TipoFolio tipoFolio) {
        this.tipoFolio = tipoFolio;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    public List<TipoActo> getActos() {
        return actos;
    }

    public void setActos(List<TipoActo> actos) {
        this.actos = actos;
    }

    public String getEscritura() {
        return escritura;
    }

    public void setEscritura(String escritura) {
        this.escritura = escritura;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
