package com.gisnet.erpp.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.ErppApplication;
import com.gisnet.erpp.web.api.firma.FirmaDTO;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ErppApplication.class)
@Transactional
public class FirmaServiceTest {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
		
	private String x="MIIFTwYJKoZIhvcNAQcCoIIFQDCCBTwCAQExCzAJBgUrDgMCGgUAMBgGCSqGSIb3DQEHAaALBAlIT0xBICEgISGgggPQMIIDzDCCArSgAwIBAgIUMDAwMDAyMDAwMDAyMDAwMDI2MzEwDQYJKoZIhvcNAQEFBQAwfTELMAkGA1UEBhMCTVgxIjAgBgNVBAMMGUFDX0RFU0FSUk9MTE9fU1VCT1JESU5BREExITAfBgNVBAsTGERJUkVDQ0lPTiBERSBJTkZPUk1BVElDQTEnMCUGA1UEChMeR09CSUVSTk8gREVMIEVTVEFETyBERSBKQUxJU0NPMB4XDTE3MDUwNDAwMDAwMFoXDTE5MDUwMTAwMDAwMFowga0xCzAJBgNVBAYTAk1YMRcwFQYDVQQDEw5QcnVlYmEgUlBQQyAwNDE6MDgGA1UECxMxUmVnaXN0cm8gUHVibGljbyBkZSBsYSBQcm9waWVkYWQgeSBDb21lcmNpbyAtIFNHRzEnMCUGA1UEChMeR29iaWVybm8gZGVsIEVzdGFkbyBkZSBKYWxpc2NvMSAwHgYJKoZIhvcNAQkBFhFwcnVlYmFAamFsaXNjby5teDCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAr27f74TlZKYEwsVJ8hU2/pDBbso7LYShgUTySH6/awUPAEre9I3UoW1K3+qaBZ9dZ2tIAtcgfgxCLoIGac/65bpN/uYPdQ6seUlqt8k0KV/ltbFpuYEVGtQ6mIIhpr19Gw/plFAULWNd6IoYDIjzxHmzXvMgAwR4JyhURvgANZsCAwEAAaOBljCBkzAfBgNVHSMEGDAWgBRv/+LY9V0IvEv7P0JFDo4v0soLETAdBgNVHQ4EFgQUj9hx+UqQ3pGpVAwt4cNcP3czoK4wHQYDVR0lBBYwFAYIKwYBBQUHAwQGCCsGAQUFBwMCMA8GA1UdEwEB/wQFMAMCAQAwDgYDVR0PAQH/BAQDAgPoMBEGCWCGSAGG+EIBAQQEAwIFoDANBgkqhkiG9w0BAQUFAAOCAQEAZl2V0o6U59MlKSDn9FcTNyr5Q1QamhThgrSfZMFbualSuE4vvUtZ5LRgpSPAdu1yhEEHi5HSQxAN2ve3eMiOg5D9yBwFuvxI9UVJxFnTrorVVEAxIGBMX1hoUbUmtpggAPMXECTrQFnUFmiSY9s5Kt1xnjLbhD+jDOuXBUrEybXmt0hysC8YyAinuEGdsXsipCUbeI+xoYw4pU2Po52ebA9B447Dluo3mTYTKogZNJr4gpu5hkeAgbkUfszYkx3Vr0tJyKbdxj74WvR4wz4Jyy8fy60L7LT85UiU8PuA0GIbMCFe4rJwODHktqgCIvxYHw4Nj0CiUhL5VC9redwDkTGCATowggE2AgEBMIGVMH0xCzAJBgNVBAYTAk1YMSIwIAYDVQQDDBlBQ19ERVNBUlJPTExPX1NVQk9SRElOQURBMSEwHwYDVQQLExhESVJFQ0NJT04gREUgSU5GT1JNQVRJQ0ExJzAlBgNVBAoTHkdPQklFUk5PIERFTCBFU1RBRE8gREUgSkFMSVNDTwIUMDAwMDAyMDAwMDAyMDAwMDI2MzEwCQYFKw4DAhoFADALBgkqhkiG9w0BAQEEgYAvRsnhFmtgnDv1TYHsrNhzBJKSo6iR8eAPD4w9IakkBD1VqHDDyFnSiUsNKyPYfgtaC9HwhvapeK6s3pL6YWbAoIquQtmF7YsF6jRpMkpn60PePyW1cxgKfZ3C1mdLYtVipxzCdanefTgUavbRlwZ2cPKyeHdmRegLU5OVRVd7Vw==";
	private String estampa="8493|Politica Estampa Tiempo=1.3.6.1.4.1.9203.2.1, Digestion Estampa Tiempo=B60897BD18C508EF99D49E2C0C247744D83A864E, Numero Secuencia Estampa Tiempo=8008, Fecha Emision Estampa Tiempo=20170601152413Z";
	
	 @Autowired 
	 FirmaService firmaService;
	 
	 @Test
	 public void firma() {
		 
		/*String result = firmaService.registrarFirmaEnServicioEstatal(x);
		 
		 log.debug("result="+result);
		 
         FirmaDTO firma = firmaService.parseEstampa(result);

         log.debug("firma="+firma);
         
         String original = firmaService.obtenCadenaOriginal(firma.getSecuencia());
         
         log.debug(original);
         
         assertEquals("HOLA ! !!", original);  */
	 }
	 
	 

}
