package fr.diginamic.recensement;

import fr.diginamic.recensement.dao.*;
import fr.diginamic.recensement.entites.*;
import fr.diginamic.recensement.utils.JpaUtil;

import jakarta.persistence.EntityManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Programme exécutable intégrant le fichier recensement.csv en base de données.
 * Rejouable : vérifie l'existence de chaque région, département et ville
 * avant de les insérer, aucun doublon n'est créé si le programme est relancé.
 */
public class IntegrationRecensement {

    private static final String CSV_PATH = "/recensement.csv";
    private static final String CSV_SEPARATOR = ";";

    private static final RegionDao regionDao = new RegionDao();
    private static final DepartementDao departementDao = new DepartementDao();
    private static final VilleDao villeDao = new VilleDao();

    static void main() {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            integrer(em);
            em.getTransaction().commit();
            System.out.println("Intégration terminée avec succès.");
        } catch (IOException e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Échec de l'intégration", e);
        } finally {
            em.close();
            JpaUtil.close();
        }
    }

    /**
     * Lit le fichier CSV ligne par ligne et intègre chaque commune en base.
     *
     * @param em EntityManager courant, partagé pour toute la durée de l'import
     * @throws IOException si le fichier CSV est introuvable ou illisible
     */
    private static void integrer(EntityManager em) throws IOException {

        InputStream inputStream = IntegrationRecensement.class.getResourceAsStream(CSV_PATH);
        if (inputStream == null) {
            throw new IllegalStateException("Fichier introuvable dans les ressources : " + CSV_PATH);
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            reader.readLine(); // ligne d'en-tête, ignorée

            String ligne;
            int compteur = 0;
            while ((ligne = reader.readLine()) != null) {
                integrerLigne(em, ligne);
                compteur++;
            }
            System.out.println(compteur + " lignes traitées.");
        }
    }

    /**
     * Intègre une ligne du CSV : crée la région, le département et la ville
     * correspondants s'ils n'existent pas déjà en base.
     *
     * @param em    EntityManager courant
     * @param ligne ligne brute du fichier CSV
     */
    private static void integrerLigne(EntityManager em, String ligne) {
        String[] champs = ligne.split(CSV_SEPARATOR);

        Integer codeRegion = Integer.parseInt(champs[0]);
        String nomRegion = champs[1];
        String codeDepartement = champs[2];
        String codeCommune = champs[5];
        String nomCommune = champs[6];
        Integer populationTotale = parsePopulation(champs[9]);

        Region region = regionDao.findByCode(em, codeRegion);
        if (region == null) {
            region = regionDao.create(em, new Region(codeRegion, nomRegion));
        }

        Departement departement = departementDao.findByCode(em, codeDepartement);
        if (departement == null) {
            departement = departementDao.create(em, new Departement(codeDepartement, region));
        }

        Ville ville = villeDao.findByCodeCommuneAndDepartement(em, codeCommune, departement);
        if (ville == null) {
            villeDao.create(em, new Ville(codeCommune, nomCommune, populationTotale, departement));
        }
    }

    /**
     * Convertit une population du CSV (ex. "14 081") en entier, en retirant
     * les espaces utilisés comme séparateurs de milliers.
     *
     * @param valeur population brute lue dans le CSV
     * @return la population sous forme d'entier
     */
    private static Integer parsePopulation(String valeur) {
        return Integer.parseInt(valeur.replace(" ", ""));
    }
}
