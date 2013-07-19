package puma.sp.mapping.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import puma.sp.mapping.AttributeMappingProcessor;
import puma.util.exceptions.mapping.DuplicateKeyMappingException;

public abstract class OneToManyAttributeKeyMapper implements AttributeMappingProcessor {

	@Override
	public Map<String, List<String>> performMapping(
			Map<String, List<String>> retrievedAttributes)
			throws DuplicateKeyMappingException {
		if (retrievedAttributes.containsKey(this.getCurrentKey())) {
			Map<String, List<String>> result = new HashMap<String, List<String>>();
			Map<String, List<String>> newItems = this.breakUp(retrievedAttributes.get(this.getCurrentKey()));
			result.putAll(retrievedAttributes);
			for (String nextKey: newItems.keySet())
				if (result.containsKey(nextKey))
					throw new DuplicateKeyMappingException(nextKey);
			result.putAll(newItems);
			result.remove(this.getCurrentKey());
			return result;
		}
		return retrievedAttributes;
	}
	
	protected abstract Map<String, List<String>> breakUp(List<String> values);

}
