package de.hm.edu.carads.models.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;

/**
 * Diese Klasse dient lediglich zur Erstellung eines Hashwerts. Wird fuer die Passwortspeicherung benoetigt.
 * @author FS, BK
 */
public class Hasher {
    
	/**
	 * Ein String wird mit SHA-256 gehasht und das Ergebnis zurueck gelierert.
	 * @param password
	 * @return hashed Passwort
	 * @throws NoSuchAlgorithmException
	 */
    public static String getShaHash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        return new String(Hex.encodeHexString(digest));
    }
    
}
