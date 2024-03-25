package com.gisnet.erpp.vo;

import com.gisnet.erpp.domain.*;

import java.lang.reflect.Field;
import java.util.HashMap;

public class BusquedaPredioVO {
    private Long id;
    private Long busquedaId;

    private TipoVialidad tipoVialidad;
    private String       vialidad;
    private String       numExt;
    private String       numInt;
    private String       edificio;
    private Nivel        nivel;
    private TipoAsent    tipoAsent;
    private String       asentamiento;
    private Estado       estado;
    private Municipio    municipio;
    private String       codigoPostal;
    private String       claveCatastral;
    private String       cuentaCatastral;
    private String       claveCatastralEstandard;
    private String       noLote;
    private String       localidadSector;
    private String       manzana;

    private String       oficina;
    private String       zona;
    private String       superficie;
    private UnidadMedida unidadMedida;
    private UsoSuelo     usoSuelo;
    private TipoInmueble tipoInmueble;
    private String       fraccion;
    private String       macroManzana;
    private TipoVialidad tipoVialidad2;
    private String       vialidad2;
    private String       numExt2;

    private EnlaceVialidad enlaceVialidad;
    private FracOCondo fracOCondo;
    private String nombreFracOCondo;
    private EtapaOSeccion etapaOSeccion;

    private Municipio varianteMunicipio;
    private TipoAsent varianteTipoAsent;

    private HashMap<String, String> linderos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusquedaId() {
        return busquedaId;
    }

    public void setBusquedaId(Long busquedaId) {
        this.busquedaId = busquedaId;
    }

    public TipoVialidad getTipoVialidad() {
        return tipoVialidad;
    }

    public void setTipoVialidad(TipoVialidad tipoVialidad) {
        this.tipoVialidad = tipoVialidad;
    }

    public String getVialidad() {
        return vialidad;
    }

    public void setVialidad(String vialidad) {
        this.vialidad = vialidad;
    }

    public String getNumExt() {
        return numExt;
    }

    public void setNumExt(String numExt) {
        this.numExt = numExt;
    }

    public String getNumInt() {
        return numInt;
    }

    public void setNumInt(String numInt) {
        this.numInt = numInt;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public TipoAsent getTipoAsent() {
        return tipoAsent;
    }

    public void setTipoAsent(TipoAsent tipoAsent) {
        this.tipoAsent = tipoAsent;
    }

    public String getAsentamiento() {
        return asentamiento;
    }

    public void setAsentamiento(String asentamiento) {
        this.asentamiento = asentamiento;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getClaveCatastral() {
        return claveCatastral;
    }

    public void setClaveCatastral(String claveCatastral) {
        this.claveCatastral = claveCatastral;
    }

    public String getCuentaCatastral() {
        return cuentaCatastral;
    }

    public void setCuentaCatastral(String cuentaCatastral) {
        this.cuentaCatastral = cuentaCatastral;
    }

    public String getClaveCatastralEstandard() {
        return claveCatastralEstandard;
    }

    public void setClaveCatastralEstandard(String claveCatastralEstandard) {
        this.claveCatastralEstandard = claveCatastralEstandard;
    }

    public String getNoLote() {
        return noLote;
    }

    public void setNoLote(String noLote) {
        this.noLote = noLote;
    }

    public String getManzana() {
        return manzana;
    }

    public void setManzana(String manzana) {
        this.manzana = manzana;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getSuperficie() {
        return superficie;
    }

    public void setSuperficie(String superficie) {
        this.superficie = superficie;
    }

    public UnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(UnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public UsoSuelo getUsoSuelo() {
        return usoSuelo;
    }

    public void setUsoSuelo(UsoSuelo usoSuelo) {
        this.usoSuelo = usoSuelo;
    }

    public TipoInmueble getTipoInmueble() {
        return tipoInmueble;
    }

    public void setTipoInmueble(TipoInmueble tipoInmueble) {
        this.tipoInmueble = tipoInmueble;
    }


    public TipoVialidad getTipoVialidad2() {
        return tipoVialidad2;
    }

    public void setTipoVialidad2(TipoVialidad tipoVialidad2) {
        this.tipoVialidad2 = tipoVialidad2;
    }

    public String getVialidad2() {
        return vialidad2;
    }

    public void setVialidad2(String vialidad2) {
        this.vialidad2 = vialidad2;
    }

    public String getNumExt2() {
        return numExt2;
    }

    public void setNumExt2(String numExt2) {
        this.numExt2 = numExt2;
    }

    public EnlaceVialidad getEnlaceVialidad() {
        return enlaceVialidad;
    }

    public void setEnlaceVialidad(EnlaceVialidad enlaceVialidad) {
        this.enlaceVialidad = enlaceVialidad;
    }

    public FracOCondo getFracOCondo() {
        return fracOCondo;
    }

    public void setFracOCondo(FracOCondo fracOCondo) {
        this.fracOCondo = fracOCondo;
    }

    public String getNombreFracOCondo() {
        return nombreFracOCondo;
    }

    public void setNombreFracOCondo(String nombreFracOCondo) {
        this.nombreFracOCondo = nombreFracOCondo;
    }

    public EtapaOSeccion getEtapaOSeccion() {
        return etapaOSeccion;
    }

    public void setEtapaOSeccion(EtapaOSeccion etapaOSeccion) {
        this.etapaOSeccion = etapaOSeccion;
    }

    public String getFraccion() {
        return fraccion;
    }

    public void setFraccion(String fraccion) {
        this.fraccion = fraccion;
    }

    public String getMacroManzana() {
        return macroManzana;
    }

    public void setMacroManzana(String macroManzana) {
        this.macroManzana = macroManzana;
    }

    public String getLocalidadSector() {
        return localidadSector;
    }

    public void setLocalidadSector(String localidadSector) {
        this.localidadSector = localidadSector;
    }

    public Municipio getVarianteMunicipio() {
        return varianteMunicipio;
    }

    public void setVarianteMunicipio(Municipio varianteMunicipio) {
        this.varianteMunicipio = varianteMunicipio;
    }

    public TipoAsent getVarianteTipoAsent() {
        return varianteTipoAsent;
    }

    public void setVarianteTipoAsent(TipoAsent varianteTipoAsent) {
        this.varianteTipoAsent = varianteTipoAsent;
    }

    public HashMap<String, String> getLinderos() {
        return linderos;
    }

    public void setLinderos(HashMap<String, String> linderos) {
        this.linderos = linderos;
    }

    public String getOficina() {
        return oficina;
    }

    public void setOficina(String oficina) {
        this.oficina = oficina;
    }
    
    public boolean isNull() {
        Field fields[] = this.getClass().getDeclaredFields();
        for (Field f : fields) {
            try {
                Object value = f.get(this);
                if (value != null) {
                    return false;
                }
            }
            catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }
        return true;

    }
}
