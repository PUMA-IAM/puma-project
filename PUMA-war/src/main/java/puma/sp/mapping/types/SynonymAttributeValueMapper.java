package puma.sp.mapping.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import puma.sp.mapping.AttributeMappingProcessor;
import puma.util.exceptions.mapping.KeySynonymMappingException;

public abstract class SynonymAttributeValueMapper implements
		AttributeMappingProcessor {

	@Override
	public Map<String, List<String>> performMapping(
			Map<String, List<String>> retrievedAttributes)
			throws KeySynonymMappingException {
		if (retrievedAttributes.containsKey(this.getCurrentKey())) {
			Boolean hasChange = false;
			List<String> values = new ArrayList<String>(retrievedAttributes.get(this.getCurrentKey()).size());
			for (String next: retrievedAttributes.get(this.getCurrentKey())) {
				if (this.specificEquals(next, this.getCurrentValue())) {
					values.add(this.getSynonymValue());
					hasChange = true;
				} else {
					values.add(next);
				}
			}
			if (hasChange) {
				Map<String, List<String>> result = new HashMap<String, List<String>>(retrievedAttributes.keySet().size());
				result.putAll(retrievedAttributes);
				result.remove(this.getCurrentKey());
				result.put(this.getCurrentKey(), values);
				return result;
			}
		}
		return retrievedAttributes;
	}
	
	protected abstract Boolean specificEquals(String first, String second);
	
	protected abstract String getCurrentValue();
	protected abstract String getSynonymValue();
}
