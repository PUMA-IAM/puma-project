package puma.sp.util;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class CookieHandler {
	private static final int DEFAULT_AGE = -1; // Cookie is only retained for the current session
	//private static final String DEFAULT_DOMAIN = ""; // Should be fetched from the properties file
	
	
	private Map<String, Cookie> availableCookies;
	
	public CookieHandler(Cookie[] cookies) {
		this.availableCookies = new HashMap<String, Cookie>();
		for (int i = 0; i < cookies.length; i++) {
			this.availableCookies.put(cookies[i].getName(), cookies[i]);
		}
	}
	
	public void addCookie(HttpServletResponse response, String cookieName, String cookieValue) {
		Cookie result = new Cookie(cookieName, cookieValue);
		result.setMaxAge(DEFAULT_AGE);
		//result.setDomain(DEFAULT_DOMAIN);
		result.setSecure(false);	// Should be true, but since we're dealing with a proof-of-concept here...
		response.addCookie(result);
	}
	
	public Cookie getCookie(String name) {
		return this.availableCookies.get(name);
	}
}
