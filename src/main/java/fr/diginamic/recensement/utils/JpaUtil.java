package fr.diginamic.recensement.utils;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Point d'accès unique à l'EntityManagerFactory de l'application.
 * On la construit une seule fois au chargement de la classe, puis on la réutilise pour ouvrir des EntityManager.
 */
public final class JpaUtil {

    private static final String PERSISTENCE_UNIT_NAME = "recensement";

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    private JpaUtil() {
        // Rend impossible d'écrire new JpaUtil() depuis l'extérieur de la classe.
    }

    /**
     * @return l'EntityManagerFactory partagée de l'application
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        return ENTITY_MANAGER_FACTORY;
    }

    /**
     * Ferme l'EntityManagerFactory. À appeler une seule fois, en fin de programme.
     */
    public static void close() {
        if (ENTITY_MANAGER_FACTORY.isOpen()) {
            ENTITY_MANAGER_FACTORY.close();
        }
    }
}
