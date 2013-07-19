/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util.exceptions.flow;

/**
 *
 * @author jasper
 */
public class FlowException extends Exception {
	private static final long serialVersionUID = -2769412185322805572L;

	public FlowException(String message) {
        super("PUMA Identification Service: Could not process the authentication: Flow was not followed properly (" + message + ")");
    }    
}
