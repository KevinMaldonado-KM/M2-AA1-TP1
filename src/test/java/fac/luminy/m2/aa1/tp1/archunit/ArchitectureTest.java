package fac.luminy.m2.aa1.tp1.archunit;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Classe de verification du respect des patterns de développement
 */
public class ArchitectureTest {

    /**
     * Vérifie que toutes les classes de repository se trouvent dans le package correct.
     * Cette règle s'assure que toutes les classes dont le nom se termine par "Repository"
     * résident dans un package contenant "repository".
     *
     * @throws AssertionError si une classe de repository n'est pas dans le package correct
     */
    @Test
    public void repositoriesShouldBeInRepositoryPackage() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("fac.luminy.m2.aa1.tp1");

        ArchRule rule = ArchRuleDefinition.classes()
                .that().haveSimpleNameEndingWith("Repository")
                .should().resideInAPackage("..repository..");

        rule.check(importedClasses);
    }

    /**
     * Vérifie que toutes les classes de controller se trouvent dans le package correct.
     * Cette règle s'assure que toutes les classes dont le nom se termine par "Controller"
     * résident dans un package contenant "controller".
     *
     * @throws AssertionError si une classe de controller n'est pas dans le package correct
     */
    @Test
    public void controllerShouldBeControllerInPackage() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("fac.luminy.m2.aa1.tp1");

        ArchRule rule = ArchRuleDefinition.classes()
                .that().haveSimpleNameEndingWith("Controller")
                .should().resideInAPackage("..controller..");

        rule.check(importedClasses);
    }

    /**
     * Vérifie qu'aucune classe étendant JpaRepository ne se trouve en dehors du package 'repository'.
     *
     * @throws AssertionError si une classe étendant JpaRepository est en dehors du package 'repository'
     */
    @Test
    public void jpaRepositoriesShouldBeInRepositoryPackage() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("fac.luminy.m2.aa1.tp1");

        ArchRule rule = ArchRuleDefinition.classes()
                .that().areAssignableTo(JpaRepository.class)
                .should().resideInAPackage("..repository..");

        rule.check(importedClasses);
    }

    /**
     * Vérifie que les classes de la couche controller ne dépendent pas des classes de la couche repository.
     *
     * @throws AssertionError
     */
    @Test
    public void controllersShouldNotDependOnRepositories() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("fac.luminy.m2.aa1.tp1");

        ArchRule rule = ArchRuleDefinition.noClasses()
                .that().resideInAPackage("..controller..")
                .should().dependOnClassesThat().resideInAPackage("..repository..");

        rule.check(importedClasses);
    }


}
