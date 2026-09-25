package com.ventasropa.service;
import com.ventasropa.dto.PrendaDTO;
import com.ventasropa.model.Prenda;
import java.util.List;

public interface IPrendaService {
    List<Prenda> listarTodas();
    void agregarPrenda(PrendaDTO dto);
    void actualizarPrenda(int id, PrendaDTO dto);
    void eliminarPrenda(int id);
}