package com.smalaca.architecturetests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.core.importer.ImportOption.Predefined.DO_NOT_INCLUDE_JARS;
import static com.tngtech.archunit.core.importer.ImportOption.Predefined.DO_NOT_INCLUDE_TESTS;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class ProjectManagementContextTest {
    private static final String PROJECT_MANAGEMENT_CONTEXT = "com.smalaca.projectmanagement";

    @Test
    void projectManagementContextShouldBeIndependent() {
        classes().that()
                .resideInAPackage(PROJECT_MANAGEMENT_CONTEXT)
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                        javaPackages(), jpaPackages(), springFrameworkPackages(), parallelRunPackages(),
                        PROJECT_MANAGEMENT_CONTEXT)

                .because("0001-component-based-architecture.md")
                .check(projectManagementClasses());
    }

    public static JavaClasses projectManagementClasses() {
        return classesFrom(PROJECT_MANAGEMENT_CONTEXT);
    }

    private static JavaClasses classesFrom(String packageName) {
        return new ClassFileImporter()
                .withImportOption(DO_NOT_INCLUDE_JARS)
                .withImportOption(DO_NOT_INCLUDE_TESTS)
                .importPackages(packageName);
    }

    private String parallelRunPackages() {
        return allPackagesIn("com.smalaca.parallelrun.projectmanagement");
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
