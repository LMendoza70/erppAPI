package com.gisnet.erpp.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import sun.misc.BASE64Encoder;
import sun.security.pkcs.ContentInfo;
import sun.security.pkcs.PKCS7;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.AccessDescription;
import sun.security.x509.GeneralName;
import sun.security.x509.GeneralNameInterface;
import sun.security.x509.URIName;

public class SignerUtil {
    private static final String SUBJECT_INFO_ACCESS_OID = "1.3.6.1.5.5.7.1.11";

    /*
     * Object identifier for the timestamping access descriptors.
     */
    private static final ObjectIdentifier AD_TIMESTAMPING_Id;
    static {
        ObjectIdentifier tmp = null;
        try {
            tmp = new ObjectIdentifier("1.3.6.1.5.5.7.48.3");
        } catch (IOException e) {
            // ignore
        }
        AD_TIMESTAMPING_Id = tmp;
    }

    public static String[] generatePkcs7SignedData(byte[] p12, String password, String cadenaoriginal) throws Exception{
    	KeyStore clientStore = KeyStore.getInstance("PKCS12");
        clientStore.load(new ByteArrayInputStream(p12), password.toCharArray());

        Enumeration<String> aliases = clientStore.aliases();
        String aliaz = "";
        while(aliases.hasMoreElements()){
            aliaz = aliases.nextElement();
            if(clientStore.isKeyEntry(aliaz)){
                break;
            }
        }
        X509Certificate certificate = (X509Certificate)clientStore.getCertificate(aliaz);

        //Data to sign
        byte[] dataToSign = cadenaoriginal.getBytes();
        //compute signature:
        Signature signature = Signature.getInstance("Sha256WithRSA");
        PrivateKey key = (PrivateKey)clientStore.getKey(aliaz, password.toCharArray());
        signature.initSign(key);
        signature.update(dataToSign);
        byte[] signedData = signature.sign();

        ContentInfo contentInfo = new ContentInfo(ContentInfo.DATA_OID, new DerValue(DerValue.tag_OctetString, dataToSign));

        byte[] pkcs7 = PKCS7.generateSignedData(signedData,new java.security.cert.X509Certificate[] { certificate }, null, "Sha256WithRSA", getTimestampingURI(certificate), "", "");

        return new String[]{ new String(new BASE64Encoder().encode(signedData)).replaceAll("(?:\\r\\n|\\n\\r|\\n|\\r)", ""), new String(new BASE64Encoder().encode(pkcs7)).replaceAll("(?:\\r\\n|\\n\\r|\\n|\\r)", "")};
    }


    private static URI getTimestampingURI(X509Certificate tsaCertificate) {

        if (tsaCertificate == null) {
            return null;
        }
        // Parse the extensions
        try {
            byte[] extensionValue =
                tsaCertificate.getExtensionValue(SUBJECT_INFO_ACCESS_OID);
            if (extensionValue == null) {
                return null;
            }
            DerInputStream der = new DerInputStream(extensionValue);
            der = new DerInputStream(der.getOctetString());
            DerValue[] derValue = der.getSequence(5);
            AccessDescription description;
            GeneralName location;
            URIName uri;
            for (int i = 0; i < derValue.length; i++) {
                description = new AccessDescription(derValue[i]);
                if (description.getAccessMethod()
                        .equals((Object)AD_TIMESTAMPING_Id)) {
                    location = description.getAccessLocation();
                    if (location.getType() == GeneralNameInterface.NAME_URI) {
                        uri = (URIName) location.getName();
                        if (uri.getScheme().equalsIgnoreCase("http") ||
                                uri.getScheme().equalsIgnoreCase("https")) {
                            return uri.getURI();
                        }
                    }
                }
            }
        } catch (IOException ioe) {

        }
        return null;
    }



}
