package com.ventasropa.repository;
import com.ventasropa.model.Prenda;
import java.util.List;

public interface IPrendaRepository {
    List<Prenda> findAll();
    Prenda findById(int id);
    void save(Prenda prenda);
    void update(Prenda prenda);
    void delete(int id);
}