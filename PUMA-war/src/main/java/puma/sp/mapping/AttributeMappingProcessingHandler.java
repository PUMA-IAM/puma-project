package puma.sp.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import puma.util.exceptions.mapping.AttributeMappingException;

public class AttributeMappingProcessingHandler {
	private List<AttributeMappingProcessor> processors;
	private Map<String, List<String>> attributes;
	
	public AttributeMappingProcessingHandler(Map<String, List<String>> attributes) {
		this.processors = new ArrayList<AttributeMappingProcessor>();
		this.attributes = attributes;
		// LATER Slightly more efficient
		// for (AttributeMappingProcessor processor: this.processors) 
		//		this.processorMap.put(processor.getKey(), processor); 
		// for (String nextKey: this.attributes.keySet()) 
		// 		this.attributeMapping.put(nextKey, this.processorMap.get(nextKey));
	}
	
	public void subscribeProcessor(AttributeMappingProcessor processor) {
		if (this.processors.contains(processor))
			return;
		this.processors.add(processor);
		// LATER Slightly more efficient
		// this.processorMap.put(processor.getKey(), processor); 
		// if (this.attributes.keySet().contains(processor.getKey())) 
		// 		this.attributeMapping.put(processor.getKey(), processor);
		//
	}
	
	public Map<String, List<String>> handle() throws AttributeMappingException {
		if (this.attributes == null || this.attributes.isEmpty())
			return new HashMap<String, List<String>>(0);
		Map<String, List<String>> result = this.attributes;
		for (AttributeMappingProcessor processor: this.processors)
			result = processor.performMapping(result);
		return result;			
	}
}
