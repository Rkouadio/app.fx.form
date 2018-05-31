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
import entities.Categories;
import entities.Commandes;
import entities.Operations;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hp
 */
public class CommandesJpaController implements Serializable {

    public CommandesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Commandes commandes) {
        if (commandes.getOperationsCollection() == null) {
            commandes.setOperationsCollection(new ArrayList<Operations>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categories categories = commandes.getCategories();
            if (categories != null) {
                categories = em.getReference(categories.getClass(), categories.getId());
                commandes.setCategories(categories);
            }
            Collection<Operations> attachedOperationsCollection = new ArrayList<Operations>();
            for (Operations operationsCollectionOperationsToAttach : commandes.getOperationsCollection()) {
                operationsCollectionOperationsToAttach = em.getReference(operationsCollectionOperationsToAttach.getClass(), operationsCollectionOperationsToAttach.getId());
                attachedOperationsCollection.add(operationsCollectionOperationsToAttach);
            }
            commandes.setOperationsCollection(attachedOperationsCollection);
            em.persist(commandes);
            if (categories != null) {
                categories.getCommandesCollection().add(commandes);
                categories = em.merge(categories);
            }
            for (Operations operationsCollectionOperations : commandes.getOperationsCollection()) {
                Commandes oldCommandesOfOperationsCollectionOperations = operationsCollectionOperations.getCommandes();
                operationsCollectionOperations.setCommandes(commandes);
                operationsCollectionOperations = em.merge(operationsCollectionOperations);
                if (oldCommandesOfOperationsCollectionOperations != null) {
                    oldCommandesOfOperationsCollectionOperations.getOperationsCollection().remove(operationsCollectionOperations);
                    oldCommandesOfOperationsCollectionOperations = em.merge(oldCommandesOfOperationsCollectionOperations);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Commandes commandes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Commandes persistentCommandes = em.find(Commandes.class, commandes.getId());
            Categories categoriesOld = persistentCommandes.getCategories();
            Categories categoriesNew = commandes.getCategories();
            Collection<Operations> operationsCollectionOld = persistentCommandes.getOperationsCollection();
            Collection<Operations> operationsCollectionNew = commandes.getOperationsCollection();
            if (categoriesNew != null) {
                categoriesNew = em.getReference(categoriesNew.getClass(), categoriesNew.getId());
                commandes.setCategories(categoriesNew);
            }
            Collection<Operations> attachedOperationsCollectionNew = new ArrayList<Operations>();
            for (Operations operationsCollectionNewOperationsToAttach : operationsCollectionNew) {
                operationsCollectionNewOperationsToAttach = em.getReference(operationsCollectionNewOperationsToAttach.getClass(), operationsCollectionNewOperationsToAttach.getId());
                attachedOperationsCollectionNew.add(operationsCollectionNewOperationsToAttach);
            }
            operationsCollectionNew = attachedOperationsCollectionNew;
            commandes.setOperationsCollection(operationsCollectionNew);
            commandes = em.merge(commandes);
            if (categoriesOld != null && !categoriesOld.equals(categoriesNew)) {
                categoriesOld.getCommandesCollection().remove(commandes);
                categoriesOld = em.merge(categoriesOld);
            }
            if (categoriesNew != null && !categoriesNew.equals(categoriesOld)) {
                categoriesNew.getCommandesCollection().add(commandes);
                categoriesNew = em.merge(categoriesNew);
            }
            for (Operations operationsCollectionOldOperations : operationsCollectionOld) {
                if (!operationsCollectionNew.contains(operationsCollectionOldOperations)) {
                    operationsCollectionOldOperations.setCommandes(null);
                    operationsCollectionOldOperations = em.merge(operationsCollectionOldOperations);
                }
            }
            for (Operations operationsCollectionNewOperations : operationsCollectionNew) {
                if (!operationsCollectionOld.contains(operationsCollectionNewOperations)) {
                    Commandes oldCommandesOfOperationsCollectionNewOperations = operationsCollectionNewOperations.getCommandes();
                    operationsCollectionNewOperations.setCommandes(commandes);
                    operationsCollectionNewOperations = em.merge(operationsCollectionNewOperations);
                    if (oldCommandesOfOperationsCollectionNewOperations != null && !oldCommandesOfOperationsCollectionNewOperations.equals(commandes)) {
                        oldCommandesOfOperationsCollectionNewOperations.getOperationsCollection().remove(operationsCollectionNewOperations);
                        oldCommandesOfOperationsCollectionNewOperations = em.merge(oldCommandesOfOperationsCollectionNewOperations);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = commandes.getId();
                if (findCommandes(id) == null) {
                    throw new NonexistentEntityException("The commandes with id " + id + " no longer exists.");
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
            Commandes commandes;
            try {
                commandes = em.getReference(Commandes.class, id);
                commandes.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The commandes with id " + id + " no longer exists.", enfe);
            }
            Categories categories = commandes.getCategories();
            if (categories != null) {
                categories.getCommandesCollection().remove(commandes);
                categories = em.merge(categories);
            }
            Collection<Operations> operationsCollection = commandes.getOperationsCollection();
            for (Operations operationsCollectionOperations : operationsCollection) {
                operationsCollectionOperations.setCommandes(null);
                operationsCollectionOperations = em.merge(operationsCollectionOperations);
            }
            em.remove(commandes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Commandes> findCommandesEntities() {
        return findCommandesEntities(true, -1, -1);
    }

    public List<Commandes> findCommandesEntities(int maxResults, int firstResult) {
        return findCommandesEntities(false, maxResults, firstResult);
    }

    private List<Commandes> findCommandesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Commandes.class));
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

    public Commandes findCommandes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Commandes.class, id);
        } finally {
            em.close();
        }
    }

    public int getCommandesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Commandes> rt = cq.from(Commandes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
