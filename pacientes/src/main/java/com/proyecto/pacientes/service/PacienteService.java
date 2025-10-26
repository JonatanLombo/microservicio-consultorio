package com.proyecto.pacientes.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.proyecto.pacientes.dto.PacienteUpdateDTO;
import com.proyecto.pacientes.model.Paciente;
import com.proyecto.pacientes.repository.IPacienteRepository;

/**
 * Implementación de la interfaz de servicio {@link IPacienteService} que realiza las operaciones CRUD
 * sobre la entidad {@link Paciente}.
 *
 * <p>Utiliza {@link IPacienteRepository} para interactuar con la base de datos.</p>
 *
 * @author Jonatan
 * @version 1.0
 * @see Paciente
 * @see IPacienteRepository
 * @see IPacienteService
 */
@Service
public class PacienteService implements IPacienteService {

    /**
     * Repositorio usado para acceder a los datos de los pacientes
     */
    @Autowired
    private IPacienteRepository pacienteRepo;


    /**
     * Guarda un nuevo registro de paciente en la base de datos.
     * 
     * <p>El registro no se almacena si el objeto recibido es {@code null}.</p>
     * 
     * @param paci Objeto {@link Paciente} a registrar.
     */
    @Override
    public void savePaciente(Paciente paci) {
        Optional.ofNullable(paci)              
            .ifPresent(pacienteRepo::save);         
    }


    /**
     * Obtiene el listado de todos pacientes registrados en la base de datos.
     * 
     * <p>Los registros null son omitidos para garantizar la integridad del listado.</p>
     * 
     * @return lista de pacientes válidos (sin valores null).
     */
    @Override
    public List<Paciente> getPacientes() {
        return pacienteRepo.findAll().stream()     
            .filter(Objects::nonNull)             
            .toList();                              
    }
 

    /**
     * Elimina un nuevo registro de paciente por medio de id.
     * 
     * <p>No realiza ninguna acción si el id es {@code null}.</p>
     * 
     * @param id identificador del paciente a eliminar.
     */
    @Override
    public void deletePaciente(Long id) {            
        Optional.ofNullable(id)  
            .flatMap(pacienteRepo::findById)            
            .ifPresentOrElse(
                pacienteRepo::delete, 
                () -> { throw new NoSuchElementException("No se encontró el paciente con id " + id); }
            );
    }


    /**
     * Retorna un registro de paciente por Id.
     * 
     * <p>No realiza ninguna acción si el id no se encuentra registrado en BD.</p>
     * 
     * @param id identificador paciente a buscar.
     */
    @Override
    public Optional<Paciente> findPaciente(Long id) {
        return Optional.ofNullable(id)
            .flatMap(pacienteRepo::findById);
    }


    /**
     * Busca un paciente registrado por su número de documento.
     *
     * <p>Este método también es utilizado por otros servicios del sistema,
     * como el de turnos, para obtener la información del paciente y así
     * crear o asociar un turno sin duplicar registros.</p>
     *
     * @param numDocumento número de documento del paciente a buscar.
     * @return un {@link Optional} que contiene el paciente encontrado,
     *         o vacío si no existe.
     */ 
    @Override
    public Optional<Paciente> findPacienteByDoc(String numDocumento) {
        return Optional.ofNullable(numDocumento)
            .flatMap(pacienteRepo::findByDoc);     
    }


    /**
    * Actualiza parcialmente los datos de un paciente existente aplicando un enfoque funcional.
    *
    * <p>Solo se actualizan los campos no nulos y no vacíos del DTO. Si el paciente no existe,
    * no se realiza ninguna acción.</p>
    *
    * @param id  identificador del paciente a actualizar.
    * @param dto objeto {@link PacienteUpdateDTO} con los campos a modificar.
    */
    @Override
    public void editPaciente(Long id, PacienteUpdateDTO paciDTO) {
        Optional.ofNullable(id)
            .flatMap(pacienteRepo::findById)
            .map(actual -> {
                Optional.ofNullable(paciDTO.getNombre())
                    .filter(nombre -> !nombre.isBlank())
                    .ifPresent(actual::setNombre);

                Optional.ofNullable(paciDTO.getApellido())
                    .filter(apellido -> !apellido.isBlank())
                    .ifPresent(actual::setApellido);

                Optional.ofNullable(paciDTO.getTelefono())
                    .filter(telefono -> !telefono.isBlank())
                    .ifPresent(actual::setTelefono);

                return actual;
            })
            .ifPresent(pacienteRepo::save);
    }




}
