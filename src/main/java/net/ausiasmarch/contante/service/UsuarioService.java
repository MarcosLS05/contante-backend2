package net.ausiasmarch.contante.service;

import java.util.Optional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.ausiasmarch.contante.entity.UsuarioEntity;
import net.ausiasmarch.contante.exception.ResourceNotFoundException;
import net.ausiasmarch.contante.repository.UsuarioRepository;

@Transactional
@Service
public class UsuarioService implements ServiceInterface<UsuarioEntity> {

    @Autowired
    UsuarioRepository oUsuarioRepository;

    @Autowired
    RandomService oRandomService;

    private String[] arrNombres = {"Pepe", "Laura", "Ignacio", "Maria", "Lorenzo", "Carmen", "Rosa", "Paco", "Luis",
        "Ana", "Rafa", "Manolo", "Lucia", "Marta", "Sara", "Rocio"};

    private String[] arrApellidos = {"Sancho", "Gomez", "Pérez", "Rodriguez", "Garcia", "Fernandez", "Lopez",
        "Martinez", "Sanchez", "Gonzalez", "Gimenez", "Feliu", "Gonzalez", "Hermoso", "Vidal", "Escriche"};

    public Long randomCreate(Long cantidad) {
        for (int i = 0; i < cantidad; i++) {
            UsuarioEntity oUsuarioEntity = new UsuarioEntity();
            oUsuarioEntity.setNombre(arrNombres[oRandomService.getRandomInt(0, arrNombres.length - 1)]);
            oUsuarioEntity.setApellido1(arrApellidos[oRandomService.getRandomInt(0, arrApellidos.length - 1)]);
            oUsuarioEntity.setApellido2(arrApellidos[oRandomService.getRandomInt(0, arrApellidos.length - 1)]);
            oUsuarioEntity.setEmail("email" + oUsuarioEntity.getNombre() + oRandomService.getRandomInt(999, 9999) + "@gmail.com");
            oUsuarioRepository.save(oUsuarioEntity);
        }
        return oUsuarioRepository.count();
    }

    public Page<UsuarioEntity> getPage(Pageable oPageable, Optional<String> filter) {
        if (filter.isPresent()) {
            return oUsuarioRepository
                    .findByNombreContainingOrApellido1ContainingOrApellido2ContainingOrEmailContaining(
                            filter.get(), filter.get(), filter.get(), filter.get(),
                            oPageable);
        } else {
            return oUsuarioRepository.findAll(oPageable);
        }
    }

    public UsuarioEntity get(Long id) {
        return oUsuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    public Long count() {
        return oUsuarioRepository.count();
    }

    
    public Long delete(Long id) {
        oUsuarioRepository.deleteById(id);
        return 1L;
    }

    public UsuarioEntity create(UsuarioEntity oUsuarioEntity) {
        return oUsuarioRepository.save(oUsuarioEntity);
    }

    public UsuarioEntity update(UsuarioEntity oUsuarioEntity) {
        UsuarioEntity oUsuarioEntityFromDatabase = oUsuarioRepository.findById(oUsuarioEntity.getId()).get();
        if (oUsuarioEntity.getNombre() != null) {
            oUsuarioEntityFromDatabase.setNombre(oUsuarioEntity.getNombre());
        }
        if (oUsuarioEntity.getApellido1() != null) {
            oUsuarioEntityFromDatabase.setApellido1(oUsuarioEntity.getApellido1());
        }
        if (oUsuarioEntity.getApellido2() != null) {
            oUsuarioEntityFromDatabase.setApellido2(oUsuarioEntity.getApellido2());
        }
        if (oUsuarioEntity.getEmail() != null) {
            oUsuarioEntityFromDatabase.setEmail(oUsuarioEntity.getEmail());
        }
        return oUsuarioRepository.save(oUsuarioEntityFromDatabase);
    }

    public Long deleteAll() {
        oUsuarioRepository.deleteAll();
        return this.count();
    }

    public Long deleteEvenIds() {
        // Buscar los IDs pares
        List<Long> evenIds = oUsuarioRepository.findEvenIds();
    
        // Si hay IDs pares, eliminarlos
        if (!evenIds.isEmpty()) {
            oUsuarioRepository.deleteByIdIn(evenIds);
            return (long) evenIds.size(); // Devolver la cantidad eliminada
        }
        return 0L; // Si no hay registros pares, devolver 0
    }
}
