package com.proyecto.pacientes.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.proyecto.pacientes.model.Paciente;
import com.proyecto.pacientes.service.IPacienteService;

/**
 * Repositorio de acceso a datos para la entidad {@link Paciente}.
 *
 * <p>Extiende de {@link JpaRepository} para proporcionar las operaciones
 * CRUD básicas y el manejo de persistencia de la entidad {@code Paciente}.
 * Permite la integración directa con la base de datos sin necesidad de 
 * implementar código adicional.</p>
 * 
 * <p>Además, define una consulta personalizada mediante {@link Query}
 * para buscar un paciente por su número de documento.</p>
 *
 * <p>Forma parte de la capa de persistencia dentro de la arquitectura
 * multicapa, siendo utilizada por el servicio {@link IPacienteService}
 * para interactuar con los datos.</p>
 *
 * @see Paciente
 * @see IPacienteService
 */
@Repository
public interface IPacienteRepository extends JpaRepository<Paciente, Long> {


    /**
     * Busca un paciente por su número de documento.
     *
     * <p>Utiliza una consulta JPQL para seleccionar el registro correspondiente
     * en la base de datos. Si no se encuentra ningún resultado, retorna un
     * {@link Optional} vacío.</p>
     *
     * @param numDocumento número de documento del paciente a buscar.
     * @return un {@link Optional} que contiene el paciente encontrado, o vacío si no existe.
     */
    @Query("SELECT paci FROM Paciente paci WHERE paci.numDocumento = :numDocumento")
    Optional<Paciente>findByDoc(String numDocumento);

}
