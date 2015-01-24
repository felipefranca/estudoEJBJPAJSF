package ejbjsfjpa.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import ejbjsfjpa.modelo.EntidadeBase;

public class GenericDAO {
	
	/**
	 * Define o valor m�ximo de registros resultados em pesquisas
	 */
	public static final int MAX_PAGE_SIZE = 100; 
	
	EntityManager entityManager;
	
	public EntityManager getEntityManager() {
		return FabricaSingleton.getEntityManager();
	}
 
	/**
	 * Cria a entidade
	 * @param t
	 * @return entidade criada
	 */
	public <T extends EntidadeBase> T create(T t) {
		this.getEntityManager().persist(t);
		this.getEntityManager().flush();
		this.getEntityManager().refresh(t);
		return t;
	}

	/**
	 * Cria ou atualiza a entidade
	 * @param t
	 * @return entidade criada
	 */
	public <T extends EntidadeBase> T createOrUpdate(T t) {
		return (T) this.getEntityManager().merge(t);
	}

	/**
	 * Cria ou atualiza uma lista de entidades transients
	 * @param ts
	 * @return List de entidades persistidas
	 */
	public <T extends EntidadeBase> List<T> createOrUpdate(Collection<T> ts) {
		List<T> persistedTs = new ArrayList<T>();
		for (T t: ts) {
			T pt = this.getEntityManager().merge(t);
			persistedTs.add(pt);
		}
		return persistedTs;
	}
	
	/**
	 * Atualiza entidade
	 * @param t
	 * @return entidade atualizada
	 */
	public <T extends EntidadeBase> T update(T t) {
		return (T) this.getEntityManager().merge(t);
	}

	/**
	 * Deleta entidade
	 * @param t
	 */
	public void delete(Object t) {
		this.getEntityManager().remove(this.getEntityManager().merge(t));
	}
	
	/**
	 * Deleta entidade pelo id
	 * @param id
	 * @param type
	 */
	public <T extends EntidadeBase> void delete(Object id, Class<T> type) {
		T t = find(id, type);
		if (t != null)
			delete(t);
	}

	/**
	 * Remove os objetos pelo id
	 * @param ids
	 * @param type
	 */
	public <T extends EntidadeBase> void delete(Iterable<Long> ids, Class<T> type) {
		for (Long id : ids) {
			T entity = this.find(id, type);
			if (entity != null)
				this.delete(entity);
		}
	}
	
	/**
	 * Deleta as entidades da lista
	 * @param ts
	 */
	public <T extends EntidadeBase> void delete(Collection<T> ts) {
		for (T entity: ts) {
			this.delete(entity);
		}
	}
	
	/**
	 * Encontra entidade pelo id
	 * @param id
	 * @param type
	 * @return entidade encontrada ou null
	 */
	public <T extends EntidadeBase> T find(Object id, Class<T> type) {
		return (T) this.getEntityManager().find(type, id);
	}
	
	/**
	 * Recupera lista de entidades atrav�s de uma consulta nomeada. (non-Javadoc)
	 * 
	 * @see br.gov.prodemge.prodemgejee.business.dataservices.grp.integracao.servicosdados.CrudService#findByNamedQuery(java.lang.String)
	 */
	public List<?> findByNamedQuery(String queryName) {
		return findByNamedQuery(queryName, null, 0);
	}

	/**
	 * Recupera lista de entidades atrav�s de uma consulta nomeada com par�metros (non-Javadoc)
	 * 
	 * @see br.gov.prodemge.prodemgejee.business.dataservices.grp.integracao.servicosdados.CrudService#findByNamedQuery(java.lang.String, java.util.Map)
	 */
	public List<?> findByNamedQuery(String queryName, Map<String, Object> parameters) {
		return findByNamedQuery(queryName, parameters, 0);
	}

	/**
	 * Recupera lista de entidades atrav�s de uma consulta nomeada com limite de tamanho para a lista. (non-Javadoc)
	 * 
	 * @see br.gov.prodemge.prodemgejee.business.dataservices.grp.integracao.servicosdados.CrudService#findByNamedQuery(java.lang.String, int)
	 */
	public List<?> findByNamedQuery(String queryName, int resultLimit) {
		return findByNamedQuery(queryName, null, resultLimit);
	}

	/**
	 * Recupera lista de entidades atrav�s de uma consulta nomeada com par�metros e limite de tamanho para a lista. (non-Javadoc)
	 * 
	 * @see br.gov.prodemge.prodemgejee.business.dataservices.grp.integracao.servicosdados.CrudService#findByNamedQuery(java.lang.String, java.util.Map, int)
	 */
	public List<?> findByNamedQuery(String queryName, Map<String, Object> parameters, int resultLimit) {
		return findByNamedQuery(queryName, parameters, resultLimit, 0);
	}
	

	/**
	 * Recupera uma p�gina (lista de objetos) via consulta nomeada com par�metros para a cl�usula where. A p�gina � definida pelo �ndice do 
	 * primeiro registro e pelo tamanho da p�gina.
	 * 
	 * @param queryName nome da consulta nomeada que retornar� os objetos
	 * @param parameters par�metros de pesquisa no formato <nome do atributo, valor>
	 * @param resultLimit tamanho da p�gina
	 * @param firstResult �ndice do primeiro registro da p�gina
	 * @return lista (p�gina) dos objetos recuperados
 	 */
	protected List<?> findByNamedQuery(String queryName, Map<String, Object> parameters, int resultLimit, int firstResult) {
		Query query = createNamedQuery(parameters, resultLimit, firstResult, queryName, null);
				
		return query.getResultList();
	}

	public <T extends EntidadeBase> List<T> findByNamedQuery(String queryName, Map<String,Object> parameters, int resultLimit, Class<T> type) {
		return findByNamedQuery(queryName, parameters, resultLimit, 0, type);
		
	}
	
	/**
	 * Recupera uma p�gina (lista de entidades tipadas) via consulta nomeada com par�metros para a cl�usula where. A p�gina � definida pelo 
	 * �ndice do primeiro registro e pelo tamanho da p�gina.
	 * 
	 * @param queryName nome da consulta nomeada que retornar� os objetos
	 * @param parameters par�metros de pesquisa no formato <nome do atributo, valor>
	 * @param resultLimit tamanho da p�gina
	 * @param firstResult �ndice do primeiro registro da p�gina
	 * @param type tipo das entidades retornadas 
	 * @return lista (p�gina) das entidades recuperadas
	 */
	protected <T extends EntidadeBase> List<T> findByNamedQuery(String queryName, Map<String, Object> parameters, int resultLimit, int firstResult, Class<T> type) {
		@SuppressWarnings("unchecked")
		TypedQuery<T> query = (TypedQuery<T>)createNamedQuery(parameters, resultLimit, firstResult, queryName, type);
				
		return query.getResultList();
	}
	
	/**
	 * Cria uma query JPA para recupera��o de entidades baseado em uma consulta nomeada. A query pode conter par�metros para a cl�usula where 
	 * e defini��o do tamanho da p�gina. A p�gina � definida pelo �ndice do primeiro registro e pelo seu tamanho. A query pode ser tipada
	 * ou n�o tipada.
	 * 
	 * @param parameters par�metros de pesquisa no formato <nome do atributo, valor>
	 * @param resultLimit tamanho da p�gina
	 * @param firstResult �ndice do primeiro registro da p�gina
	 * @param queryName nome da consulta nomeada 
	 * @param type tipo das entidades retornada na p�gina. Se nulo, a query criada n�o ser� tipada.
	 * @return query JPA
	 */
	private <T extends EntidadeBase> Query createNamedQuery(Map<String, Object> parameters, int resultLimit, int firstResult, String queryName, Class<T> type) {
		// Limita o tamanho m�ximo do resultado
		if (resultLimit <= 0 || resultLimit > MAX_PAGE_SIZE)
			resultLimit = MAX_PAGE_SIZE;
		else
			resultLimit += 1;

		Query query;
		if (type == null)
			query = this.getEntityManager().createNamedQuery(queryName);
		else
			query = this.getEntityManager().createNamedQuery(queryName, type);

		if (parameters != null) {
			Set<Entry<String, Object>> rawParameters = parameters.entrySet();
			for (Entry<String, Object> entry : rawParameters) {
				query.setParameter(entry.getKey(), entry.getValue());
			}
		}

		// Define o primeiro registro da consulta
		query.setFirstResult(firstResult);
		
		// Define limite m�ximo de registros da consulta
		query.setMaxResults(resultLimit);

		return query;
	}

	
}