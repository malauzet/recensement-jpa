package fr.diginamic.recensement.entites;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Représente une région française.
 * Le code région (issu du fichier de recensement) constitue la clé métier unique.
 */
@Entity
@Table(name = "REGION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Region {

    @Id
    @Column(name = "code_region")
    private Integer codeRegion;

    @Column(nullable = false)
    private String nom;

}
