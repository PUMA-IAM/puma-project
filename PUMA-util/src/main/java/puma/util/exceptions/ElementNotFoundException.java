package puma.util.exceptions;

public class ElementNotFoundException extends Exception {
	private static final long serialVersionUID = 4237261419995483503L;
    
    public ElementNotFoundException(String name) {
        super("Could not find element " + name + " in the data structure");
    }
}
