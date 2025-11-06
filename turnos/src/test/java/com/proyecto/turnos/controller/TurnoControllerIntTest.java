package com.proyecto.turnos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.turnos.dto.TurnoSaveDTO;
import com.proyecto.turnos.dto.TurnoUpdateDTO;
import com.proyecto.turnos.model.Paciente;
import com.proyecto.turnos.model.Turno;
import com.proyecto.turnos.repository.IPacientesAPI;
import com.proyecto.turnos.repository.ITurnoRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
* Pruebas de integración para el controlador de turnos.
*
* <p>Esta clase valida el comportamiento de los endpoints REST del {@link TurnoController},
* verificando las respuestas HTTP y la persistencia en la base de datos H2 configurada para pruebas.</p>
*
* <p>Configuración principal:</p>
* <ul>
*   <li>{@link SpringBootTest}: levanta el contexto completo de la aplicación.</li>
*   <li>{@link AutoConfigureMockMvc}: permite realizar solicitudes HTTP simuladas.</li>
*   <li>{@link AutoConfigureTestDatabase}: crea una base de datos H2 en memoria.</li>
*   <li>{@link ActiveProfiles}("test"): usa el perfil de pruebas.</li>
* </ul>
*/
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class TurnoControllerIntTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ITurnoRepository turnoRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IPacientesAPI pacienteAPI;

    /**
     * Limpia la base de datos antes de cada prueba.
     * <p>
     * Esto garantiza un entorno aislado y reproducible entre ejecuciones.
     * </p>
     */
    @BeforeEach
    public void limpiar(){
        turnoRepo.deleteAll();;
    }

    /**
     * Verifica la creación de un nuevo turno mediante el endpoint POST /turnos/crear.
     * <p>
     * <b>Arrange:</b> Se construye un objeto turno con datos válidos.<br>
     * <b>Act:</b> Se ejecuta la petición POST enviando el JSON del turno.<br>
     * <b>Assert:</b> Se valida que el estado HTTP sea 201, el mensaje sea el esperado,
     *  y que el turno se haya guardado correctamente en la base de datos.
     * </p>
     */
    @Test
    public void saveTurnoTest() throws Exception {
        TurnoSaveDTO nuevoTurno = new TurnoSaveDTO(LocalDate.of(2025,8,24), "Medicina General","123456789");
        Paciente nuevoPaci = new Paciente(null, "123456789","Alejandra","Martinez", LocalDate.of(1997,6,24),"3104698520");
        Mockito.when(pacienteAPI.getPacienteByDocumento("123456789"))
                .thenReturn(nuevoPaci);
        MvcResult mvcResult = mvc.perform(
                        post("/turnos/crear")
                                .contentType(MediaType.APPLICATION_JSON)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .content(objectMapper.writeValueAsString(nuevoTurno)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Turno creado exitosamente"))
                .andReturn();
        Turno turnoBD = turnoRepo.findAll().stream()
                .filter(t -> LocalDate.of(2025,8,24).equals(t.getFecha()))
                .filter(t -> "Medicina General".equals(t.getTratamiento()))
                .filter(t -> "Alejandra Martinez".equals(t.getNombrePaciente()))
                .findFirst()
                .orElseThrow(()-> new AssertionError("No se encontró el turno persistido en la BD"));
        Assertions.assertEquals("Alejandra Martinez", turnoBD.getNombrePaciente());
    }

    /**
     * Verifica que el endpoint GET /turnos/traer retorne correctamente una lista de turnos.
     */
    @Test
    public void traerTurnosTest() throws Exception {
        List<Turno> listaTurnos = this.nuevosTurnos();
        turnoRepo.saveAll(listaTurnos);
        mvc.perform(get("/turnos/traer")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(listaTurnos.size())) // Verifica que tenga el mismo tamaño
                .andExpect(jsonPath("$[0].nombrePaciente").value(listaTurnos.get(0).getNombrePaciente()))
                .andExpect(jsonPath("$[1].nombrePaciente").value(listaTurnos.get(1).getNombrePaciente()));
    }

    /**
     * Verifica el comportamiento del endpoint GET /turnos/traer cuando no existen registros.
     */
    @Test
    public void traerTurnosVacioTest() throws Exception {
        mvc.perform(get("/turnos/traer")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("No se encontraron registros"));
    }

    /**
     * Valida que el endpoint GET /turnos/traer/{id} retorne un turno existente por ID.
     */
    @Test
    public void findTurnoTest() throws Exception {
        List<Turno> listaTurnos = this.nuevosTurnos();
        turnoRepo.saveAll(listaTurnos);
        Turno turno = listaTurnos.get(1);
        mvc.perform(get("/turnos/traer/{id}",turno.getIdTurno())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idTurno").value(turno.getIdTurno()))
                .andExpect(jsonPath("$.fecha").value(turno.getFecha().toString()))
                .andExpect(jsonPath("$.tratamiento").value(turno.getTratamiento()))
                .andExpect(jsonPath("$.nombrePaciente").value(turno.getNombrePaciente()));
    }

    /**
     * Verifica que el endpoint GET /turnos/traer/{id} devuelva 404 cuando el turno no existe.
     */
    @Test
    public void findTurnoInexistenteTest() throws Exception{
        this.nuevosTurnos();
        Long idInexistente = 100L;
        mvc.perform(get("/turnos/traer/{id}",idInexistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontró el turno con Id "+idInexistente));
    }


    /**
     * Verifica la eliminación de un turno existente mediante el endpoint DELETE /turnos/eliminar/{id}.
     */
    @Test
    public void deleteTurnoTest() throws Exception {
        List<Turno> listaTurnos = this.nuevosTurnos();
        turnoRepo.saveAll(listaTurnos);
        Turno turno = listaTurnos.get(1);
        mvc.perform(delete("/turnos/eliminar/{id}",turno.getIdTurno())
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("Turno eliminado correctamente"));
        boolean existe = turnoRepo.findById(turno.getIdTurno()).isPresent();
        Assertions.assertFalse(existe, "El turno no fue eliminado de la BD");
    }

    /**
     * Verifica la edición de un turno existente mediante el endpoint PUT /turnos/editar/{id}.
     * <p>
     * Comprueba que los campos sean actualizados correctamente en la base de datos.
     * </p>
     */
    @Test
    public void editTurnoTest() throws Exception {
        List<Turno> listaTurnos = this.nuevosTurnos();
        turnoRepo.saveAll(listaTurnos);
        Turno turno = listaTurnos.get(1);
        TurnoUpdateDTO turnoDTO = new TurnoUpdateDTO();
        turnoDTO.setFecha(LocalDate.of(2025,11,1));
        turnoDTO.setTratamiento("Medicina Interna");
        mvc.perform(put("/turnos/editar/{id}",turno.getIdTurno())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(turnoDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Turno con Id "+turno.getIdTurno() + " actualizado correctamente"));
        Turno turnoActualizado = turnoRepo.findById(turno.getIdTurno())
                .orElseThrow(() -> new AssertionError("El turno no se encontró después de actualizar"));
        Assertions.assertEquals(LocalDate.of(2025,11,1), turnoActualizado.getFecha());
        Assertions.assertEquals("Medicina Interna", turnoActualizado.getTratamiento());
    }

    //_______________________________________________________________________________________
    /**
     * Crea una lista de turnos de ejemplo para reutilizar en los distintos tests.
     *
     * @return lista con dos instancias de {@link Turno}.
     */
    public List<Turno> nuevosTurnos(){
        Turno t1 = new Turno(null,LocalDate.of(2025,8,24), "Medicina General","Alejandra Ramos");
        Turno t2 = new Turno(null,LocalDate.of(2025,10,15), "Medicina General","Camila Torres");

        List<Turno> listaTurnos = List.of(t1,t2);
        return listaTurnos;
    }

}



