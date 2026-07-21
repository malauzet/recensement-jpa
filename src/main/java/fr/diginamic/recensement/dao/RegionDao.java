package fr.diginamic.recensement.dao;

import fr.diginamic.recensement.entites.Region;
import jakarta.persistence.EntityManager;

/**
 * DAO gérant l'accès aux données de l'entité Region.
 */
public class RegionDao {

    /**
     * Recherche une région par son code.
     * Le code région étant la clé primaire de l'entité, il s'agit d'un accès direct.
     *
     * @param em         EntityManager courant
     * @param codeRegion code de la région recherchée
     * @return la région si elle existe, vide sinon
     */
    public Region findByCode(EntityManager em, Integer codeRegion) {
        return em.find(Region.class, codeRegion);
    }

    /**
     * Persiste une nouvelle région.
     *
     * @param em     EntityManager courant
     * @param region région à créer
     * @return la région persistée
     */
    public Region create(EntityManager em, Region region) {
        em.persist(region);
        return region;
    }
}
