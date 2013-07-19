package puma.util.html;

import java.util.Map;

import org.apache.commons.lang.WordUtils;

public abstract class FormGenerator extends PageGenerator {
	private Map<String, String> inputFields;
	
	@Override
	protected String getBody() {
		String result;
		result = "<h1>" + this.getFormTitle() + "</h1>\n";
		result = "<form method=\"post\" id=\"" + this.getId() + "\" action=\"" + this.getEndpoint() + "\">";
		result = result + "<table>\n";
		for (String key: inputFields.keySet()) {
			result = result + "<tr><td>" + key + ":</td><td>" + inputFields.get(key) + "</td></tr>\n";
		}
		result = result + "</table>\n";
		result = result + "</form>\n";
		return result;
	}
	
	protected abstract String getId();
	protected abstract String getEndpoint();
	
	protected String getFormTitle() {
		return WordUtils.capitalize(this.getId());
	}
}
