package com.proyecto.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Clase principal que inicia el servidor de Eureka.
 *
 * <p>Este servicio actúa como un registro central donde los microservicios 
 * pueden registrarse y descubrirse entre sí dinámicamente.</p>
 *
 * <p>La anotación {@link EnableEurekaServer} habilita las capacidades de 
 * servidor de Eureka en esta aplicación.</p>
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {

	/**
     * Método principal que arranca la aplicación Spring Boot.
     *
     * @param args argumentos de línea de comandos.
     */
	public static void main(String[] args) {
		SpringApplication.run(EurekaApplication.class, args);
	}

}
