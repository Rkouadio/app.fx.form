/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.jpa;

import controller.jpa.exceptions.NonexistentEntityException;
import entities.Categories;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Commandes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hp
 */
public class CategoriesJpaController implements Serializable {

    public CategoriesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Categories categories) {
        if (categories.getCommandesCollection() == null) {
            categories.setCommandesCollection(new ArrayList<Commandes>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Commandes> attachedCommandesCollection = new ArrayList<Commandes>();
            for (Commandes commandesCollectionCommandesToAttach : categories.getCommandesCollection()) {
                commandesCollectionCommandesToAttach = em.getReference(commandesCollectionCommandesToAttach.getClass(), commandesCollectionCommandesToAttach.getId());
                attachedCommandesCollection.add(commandesCollectionCommandesToAttach);
            }
            categories.setCommandesCollection(attachedCommandesCollection);
            em.persist(categories);
            for (Commandes commandesCollectionCommandes : categories.getCommandesCollection()) {
                Categories oldCategoriesOfCommandesCollectionCommandes = commandesCollectionCommandes.getCategories();
                commandesCollectionCommandes.setCategories(categories);
                commandesCollectionCommandes = em.merge(commandesCollectionCommandes);
                if (oldCategoriesOfCommandesCollectionCommandes != null) {
                    oldCategoriesOfCommandesCollectionCommandes.getCommandesCollection().remove(commandesCollectionCommandes);
                    oldCategoriesOfCommandesCollectionCommandes = em.merge(oldCategoriesOfCommandesCollectionCommandes);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Categories categories) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categories persistentCategories = em.find(Categories.class, categories.getId());
            Collection<Commandes> commandesCollectionOld = persistentCategories.getCommandesCollection();
            Collection<Commandes> commandesCollectionNew = categories.getCommandesCollection();
            Collection<Commandes> attachedCommandesCollectionNew = new ArrayList<Commandes>();
            for (Commandes commandesCollectionNewCommandesToAttach : commandesCollectionNew) {
                commandesCollectionNewCommandesToAttach = em.getReference(commandesCollectionNewCommandesToAttach.getClass(), commandesCollectionNewCommandesToAttach.getId());
                attachedCommandesCollectionNew.add(commandesCollectionNewCommandesToAttach);
            }
            commandesCollectionNew = attachedCommandesCollectionNew;
            categories.setCommandesCollection(commandesCollectionNew);
            categories = em.merge(categories);
            for (Commandes commandesCollectionOldCommandes : commandesCollectionOld) {
                if (!commandesCollectionNew.contains(commandesCollectionOldCommandes)) {
                    commandesCollectionOldCommandes.setCategories(null);
                    commandesCollectionOldCommandes = em.merge(commandesCollectionOldCommandes);
                }
            }
            for (Commandes commandesCollectionNewCommandes : commandesCollectionNew) {
                if (!commandesCollectionOld.contains(commandesCollectionNewCommandes)) {
                    Categories oldCategoriesOfCommandesCollectionNewCommandes = commandesCollectionNewCommandes.getCategories();
                    commandesCollectionNewCommandes.setCategories(categories);
                    commandesCollectionNewCommandes = em.merge(commandesCollectionNewCommandes);
                    if (oldCategoriesOfCommandesCollectionNewCommandes != null && !oldCategoriesOfCommandesCollectionNewCommandes.equals(categories)) {
                        oldCategoriesOfCommandesCollectionNewCommandes.getCommandesCollection().remove(commandesCollectionNewCommandes);
                        oldCategoriesOfCommandesCollectionNewCommandes = em.merge(oldCategoriesOfCommandesCollectionNewCommandes);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = categories.getId();
                if (findCategories(id) == null) {
                    throw new NonexistentEntityException("The categories with id " + id + " no longer exists.");
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
            Categories categories;
            try {
                categories = em.getReference(Categories.class, id);
                categories.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categories with id " + id + " no longer exists.", enfe);
            }
            Collection<Commandes> commandesCollection = categories.getCommandesCollection();
            for (Commandes commandesCollectionCommandes : commandesCollection) {
                commandesCollectionCommandes.setCategories(null);
                commandesCollectionCommandes = em.merge(commandesCollectionCommandes);
            }
            em.remove(categories);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Categories> findCategoriesEntities() {
        return findCategoriesEntities(true, -1, -1);
    }

    public List<Categories> findCategoriesEntities(int maxResults, int firstResult) {
        return findCategoriesEntities(false, maxResults, firstResult);
    }

    private List<Categories> findCategoriesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Categories.class));
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

    public Categories findCategories(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Categories.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoriesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Categories> rt = cq.from(Categories.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
