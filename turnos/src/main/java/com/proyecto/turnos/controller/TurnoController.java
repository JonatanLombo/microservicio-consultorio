package com.proyecto.turnos.controller;

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
import com.proyecto.turnos.dto.TurnoSaveDTO;
import com.proyecto.turnos.dto.TurnoUpdateDTO;
import com.proyecto.turnos.model.Turno;
import com.proyecto.turnos.service.ITurnoService;

import jakarta.validation.Valid;


/**
 * Controlador REST que gestiona las operaciones CRUD sobre la entidad {@link Turno}.
 *
 * <p>Esta clase expone los endpoints HTTP para crear, consultar, actualizar y eliminar
 * registros de turnos, delegando la lógica de negocio a {@link ITurnoService}.
 * Además, implementa validación automática de datos mediante {@code @Valid} y 
 * delega el manejo de errores al {@code GlobalExceptionHandler}.</p>
 *
 * @author Jonatan Lombo
 * @version 1.0
 * @see Turno
 * @see ITurnoService
 */
@RestController
@RequestMapping("/turnos")
public class TurnoController {

    /**
     * Servicio que contiene la lógica de negocio relacionada con la entidad {@link Turno}.
     */
    @Autowired
    private ITurnoService turnoServ;

    /**
     * Crea un nuevo registro de Turno.
     *
     * <p>Válida automáticamente los campos del objeto {@link Turno} gracias a la anotación
     * {@code @Valid}. Si falta algún campo obligatorio, se captura mediante el 
     * {@link GlobalExceptionHandler} y se retorna un código {@code 400 BAD_REQUEST}.</p>
     *
     * @param turnoDTO Objeto {@link TurnoSaveDTO} con los datos a registrar.
     * @return {@link ResponseEntity} con el resultado de la operación.
     */
    @PostMapping("/crear")
    public ResponseEntity<?> saveTurno(@Valid @RequestBody TurnoSaveDTO turnoDTO){
        turnoServ.saveTurno(turnoDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
        .body("Turno creado exitosamente");
    }

    /**
     * Obtiene el listado completo de turno registrados.
     *
     * <p>Si la lista está vacía o no contiene registros válidos, se retorna un código
     * {@code 404 NOT_FOUND} con un mensaje informativo.</p>
     *
     * @return {@link ResponseEntity} que contiene la lista de turno o un mensaje de error.
     */
    @GetMapping("/traer")
    public ResponseEntity<?> traerTurno(){
        List<Turno> Turno = turnoServ.getTurnos();

        if(Turno.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No se encontraron registros");
        }
        return ResponseEntity.ok().body(Turno);
    }

    /**
     * Busca un turno por su identificador único.
     *
     * <p>Si el turno no existe, se devuelve un código {@code 404 NOT_FOUND}.</p>
     *
     * @param id identificador del turno a buscar.
     * @return {@link ResponseEntity} con el turno encontrado o un mensaje de error.
     */
    @GetMapping("/traer/{id}")
    public ResponseEntity<?> findTurno(@PathVariable Long id){
        return turnoServ.findTurno(id)
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No se encontró el turno con Id " + id));
    }

    /**
     * Elimina un turno por su identificador único.
     *
     * <p>Si el id no existe, el servicio puede lanzar una excepción personalizada
     * {@code TurnoNotFoundException}, la cual será gestionada por el 
     * {@code GlobalExceptionHandler} devolviendo un {@code 404 NOT_FOUND}.</p>
     *
     * @param id identificador del turno a eliminar.
     * @return {@link ResponseEntity} con el resultado de la operación.
     */
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> deleteTurno(@PathVariable Long id){
        turnoServ.deleteTurno(id);
        return ResponseEntity.ok("Turno eliminado correctamente"); 
    }

    /**
     * Actualiza parcialmente los datos de un turno existente.
     *
     * <p>Este endpoint permite modificar uno o varios campos del turno sin requerir
     * que se envíen todos los atributos. Utiliza el objeto {@link TurnoUpdateDTO}
     * para recibir únicamente los datos que se desean actualizar.</p>
     *
     * <p>Si el turno con el {@code id} especificado no existe, el servicio no realiza
     * ninguna acción. En caso de éxito, retorna un código {@code 200 OK} con un mensaje
     * confirmando la actualización.</p>
     *
     * @param id identificador único del turno a actualizar.
     * @param TurnDTO objeto {@link TurnoUpdateDTO} que contiene los nuevos valores
     *                de los campos a modificar (por ejemplo: nombre, apellido o teléfono).
     * @return {@link ResponseEntity} con un mensaje de confirmación o error.
     */
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editTurno(@PathVariable Long id, @RequestBody TurnoUpdateDTO TurnDTO){
        turnoServ.editTurno(id, TurnDTO);
        return ResponseEntity.ok("Turno con Id " + id + " actualizado correctamente");
    }

}
