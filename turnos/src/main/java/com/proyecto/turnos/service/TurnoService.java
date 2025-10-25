package com.proyecto.turnos.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.proyecto.turnos.dto.TurnoSaveDTO;
import com.proyecto.turnos.dto.TurnoUpdateDTO;
import com.proyecto.turnos.model.Turno;
import com.proyecto.turnos.repository.IPacientesAPI;
import com.proyecto.turnos.repository.ITurnoRepository;
import jakarta.persistence.EntityNotFoundException;

/**
 * Implementación de la interfaz de servicio {@link ITurnoService} que realiza las operaciones CRUD
 * sobre la entidad {@link Turno}.
 *
 * <p>Utiliza {@link ITurnoRepository} para interactuar con la base de datos.</p>
 *
 * @author Jonatan
 * @version 1.0
 * @see Turno
 * @see ITurnoRepository
 * @see ITurnoService
 */
@Service
public class TurnoService implements ITurnoService{

    /**
     * Repositorio usado para acceder a los datos de los turnos
     */
    @Autowired
    private ITurnoRepository turnoRepo;

    /**
     * Cliente Feign utilizado para comunicarse con el microservicio de pacientes.
     *
     * <p>Permite consultar la información de pacientes registrados mediante llamadas HTTP
     * al servicio remoto identificado como <strong>"PACIENTES"</strong> en el servidor Eureka.</p>
     *
     * @see IPacientesAPI
     */
    @Autowired
    private IPacientesAPI pacienteAPI;


    /**
     * Guarda un nuevo turno asociado a un paciente existente.
     *
     * <p>Este método consume el microservicio de pacientes a través del cliente Feign
     * {@link IPacientesAPI} para obtener los datos del paciente correspondiente al
     * número de documento recibido. Si el paciente existe, se construye un objeto
     * {@link Turno} con la información del tratamiento, fecha y nombre del paciente,
     * y se persiste en la base de datos local.</p>
     *
     * <p>Si el paciente no existe, se lanza una excepción {@link EntityNotFoundException}
     * indicando que no se encontró un registro asociado.</p>
     *
     * @param turnDTO objeto con los datos necesarios para registrar el turno.
     * @throws EntityNotFoundException si no se encuentra el paciente con el documento indicado.
     * @see IPacientesAPI
     * @see Turno
     */
    @Override
    public void saveTurno(TurnoSaveDTO turnDTO) {
        // Consumir el microservicio paciente
        Optional.ofNullable(pacienteAPI.getPacienteByDocumento(turnDTO.getNumDocumento()))
            .ifPresentOrElse(paci -> {
                // Crea el turno si paciente existe
                Turno turno = new Turno();
                turno.setFecha(turnDTO.getFecha());
                turno.setTratamiento(turnDTO.getTratamiento());
                turno.setNombrePaciente(paci.getNombre() +" "+ paci.getApellido());        
                turnoRepo.save(turno);
            },
            () -> {throw new EntityNotFoundException("No se encontró el paciente con documento " + turnDTO.getNumDocumento()); });
    }


    /**
     * Obtiene el listado de todos turnos registrados en la base de datos.
     * 
     * <p>Los registros null son omitidos para garantizar la integridad del listado.</p>
     * 
     * @return lista de turnos válidos (sin valores null).
     */
    @Override
    public List<Turno> getTurnos() {
        return turnoRepo.findAll().stream()     
            .filter(Objects::nonNull)             
            .toList(); 
    }


    /**
     * Retorna un registro de turno por id.
     * 
     * <p>No realiza ninguna acción si el id no se encuentra registrado en BD.</p>
     * 
     * @param id identificador turno a buscar.
     */
    @Override
    public Optional<Turno> findTurno(Long id) {
        return Optional.ofNullable(id)
            .flatMap(turnoRepo::findById);
    }


    /**
     * Elimina un nuevo registro de turno por medio de id.
     * 
     * <p>No realiza ninguna acción si el id es {@code null}.</p>
     * 
     * @param id identificador del turno a eliminar.
     */
    @Override
    public void deleteTurno(Long id) {
        Optional.ofNullable(id)  
            .flatMap(turnoRepo::findById)            
            .ifPresentOrElse(
                turnoRepo::delete, 
                () -> { throw new NoSuchElementException("No se encontró el paciente con id " + id); }
            );
    }


    /**
     * Actualiza parcialmente los datos de un paciente existente aplicando un enfoque funcional.
     *
     * <p>Solo se actualizan los campos no nulos y no vacíos del DTO. Si el paciente no existe,
     * no se realiza ninguna acción.</p>
     *
     * @param id identificador único del turno a actualizar.
     * @param turnDTO objeto {@link TurnoUpdateDTO} con los campos modificados.
     */
    @Override
    public void editTurno(Long id, TurnoUpdateDTO turnDTO) {
        Optional.ofNullable(id)
        .flatMap(turnoRepo::findById)
        .ifPresentOrElse(actual -> {
            Optional.ofNullable(turnDTO.getFecha())
                    .ifPresent(actual::setFecha);
            
            Optional.ofNullable(turnDTO.getTratamiento())
                    .filter(tratamiento -> !tratamiento.isBlank())
                    .ifPresent(actual::setTratamiento); 
            turnoRepo.save(actual);  
        },
        () -> {throw new EntityNotFoundException("No se encontró el turno con Id " + id); });
    }


}
