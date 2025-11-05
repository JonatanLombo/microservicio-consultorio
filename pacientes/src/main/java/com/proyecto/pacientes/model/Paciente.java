package com.proyecto.pacientes.model;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Entidad JPA que representa un paciente dentro del sistema de gestión clínica.
 *
 * <p>Define los atributos persistentes de un paciente y aplica validaciones
 * mediante anotaciones de Jakarta Validation para garantizar la integridad
 * de los datos en operaciones de creación.</p>
 *
 * <p>Durante las actualizaciones parciales, los campos se validan a través
 * del uso de un DTO específico ({@code PacienteUpdateDTO}), permitiendo 
 * modificar solo los valores requeridos sin invalidar los demás.</p>
 *
 * <p>Esta clase se encuentra mapeada a una tabla de base de datos y 
 * es administrada por el contexto de persistencia de JPA.</p>
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Paciente {

    /**
     * Identificador único del paciente.
     *
     * <p>Generado automáticamente mediante la estrategia 
     * {@link GenerationType#IDENTITY} al persistir la entidad.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPaciente;

    /**
     * Número de documento del paciente.
     *
     * <p>Campo obligatorio y único que identifica al paciente dentro del sistema.</p>
     */
    @NotBlank(message = "El número de documento es obligatorio")
    private String numDocumento;

    /**
     * Nombre del paciente.
     *
     * <p>No puede estar vacío y es requerido para el registro de la entidad.</p>
     * <p>No puede superar los 30 caracteres.</p>
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 40, message = "El nombre no puede superar los 30 caracteres")
    private String nombre;

    /**
     * Apellido del paciente.
     *
     * <p>Campo obligatorio que complementa la identificación del paciente.</p>
     * <p>No puede superar los 30 caracteres.</p>
     */
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 40, message = "El apellido no puede superar los 30 caracteres")
    private String apellido;
    
    /**
     * Fecha de nacimiento del paciente.
     *
     * <p>Debe ser anterior a la fecha actual. Se utiliza para validar la edad 
     * y otros procesos relacionados.</p>
     */
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaNac;

    /**
     * Número de teléfono de contacto del paciente.
     *
     * <p>Campo obligatorio, utilizado para la comunicación con el paciente 
     * y registro administrativo.</p>
     */
    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono; 


}
