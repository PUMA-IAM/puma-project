package puma.util.saml.cloners;
import org.opensaml.common.SAMLObject;
	
public interface SAMLObjectCloner<T extends SAMLObject> {
	public T cloneElement(T item);
}
