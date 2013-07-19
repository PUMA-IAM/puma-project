/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 *
 * @author jasper
 */
public class SecureIdentifierGenerator {
    private static Random random = new SecureRandom();
    public static String generate() {
        // http://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string-in-java
        return new BigInteger(130, random).toString(32);        
    }
}
