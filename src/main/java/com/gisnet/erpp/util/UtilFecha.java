package com.gisnet.erpp.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;


public class UtilFecha {
	public static Date addDays(Date date, int days){
		DateTime dateTime = new DateTime(date);
		dateTime = dateTime.plusDays(days);
		return dateTime.toDate();
	}

    public static String formatToDatePattern(java.util.Date date) {
        String resultado = null;

        try {
            SimpleDateFormat simpledateformat = new SimpleDateFormat(Constantes.DATE_PATTERN);
            resultado = simpledateformat.format(date);
        } catch (Exception _ex) {
            resultado = "";
        }

        return resultado;
    }
    
    public static String formatToDateTimePattern(java.util.Date date) {
        String resultado = null;

        try {
            SimpleDateFormat simpledateformat = new SimpleDateFormat(Constantes.DATETIME_PATTERN);
            resultado = simpledateformat.format(date);
        } catch (Exception _ex) {
            resultado = "";
        }

        return resultado;
    }
    
    public static Date toDate(String s, String formato){
   	 Date fecha = null;
   	 DateFormat format = new SimpleDateFormat(formato);
   	 try {
			fecha= format.parse(s);
		} catch (ParseException e) {
		}
   	 
   	 return fecha;
   	 
   }

        
    public static Date toDate(String s){
    	 return toDate(s, Constantes.DATE_PATTERN);    	 
    }

    public static Date toDateTime(String s){
   	 return toDateTime(s, Constantes.DATETIME_PATTERN);
   	 
   }
    
   public static Date toDateTime(String s, String formato){
      	 Date fecha = null;
      	 DateFormat format = new SimpleDateFormat(formato);
      	 try {
   			fecha= format.parse(s);
   		} catch (ParseException e) {
   		}
      	 
      	 return fecha;
      	 
      }
   
   public static String getTimeStamp() {
   	return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
   }

    
	public static Date getEndOfDay(Date date) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    calendar.set(Calendar.MILLISECOND, 999);
	    return calendar.getTime();
	}
	
	public static Date getStartOfDay(Date date) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    return calendar.getTime();
	}

	
	public static int getYear(Date fecha){
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(fecha);
	    return cal.get(Calendar.YEAR);
	}
	
	
	public static int getCurrentYear(){
		return Calendar.getInstance().get(Calendar.YEAR);
	}
	
	public static Date minusMonths(Calendar fecha){
		DateTime now = new DateTime(fecha);
		return  now.minusMonths(1).toDate();
	}
	
    
	public static Date getCurrentDate(){
		return Calendar.getInstance().getTime();
	}
	
	public static int diasHabiles(Date _fechaInicial, Date _fechaFinal, List<Date> listaFechasNoLaborables) {
	       int diffDays = 0;
	       boolean diaHabil = false;
	       
	       Calendar fechaInicial = Calendar.getInstance();
	       fechaInicial.setTime(_fechaInicial);
	       
	       Calendar fechaFinal = Calendar.getInstance();
	       fechaFinal.setTime(_fechaFinal);
	       
	       //mientras la fecha inicial sea menor o igual que la fecha final se cuentan los dias
	       while (fechaInicial.before(fechaFinal) || fechaInicial.equals(fechaFinal)) {

	          if (!listaFechasNoLaborables.isEmpty()) {
	              for (Date date : listaFechasNoLaborables) {
	                  Date fechaNoLaborablecalendar = fechaInicial.getTime();
	                  //si el dia de la semana de la fecha minima es diferente de sabado o domingo
	                  if (fechaInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && fechaInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && !fechaNoLaborablecalendar.equals(date)) {
	                      //se aumentan los dias de diferencia entre min y max
	                      diaHabil = true;
	                  } else {
	                      diaHabil = false;
	                      break;
	                  }
	              }
	          } else {
	              if (fechaInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && fechaInicial.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
	                  //se aumentan los dias de diferencia entre min y max
	                  diffDays++;
	              }
	          }
	          if (diaHabil == true) {
	          diffDays++;
	          }
	          //se suma 1 dia para hacer la validacion del siguiente dia.
	          fechaInicial.add(Calendar.DATE, 1);
	     }
	     return diffDays;
	  }
	
	public static String monthNameOrIdtoNumerMonth(String mes) {
		
		String mesNumero =  "01";
		
		switch (mes) {
		case "2113":
		case "ENERO":
			mesNumero =  "01";
			break;
		case "2114":
		case "FEBRERO":
			mesNumero =  "02";
			break;
		case "2115":
		case "MARZO":
			mesNumero =  "03";
			break;
		case "2116":
		case "ABRIL":
			mesNumero =  "04";
			break;
		case "2117":
		case "MAYO":
			mesNumero =  "05";
			break;
		case "2118":
		case "JUNIO":
			mesNumero =  "06";
			break;
		case "2119":
		case "JULIO":
			mesNumero =  "07";
			break;
		case "2120":
		case "AGOSTO":
			mesNumero =  "08";
			break;
		case "2121":
		case "SEPTIEMBRE":
			mesNumero =  "09";
			break;
		case "2122":
		case "OCTUBRE":
			mesNumero =  "10";
			break;
		case "2123":
		case "NOVIEMBRE":
			mesNumero =  "11";
			break;
		case "2124":
		case "DICIEMBRE":
			mesNumero =  "12";
			break;

		}

		return mesNumero;
	}
	
	public static Boolean isDiaHabil(Date fecha) {
		
		 Boolean diaHabil = false; 
		
	       Calendar fechaInicial = Calendar.getInstance();
	       fechaInicial.setTime(fecha);
	
	       
	       if (fechaInicial.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY 
	    		|| fechaInicial.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ) {
              diaHabil = false;
          } else {
              diaHabil = true;
          }
	       
	       
	       System.out.println("DIA PORTAL VALIDO? --> " +  diaHabil );	       
	       
		return diaHabil;
	}
	
}
