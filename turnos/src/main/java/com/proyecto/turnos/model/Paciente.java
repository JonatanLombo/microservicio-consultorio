package com.proyecto.turnos.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Clase de modelo que representa los datos de un paciente dentro del microservicio de turnos.
 *
 * <p>Esta clase no se persiste en la base de datos local. Su propósito es servir como 
 * estructura temporal para manipular la información de un paciente obtenida desde 
 * el microservicio de pacientes, facilitando las operaciones relacionadas con los turnos.</p>
 *
 * <p>Generalmente, esta clase se utiliza como parte de las respuestas o solicitudes
 * dentro de los endpoints del servicio de turnos, permitiendo la integración 
 * entre microservicios mediante intercambio de datos (por REST o mensajería).</p>
 *
 * <p><strong>No debe incluir anotaciones JPA</strong> como {@code @Entity} o {@code @Table},
 * ya que no forma parte del modelo persistente de este servicio.</p>
 *
 * @see Turno
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Paciente {

    /** Identificador único del paciente proveniente del microservicio de pacientes. */
    private Long idPaciente;

    /** Número de documento con el que el paciente está registrado. */
    private String numDocumento;

    /** Nombre del paciente. */
    private String nombre; 
    
    /** Apellido del paciente. */
    private String apellido;

    /** Fecha de nacimiento del paciente. */
    private LocalDate fechaNac;

    /** Número de teléfono asociado al paciente. */
    private String telefono; 

}
