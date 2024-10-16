package com.smalaca.architecturetests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.core.importer.ImportOption.Predefined.DO_NOT_INCLUDE_JARS;
import static com.tngtech.archunit.core.importer.ImportOption.Predefined.DO_NOT_INCLUDE_TESTS;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class UserManagementContextTest {
    private static final String USERMANAGEMENT_CONTEXT = "com.smalaca.usermanagement";

    @Test
    void userManagementContextShouldBeIndependent() {
        classes().that()
                .resideInAPackage(USERMANAGEMENT_CONTEXT)
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                        javaPackages(), jpaPackages(), springFrameworkPackages(),
                        USERMANAGEMENT_CONTEXT)

                .because("0001-component-based-architecture.md")
                .check(userManagementClasses());
    }

    public static JavaClasses userManagementClasses() {
        return classesFrom(USERMANAGEMENT_CONTEXT);
    }

    private static JavaClasses classesFrom(String packageName) {
        return new ClassFileImporter()
                .withImportOption(DO_NOT_INCLUDE_JARS)
                .withImportOption(DO_NOT_INCLUDE_TESTS)
                .importPackages(packageName);
    }

    private String javaPackages() {
        return allPackagesIn("java");
    }

    private String jpaPackages() {
        return allPackagesIn("javax.persistence");
    }

    private String springFrameworkPackages() {
        return allPackagesIn("org.springframework");
    }

    private static String allPackagesIn(String packageName) {
        return packageName + "..";
    }
}
