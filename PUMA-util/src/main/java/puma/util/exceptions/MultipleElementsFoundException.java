package puma.util.exceptions;

public class MultipleElementsFoundException extends Exception {
	private static final long serialVersionUID = 4237261419995483503L;
    
    public MultipleElementsFoundException(String name) {
        super("Multiple instances found for element " + name + " in the data store");
    }
}
