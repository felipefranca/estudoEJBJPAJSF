package ejbjsfjpa.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class FabricaSingleton {
	
	private static EntityManagerFactory factory;
	private static EntityManager entityManager;
	
	public static EntityManager getEntityManager() {
		if(entityManager.isOpen()){
			return entityManager;
		} else {
			entityManager = factory.createEntityManager();
		}
		return entityManager;
	}
}
