package com.proyecto.turnos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.turnos.model.Turno;
import com.proyecto.turnos.service.ITurnoService;

/**
 * Repositorio de acceso a datos para la entidad {@link Turno}.
 *
 * <p>Extiende de {@link JpaRepository} para proporcionar las operaciones
 * CRUD básicas y el manejo de persistencia de la entidad {@code Turno}.
 * Permite la integración directa con la base de datos sin necesidad de 
 * implementar código adicional.</p>
 * 
 * <p>Forma parte de la capa de persistencia dentro de la arquitectura
 * multicapa, siendo utilizada por el servicio {@link ITurnoService}
 * para interactuar con los datos.</p>
 *
 * @see Turno
 * @see ITurnoService
 */
@Repository
public interface ITurnoRepository extends JpaRepository <Turno, Long>{

}
