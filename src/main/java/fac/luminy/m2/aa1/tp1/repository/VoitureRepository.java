package fac.luminy.m2.aa1.tp1.repository;

import fac.luminy.m2.aa1.tp1.model.TypeVoiture;
import fac.luminy.m2.aa1.tp1.model.entity.Voiture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoitureRepository extends JpaRepository<Voiture, Long> {

    List<Voiture> findByProprietaireNom(@Param("nom") String nom);

    List<Voiture> findByLocataireNom(String greenwood);

    @Query("SELECT v FROM VOITURE v WHERE "
            + "(:type IS NULL OR v.type = :type) "
            + "AND (:prixMin IS NULL OR v.prix BETWEEN :prixMin * 0.9 AND :prixMin * 1.1)")
    List<Voiture> searchByPreferences(@Param("type") TypeVoiture type, @Param("prixMin") Double prixMin);


}
