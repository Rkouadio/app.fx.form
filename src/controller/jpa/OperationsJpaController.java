/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.jpa;

import controller.jpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Commandes;
import entities.Operations;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hp
 */
public class OperationsJpaController implements Serializable {

    public OperationsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Operations operations) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Commandes commandes = operations.getCommandes();
            if (commandes != null) {
                commandes = em.getReference(commandes.getClass(), commandes.getId());
                operations.setCommandes(commandes);
            }
            em.persist(operations);
            if (commandes != null) {
                commandes.getOperationsCollection().add(operations);
                commandes = em.merge(commandes);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Operations operations) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Operations persistentOperations = em.find(Operations.class, operations.getId());
            Commandes commandesOld = persistentOperations.getCommandes();
            Commandes commandesNew = operations.getCommandes();
            if (commandesNew != null) {
                commandesNew = em.getReference(commandesNew.getClass(), commandesNew.getId());
                operations.setCommandes(commandesNew);
            }
            operations = em.merge(operations);
            if (commandesOld != null && !commandesOld.equals(commandesNew)) {
                commandesOld.getOperationsCollection().remove(operations);
                commandesOld = em.merge(commandesOld);
            }
            if (commandesNew != null && !commandesNew.equals(commandesOld)) {
                commandesNew.getOperationsCollection().add(operations);
                commandesNew = em.merge(commandesNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = operations.getId();
                if (findOperations(id) == null) {
                    throw new NonexistentEntityException("The operations with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Operations operations;
            try {
                operations = em.getReference(Operations.class, id);
                operations.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The operations with id " + id + " no longer exists.", enfe);
            }
            Commandes commandes = operations.getCommandes();
            if (commandes != null) {
                commandes.getOperationsCollection().remove(operations);
                commandes = em.merge(commandes);
            }
            em.remove(operations);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Operations> findOperationsEntities() {
        return findOperationsEntities(true, -1, -1);
    }

    public List<Operations> findOperationsEntities(int maxResults, int firstResult) {
        return findOperationsEntities(false, maxResults, firstResult);
    }

    private List<Operations> findOperationsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Operations.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Operations findOperations(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Operations.class, id);
        } finally {
            em.close();
        }
    }

    public int getOperationsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Operations> rt = cq.from(Operations.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
