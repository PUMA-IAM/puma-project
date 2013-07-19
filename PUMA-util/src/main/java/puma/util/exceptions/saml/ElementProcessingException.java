/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util.exceptions.saml;

/**
 *
 * @author jasper
 */
public class ElementProcessingException extends Exception {
	private static final long serialVersionUID = -592967039708729095L;

	public ElementProcessingException(String message, String reason) {
        super("Proxy: Error while processing SAML message " + message + " (" + reason + ")");
    }
}
