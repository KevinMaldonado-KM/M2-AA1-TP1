package fac.luminy.m2.aa1.tp1.controller;

import fac.luminy.m2.aa1.tp1.model.TypeVoiture;
import fac.luminy.m2.aa1.tp1.model.dto.VoitureDTO;
import fac.luminy.m2.aa1.tp1.model.entity.Personne;
import fac.luminy.m2.aa1.tp1.model.entity.Voiture;
import fac.luminy.m2.aa1.tp1.repository.XUserRepository;
import fac.luminy.m2.aa1.tp1.service.VoitureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;


/**
 * Contrôleur REST pour gérer les opérations liées aux voitures.
 * Fournit des points d'accès pour récupérer les informations des voitures
 * en fonction du nom du propriétaire.
 *
 * @author matmiche
 */
@RestController
@RequestMapping("voitures")
@Slf4j
@AllArgsConstructor
public class VoitureController {

    private VoitureService service;

    @Autowired
    private XUserRepository userRepository;


    /**
     * Récupère la liste des voitures pour un propriétaire donné.
     *
     * @param nom le nom du propriétaire dont les voitures doivent être récupérées
     * @return une liste de {@link VoitureDTO} représentant les voitures du propriétaire
     */
    @Operation(summary = "Récupère la liste des voitures pour un propriétaire donné",
            description = "Retourne une liste de VoitureDTO pour le propriétaire spécifié par son nom")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des voitures récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Propriétaire non trouvé"),
            @ApiResponse(responseCode = "500", description = "Erreur interne du serveur")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_PROPRIETAIRE') and #nom == authentication.name)")
    @GetMapping("proprietaire/{nom}")
    public List<VoitureDTO> getVoitures(@PathVariable String nom) {
        log.info("Controller - recuperation de voiture pour {}", nom);
        return service.recupererVoituresProprietaire(nom);
    }

    @QueryMapping
    public List<VoitureDTO> getVoitures(@Argument TypeVoiture typeVoiture, @Argument Double prix, Principal principal) {
        log.info("Controller - recuperation des voitures");
        return service.searchVoitures(typeVoiture, prix);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SchemaMapping(typeName = "Voiture", field = "proprietaire")
    public Personne getProprietaire(VoitureDTO voitureDTO) {
        Voiture voiture = service.findVoitureById(voitureDTO.getId());
        return voiture.getProprietaire();  // Récupère le propriétaire lié à la voiture
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @SchemaMapping(typeName = "Voiture", field = "locataire")
    public Personne getLocataire(VoitureDTO voitureDTO) {
        Voiture voiture = service.findVoitureById(voitureDTO.getId());
        return voiture.getLocataire();  // Récupère le locataire lié à la voiture
    }
}
