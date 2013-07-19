/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util.exceptions.flow;

/**
 *
 * @author jasper
 */
public class ResponseProcessingException extends Exception {
	private static final long serialVersionUID = 8331110103406334527L;

	public ResponseProcessingException(String message) {
        super("PUMA Id Service: Response rejected: " + message);
    }
}
