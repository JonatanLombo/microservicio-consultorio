package com.proyecto.pacientes.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto.pacientes.dto.PacienteUpdateDTO;
import com.proyecto.pacientes.model.Paciente;
import com.proyecto.pacientes.service.IPacienteService;
import jakarta.validation.Valid;

/**
 * Controlador REST que gestiona las operaciones CRUD sobre la entidad {@link Paciente}.
 *
 * <p>Esta clase expone los endpoints HTTP para crear, consultar, actualizar y eliminar
 * registros de pacientes, delegando la lógica de negocio a {@link IPacienteService}.
 * Además, implementa validación automática de datos mediante {@code @Valid} y 
 * delega el manejo de errores al {@code GlobalExceptionHandler}.</p>
 *
 * @author Jonatan Lombo
 * @version 1.0
 * @see Paciente
 * @see IPacienteService
 */
@RequestMapping("/pacientes")
@RestController
public class PacienteController {

    /**
     * Servicio que contiene la lógica de negocio relacionada con la entidad {@link Paciente}.
     */
    @Autowired
    private IPacienteService pacienteServ;


    /**
     * Crea un nuevo registro de paciente.
     *
     * <p>Valida automáticamente los campos del objeto {@link Paciente} gracias a la anotación
     * {@code @Valid}. Si falta algún campo obligatorio, se captura mediante el 
     * {@link GlobalExceptionHandler} y se retorna un código {@code 400 BAD_REQUEST}.</p>
     *
     * @param paci Objeto {@link Paciente} con los datos a registrar.
     * @return {@link ResponseEntity} con el resultado de la operación.
     */
    @PostMapping("/crear")
    public ResponseEntity<?> savePaciente(@Valid @RequestBody Paciente paci){
                pacienteServ.savePaciente(paci);
                return ResponseEntity.status(HttpStatus.CREATED)
                .body("Paciente creado exitosamente");
    }


    /**
     * Obtiene el listado completo de pacientes registrados.
     *
     * <p>Si la lista está vacía o no contiene registros válidos, se retorna un código
     * {@code 404 NOT_FOUND} con un mensaje informativo.</p>
     *
     * @return {@link ResponseEntity} que contiene la lista de pacientes o un mensaje de error.
     */
    @GetMapping("/traer")
    public ResponseEntity<?> traerPacientes(){
        List<Paciente> pacientes = pacienteServ.getPacientes();

        if(pacientes.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No se encontraron registros");
        }
        return ResponseEntity.ok().body(pacientes);
    }


    /**
     * Busca un paciente por su identificador único.
     *
     * <p>Si el paciente no existe, se devuelve un código {@code 404 NOT_FOUND}.</p>
     *
     * @param id Identificador del paciente a buscar.
     * @return {@link ResponseEntity} con el paciente encontrado o un mensaje de error.
     */
    @GetMapping("/traer/{id}")
    public ResponseEntity<?> findPaciente(@PathVariable Long id){
        return pacienteServ.findPaciente(id)
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No se encontró el paciente con Id " + id));
    }


    /**
     * Busca un paciente por su número de documento.
     *
     * <p>Este endpoint no solo permite consultar los datos del paciente, sino que también
     * actúa como punto de integración con el servicio de turnos. A través de esta ruta,
     * el servicio de turnos puede obtener los datos del paciente existente para crear y
     * asociar un nuevo turno.</p>
     * 
     * <p>Si el paciente no existe, se devuelve un código {@code 404 NOT_FOUND}.</p>
     *
     * @param nomDocumento número de documento del paciente a buscar.
     * @return {@link ResponseEntity} con el paciente encontrado o un mensaje de error.
     */
    @GetMapping("/traer/documento/{numDocumento}")
    public ResponseEntity<?> findPaciente(@PathVariable String numDocumento){
        return pacienteServ.findPacienteByDoc(numDocumento)
            .<ResponseEntity<?>>map(paciente -> ResponseEntity.ok().body(paciente))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No se encontró el paciente con Id " + numDocumento));
    }


    /**
     * Elimina un paciente por su identificador único.
     *
     * <p>Si el id no existe, el servicio puede lanzar una excepción personalizada
     * {@code PacienteNotFoundException}, la cual será gestionada por el 
     * {@code GlobalExceptionHandler} devolviendo un {@code 404 NOT_FOUND}.</p>
     *
     * @param id identificador del paciente a eliminar.
     * @return {@link ResponseEntity} con el resultado de la operación.
     */
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> deletePaciente(@PathVariable Long id){
        pacienteServ.deletePaciente(id);
        return ResponseEntity.ok("Paciente eliminado correctamente"); 
    }


    /**
     * Actualiza parcialmente los datos de un paciente existente.
     *
     * <p>Este endpoint permite modificar uno o varios campos del paciente sin requerir
     * que se envíen todos los atributos. Utiliza el objeto {@link PacienteUpdateDTO}
     * para recibir únicamente los datos que se desean actualizar.</p>
     *
     * <p>Si el paciente con el {@code id} especificado no existe, el servicio no realiza
     * ninguna acción. En caso de éxito, retorna un código {@code 200 OK} con un mensaje
     * confirmando la actualización.</p>
     *
     * @param id identificador único del paciente a actualizar.
     * @param paciDTO objeto {@link PacienteUpdateDTO} que contiene los nuevos valores
     *                de los campos a modificar (por ejemplo: nombre, apellido o teléfono).
     * @return {@link ResponseEntity} con un mensaje de confirmación o error.
     */
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editPaciente(@PathVariable Long id, @RequestBody PacienteUpdateDTO paciDTO){
        pacienteServ.editPaciente(id, paciDTO);
        return ResponseEntity.ok("Paciente con Id " + id + " actualizado correctamente");
    }



}