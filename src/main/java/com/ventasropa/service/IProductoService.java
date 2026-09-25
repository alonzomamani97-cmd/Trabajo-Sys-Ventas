package com.ventasropa.service;

import com.ventasropa.dto.ProductoDTO;
import com.ventasropa.model.Producto;
import java.util.List;

public interface IProductoService {
    List<Producto> listarTodos();
    void agregarProducto(ProductoDTO dto);
    void actualizarProducto(int id, ProductoDTO dto);
    void eliminarProducto(int id);
}