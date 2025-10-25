package com.proyecto.pacientes.service;

import java.util.List;
import java.util.Optional;
import com.proyecto.pacientes.dto.PacienteUpdateDTO;
import com.proyecto.pacientes.model.Paciente;

/**
 * Interfaz que define las operaciones del servicio relacionadas con la entidad {@link Paciente}.
 *
 * <p>Proporciona los métodos necesarios para gestionar el ciclo de vida de los pacientes,
 * incluyendo creación, consulta, actualización y eliminación de registros.</p>
 *
 * <p>Su implementación concreta delega la persistencia en el repositorio correspondiente
 * y aplica la lógica de negocio necesaria antes de interactuar con la base de datos.</p>
 */
public interface IPacienteService {

    /**
     * Obtiene la lista completa de pacientes registrados en el sistema.
     *
     * @return una lista de objetos {@link Paciente}.
     */
    public List<Paciente> getPacientes();


    /**
     * Crea un nuevo registro de paciente.
     *
     * <p>Valida los datos del objeto recibido antes de persistirlo.</p>
     *
     * @param paci objeto {@link Paciente} que contiene los datos a guardar.
     */
    public void savePaciente(Paciente paci);


    /**
     * Elimina el registro de un paciente existente.
     *
     * @param id identificador único del paciente a eliminar.
     */
    public void deletePaciente(Long id);


    /**
     * Busca un paciente por su identificador único.
     *
     * @param id identificador del paciente a buscar.
     * @return un {@link Optional} que contiene el paciente encontrado, o vacío si no existe.
     */
    public Optional<Paciente> findPaciente(Long id);


    /**
     * Busca un paciente por su número de documento.
     * 
     * <p>Este método no solo se utiliza para la consulta directa de pacientes,
     * sino también como parte del flujo de integración con el servicio de turnos.
     * A través de este método, se recuperan los datos de un paciente existente
     * para asignarle un nuevo turno sin necesidad de volver a registrarlo.</p>
     * 
     * @param numDocumento número de documento del paciente.
     * @return un {@link Optional} que contiene el paciente encontrado,
     *         o vacío si no existe.
     */
    public Optional<Paciente> findPacienteByDoc(String numDocumento);


    /**
     * Actualiza parcialmente los datos de un paciente existente.
     *
     * <p>Solo se modifican los campos no nulos o no vacíos del objeto {@link PacienteUpdateDTO}.
     * Si el paciente no existe, la operación no realiza cambios.</p>
     *
     * @param id identificador único del paciente a actualizar.
     * @param paciDTO objeto {@link PacienteUpdateDTO} con los campos a modificar.
     */
    public void editPaciente(Long id, PacienteUpdateDTO paciDTO);

}
