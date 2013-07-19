/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util.exceptions;

/**
 *
 * @author jasper
 */
public class RelayStateException extends Exception {
	private static final long serialVersionUID = 9147187448103585845L;

	public RelayStateException() {
        super("PUMA Identity Service Exception: Could not find a redirection address");
    }
}
