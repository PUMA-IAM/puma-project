package puma.sp.bean.management;

import java.io.Serializable;
import puma.sp.controllers.management.TenantManagementController;
import puma.sp.model.Tenant;

public class TenantPropertyManagementBean extends AbstractTenantAdministrativeDataSubmittionBean implements Serializable {
	private static final long serialVersionUID = 4625515918015294568L;
	private TenantManagementController tenantCtrl;
	private String attributeRequestEndpoint;
	private String authenticationRequestEndpoint;
	private Boolean locallyManaged;
	private String publicKey;
	private String imageName;
	
	public TenantPropertyManagementBean() {
		this.tenantCtrl = new TenantManagementController();
	}

	@Override
	protected void performSpecificSuccessAction() {
		Tenant currentTenant = this.getActiveTenant();
		if (this.locallyManaged != null)
			currentTenant.setLocallyManaged(this.locallyManaged);
		if (this.attributeRequestEndpoint != null && !this.attributeRequestEndpoint.isEmpty())
			currentTenant.setAttrRequestEndpoint(this.attributeRequestEndpoint);
		if (this.authenticationRequestEndpoint != null && !this.authenticationRequestEndpoint.isEmpty())
			currentTenant.setAuthnRequestEndpoint(this.authenticationRequestEndpoint);
		if (this.publicKey != null && !this.publicKey.isEmpty())
			currentTenant.setIdentityProviderPublicKey(this.publicKey);
		if (this.imageName != null && !this.imageName.isEmpty())
			currentTenant.setImageName(this.imageName);
		this.tenantCtrl.modifyTenant(currentTenant);
	}

	@Override
	protected void performSpecificFailureAction() {
		// Stub		
	}
	
	
	public void setAttributeRequestEndpoint(String attrReq) {
		this.attributeRequestEndpoint = attrReq;
	}
	
	public void setAuthenticationRequestEndpoint(String authnReq) {
		this.authenticationRequestEndpoint = authnReq;
	}
	
	public void setLocallyManaged(Boolean federated) {
		this.locallyManaged = !federated;
	}
	
	public void setPublicKey(String key) {
		this.publicKey = key;
	}
	
	public void setImageName(String name) {
		this.imageName = name;
	}
	
	public String getAttributeRequestEndpoint() {
		return this.attributeRequestEndpoint;
	}

	public String getAuthenticationRequestEndpoint() {
		return this.authenticationRequestEndpoint;
	}
	
	public Boolean getLocallyManaged() {
		return this.locallyManaged;
	}

	public String getPublicKey() {
		return this.publicKey;
	}
	
	public String getImageName() {
		return this.imageName;
	}
}
