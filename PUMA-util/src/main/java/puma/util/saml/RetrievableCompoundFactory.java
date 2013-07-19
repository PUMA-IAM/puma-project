package puma.util.saml;

import java.util.List;
import org.opensaml.xml.XMLObject;

public interface RetrievableCompoundFactory<T extends XMLObject> extends CompoundFactory<T> {
	public List<SAMLElementFactory<? extends T>> retrieveFactories();
}
