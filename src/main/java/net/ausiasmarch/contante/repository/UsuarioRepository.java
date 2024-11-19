package net.ausiasmarch.contante.repository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import net.ausiasmarch.contante.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.Query;
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    // Ya existente
    Page<UsuarioEntity> findByNombreContainingOrApellido1ContainingOrApellido2ContainingOrEmailContaining(
            String filter2, String filter3, String filter4, String filter5, Pageable oPageable);

    // Método para obtener los IDs pares
    @Query("SELECT u.id FROM UsuarioEntity u WHERE MOD(u.id, 2) = 0")
    List<Long> findEvenIds();

    // Método para eliminar registros por una lista de IDs
    void deleteByIdIn(List<Long> ids);
}
