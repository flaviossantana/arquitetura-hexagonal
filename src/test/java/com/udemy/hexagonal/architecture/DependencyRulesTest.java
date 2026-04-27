package com.udemy.hexagonal.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.udemy.hexagonal")
public class DependencyRulesTest {

    @ArchTest
    @SuppressWarnings("unused")
    static final ArchRule usecase_should_not_access_adapters =
            noClasses()
                    .that().resideInAPackage("..application.core.usecase..")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage("..adapters..");

    @ArchTest
    @SuppressWarnings("unused")
    static final ArchRule domain_should_be_pure =
            noClasses()
                    .that().resideInAPackage("..application.core.domain..")
                    .should().dependOnClassesThat()
                    .resideInAnyPackage(
                            "..adapters..",
                            "..config..",
                            "..application.ports.."
                    );

    @ArchTest
    @SuppressWarnings("unused")
    static final ArchRule adapter_in_should_not_access_adapter_out =
            noClasses()
                    .that().resideInAPackage("..adapters.in..")
                    .should().dependOnClassesThat()
                    .resideInAPackage("..adapters.out..");

}
