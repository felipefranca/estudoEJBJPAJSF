package ejbjsfjpa.dao;

import javax.persistence.EntityManager;

public class GenericDAO {
	
	EntityManager entityManager;
	
	public EntityManager getEntityManager() {
		return FabricaSingleton.getEntityManager();
	}

	public Object salvar(Object object) throws Exception {
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(object);
			entityManager.getTransaction().commit();
			return object;
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			throw e;
		} 
	}
	
	
	
}
