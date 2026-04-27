package com.udemy.hexagonal.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.udemy.hexagonal")
public class NamingConventionTest {

    @ArchTest
    @SuppressWarnings("unused")
    public static final ArchRule consumers_should_reside_in_consumer_package = classes()
            .that()
            .haveNameMatching(".*Consumer")
            .should()
            .resideInAPackage("..adapters.in.consumer")
            .as("Consumers should reside only in consumer package");

    @ArchTest
    @SuppressWarnings("unused")
    public static final ArchRule controllers_should_be_in_controller_package =
            classes()
                    .that().haveNameMatching(".*Controller")
                    .should().resideInAPackage("..adapters.in.controller");

    @ArchTest
    @SuppressWarnings("unused")
    public static final ArchRule usecases_should_be_in_usecase_package =
            classes()
                    .that().haveNameMatching(".*UseCase")
                    .should().resideInAPackage("..application.core.usecase");

    @ArchTest
    @SuppressWarnings("unused")
    public static final ArchRule input_ports_should_be_in_ports_in =
            classes()
                    .that().haveNameMatching(".*InputPort")
                    .should().resideInAPackage("..application.ports.in");

    @ArchTest
    @SuppressWarnings("unused")
    public static final ArchRule output_ports_should_be_in_ports_out =
            classes()
                    .that().haveNameMatching(".*OutputPort")
                    .should().resideInAPackage("..application.ports.out");

    @ArchTest
    @SuppressWarnings("unused")
    public static final ArchRule adapters_out_should_be_in_correct_package =
            classes()
                    .that().haveNameMatching(".*Adapter")
                    .and().resideOutsideOfPackage("..adapters.in..")
                    .should().resideInAPackage("..adapters.out");

    @ArchTest
    @SuppressWarnings("unused")
    public static final ArchRule configs_should_be_in_config_package =
            classes()
                    .that().haveNameMatching(".*Config")
                    .should().resideInAPackage("..config");

    @ArchTest
    @SuppressWarnings("unused")
    public static final ArchRule mappers_should_be_in_mapper_package =
            classes()
                    .that().haveNameMatching(".*Mapper")
                    .should().resideInAPackage("..mapper..");

    @ArchTest
    @SuppressWarnings("unused")
    public static final ArchRule entities_should_be_in_entity_package =
            classes()
                    .that().haveNameMatching(".*Entity")
                    .should().resideInAPackage("..entity");

    @ArchTest
    @SuppressWarnings("unused")
    public static final ArchRule request_should_be_in_request_package =
            classes()
                    .that().haveNameMatching(".*Request")
                    .should().resideInAPackage("..request");

    @ArchTest
    @SuppressWarnings("unused")
    public static final ArchRule response_should_be_in_response_package =
            classes()
                    .that().haveNameMatching(".*Response")
                    .should().resideInAPackage("..response");

    @ArchTest
    @SuppressWarnings("unused")
    public static final ArchRule no_controller_outside_adapter_in =
            classes()
                    .that().haveNameMatching(".*Controller")
                    .should().resideInAPackage("..adapters.in..");

}
