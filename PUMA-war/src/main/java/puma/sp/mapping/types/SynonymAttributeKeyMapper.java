package puma.sp.mapping.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import puma.sp.mapping.AttributeMappingProcessor;
import puma.util.exceptions.mapping.KeySynonymMappingException;

public abstract class SynonymAttributeKeyMapper implements AttributeMappingProcessor {
	public Map<String, List<String>> performMapping(Map<String, List<String>> retrievedAttributes) throws KeySynonymMappingException {
		if (retrievedAttributes.containsKey(this.getSynonymKey()))
			throw new KeySynonymMappingException(this.getCurrentKey(), this.getSynonymKey());
		if (retrievedAttributes.containsKey(this.getCurrentKey())) {
			Map<String, List<String>> result = new HashMap<String, List<String>>();
			result.putAll(retrievedAttributes);
			result.put(this.getSynonymKey(), retrievedAttributes.get(this.getCurrentKey()));
			result.remove(this.getCurrentKey());
			return result;
		} 
		return retrievedAttributes;
	}

	protected abstract String getSynonymKey();
}
