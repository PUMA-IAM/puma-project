package puma.sp.mapping.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import puma.sp.mapping.AttributeMappingProcessor;
import puma.util.exceptions.mapping.AttributeDivisionException;

public abstract class DivisionAttributeMapper implements AttributeMappingProcessor {
	@Override
	public Map<String, List<String>> 
		performMapping(Map<String, List<String>> retrievedAttributes) 
				throws AttributeDivisionException {
		if (retrievedAttributes.containsKey(this.getCurrentKey())) {
			Map<String, List<String>> result = new HashMap<String, List<String>>(retrievedAttributes.size() + 1);
			result.putAll(retrievedAttributes);
			result.remove(this.getCurrentKey());
			result.putAll(this.divide(retrievedAttributes.get(this.getCurrentKey())));
			return result;
		}
		return retrievedAttributes;
	}
	
	protected abstract String divisionerName();
	protected abstract Map<String, List<String>> divide(List<String> values);
}
