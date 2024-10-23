package fac.luminy.m2.aa1.tp1.service;

import fac.luminy.m2.aa1.tp1.model.TypeVoiture;
import fac.luminy.m2.aa1.tp1.model.dto.VoitureDTO;
import fac.luminy.m2.aa1.tp1.model.entity.Voiture;
import fac.luminy.m2.aa1.tp1.repository.VoitureRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class VoitureService {

    private final VoitureRepository voitureRepository;

    /**
     * Récupère la liste des voitures pour un propriétaire donné.
     *
     * @param nomProprietaire le nom du propriétaire dont les voitures doivent être récupérées
     * @return une liste de {@link VoitureDTO} représentant les voitures du propriétaire
     */

    public List <VoitureDTO> recupererVoituresProprietaire(String nomProprietaire){
        log.info("Demande de recuperation des voitures pour le proprietaire avec le nom {}", nomProprietaire);
        //Faire l'appel au repository pour recuperer la voiture a partir du nom du proprietaire
        List<Voiture> voitures = voitureRepository.findByProprietaireNom(nomProprietaire);

        // Si la liste est null, on retourne une liste vide
        if (voitures == null) {
            return new ArrayList<>();
        }

        // Convertir les voitures en voituresDTO
        List<VoitureDTO> listeRetour = voitures.stream().map(voiture -> new VoitureDTO(
                voiture.getId(),
                voiture.getModele(),
                voiture.getMarque(),
                voiture.getAnnee(),
                voiture.getType(),
                voiture.getChevauxFiscaux(),
                voiture.getPrix(),
                voiture.getConsommation(),
                voiture.getCouleur()
        )).collect(Collectors.toList());

        // Retourner la liste des voitures
        log.info("{} voitures pour le proprietaire avec le nom {}",listeRetour.size(),nomProprietaire);
        return listeRetour;
    }

    /**
     * Recherche des voitures en fonction du type et du prix avec une marge de +/- 10% sur le prix.
     *
     * @param type le type de voiture à rechercher (optionnel)
     * @param prix le prix de la voiture à rechercher (optionnel)
     * @return une liste de voitures correspondant aux critères
     */
    public List<VoitureDTO> searchVoitures(TypeVoiture type, Double prix) {
        log.info("Demande de recuperation des voitures selon les filtres type {} et prix {}", type, prix);

        List<Voiture> voitures= voitureRepository.searchByPreferences(type, prix);

        // Convertir les voitures en voituresDTO
        List<VoitureDTO> listeRetour = voitures.stream().map(voiture -> new VoitureDTO(
                voiture.getId(),
                voiture.getModele(),
                voiture.getMarque(),
                voiture.getAnnee(),
                voiture.getType(),
                voiture.getChevauxFiscaux(),
                voiture.getPrix(),
                voiture.getConsommation(),
                voiture.getCouleur()
        )).collect(Collectors.toList());

        log.info("{} voitures avec les critères type {} et prix {}",listeRetour.size(), type, prix);
        return listeRetour;
    }

    public Voiture findVoitureById(Long id) {
        return voitureRepository.findById(id).orElse(null);
    }
}
