package fr.diginamic.recensement.dao;

import fr.diginamic.recensement.entites.Departement;
import jakarta.persistence.EntityManager;

/**
 * DAO gérant l'accès aux données de l'entité Departement.
 */
public class DepartementDao {

    /**
     * Recherche un département par son code.
     * Le code département étant la clé primaire de l'entité, il s'agit d'un accès direct.
     *
     * @param em              EntityManager courant
     * @param codeDepartement code du département recherché
     * @return le département s'il existe, vide sinon
     */
    public Departement findByCode(EntityManager em, String codeDepartement) {
        return em.find(Departement.class, codeDepartement);
    }

    /**
     * Persiste un nouveau département.
     *
     * @param em          EntityManager courant
     * @param departement département à créer
     * @return le département persisté
     */
    public Departement create(EntityManager em, Departement departement) {
        em.persist(departement);
        return departement;
    }
}
