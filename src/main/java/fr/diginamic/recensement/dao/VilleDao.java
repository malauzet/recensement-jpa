package fr.diginamic.recensement.dao;

import fr.diginamic.recensement.entites.Departement;
import fr.diginamic.recensement.entites.Ville;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * DAO gérant l'accès aux données de l'entité Ville.
 */
public class VilleDao {

    /**
     * Recherche une ville par son code commune au sein d'un département donné.
     * Contrairement à Region/Departement, la clé métier de Ville est composite
     * (département + code commune) : une requête JPQL est nécessaire.
     *
     * @param em          EntityManager courant
     * @param codeCommune code commune recherché
     * @param departement département auquel la commune est rattachée
     * @return la ville si elle existe, vide sinon
     */
    public Ville findByCodeCommuneAndDepartement(EntityManager em, String codeCommune, Departement departement) {
        TypedQuery<Ville> query = em.createQuery(
                "SELECT v FROM Ville v WHERE v.codeCommune = :codeCommune AND v.departement = :departement",
                Ville.class);
        query.setParameter("codeCommune", codeCommune);
        query.setParameter("departement", departement);

        List<Ville> resultats = query.getResultList();
        if (resultats.isEmpty()) {
            return null;
        }
        return resultats.getFirst();
    }

    /**
     * Persiste une nouvelle ville.
     *
     * @param em    EntityManager courant
     * @param ville ville à créer
     * @return la ville persistée
     */
    public Ville create(EntityManager em, Ville ville) {
        em.persist(ville);
        return ville;
    }

    /**
     * Recherche les villes triées par population décroissante.
     *
     * @param em EntityManager courant
     * @return la liste des villes triées
     */
    public List<Ville> findAllOrderByPopulationDesc(EntityManager em) {
        TypedQuery<Ville> query = em.createQuery(
                "SELECT v FROM Ville v ORDER BY v.populationTotale DESC", Ville.class);
        return query.getResultList();
    }

    /**
     * Recherche les villes d'un département, triées par population décroissante.
     *
     * @param em              EntityManager courant
     * @param codeDepartement code du département
     * @return la liste des villes triées
     */
    public List<Ville> findByDepartementOrderByPopulationDesc(EntityManager em, String codeDepartement) {
        TypedQuery<Ville> query = em.createQuery(
                "SELECT v FROM Ville v WHERE v.departement.codeDepartement = :codeDepartement " +
                        "ORDER BY v.populationTotale DESC", Ville.class);
        query.setParameter("codeDepartement", codeDepartement);
        return query.getResultList();
    }

    /**
     * Recherche les villes d'une région, triées par population décroissante.
     *
     * @param em         EntityManager courant
     * @param codeRegion code de la région
     * @return la liste des villes triées
     */
    public List<Ville> findByRegionOrderByPopulationDesc(EntityManager em, Integer codeRegion) {
        TypedQuery<Ville> query = em.createQuery(
                "SELECT v FROM Ville v WHERE v.departement.region.codeRegion = :codeRegion " +
                        "ORDER BY v.populationTotale DESC", Ville.class);
        query.setParameter("codeRegion", codeRegion);
        return query.getResultList();
    }

    /**
     * Calcule la population totale d'un département donné.
     *
     * @param em              EntityManager courant
     * @param codeDepartement code du département
     * @return la somme des populations des villes de ce département
     */
    public Long sumPopulationByDepartement(EntityManager em, String codeDepartement) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT SUM(v.populationTotale) FROM Ville v " +
                        "WHERE v.departement.codeDepartement = :codeDepartement " +
                        "GROUP BY v.departement.codeDepartement", Long.class);
        query.setParameter("codeDepartement", codeDepartement);
        return query.getSingleResult();
    }

    /**
     * Calcule la population totale d'une région donnée.
     *
     * @param em         EntityManager courant
     * @param codeRegion code de la région
     * @return la somme des populations des villes de cette région
     */
    public Long sumPopulationByRegion(EntityManager em, Integer codeRegion) {
        TypedQuery<Long> query = em.createQuery(
                "SELECT SUM(v.populationTotale) FROM Ville v " +
                        "WHERE v.departement.region.codeRegion = :codeRegion " +
                        "GROUP BY v.departement.region.codeRegion", Long.class);
        query.setParameter("codeRegion", codeRegion);
        return query.getSingleResult();
    }

    /**
     * Recherche les villes d'un département dont la population est comprise entre min et max.
     *
     * @param em              EntityManager courant
     * @param min             population minimale (incluse)
     * @param max             population maximale (incluse)
     * @param codeDepartement code du département
     * @return la liste des villes correspondantes
     */
    public List<Ville> findByPopulationBetweenForDepartement(EntityManager em, int min, int max, String codeDepartement) {
        TypedQuery<Ville> query = em.createQuery(
                "SELECT v FROM Ville v WHERE v.populationTotale BETWEEN :min AND :max " +
                        "AND v.departement.codeDepartement = :codeDepartement", Ville.class);
        query.setParameter("min", min);
        query.setParameter("max", max);
        query.setParameter("codeDepartement", codeDepartement);
        return query.getResultList();
    }

    /**
     * Recherche les villes d'une région dont la population est comprise entre min et max.
     *
     * @param em         EntityManager courant
     * @param min        population minimale (incluse)
     * @param max        population maximale (incluse)
     * @param codeRegion code de la région
     * @return la liste des villes correspondantes
     */
    public List<Ville> findByPopulationBetweenForRegion(EntityManager em, int min, int max, Integer codeRegion) {
        TypedQuery<Ville> query = em.createQuery(
                "SELECT v FROM Ville v WHERE v.populationTotale BETWEEN :min AND :max " +
                        "AND v.departement.region.codeRegion = :codeRegion", Ville.class);
        query.setParameter("min", min);
        query.setParameter("max", max);
        query.setParameter("codeRegion", codeRegion);
        return query.getResultList();
    }

    /**
     * Recherche les villes de France dont la population est comprise entre min et max.
     *
     * @param em  EntityManager courant
     * @param min population minimale (incluse)
     * @param max population maximale (incluse)
     * @return la liste des villes correspondantes
     */
    public List<Ville> findByPopulationBetween(EntityManager em, int min, int max) {
        TypedQuery<Ville> query = em.createQuery(
                "SELECT v FROM Ville v WHERE v.populationTotale BETWEEN :min AND :max", Ville.class);
        query.setParameter("min", min);
        query.setParameter("max", max);
        return query.getResultList();
    }
}
