package com.ventasropa.repository.impl;

import com.ventasropa.enums.Talla;
import com.ventasropa.model.Prenda;
import com.ventasropa.repository.IPrendaRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PrendaRepositoryImpl implements IPrendaRepository {
    private final List<Prenda> prendas = new ArrayList<>();
    private int contadorId = 1;
    private final String ARCHIVO_DATOS = "prendas.txt";

    public PrendaRepositoryImpl() {
        cargarDatosDeArchivo();
    }

    @Override
    public List<Prenda> findAll() {
        return prendas;
    }

    @Override
    public Prenda findById(int id) {
        return prendas.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void save(Prenda prenda) {
        prenda.setId(contadorId++);
        prendas.add(prenda);
        guardarDatosEnArchivo();
    }

    @Override
    public void update(Prenda prenda) {
        Prenda existente = findById(prenda.getId());
        if (existente != null) {
            existente.setNombre(prenda.getNombre());
            existente.setTalla(prenda.getTalla());
            existente.setPrecio(prenda.getPrecio());
            existente.setStock(prenda.getStock());
            guardarDatosEnArchivo();
        }
    }

    @Override
    public void delete(int id) {
        prendas.removeIf(p -> p.getId() == id);
        guardarDatosEnArchivo();
    }

    // --- MÉTODOS PARA GUARDAR Y LEER EL ARCHIVO ---

    private void guardarDatosEnArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_DATOS))) {
            for (Prenda p : prendas) {
                // Formato: id,nombre,talla,precio,stock
                writer.write(p.getId() + "," + p.getNombre() + "," + p.getTalla() + "," + p.getPrecio() + "," + p.getStock());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar los datos: " + e.getMessage());
        }
    }

    private void cargarDatosDeArchivo() {
        File archivo = new File(ARCHIVO_DATOS);
        if (!archivo.exists()) return; // Si no existe, no hace nada y el ArrayList inicia vacío

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

                    prendas.add(new Prenda(id, nombre, talla, precio, stock));
                    if (id > maxId) {
                        maxId = id;
                    }
                }
            }
            contadorId = maxId + 1; // Para que el próximo ID no se repita
        } catch (Exception e) {
            System.err.println("Error al cargar los datos: " + e.getMessage());
        }
    }
}