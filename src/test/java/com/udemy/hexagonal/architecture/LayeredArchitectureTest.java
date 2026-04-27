package com.udemy.hexagonal.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AnalyzeClasses(packages = "com.udemy.hexagonal")
public class LayeredArchitectureTest {


    private static final String ADAPTERS_IN = "AdaptersIn";
    private static final String ADAPTERS_OUT = "AdaptersOut";
    private static final String USE_CASE = "UseCase";
    private static final String PORTS_IN = "PortsIn";
    private static final String PORTS_OUT = "PortsOut";
    private static final String CONFIG = "Config";

    @ArchTest
    public static final ArchRule layeredArchitecture =
            Architectures
                    .layeredArchitecture()
                    .consideringAllDependencies()
                    .layer(ADAPTERS_IN).definedBy("..adapters.in..")
                    .layer(ADAPTERS_OUT).definedBy("..adapters.out..")
                    .layer(USE_CASE).definedBy("..application.core.usecase..")
                    .layer(PORTS_IN).definedBy("..application.ports.in..")
                    .layer(PORTS_OUT).definedBy("..application.ports.out..")
                    .layer(CONFIG).definedBy("..config..")
                    .whereLayer(ADAPTERS_IN).mayOnlyBeAccessedByLayers(CONFIG)
                    .whereLayer(ADAPTERS_OUT).mayOnlyBeAccessedByLayers(CONFIG)
                    .whereLayer(USE_CASE).mayOnlyBeAccessedByLayers(CONFIG)
                    .whereLayer(PORTS_IN).mayOnlyBeAccessedByLayers(USE_CASE, ADAPTERS_IN, CONFIG)
                    .whereLayer(PORTS_OUT).mayOnlyBeAccessedByLayers(USE_CASE, ADAPTERS_OUT, CONFIG)
                    .whereLayer(CONFIG).mayNotBeAccessedByAnyLayer();

    }
