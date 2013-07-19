package puma.util.exceptions.mapping;

public class DuplicateKeyMappingException extends AttributeMappingException {
	private static final long serialVersionUID = 5774908516400294788L;

	public DuplicateKeyMappingException(String duplicateEntryName) {
		super(duplicateEntryName);
	}
}
