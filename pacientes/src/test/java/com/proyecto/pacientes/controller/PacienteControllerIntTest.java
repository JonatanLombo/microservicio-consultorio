package com.proyecto.pacientes.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.pacientes.dto.PacienteUpdateDTO;
import com.proyecto.pacientes.model.Paciente;
import com.proyecto.pacientes.repository.IPacienteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración para el controlador de pacientes.
 * <p>
 * Esta clase valida el comportamiento de los endpoints REST del {@link PacienteController},
 * verificando tanto las respuestas HTTP como los efectos sobre la base de datos en memoria.
 * </p>
 *
 * <p>Configuración principal:</p>
 * <ul>
 *   <li>{@link SpringBootTest}: levanta el contexto completo de la aplicación.</li>
 *   <li>{@link AutoConfigureMockMvc}: permite realizar solicitudes HTTP simuladas con {@link MockMvc}.</li>
 *   <li>{@link AutoConfigureTestDatabase}: configura una base de datos H2 en memoria para los tests.</li>
 *   <li>{@link ActiveProfiles}("test"): usa el perfil de propiedades destinado a las pruebas.</li>
 * </ul>
 *
 * <p>Las pruebas cubren los escenarios principales de CRUD: creación, obtención, edición y eliminación.</p>
 */
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class PacienteControllerIntTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private IPacienteRepository paciRepo;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Limpia la base de datos antes de cada prueba.
     * <p>
     * Esto garantiza un entorno aislado y reproducible entre ejecuciones.
     * </p>
     */
    @BeforeEach
    public void limpiar(){
        paciRepo.deleteAll();;
    }

    /**
     * Verifica la creación de un nuevo paciente mediante el endpoint POST /pacientes/crear.
     * <p>
     * <b>Arrange:</b> Se construye un objeto Paciente con datos válidos.<br>
     * <b>Act:</b> Se ejecuta la petición POST enviando el JSON del paciente.<br>
     * <b>Assert:</b> Se valida que el estado HTTP sea 201 y que el paciente se haya persistido correctamente.
     * </p>
     */
    @Test
    public void savePacienteTest() throws Exception {
        Paciente nuevoPaci = new Paciente(null, "123456789","Alejandra","Martinez", LocalDate.of(1997,6,24),"3104698520");
        MvcResult mvcResult = mvc.perform(
                post("/pacientes/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(nuevoPaci))
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Paciente creado exitosamente"))
                .andReturn();
        Paciente paciBD = paciRepo.findAll().stream()
                .filter(p -> "123456789".equals(p.getNumDocumento()))
                .filter(p -> "Alejandra".equals(p.getNombre()))
                .filter(p -> LocalDate.of(1997,6,24).equals(p.getFechaNac()))
                .filter(p -> "3104698520".equals(p.getTelefono()))
                .findFirst()
                .orElseThrow(()-> new AssertionError("No se encontró al paciente relacionado"));
    }

    /**
     * Verifica que el endpoint GET /pacientes/traer retorne correctamente una lista de pacientes.
     */
    @Test
    public void traerPacientesTest() throws Exception {
        List<Paciente> listaPacientes = this.nuevosPacientes();
        paciRepo.saveAll(listaPacientes);
        mvc.perform(get("/pacientes/traer")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(listaPacientes.size())) // Verifica que tenga el mismo tamaño
                .andExpect(jsonPath("$[0].nombre").value(listaPacientes.get(0).getNombre()))
                .andExpect(jsonPath("$[1].nombre").value(listaPacientes.get(1).getNombre()));
    }

    /**
     * Verifica el comportamiento del endpoint GET /pacientes/traer cuando no existen registros.
     */
    @Test
    public void traerPacientesVacioTest() throws Exception {
        mvc.perform(get("/pacientes/traer")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("No se encontraron registros"));
    }

    /**
     * Valida que el endpoint GET /pacientes/traer/{id} retorne un paciente existente por ID.
     */
    @Test
    public void findPacinteTest() throws Exception {
        List<Paciente> listaPacientes = this.nuevosPacientes();
        paciRepo.saveAll(listaPacientes);
        Paciente paciente = listaPacientes.get(1);
        mvc.perform(get("/pacientes/traer/{id}",paciente.getIdPaciente())
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idPaciente").value(paciente.getIdPaciente()))
                .andExpect(jsonPath("$.nombre").value(paciente.getNombre()))
                .andExpect(jsonPath("$.apellido").value(paciente.getApellido()))
                .andExpect(jsonPath("$.numDocumento").value(paciente.getNumDocumento()))
                .andExpect(jsonPath("$.telefono").value(paciente.getTelefono()));
    }

    /**
     * Verifica que el endpoint GET /pacientes/traer/{id} devuelva 404 cuando el paciente no existe.
     */
    @Test
    public void findPacienteInexistenteTest() throws Exception{
        this.nuevosPacientes();
        Long idInexistente = 100L;
        mvc.perform(get("/pacientes/traer/{id}",idInexistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontró el paciente con Id "+idInexistente));
    }

    /**
     * Verifica la búsqueda de un paciente existente por número de documento.
     */
    @Test
    public void findPacintByDocTest() throws Exception {
        List<Paciente> listaPacientes = this.nuevosPacientes();
        paciRepo.saveAll(listaPacientes);
        Paciente paciente = listaPacientes.get(1);
        mvc.perform(get("/pacientes/traer/documento/{numDocumento}",paciente.getNumDocumento())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idPaciente").value(paciente.getIdPaciente()))
                .andExpect(jsonPath("$.nombre").value(paciente.getNombre()))
                .andExpect(jsonPath("$.apellido").value(paciente.getApellido()))
                .andExpect(jsonPath("$.numDocumento").value(paciente.getNumDocumento()))
                .andExpect(jsonPath("$.telefono").value(paciente.getTelefono()));
    }

    /**
     * Verifica que la búsqueda por documento devuelva 404 si el documento no existe.
     */
    @Test
    public void findPacienteByDocInexistenteTest() throws Exception{
        this.nuevosPacientes();
        String docInexistente = "11111111111";
        mvc.perform(get("/pacientes/traer/documento/{numDocumento}",docInexistente)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No se encontró el paciente con documento "+docInexistente));
    }

    /**
     * Verifica la eliminación de un paciente existente mediante el endpoint DELETE /pacientes/eliminar/{id}.
     */
    @Test
    public void deletePacienteTest() throws Exception {
        List<Paciente> listaPacientes = this.nuevosPacientes();
        paciRepo.saveAll(listaPacientes);
        Paciente paciente = listaPacientes.get(1);
        mvc.perform(delete("/pacientes/eliminar/{id}",paciente.getIdPaciente())
                    .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("Paciente eliminado correctamente"));
        boolean existe = paciRepo.findById(paciente.getIdPaciente()).isPresent();
        Assertions.assertFalse(existe, "El paciente no fue eliminado de la BD");
    }

    /**
     * Verifica la edición de un paciente existente mediante el endpoint PUT /pacientes/editar/{id}.
     * <p>
     * Comprueba que los campos sean actualizados correctamente en la base de datos.
     * </p>
     */
    @Test
    public void editPacienteTest() throws Exception {
        List<Paciente> listaPacientes = this.nuevosPacientes();
        paciRepo.saveAll(listaPacientes);
        Paciente paciente = listaPacientes.get(1);
        PacienteUpdateDTO paciDTO = new PacienteUpdateDTO();
        paciDTO.setNombre("Alejandra");
        paciDTO.setApellido("Ramos");
        paciDTO.setTelefono("320145896185");
        mvc.perform(put("/pacientes/editar/{id}",paciente.getIdPaciente())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paciDTO)))
            .andExpect(status().isOk())
            .andExpect(content().string("Paciente con Id "+paciente.getIdPaciente() + " actualizado correctamente"));
        Paciente paciActualizado = paciRepo.findById(paciente.getIdPaciente())
                .orElseThrow(() -> new AssertionError("El paciente no se encontró después de actualizar"));
        Assertions.assertEquals("Alejandra", paciActualizado.getNombre());
        Assertions.assertEquals("Ramos", paciActualizado.getApellido());
        Assertions.assertEquals("320145896185", paciActualizado.getTelefono());

    }

    //_______________________________________________________________________________________
    /**
     * Crea una lista de pacientes de ejemplo para reutilizar en los distintos tests.
     *
     * @return lista con dos instancias de {@link Paciente}.
     */
    public List<Paciente> nuevosPacientes(){
        Paciente p1 = new Paciente(null, "123456789","Alejandra","Martinez", LocalDate.of(1997,6,24),"3104698520");
        Paciente p2 = new Paciente(null, "987456321","Samanta","Carreño", LocalDate.of(2000,4,2),"3645897521");

        List<Paciente> listaPacientes = List.of(p1,p2);
        return listaPacientes;
    }

}
