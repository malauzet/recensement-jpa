package fr.diginamic.recensement;

import fr.diginamic.recensement.dao.VilleDao;
import fr.diginamic.recensement.entites.Ville;
import fr.diginamic.recensement.utils.JpaUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Programme exécutable illustrant des extractions JPQL sur les données de recensement.
 */
public class RechercheRecensement {

    private static final VilleDao villeDao = new VilleDao();

    static void main() {

        try (EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager()) {
            System.out.println("=== Villes de France par population décroissante (top 10) ===");
            afficher(villeDao.findAllOrderByPopulationDesc(em), 10);

            System.out.println("\n=== Villes du département 69 par population décroissante ===");
            afficher(villeDao.findByDepartementOrderByPopulationDesc(em, "69"), 10);

            System.out.println("\n=== Villes de la région 84 (Auvergne-Rhône-Alpes) par population décroissante ===");
            afficher(villeDao.findByRegionOrderByPopulationDesc(em, 84), 10);

            System.out.println("\n=== Population totale du département 69 ===");
            System.out.println(villeDao.sumPopulationByDepartement(em, "69"));

            System.out.println("\n=== Population totale de la région 84 ===");
            System.out.println(villeDao.sumPopulationByRegion(em, 84));

            System.out.println("\n=== Villes du département 69 entre 10 000 et 50 000 habitants ===");
            afficher(villeDao.findByPopulationBetweenForDepartement(em, 10_000, 50_000, "69"), Integer.MAX_VALUE);

            System.out.println("\n=== Villes de la région 84 entre 10 000 et 50 000 habitants ===");
            afficher(villeDao.findByPopulationBetweenForRegion(em, 10_000, 50_000, 84), Integer.MAX_VALUE);

            System.out.println("\n=== Villes de France entre 190 000 et 220 000 habitants ===");
            afficher(villeDao.findByPopulationBetween(em, 190_000, 220_000), Integer.MAX_VALUE);
        } finally {
            JpaUtil.close();
        }
    }

    /**
     * Affiche une liste de villes, limitée à un nombre maximum de lignes.
     *
     * @param villes liste des villes à afficher
     * @param limite nombre maximum de villes affichées
     */
    private static void afficher(List<Ville> villes, int limite) {

        int compteur = 0;

        for (Ville ville : villes) {
            if (compteur >= limite) {
                break;
            }
            System.out.printf("%-30s %10d hab. (dép. %s)%n",
                    ville.getNom(), ville.getPopulationTotale(), ville.getDepartement().getCodeDepartement());

            compteur++;
        }
    }
}
