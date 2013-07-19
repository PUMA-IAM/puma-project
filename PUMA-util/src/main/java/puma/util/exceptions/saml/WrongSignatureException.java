/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util.exceptions.saml;

/**
 *
 * @author jasper
 */
public class WrongSignatureException extends Exception {
	private static final long serialVersionUID = 2858198451757123936L;

	public WrongSignatureException(String issuer, String inResponseTo) {
        super("Could not verify signature of issuer " + issuer + " in response to message " + inResponseTo + ".");
    }
    
}
