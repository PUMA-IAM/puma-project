/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util.exceptions.management;

/**
 *
 * @author jasper
 */
public class UnsubscribeException extends Exception {
	private static final long serialVersionUID = 1624533398328898309L;

	public UnsubscribeException(String tenantName, String serviceName) {
        super("Unsubscribe failed. The tenant " + tenantName + " was not subscribed to service " + serviceName + ".");
    }    
}
