package com.proyecto.turnos.dto;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.proyecto.turnos.model.Turno;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * DTO (Data Transfer Object) utilizado para la creación de nuevos turnos
 * asociados a pacientes en el sistema.
 *
 * <p>Este objeto transporta los datos necesarios desde la capa de presentación
 * hacia la capa de servicio, permitiendo registrar un turno sin exponer la entidad
 * completa {@link Turno}.</p>
 *
 * <p>Incluye validaciones mediante anotaciones de Jakarta Validation
 * para asegurar que los datos requeridos sean proporcionados antes de
 * ejecutar la operación de persistencia.</p>
 *
 * @see Turno
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TurnoSaveDTO {

    /**
     * Fecha en la que se agenda el turno.
     * <p>Campo obligatorio validado mediante {@link NotNull}.
     * Se espera un valor compatible con {@link LocalDate}.</p>
     */
    @NotNull(message = "La fecha es obligatoria")
    @Temporal(TemporalType.DATE)
    private LocalDate fecha;

    /**
     * Nombre o descripción del tratamiento que se aplicará durante el turno.
     * <p>Campo obligatorio validado mediante {@link NotBlank}.</p>
     */
    @NotBlank(message = "Indicar el tratamiento es obligatorio")
    private String tratamiento;

    /**
     * Número de documento del paciente asociado al turno.
     * <p>Campo obligatorio validado mediante {@link NotBlank}.
     * Este valor se utiliza para consultar los datos del paciente
     * en el microservicio correspondiente antes de registrar el turno.</p>
     */
    @NotBlank(message = "El número de documento es obligatorio")
    private String numDocumento;
}
