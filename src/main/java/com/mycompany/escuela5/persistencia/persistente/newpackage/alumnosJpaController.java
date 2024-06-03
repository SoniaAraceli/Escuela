/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.escuela5.persistencia.persistente.newpackage;

import com.mycompany.escuela5.logica.alumnos;
import com.mycompany.escuela5.persistencia.persistente.newpackage.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author edati
 */
public class alumnosJpaController implements Serializable {

    public alumnosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(alumnos alumnos) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(alumnos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(alumnos alumnos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            alumnos = em.merge(alumnos);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = alumnos.getId();
                if (findalumnos(id) == null) {
                    throw new NonexistentEntityException("The alumnos with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            alumnos alumnos;
            try {
                alumnos = em.getReference(alumnos.class, id);
                alumnos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The alumnos with id " + id + " no longer exists.", enfe);
            }
            em.remove(alumnos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<alumnos> findalumnosEntities() {
        return findalumnosEntities(true, -1, -1);
    }

    public List<alumnos> findalumnosEntities(int maxResults, int firstResult) {
        return findalumnosEntities(false, maxResults, firstResult);
    }

    private List<alumnos> findalumnosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(alumnos.class));
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

    public alumnos findalumnos(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(alumnos.class, id);
        } finally {
            em.close();
        }
    }

    public int getalumnosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<alumnos> rt = cq.from(alumnos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
