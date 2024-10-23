package fac.luminy.m2.aa1.tp1.repository;

import fac.luminy.m2.aa1.tp1.model.TypeVoiture;
import fac.luminy.m2.aa1.tp1.model.entity.Personne;
import fac.luminy.m2.aa1.tp1.model.entity.Voiture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VoitureRepositoryTest {

    @Autowired
    private VoitureRepository voitureRepository;

    @Test
    public void testFindByProprietaireNom() {
        // Arrange
        Voiture voiture = new Voiture();

        Personne proprietaire = new Personne();
        proprietaire.setNom("Greenwood");

        voiture.setProprietaire(proprietaire);
        voiture.setMarque("Ferrari");
        voitureRepository.save(voiture);

        // Act
        List<Voiture> result = voitureRepository.findByProprietaireNom("Greenwood");
        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("Greenwood", result.get(0).getProprietaire().getNom());
    }

    @Test
    public void testFindByProprietaireNom_NotFound() {
        // Act
        List<Voiture> result = voitureRepository.findByProprietaireNom("NonExistent");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindByPreferences() {
        // Arrange
        Voiture voiture1 = new Voiture();
        Personne proprietaire = new Personne();
        proprietaire.setNom("Greenwood");

        voiture1.setProprietaire(proprietaire);
        voiture1.setType(TypeVoiture.SUV);
        voiture1.setPrix(20000.0);
        voitureRepository.save(voiture1);

        Voiture voiture2 = new Voiture();
        voiture2.setProprietaire(proprietaire);
        voiture2.setType(TypeVoiture.BERLINE);
        voiture2.setPrix(50000.0);
        voitureRepository.save(voiture2);

        // Act
        List<Voiture> result1 = voitureRepository.searchByPreferences(TypeVoiture.SUV, null); // Filtre par type seulement
        List<Voiture> result2 = voitureRepository.searchByPreferences(null, 20000.0); // Filtre par prix avec +/- 10%
        List<Voiture> result3 = voitureRepository.searchByPreferences(TypeVoiture.BERLINE, 50000.0); // Filtre par type et prix

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);

        // Vérification des résultats
        assertFalse(result1.isEmpty());
        assertFalse(result2.isEmpty());
        assertFalse(result3.isEmpty());

        // Vérification pour SUV (type uniquement)
        assertEquals(TypeVoiture.SUV, result1.get(0).getType());

        // Vérification pour prix 20000 avec +/- 10%
        double expectedMinPriceFor20000 = 20000 * 0.9;
        double expectedMaxPriceFor20000 = 20000 * 1.1;
        assertTrue(result2.get(0).getPrix() >= expectedMinPriceFor20000 && result2.get(0).getPrix() <= expectedMaxPriceFor20000);

        // Vérification pour BERLINE (type et prix avec +/- 10% pour 50000)
        assertEquals(TypeVoiture.BERLINE, result3.get(0).getType());
        double expectedMinPriceFor50000 = 50000 * 0.9;
        double expectedMaxPriceFor50000 = 50000 * 1.1;
        assertTrue(result3.get(0).getPrix() >= expectedMinPriceFor50000 && result3.get(0).getPrix() <= expectedMaxPriceFor50000);
    }
}
