package com.proyecto.turnos.dto;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.proyecto.turnos.model.Turno;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) utilizado para realizar actualizaciones parciales
 * sobre la entidad {@link Turno}.
 *
 * <p>Contiene únicamente los campos que pueden ser modificados en una
 * operación de actualización. Los valores nulos o vacíos no serán
 * considerados al momento de persistir los cambios.</p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class TurnoUpdateDTO {

    /** Fecha actualizada del turno  */
    @Temporal(TemporalType.DATE)
    private LocalDate fecha;

    /** Tratamiento actualizado del turno  */
    private String tratamiento;

}
