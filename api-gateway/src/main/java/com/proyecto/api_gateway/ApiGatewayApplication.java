package com.proyecto.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Clase principal del microservicio API Gateway.
 *
 * <p>Inicia el contexto de Spring Boot y habilita la integración con el
 * servidor de descubrimiento mediante la anotación {@link EnableDiscoveryClient}.
 * Este servicio actúa como un punto de entrada central que enruta las solicitudes
 * a otros microservicios registrados en Eureka.</p>
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

	/**
     * Método principal que arranca la aplicación de Spring Boot.
     *
     * @param args argumentos de línea de comandos.
     */
	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

}
