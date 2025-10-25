package com.proyecto.turnos.service;

import java.util.List;
import java.util.Optional;
import com.proyecto.turnos.dto.TurnoSaveDTO;
import com.proyecto.turnos.dto.TurnoUpdateDTO;
import com.proyecto.turnos.model.Turno;

import jakarta.persistence.EntityNotFoundException;

/**
 * Interfaz que define las operaciones del servicio relacionadas con la entidad {@link Turno}.
 *
 * <p>Proporciona los métodos necesarios para gestionar el ciclo de vida de los turnos,
 * incluyendo creación, consulta, actualización y eliminación de registros.</p>
 *
 * <p>Su implementación concreta delega la persistencia en el repositorio correspondiente
 * y aplica la lógica de negocio necesaria antes de interactuar con la base de datos.</p>
 */
public interface ITurnoService {

    /**
     * Obtiene la lista completa de turnos registrados en el sistema.
     *
     * @return una lista de objetos {@link Turno}.
     */
    public List<Turno> getTurnos();


    /**
     * Crea un nuevo turno asociado a un paciente existente.
     *
     * <p>Valida que el paciente exista consultando el microservicio de pacientes
     * antes de registrar el turno. Si el paciente no existe, la operación no se completa.</p>
     *
     * @param turnDTO objeto con los datos necesarios para registrar el turno.
     * @throws EntityNotFoundException si no se encuentra un paciente con el documento indicado.
     */
    public void saveTurno(TurnoSaveDTO turnDTO);


    /**
     * Elimina el registro de un turno existente.
     *
     * @param id identificador único del turno a eliminar.
     */
    public void deleteTurno(Long id);


    /**
     * Busca un turno por su identificador único.
     *
     * @param id identificador del turno a buscar.
     */
    public Optional<Turno> findTurno(Long id);


    /**
     * Actualiza parcialmente los datos de un paciente existente aplicando un enfoque funcional.
     *
     * <p>Solo se actualizan los campos no nulos y no vacíos del DTO. Si el paciente no existe,
     * no se realiza ninguna acción.</p>
     *
     * @param id identificador único del turno a actualizar.
     * @param turnDTO objeto {@link TurnoUpdateDTO} con los campos modificados.
     */
    public void editTurno(Long id, TurnoUpdateDTO turnDTO);

}
