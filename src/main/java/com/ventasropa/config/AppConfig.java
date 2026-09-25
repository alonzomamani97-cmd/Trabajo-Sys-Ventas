package com.ventasropa.config;

import com.ventasropa.repository.impl.ProductoRepositoryImpl;
import com.ventasropa.service.IProductoService;
import com.ventasropa.service.impl.ProductoServiceImpl;

public class AppConfig {
    public static final IProductoService productoService = new ProductoServiceImpl(new ProductoRepositoryImpl());
}