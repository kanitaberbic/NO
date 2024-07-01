package ba.smoki.mozaik.ejb.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.List;

//EJB -> potomci
//E -> User, ServiceType, Country
public abstract class AbstractService<E> {

    private final Class<E> entityClass;

    public AbstractService(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager entityManager();

    public void create(E entity){
        entityManager().persist(entity);
    }

    public void edit(E entity){
        entityManager().merge(entity);
    }

    public void delete(E entity){
        EntityManager entityManager = entityManager();
        entityManager.remove(entityManager.merge(entity));
    }

    public E find(Object id){
        return entityManager().find(entityClass, id);
    }

    //Criteria API
    public List<E> findAll(){
        CriteriaQuery criteriaQuery = entityManager().getCriteriaBuilder().createQuery();
        criteriaQuery.select(criteriaQuery.from(entityClass));
        return entityManager().createQuery(criteriaQuery).getResultList();
    }
}
