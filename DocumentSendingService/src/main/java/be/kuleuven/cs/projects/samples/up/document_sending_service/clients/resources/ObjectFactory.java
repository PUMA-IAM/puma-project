
package be.kuleuven.cs.projects.samples.up.document_sending_service.clients.resources;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the puma.sp package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GenerateSharedPassword_QNAME = new QName("http://puma/sp", "generateSharedPassword");
    private final static QName _RetrieveAttributesResponse_QNAME = new QName("http://puma/sp", "retrieveAttributesResponse");
    private final static QName _GetTenantResponse_QNAME = new QName("http://puma/sp", "getTenantResponse");
    private final static QName _RetrieveAttributes_QNAME = new QName("http://puma/sp", "retrieveAttributes");
    private final static QName _RegisterAccess_QNAME = new QName("http://puma/sp", "registerAccess");
    private final static QName _IsAllowedAccessResponse_QNAME = new QName("http://puma/sp", "isAllowedAccessResponse");
    private final static QName _IsAuthenticatedResponse_QNAME = new QName("http://puma/sp", "isAuthenticatedResponse");
    private final static QName _GenerateSharedPasswordResponse_QNAME = new QName("http://puma/sp", "generateSharedPasswordResponse");
    private final static QName _RegisterAccessResponse_QNAME = new QName("http://puma/sp", "registerAccessResponse");
    private final static QName _IsAllowedAccess_QNAME = new QName("http://puma/sp", "isAllowedAccess");
    private final static QName _GetAuthenticationAddressResponse_QNAME = new QName("http://puma/sp", "getAuthenticationAddressResponse");
    private final static QName _IsAuthenticated_QNAME = new QName("http://puma/sp", "isAuthenticated");
    private final static QName _GetTenant_QNAME = new QName("http://puma/sp", "getTenant");
    private final static QName _GetAuthenticationAddress_QNAME = new QName("http://puma/sp", "getAuthenticationAddress");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: puma.sp
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetTenant }
     * 
     */
    public GetTenant createGetTenant() {
        return new GetTenant();
    }

    /**
     * Create an instance of {@link GetAuthenticationAddress }
     * 
     */
    public GetAuthenticationAddress createGetAuthenticationAddress() {
        return new GetAuthenticationAddress();
    }

    /**
     * Create an instance of {@link IsAuthenticated }
     * 
     */
    public IsAuthenticated createIsAuthenticated() {
        return new IsAuthenticated();
    }

    /**
     * Create an instance of {@link IsAllowedAccess }
     * 
     */
    public IsAllowedAccess createIsAllowedAccess() {
        return new IsAllowedAccess();
    }

    /**
     * Create an instance of {@link GetAuthenticationAddressResponse }
     * 
     */
    public GetAuthenticationAddressResponse createGetAuthenticationAddressResponse() {
        return new GetAuthenticationAddressResponse();
    }

    /**
     * Create an instance of {@link IsAllowedAccessResponse }
     * 
     */
    public IsAllowedAccessResponse createIsAllowedAccessResponse() {
        return new IsAllowedAccessResponse();
    }

    /**
     * Create an instance of {@link IsAuthenticatedResponse }
     * 
     */
    public IsAuthenticatedResponse createIsAuthenticatedResponse() {
        return new IsAuthenticatedResponse();
    }

    /**
     * Create an instance of {@link GenerateSharedPasswordResponse }
     * 
     */
    public GenerateSharedPasswordResponse createGenerateSharedPasswordResponse() {
        return new GenerateSharedPasswordResponse();
    }

    /**
     * Create an instance of {@link RegisterAccessResponse }
     * 
     */
    public RegisterAccessResponse createRegisterAccessResponse() {
        return new RegisterAccessResponse();
    }

    /**
     * Create an instance of {@link GetTenantResponse }
     * 
     */
    public GetTenantResponse createGetTenantResponse() {
        return new GetTenantResponse();
    }

    /**
     * Create an instance of {@link RetrieveAttributes }
     * 
     */
    public RetrieveAttributes createRetrieveAttributes() {
        return new RetrieveAttributes();
    }

    /**
     * Create an instance of {@link RegisterAccess }
     * 
     */
    public RegisterAccess createRegisterAccess() {
        return new RegisterAccess();
    }

    /**
     * Create an instance of {@link RetrieveAttributesResponse }
     * 
     */
    public RetrieveAttributesResponse createRetrieveAttributesResponse() {
        return new RetrieveAttributesResponse();
    }

    /**
     * Create an instance of {@link GenerateSharedPassword }
     * 
     */
    public GenerateSharedPassword createGenerateSharedPassword() {
        return new GenerateSharedPassword();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerateSharedPassword }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://puma/sp", name = "generateSharedPassword")
    public JAXBElement<GenerateSharedPassword> createGenerateSharedPassword(GenerateSharedPassword value) {
        return new JAXBElement<GenerateSharedPassword>(_GenerateSharedPassword_QNAME, GenerateSharedPassword.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveAttributesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://puma/sp", name = "retrieveAttributesResponse")
    public JAXBElement<RetrieveAttributesResponse> createRetrieveAttributesResponse(RetrieveAttributesResponse value) {
        return new JAXBElement<RetrieveAttributesResponse>(_RetrieveAttributesResponse_QNAME, RetrieveAttributesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTenantResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://puma/sp", name = "getTenantResponse")
    public JAXBElement<GetTenantResponse> createGetTenantResponse(GetTenantResponse value) {
        return new JAXBElement<GetTenantResponse>(_GetTenantResponse_QNAME, GetTenantResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RetrieveAttributes }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://puma/sp", name = "retrieveAttributes")
    public JAXBElement<RetrieveAttributes> createRetrieveAttributes(RetrieveAttributes value) {
        return new JAXBElement<RetrieveAttributes>(_RetrieveAttributes_QNAME, RetrieveAttributes.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterAccess }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://puma/sp", name = "registerAccess")
    public JAXBElement<RegisterAccess> createRegisterAccess(RegisterAccess value) {
        return new JAXBElement<RegisterAccess>(_RegisterAccess_QNAME, RegisterAccess.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsAllowedAccessResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://puma/sp", name = "isAllowedAccessResponse")
    public JAXBElement<IsAllowedAccessResponse> createIsAllowedAccessResponse(IsAllowedAccessResponse value) {
        return new JAXBElement<IsAllowedAccessResponse>(_IsAllowedAccessResponse_QNAME, IsAllowedAccessResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsAuthenticatedResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://puma/sp", name = "isAuthenticatedResponse")
    public JAXBElement<IsAuthenticatedResponse> createIsAuthenticatedResponse(IsAuthenticatedResponse value) {
        return new JAXBElement<IsAuthenticatedResponse>(_IsAuthenticatedResponse_QNAME, IsAuthenticatedResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerateSharedPasswordResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://puma/sp", name = "generateSharedPasswordResponse")
    public JAXBElement<GenerateSharedPasswordResponse> createGenerateSharedPasswordResponse(GenerateSharedPasswordResponse value) {
        return new JAXBElement<GenerateSharedPasswordResponse>(_GenerateSharedPasswordResponse_QNAME, GenerateSharedPasswordResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterAccessResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://puma/sp", name = "registerAccessResponse")
    public JAXBElement<RegisterAccessResponse> createRegisterAccessResponse(RegisterAccessResponse value) {
        return new JAXBElement<RegisterAccessResponse>(_RegisterAccessResponse_QNAME, RegisterAccessResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsAllowedAccess }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://puma/sp", name = "isAllowedAccess")
    public JAXBElement<IsAllowedAccess> createIsAllowedAccess(IsAllowedAccess value) {
        return new JAXBElement<IsAllowedAccess>(_IsAllowedAccess_QNAME, IsAllowedAccess.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAuthenticationAddressResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://puma/sp", name = "getAuthenticationAddressResponse")
    public JAXBElement<GetAuthenticationAddressResponse> createGetAuthenticationAddressResponse(GetAuthenticationAddressResponse value) {
        return new JAXBElement<GetAuthenticationAddressResponse>(_GetAuthenticationAddressResponse_QNAME, GetAuthenticationAddressResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IsAuthenticated }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://puma/sp", name = "isAuthenticated")
    public JAXBElement<IsAuthenticated> createIsAuthenticated(IsAuthenticated value) {
        return new JAXBElement<IsAuthenticated>(_IsAuthenticated_QNAME, IsAuthenticated.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetTenant }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://puma/sp", name = "getTenant")
    public JAXBElement<GetTenant> createGetTenant(GetTenant value) {
        return new JAXBElement<GetTenant>(_GetTenant_QNAME, GetTenant.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAuthenticationAddress }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://puma/sp", name = "getAuthenticationAddress")
    public JAXBElement<GetAuthenticationAddress> createGetAuthenticationAddress(GetAuthenticationAddress value) {
        return new JAXBElement<GetAuthenticationAddress>(_GetAuthenticationAddress_QNAME, GetAuthenticationAddress.class, null, value);
    }

}
