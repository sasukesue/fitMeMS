package com.proj.location;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.proj.location");

        noClasses()
            .that()
                .resideInAnyPackage("com.proj.location.service..")
            .or()
                .resideInAnyPackage("com.proj.location.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..com.proj.location.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
