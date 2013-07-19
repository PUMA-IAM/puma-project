/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author jasper
 */
public class PasswordHasher {
	private static final Logger logger = Logger.getLogger(PasswordHasher.class.getName());
	private final static Integer numberOfIterations = 2000;
	private final static Integer saltLength = 10;        
        private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
    
	public static byte[] getHashValue(String password, byte[] salt) {
            return PasswordHasher.pbkdf2(password.toCharArray(), salt);
	}
	
        private static byte[] pbkdf2(char[] password, byte[] salt) {
            // http://crackstation.net/hashing-security.htm
            try {
                PBEKeySpec spec = new PBEKeySpec(password, salt, PasswordHasher.numberOfIterations, password.length + salt.length);
                SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
                return skf.generateSecret(spec).getEncoded();
            } catch (NoSuchAlgorithmException ex) {
            	logger.log(Level.SEVERE, null, ex);
                return new byte[8];
            } catch (InvalidKeySpecException ex) {
            	logger.log(Level.SEVERE, null, ex);
                return new byte[8];                
            }
        }
        
	public static byte[] generateSalt() {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[PasswordHasher.saltLength];
            random.nextBytes(salt);
            return salt;
	}
	
	public static Boolean equalHash(byte[] one, byte[] two) {
        for (Integer i = 0; i < one.length && i < two.length; i++) {
            if (!((Byte) one[i]).equals((Byte) two[i])) {
                return false;
            }
        }
        if (one.length != two.length) {
            return false;
        }
        return true;
    }
}
