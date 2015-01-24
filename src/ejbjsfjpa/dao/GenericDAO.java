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
	 * Define o valor máximo de registros resultados em pesquisas
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
	 * Recupera lista de entidades através de uma consulta nomeada. (non-Javadoc)
	 * 
	 * @see br.gov.prodemge.prodemgejee.business.dataservices.grp.integracao.servicosdados.CrudService#findByNamedQuery(java.lang.String)
	 */
	public List<?> findByNamedQuery(String queryName) {
		return findByNamedQuery(queryName, null, 0);
	}

	/**
	 * Recupera lista de entidades através de uma consulta nomeada com parâmetros (non-Javadoc)
	 * 
	 * @see br.gov.prodemge.prodemgejee.business.dataservices.grp.integracao.servicosdados.CrudService#findByNamedQuery(java.lang.String, java.util.Map)
	 */
	public List<?> findByNamedQuery(String queryName, Map<String, Object> parameters) {
		return findByNamedQuery(queryName, parameters, 0);
	}

	/**
	 * Recupera lista de entidades através de uma consulta nomeada com limite de tamanho para a lista. (non-Javadoc)
	 * 
	 * @see br.gov.prodemge.prodemgejee.business.dataservices.grp.integracao.servicosdados.CrudService#findByNamedQuery(java.lang.String, int)
	 */
	public List<?> findByNamedQuery(String queryName, int resultLimit) {
		return findByNamedQuery(queryName, null, resultLimit);
	}

	/**
	 * Recupera lista de entidades através de uma consulta nomeada com parâmetros e limite de tamanho para a lista. (non-Javadoc)
	 * 
	 * @see br.gov.prodemge.prodemgejee.business.dataservices.grp.integracao.servicosdados.CrudService#findByNamedQuery(java.lang.String, java.util.Map, int)
	 */
	public List<?> findByNamedQuery(String queryName, Map<String, Object> parameters, int resultLimit) {
		return findByNamedQuery(queryName, parameters, resultLimit, 0);
	}
	

	/**
	 * Recupera uma página (lista de objetos) via consulta nomeada com parâmetros para a cláusula where. A página é definida pelo índice do 
	 * primeiro registro e pelo tamanho da página.
	 * 
	 * @param queryName nome da consulta nomeada que retornará os objetos
	 * @param parameters parâmetros de pesquisa no formato <nome do atributo, valor>
	 * @param resultLimit tamanho da página
	 * @param firstResult índice do primeiro registro da página
	 * @return lista (página) dos objetos recuperados
 	 */
	protected List<?> findByNamedQuery(String queryName, Map<String, Object> parameters, int resultLimit, int firstResult) {
		Query query = createNamedQuery(parameters, resultLimit, firstResult, queryName, null);
				
		return query.getResultList();
	}

	public <T extends EntidadeBase> List<T> findByNamedQuery(String queryName, Map<String,Object> parameters, int resultLimit, Class<T> type) {
		return findByNamedQuery(queryName, parameters, resultLimit, 0, type);
		
	}
	
	/**
	 * Recupera uma página (lista de entidades tipadas) via consulta nomeada com parâmetros para a cláusula where. A página é definida pelo 
	 * índice do primeiro registro e pelo tamanho da página.
	 * 
	 * @param queryName nome da consulta nomeada que retornará os objetos
	 * @param parameters parâmetros de pesquisa no formato <nome do atributo, valor>
	 * @param resultLimit tamanho da página
	 * @param firstResult índice do primeiro registro da página
	 * @param type tipo das entidades retornadas 
	 * @return lista (página) das entidades recuperadas
	 */
	protected <T extends EntidadeBase> List<T> findByNamedQuery(String queryName, Map<String, Object> parameters, int resultLimit, int firstResult, Class<T> type) {
		@SuppressWarnings("unchecked")
		TypedQuery<T> query = (TypedQuery<T>)createNamedQuery(parameters, resultLimit, firstResult, queryName, type);
				
		return query.getResultList();
	}
	
	/**
	 * Cria uma query JPA para recuperação de entidades baseado em uma consulta nomeada. A query pode conter parâmetros para a cláusula where 
	 * e definição do tamanho da página. A página é definida pelo índice do primeiro registro e pelo seu tamanho. A query pode ser tipada
	 * ou não tipada.
	 * 
	 * @param parameters parâmetros de pesquisa no formato <nome do atributo, valor>
	 * @param resultLimit tamanho da página
	 * @param firstResult índice do primeiro registro da página
	 * @param queryName nome da consulta nomeada 
	 * @param type tipo das entidades retornada na página. Se nulo, a query criada não será tipada.
	 * @return query JPA
	 */
	private <T extends EntidadeBase> Query createNamedQuery(Map<String, Object> parameters, int resultLimit, int firstResult, String queryName, Class<T> type) {
		// Limita o tamanho máximo do resultado
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
		
		// Define limite máximo de registros da consulta
		query.setMaxResults(resultLimit);

		return query;
	}

	
}