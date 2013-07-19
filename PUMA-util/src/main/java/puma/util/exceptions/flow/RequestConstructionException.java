/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util.exceptions.flow;

/**
 *
 * @author jasper
 */
public class RequestConstructionException extends Exception {
	private static final long serialVersionUID = -269423579021416082L;

	public RequestConstructionException(String tenantName, String reason) {
        super("Could not communicate to Identity Provider of " + tenantName + ": " + reason);
    }
}
