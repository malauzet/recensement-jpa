package fr.diginamic.recensement.entites;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Représente un département français, rattaché à une région.
 * Le code département (ex. "01", "2A", "974") constitue la clé métier unique.
 */
@Entity
@Table(name = "DEPARTEMENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Departement {

    @Id
    @Column(name = "code_departement")
    private String codeDepartement;

    @ManyToOne(optional = false) // Un département doit toujours avoir une Region associée, jamais null.
    @JoinColumn(name = "region_code", nullable = false)
    private Region region;

}