package com.udemy.hexagonal.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.udemy.hexagonal")
public class SpringRulesTest {

    @ArchTest
    @SuppressWarnings("unused")
    static final ArchRule domain_should_not_use_spring =
            noClasses()
                    .that().resideInAPackage("..application.core.domain..")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage("org.springframework..");

    @ArchTest
    @SuppressWarnings("unused")
    static final ArchRule usecase_should_not_use_web =
            noClasses()
                    .that().resideInAPackage("..application.core.usecase..")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage("org.springframework.web..");

}
