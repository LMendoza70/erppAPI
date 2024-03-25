package com.gisnet.erpp.vo.prelacion;


public class ModificarPrelacionVO {
    Long prelacionId;
    Long marcaId;
    Long causaId;

    Long registradorId;

    public Long getPrelacionId() {
        return prelacionId;
    }

    public void setPrelacionId(Long prelacionId) {
        this.prelacionId = prelacionId;
    }

    public Long getMarcaId() {
        return marcaId;
    }

    public void setMarcaId(Long marcaId) {
        this.marcaId = marcaId;
    }

    public Long getCausaId() {
        return causaId;
    }

    public void setCausaId(Long causaId) {
        this.causaId = causaId;
    }

    public Long getRegistradorId() {
        return registradorId;
    }

    public void setRegistradorId(Long registradorId) {
        this.registradorId = registradorId;
    }
}