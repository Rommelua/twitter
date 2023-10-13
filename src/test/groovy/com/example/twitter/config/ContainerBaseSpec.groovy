package com.example.twitter.config

import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.lifecycle.Startables
import org.testcontainers.spock.Testcontainers
import spock.lang.Specification

@Testcontainers
class ContainerBaseSpec extends Specification {
    static final GenericContainer mongo

    static {
        mongo = new GenericContainer("mongo:6.0.3")
                .withExposedPorts(27017)
                .waitingFor(Wait.forLogMessage(".*Waiting for connections.*\\n", 1))
    }

    static {
        Startables.deepStart(mongo).join();
    }

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", mongo::getHost)
        registry.add("spring.data.mongodb.port", mongo::getFirstMappedPort)
        registry.add("spring.data.mongodb.database", () -> "app_db")
    }
}
