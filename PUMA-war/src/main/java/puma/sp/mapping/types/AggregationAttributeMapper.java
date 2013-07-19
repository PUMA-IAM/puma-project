package puma.sp.mapping.types;

import java.util.List;
import java.util.Map;

import puma.sp.mapping.AttributeMappingProcessor;
import puma.util.exceptions.mapping.AttributeMappingException;

public abstract class AggregationAttributeMapper implements
		AttributeMappingProcessor {

	@Override
	public Map<String, List<String>> performMapping(
			Map<String, List<String>> retrievedAttributes)
			throws AttributeMappingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCurrentKey() {
		// TODO Auto-generated method stub
		return null;
	}

}
