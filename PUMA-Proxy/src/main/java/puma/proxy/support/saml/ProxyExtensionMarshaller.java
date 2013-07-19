package puma.proxy.support.saml;

import java.util.List;

import org.opensaml.saml2.common.Extensions;

public class ProxyExtensionMarshaller extends CustomExtensionsMarshaller {
	private static final String PROXY_EXTENSION_NAME = "Proxy";

	public ProxyExtensionMarshaller(Extensions messageExtensions) {
		super(messageExtensions);
	}

	@Override
	public List<String> get() {
		return super.mapping.get(PROXY_EXTENSION_NAME);
	}
	
}
