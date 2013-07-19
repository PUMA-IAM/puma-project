/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util.exceptions.saml;

/**
 *
 * @author jasper
 */
public class ServiceParameterException extends Exception {
	private static final long serialVersionUID = 3107317111149914697L;

	public ServiceParameterException(String message) {
        super("Proxy: Error while processing the parameters of a service: " + message);
    }
}