package com.ventasropa.repository;

import com.ventasropa.model.Producto;
import java.util.List;

public interface IProductoRepository {
    List<Producto> findAll();
    Producto findById(int id);
    void save(Producto producto); // abtraccion
    void update(Producto producto);
    void delete(int id);
}