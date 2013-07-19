package puma.sp.mapping;

import java.util.List;
import java.util.Map;
import puma.util.exceptions.mapping.AttributeMappingException;

public interface AttributeMappingProcessor {
	public Map<String, List<String>> performMapping(Map<String, List<String>> retrievedAttributes) throws AttributeMappingException;
	public String getCurrentKey();
}
