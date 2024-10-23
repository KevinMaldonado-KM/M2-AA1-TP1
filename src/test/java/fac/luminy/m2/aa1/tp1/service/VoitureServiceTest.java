package fac.luminy.m2.aa1.tp1.service;


import fac.luminy.m2.aa1.tp1.model.TypeVoiture;
import fac.luminy.m2.aa1.tp1.model.dto.VoitureDTO;
import fac.luminy.m2.aa1.tp1.model.entity.Voiture;
import fac.luminy.m2.aa1.tp1.repository.VoitureRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VoitureServiceTest {

    @Mock
    private VoitureRepository voitureRepository;

    @InjectMocks
    private VoitureService voitureService;

    @Test
    public void testGetVoitures() {
        // Arrange
        Voiture voiture1 = new Voiture();
        voiture1.setId(1L);
        voiture1.setModele("Model S");
        voiture1.setMarque("Tesla");
        voiture1.setAnnee(2020);
        voiture1.setType(TypeVoiture.SUV);
        voiture1.setChevauxFiscaux(10);
        voiture1.setPrix(80000.0);
        voiture1.setConsommation(15.0);
        voiture1.setCouleur("Red");

        Voiture voiture2 = new Voiture();
        voiture2.setId(2L);
        voiture2.setModele("Model 3");
        voiture2.setMarque("Tesla");
        voiture2.setAnnee(2021);
        voiture2.setType(TypeVoiture.BERLINE);
        voiture2.setChevauxFiscaux(8);
        voiture2.setPrix(50000.0);
        voiture2.setConsommation(10.0);
        voiture2.setCouleur("Blue");

        List<Voiture> voitures = Arrays.asList(voiture1, voiture2);
        when(voitureRepository.findByProprietaireNom("Doe")).thenReturn(voitures);

        // Act
        List<VoitureDTO> result = voitureService.recupererVoituresProprietaire("Doe");

        // Assert
        assertEquals(2, result.size());
        assertEquals(voiture1.getId(), result.get(0).getId());
        assertEquals(voiture1.getModele(), result.get(0).getModele());
        assertEquals(voiture1.getMarque(), result.get(0).getMarque());
        assertEquals(voiture1.getAnnee(), result.get(0).getAnnee());
        assertEquals(voiture1.getType(), result.get(0).getType());
        assertEquals(voiture1.getChevauxFiscaux(), result.get(0).getChevauxFiscaux());
        assertEquals(voiture1.getPrix(), result.get(0).getPrix());
        assertEquals(voiture1.getConsommation(), result.get(0).getConsommation());
        assertEquals(voiture1.getCouleur(), result.get(0).getCouleur());

        assertEquals(voiture2.getId(), result.get(1).getId());
        assertEquals(voiture2.getModele(), result.get(1).getModele());
        assertEquals(voiture2.getMarque(), result.get(1).getMarque());
        assertEquals(voiture2.getAnnee(), result.get(1).getAnnee());
        assertEquals(voiture2.getType(), result.get(1).getType());
        assertEquals(voiture2.getChevauxFiscaux(), result.get(1).getChevauxFiscaux());
        assertEquals(voiture2.getPrix(), result.get(1).getPrix());
        assertEquals(voiture2.getConsommation(), result.get(1).getConsommation());
        assertEquals(voiture2.getCouleur(), result.get(1).getCouleur());
    }

    @Test
    public void testGetVoituresWithNullList() {
        // Arrange
        when(voitureRepository.findByProprietaireNom("Doe")).thenReturn(null);

        // Act
        List<VoitureDTO> result = voitureService.recupererVoituresProprietaire("Doe");

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    public void testSearchVoituresByPreferences() {
        // Arrange
        Voiture voiture1 = new Voiture();
        voiture1.setId(1L);
        voiture1.setType(TypeVoiture.SUV);
        voiture1.setPrix(20000.0);

        Voiture voiture2 = new Voiture();
        voiture2.setId(2L);
        voiture2.setType(TypeVoiture.BERLINE);
        voiture2.setPrix(50000.0);

        // Simule la réponse du repository
        when(voitureRepository.searchByPreferences(TypeVoiture.SUV, null)).thenReturn(Arrays.asList(voiture1));
        when(voitureRepository.searchByPreferences(null, 20000.0)).thenReturn(Arrays.asList(voiture1));
        when(voitureRepository.searchByPreferences(TypeVoiture.BERLINE, 50000.0)).thenReturn(Arrays.asList(voiture2));

        // Act
        List<VoitureDTO> result1 = voitureService.searchVoitures(TypeVoiture.SUV, null); // Recherche par type uniquement
        List<VoitureDTO> result2 = voitureService.searchVoitures(null, 20000.0); // Recherche par prix avec +/- 10%
        List<VoitureDTO> result3 = voitureService.searchVoitures(TypeVoiture.BERLINE, 50000.0); // Recherche par type et prix

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);

        // Vérification des résultats
        assertEquals(1, result1.size());
        assertEquals(TypeVoiture.SUV, result1.get(0).getType());

        // Vérification de la marge de 10% pour un prix de 20000
        double expectedMinPriceFor20000 = 20000 * 0.9;
        double expectedMaxPriceFor20000 = 20000 * 1.1;
        assertTrue(result2.get(0).getPrix() >= expectedMinPriceFor20000 && result2.get(0).getPrix() <= expectedMaxPriceFor20000);

        // Vérification de la marge de 10% pour un prix de 50000
        assertEquals(1, result3.size());
        assertEquals(TypeVoiture.BERLINE, result3.get(0).getType());
        double expectedMinPriceFor50000 = 50000 * 0.9;
        double expectedMaxPriceFor50000 = 50000 * 1.1;
        assertTrue(result3.get(0).getPrix() >= expectedMinPriceFor50000 && result3.get(0).getPrix() <= expectedMaxPriceFor50000);
    }
}
