/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util.exceptions.management;

/**
 *
 * @author jasper
 */
public class SubscribeException extends Exception {
	private static final long serialVersionUID = 4424498691562474220L;

	public SubscribeException(String tenantName, String serviceName) {
        super("Subscribe failed. The tenant " + tenantName + " was already subscribed to service " + serviceName + ".");
    }
}
