package com.proyecto.pacientes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Punto de entrada principal para el microservicio de pacientes.
 *
 * <p>Este microservicio gestiona la información de los pacientes y
 * expone endpoints REST para su consulta y administración.</p>
 *
 * <p>La anotación {@link EnableDiscoveryClient} permite que este servicio
 * se registre en el servidor de descubrimiento (por ejemplo, Eureka),
 * facilitando su integración con otros microservicios como el de turnos.</p>
 *
 * <p>La clase utiliza {@link SpringBootApplication} para habilitar la
 * configuración automática de Spring Boot y arrancar el contexto de aplicación.</p>
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PacientesApplication {

	/**
     * Método principal que inicia la aplicación de Spring Boot.
     *
     * @param args argumentos de línea de comandos pasados al iniciar la aplicación.
     */
	public static void main(String[] args) {
		SpringApplication.run(PacientesApplication.class, args);
	}

}
