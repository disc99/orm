package com.github.disc99.orm;

import java.util.Map;

import javax.persistence.Cache;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.Query;
import javax.persistence.SynchronizationType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;

public class EntityManagerFactoryImpl implements EntityManagerFactory {

    public EntityManager createEntityManager() {
        // TODO Auto-generated method stub
        return null;
    }

    public EntityManager createEntityManager(Map map) {
        // TODO Auto-generated method stub
        return null;
    }

    public EntityManager createEntityManager(SynchronizationType synchronizationType) {
        // TODO Auto-generated method stub
        return null;
    }

    public EntityManager createEntityManager(SynchronizationType synchronizationType, Map map) {
        // TODO Auto-generated method stub
        return null;
    }

    public CriteriaBuilder getCriteriaBuilder() {
        // TODO Auto-generated method stub
        return null;
    }

    public Metamodel getMetamodel() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isOpen() {
        // TODO Auto-generated method stub
        return false;
    }

    public void close() {
        // TODO Auto-generated method stub

    }

    public Map<String, Object> getProperties() {
        // TODO Auto-generated method stub
        return null;
    }

    public Cache getCache() {
        // TODO Auto-generated method stub
        return null;
    }

    public PersistenceUnitUtil getPersistenceUnitUtil() {
        // TODO Auto-generated method stub
        return null;
    }

    public void addNamedQuery(String name, Query query) {
        // TODO Auto-generated method stub

    }

    public <T> T unwrap(Class<T> cls) {
        // TODO Auto-generated method stub
        return null;
    }

    public <T> void addNamedEntityGraph(String graphName, EntityGraph<T> entityGraph) {
        // TODO Auto-generated method stub

    }

}
