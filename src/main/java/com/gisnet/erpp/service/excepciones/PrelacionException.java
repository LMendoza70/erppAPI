package com.gisnet.erpp.service.excepciones;


import com.gisnet.erpp.domain.Prelacion;

public class PrelacionException extends RuntimeException {
	private String    message;
	private Prelacion prelacion;


        public PrelacionException(String mensaje, Prelacion prelacion){
            this.message=mensaje;
            this.prelacion=prelacion;
        }


    public String getMessage(){
        return this.message;
    }

    public void setMessage(String mensaje){
        this.message=mensaje;
    }

    public Prelacion getPrelacion(){
        return this.prelacion;
    }

    public void setPrelacion(Prelacion prelacion){
        this.prelacion=prelacion;
    }

        

}
