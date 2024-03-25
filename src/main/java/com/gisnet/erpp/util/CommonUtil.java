package com.gisnet.erpp.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;

import org.apache.commons.lang3.StringUtils;

import com.lowagie.text.Rectangle;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.PageSize;

public class CommonUtil {
	public static String padCeros(Integer cad, int size){
		return padCeros(String.valueOf(cad), size);
	}

	public static String padCeros(String cad, int size){
		if (cad!=null){
			return StringUtils.leftPad(cad, size, '0');
		} else {
			return StringUtils.leftPad("", size, '0');
		}
	}
	
	public static String generarBisDocumento(String documento,String bis){
		String documentoBis = CommonUtil.padCeros(documento, Constantes.SIZE_DOCUMENTO);
		if(bis != null && !bis.isEmpty()){
			documentoBis = documentoBis+bis;
		}else{
			documentoBis = documentoBis +"0";
		}
		
		return documentoBis;
	}
	
	public static List<String> generarListTipoLibro(String tipoLibro){
		List<String> tiposLibro = new ArrayList<>();
		int index = 0;
		if(tipoLibro != null && !tipoLibro.isEmpty()){
			while(index < tipoLibro.length()){
				tiposLibro.add(String.valueOf(tipoLibro.charAt(index)));
				index++;
			}
		}
		return tiposLibro;
	}
	
	public static boolean isInRange(String pagina,Integer desde,Integer hasta){
		boolean in = false;
		String numer = null;
		int numPagina = 0;
		if(desde == null && hasta ==null){
			in = true;
		}else {
			if(pagina.indexOf(".") > 0){
				numer = pagina.substring(0, pagina.indexOf("."));
				if(numer.indexOf("-") < 0){
					numPagina = Integer.parseInt(numer);
				}else{
					numer = numer.substring(0, pagina.indexOf("-"));
					numPagina = Integer.parseInt(numer);
				}
				
				if(desde != null && hasta ==null){
					if(numPagina >= desde.intValue()){
						in = true;
					}
				}else if(desde == null && hasta !=null){
					if(numPagina <= hasta.intValue()){
						in = true;
					}					
				}else{
					if(numPagina >= desde.intValue() && numPagina <= hasta.intValue()){
						in = true;
					}
				}
			}	
		}
		
		return in;
	}
	
	public static boolean isImageExtension(String imageName){		
		boolean isImage = false;
		if(imageName != null && imageName.length() > 0){
			imageName = imageName.toUpperCase();
				for (int i = 0; i< Constantes.SUPPORT_TYPES.length; i++){
					if (imageName.endsWith(Constantes.SUPPORT_TYPES[i])){
						isImage = true;
						break;
				}				
			}
		}
		return isImage;
	}

	/**
     * Obtiene el total de hojas
     * 
     * @param Input stream
     * 
     * 
     */
    public static int getNumeroDeHojas(InputStream in) {
    	int num=0;
    	PdfReader reader;
		try {
			reader = new PdfReader(in);
			num=reader.getNumberOfPages();
		} catch (IOException e) {			
			e.printStackTrace();
		}    	
    	return num;    	
	}
	
	public static byte[] doExtraeToByteArray(InputStream inputStream,String hojas_seleccionadas,Boolean tipoDoc)
            throws DocumentException, IOException {
    	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    	String hojas[] = hojas_seleccionadas.split(",");
    	
		//Document document = new Document(PageSize.LEGAL,new Float(0.5),new Float(0.5),new Float(0.5),new Float(0.5));
		Rectangle pageSize = new Rectangle(PageSize.LEGAL.getWidth(), PageSize.LEGAL.getHeight()); //ancho y alto
        Document document = new Document(pageSize);

        PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();
        PdfContentByte cb = writer.getDirectContent();
        
        PdfReader reader = new PdfReader(inputStream);
        
        int totalPaginas = reader.getNumberOfPages();
        System.out.println("Numero Total de Paginas:"+totalPaginas);
        int hoja_seleccionada=0;
        for(int i=0;i<hojas.length;i++) {
        	hoja_seleccionada=Integer.valueOf(hojas[i]);
            if(hoja_seleccionada>0 && hoja_seleccionada<=totalPaginas) {            
                document.newPage();
                //import the page from source pdf
                PdfImportedPage page = writer.getImportedPage(reader, hoja_seleccionada);                
				//add the page to the destination pdf
				
				if(tipoDoc == null || tipoDoc == false) {
					//cb.addTemplate(page,0.9F,0,0,0.9F,0,50);  //Configuracion de Copias de Mayor Tamaño					
					cb.addTemplate(page,1,1);  //Configuracion de Copias de Mayor Tamaño CORRECTO
					//cb.addTemplate(page,5,-200);  //Configuracion de Copias de Mayor Tamaño 
					//cb.addTemplate(page, -0.5f, 0f, 0f, -0.5f, PageSize.LEGAL.getWidth() / 2, PageSize.LEGAL.getHeight());
				} else { 
					cb.addTemplate(page,1.99F,0,0,1.99F,0,70);  //Configuracion de Copias de Menor Tamaño
				}
            }
        }        
        
        document.close();        
        return byteArrayOutputStream.toByteArray();
	}
	
	public static String decodeValue(String value) {
        try {
        	if(value!=null && !value.isEmpty())
               return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        	else 
        		return value;
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
    
    public static String encodeValue(String value) {
        try {
        	if(value!=null && !value.isEmpty())
               return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        	else 
        		return value;
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
    
    public static String formatMoney(String valor) {
    	StringBuilder result =  new StringBuilder();
    	try 
    	{
    		if(valor!=null  && !valor.isEmpty()) {
    			String[] split =  valor.split("\\.");
    			String entero =  split[0];
    			String decimal = split.length>1 ? split[1] : "";
    			entero = entero.trim();
    			entero = entero.replace(",","");
    			entero = entero.replace("$","");
    			NumberFormat format =NumberFormat.getCurrencyInstance(new Locale("es","MX"));
    			format.setMaximumFractionDigits(0);
    			entero =  format.format(new Double(entero));
    			result.append(entero);
    			if(!decimal.isEmpty())
    				 result.append(".").append(decimal);
    			
    		}
    		
    	}catch(Exception e){
    		 
    	}
    	return result.toString();
    }
    
    public static String formatDecimal(String valor) {
    	StringBuilder result =  new StringBuilder();
    	try 
    	{
    		if(valor!=null  && !valor.isEmpty()) {
    			String[] split =  valor.split("\\.");
    			String entero =  split[0];
    			String decimal = split.length>1 ? split[1] : "";
    			entero = entero.trim();
    			entero = entero.replace(",","");
    			entero = entero.replace("$","");
    			NumberFormat format =NumberFormat.getCurrencyInstance(new Locale("es","MX"));
    			format.setMaximumFractionDigits(0);
    			entero =  format.format(new Double(entero));
    			result.append(entero.replace("$",""));
    			if(!decimal.isEmpty())
    				 result.append(".").append(decimal);
    			
    		}
    		
    	}catch(Exception e){
    		 
    	}
    	return result.toString();
    }
}
