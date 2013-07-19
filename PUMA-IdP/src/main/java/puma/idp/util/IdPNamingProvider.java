package puma.idp.util;

import puma.util.persistence.EntityManagerProducer;

public class IdPNamingProvider extends EntityManagerProducer {
	@Override
	public String getPersistenceUnitName() {
		return "IdPPU";
	}
}
