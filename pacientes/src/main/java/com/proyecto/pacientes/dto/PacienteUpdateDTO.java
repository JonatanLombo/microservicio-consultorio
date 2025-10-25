package com.proyecto.pacientes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.proyecto.pacientes.model.Paciente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) utilizado para realizar actualizaciones parciales
 * sobre la entidad {@link Paciente}.
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
public class PacienteUpdateDTO {

    /** Nombre actualizado del paciente.  */
    private String nombre;  

    /** Apellido actualizado del paciente.  */
    private String apellido;

    /** Número de teléfono actualizado del paciente. */
    private String telefono; 

}
