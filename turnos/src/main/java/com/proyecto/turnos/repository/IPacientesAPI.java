package com.proyecto.turnos.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.proyecto.turnos.model.Paciente;

/**
 * Cliente Feign para la comunicación con el microservicio de pacientes.
 *
 * <p>Esta interfaz define los métodos que permiten al microservicio de turnos
 * consumir los endpoints expuestos por el microservicio de pacientes
 * mediante llamadas HTTP declarativas a través de Feign.</p>
 *
 * <p>El valor de {@code name} debe coincidir exactamente con el nombre
 * con el que el servicio de pacientes está registrado en el servidor Eureka,
 * en este caso: <strong>"PACIENTES"</strong>.</p>
 *
 * <p>Feign se encarga de construir las solicitudes HTTP y deserializar las
 * respuestas en objetos del modelo correspondiente, como {@link Paciente}.</p>
 *
 * @see Paciente
 */
@FeignClient(name = "PACIENTES")
public interface IPacientesAPI {

    /**
     * Obtiene la información de un paciente registrado en el microservicio de pacientes
     * a partir de su número de documento.
     *
     * <p>Este método realiza una solicitud GET al endpoint remoto
     * <code>/pacientes/traer/documento/{numDocumento}</code> del servicio de pacientes,
     * retornando los datos correspondientes al paciente solicitado.</p>
     *
     * @param numDocumento número de documento del paciente a buscar.
     * @return un objeto {@link Paciente} con la información obtenida del microservicio remoto.
     */
    @GetMapping("/pacientes/traer/documento/{numDocumento}")
    Paciente getPacienteByDocumento(@PathVariable("numDocumento") String numDocumento);

}
