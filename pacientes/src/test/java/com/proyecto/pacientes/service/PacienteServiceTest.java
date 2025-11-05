package com.proyecto.pacientes.service;

import com.proyecto.pacientes.dto.PacienteUpdateDTO;
import com.proyecto.pacientes.model.Paciente;
import com.proyecto.pacientes.repository.IPacienteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Clase de pruebas unitarias para {@link PacienteService}.
 * <p>
 * Utiliza Mockito para simular el comportamiento del repositorio {@link IPacienteRepository}
 * y verificar la lógica de negocio sin depender de una base de datos real.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
public class PacienteServiceTest {

    @Mock
    private IPacienteRepository paciRepo;

    @InjectMocks
    private PacienteService paciServ;

    /**
     * Verifica que el método {@code savePaciente()} del servicio invoque correctamente
     * el método {@code save()} del repositorio.
     */
    @Test
    public void savePacienteTests(){
        Paciente paci = new Paciente(null, "123456789","Alejandra","Martinez", LocalDate.of(1997,6,24),"3104698520");
        paciServ.savePaciente(paci);
        Mockito.verify(paciRepo).save(paci);
        Mockito.verifyNoMoreInteractions(paciRepo);
    }

    /**
     * Verifica que {@code getPacientes()} retorne correctamente la lista de pacientes
     * obtenida del repositorio y que los datos coincidan con los esperados.
     */
    @Test
    public void getPacientesTest(){
        List<Paciente> listaPacientes = this.nuevosPacientes();

        Mockito.when(paciRepo.findAll()).thenReturn(listaPacientes);
        List<Paciente> resultado = paciServ.getPacientes();

        Mockito.verify(paciRepo).findAll();
        Mockito.verifyNoMoreInteractions(paciRepo);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(2, resultado.size());
        Assertions.assertTrue(resultado.contains(listaPacientes.get(0)));
        Assertions.assertTrue(resultado.contains(listaPacientes.get(1)));
    }

    /**
     * Verifica que {@code findPaciente()} retorne el paciente correcto cuando el id existe
     * y que las llamadas al repositorio sean las esperadas.
     */
    @Test
    public void findPacienteTest() {
        List<Paciente> listaPacientes = this.nuevosPacientes();
        Paciente pacienteEsperado = listaPacientes.get(1);
        Mockito.when(paciRepo.findById(pacienteEsperado.getIdPaciente()))
                .thenReturn(Optional.of(pacienteEsperado));

        Optional<Paciente> pacienteFinal = paciServ.findPaciente(pacienteEsperado.getIdPaciente());
        Mockito.verify(paciRepo).findById(pacienteEsperado.getIdPaciente());
        Mockito.verifyNoMoreInteractions(paciRepo);

        Assertions.assertTrue(pacienteFinal.isPresent());
        Paciente paciente = pacienteFinal.get();
        Assertions.assertEquals(pacienteEsperado.getIdPaciente(), paciente.getIdPaciente() );
        Assertions.assertEquals(pacienteEsperado.getNombre(), paciente.getNombre() );
    }

    /**
     * Verifica que {@code findPacienteByDoc()} retorne el paciente correspondiente
     * al número de documento especificado.
     */
    @Test
    public void findPacienteByDocTest() {
        List<Paciente> listaPacientes = this.nuevosPacientes();
        Paciente pacienteEsperado = listaPacientes.get(1);
        Mockito.when(paciRepo.findByDoc(pacienteEsperado.getNumDocumento()))
                .thenReturn(Optional.of(pacienteEsperado));

        Optional<Paciente> pacienteFinal = paciServ.findPacienteByDoc(pacienteEsperado.getNumDocumento());
        Mockito.verify(paciRepo).findByDoc(pacienteEsperado.getNumDocumento());
        Mockito.verifyNoMoreInteractions(paciRepo);

        Assertions.assertTrue(pacienteFinal.isPresent());
        Paciente paciente = pacienteFinal.get();
        Assertions.assertEquals(pacienteEsperado.getNumDocumento(), paciente.getNumDocumento() );
        Assertions.assertEquals(pacienteEsperado.getNombre(), paciente.getNombre() );
    }

    /**
     * Verifica que {@code deletePaciente()} elimine correctamente un paciente existente
     * cuando el id es válido y que no se produzcan interacciones adicionales.
     */
    @Test
    public void deletePacienteTest() {
        List<Paciente> listaPacientes = this.nuevosPacientes();
        Paciente paciente = listaPacientes.get(1);
        Mockito.when(paciRepo.findById(paciente.getIdPaciente()))
                .thenReturn(Optional.of(paciente));
        paciServ.deletePaciente(paciente.getIdPaciente());
        Mockito.verify(paciRepo).findById(paciente.getIdPaciente());
        Mockito.verify(paciRepo).delete(paciente);
        Mockito.verifyNoMoreInteractions(paciRepo);
    }

    /**
     * Verifica que {@code deletePaciente()} lance una excepción cuando
     * se intenta eliminar un paciente inexistente.
     */
    @Test
    public void deletePacienteNotFoundTest(){
        Long idInexistente = 100L;
        Mockito.when(paciRepo.findById(idInexistente))
                .thenReturn(Optional.empty());

        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> paciServ.deletePaciente(idInexistente)
        );

        Assertions.assertEquals("No se encontró el paciente con id 100", ex.getMessage());
        Mockito.verify(paciRepo).findById(idInexistente);
        Mockito.verifyNoMoreInteractions(paciRepo);
    }


    /**
     * Verifica que {@code editPaciente()} actualice únicamente los campos no nulos y no vacíos
     * del DTO, y que el repositorio guarde el paciente actualizado correctamente.
     */
    @Test
    public void editPacienteTest() {
        List<Paciente> listaPacientes = this.nuevosPacientes();
        Paciente paciente = listaPacientes.get(1);
        PacienteUpdateDTO paciDTO = new PacienteUpdateDTO();
        paciDTO.setApellido("Lopez");
        paciDTO.setTelefono("3214698752");
        Mockito.when(paciRepo.findById(paciente.getIdPaciente()))
                .thenReturn(Optional.of(paciente));
        paciServ.editPaciente(paciente.getIdPaciente(), paciDTO);

        Mockito.verify(paciRepo).findById(paciente.getIdPaciente());
        Mockito.verify(paciRepo).save(paciente);

        Assertions.assertEquals("Lopez", paciente.getApellido());
        Assertions.assertEquals("Samanta", paciente.getNombre());
        Assertions.assertEquals("3214698752", paciente.getTelefono());
        Mockito.verifyNoMoreInteractions(paciRepo);
    }

    /**
     * Verifica que {@code editPaciente()} lance una excepción cuando se intenta
     * actualizar un paciente con un ID inexistente en la base de datos.
     */
    @Test
    public void editPacienteNotFoundTest(){
        Long idInexistente = 100L;
        PacienteUpdateDTO paciDTO = new PacienteUpdateDTO();
        paciDTO.setNombre("Camila");

        Mockito.when(paciRepo.findById(idInexistente))
                .thenReturn(Optional.empty());

        NoSuchElementException ex = Assertions.assertThrows(
                NoSuchElementException.class,
                () -> paciServ.editPaciente(idInexistente, paciDTO)
        );

        Assertions.assertEquals("No se encontró el paciente con id 100", ex.getMessage());
        Mockito.verify(paciRepo).findById(idInexistente);
        Mockito.verifyNoMoreInteractions(paciRepo);
    }

    //_______________________________________________________________________________________
    /**
     * Crea una lista de pacientes de ejemplo para reutilizar en los distintos tests.
     *
     * @return lista con dos instancias de {@link Paciente}.
     */
    public List<Paciente> nuevosPacientes(){
        Paciente p1 = new Paciente(1L, "123456789","Alejandra","Martinez", LocalDate.of(1997,6,24),"3104698520");
        Paciente p2 = new Paciente(2L, "987654321","Samanta","Carreño", LocalDate.of(2000,4,2),"3645897521");

        List<Paciente> listaPacientes = List.of(p1,p2);
        return listaPacientes;
    }

    }
