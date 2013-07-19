package puma.util.exceptions.mapping;

public class KeySynonymMappingException extends AttributeMappingException {
	private static final long serialVersionUID = 7855070391128685477L;

	public KeySynonymMappingException(String currentKeyValue, String synonymKeyValue) {
		super("Could not convert the attribute key from " + currentKeyValue + " to " + synonymKeyValue + ": Synonym key value already exists");
	}
}
