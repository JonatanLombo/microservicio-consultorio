package com.proyecto.turnos.service;

import com.proyecto.turnos.dto.TurnoSaveDTO;
import com.proyecto.turnos.dto.TurnoUpdateDTO;
import com.proyecto.turnos.model.Paciente;
import com.proyecto.turnos.model.Turno;
import com.proyecto.turnos.repository.IPacientesAPI;
import com.proyecto.turnos.repository.ITurnoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Pruebas unitarias para la clase {@link TurnoService}.
 *
 * <p>Se utilizan mocks de las dependencias externas:
 * <ul>
 *   <li>{@link IPacientesAPI} para simular el consumo del microservicio de pacientes.</li>
 *   <li>{@link ITurnoRepository} para simular la capa de persistencia.</li>
 * </ul>
 *
 * <p>Estas pruebas verifican el comportamiento del servicio en los diferentes
 * escenarios del CRUD de turnos sin depender de la base de datos real ni del
 * microservicio remoto.</p>
 *
 */
@ExtendWith(MockitoExtension.class)
public class TurnoServiceTest {

    @Mock
    private ITurnoRepository turnoRepo;

    @Mock
    private IPacientesAPI pacienteAPI;

    @InjectMocks
    private TurnoService turnoServ;

    /**
     * Verifica que guard correctamente un turno cuando el paciente
     * existe en el microservicio remoto.
     */
    @Test
    public void saveTurnoTests(){
        MockitoAnnotations.openMocks(this);

        TurnoSaveDTO turnoDTO = new TurnoSaveDTO();
        turnoDTO.setNumDocumento("123456789");
        turnoDTO.setTratamiento("Medicina General");
        turnoDTO.setFecha(LocalDate.of(2025,10,20));
        Paciente paciente = new Paciente(null, "123456789","Alejandra","Martinez", LocalDate.of(1997,6,24),"3104698520");
        Mockito.when(pacienteAPI.getPacienteByDocumento(turnoDTO.getNumDocumento())).thenReturn(paciente);
        turnoServ.saveTurno(turnoDTO);

        Mockito.verify(turnoRepo, Mockito.times(1)).save(any(Turno.class));
        Mockito.verify(pacienteAPI, Mockito.times(1)).getPacienteByDocumento("123456789");
    }

    /**
     * Verifica que lance una excepción
     * cuando el paciente no existe en el microservicio remoto.
     */
    @Test
    public void saveTurnoPacienteInexistente() {
        MockitoAnnotations.openMocks(this);

        TurnoSaveDTO turnoDTO = new TurnoSaveDTO();
        turnoDTO.setNumDocumento("987654321");
        turnoDTO.setTratamiento("Medicina General");
        turnoDTO.setFecha(LocalDate.of(2025,6,10));

        Mockito.when(pacienteAPI.getPacienteByDocumento(turnoDTO.getNumDocumento())).thenReturn(null);
        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> turnoServ.saveTurno(turnoDTO));

        Assertions.assertEquals("No se encontró el paciente con documento 987654321", exception.getMessage());
        Mockito.verify(turnoRepo, Mockito.never()).save(any(Turno.class));
    }

    /**
     * Verifica que {@code getTurnos()} retorne correctamente la lista de turnos
     * obtenida del repositorio y que los datos coincidan con los esperados.
     */
    @Test
    public void getTurnosTest(){
        List<Turno> listaTurnos = this.nuevosTurnos();

        Mockito.when(turnoRepo.findAll()).thenReturn(listaTurnos);
        List<Turno> resultado = turnoServ.getTurnos();

        Mockito.verify(turnoRepo).findAll();
        Mockito.verifyNoMoreInteractions(turnoRepo);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(2, resultado.size());
        Assertions.assertTrue(resultado.contains(listaTurnos.get(0)));
        Assertions.assertTrue(resultado.contains(listaTurnos.get(1)));
    }

    /**
     * Verifica que {@code findTurno()} retorne el turno correcto cuando el id existe
     * y que las llamadas al repositorio sean las esperadas.
     */
    @Test
    public void findTurnoTest() {
        List<Turno> listaTurnos = this.nuevosTurnos();
        Turno turnoEsperado = listaTurnos.get(1);
        Mockito.when(turnoRepo.findById(turnoEsperado.getIdTurno()))
                .thenReturn(Optional.of(turnoEsperado));

        Optional<Turno> turnoFinal = turnoServ.findTurno(turnoEsperado.getIdTurno());
        Mockito.verify(turnoRepo).findById(turnoEsperado.getIdTurno());
        Mockito.verifyNoMoreInteractions(turnoRepo);

        Assertions.assertTrue(turnoFinal.isPresent());
        Turno turno = turnoFinal.get();
        Assertions.assertEquals(turnoEsperado.getIdTurno(), turno.getIdTurno() );
        Assertions.assertEquals(turnoEsperado.getTratamiento(), turno.getTratamiento() );
    }

    /**
     * Verifica que {@code deleteTurno()} elimine correctamente un turno existente
     * cuando el id es válido y que no se produzcan interacciones adicionales.
     */
    @Test
    public void deleteTurnoTest() {
        List<Turno> listaTurnos = this.nuevosTurnos();
        Turno turno = listaTurnos.get(1);
        Mockito.when(turnoRepo.findById(turno.getIdTurno()))
                .thenReturn(Optional.of(turno));
        turnoServ.deleteTurno(turno.getIdTurno());
        Mockito.verify(turnoRepo).findById(turno.getIdTurno());
        Mockito.verify(turnoRepo).delete(turno);
        Mockito.verifyNoMoreInteractions(turnoRepo);
    }

    /**
     * Verifica que {@code deleteTurno()} lance una excepción cuando
     * se intenta eliminar un turno inexistente.
     */
    @Test
    public void deleteTurnoNotFoundTest(){
        Long idInexistente = 100L;
        Mockito.when(turnoRepo.findById(idInexistente))
                .thenReturn(Optional.empty());

        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> turnoServ.deleteTurno(idInexistente)
        );

        Assertions.assertEquals("No se encontró el turno con id 100", ex.getMessage());
        Mockito.verify(turnoRepo).findById(idInexistente);
        Mockito.verifyNoMoreInteractions(turnoRepo);
    }


    /**
     * Verifica que {@code editTurno()} actualice únicamente los campos no nulos y no vacíos
     * del DTO, y que el repositorio guarde el turno actualizado correctamente.
     */
    @Test
    public void editTurnoTest() {
        List<Turno> listaTurnos = this.nuevosTurnos();
        Turno turno = listaTurnos.get(1);
        TurnoUpdateDTO turnoDTO = new TurnoUpdateDTO();
        turnoDTO.setFecha(LocalDate.of(2025,11,25));
        turnoDTO.setTratamiento("Medicina interna");
        Mockito.when(turnoRepo.findById(turno.getIdTurno()))
                .thenReturn(Optional.of(turno));
        turnoServ.editTurno(turno.getIdTurno(), turnoDTO);

        Mockito.verify(turnoRepo).findById(turno.getIdTurno());
        Mockito.verify(turnoRepo).save(turno);

        Assertions.assertEquals(LocalDate.of(2025,11,25), turno.getFecha());
        Assertions.assertEquals("Medicina interna", turno.getTratamiento());
        Mockito.verifyNoMoreInteractions(turnoRepo);
    }

    /**
     * Verifica que {@code editTurno()} lance una excepción cuando se intenta
     * actualizar un turno con un ID inexistente en la base de datos.
     */
    @Test
    public void editTurnoNotFoundTest(){
        Long idInexistente = 100L;
        TurnoUpdateDTO turnoDTO = new TurnoUpdateDTO();
        turnoDTO.setTratamiento("Medicina interna");

        Mockito.when(turnoRepo.findById(idInexistente))
                .thenReturn(Optional.empty());

        EntityNotFoundException ex = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> turnoServ.editTurno(idInexistente, turnoDTO)
        );

        Assertions.assertEquals("No se encontró el turno con Id 100", ex.getMessage());
        Mockito.verify(turnoRepo).findById(idInexistente);
        Mockito.verifyNoMoreInteractions(turnoRepo);
    }

    //_______________________________________________________________________________________
    /**
     * Crea una lista de turnos de ejemplo para reutilizar en los distintos tests.
     *
     * @return lista con dos instancias de {@link Turno}.
     */
    public List<Turno> nuevosTurnos(){
        Turno t1 = new Turno(1L, LocalDate.of(1997,6,24),"Medicina General","Alejandra Ramos");
        Turno t2 = new Turno(2L, LocalDate.of(2000,7,10),"Medicina General","Catalina Mendez");

        List<Turno> listaTurnos = List.of(t1,t2);
        return listaTurnos;
    }

}

