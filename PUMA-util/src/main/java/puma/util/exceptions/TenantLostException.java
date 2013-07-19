/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util.exceptions;

/**
 *
 * @author jasper
 */
public class TenantLostException extends Exception {
	private static final long serialVersionUID = 5212043353709743382L;

	public TenantLostException() {
        super("PUMA Identity Service Exception: Could not find the tenant used for communication");
    }
}
