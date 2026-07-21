package fr.diginamic.recensement.entites;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Représente une commune rattachée à un département.
 * Le couple (département, code commune) constitue la clé métier unique :
 * le code commune seul n'est unique qu'au sein d'un même département.
 */
@Entity
@Table(name = "VILLE", uniqueConstraints = @UniqueConstraint(columnNames = {"departement_id", "code_commune"}))
@Getter
@Setter
@NoArgsConstructor
public class Ville {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_commune", nullable = false)
    private String codeCommune;

    @Column(nullable = false)
    private String nom;

    @Column(name = "population_totale")
    private Integer populationTotale;

    @ManyToOne(optional = false)
    @JoinColumn(name = "departement_id", nullable = false)
    private Departement departement;

    /**
     * Crée une nouvelle ville.
     *
     * @param codeCommune      code commune (unique au sein du département)
     * @param nom               nom de la commune
     * @param populationTotale  population totale de la commune
     * @param departement       département de rattachement
     */
    public Ville(String codeCommune, String nom, Integer populationTotale, Departement departement) {
        this.codeCommune = codeCommune;
        this.nom = nom;
        this.populationTotale = populationTotale;
        this.departement = departement;
    }
}
