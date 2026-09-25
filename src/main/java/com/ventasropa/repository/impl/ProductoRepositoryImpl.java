package com.ventasropa.repository.impl;

import com.ventasropa.enums.Talla;
import com.ventasropa.model.Producto;
import com.ventasropa.repository.IProductoRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepositoryImpl implements IProductoRepository {
    private final List<Producto> productos = new ArrayList<>();
    private int contadorId = 1;
    private final String ARCHIVO_DATOS = "productos.txt";

    public ProductoRepositoryImpl() {
        cargarDatosDeArchivo();
    }

    @Override
    public List<Producto> findAll() {
        return productos;
    }

    @Override
    public Producto findById(int id) {
        return productos.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void save(Producto producto) {
        producto.setId(contadorId++);
        productos.add(producto);
        guardarDatosEnArchivo();
    }

    @Override
    public void update(Producto producto) {
        Producto existente = findById(producto.getId());
        if (existente != null) {
            existente.setNombre(producto.getNombre());
            existente.setTalla(producto.getTalla());
            existente.setPrecio(producto.getPrecio());
            existente.setStock(producto.getStock());
            guardarDatosEnArchivo();
        }
    }

    @Override
    public void delete(int id) {
        productos.removeIf(p -> p.getId() == id);
        guardarDatosEnArchivo();
    }

    private void guardarDatosEnArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_DATOS))) {
            for (Producto p : productos) {
                writer.write(p.getId() + "," + p.getNombre() + "," + p.getTalla() + "," + p.getPrecio() + "," + p.getStock());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }
    }

    private void cargarDatosDeArchivo() {
        File archivo = new File(ARCHIVO_DATOS);
        if (!archivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            int maxId = 0;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos.length == 5) {
                    int id = Integer.parseInt(datos[0]);
                    String nombre = datos[1];
                    Talla talla = Talla.valueOf(datos[2]);
                    double precio = Double.parseDouble(datos[3]);
                    int stock = Integer.parseInt(datos[4]);

                    productos.add(new Producto(id, nombre, talla, precio, stock));
                    if (id > maxId) maxId = id;
                }
            }
            contadorId = maxId + 1;
        } catch (Exception e) {
            System.err.println("Error al cargar: " + e.getMessage());
        }
    }
}