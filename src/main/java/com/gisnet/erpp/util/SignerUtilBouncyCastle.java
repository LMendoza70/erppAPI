package com.gisnet.erpp.util;

import java.util.Base64;
import java.util.Collection;
import java.util.Iterator;

import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;

public class SignerUtilBouncyCastle {
	public static String getEncryptedDigest(String pkcs7){
		byte[] result = getEncryptedDigest(Base64.getDecoder().decode(pkcs7));
		return new String(Base64.getEncoder().encode(result));
	}

	public static byte[] getEncryptedDigest(byte[] pkcs7) {
		CMSSignedData signature = null;
		try {
			signature = new CMSSignedData(pkcs7);
		} catch (CMSException e) {
			throw new IllegalArgumentException("No se pudo construir un CMS del pkcs7");
		}
		SignerInformationStore signers = signature.getSignerInfos();
		Collection<SignerInformation> c = signers.getSigners();
		Iterator<SignerInformation> it = c.iterator();

		byte[] data = null;

		while (it.hasNext()) {
			SignerInformation signer = (SignerInformation) it.next();
			data = signer.getSignature();
		}

		return data;
	}

}
