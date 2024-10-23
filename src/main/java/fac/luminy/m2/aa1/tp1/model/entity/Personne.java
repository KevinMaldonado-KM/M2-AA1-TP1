package fac.luminy.m2.aa1.tp1.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity(name = "PERSONNE")
public class Personne {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String nom;
    private String prenom;
    private String adresse;
    private String codePostal;
    private String email;

    @OneToMany(mappedBy = "proprietaire", fetch = FetchType.LAZY)
    private List<Voiture> voituresPossedees;

    @OneToMany(mappedBy = "locataire", fetch = FetchType.LAZY)
    private List<Voiture> voituresLouees;
}
