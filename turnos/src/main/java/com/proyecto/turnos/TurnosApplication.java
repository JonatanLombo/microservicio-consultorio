package com.proyecto.turnos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Punto de entrada principal para el microservicio de turnos.
 *
 * <p>Este microservicio gestiona la creación, consulta y asignación
 * de turnos a pacientes. Se comunica con el microservicio de pacientes
 * mediante clientes Feign y se registra en el servidor de descubrimiento
 * (Eureka) para facilitar la integración con otros servicios del sistema.</p>
 *
 * <p>La anotación {@link EnableFeignClients} habilita el uso de clientes Feign
 * para realizar llamadas HTTP declarativas a otros microservicios.
 * La anotación {@link EnableDiscoveryClient} permite registrar este servicio
 * en el servidor Eureka.</p>
 *
 * <p>La clase utiliza {@link SpringBootApplication} para habilitar la
 * configuración automática de Spring Boot y arrancar el contexto de aplicación.</p>
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class TurnosApplication {

	/**
     * Método principal que inicia la aplicación de Spring Boot.
     *
     * @param args argumentos de línea de comandos pasados al iniciar la aplicación.
     */
	public static void main(String[] args) {
		SpringApplication.run(TurnosApplication.class, args);
	}

}
