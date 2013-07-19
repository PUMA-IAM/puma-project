package puma.util.exceptions;

import java.util.List;

public class TenantIdentificationException extends Exception {
	private static final long serialVersionUID = -8124689443198376391L;

	public TenantIdentificationException(List<String> possibilities) {
		super("Could not find a matching tenant for the given possibilities: " + foldPossibilities(possibilities) + "");
	}

	private static String foldPossibilities(List<String> possibilities) {
		String separator = ", ";
		String result = "";
		for (String next: possibilities) {
			result = result + next + separator;
		}
		return result.substring(0, (result.length() - separator.length()));
	}
}
