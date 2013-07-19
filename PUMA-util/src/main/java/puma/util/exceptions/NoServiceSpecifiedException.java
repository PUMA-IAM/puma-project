/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package puma.util.exceptions;

/**
 *
 * @author jasper
 */
public class NoServiceSpecifiedException extends Exception {
	private static final long serialVersionUID = 2348977549682047320L;

	public NoServiceSpecifiedException() {
        this("");
    }
    
    public NoServiceSpecifiedException(String name) {
        super("PUMA Identity Service Exception: Could not look up attributes - no service was specified (" + name + ")");
    }
}
