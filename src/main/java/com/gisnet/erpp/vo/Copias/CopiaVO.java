package com.gisnet.erpp.vo.Copias;

import com.gisnet.erpp.domain.*;

import java.util.List;

public class CopiaVO {
    private Long           id;
    private Integer        folioReal;
    private TipoFolio      tipoFolio;
    private Oficina        oficina;
    private List<TipoActo> actos;
    private String         escritura;
    private FoliosrDigital foliosrDigital;

    private String libro;
    private String  libroBis;
    private Boolean esDocumento;
    private String  noDocumento;
    private String  documentoBis;
    private Seccion seccion;


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

    public FoliosrDigital getFoliosrDigital() {
        return foliosrDigital;
    }

    public void setFoliosrDigital(FoliosrDigital foliosrDigital) {
        this.foliosrDigital = foliosrDigital;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public String getLibroBis() {
        return libroBis;
    }

    public void setLibroBis(String libroBis) {
        this.libroBis = libroBis;
    }

    public String getNoDocumento() {
        return noDocumento;
    }

    public void setNoDocumento(String noDocumento) {
        this.noDocumento = noDocumento;
    }

    public String getDocumentoBis() {
        return documentoBis;
    }

    public void setDocumentoBis(String documentoBis) {
        this.documentoBis = documentoBis;
    }

    public Seccion getSeccion() {
        return seccion;
    }

    public void setSeccion(Seccion seccion) {
        this.seccion = seccion;
    }

    public Boolean getEsDocumento() {
        return esDocumento;
    }

    public void setEsDocumento(Boolean esDocumento) {
        this.esDocumento = esDocumento;
    }
}
