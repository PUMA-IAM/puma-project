package puma.util.exceptions.mapping;

public class AttributeDivisionException extends AttributeMappingException {
	private static final long serialVersionUID = -4241058385233820664L;
	public AttributeDivisionException(String attributeName, String attributeValue, String divisionerName) {
		super("Could not divide the attribute " + attributeName + " with value \"" + attributeValue + "\" for the divisioner pattern " + divisionerName + ": division failed");
	}
}
