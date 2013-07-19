package puma.util.html;

public abstract class PageGenerator {
	public final String generate() {
		String result = "<html>\n";
		result = result + "<head>\n";
		result = result + "<title>" + this.getTitle() + "</title>\n";
		result = result + "</head>\n";
		result = result + "<body>\n";
		result = result + this.getBody();
		result = result + "</body>\n";
		result = result + "</html>\n";		
		return result;
	}
	
	protected abstract String getTitle();
	protected abstract String getBody();	
}
