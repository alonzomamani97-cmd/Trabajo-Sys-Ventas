package com.ventasropa.service.impl;

import com.ventasropa.dto.ProductoDTO;
import com.ventasropa.exception.RecursoNoEncontradoException;
import com.ventasropa.model.Producto;
import com.ventasropa.repository.IProductoRepository;
import com.ventasropa.service.IProductoService;
import java.util.List;

public class ProductoServiceImpl implements IProductoService {
    private final IProductoRepository repository;

    public ProductoServiceImpl(IProductoRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Producto> listarTodos() {
        return repository.findAll();
    }

    @Override
    public void agregarProducto(ProductoDTO dto) {
        Producto nuevo = new Producto(0, dto.getNombre(), dto.getTalla(), dto.getPrecio(), dto.getStock());
        repository.save(nuevo);
    }

    @Override
    public void actualizarProducto(int id, ProductoDTO dto) {
        if (repository.findById(id) == null) {
            throw new RecursoNoEncontradoException("Producto no encontrado");
        }
        Producto actualizado = new Producto(id, dto.getNombre(), dto.getTalla(), dto.getPrecio(), dto.getStock());
        repository.update(actualizado);
    }

    @Override
    public void eliminarProducto(int id) {
        repository.delete(id);
    }
}