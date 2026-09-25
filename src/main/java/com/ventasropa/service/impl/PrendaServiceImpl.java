package com.ventasropa.service.impl;

import com.ventasropa.dto.PrendaDTO;
import com.ventasropa.exception.RecursoNoEncontradoException;
import com.ventasropa.model.Prenda;
import com.ventasropa.repository.IPrendaRepository;
import com.ventasropa.service.IPrendaService;
import java.util.List;

public class PrendaServiceImpl implements IPrendaService {
    private final IPrendaRepository repository;

    public PrendaServiceImpl(IPrendaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Prenda> listarTodas() {
        return repository.findAll();
    }

    @Override
    public void agregarPrenda(PrendaDTO dto) {
        // Se agregó dto.getStock() al final
        Prenda nueva = new Prenda(0, dto.getNombre(), dto.getTalla(), dto.getPrecio(), dto.getStock());
        repository.save(nueva);
    }

    @Override
    public void actualizarPrenda(int id, PrendaDTO dto) {
        if (repository.findById(id) == null) {
            throw new RecursoNoEncontradoException("Prenda no encontrada");
        }
        // Se agregó dto.getStock() al final
        Prenda actualizada = new Prenda(id, dto.getNombre(), dto.getTalla(), dto.getPrecio(), dto.getStock());
        repository.update(actualizada);
    }

    @Override
    public void eliminarPrenda(int id) {
        repository.delete(id);
    }
}