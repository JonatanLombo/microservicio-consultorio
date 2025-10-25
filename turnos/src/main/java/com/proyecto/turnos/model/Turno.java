package com.proyecto.turnos.model;

import java.time.LocalDate;
import com.proyecto.turnos.dto.TurnoSaveDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Entidad JPA que representa un turno asignadoa a un paciente dentro del sistema.
 *
 * <p>Esta clase forma parte del modelo de datos y define la estructura 
 * de la tabla correspondiente en la base de datos. Cada turno contiene 
 * la información del tratamiento, la fecha en la que se realizará 
 * y el nombre del paciente asociado, obtenido desde el microservicio de pacientes.</p>
 *
 * <p>Incluye validaciones para garantizar que los campos esenciales 
 * sean proporcionados antes de realizar operaciones de persistencia.</p>
 *
 * @see TurnoSaveDTO
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Turno {


    /**
     * Identificador único del turno.
     *
     * <p>Se genera automáticamente mediante la estrategia 
     * {@link GenerationType#IDENTITY} al persistir la entidad.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTurno;

    /**
     * Fecha en la que se programó el turno.
     *
     * <p>Debe ser una fecha válida y no puede ser nula.</p>
     */
    @NotNull(message = "La fecha es obligatoria")
    @Temporal(TemporalType.DATE)
    private LocalDate fecha;

    /**
     * Nombre del tratamiento asociado al turno.
     *
     * <p>Campo obligatorio que indica el tipo de atención 
     * o procedimiento que se realizará.</p>
     */
    @NotBlank(message = "Indicar el tratamiento es obligatorio")
    private String tratamiento;

    /**
     * Nombre completo del paciente al que pertenece el turno.
     *
     * <p>Este dato no se ingresa manualmente, sino que es obtenido 
     * desde el microservicio de pacientes al momento de crear el turno.</p>
     */
    @NotBlank(message = "El nombre es obligatorio")
    private String nombrePaciente; 

}
