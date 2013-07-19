package puma.sp.mapping.types;

public abstract class SynonymAttributeCaseInsensitiveValueMapper extends
		SynonymAttributeValueMapper {
	@Override
	protected final Boolean specificEquals(String first, String second) {
		return first.equalsIgnoreCase(second);
	}
}
