package com.gisnet.erpp.service.excepciones;

public class RequerimientoException extends Exception {
	private String    message;


        public RequerimientoException(String mensaje){
            this.message=mensaje;
        }


    public String getMessage(){
        return this.message;
    }

    public void setMessage(String mensaje){
        this.message=mensaje;
    }


}
